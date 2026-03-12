# vLLM Compilation 机制深度解析

> 本文整理自对 vLLM `compilation/decorators.py` 及相关模块的源码分析。

---

## 1. `support_torch_compile` 装饰器详解

### 1.1 整体架构：装饰器做了什么

```python
@support_torch_compile(dynamic_arg_dims={"input_ids": 0})
class MyModel(nn.Module):
    def forward(self, input_ids, ...): ...
```

经过装饰后，`MyModel` 的类结构被**运行时动态修改**：

```
原始:   MyModel(nn.Module)
修改后: MyModel(nn.Module, TorchCompileWithNoGuardsWrapper)
         ├── __init__ 被替换 → 注入编译状态
         └── __call__ 被替换 → 控制编译时机和执行路径
```

装饰器本身（`decorators.py:115-237`）只做**参数推断和验证**，真正的类改造在 `_support_torch_compile`（`decorators.py:314-641`）中完成。

---

### 1.2 Dynamic Shapes（动态形状标记）原理

#### 背景问题

`torch.compile` 默认对**具体的 tensor 形状进行特化**（specialization）：第一次 forward 用 `[batch=4, seq=128]` 的输入，Dynamo 就把这个形状"烧进"了编译产物，下次如果 batch 或 seq 变了，就会重新编译。这对推理完全不可接受。

#### vLLM 的解决方案

**第一步：标记动态维度**（`decorators.py:381-432`）

```python
# 装饰器时声明哪些维度是动态的
@support_torch_compile(dynamic_arg_dims={"input_ids": 0, "positions": 0})
```

在**第一次** `__call__` 时，`_mark_dynamic_inputs` 会在实际 tensor 上调用：

```python
torch._dynamo.mark_dynamic(tensor, dims)  # BACKED 模式
# 或
torch._dynamo.decorators.mark_unbacked(tensor, dim)  # UNBACKED 模式
```

两者的区别：

| 类型 | 含义 | 适用场景 |
|---|---|---|
| `mark_dynamic` | 维度有具体值作为 hint，但被视为符号变量 | 通常 decode 场景 |
| `mark_unbacked` | 维度没有具体 hint，Dynamo 不会特化 0/1 | 视觉模型 prefill，防止对虚拟输入特化 |

**第二步：推断 dynamic_arg_dims**（`decorators.py:196-211`）

如果用户没有显式传 `dynamic_arg_dims`，装饰器会通过**类型注解**自动推断：

```python
# forward(self, x: torch.Tensor, y: Optional[torch.Tensor])
# → 自动推断 {"x": 0, "y": 0}
```

---

### 1.3 "No Guards" 优化原理

这是 vLLM 与标准 `torch.compile` 最大的区别之一。

#### 标准 torch.compile 的 Guard 机制

Dynamo 在编译时生成大量**守卫条件**（Guards），例如：
```python
# 运行时检查
assert input.shape[0] == 4  # shape guard
assert input.dtype == torch.float16  # dtype guard
```

每次调用都要检查这些 guards，不满足就**重新编译**。

#### vLLM 的处理（`wrapper.py:146-158`）

```python
# 丢弃所有 guards
options["guard_filter_fn"] = lambda x: [False for _ in x]
```

vLLM 通过 `guard_filter_fn` 直接过滤掉所有 guards，只保留 `SHAPE_ENV` 类型的符号形状约束（当 `evaluate_guards=True` 时）。

**为什么可以这样做？**
- vLLM 对输入做了 **padding**，保证 batch size 落在预定义的 bucket 内
- 推理阶段模型权重不变，dtype/device 不变
- 因此"编译一次，永久有效"

---

### 1.4 `__call__` 的执行流程

`decorators.py:434-604` 中的 `__call__` 是核心调度逻辑：

```
第一次调用
  ├── 标记动态维度 (_mark_dynamic_inputs)
  ├── 收集 traced_files（用于缓存失效检测）
  └── 触发编译
        ├── AOT 模式 → aot_compile() → 保存产物到磁盘
        └── JIT 模式 → TorchCompileWithNoGuardsWrapper.__call__()

后续调用
  ├── 已有 aot_compiled_fn → 直接用 partition wrapper 执行
  └── 已 compiled → TorchCompileWithNoGuardsWrapper.__call__()
```

关键判断点：

```python
# 在编译内部（如 TPU 上层 runner 负责编译），直接 forward 跳过
if self.do_not_compile or torch.compiler.is_compiling():
    return self.forward(*args, **kwargs)

# enc-dec 模型形状多变，跳过编译图
if get_forward_context().skip_compiled:
    return self.forward(*args, **kwargs)
```

---

### 1.5 CUDA Graph 原理与 vLLM 的 Piecewise 实现

#### CUDA Graph 基本原理

```
普通执行：CPU 逐个提交 CUDA kernel
  CPU → [kernel1] → [kernel2] → [kernel3] → ...
  每次提交有 CPU overhead（~微秒级，但累积显著）

CUDA Graph 执行：
  录制阶段：记录所有 kernel 及其依赖关系 → 形成 Graph
  回放阶段：一次性提交整个 Graph
  CPU → [GRAPH: kernel1+kernel2+kernel3] → 完成
```

**优势**：消除 kernel launch overhead，GPU 调度更高效，内存访问模式更规律。

**限制**：Graph 录制时，tensor 的**地址必须固定**（静态内存）；Graph 回放时，如果地址变了，结果就错了。

#### vLLM 的 Piecewise CUDA Graph

LLM 推理中有些操作**不能被 CUDA Graph 捕获**（如 sampling、涉及 data-dependent 控制流的 all-reduce 等），因此 vLLM 采用**分段（Piecewise）策略**：

```
完整计算图（FX Graph）
    ↓ Inductor 分析
 ┌──────────┬──────────┬──────────┐
 │ 可捕获段 │ 不可捕获 │ 可捕获段 │
 │ (CUDA    │ (eager)  │ (CUDA    │
 │  Graph)  │          │  Graph)  │
 └──────────┴──────────┴──────────┘
```

#### `maybe_use_cudagraph_partition_wrapper` 的作用

`decorators.py:644-700` 中的 context manager 是将 vLLM 自定义的 CUDA Graph wrapper 注入 Inductor 分区流程的关键：

```python
def customized_cudagraph_wrapper(f, metadata):
    partition_id = metadata.partition_index
    num_partitions = metadata.num_partitions
    return static_graph_wrapper_class(
        runnable=f,
        runtime_mode=CUDAGraphMode.PIECEWISE,
        cudagraph_options=CUDAGraphOptions(
            debug_log_enable=partition_id == 0,      # 只有第一段打 log
            gc_disable=partition_id != 0,            # 非首段禁用 GC（防止打断图录制）
            weak_ref_output=partition_id == num_partitions - 1,  # 最后段用弱引用（省内存）
        ),
    )
torch._inductor.utils.set_customized_partition_wrappers(customized_cudagraph_wrapper)
```

**为什么这个 context 需要在多处激活？**

```python
# 场景1：AOT 加载时
with maybe_use_cudagraph_partition_wrapper(model.vllm_config):
    loaded_fn.finalize_loading(...)   # ← 加载时需要 wrapper 存在

# 场景2：运行时有 aot_compiled_fn
with maybe_use_cudagraph_partition_wrapper(self.vllm_config):
    return self.aot_compiled_fn(self, *args, **kwargs)  # ← 回放时 wrapper 必须激活

# 场景3：首次编译
with maybe_use_cudagraph_partition_wrapper(self.vllm_config):
    self.aot_compiled_fn = self.aot_compile(...)  # ← 编译时注入分区 wrapper
```

**根本原因**：CUDA Graph 的**捕获 (capture) 和回放 (replay) 必须在相同的 wrapper 上下文中进行**，否则 tensor 地址映射会出错，导致 silent 错误（数据损坏而不是崩溃）。

---

### 1.6 AOT 编译与缓存机制

`decorators.py:457-496` 实现了跨进程的编译缓存：

```
首次运行：
  1. 计算 hash（vllm版本 + 模型 qualname + 配置参数）
  2. torch.compile → Dynamo + Inductor → 生成 kernel
  3. 保存到 VLLM_CACHE_ROOT/torch_compile_cache/torch_aot_compile/{hash}/rank_N/model

后续运行：
  1. 计算相同 hash
  2. 尝试从磁盘加载 → 验证源码未变更（_verify_source_unchanged）
  3. 直接跳过 Dynamo + Inductor，加载预编译 kernel
```

源码验证（`decorators.py:250-266`）通过对比 Dynamo tracing 时内联的所有文件的 checksum，确保代码变更后缓存自动失效。

---

### 1.7 总结：完整数据流

```
@support_torch_compile 装饰
        │
        ▼
__init__: 注入 TorchCompileWithNoGuardsWrapper
          ↓ torch.compile(forward, fullgraph=True, dynamic=False)
          ↓ guard_filter_fn = 丢弃所有 guards

首次 __call__
        │
        ├─ mark_dynamic / mark_unbacked（告知 Dynamo 哪些维度可变）
        │
        ├─ Dynamo tracing（全图追踪，拦截 inline_call_ 收集 traced_files）
        │         ↓
        │   FX Graph（符号化计算图）
        │         ↓
        ├─ Inductor 编译 + 图分区
        │         ↓
        │   [partition0] [partition1] [partition2]
        │       ↓                           ↓
        │  CUDA Graph               non-capturable
        │  static_graph_wrapper        (eager)
        │  （由 maybe_use_cudagraph_partition_wrapper 注入）
        │
        └─ 保存 AOT artifact（可选）

后续 __call__
        │
        └─ with maybe_use_cudagraph_partition_wrapper:
               aot_compiled_fn(self, *args)
               → partition0 回放 CUDA Graph
               → partition1 eager 执行
               → partition2 回放 CUDA Graph
```

这套机制实现了：**一次编译，丢弃 guards，CUDA Graph 分段加速，跨进程缓存复用**，是 vLLM 高吞吐推理性能的关键支撑。

---

## 2. `dynamic_arg_dims={"input_ids": 0}` 含义解析

### 2.1 字面含义

这是一个字典，格式为 `{参数名: 动态维度索引}`：

- **key** `"input_ids"` → `forward` 方法中某个参数的名字
- **value** `0` → 该 tensor 的**第 0 维**（batch/token 数量那一维）是动态的

### 2.2 对应 forward 签名

```python
@support_torch_compile(dynamic_arg_dims={"input_ids": 0})
class MyModel(nn.Module):
    def forward(
        self,
        input_ids: torch.Tensor,  # shape: [num_tokens, ...]
        ...
    ): ...
```

`input_ids` 的 shape 在不同请求间会变化：
- prefill 时可能是 `[512]`（512个输入 token）
- decode 时可能是 `[4]`（4个并发请求各产生1个token）

`0` 就是告诉编译器：**第0维（token数量）是可变的，不要把它的具体值编译进去**。

### 2.3 运行时的效果

在第一次 `__call__` 时，`_mark_dynamic_inputs` 会在实际 tensor 上调用：

```python
torch._dynamo.mark_dynamic(input_ids, [0])
```

这告诉 Dynamo：

```
input_ids.shape = (s0, ...)
                    ↑
               这是符号变量，不是具体整数
               Dynamo 生成的图对任意 s0 都成立
```

### 2.4 不加 vs 加的对比

```python
# 不标记动态维度
# Dynamo 看到 input_ids.shape = (512,)，直接把 512 "烧进"编译图
# 下次 shape=(4,) 时 → 重新编译！→ 推理延迟飙升

# 标记 dim=0 为动态
# Dynamo 用符号变量 s0 代替 512
# 编译图对任意 batch size 都有效 → 一次编译永久使用
```

### 2.5 value 支持多种写法

| 写法 | 含义 |
|---|---|
| `{"x": 0}` | x 的第0维动态 |
| `{"x": [0, 1]}` | x 的第0、1维都动态 |
| `{"x": -1}` | x 的最后一维动态（负数索引） |
| `{"x": 0, "y": 0}` | x 和 y 的第0维都动态 |

### 2.6 为什么不直接把所有维度都标记为动态？

动态维度越多，编译器能做的**形状推导和优化越少**（比如无法融合某些算子、无法确定内存布局），会损失性能。只标记真正会变化的维度，是精度和性能之间的权衡。

---

## 3. Piecewise CUDA Graph 切图机制

### 3.1 切图的判断标准：splitting_ops

vLLM 使用一个**算子白名单**来决定在哪里切图，而不是分析什么"是否能被 CUDA Graph 捕获"。这个列表叫 `splitting_ops`，核心内容来自 `config/compilation.py`：

```python
_attention_ops = [
    "vllm::unified_attention",
    "vllm::unified_attention_with_output",
    "vllm::unified_mla_attention",
    "vllm::mamba_mixer2",
    "vllm::unified_kv_cache_update",
    ...
]
```

**这些都是 vLLM 自定义的 C++ 算子**（Attention、KV Cache 更新、Mamba 等），它们有以下特征导致无法被 CUDA Graph 捕获：
- 内部有基于运行时数据的**条件分支**（prefill vs decode 走不同路径）
- 访问 **PagedAttention KV Cache**（内存地址在运行时才确定）
- 包含 **all-reduce** 等通信原语（NCCL 通信不能在 CUDA Graph 内部）

---

### 3.2 两种切图模式

#### 模式1：FX 级别切图（默认，`use_inductor_graph_partition=False`）

**这部分完全在 vLLM 里实现**，核心逻辑在 `backends.py:474-539` 的 `split_graph()`：

```python
def split_graph(graph, splitting_ops):
    subgraph_id = 0
    node_to_subgraph_id = {}

    for node in graph.graph.nodes:
        if should_split(node, splitting_ops):  # 命中 attention 等算子
            subgraph_id += 1
            node_to_subgraph_id[node] = subgraph_id  # 单独一个子图
            subgraph_id += 1  # 下一个普通段重新开始
        else:
            node_to_subgraph_id[node] = subgraph_id  # 归入当前普通子图

    # 调用 PyTorch 工具函数物理分割图
    split_gm = torch.fx.passes.split_module.split_module(
        graph, None,
        lambda node: node_to_subgraph_id[node],
        keep_original_order=True
    )
```

切图判断在 `partition_rules.py:14-38`：

```python
def should_split(node, splitting_ops):
    if node.op != "call_function":
        return False
    target = node.target
    # 检查是否是 splitting_ops 白名单中的算子
    if isinstance(target, torch._ops.OpOverload):
        return target.name() in splitting_ops
    ...
```

切图后的处理（`backends.py:1094-1098`）：

```python
# is_splitting_graph=True 的子图（attention等）→ 不编译，eager执行
# is_splitting_graph=False 的子图（FFN等）→ 送 Inductor 编译 + CUDA Graph 捕获
submod_names_to_compile = [
    item.submod_name
    for item in self.piecewise_graphs
    if not item.is_splitting_graph   # ← 只编译非切分点子图
]
```

最终每个可捕获子图被包装（`backends.py:545-596`）：

```python
return static_graph_wrapper_class(
    runnable=piecewise_backend,
    runtime_mode=CUDAGraphMode.PIECEWISE,
    cudagraph_options=CUDAGraphOptions(
        debug_log_enable=is_first_graph,
        gc_disable=not is_first_graph,
        weak_ref_output=is_last_graph,
    ),
)
```

#### 模式2：Inductor 级别分区（`use_inductor_graph_partition=True`）

**切图判断逻辑仍在 vLLM 里**，但执行时机交给了 Inductor。通过 `partition_rules.py:41-75`：

```python
# vLLM 将 splitting_ops 写入 PyTorch 的 Inductor 配置
torch._inductor.config.custom_should_partition_ops = splitting_ops
```

这告诉 Inductor："在 codegen 阶段，遇到这些算子就在这里切分区"。整个 FX 图**不预先切割**，让 Inductor 先做完所有 pass 和 fusion，再在最后切分，这样跨段的算子融合机会更多。

---

### 3.3 完整流程对比

```
Dynamo Trace → FX Graph
       │
       ▼
  VllmBackend.__call__()
       │
       ├─[默认模式]─────────────────────────────────────────────────────
       │   split_graph() ← vLLM实现
       │   ┌──────────┬──────────────┬──────────┐
       │   │ submod_0 │   submod_1   │ submod_2 │
       │   │  FFN等   │  attention   │  FFN等   │
       │   │  ↓Inductor│  ↓eager执行 │  ↓Inductor
       │   │  编译     │  不入图      │  编译    │
       │   │ CUDAGraph │             │ CUDAGraph│
       │   └──────────┴──────────────┴──────────┘
       │
       └─[Inductor分区模式]────────────────────────────────────────────
           完整FX图 → Inductor全图优化/fusion
                          ↓
               inductor codegen阶段
               按custom_should_partition_ops切分
               ┌────────┬────────────┬────────┐
               │ part_0 │  part_1    │ part_2 │
               │ CUDAGraph │ eager  │ CUDAGraph
               └────────┴────────────┴────────┘
               每个partition由 customized_cudagraph_wrapper 包装
```

---

### 3.4 总结

| 问题 | 答案 |
|---|---|
| **切图逻辑在哪？** | vLLM（`backends.py`, `partition_rules.py`） |
| **PyTorch 参与什么？** | `torch.fx.passes.split_module.split_module()` 负责物理分割；`torch._inductor.config.custom_should_partition_ops` 通知 Inductor 在哪切 |
| **切图依据是什么？** | 算子名白名单（`splitting_ops`），主要是 vLLM 自定义的 attention、KV cache 更新算子 |
| **为什么这些算子不能入图？** | 动态控制流、PagedAttention 地址运行时确定、NCCL 通信无法录入 CUDA Graph |
| **两种模式区别？** | 默认模式：先切再分别编译（简单稳定）；Inductor分区模式：全图优化后再切（fusion效果更好，需 PyTorch ≥ 2.9） |

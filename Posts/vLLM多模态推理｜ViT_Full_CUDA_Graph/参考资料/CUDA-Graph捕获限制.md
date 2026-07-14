> **Q：在 cuda graph python api 的 capture 执行路径上，可以包含类似 torch.cat() 这样的 cpu 操作吗？**

一般来说：**不建议，也通常不能在 CUDA Graph capture 的执行路径里包含会触发 CPU 同步、动态分配或 Python-side shape/control-flow 的操作**。
`torch.cat()` 要分情况看。

核心原则是：

> CUDA Graph capture 期间，CPU 不能参与“决定执行图结构”，图里的 GPU work 必须是静态、可复现、地址稳定的。

---

# 先说结论

## `torch.cat()` 有时可以 capture

如果满足：

* 输入 tensor 数量固定
* shape 固定
* dtype/device 固定
* 输出 tensor 大小固定
* 不发生新的动态显存分配
* 不依赖 CPU 数据

那么：

```python
y = torch.cat([a, b], dim=0)
```

通常是**可以被 capture 进去的**。

因为本质上它最终会变成：

* 一个或多个 CUDA kernel
* 外加 allocator 行为

而 allocator 在 warmup 后可能稳定。

---

# 但很多 `torch.cat()` 实际上会导致 graph break / capture failure

最常见原因：

---

## 1. capture 期间发生新的 cuda malloc

这是最典型问题。

例如：

```python
y = torch.cat([a, b], dim=0)
```

默认会：

* 新建输出 tensor
* 触发 allocator

而 CUDA Graph 要求：

* capture/replay 时显存地址稳定

如果 allocator 在 replay 时行为不一致：

就可能报：

```text
cudaErrorStreamCaptureUnsupported
```

或者：

```text
operation not permitted when stream is capturing
```

---

## 2. Python list 动态变化

例如：

```python
xs = []
for i in range(n):
    xs.append(...)
y = torch.cat(xs)
```

这里：

* tensor 数量可能变化
* graph topology 不稳定

CUDA Graph 不允许。

---

## 3. shape 动态

例如：

```python
torch.cat([a[:k], b], dim=0)
```

这里：

* 输出 shape 随 k 变化
* kernel launch 参数变化

graph replay 会失效。

---

# PyTorch 官方实践里怎么处理 cat

高性能 inference（例如 [PyTorch CUDA Graphs 文档](https://pytorch.org/docs/stable/notes/cuda.html?utm_source=chatgpt.com#cuda-graphs)、[vLLM 官方网站](https://vllm.ai?utm_source=chatgpt.com)、[NVIDIA CUDA Graphs 文档](https://developer.nvidia.com/blog/cuda-graphs/?utm_source=chatgpt.com)）通常会：

## 方法1：提前分配 output buffer（推荐）

避免：

```python
y = torch.cat(...)
```

改成：

```python
out[:a_size].copy_(a)
out[a_size:].copy_(b)
```

这样：

* 没有动态 allocator
* 没有新 tensor
* 地址稳定
* graph-friendly

这是最常见写法。

---

## 方法2：静态 shape + warmup

有时：

```python
torch.cat()
```

在：

* shape 完全固定
* allocator 已 warmup

时也能稳定 capture。

但大型推理框架一般仍然避免它。

---

# 为什么你说“cpu操作”不准确

`torch.cat()` 本身：

* 不是纯 CPU op
* 真正的数据拼接在 GPU kernel 上

但：

* Python dispatcher
* shape inference
* allocator
* Tensor metadata 创建

都在 CPU 侧。

CUDA Graph capture 最大的问题其实是：

> “CPU-side dynamic behavior”

而不是“有没有 CPU 代码”。

因为所有 PyTorch op 都会经过 CPU dispatcher。

---

# 哪些 CPU 行为一定不能出现在 capture 中

典型包括：

## 明确同步 GPU

```python
torch.cuda.synchronize()
x.item()
print(x)
```

因为：

* 会强制 CPU 等 GPU

---

## 根据 GPU 结果做 Python control flow

```python
if x.sum() > 0:
```

这里：

* `x.sum()` 要同步回 CPU

不允许。

---

## 动态创建 tensor shape

```python
torch.empty(x.shape[0] + k)
```

如果 k 变化：

graph 不稳定。

---

# vLLM / TensorRT-LLM / SGLang 里怎么做

这些框架在 CUDA Graph 路径上通常：

* 禁止动态 batch
* 禁止动态 seq
* 使用 static buffer
* 使用 inplace copy
* 避免 cat/stack/repeat 等动态分配 op

例如 KV cache append：

不会：

```python
torch.cat([old_kv, new_kv], dim=1)
```

而是：

```python
kv_cache[:, start:end].copy_(new_kv)
```

这是 graph-compatible 的标准做法。

---

# 一个经验判断

如果一个 op：

* 会创建新 tensor
* shape 可能变化
* allocator 参与明显

那它大概率：

> “理论上可能支持，但工程上不适合 CUDA Graph”

`torch.cat()` 就属于这一类。

---
> **Q：为什么 python 的 `.tolist()` 操作不能被 cuda graph 捕获？**

本质原因是：**`Tensor.tolist()` 会触发 GPU → CPU 的同步数据拷贝，而 CUDA Graph 要求捕获期间不能出现依赖 CPU 结果的同步操作。**

---

## CUDA Graph 捕获的基本原则

CUDA Graph 记录的是一串 GPU 操作：

```text
CPU
 │
 ├── launch kernel A
 ├── launch kernel B
 ├── launch kernel C
 │
GPU
```

Capture 时，CUDA 会把这些 kernel launch 记录下来：

```python
with torch.cuda.graph(g):
    y = model(x)
```

之后 replay：

```python
g.replay()
```

就不需要 CPU 再逐个发射 kernel 了。

因此 CUDA Graph 有一个重要约束：

> Capture 期间 CPU 不能等待 GPU 结果。

---

## `.tolist()` 干了什么

例如：

```python
x = torch.tensor([1,2,3], device="cuda")

x.tolist()
```

内部实际上类似于：

```python
x_cpu = x.cpu()      # GPU -> CPU copy
return list(x_cpu.numpy())
```

需要：

1. 等待 GPU 计算完成
2. 把数据拷回 Host Memory
3. Python 构造 list

即：

```text
GPU Tensor
    │
    ▼
同步(copy)
    │
    ▼
CPU Tensor
    │
    ▼
Python List
```

这里发生了：

```text
device -> host synchronization
```

而 CUDA Graph 无法记录这种行为。

---

## 为什么无法捕获

假设：

```python
with torch.cuda.graph(g):
    n = x.sum().tolist()
```

Capture 时会遇到：

```text
GPU kernel (sum)
      ↓
CPU 等待结果
      ↓
Python list
```

问题来了：

### Graph 只能记录 GPU 工作

CUDA Graph 能记录：

```text
kernel
memcpy(device->device)
memset
```

不能记录：

```text
Python object creation
Python list
CPU branching
CPU synchronization
```

因为 replay 时：

```python
g.replay()
```

根本不会再执行 Python。

CUDA Graph 不知道：

```python
[123]
```

这个 list 应该怎么生成。

---

## 更深层原因：动态 Host 数据

例如：

```python
with torch.cuda.graph(g):
    idx = x.argmax().item()
```

第一次 capture：

```text
idx = 17
```

第二次 replay：

```text
idx = 42
```

那么问题来了：

Graph 应该返回哪个？

```text
17 ?
42 ?
```

CUDA Graph 的设计是：

> Replay 不产生新的 CPU 可见结果。

所以：

```python
.item()
.tolist()
.numpy()
.cpu()
```

这类需要把值返回给 Python 的操作都不允许。

---

## PyTorch 官方常见禁止项

在 CUDA Graph Capture 中，下面通常都会报错：

```python
tensor.item()
tensor.tolist()
tensor.numpy()
tensor.cpu()
print(tensor)
```

因为它们最终都需要：

```text
cudaStreamSynchronize()
```

或者

```text
cudaMemcpy(Device → Host)
```

而这会破坏 Graph 的可重放性。

---

## 正确做法

### 方案1：保持 Tensor 形式

不要：

```python
n = x.sum().item()
```

而是：

```python
n = x.sum()
```

继续在 GPU 上使用：

```python
y = torch.arange(100, device="cuda")[:n]
```

（如果算子支持 Tensor 输入）

---

### 方案2：Capture 外取值

```python
g.replay()

n = x.sum().item()
```

即：

```text
Graph内部:
    全GPU

Graph外部:
    Host读取结果
```

---

### 方案3：提前分配输出 Buffer

CUDA Graph 推荐：

```python
output = torch.empty(1, device="cuda")

with torch.cuda.graph(g):
    output.copy_(x.sum())
```

Replay 后：

```python
g.replay()

value = output.item()
```

同步发生在 Graph 外。

---

## vLLM 中常见场景

你最近在看 vLLM 的代码，里面经常有类似逻辑：

```python
grid_hws.tolist()
seq_lens.tolist()
cu_seqlens.tolist()
```

这些通常出现在：

* FlashAttention 参数准备
* MRotary embedding
* Vision encoder patch 计算

如果这些代码出现在：

```python
forward()
```

里，就会导致：

```text
CUDA graph capture failed
operation not permitted when stream is capturing
```

因为：

```python
tensor(cuda)
    ↓
tolist()
    ↓
Python list
```

强制进行了 device→host 同步。

因此很多支持 CUDA Graph 的实现会改成：

```python
torch.ops.xxx(tensor)
```

直接让 CUDA kernel 接收 Tensor，而不是 Python list。

---

一句话总结：

> `tolist()` 不能被 CUDA Graph 捕获，不是因为它本身是 Python API，而是因为它必须把 GPU Tensor 的内容同步拷贝到 CPU，并生成 Python 对象；CUDA Graph 只能记录 GPU 侧可重放的操作，无法记录这种 GPU→CPU 同步和 Python 执行过程。

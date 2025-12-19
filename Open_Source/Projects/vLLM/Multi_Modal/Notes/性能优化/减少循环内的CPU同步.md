PR: https://github.com/vllm-project/vllm/pull/30232

---

将下面的代码：

```python
for i in range(1, len(cu_seqlens)):
    start_idx = cu_seqlens[i - 1]
    end_idx = cu_seqlens[i]
    q_i = query[:, start_idx:end_idx]
    k_i = key[:, start_idx:end_idx]
    v_i = value[:, start_idx:end_idx]
    q_i, k_i, v_i = (
        einops.rearrange(x, "b s h d -> b h s d") for x in [q_i, k_i, v_i]
    )
    output_i = F.scaled_dot_product_attention(q_i, k_i, v_i, dropout_p=0.0)
    output_i = einops.rearrange(output_i, "b h s d -> b s h d ")
    outputs.append(output_i)
```

修改为：

```python
lens = (cu_seqlens[1:] - cu_seqlens[:-1]).tolist()
q_chunks = torch.split(q, lens, dim=1)
k_chunks = torch.split(k, lens, dim=1)
v_chunks = torch.split(v, lens, dim=1)
for q_i, k_i, v_i in zip(q_chunks, k_chunks, v_chunks):
    q_i, k_i, v_i = (
        einops.rearrange(x, "b s h d -> b h s d") for x in [q_i, k_i, v_i]
    )
    output_i = F.scaled_dot_product_attention(q_i, k_i, v_i, dropout_p=0.0)
    output_i = einops.rearrange(output_i, "b h s d -> b s h d ")
    outputs.append(output_i)
```

为什么修改之后可以减少 cuda stream 同步的次数，从而提升性能，这背后的原理是什么？

下面给你**深入但不啰嗦**地解释：为什么用 `torch.split` + `zip` 的写法，会减少 CUDA stream 同步（synchronization），从而提升性能。

---

# 🔍 原因总结

核心原因只有一句话：

> **你把许多小的 Python-level slice 操作（会频繁触发同步）变成了更大的、一次性完成的 tensor split 操作（更少的同步）**。

接下来讲背后的机制。

---

# 🚦 关键区别：切片 vs. split 的执行模型

## ## 1. 原始写法：大量 *Python 循环 + 切片*

你的原始代码，每一轮循环都做这些操作：

```python
q_i = query[:, start_idx:end_idx]
k_i = key[:, start_idx:end_idx]
v_i = value[:, start_idx:end_idx]
```

### 👉 **这些切片是 Python-level indexing**

虽然它们是「view 操作」，**但 Python 层每次访问形状、步长或触发 indexing 运算时，可能要求 CUDA 张量的 metadata 可用**。

如果前面有任何 CUDA kernel 还在异步执行，Python 有时必须**等待之前的 CUDA 工作完成**（synchronize）才能继续安全执行下一段逻辑（尤其涉及跨 kernel 的 shape 运算、检查、dispatch 等场景）。

因此，你有：

* 一次循环 → 多次 indexing → 可能多次 sync
* 循环次数 = `len(cu_seqlens) - 1`（可能几十到几百）

同步次数 = **循环次数 × 3 个切片操作**
非常多。

---

## ## 2. 新写法：一次 torch.split → 预生成所有 chunk

你改写后：

```python
lens = (cu_seqlens[1:] - cu_seqlens[:-1]).tolist()
q_chunks = torch.split(q, lens, dim=1)
```

### 👉 torch.split 是 **一个单一的 C++ op**

它一次性完成所有 chunk 的 metadata 构建：

* 不需要 Python 在循环中逐次执行切片运算
* 不需要 Python 反复和 CUDA 同步地查询 tensor metadata
* 不需要在每次循环里调度 indexing kernel（split 不需要 kernel，只创建 views）

其内部调度一次，相比：

```
循环 N 次，每次做 slicing
```

变成了：

```
只做 1 次 split（只可能同步 1 次或甚至不需要）
循环 N 次只是处理已经创建好的 views
```

因此：

### ✔️ **同步次数大幅降低**

### ✔️ **Python interpreter 开销降低**

### ✔️ **减少 PyTorch Dispatcher 的热点开销**

（每次切片需要走 dispatcher，split 一次性调度）

---

# 🧠 为什么 split 不需要同步，而切片可能需要？

因为：

### **切片是 Python-level indexing 运算，需要在 CPU 侧构建 tensor view metadata**

如果 PyTorch 判断需要知道：

* 当前张量的 strides
* storage offset
* shape 检查
* dtype 检查
* contiguous 性判断
* 未来 kernel 调用的参数准备

这可能需要 GPU 前面的 kernel 执行完毕，而导致 sync。

而：

### **torch.split 是一个原子操作在 C++ 里执行，不需要 Python 多次干预**

内部可以直接基于已经已知的 metadata 进行 view 分割，不会触发 N 次 dispatcher 调度，更不会在 python 循环里重复查询 GPU 状态。

---

# 📈 实际性能提升来源

你的修改带来的性能改进来自：

### **1. 减少 CUDA stream 同步次数**

* 原来 = 每个 token chunk 切 3 次（q/k/v）
* 现在 = 只在 split/broadcast 处同步很少的次数

### **2. 减少 Python 循环中的 dispatcher 调度**

* 原来 = 每轮都做 3 个 indexing（非常慢）
* 现在 = 只循环处理已经准备好的 view

### **3. 提前构建所有 view → 更高的 cache locality**

split 创建的 views 连续存储在 Python list 里，Python 侧循环更快。

### **4. 避免 Python 层反复与 CUDA runtime 交互**

每次 indexing 都有可能触发：

* shape inference
* dynamic dispatch
* kernel launch barrier
  这些在 Python 循环中造成大量 overhead。

---

# 📌 一句话的深度解释（面试级别）

> 改成 split 后，GPU 上的张量分段工作在 C++ 后端一次性完成，不再需要在 Python 层对 CUDA 张量做逐次切片，而逐次切片常常需要在 Python 与 CUDA runtime 同步，因此减少了 CUDA stream synchronization，从而提升了整体性能。

---

# 如果需要，我还可以帮你：

* 画图解释 CUDA pipeline 中的 sync 行为
* 给你写 profiler 对比代码
* 解释为什么 indexing 有时异步、有时同步
* 展示 NVTX trace 的对比图（理论版）

你想继续深入吗？

---

画图解释 CUDA pipeline 中的 sync 行为?

下面给你一个**清晰直观的图示**，解释为什么原始代码会导致更多 **CUDA pipeline 同步（sync）**，而改成 `torch.split()` 后同步大幅减少。

为方便画图，下面采用 **ASCII timeline 示意图**。

---

# 🧠 背景：CUDA 异步执行模型（很重要）

* **GPU kernels 异步执行**：CPU 发出 kernel → 不等 GPU 完成 → CPU 继续执行下一行代码
* 但：
  **某些 CPU 操作（例如 tensor slicing / shape query / dispatcher 检查）可能需要 GPU 的最新状态** → 导致 CPU 强制等待 GPU → **sync**

---

# ❌ 原始写法：循环中不断切片导致反复 sync

你原来每一步都做：

```python
start_idx = cu_seqlens[i - 1]   # CPU
end_idx = cu_seqlens[i]         # CPU
q_i = query[:, start_idx:end_idx]  # CPU  slicing
k_i = key[:, start_idx:end_idx]
v_i = value[:, start_idx:end_idx]
```

## 🔻 图示：CPU 重复触发 slicing → 多次 sync

下面的图中：

* `K1, K2, …` 是 GPU kernels（比如上一轮 attention）
* `slice()` 表示 Python 在 CPU 做切片
* `SYNC` 表示 slicing 需要 GPU 先完成前面的 kernel  → 强制等待

```
Time →
CPU:   launch K1 ──────────────────► slice() ───SYNC───► launch K2 ─────────────────► slice() ───SYNC──► launch K3 ...
GPU:        [ K1 running ]                     [wait CPU]      [ K2 running ]                    [wait CPU]   ...
```

解释：

1. CPU 启动 K1 （异步）
2. 进入下一行做切片
3. Python 需要读取 GPU tensor 的 metadata（shape/stride）
4. GPU 上 K1 还没完全完成 → CPU 不敢乱读 → 强制 `SYNC`
5. GPU 执行完 → 继续
6. CPU 启动 K2
7. 下一次切片 → 再次 sync
8. 如此循环 N 次（N = chunk 数量 × 3）

**每个 chunk 都至少 sync 1～3 次**（q/k/v 三个切片）

---

# ✔ 新写法：一次 torch.split() → 只同步 1 次（或 0 次）

```python
lens = (cu_seqlens[1:] - cu_seqlens[:-1]).tolist()
q_chunks = torch.split(q, lens, dim=1)
```

### `torch.split` 在 C++ 中一次性构建所有 view，不需要 Python N 次干预

## 🔻 图示：只同步一次（甚至零次）

```
Time →
CPU:   launch K1 ──────────────────────► torch.split() ──SYNC?──► launch K2 ───────────► launch K3 ...
GPU:        [ K1 running ]                            [continue async]   [ K2 running ]   ...
```

然后你的循环只是：

```python
for q_i, k_i, v_i in zip(q_chunks, ...):
    # 只是 Python 遍历 list，不触发 CUDA 操作
```

因此：

### 在循环中 **不再触发切片，不再触发 sync**

---

# 🎯 直观对比（非常关键）

### 原始写法（假设有 20 个 chunk）

```
Loop:
    slice q  → sync
    slice k  → sync
    slice v  → sync
    attention kernel
```

同步次数 ≈ **20 × 3 = 60 次**

---

### split 写法

```
torch.split → (0~1 次 sync)
Loop:
    只是 Python 遍历，不触发 sync
    attention kernel
```

同步次数 ≈ **1 次**

---

# 📈 为什么 `torch.split` 不需要循环同步？

* split 内部一次获取 tensor metadata
* 在 C++ 后端批量创建所有 view
* Python 不再需要反复访问 GPU tensor 的 stride/shape
* 因此同步几乎消失

---

# 🎨 完整对比图（ASCII 精准展示）

## 原始代码（多 sync）

```
CPU:  launch K1 → slice() ─sync→ launch K2 → slice() ─sync→ launch K3 → slice() ─sync→ ...
GPU:      K1 running     wait     K2 running     wait     K3 running     wait
```

## split 优化后（几乎无 sync）

```
CPU:  launch K1 → torch.split() (maybe sync once) → loop over chunks (no sync) → launch K2 → launch K3 ...
GPU:      K1 running               continue async                K2 running      K3 running
```

---

# 🌟 结论（易懂版）

> **Python 在循环中做切片，会频繁访问 GPU 张量的 metadata，从而强制等待 GPU 完成之前的 kernel（sync）。**
>
> **把切片提前到 torch.split 中一次性完成，就避免了循环内的同步，大幅减少 CUDA stream sync，性能自然提升。**

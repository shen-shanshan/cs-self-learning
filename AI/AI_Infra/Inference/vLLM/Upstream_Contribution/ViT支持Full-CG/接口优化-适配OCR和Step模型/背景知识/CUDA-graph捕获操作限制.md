> Q：在 cuda graph python api 的 capture 执行路径上，可以包含类似 torch.cat() 这样的 cpu 操作吗？

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

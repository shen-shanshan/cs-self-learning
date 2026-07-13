# CUDA Graph 学习笔记

TODO：blog writing skill

## 为什么需要 CUDA Graph？

Modern GPUs are so fast that, in many cases of interest, the time taken by each GPU operation (e.g. kernel or memory copy) is now measured in microseconds. However, there are overheads associated with the submission of each operation to the GPU – also at the microsecond scale – which are now becoming significant in an increasing number of cases.

CUDA Graphs have been designed to allow work to be defined as graphs rather than single operations. They address the above issue by providing a mechanism to launch multiple GPU operations through a single CPU operation, and hence reduce overheads.

---

Replaying a graph sacrifices the dynamic flexibility of typical eager execution in exchange for greatly reduced CPU overhead. A graph’s arguments and kernels are fixed, so a graph replay skips all layers of argument setup and kernel dispatch, including Python, C++, and CUDA driver overheads. Under the hood, a replay submits the entire graph’s work to the GPU with a single call to cudaGraphLaunch. Kernels in a replay also execute slightly faster on the GPU, but eliding CPU overhead is the main benefit.

## 基本概念

So, first we must define the graph, and we do this by capturing information on GPU activities that are submitted to the stream between the cudaStreamBeginCapture and cudaStreamEndCapture calls. Then, we must instantiate the graph through the cudaGraphInstantiate call, which creates and pre-initialises all the kernel work descriptors so that they can be launched repeatedly as rapidly as possible. The resulting instance can then be submitted for execution through the cudaGraphLaunch call.

Crucially, it is only necessary to capture and instantiate once (on the first timestep), with re-use of the same instance on all subsequent timesteps (as controlled here by the conditional statement on the graphCreated boolean value).

Note that in this case, the time to create and instantiate the graph is relatively large at around 400μs, but this is only performed a single time, so this is only contributes around 0.02μs to our per-kernel cost. Similarly, the first graph launch is around 33% slower that all subsequent launches, but that becomes insignificant when re-using the same graph many times.

The severity of the initialization overhead is obviously problem dependent: typically in order to benefit from graphs you need to re-use the same graph enough times.

---

Terms:

- Straight line kernel graph: A CUDA Graph made completely out of kernel nodes, where every node has a single dependent node except for the last node.
- Parallel straight line: A CUDA Graph that has width as the number of entry points. Each entry point is followed by its own set of lengths as the number of nodes arrayed in a straight-line formation. This is also referred to as parallel chain in the charts and source code.
- First launch: The first launch of a graph, which also includes uploading the instantiated graph to the device.
- Repeat launch: Launching a graph that has already been uploaded to the device.

## PyTorch 集成

PyTorch supports the construction of CUDA graphs using stream capture, which puts a CUDA stream in capture mode. CUDA work issued to a capturing stream doesn’t actually run on the GPU. Instead, the work is recorded in a graph.

After capture, the graph can be launched to run the GPU work as many times as needed. Each replay runs the same kernels with the same arguments. For pointer arguments this means the same memory addresses are used. By filling input memory with new data (e.g., from a new batch) before each replay, you can rerun the same work on new data.

## 使用约束（捕获限制）

However, this performance comes at the cost of flexibility. If the full workflow is not known in advance, then GPU execution must be interrupted to return to the CPU to make a decision.

> CUDA device graph launch offers a performant way to enable dynamic control flow within CUDA kernels.

---

CUDA Graph 要求被捕获的计算图中所有 tensor shapes 在 capture 和 replay 间保持一致。

核心约束：

- **无 CPU 同步**: 不能在 graph 内调用 `.item()`、`.cpu()` 或任何触发 D2H copy 的操作；
- **无动态控制流**: 不能在 graph 内使用依赖 tensor 值的 `if/else` 分支（依赖 shape 的 `if` 在 capture 时 bake）；
- **固定 shape**: 所有中间 tensor 的 shape 必须在 capture 时确定。

更多细节：[link 🌟](./CUDA-Graph捕获限制.md)

参考资料：

> `tensor.item()` 的核心作用就是将一个只包含单个元素的 Tensor 转换为 Python 标量。它最常用于获取损失值、评估指标等数值，便于打印、记录日志或与普通 Python 代码交互。需要注意的是，它只能用于单元素 Tensor，并且对于 GPU Tensor 会触发一次 GPU 到 CPU 的同步，因此不宜在性能关键路径中频繁调用。

# CUDA Graph 学习笔记

TODO：

- blog writing skill
- 为什么 capture 之前需要 warmup？

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

---

`torch.cuda.graph` is a simple, versatile context manager that captures CUDA work in its context. Before capture, warm up the workload to be captured by running a few eager iterations. Warmup must occur on a side stream. Because the graph reads from and writes to the same memory addresses in every replay, you must maintain long-lived references to tensors that hold input and output data during capture. To run the graph on new input data, copy new data to the capture’s input tensor(s), replay the graph, then read the new output from the capture’s output tensor(s).

```python
g = torch.cuda.CUDAGraph()

# Placeholder input used for capture
static_input = torch.empty((5,), device="cuda")

# Warmup before capture
s = torch.cuda.Stream()
s.wait_stream(torch.cuda.current_stream())
with torch.cuda.stream(s):
    for _ in range(3):
        static_output = static_input * 2
torch.cuda.current_stream().wait_stream(s)

# Captures the graph
# To allow capture, automatically sets a side stream as the current stream in the context
with torch.cuda.graph(g):
    static_output = static_input * 2

# Fills the graph's input memory with new data to compute on
static_input.copy_(torch.full((5,), 3, device="cuda"))
g.replay()
# static_output holds the results
print(static_output)  # full of 3 * 2 = 6

# Fills the graph's input memory with more data to compute on
static_input.copy_(torch.full((5,), 4, device="cuda"))
g.replay()
print(static_output)  # full of 4 * 2 = 8
```

## 使用约束（捕获限制）

However, this performance comes at the cost of flexibility. If the full workflow is not known in advance, then GPU execution must be interrupted to return to the CPU to make a decision.

> CUDA device graph launch offers a performant way to enable dynamic control flow within CUDA kernels.

---

**Constraints:**

Violating any of these will likely cause a runtime error:

- **Capture must occur on a non-default stream.** (This is only a concern if you use the raw CUDAGraph.capture_begin and CUDAGraph.capture_end calls. graph and make_graphed_callables() set a side stream for you.)
- **Ops that synchronize the CPU with the GPU (e.g., .item() calls) are prohibited.**
- CUDA RNG operations are permitted, and when using multiple torch.Generator instances within a graph, they must be registered using CUDAGraph.register_generator_state before graph capture. Avoid using Generator.get_state and Generator.set_state during capture; instead, utilize Generator.graphsafe_set_state and Generator.graphsafe_get_state for managing generator states safely within the graph context. This ensures proper RNG operation and generator management within CUDA graphs. (?)
- **Dynamic control flow (based on CPU or GPU data) is prohibited**, unless it is based on GPU data and implemented via higher order operator torch.cond(). See Data Dependent Control Flow.

> `tensor.item()` 的核心作用就是将一个只包含单个元素的 Tensor 转换为 Python 标量。它最常用于获取损失值、评估指标等数值，便于打印、记录日志或与普通 Python 代码交互。需要注意的是，它只能用于单元素 Tensor，并且对于 GPU Tensor 会触发一次 GPU 到 CPU 的同步，因此不宜在性能关键路径中频繁调用。

Violating any of these will likely cause silent numerical errors or undefined behavior:

- **Within a process, only one capture may be underway at a time.**
- No non-captured CUDA work may run in this process (on any thread) while capture is underway. (?)
- **CPU work is not captured.** If the captured ops include CPU work, that work will be elided during replay.
- **Every replay reads from and writes to the same (virtual) memory addresses.**
- **Dynamic shapes are prohibited.** The graph assumes every tensor in the captured op sequence has the same size and layout in every replay.
- Using multiple streams in a capture is allowed, but there are restrictions.

---

CUDA Graph 要求被捕获的计算图中所有 tensor shapes 在 capture 和 replay 间保持一致。

核心约束：

- **无 CPU 同步**: 不能在 graph 内调用 `.item()`、`.cpu()` 或任何触发 D2H copy 的操作；
- **无动态控制流**: 不能在 graph 内使用依赖 tensor 值的 `if/else` 分支（依赖 shape 的 `if` 在 capture 时 bake）；
- **固定 shape**: 所有中间 tensor 的 shape 必须在 capture 时确定。

更多细节：[link](./CUDA-Graph捕获限制.md) 🌟

---

**Non-constraints:**

Once captured, the graph may be replayed on any stream.

---

**Partial-network capture:**

If some of your network is unsafe to capture (e.g., due to **dynamic control flow, dynamic shapes, CPU syncs, or essential CPU-side logic**), you can run the unsafe part(s) eagerly and use `torch.cuda.make_graphed_callables()` to graph only the capture-safe part(s).

By default, callables returned by `make_graphed_callables()` are autograd-aware, and can be used in the training loop as direct replacements for the functions or `nn.Module`\ s you passed.

`make_graphed_callables()` internally creates CUDAGraph objects, runs warmup iterations, and maintains static inputs and outputs as needed. Therefore (unlike with torch.cuda.graph) you don’t need to handle those manually.

---

Data-dependent control flow can with cuda graphs if the control flow is implemented using `torch.cond()`.

## Graph memory management

A captured graph acts on the same virtual addresses every time it replays. If PyTorch frees the memory, a later replay can hit an illegal memory access. If PyTorch reassigns the memory to new tensors, the replay can corrupt the values seen by those tensors. Therefore, the virtual addresses used by the graph must be reserved for the graph across replays. The PyTorch caching allocator achieves this by detecting when capture is underway and satisfying the capture’s allocations from a graph-private memory pool. The private pool stays alive until its CUDAGraph object and all tensors created during capture go out of scope.

Private pools are maintained automatically. **By default, the allocator creates a separate private pool for each capture.** If you capture multiple graphs, this conservative approach ensures graph replays never corrupt each other’s values, but sometimes needlessly wastes memory.

---

**Sharing memory across captures:**

To economize the memory stashed in private pools, `torch.cuda.graph` and `torch.cuda.make_graphed_callables()` optionally allow different captures to share the same private pool. It’s safe for a set of graphs to share a private pool if you know they’ll always be replayed in the same order they were captured, and never be replayed concurrently.

It’s also safe to share a memory pool across separate graphs that do not depend on each other’s outputs, provided they never run concurrently. Be aware that replaying one graph can clobber another graph’s outputs when they share a pool, unless `clone()` is called on the outputs beforehand. This pattern is frequently used in inference servers that accept variable batch sizes at runtime.

## torch.compile 和 CUDA Graph 的区别？

`torch.compile` 是编译期的优化。 它做的事情是：把你写的 Python 代码追踪（trace）成一张计算图（FX Graph），然后在这张图上做各种变换：算子融合、内存优化、最后直接生成更贴近硬件的 kernel 代码（比如 GPU 上的 Triton kernel）。即使不配合 CUDA Graph，仅仅以普通的 eager 路径去执行这些编译后的子图，性能也可能比原始 eager 执行更好。但当它和 CUDA Graph 叠加时，重点就不再是 CPU 侧的 launch 开销，而是 fusion、codegen 以及调度优化带来的收益。

CUDA Graph 是运行期的优化。 它做的事情是：在第一次运行时，把 GPU 上所有的 kernel 启动序列“捕获”下来，之后每次推理直接“回放”这段录像，省掉了 CPU 侧反复下发 kernel 的开销。它不关心你的代码写得好不好、有没有优化过，它只管“一次性下发”。

因为它们解决的是不同层面的问题，所以两者可以结合起来使用：torch.compile 让你的计算本身跑得更快（更少的 kernel、更少的中间张量，不同资源利用效率更高），CUDA Graph 让计算的启动开销趋近于零。打个比方：torch.compile 是把统筹规划，把原先的十道菜，合成五道菜，营养不变，CUDA Graph 则是把炒菜的动作从“备菜 - 做菜”变成“流水线自动做菜”。两者叠加，才有了我们现在看到的图模式性能。

---

`torch.compile` 在 vLLM 中除了编译优化，还做了什么？诶，很多人可能知道 PIECEWISE 这种图模式，具体这模式是啥我们后面再讲。顾名思义，PIECEWISE 它得把图处理成一个个“PIECE”啊，那“切图”这部分工作就是在编译这阶段完成的：将 attention 部分的代码，统一注册成一个自定义算子，把模型按这些自定义算子切成若干子图，好让 CUDA Graph 分段捕获。如果没有编译，将各种模型结构处理成统一的中间表示，那么这个切分逻辑的维护成本，可能就会相当之高了。

## vLLM 集成

形状上的动态转静态：

推理服务中每个批次的请求数量是变化的，num_tokens 不可能每次都一样，这就是“张量形状的动态性”。怎么办？

答案是分桶（Bucketing）：提前选好一组固定的桶大小，针对每个桶大小各捕获一张图。然后运行时把实际的 num_tokens 向上取整到最近的桶大小，多出来的部分用 padding 填充。

tradeoff：为了把动态 shape 变成静态图，我们需要做分桶与填充；而一旦有填充，就一定会引入冗余计算。问题不在于“有没有冗余”，而在于这部分冗余，值不值得换来更少的下发开销。这就是一个权衡，不能把桶的范围设置过宽，什么批次大小来了都捕获一遍。假如批次较大，原本计算耗时就已经长于下发耗时，那么此时仍然去做填充让其入图，反而会降低性能。诶，这时候大家又说了，填充会降低性能，那我步长为一来捕获，不触发填充，不就没问题了吗？真聪明，但一是没必要，如果批次已经大到计算本身成了瓶颈，那就老老实实走 eager / 非图路径就行了；二是桶也不是越多越好，上一节已经说过，资源不是免费的。

---

FULL / PIECEWISE / FULL_DECODE_ONLY / FULL_AND_PIECEWISE？

这些模式并不是在给用户制造选择困难，而是在回答两个简单问题：attention 这块到底要不要进图？prefill 和 decode 又要不要用同一种策略？

- **PIECEWISE**：把整个 forward 按 attention 切成若干子图（切图的方式我们已经在前文讲过了），attention 自己跑 eager，其余部分走图。它的动机非常朴素：一般来说，attention 往往是最麻烦的一段，backend 差异多，shape 变化也多，先把它留在图外，换来的是更高的兼容性。代价也很直接：没有把 attention 这块 launch overhead 给解决掉，所以性能在某些场景不如整图高；
- **FULL**：整个 forward 就是一张图，attention 也在里面。这个方案最“理想主义”，因为它把图模式的收益吃得最完整：路径统一，launch 开销最低，运行时调度也最干净。但也正因为它什么都想包进去，所以对 attention backend、模型路径、shape 管理的要求最苛刻，一旦某个环节不满足条件，就只能降级；
- **FULL_DECODE_ONLY**：只在纯 decode 阶段走整图，prefill 批次或者混合批次退回 eager。为什么要这么干？因为 decode 的 batch 更规整，尤其是普通单 token decode，最适合捕获并重放整图；而 prefill 往往 query_len 更长，shape 更活跃，图模式的成本大，收益还没那么稳定，那不就算咯？所以这模式的思路就是：只在成本低，收益高的纯解码批次才应用图模式；
- **FULL_AND_PIECEWISE**（V1 默认）：decode 走 FULL，prefill 走 PIECEWISE。 这其实也点出了 prefill 和 decode 的根本差异：decode 更“规整”，适合整图；prefill 更“活”，适合保守一些，给 attention 留出 eager 的退路。站在工程实现的角度看，这个默认值是比较合理，能吃到 decode 侧最主要的图模式收益，又不想把 prefill 那一堆复杂情况全都硬塞进一张大图里，所以通常是性能与兼容性的折中最优解。

最后，图模式能不能用、最终会落到哪种模式，很多时候不是用户“想选什么”就能选什么，而是由兼容性检查决定的。 最常见的约束有这么几类：

- Attention backend 的能力边界：有些 backend 只能支持 decode 侧的整图，有些连 uniform batch 都不接受，更别说 full graph 了。此时你即便手工指定 FULL，运行时也可能被自动降级到 FULL_AND_PIECEWISE、FULL_DECODE_ONLY，甚至 PIECEWISE；
- 模型结构本身的限制：并不是所有模型路径都适合整图，例如 encoder-decoder、pooling 一类特殊结构，在上游 vLLM 中本来就有单独的兼容性处理；
- 运行时 shape 的动态性：图模式终究还是静态图，即使是 FULL_AND_PIECEWISE，如果批次过大，超出了最大桶，那一样会回退 eager 模式。

---

vLLM 中的选择：小批次更容易从图模式中获益，大批次则未必；decode 更容易拿到稳定收益，prefill 则更像是在收益和兼容性之间找平衡。 这也正是为什么上游 vLLM 的默认策略最终收敛到了 FULL_AND_PIECEWISE。

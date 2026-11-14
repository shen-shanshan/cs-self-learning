**Memory Coalescing（内存合并访问）** 是 GPU 编程中一种非常重要的内存访问优化技术。

简单来说：

> Memory Coalescing 指的是：让同一个 Warp（NVIDIA）或 Wavefront（AMD）中的多个 Thread 尽可能访问连续的内存地址，使 GPU 能够将多个 Thread 的内存访问合并成尽可能少的内存事务（Memory Transactions）。

它的主要目的是：**减少 Global Memory Access 的事务数量，提高内存带宽利用率。**

这对于 Triton Kernel、GEMM、Attention 等大模型推理中的计算 Kernel 尤其重要。

## 1. 为什么需要 Memory Coalescing？

先理解 GPU 是如何访问 Global Memory 的。

假设 AMD GPU 上有一个 Wavefront，包含 64 个 Lane（可以理解为 64 个并行执行的 Thread）。

现在每个 Lane 都需要从 Global Memory 读取一个 FP32 数据。

FP32 每个元素占 4 Bytes。

### 情况 A：连续访问（Coalesced Access）

假设 64 个 Lane 分别读取：

```
Lane 0  -> A[0]
Lane 1  -> A[1]
Lane 2  -> A[2]
Lane 3  -> A[3]
...
Lane 63 -> A[63]
```

对应的内存地址：

```
0x0000
0x0004
0x0008
0x000C
...
```

这 64 个 Lane 总共需要读取：

\\[ 64\times4=256\ Bytes \\]

由于访问的内存地址连续，GPU 可以将这些访问合并成较少的 Memory Transactions。

这就是 Coalesced Memory Access。

### 情况 B：不连续访问（Uncoalesced Access）

假设 64 个 Lane 分别读取：

```
Lane 0  -> A[0]
Lane 1  -> A[64]
Lane 2  -> A[128]
Lane 3  -> A[192]
...
Lane 63 -> A[4032]
```

对应地址之间相隔：

\\[ 64\times4=256\ Bytes \\]

虽然每个 Lane 仍然只需要读取 4 Bytes，但由于访问地址分散，GPU 可能需要发起更多 Memory Transactions。

最终可能产生：

- 更多的内存事务。
- 更多的内存带宽浪费。
- 更低的 Effective Memory Bandwidth。
- 更长的 Kernel Execution Time。

注意：实际事务数量取决于 GPU 的 Cache Line、Memory Segment、访问对齐情况和 Cache 命中情况，并不是简单地一个 Lane 对应一个事务。

---

## 2. Memory Coalescing 和 Stride 有什么关系？

这是理解 Memory Coalescing 的关键。

假设有一个二维矩阵：

\\[ A= \begin{bmatrix} A\_{0,0}&A\_{0,1}&A\_{0,2}&A\_{0,3}\\\ A\_{1,0}&A\_{1,1}&A\_{1,2}&A\_{1,3}\\\ A\_{2,0}&A\_{2,1}&A\_{2,2}&A\_{2,3} \end{bmatrix} \\]

假设它采用 Row-major（行优先）存储。

那么内存布局是：

```
A[0,0] A[0,1] A[0,2] A[0,3]
A[1,0] A[1,1] A[1,2] A[1,3]
A[2,0] A[2,1] A[2,2] A[2,3]
```

对应的内存地址是连续的。

假设有 4 个 Thread：

**访问方式 A：每个 Thread 访问同一行的不同列**

```
Thread 0 -> A[0,0]
Thread 1 -> A[0,1]
Thread 2 -> A[0,2]
Thread 3 -> A[0,3]
```

这是连续访问，容易实现 Coalescing。

**访问方式 B：每个 Thread 访问同一列的不同行**

```
Thread 0 -> A[0,0]
Thread 1 -> A[1,0]
Thread 2 -> A[2,0]
Thread 3 -> A[3,0]
```

由于每行之间的间隔是 4 个元素，因此访问地址不连续。

这就是典型的 Strided Access。

对于 Row-major 矩阵：

- 沿着最后一个维度（Column）访问，通常更容易 Coalesced。
- 沿着第一个维度（Row）访问，通常更容易产生 Strided Access。

当然，这还需要结合 Thread 到数据元素的映射方式判断。

## 3. 在 Triton Kernel 中如何实现 Memory Coalescing？

以 Triton 的二维矩阵访问为例：

```
@triton.jit
def load_kernel(
    A,
    M: tl.constexpr,
    N: tl.constexpr,
    BLOCK_M: tl.constexpr,
    BLOCK_N: tl.constexpr,
):
    pid = tl.program_id(0)

    rows = pid * BLOCK_M + tl.arange(0, BLOCK_M)
    cols = tl.arange(0, BLOCK_N)

    # 计算二维矩阵的线性地址
    offsets = rows[:, None] * N + cols[None, :]

    x = tl.load(A + offsets)
```

这里：

```
offsets = rows[:, None] * N + cols[None, :]
```

假设矩阵是 Row-major。

Triton 会根据 Tensor 的布局（Layout），决定不同 Lane 如何映射到这些元素。

如果相邻 Lane 访问相邻的 `cols`，那么就更容易实现 Coalesced Access。

这里有一个非常重要的区别：

**Triton 中，Tensor 的逻辑维度顺序不等于 Thread 的实际内存访问顺序。**

Triton 的 Layout（例如 `BlockedLayout`）会决定：

- 每个 Thread 负责多少元素。
- 不同 Thread 之间如何分配元素。
- Warp / Wavefront 内各个 Lane 如何映射到矩阵坐标。

因此，即使两个 Kernel 的 `tl.load()` 看起来完全一样，它们也可能因为 Layout 不同而产生不同的 Memory Coalescing 效果。

---

## 4. Memory Coalescing 和 Tiling 有什么关系？

这两个概念经常一起出现，但解决的问题不同。

| 概念                | 主要解决的问题                              |
| ----------------- | ------------------------------------ |
| Tiling            | 如何提高数据复用率，减少重复的 Global Memory Access |
| Memory Coalescing | 如何让一次 Global Memory Access 尽可能高效     |
| Split-K           | 如何增加并行度                              |
| Register Tiling   | 如何利用寄存器保存和复用数据                       |

举个例子：

假设一个 GEMM Kernel 使用：

```
BLOCK_M = 64
BLOCK_N = 128
BLOCK_K = 32
```

Tiling 决定每个 Thread Block 负责多大的矩阵区域。

Memory Coalescing 则决定这个 Thread Block 内的各个 Lane 如何访问 A 和 B 的数据。

一个好的 GEMM Kernel 通常需要同时做到：

1. 合理的 Tile Size。
2. 良好的数据复用。
3. Coalesced Global Memory Access。
4. 合理的 Register Usage。
5. 高效利用 MFMA。

## 5. 如何判断 Kernel 是否存在 Memory Coalescing 问题？

在 AMD ROCm 环境中，可以重点关注：

- Global Memory Load / Store 的事务数量。
- L1 / L2 Cache 命中率。
- HBM Read / Write Bandwidth。
- Memory Access Efficiency。
- 实际 Memory Traffic 与理论 Memory Traffic 的差距。

可以使用 ROCm Compute Profiler（RCP）分析这些指标。

一个值得关注的现象：

假设某 Kernel 理论上只需要读取 1 GB 数据，但实际发生了远高于 1 GB 的内存事务。

这可能意味着：

- 存在 Uncoalesced Access。
- 存在重复加载。
- Cache 行利用率较低。
- 存在额外的临时数据访问。

不过，实际流量还受到 Cache、数据复用和其他访存行为影响，不能仅凭这个现象就断定是 Coalescing 问题。

---

## 6. 最后总结

记住下面这个例子就基本理解了 Memory Coalescing：

(Good) **Coalesced Access**

相邻 Lane 访问相邻的内存地址

较少的 Memory Transactions，更高的带宽利用率。

---

(Bad) **Uncoalesced Access**

相邻 Lane 访问相距较远的内存地址

可能需要更多 Memory Transactions，降低有效带宽。

**对于你做 Triton / AMD GPU Kernel 优化，我建议记住一个核心原则：**

> 尽量让相邻的 Lane 访问相邻的内存地址。

尤其是在优化 GEMM 时，要同时考虑 Matrix Layout、Thread Mapping 和 Memory Access Pattern。它们直接决定了 Kernel 能否高效利用 GPU 的 Global Memory Bandwidth。

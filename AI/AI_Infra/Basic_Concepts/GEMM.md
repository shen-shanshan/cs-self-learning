在大模型推理（LLM Inference）中，**GEMM 是 General Matrix Multiplication 的缩写，即通用矩阵乘法**。

它是 GPU 上最核心、最常见的计算操作之一，也是大模型推理过程中最重要的计算负载之一。

## 1. GEMM 到底是什么？

GEMM 本质上就是矩阵乘法，通常表示为：

\\[ C = \alpha AB + \beta C \\]

其中：

- \\(A\\)：矩阵 A，维度为 \\(M \times K\\)
- \\(B\\)：矩阵 B，维度为 \\(K \times N\\)
- \\(C\\)：输出矩阵，维度为 \\(M \times N\\)
- \\(\alpha\\)、\\(\beta\\)：标量系数

如果忽略系数，就是最普通的矩阵乘法：

\\[ C = AB \\]

例如：

\\[ (128 \times 4096) \times (4096 \times 11008) \\]

最终得到一个 \\(128 \times 11008\\) 的矩阵。

## 2. GEMM 在大模型推理中有什么作用？

大模型中的绝大多数 Linear Layer（线性层）都可以转化为 GEMM。

例如，一个 Transformer 中的 FFN（Feed Forward Network）：

\\[ Y = XW_1 \\]\\[ Z = \text{Activation}(Y) \\]\\[ O = ZW_2 \\]

其中：

- \\(X\\)：输入 Hidden States
- \\(W_1, W_2\\)：模型权重
- \\(Y, Z, O\\)：中间计算结果

这里的两次矩阵乘法就是 GEMM。

类似地，Attention 中的 Q、K、V Projection，以及 Attention Output Projection，也都涉及 GEMM。

## 3. GEMM 在 Prefill 和 Decode 阶段有什么区别？

这是大模型推理中非常重要的一个区别。

假设模型的 Hidden Size 是 4096，Batch Size 是 128。

| 阶段          | GEMM 的典型形状                            | 特点                    |
| ----------- | ------------------------------------- | --------------------- |
| Prefill     | \\([128, 4096] \times [4096, 4096]\\) | M 较大，计算密集             |
| Decode      | \\([128, 4096] \times [4096, 4096]\\) | 这里假设 Batch Size 为 128 |
| Decode（单请求） | \\([1, 4096] \times [4096, 4096]\\)   | M 很小，通常更容易受到内存带宽限制    |

注意：这里的 M 对应输入矩阵的行数，通常与参与计算的 Token 数量有关。

- **Prefill**：一次处理大量 Prompt Tokens，通常能够充分利用 GPU 的计算能力。
- **Decode**：每个请求每次通常只生成一个 Token，因此 GEMM 的 M 维度往往较小，权重读取开销相对突出。

这也是为什么大模型推理中，Prefill 和 Decode 往往需要不同的 Kernel 优化策略。

## 4. GEMM 和相关概念的区别

在 GPU Kernel 优化中，你经常会看到以下几个概念：

| 概念          | 含义                                          |
| ----------- | ------------------------------------------- |
| GEMM        | 通用矩阵乘法                                      |
| GEMV        | General Matrix-Vector Multiplication，矩阵向量乘法 |
| GEMM Kernel | 专门执行矩阵乘法的 GPU Kernel                        |
| Tensor Core | GPU 中专门加速矩阵运算的硬件计算单元                        |
| Tiling      | 将大矩阵切分成小块进行计算                               |
| MFMA        | AMD GPU 上的 Matrix Fused Multiply-Add 指令     |
| MMA         | Matrix Multiply-Accumulate，矩阵乘累加操作          |

特别是你在研究 AMD GPU / ROCm / Triton Kernel 时，可能会经常看到 MFMA、GEMM、Tiling 等术语。

**一个值得记住的概念：**

GEMM 通常是计算任务（What），而 GEMM Kernel 是具体的实现方式（How）。

例如，同样一个 GEMM，可以通过不同的 Tile Size、Warp 数量、数据类型（FP16、BF16、FP8 等）和内存访问策略实现，从而产生非常不同的性能。

这也是 GPU Kernel Optimization 的核心工作之一。

---

在 GPU Kernel 优化，尤其是大模型推理中的 GEMM 优化中，**Tiling 和 Split-K 是两种非常重要的优化技术**。

它们解决的问题不太一样：

- **Tiling**：把一个大矩阵乘法拆成多个小矩阵块（Tiles），让 GPU 更高效地执行计算。
- **Split-K**：把矩阵乘法中 K 维度的计算拆成多个部分，让多个 GPU Thread Block 并行计算，增加并行度。

下面结合 GEMM 来解释。

## 1. Tiling：矩阵分块

假设我们有一个 GEMM：

\\[ C\_{M\times N}=A\_{M\times K}\times B\_{K\times N} \\]

如果直接计算整个输出矩阵 C，计算规模可能非常大。

因此，我们将输出矩阵 C 切分成多个小块（Tile），每个 Tile 由一个 GPU Thread Block 负责计算。

例如：

- M = 128
- N = 128
- K = 64
- Tile Size = 32 × 32

那么，输出矩阵 C 就被划分成：

\\[ \frac{128}{32}\times\frac{128}{32}=16 \\]

个 Tile。

每个 Tile 的计算过程可以表示为：

\\[ C\_{tile}=A\_{tile}\times B\_{tile} \\]

实际上，K 维度通常也会进行分块：

\\[ C\_{tile}=\sum\_{k=0}^{K/B_K-1} A\_{tile,k}B\_{k,tile} \\]

这里的 \\(B_K\\) 是 K 维度上的分块大小。

**Tiling 的主要好处：**

1. 提高数据复用率：一个 Tile 加载到 GPU 的寄存器或 Shared Memory 后，可以重复参与计算。
2. 减少 Global Memory 访问：避免反复从显存读取相同数据。
3. 提高并行度：不同 Tile 可以分配给不同的 Thread Block 并行执行。
4. 更好地利用 GPU 的 Tensor Core（NVIDIA）或 MFMA（AMD）。

### Triton 中的 Tiling

例如：

```
@triton.jitdef matmul_kernel(    A, B, C,    M: tl.constexpr,    N: tl.constexpr,    K: tl.constexpr,    BLOCK_M: tl.constexpr,    BLOCK_N: tl.constexpr,    BLOCK_K: tl.constexpr,):    pid_m = tl.program_id(0)    pid_n = tl.program_id(1)    # 当前 Thread Block 负责的输出 Tile    offs_m = pid_m * BLOCK_M + tl.arange(0, BLOCK_M)    offs_n = pid_n * BLOCK_N + tl.arange(0, BLOCK_N)    # 沿 K 维度循环计算    acc = tl.zeros((BLOCK_M, BLOCK_N), dtype=tl.float32)    for k in range(tl.cdiv(K, BLOCK_K)):        # 加载 A、B 的 Tile        # 执行矩阵乘累加        ...    # 写回输出    ...
```

这里：

- `BLOCK_M`：每个 Thread Block 负责的输出 Tile 的 M 维度。
- `BLOCK_N`：每个 Thread Block 负责的输出 Tile 的 N 维度。
- `BLOCK_K`：每次沿 K 维度处理的大小。

这三个参数通常是 GEMM Kernel 调优时最重要的参数之一。

## 2. Split-K：沿 K 维度拆分计算

理解 Split-K，需要先理解 GEMM 的计算方式。

\\[ C\_{M\times N}=A\_{M\times K}\times B\_{K\times N} \\]

对于 C 中的每个元素：

\\[ C\_{i,j}=\sum\_{k=0}^{K-1}A\_{i,k}B\_{k,j} \\]

可以看到，每个输出元素都需要沿 K 维度进行累加。

**Split-K 的核心思想就是：把 K 维度拆成多个部分，让多个 Thread Block 分别计算部分结果，最后再把结果合并。**

例如，假设：

- M = 16
- N = 16
- K = 4096
- Split-K = 4

将 K 维度拆成 4 份，每份 K = 1024。

原本：

\\[ C=A\_{M\times4096}B\_{4096\times N} \\]

拆分后：

\\[ C_0=A_0B_0 \\]\\[ C_1=A_1B_1 \\]\\[ C_2=A_2B_2 \\]\\[ C_3=A_3B_3 \\]

最终：

\\[ C=C_0+C_1+C_2+C_3 \\]

这里每个 \\(C_i\\) 都是一个 \\(M\times N\\) 的部分结果。

### 为什么需要 Split-K？

考虑一个 GEMM：

\\[ M=16,\quad N=16,\quad K=4096 \\]

如果 Tile Size 为 16 × 16，那么整个输出矩阵只有一个 Tile。

也就是说，可能只有一个 Thread Block 在执行计算。

对于拥有大量计算单元的 GPU 来说，这会造成严重的 GPU 利用率不足。

使用 Split-K = 4 后：

- 原本：1 个 Thread Block。
- Split-K 后：4 个 Thread Block。
- 每个 Thread Block 计算一部分 K。
- 最后将 4 个部分结果进行 Reduction。

这样可以增加 GPU 的并行度。

**Split-K 特别适合 M、N 较小，但 K 较大的 GEMM。**

不过，它也有代价：

1. 需要额外的临时存储空间保存部分结果（取决于实现方式）。
2. 需要额外的 Reduction 操作。
3. 增加 Global Memory 读写。
4. 可能引入额外的 Kernel Launch 开销。

因此，Split-K 并不是越大越好。

## 3. Tiling 和 Split-K 的核心区别

| 对比维度   | Tiling            | Split-K                 |
| ------ | ----------------- | ----------------------- |
| 核心思想   | 将矩阵划分成小块          | 将 K 维度拆分成多个部分           |
| 主要目的   | 提高数据复用率和计算效率      | 增加并行度                   |
| 主要划分维度 | M、N，通常也包括 K       | K                       |
| 并行方式   | 不同输出 Tile 并行计算    | 同一个输出 Tile 的不同 K 分段并行计算 |
| 结果合并   | 通常不需要额外 Reduction | 通常需要 Reduction          |
| 典型适用场景 | 大多数 GEMM          | M、N 较小而 K 较大的 GEMM      |

## 4. 两者可以同时使用吗？

(可以，而且很常见)

实际上，Tiling 和 Split-K 并不冲突。

例如：

\\[ M=64,\quad N=128,\quad K=8192 \\]

假设：

- BLOCK_M = 32
- BLOCK_N = 32
- Split-K = 4

那么：

输出矩阵的 Tile 数量：

\\[ \frac{64}{32}\times\frac{128}{32}=8 \\]

每个输出 Tile 再拆分成 4 个 K 分区。

因此，总共可以产生：

\\[ 8\times4=32 \\]

个独立的部分计算任务。

最后再对每个输出 Tile 的 4 个部分结果进行 Reduction。

## 5. 在实际 Kernel Optimization 中怎么选择？

可以记住下面这个经验：

| GEMM 特征                        | 优化方向                            |
| ------------------------------ | ------------------------------- |
| M、N、K 都比较大                     | 优先优化 Tiling、数据复用和计算效率           |
| M、N 较小，K 很大                    | 考虑 Split-K，增加并行度                |
| M 很小（例如 Decode，Batch Size = 1） | 重点关注内存带宽和并行度                    |
| M 较大（例如 Prefill）               | 通常有足够的输出 Tile 并行度，Split-K 未必有帮助 |

对于你研究的 Triton / AMD GPU Kernel，建议重点关注三个参数：

- `BLOCK_M`
- `BLOCK_N`
- `BLOCK_K`

以及 Split-K 对应的分区数量。

**最重要的一点：Tiling 主要解决如何高效地计算，Split-K 主要解决如何让更多 GPU 计算单元参与计算。**

这两个概念是理解 GEMM Kernel 性能优化的基础。

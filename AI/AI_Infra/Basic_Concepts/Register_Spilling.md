在 GPU Kernel 优化中，**寄存器溢出（Register Spilling）** 是指：一个 Thread 需要使用的寄存器数量超过了硬件或编译器能够分配给它的寄存器预算，导致部分原本应该存放在寄存器中的数据被转移到其他存储空间（通常是 GPU 的 Local Memory）。

它是 GPU Kernel 性能优化中非常重要的一个问题，尤其是在 Triton GEMM、MFMA 和大型矩阵计算中。

## 1. 先理解 GPU Register 是什么

GPU Register（寄存器）是 GPU 上速度非常快的存储资源。

每个 GPU Thread 都可以使用一定数量的寄存器，用来保存计算过程中的临时数据，例如：

- 中间计算结果
- 矩阵 Tile
- 循环变量
- 地址和索引
- Accumulator（累加器）

例如：

```
acc = 0.0

for k in range(K):
    acc += A[k] * B[k]
```

这里的 `acc` 就是一个典型的临时变量，通常会被编译器放在寄存器中。

寄存器的特点：

- 访问速度非常快。
- 直接供 GPU 计算单元使用。
- 每个 Compute Unit（AMD）或 SM（NVIDIA）拥有有限的寄存器资源。
- 寄存器资源需要由多个并发执行的 Thread / Wavefront / Warp 共享。

## 2. 什么是 Register Spilling？

假设一个 GPU Thread 可用的寄存器资源有限。

假设：

- 编译器希望给一个 Thread 分配 128 个寄存器。
- 但由于寄存器预算或硬件资源限制，实际无法满足。
- 有一部分临时变量无法放入寄存器。

编译器就可能将这些变量存放到 Local Memory 中。

这个过程就是 Register Spilling。

为什么性能会下降？

因为 Local Memory 虽然名字叫 Local Memory，但它并不是物理上独立的高速片上存储器。

在 AMD 和 NVIDIA GPU 上，Local Memory 通常映射到 GPU 的显存地址空间，并通过缓存层级访问。

因此：

| 存储类型                            | 特点                      | 访问开销 |
| ------------------------------- | ----------------------- | ---- |
| Register                        | Thread 私有，片上            | 极低   |
| Shared Memory（NVIDIA）/ LDS（AMD） | Block/Wave 共享，片上        | 较低   |
| Local Memory                    | Thread 私有的逻辑地址空间，通常位于显存 | 通常较高 |
| Global Memory                   | GPU 全局显存                | 较高   |

注意：Local Memory 是逻辑概念，不代表数据一定每次都访问 HBM，实际访问可能命中 Cache。

## 3. 为什么 Triton Kernel 容易出现寄存器溢出？

一个常见原因是：**每个 Thread 需要处理的数据量太大。**

例如，你在 Triton GEMM 中使用：

```
BLOCK_M = 128
BLOCK_N = 128
BLOCK_K = 64
```

每个 Thread Block 需要处理的输出 Tile 是：

\\[ 128\times128=16384 \\]

个元素。

如果累加器使用 FP32，那么整个 Accumulator Tile 需要：

\\[ 128\times128\times4=65536\ Bytes \\]

即 64 KB。

这些数据需要分配到该 Thread Block 中所有 Thread 的寄存器资源里。

如果每个 Thread 分配的寄存器太多，就可能出现：

- Register Pressure 增大。
- Occupancy 下降。
- Register Spilling。
- Kernel 性能下降。

尤其是 Triton 中，以下情况容易导致寄存器压力增大：

1. 增大 `BLOCK_M`、\`BLOCK_N\`。
2. 增大每个 Thread 负责的数据量。
3. 使用大量临时变量。
4. 同时保留多个大型 Tensor 中间结果。
5. 展开过多循环（Loop Unrolling）。

## 4. Register Spilling 和 Occupancy 有什么关系？

这两个概念经常一起出现，但它们不是一回事。

**Occupancy（占用率）** 指的是一个 Compute Unit / SM 上，实际驻留的 Wavefront / Warp 数量相对于硬件最大支持数量的比例。

例如：

假设一个 AMD Compute Unit 最多支持 40 个 Wavefront。

| 情况   | 驻留 Wavefront 数量 | Occupancy |
| ---- | --------------- | --------- |
| 情况 A | 40              | 100%      |
| 情况 B | 20              | 50%       |
| 情况 C | 10              | 25%       |

如果每个 Thread 使用的寄存器越多，那么每个 Compute Unit 能同时驻留的 Wavefront 通常就越少。

因此：

\\[ \text{Register Usage}\uparrow \Rightarrow \text{Occupancy}\downarrow \\]

不过，寄存器使用量增加并不一定意味着发生 Spilling。

这是两个不同的问题：

- **Register Pressure**：寄存器需求较大。
- **Register Spilling**：寄存器不够用，部分数据被转移到其他存储空间。

即使没有 Spilling，过高的 Register Usage 也可能导致 Occupancy 下降。

## 5. 怎么判断 Triton Kernel 是否发生 Register Spilling？

在 AMD ROCm 环境下，可以重点关注以下指标。

| 指标             | 含义                                      |
| -------------- | --------------------------------------- |
| VGPR           | Vector General Purpose Register，向量通用寄存器 |
| SGPR           | Scalar General Purpose Register，标量通用寄存器 |
| Scratch Memory | 编译器使用的临时存储空间，可能用于 Spilling              |
| Occupancy      | GPU 上并发执行的 Wavefront 占比                 |

其中最值得关注的是 VGPR 和 Scratch Memory。

例如，编译 Kernel 后，如果发现：

```
VGPRs: 256
SGPRs: 64
Scratch Size: 1024 bytes
```

说明这个 Kernel 使用了较多 VGPR，并且分配了 Scratch Space。

但要注意：Scratch Space 非零是发生 Spilling 的重要线索，不一定代表所有 Scratch 都来自寄存器溢出，最好结合编译器生成的汇编和 Profiler 信息确认。

## 6. 如果发生 Register Spilling，怎么优化？

常见方法如下：

| 优化方法                | 原理                   |
| ------------------- | -------------------- |
| 减小 `BLOCK_M`        | 减少每个 Block 的数据量      |
| 减小 `BLOCK_N`        | 减少 Accumulator 的规模   |
| 调整 `BLOCK_K`        | 减少每次计算需要保留的临时数据      |
| 减少临时变量              | 降低 Register Pressure |
| 避免过度 Loop Unrolling | 减少同时存活的变量            |
| 调整 `num_warps`      | 改变工作分配和寄存器分布         |

需要注意：增加 `num_warps` 有时可以降低每个 Thread 的数据负担，但也可能增加资源消耗，不能保证一定改善性能。

---

## 7. 总结：几个容易混淆的概念

| 概念                | 含义                          | 对性能的影响           |
| ----------------- | --------------------------- | ---------------- |
| Register Pressure | 寄存器需求较大                     | 可能降低 Occupancy   |
| Register Spilling | 寄存器不够，数据溢出到 Local Memory    | 通常会降低性能          |
| Occupancy         | GPU 并发驻留的 Warp/Wavefront 比例 | 影响延迟隐藏能力         |
| Scratch Memory    | 编译器使用的临时存储空间                | 可能意味着存在 Spilling |

**对于 Triton GEMM Kernel，我建议你特别关注一个关系：**

\\[ \boxed{ BLOCK\\\_M\times BLOCK\\\_N \rightarrow Accumulator\ Size \rightarrow Register\ Pressure \rightarrow Occupancy } \\]

也就是说，增大 Tile Size 虽然可能提高数据复用率，但同时也可能增加寄存器压力，甚至导致 Spilling。

这就是为什么 GEMM Kernel 调优不能简单地认为 Tile 越大越好，而是需要在 **Data Reuse、Register Usage、Occupancy 和 Compute Throughput** 之间寻找平衡。

在 GPU Kernel 性能优化中，**Roofline Estimate（屋顶线估算）** 是一种用于评估 Kernel 理论性能上限、判断性能瓶颈，并指导优化方向的方法。

它主要回答三个问题：

1. 这个 Kernel 理论上最快能跑多快？
2. 当前 Kernel 是 Compute-bound（计算受限）还是 Memory-bound（内存带宽受限）？
3. 当前 Kernel 的性能距离 GPU 的理论上限还有多远？

对于你正在研究的 Triton / ROCm / AMD GPU Kernel，Roofline Model 是非常值得掌握的性能分析工具。

## 1. 什么是 Roofline Model？

Roofline Model（屋顶线模型）将 GPU 的性能上限表示为计算能力和内存带宽共同决定的上界。

核心公式是：

\\[ \boxed{ P\_{\text{achievable}} = \min(P\_{\text{peak}}, BW\_{\text{peak}}\times AI) } \\]

其中：

| 参数                           | 含义                                   |
| ---------------------------- | ------------------------------------ |
| \\(P\_{\text{peak}}\\)       | GPU 理论峰值计算能力（FLOP/s）                 |
| \\(BW\_{\text{peak}}\\)      | GPU 理论峰值内存带宽（Byte/s）                 |
| \\(AI\\)                     | Arithmetic Intensity，算术强度（FLOP/Byte） |
| \\(P\_{\text{achievable}}\\) | Kernel 理论可达到的性能上限                    |

这里最重要的是 Arithmetic Intensity（AI）。

\\[ \boxed{ AI = \frac{\text{Total FLOPs}}{\text{Total Memory Bytes}} } \\]

也就是：

**每从内存中读取或写入 1 Byte 数据，能够完成多少次浮点运算。**

AI 越高，意味着数据复用率越高，Kernel 越有可能充分利用 GPU 的计算能力。

## 2. Roofline Model 的直观理解

Roofline 图有两个核心区域：

**Memory-bound 区域**

- 位于 Roofline 的斜线部分。
- 性能上限由内存带宽决定。
- 增加计算单元或提高计算峰值，对性能帮助不大。
- 优化重点是减少 Global Memory Access、提高数据复用率。

**Compute-bound 区域**

- 位于 Roofline 的水平线部分。
- 性能上限由 GPU 的计算能力决定。
- 优化重点是提高计算单元利用率、优化指令和矩阵计算效率。

两者的交界点叫做 **Ridge Point（屋顶线拐点）**：

\\[ AI\_{\text{ridge}} = \frac{P\_{\text{peak}}}{BW\_{\text{peak}}} \\]

判断方法：

- \\(AI < AI\_{\text{ridge}}\\)：理论上 Memory-bound。
- \\(AI > AI\_{\text{ridge}}\\)：理论上 Compute-bound。

这里的 Memory-bound / Compute-bound 是基于 Roofline 假设的初步判断，实际 Kernel 还可能受到 Launch Overhead、Occupancy、Latency 等因素限制。

## 3. 优化 Kernel 时，怎么做 Roofline Estimate？

实际工作中，我建议按照下面五个步骤进行。

### Step 1：确定 GPU 的硬件理论峰值

首先需要知道目标 GPU 的两个硬件指标：

1. Peak Compute Throughput：理论峰值计算能力。
2. Peak Memory Bandwidth：理论峰值内存带宽。

例如，假设某 GPU 的硬件参数如下（这里使用假设值，仅用于演示）：

| 指标                     | 数值            |
| ---------------------- | ------------- |
| FP16/BF16 Peak Compute | 1,000 TFLOP/s |
| HBM Bandwidth          | 5 TB/s        |

那么 Ridge Point：

\\[ AI\_{\text{ridge}} = \frac{1000\ TFLOP/s}{5\ TB/s} \\]\\[ \boxed{AI\_{\text{ridge}}=200\ FLOP/Byte} \\]

这意味着：

- AI 小于 200：倾向于 Memory-bound。
- AI 大于 200：倾向于 Compute-bound。

注意：计算峰值必须使用与你的 Kernel 实际执行指令相匹配的数据类型和硬件计算路径。例如，FP16/BF16 MFMA 的峰值不能直接拿 FP32 Vector ALU 的峰值代替。

### Step 2：计算 Kernel 的 FLOPs

需要统计这个 Kernel 一共执行了多少次浮点运算。

以 GEMM 为例：

\\[ C\_{M\times N}=A\_{M\times K}\times B\_{K\times N} \\]

每个输出元素需要：

- K 次乘法
- K 次加法

因此：

\\[ \boxed{FLOPs=2MNK} \\]

例如：

\\[ M=128,\quad N=4096,\quad K=4096 \\]

那么：

\\[ FLOPs=2\times128\times4096\times4096 \\]\\[ =4,294,967,296 \\]

也就是约 4.295 GFLOPs。

这里采用常见约定：一次 Fused Multiply-Add（FMA）计为 2 FLOPs。

### Step 3：计算 Kernel 的 Memory Traffic

这一步往往最容易出错。

需要统计 Kernel 从内存读取和向内存写入了多少数据。

对于上面的 GEMM：

\\[ C=AB \\]

假设：

- A、B、C 都是 FP16。
- 每个元素占 2 Bytes。
- A、B 各读取一次。
- C 写入一次。

那么理论上的数据传输量为：

\\[ Bytes=2(MK+KN+MN) \\]

代入：

\\[ Bytes=2(128\times4096+ 4096\times4096+ 128\times4096) \\]\\[ =34,078,720\ Bytes \\]

约为 34.08 MB（十进制）。

注意：这里假设 A、B 从 HBM 各读取一次，忽略 Cache、临时 Buffer、重复访存等因素。这是一个理想化的算法级估算。

真实 Kernel 的 Memory Traffic 可能与这个值不同：

- 数据重复加载可能增加流量。
- Cache Hit 可以减少 HBM 访问。
- Tiling 可以提高数据复用率。
- 临时结果写入和读取也会增加流量。

因此，要区分两个概念：

- **Theoretical Memory Traffic**：根据算法和数据规模估算。
- **Actual Memory Traffic**：通过 Profiler 测量实际发生的内存传输。

AMD ROCm 文档也强调，Arithmetic Intensity 可以通过理论计算或实际测量获得。

### Step 4：计算 Arithmetic Intensity

现在我们已经知道：

\\[ FLOPs=4.295\times10^9 \\]\\[ Bytes=34.079\times10^6 \\]

所以：

\\[ AI=\frac{FLOPs}{Bytes} \\]\\[ \boxed{AI\approx126\ FLOP/Byte} \\]

前面计算出的 Ridge Point 是 200 FLOP/Byte。

因此：

\\[ 126 < 200 \\]

从理想化的 HBM Roofline 模型来看，这个 GEMM 属于 Memory-bound。

### Step 5：估算理论性能上限

现在可以计算这个 Kernel 的理论性能上限：

\\[ P\_{\text{achievable}}= \min(P\_{\text{peak}}, BW\_{\text{peak}}\times AI) \\]

代入：

\\[ P\_{\text{achievable}}= \min(1000,\ 5\times126) \\]

这里的单位是 TFLOP/s：

\\[ \boxed{P\_{\text{achievable}}=630\ TFLOP/s} \\]

也就是说：

虽然 GPU 的 FP16/BF16 理论峰值计算能力是 1,000 TFLOP/s，但根据这个 GEMM 的算术强度和 HBM 带宽，其理想化 Roofline 上限只有 630 TFLOP/s。

这就是 Roofline Estimate 最核心的价值：**不是直接拿 GPU 的峰值算力作为 Kernel 的性能目标，而是根据 Kernel 自身的计算量和内存访问量，估算一个更合理的性能上限。**

---

## 4. 实际优化时，如何利用 Roofline Estimate 指导优化？

上面的计算只是理论分析。真正做 Kernel Optimization 时，还需要把理论值和实际 Benchmark 结果放在一起比较。

假设我们通过 Benchmark 测得：

- Kernel Execution Time = 10 μs
- FLOPs = 4.295 GFLOPs
- 理论 Memory Traffic = 34.08 MB

那么实际计算吞吐量：

\\[ P\_{\text{actual}}= \frac{4.295\times10^9}{10\times10^{-6}} \\]\\[ =429.5\ TFLOP/s \\]

实际有效带宽：

\\[ BW\_{\text{actual}}= \frac{34.08\times10^6}{10\times10^{-6}} \\]\\[ =3.408\ TB/s \\]

整理一下：

| 指标                   | 理论值           | 实际值           |
| -------------------- | ------------- | ------------- |
| Compute Throughput   | 1000 TFLOP/s  | 429.5 TFLOP/s |
| Memory Bandwidth     | 5 TB/s        | 3.408 TB/s    |
| Arithmetic Intensity | 126 FLOP/Byte | 126 FLOP/Byte |

从这里可以得到：

- 实际带宽利用率：3.408 / 5 = 68.2%
- 实际计算吞吐量：429.5 / 1000 = 42.95%

因为这个 Kernel 在理想 Roofline 模型中属于 Memory-bound，所以优化时应该优先考虑：

1. 提高 Global Memory Access 效率。
2. 优化 Tiling，提高数据复用率。
3. 减少不必要的 Global Memory Load / Store。
4. 优化 Memory Coalescing。
5. 减少临时 Buffer 的读写。

如果实际带宽已经非常接近硬件峰值，那么继续优化计算指令（例如增加 MFMA 指令吞吐量）可能不会带来明显收益。

反过来，如果 Kernel 属于 Compute-bound，就应该重点考虑：

- 提高 MFMA / Matrix Core 利用率。
- 优化 Instruction Scheduling。
- 调整 BLOCK_M、BLOCK_N、BLOCK_K。
- 优化 Occupancy。
- 减少不必要的指令和计算。

## 5. 实际工作中需要注意的几个问题

### 5.1 Roofline 不只有 HBM Roofline

GPU 有多级存储结构：

例如：

- HBM
- L2 Cache
- L1 Cache / Shared Memory
- Registers

不同层级的带宽差异很大。

因此，严格来说，可以针对不同的 Memory Hierarchy 分别建立 Roofline：

- HBM Roofline
- L2 Roofline
- L1 Roofline

例如，一个 Kernel 的数据全部命中 L2 Cache，那么它实际受到的约束可能是 L2 带宽，而不是 HBM 带宽。

所以做实际性能分析时，要注意 Roofline 中的 Memory Traffic 究竟对应哪一级存储。

### 5.2 不要把所有 Kernel 都简单归类为 Compute-bound 或 Memory-bound

Roofline 是一个简化模型，它主要考虑计算吞吐量和内存带宽。

实际 GPU Kernel 还可能受到：

- Launch Overhead
- Occupancy
- Register Pressure
- Instruction Latency
- Synchronization Overhead
- Memory Access Latency

等因素限制。

AMD ROCm 的性能分析文档也将 Overhead-bound 作为独立的性能瓶颈类型。

### 5.3 FLOPs 和 Memory Bytes 要使用一致的统计口径

例如：

- FMA 按 2 FLOPs 计算。
- FP16 一个元素是 2 Bytes。
- FP8 一个元素是 1 Byte。
- 累加器可能使用 FP32。
- 不能把理论数据量直接当成实际 HBM Traffic。

尤其是使用 Triton、MFMA 和 FP8 Kernel 时，需要明确实际执行的计算指令和数据类型。

## 6. 在 AMD ROCm 环境下，使用什么工具？

如果你是在 AMD GPU 上优化 Triton Kernel，可以关注以下工具：

| 工具                    | 用途                          |
| --------------------- | --------------------------- |
| rocprofv3             | GPU Kernel Profiling，采集性能指标 |
| ROCm Compute Profiler | 分析 GPU 硬件指标和性能瓶颈            |
| TraceLens             | 分析性能数据，包括 Roofline 相关指标     |

参考资料：

- AMD ROCm — Understanding GPU Performance
- AMD ROCm — Performance Analysis Glossary
- AMD TraceLens — Performance Report Columns

---

## 7. 总结：优化 Kernel 时的 Roofline Estimate Checklist

实际优化一个 Kernel 时，可以按照下面的流程操作：

1. **确定 GPU 硬件参数**

   获取目标 GPU 的 Peak FLOPS 和 Peak Memory Bandwidth。
2. **计算 Kernel 的工作量**

   计算 Total FLOPs 和 Total Memory Traffic。
3. **计算 Arithmetic Intensity**

   \\[ AI=FLOPs/Bytes \\]
4. **计算 Ridge Point**

   \\[ AI\_{\text{ridge}}=PeakFLOPS/PeakBW \\]
5. **判断理论瓶颈**

   比较 AI 和 Ridge Point，判断 Compute-bound 或 Memory-bound。
6. **Benchmark + Profiler**

   获取实际 Execution Time、FLOPS/s、Memory Bandwidth。
7. **选择优化方向**

   根据瓶颈决定是否优化 Tiling、Memory Access、MFMA、Occupancy 等。

**我认为你在做 Triton / AMD GPU Kernel 优化时，最值得养成的一个习惯是：**

每次开始优化一个 Kernel 之前，先做一次 Roofline Estimate。

不要一上来就尝试修改 `BLOCK_M`、\`BLOCK_N\`、\`BLOCK_K\` 或者增加 `num_warps`。

先搞清楚：

- 这个 Kernel 理论上受什么限制？
- 当前实际性能距离理论上限还有多少？
- 你的优化究竟是在改善真正的瓶颈，还是在优化一个本来就不构成瓶颈的部分？

这样可以显著减少盲目调参的时间。

**Bank Conflict（Bank 冲突）** 是 GPU Shared Memory（NVIDIA）或 LDS（AMD）访问中的一种性能问题。

简单来说：

> Bank Conflict 指的是：同一个 Warp（NVIDIA）或 Wavefront（AMD）中的多个 Lane 同时访问同一个 Memory Bank 中的不同地址，导致这些访问无法同时完成，需要被拆分成多个 Memory Transactions。

它会增加 Shared Memory / LDS 的访问延迟，降低 Kernel 性能。

这个概念经常出现在 GEMM、Transpose、Attention 等 GPU Kernel 优化中。

## 1. 先理解什么是 Memory Bank

GPU 的 Shared Memory / LDS 并不是一个只能整体访问的存储区域，而是被划分成多个独立的 Memory Bank。

每个 Bank 可以独立处理访问请求。

以一个简化的 NVIDIA GPU 模型为例：

- Shared Memory 被划分为 32 个 Bank。
- 每个 Bank 通常对应连续的 4 Bytes 地址。
- Warp 包含 32 个 Thread。
- 不同 Bank 可以并行处理访问。

假设有一个 Warp，包含 32 个 Thread。

如果：

```
Thread 0  -> Bank 0
Thread 1  -> Bank 1
Thread 2  -> Bank 2
...
Thread 31 -> Bank 31
```

那么 32 个 Thread 分别访问不同的 Bank，可以并行完成。

这就是没有 Bank Conflict 的情况。

## 2. 什么情况下会发生 Bank Conflict？

假设有一个 Shared Memory 数组：

```
__shared__ float shared_mem[32][32];
```

假设每个 Thread 访问：

```
shared_mem[thread_id][0]
```

由于数组采用 Row-major 存储，每一行包含 32 个 FP32 元素。

每行占用：

\\[ 32\times4=128\ Bytes \\]

而 NVIDIA 的简化 Bank 映射规则是：

\\[ Bank\ Index = \left(\frac{Address}{4}\right)\bmod32 \\]

因此：

\\[ Bank\ Index = (thread\\\_id\times32)\bmod32 \\]\\[ =0 \\]

这意味着：

```
Thread 0  -> Bank 0
Thread 1  -> Bank 0
Thread 2  -> Bank 0
...
Thread 31 -> Bank 0
```

所有 Thread 都访问 Bank 0，但访问的是不同地址。

这就发生了严重的 Bank Conflict。

在这个简化例子中，32 个不同地址的访问可能需要串行化为 32 次 Bank 服务。

## 3. 如何解决 Bank Conflict？

一个经典方法是 Padding（填充）。

例如，将：

```
__shared__ float shared_mem[32][32];
```

改成：

```
__shared__ float shared_mem[32][33];
```

为什么这样就可以解决问题？

因为现在每行包含 33 个 FP32 元素。

每行占用：

\\[ 33\times4=132\ Bytes \\]

重新计算 Bank Index：

\\[ Bank\ Index = (thread\\\_id\times33)\bmod32 \\]\\[ =thread\\\_id\bmod32 \\]

于是：

```
Thread 0  -> Bank 0
Thread 1  -> Bank 1
Thread 2  -> Bank 2
...
Thread 31 -> Bank 31
```

原本所有 Thread 都访问 Bank 0，现在每个 Thread 访问不同的 Bank。

这就是通过 Padding 解决 Bank Conflict 的经典方法。

## 4. Bank Conflict 和 Memory Coalescing 有什么区别？

这两个概念非常容易混淆。

它们都与 GPU 内存访问模式有关，但发生在不同的存储层级。

| 对比    | Memory Coalescing  | Bank Conflict         |
| ----- | ------------------ | --------------------- |
| 主要涉及  | Global Memory      | Shared Memory / LDS   |
| 核心问题  | 多个 Lane 的访问能否合并    | 多个 Lane 是否竞争同一个 Bank  |
| 理想访问  | 连续地址               | 不同 Bank               |
| 不理想情况 | 地址分散               | 同 Bank 不同地址           |
| 主要影响  | Global Memory 事务数量 | Shared Memory 访问次数和延迟 |

可以简单记忆：

- Memory Coalescing：关注 Global Memory 访问是否连续。
- Bank Conflict：关注 Shared Memory / LDS 访问是否发生 Bank 竞争。

## 5. 在 AMD GPU / ROCm 中需要注意什么？

你研究的是 AMD GPU 和 Triton Kernel，因此这里有一个特别值得注意的区别：

| NVIDIA        | AMD                   |
| ------------- | --------------------- |
| Shared Memory | LDS（Local Data Share） |
| Warp          | Wavefront             |
| Bank Conflict | LDS Bank Conflict     |
| `__shared__`  | LDS 地址空间              |

AMD GPU 的 LDS 同样采用多 Bank 结构。

Bank 数量、Bank Width、访问粒度以及冲突行为取决于具体 GPU 架构和访问指令，不能直接把 NVIDIA 的 32 Banks、4 Bytes 规则套用到 AMD GPU。

在 AMD ROCm 环境下，可以通过 ROCm Compute Profiler 分析 LDS 相关性能指标，结合生成的 ISA 判断是否存在 Bank Conflict。

---

## 6. 总结：GPU Kernel 优化中的三个重要概念

| 概念                | 解决什么问题                   | 关注点                    |
| ----------------- | ------------------------ | ---------------------- |
| Memory Coalescing | Global Memory 访问效率       | Lane 与连续地址的映射          |
| Bank Conflict     | Shared Memory / LDS 访问冲突 | Lane 与 Memory Bank 的映射 |
| Register Spilling | 寄存器资源不足                  | Register Pressure      |

这三个概念在 GEMM Kernel 优化中尤其重要：

- Global Memory：尽量实现 Coalesced Access。
- Shared Memory / LDS：尽量避免 Bank Conflict。
- Register：控制 Register Pressure，避免 Spilling。

**最终目标是让数据尽可能高效地流经 GPU 的存储层级，减少不必要的访存开销，让 MFMA 等计算单元能够持续工作。**

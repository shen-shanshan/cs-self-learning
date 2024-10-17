# CUDA 基本概念

## Host & Device

CPU 和主存被称为 **Host**，GPU 被称为 **Device**。

## CUDA Context

类似于 CPU 进程的上下文，CUDA context 管理各项资源的生命周期。每个 Context 有自己的地址空间。

在每次运行 cuda 的第一个函数（kernel）时，cuda 会进行 initialization，这时就会创建 cuda context。cuda 函数的调用都需要 context 来管理。

通常情况下，一个 context 对应一个 CPU 进程。但多个 CPU 进程也可以共享同一 GPU context，不同进程的 kernel 和 memcpy 操作在同一 GPU 上并发执行，以实现最大化 GPU 利用率、减少 GPU 上下文的切换时间与存储空间。

在同一个 GPU 上，可能同时存在多个 CUDA Context。但在任意时刻，GPU 上只有一个活动的 context。多个 context 之间按照 time slice 的方式轮流使用 GPU。

CUDA runtime 通过延迟初始化创建 context。即在调用每一个 CUDART 库函数时，它会检查当前是否有 context 存在，若没有 context，那么才自动创建。cuda runtime 将 context 和 device 的概念合并了，即在一个 gpu 上的操作可看成在一个 context 下。也就是说，一个 device 对应一个 context。

## CUDA Stream

CUDA Stream 是指一堆异步的 CUDA 操作，他们按照 host 代码调用的顺序执行在 device 上，Stream 维护了这些操作的顺序，并在所有预处理完成后允许这些操作进入工作队列，同时也可以对这些操作进行一些查询操作。

这些 CUDA 操作包括 host 到 device 的数据传输，调用 kernel 以及其他的由 host 发起由 device 执行的动作。这些操作的执行是异步的，CUDA runtime 会决定这些操作合适的执行时机。我们则可以使用相应的 cuda api 来保证所取得结果是在所有操作完成后获得的。同一个 stream 里的操作是串行的，不同的 stream 则没有此限制。

- **隐式声明 stream（NULL stream）**：默认流，无需显式创建，CUDA 中默认存在；
- **显示声明 stream（non-NULL stream）**：在代码中创建，并对某个 CUDA 操作指明 Stream。

CUDA 开始执行时，会有一个默认的 stream（NULL 流），当代码中没有明确指明操作的 stream 时，就放在默认的 stream 中，这样无关的操作也无法并行。

通过在代码中创建不同的 stream，并对操作指明具体的 stream，就可以实现多个操作的并行。这里的并行，可以说真正的并行，但也有可能两个操作因为数据依赖或者硬件资源冲突，而等待的现象。

> 来自不同流的 kernel 可以通过共享 GPU 的内核并发运行。

## CUDA Kernel

在 GPU 上调用的函数，称为 CUDA 核函数（Kernel function），核函数会被 GPU 上的多个线程执行。

定义 kernel 函数：

```cpp
__global__ void add(...)
{
    // ...
}
```

CUDA Kernel 是从单个 GPU 线程的角度编写的。当 kernel 执行时，每个线程同时对不同的数据执行相同的操作。

Kernel 作为一组可以以任何顺序执行的线程块（block）在 GPU 上运行。块数和每个块的线程数是程序员指定的。

block 是 GPU 上的可调度实体。一个 block 中的所有线程总是在同一个 SM 上执行，并且非抢占式运行直到完成。当所有块中的所有线程都退出时，Kernel 就执行完成了。CUDA kernel 的启动是异步的，当 CUDA kernel 被调用时，控制权会立即返回给 CPU。

## CUDA 线程层次

Grid（网格）-> Block（线程块）-> Warp（线程束）-> Thread（线程）。

由一个单独的 kernel 启动的所有线程组成一个 grid，grid 中所有线程共享 global memory。可以通过配置语法 `<<< >>>` 指定 kenel 运行的块数以及每个块内的线程数，总线程数等于每个块的线程数乘以块数。

一个 grid 由多个 block 组成，block 内部的多个线程可以同步，使用 `__syncthreads()` 充当屏障（同步点）。

warp 是调度和运行的基本单元。并不是所有的 thread 都能够在同一时刻在不同的 SP 上执行。32 个 thread 组成一个 warp，同一个 warp 中的 thread 可以以任意顺序执行相同的指令，只是处理的数据不同。一个 SM 上在任意时刻只能运行一个 wrap。

## CUDA 内存结构

- 每个线程都有私有的本地内存；
- 每个线程块都具有对该块的所有线程可见的共享内存，并且与该块具有相同的生命周期；
- 所有线程都可以访问相同的全局内存。

## 参考资料

- [CUDA 基础](https://www.cnblogs.com/LLW-NEU/p/16219611.html)；
- [CUDA 介绍](https://juniorprincewang.github.io/2018/01/12/CUDA-logic/)。

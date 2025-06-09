# CUDA 学习笔记

---

**学习资料：**

- 代码仓库：https://github.com/RussWong/CUDATutorial

---

## 01-02. hello world

```cpp
#include <stdio.h>
#include <cuda.h>
#include <cuda_runtime.h>

__global__ void hello_cuda() {
    // 泛指当前线程在所有block范围内的全局id
    unsigned int idx = blockIdx.x * blockDim.x + threadIdx.x;
    printf("block id = [ %d ], thread id = [ %d ] hello cuda\n", blockIdx.x, idx);
}

int main() {
    hello_cuda<<< 1, 1 >>>();
    cudaDeviceSynchronize();
    return 0;
}
```

`__global__`：CUDA kernel 函数前缀，被 CPU 调用，在 GPU 上执行。

`<<<1, 1>>>`：启动 CUDA kernel，参数含义：block 数量；每个 block 中的线程数量。

变量含义：

- `blockIdx.x`：block 的 ID；
- `blockDim.x`：block 内线程数量；
- `threadIdx.x`：block 内线程的 ID；

> 注意：`.x` 表示在 `x` 维度上，除此之外，还有 `y` 和 `z` 维度。

`cudaDeviceSynchronize()`：强制 CPU 等待 GPU 执行完成，即同步等待。

> 注意：`printf()` 是由 CPU 执行的。

## 03. 常用修饰符

- `__global__`：CUDA kernel 函数前缀，被 CPU 调用，在 GPU 上执行；
- `__host__`：由 CPU 调用并执行（无修饰时默认是 CPU 端函数）；
- `__device__`：由 GPU 调用并执行（由编译器 nvcc 确定是否 inline）；
- `__noinline__`：强制编译器不 inline；
- `__forceinline__`：强制编译器 inline。

## 04-05. CUDA 程序组成

- 由 `__global__` 修饰的 kernel 函数；
- main 函数，通过 `<<<..., ...>>>` 调用 kernel；
- 可选函数：`__host__` 和 `__device__`。

> 必备头文件：`<cuda.h>`、`<cuda_runtime.h>`。

示例程序：

```cpp
#include <stdio.h>
#include <cuda.h>
#include <cuda_runtime.h>

__global__ void sum(float *x)
{
    // 泛指当前block在所有block范围内的id
    int block_id = blockIdx.x;
    // 泛指当前线程在所有block范围内的全局id
    int global_tid = blockIdx.x * blockDim.x + threadIdx.x;
    // 泛指当前线程在其block内的id
    int local_tid = threadIdx.x;
    printf("current block=%d, thread id in current block =%d, global thread id=%d\n", block_id, local_tid, global_tid);
    x[global_tid] += 1;
}

int main() {
    int N = 32;
    int nbytes = N * sizeof(float);
    float *dx, *hx;

    /* allocate GPU mem */
    cudaMalloc((void **)&dx, nbytes); // TODO：思考为什么要用二级指针
    /* allocate CPU mem */
    hx = (float*) malloc(nbytes);

    /* init host data */
    printf("hx original: \n");
    for (int i = 0; i < N; i++) {
        hx[i] = i;
        printf("%g\n", hx[i]);
    }

    /* copy data to GPU */
    cudaMemcpy(dx, hx, nbytes, cudaMemcpyHostToDevice);

    /* launch GPU kernel */
    sum<<<1, N>>>(dx);

    /* copy data from GPU */
    cudaMemcpy(hx, dx, nbytes, cudaMemcpyDeviceToHost);

    printf("hx current: \n");
    for (int i = 0; i < N; i++) {
        printf("%g\n", hx[i]);
    }

    cudaFree(dx);
    free(hx);

    return 0;
}
```

## 06. 线程层次结构

- **Thread**：线程（硬件视角：CUDA Core）；
- **Warp**：32 个线程，是最基本的线程调度单位；
- **Block**：线程块，一组线程（硬件视角：Warps）；
- **Grid**：网格，一组线程块。

CUDA **SIMT（Single Instruction Multiple Thread）**：多个线程执行同一条指令（每个线程有自己独立的寄存器）。

> TODO：SIMT 和 SIMD 的区别？（面试常问）

## 07. 向量加法

## 08. CUDA runtime API

- `cudaMalloc()`
- `cudaMemcpy()`
- `cudaFree()`
- `cudaGetErrorString()`

## 09. kernel 计时

```cpp
int main()
{
    // ...

    cudaEvent_t start, stop;
    cudaEventCreate(&start);
    cudaEventCreate(&stop);
    cudaEventRecord(start);
    /* launch GPU kernel */
    vec_add<<<grid, bs>>>(dx, dy, dz, N);
    cudaEventRecord(stop);
    // 让 CPU 等待 stop 事件记录完成，确保后面使用 stop 计算时间时不会出现异常
    cudaEventSynchronize(stop);
    cudaEventElapsedTime(&milliseconds, start, stop);  
    
    // ...
}
```

## 10. main 函数编写流程

1. host 端申请内存并初始化数据；
2. device 端申请内存；
3. host 端数据拷贝到 device 端；
4. 计时开始；
5. 启动 kernel；
6. 计时结束；
7. device 端把结果拷贝回 host 端；
8. 对比 device 端计算结果和 host 端计算结果；
9. 释放 host 端内存和 device 端显存。

## 11. 向量化向量加法

CUDA 只支持向量化的读取和存储，但在实际计算时，还是只能一个个标量进行计算。

> `float4`：指针每偏移 1 位，在内存上移动 4 位（即每 4 个 float 数据为一个向量）。

## 12. GPU 硬件能力查询

```cpp
#include <cuda_runtime.h>
#include <cuda.h>
#include <iostream>
#include <string>

int main() {

  int deviceCount = 0;

  // 获取当前机器的GPU数量
  cudaError_t error_id = cudaGetDeviceCount(&deviceCount);
  if (deviceCount == 0) {
    printf("There are no available device(s) that support CUDA\n");
  } else {
    printf("Detected %d CUDA Capable device(s)\n", deviceCount);
  }

  for (int dev = 0; dev < deviceCount; ++dev) {
    cudaSetDevice(dev);
    // 初始化当前device的属性获取对象
    cudaDeviceProp deviceProp;
    cudaGetDeviceProperties(&deviceProp, dev);
    printf("\nDevice %d: \"%s\"\n", dev, deviceProp.name);

    // 显存容量
    printf("  Total amount of global memory:                 %.0f MBytes "
             "(%llu bytes)\n",
             static_cast<float>(deviceProp.totalGlobalMem / 1048576.0f),
             (unsigned long long)deviceProp.totalGlobalMem);

    // 时钟频率
    printf( "  GPU Max Clock rate:                            %.0f MHz (%0.2f "
        "GHz)\n",
        deviceProp.clockRate * 1e-3f, deviceProp.clockRate * 1e-6f);

    // L2 cache大小
    printf("  L2 Cache Size:                                 %d bytes\n",
             deviceProp.l2CacheSize);

    // high-frequent used
    // 注释见每个printf内的字符串
    printf("  Total amount of shared memory per block:       %zu bytes\n",
           deviceProp.sharedMemPerBlock);
    printf("  Total shared memory per multiprocessor:        %zu bytes\n",
           deviceProp.sharedMemPerMultiprocessor);
    printf("  Total number of registers available per block: %d\n",
           deviceProp.regsPerBlock);
    printf("  Warp size:                                     %d\n",
           deviceProp.warpSize);
    printf("  Maximum number of threads per multiprocessor:  %d\n",
           deviceProp.maxThreadsPerMultiProcessor);
    printf("  Maximum number of threads per block:           %d\n",
           deviceProp.maxThreadsPerBlock);
    printf("  Max dimension size of a block size (x,y,z): (%d, %d, %d)\n",
           deviceProp.maxThreadsDim[0], deviceProp.maxThreadsDim[1],
           deviceProp.maxThreadsDim[2]);
    printf("  Max dimension size of a grid size (x,y,z): (%d, %d, %d)\n",
           deviceProp.maxGridSize[0], deviceProp.maxGridSize[1],
           deviceProp.maxGridSize[2]);
  }

  return 0;
}
```

## 13. CPU 架构

**CPU 架构：**

- 运算器（ALU）；
- 控制器（Control）；
- 存储器：内存（DRAM）、缓存（Cache）。

**CPU 工作流程：**

1. 控制器从内存（用户进程中的代码段）提取指令，并放到指令寄存器；
2. 解码（指令译码）；
3. 执行（需要先从内存，从用户进程中的数据段，取数据）；
4. 写回（可选）。

**CPU 流水线：**

**级数？**——3 级流水线，即有 3 个过程（stage）。

CPU 的流水线级数越多，性能越好（最大吞吐越高），但级数不是越多越好。

级数过多的缺点：

- 电路单元更多，芯片面积 & 功耗增加；
- 未满载时，性能收益下降。

因此，应该综合考虑以寻得一个 tradeoff。

## 14. GPU 架构

**A100 GPU 架构：**

- 108 个 SM；
  - 每个 SM 有 4 个独立的区块，可以并行执行 4 组不同指令序列；
    - 每个区块有 4 个 Tensor Core（总共是 108 * 4 = 432 个）；
    - 每个区块有独立的 L0 指令缓存、Warp Scheduler、Dispatch Unit 等；
  - 每个 SM 中的 4 个区块共享 L1 指令缓存、数据缓存、Shared Memory 等；
- 6912 个 CUDA Core（即 INT32、FP32、FP64 单元）；
- ……

**CUDA 执行模型：**

kernel 启动 -> 分配 block 到 SM 上执行。

软件 -> 硬件：

- Thread -> CUDA Core
- Block（n）->（1）SM（即同一个 block 中的线程只能同时在一个 SM 上执行，而一个 SM 上可以同时执行多个 block）
- Grid -> 整个 device

> 注意：一个 Warp（32 个线程）中的线程只能同时被调度去执行某一条指令，但可以只有部分线程在执行（active thread），剩下的线程只能等待（不能去执行别的指令），直到其它线程执行完当前的指令。

## 15. GPU 存储层次结构

- **全局内存（Global）：**
  - 所有线程 + CPU；
  - 片外，访问慢；
  - 需要连续访问，减少内存事务。
- **共享内存（Shared）：**
  - 一个 block 中的线程；
  - 片上，访问快；
  - bankconflict：……（重要）
- **寄存器（Register）：**
  - 一个线程；
  - 片上，访问快；
  - spill：溢出的部分会放到 local memory 上，从而增加延迟。
- Local
- Constant
- Texture

## 16. CPU 和 GPU 架构的区别

GPU：

- 控制单元少 -> 不适合处理分支程序（if else）；
- Cache 少 -> 不适合处理数据分散的程序；
- 核心数量多 -> 不适合处理计算分散的程序；
- 核心频率低 -> 不适合处理计算量小的程序；
- 标量运算处理器 -> 不支持向量化计算，但支持向量化存取（load & store）。

## 17. 算子类型总结

**入门学习：**

- **Reduce**（规约）：softmax
- **Elementwise**（加法）：gelu、copy_if
- **Fused**（融合算子）：MatmulAndBiasAndRelu

**进阶学习：**

- **Gemm**（现在一般是直接调库，各种形状的性能已经优化得比较好了）：matmul
- **坐标变换**：concat、transpose
- **Sliding Window**：conv2d、conv3d、maxpool
- **Scan**：prefixsum、cumsum
- **Sort**：mergesort

## 18-25. Reduce 算子实现（面试高频）

Reduce：对 N 个数据做累计的算术操作（如：总和、最大值、最小值、均值等）。

### baseline

CPU 方式（单线程）+ for 循环。

### reduce_sum kernel V0

求一个 block 内的局部和。

> 注意：每个 block 中都有属于自己的 shared memory。

### reduce_sum kernel V1

除余（%）和除法（/）在任何硬件上都是一个比较耗时的操作 -> 转换为位运算

> CUDA 编译流程：cuda -> ptx -> sass -> cubin。

### reduce_sum kernel V2

**shared memory bank conflict**：一个 warp 的多个线程访问同一个 bank 的不同字段（同一列，不同行）时，这些线程只能串行执行，从而会造成性能下降。

> 注意：一个 bank 的大小为 4 字节，一块 shared memory 中有 32 个 bank。

bank conflict 解决方法：

- 对于 Reduce 场景，对半相加（方案较固定）；
- Padding 数据（仅限于 shared memory 为 2 维的场景）；
- Permute（较难）。

### reduce_sum kernel V3

相比于 V2，让空闲线程也干活。

### reduce_sum kernel V4

`__syncthreads()`（同步一个 block 内的所有线程）-> `__syncwarp()`（开销更小）。

展开最后一个迭代的 for 循环，单独去做 reduce，只在 warp 内同步。

### reduce_sum kernel V5

完全展开 for 循环，省掉 for 循环中的判断和加法指令。

省掉了 for 循环中的判断和位运算，指令减少了，因此性能就提升了。

### reduce_sum kernel V6

对所有 Block 做 reduce。

当总线程数 < 总数据量时，每个线程以 gridSize * blockSize 为步长循环读取数据。

Debug 方法：将 block 数量和 block 大小都设为 1，此时只有一个线程在工作（串行执行），便于排查问题。

## 26-27. Warp level program

**在 Warp 层面进行编程的好处：**

- 可以省去 `__syncthreads()` 的开销，只需在 Warp 内同步线程；
- 更高的通信带宽（计算更快）；
- divergence-free across warps (?)

---

Next：26

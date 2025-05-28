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

int main(){
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

```cpp
#include <stdio.h>
#include <cuda.h>
#include <cuda_runtime.h>

typedef float FLOAT;

/* CUDA kernel function */
__global__ void vec_add(FLOAT *x, FLOAT *y, FLOAT *z, int N)
{
    /* 2D grid */
    int idx = (blockDim.x * (blockIdx.x + blockIdx.y * gridDim.x) + threadIdx.x);
    /* 1D grid */
    // int idx = blockDim.x * blockIdx.x + threadIdx.x;
    if (idx < N) z[idx] = y[idx] + x[idx];
}

void vec_add_cpu(FLOAT *x, FLOAT *y, FLOAT *z, int N)
{
    for (int i = 0; i < N; i++) z[i] = y[i] + x[i];
}

int main()
{
    int N = 10000;
    int nbytes = N * sizeof(FLOAT);

    /* 1D block */
    int bs = 256;

    /* 2D grid */
    int s = ceil(sqrt((N + bs - 1.) / bs));
    dim3 grid(s, s);
    /* 1D grid */
    // int s = ceil((N + bs - 1.) / bs);
    // dim3 grid(s);

    FLOAT *dx, *hx;
    FLOAT *dy, *hy;
    FLOAT *dz, *hz;

    /* allocate GPU mem */
    cudaMalloc((void **)&dx, nbytes);
    cudaMalloc((void **)&dy, nbytes);
    cudaMalloc((void **)&dz, nbytes);
    
    /* init time */
    float milliseconds = 0;

    /* alllocate CPU mem */
    hx = (FLOAT *) malloc(nbytes);
    hy = (FLOAT *) malloc(nbytes);
    hz = (FLOAT *) malloc(nbytes);

    /* init */
    for (int i = 0; i < N; i++) {
        hx[i] = 1;
        hy[i] = 1;
    }

    /* copy data to GPU */
    cudaMemcpy(dx, hx, nbytes, cudaMemcpyHostToDevice);
    cudaMemcpy(dy, hy, nbytes, cudaMemcpyHostToDevice);

    cudaEvent_t start, stop;
    cudaEventCreate(&start);
    cudaEventCreate(&stop);
    cudaEventRecord(start);
    /* launch GPU kernel */
    vec_add<<<grid, bs>>>(dx, dy, dz, N);
    cudaEventRecord(stop);
    cudaEventSynchronize(stop);
    cudaEventElapsedTime(&milliseconds, start, stop);  
    
    /* copy GPU result to CPU */
    cudaMemcpy(hz, dz, nbytes, cudaMemcpyDeviceToHost);

    /* CPU compute */
    FLOAT* hz_cpu_res = (FLOAT *) malloc(nbytes);
    vec_add_cpu(hx, hy, hz_cpu_res, N);

    /* check GPU result with CPU*/
    for (int i = 0; i < N; ++i) {
        if (fabs(hz_cpu_res[i] - hz[i]) > 1e-6) {
            printf("Result verification failed at element index %d!\n", i);
        }
    }
    printf("Result right\n");
    cudaFree(dx);
    cudaFree(dy);
    cudaFree(dz);

    free(hx);
    free(hy);
    free(hz);
    free(hz_cpu_res);

    return 0;
}
```

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
    cudaEventSynchronize(stop); // 让 CPU 等待 stop 事件记录完成，确保后面使用 stop 计算时间时不会出现异常。
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

`float4`：指针每偏移 1 位，在内存上移动 4 位（即每 4 个 float 数据为一个向量）。

CUDA 只支持向量化的读取和存储，但在实际计算时，还是只能一个个标量进行计算。

```cpp
#include <stdio.h>
#include <cuda.h>
#include <cuda_runtime.h>

#define ARRAY_SIZE 100000000   // Array size has to exceed L2 size to avoid L2 cache residence
#define MEMORY_OFFSET 10000000
#define BENCH_ITER 10
#define THREADS_NUM 256

__device__ __forceinline__
float4 LoadFromGlobalPTX(float4 *ptr) {
    float4 ret;
    // ptx指令，是CUDA的更底层的语言，类似于汇编对于C/C++
    asm volatile (
        "ld.global.v4.f32 {%0, %1, %2, %3}, [%4];"
        : "=f"(ret.x), "=f"(ret.y), "=f"(ret.z), "=f"(ret.w)
        : "l"(ptr)
    );

    return ret;
}

// float4 vectoradd
__global__ void mem_bw (float* A,  float* B, float* C){
    // 泛指当前线程在所有block范围内的全局id
	int idx = blockIdx.x * blockDim.x + threadIdx.x;
	// int idx = blockIdx.x * blockDim.x * 4 + threadIdx.x; // lesson11里面错写为了这行，请参考熊猫-lesson11和lesson37的勘误.mp4
	for(int i = idx; i < MEMORY_OFFSET / 4; i += blockDim.x * gridDim.x) {
		//问题1: 删除43-46行,会发现带宽数据为2666g/S
		//尝试: 使用nv ptx load global memory指令,结果数据依然没变
		//结论: 大概率是编译器优化:读了数据不做操作那就会不读
		float4 a1 = reinterpret_cast<float4*>(A)[i];
		//float4 a1 = LoadFromGlobalPTX(reinterpret_cast<float4*>(A) + i);
		float4 b1 = reinterpret_cast<float4*>(B)[i];
		//float4 b1 = LoadFromGlobalPTX(reinterpret_cast<float4*>(B) + i);
		float4 c1;
		// 测量显存带宽方法1:向量加法,248.8g/s
		c1.x = a1.x + b1.x;
		c1.y = a1.y + b1.y;
		c1.z = a1.z + b1.z;
		c1.w = a1.w + b1.w;
		// 测量显存带宽方法2:copy操作,242.3g/s	
		// c1.x = a1.x;
		// c1.y = a1.y;
		// c1.z = a1.z;
		// c1.w = a1.w;
		reinterpret_cast<float4*>(C)[i] = c1;
	}
}

void vec_add_cpu(float *x, float *y, float *z, int N)
{
    for (int i = 0; i < 20; i++) z[i] = y[i] + x[i];
}

int main() {
	float *A = (float*) malloc(ARRAY_SIZE*sizeof(float));
	float *B = (float*) malloc(ARRAY_SIZE*sizeof(float));
	float *C = (float*) malloc(ARRAY_SIZE*sizeof(float));

	float *A_g;
	float *B_g;
	float *C_g;

	float milliseconds = 0;

	for (uint32_t i=0; i<ARRAY_SIZE; i++) {
		A[i] = (float)i;
		B[i] = (float)i;
	}
	cudaMalloc((void**)&A_g, ARRAY_SIZE*sizeof(float));
	cudaMalloc((void**)&B_g, ARRAY_SIZE*sizeof(float));
	cudaMalloc((void**)&C_g, ARRAY_SIZE*sizeof(float));

	cudaMemcpy(A_g, A, ARRAY_SIZE*sizeof(float), cudaMemcpyHostToDevice);
	cudaMemcpy(B_g, B, ARRAY_SIZE*sizeof(float), cudaMemcpyHostToDevice);
  
	int BlockNums = MEMORY_OFFSET / 256;
    // warm up to occupy L2 cache
	printf("warm up start\n");
	mem_bw<<<BlockNums / 4, THREADS_NUM>>>(A_g, B_g, C_g);
	printf("warm up end\n");
    // time start using cudaEvent
	cudaEvent_t start, stop;
	cudaEventCreate(&start);
	cudaEventCreate(&stop);
	cudaEventRecord(start);
	for (int i = BENCH_ITER - 1; i >= 0; --i) {
		mem_bw<<<BlockNums / 4, THREADS_NUM>>>(A_g + i * MEMORY_OFFSET, B_g + i * MEMORY_OFFSET, C_g + i * MEMORY_OFFSET);
	}
	// time stop using cudaEvent
	cudaEventRecord(stop);
	cudaEventSynchronize(stop);
	cudaEventElapsedTime(&milliseconds, start, stop);

	cudaMemcpy(C, C_g, ARRAY_SIZE*sizeof(float), cudaMemcpyDeviceToHost);
	/* CPU compute */
	float* C_cpu_res = (float *) malloc(20*sizeof(float));
	vec_add_cpu(A, B, C_cpu_res, ARRAY_SIZE);

	/* check GPU result with CPU*/
	for (int i = 0; i < 20; ++i) {
		/* 测量显存带宽时, 修改C_cpu_res[i]为0 */
		if (fabs(C_cpu_res[i] - C[i]) > 1e-6) {
			printf("Result verification failed at element index %d!\n", i);
		}
	}
	printf("Result right\n");
	unsigned N = ARRAY_SIZE * 4;
	/* 测量显存带宽时, 根据实际读写的数组个数, 指定下行是 1*(float)N 还是 2*(float)N 还是 3*(float)N  */
	printf("Mem BW= %f (GB/sec)\n", 3 * (float)N / milliseconds / 1e6);
  	cudaFree(A_g);
  	cudaFree(B_g);
  	cudaFree(C_g);

  	free(A);
  	free(B);
  	free(C);
  	free(C_cpu_res);
}
```

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

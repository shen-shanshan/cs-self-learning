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

CUDA **SIMT（Single Instruction Multiple Thread）**：多个线程执行同一条指令。

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

# CUDA 知识补充

---

在CUDA编程中，**数据对齐（Data Alignment）** 是指数据在内存中的存储地址满足特定的字节对齐要求（如 32 位、64 位、128 位等）。CUDA 对数据访问的对齐性有严格要求，尤其是全局内存（Global Memory）和共享内存（Shared Memory）的访问，以提高内存访问效率。

## **1. 数据对齐的作用**

### **(1) 提高内存访问效率**

- **全局内存（Global Memory）**：
  - CUDA 设备（如 NVIDIA GPU）在访问全局内存时，通常以 **32 字节（256 位）、64 字节（512 位）或 128 字节（1024 位）** 为单位进行内存事务（Memory Transaction）。
  - 如果数据是 **对齐的**，GPU 可以一次性读取完整的数据块，减少内存访问次数。
  - 如果数据是 **未对齐的**，可能会导致额外的内存访问（如 `misaligned memory access`），降低性能。

- **共享内存（Shared Memory）**：
  - 共享内存的访问通常以 **bank** 为单位（如 32 位或 64 位）。
  - 如果数据没有正确对齐，可能会导致 **bank conflict**，降低并行访问效率。

### **(2) 避免未定义行为**

- 某些 CUDA 操作（如 `__half`（半精度浮点数）、`float2`、`double2` 等）要求数据必须按特定字节对齐，否则可能导致未定义行为或性能下降。

### **(3) 优化 SIMD 指令**

- GPU 使用 SIMD（单指令多数据）方式执行计算，数据对齐可以优化向量化访存（如 `float4`、`int4` 等）。

## **2. CUDA 中的数据对齐方式**

### **(1) 使用 `__align__` 或 `alignas`（C++11）**

```cpp
// 结构体对齐到 16 字节
struct __align__(16) AlignedStruct {
    float x, y, z, w;
};
```

或（C++11）：

```cpp
struct alignas(16) AlignedStruct {
    float x, y, z, w;
};
```

### **(2) 使用 `cudaMallocPitch` 对齐 2D 数组**

```cpp
float* devPtr;
size_t pitch;
cudaMallocPitch(&devPtr, &pitch, width * sizeof(float), height);
```

`pitch` 返回的是对齐后的实际行字节数（可能比 `width * sizeof(float)` 大）。

### **(3) 使用 `__restrict__` 和 `__builtin_assume_aligned`（CUDA 12+）**

```cpp
void kernel(const float* __restrict__ ptr) {
    __builtin_assume_aligned(ptr, 16); // 告诉编译器 ptr 是 16 字节对齐的
    // ...
}
```

## **3. 示例：对齐的全局内存访问**

```cpp
#include <cstdio>

// 对齐到 16 字节的结构体
struct __align__(16) Vec4 {
    float x, y, z, w;
};

__global__ void testKernel(Vec4* data) {
    int tid = threadIdx.x + blockIdx.x * blockDim.x;
    data[tid].x = tid;  // 对齐访问，高效
}

int main() {
    Vec4* d_data;
    cudaMalloc(&d_data, 1024 * sizeof(Vec4)); // 自动对齐（cudaMalloc 默认对齐 256 字节）

    testKernel<<<8, 128>>>(d_data);
    cudaDeviceSynchronize();

    cudaFree(d_data);
    return 0;
}
```

- `Vec4` 结构体对齐到 16 字节，确保 GPU 可以高效访问。

## **4. 检查数据是否对齐**

可以使用 `reinterpret_cast` 和 `%` 运算检查地址是否对齐：

```cpp
bool is_aligned(const void* ptr, size_t alignment) {
    return (reinterpret_cast<uintptr_t>(ptr) % alignment == 0);
}
```

## **5. 总结**

| **对齐要求** | **适用场景** | **优化效果** |
|-------------|-------------|-------------|
| **4/8/16 字节** | `float`, `double`, `float2` | 减少内存事务 |
| **32/64/128 字节** | 全局内存访问 | 合并内存访问（Coalesced Memory Access） |
| **共享内存 bank 对齐** | `__shared__` 变量 | 避免 bank conflict |
| **SIMD 向量化** | `float4`, `int4` | 提高计算吞吐量 |

**最佳实践：**

1. **尽量让数据对齐到 16/32/64 字节**（取决于 GPU 架构）。
2. **使用 `__align__` 或 `alignas` 显式指定对齐**。
3. **避免未对齐访问**，特别是在全局内存和共享内存中。

这样可以最大化 GPU 的内存带宽利用率，提高计算性能。

---

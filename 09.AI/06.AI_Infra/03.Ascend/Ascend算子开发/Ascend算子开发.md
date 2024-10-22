# Ascend 算子开发

## 基本概念（以 CUDA 为例）

### Host & Device

CPU 和主存被称为 **Host**，GPU 被称为 **Device**。

### CUDA Context

类似于 CPU 进程的上下文，CUDA context 管理各项资源的生命周期。每个 Context 有自己的地址空间。

在每次运行 cuda 的第一个函数（kernel）时，cuda 会进行 initialization，这时就会创建 cuda context。cuda 函数的调用都需要 context 来管理。

通常情况下，一个 context 对应一个 CPU 进程。但多个 CPU 进程也可以共享同一 GPU context，不同进程的 kernel 和 memcpy 操作在同一 GPU 上并发执行，以实现最大化 GPU 利用率、减少 GPU 上下文的切换时间与存储空间。

在同一个 GPU 上，可能同时存在多个 CUDA Context。但在任意时刻，GPU 上只有一个活动的 context。多个 context 之间按照 time slice 的方式轮流使用 GPU。

CUDA runtime 通过延迟初始化创建 context。即在调用每一个 CUDART 库函数时，它会检查当前是否有 context 存在，若没有 context，那么才自动创建。cuda runtime 将 context 和 device 的概念合并了，即在一个 gpu 上的操作可看成在一个 context 下。也就是说，一个 device 对应一个 context。

### CUDA Stream

CUDA Stream 是指一堆异步的 CUDA 操作，他们按照 host 代码调用的顺序执行在 device 上，Stream 维护了这些操作的顺序，并在所有预处理完成后允许这些操作进入工作队列，同时也可以对这些操作进行一些查询操作。

这些 CUDA 操作包括 host 到 device 的数据传输，调用 kernel 以及其他的由 host 发起由 device 执行的动作。这些操作的执行是异步的，CUDA runtime 会决定这些操作合适的执行时机。我们则可以使用相应的 cuda api 来保证所取得结果是在所有操作完成后获得的。同一个 stream 里的操作是串行的，不同的 stream 则没有此限制。

- **隐式声明 stream（NULL stream）**：默认流，无需显式创建，CUDA 中默认存在；
- **显示声明 stream（non-NULL stream）**：在代码中创建，并对某个 CUDA 操作指明 Stream。

CUDA 开始执行时，会有一个默认的 stream（NULL 流），当代码中没有明确指明操作的 stream 时，就放在默认的 stream 中，这样无关的操作也无法并行。

通过在代码中创建不同的 stream，并对操作指明具体的 stream，就可以实现多个操作的并行。这里的并行，可以说真正的并行，但也有可能两个操作因为数据依赖或者硬件资源冲突，而等待的现象。

> 来自不同流的 kernel 可以通过共享 GPU 的内核并发运行。

### CUDA Kernel

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

### CUDA 线程层次

Grid（网格）-> Block（线程块）-> Warp（线程束）-> Thread（线程）。

由一个单独的 kernel 启动的所有线程组成一个 grid，grid 中所有线程共享 global memory。可以通过配置语法 `<<< >>>` 指定 kenel 运行的块数以及每个块内的线程数，总线程数等于每个块的线程数乘以块数。

一个 grid 由多个 block 组成，block 内部的多个线程可以同步，使用 `__syncthreads()` 充当屏障（同步点）。

warp 是调度和运行的基本单元。并不是所有的 thread 都能够在同一时刻在不同的 SP 上执行。32 个 thread 组成一个 warp，同一个 warp 中的 thread 可以以任意顺序执行相同的指令，只是处理的数据不同。一个 SM 上在任意时刻只能运行一个 wrap。

### CUDA 内存结构

- 每个线程都有私有的本地内存；
- 每个线程块都具有对该块的所有线程可见的共享内存，并且与该块具有相同的生命周期；
- 所有线程都可以访问相同的全局内存。

### 总结

**Context 负责 Device 资源的管理和调度：**

- 1 个 device 可以有 n 个 context，但同时只能使用 1 个 context；1 个 context 唯一对应 1 个 device；
- 1 个进程可以创建 n 个 context；
- 1 个线程只能同时使用 1 个 context，该 context 关联对应的 device；可以通过切换 context 来切换要使用的 device；
- 在线程中，默认使用最后一次创建的 context。

**Stream 维护一些异步操作的执行顺序：**

- 1 个线程/context 可以创建 n 个 stream；
- 同一个 stream 内的任务顺序执行，不同 stream 间并行执行；
- 不同线程/context 间的 stream 在 device 上相互独立/隔离。

**Task/Kernel，是 Device 上真正的任务执行体：**

- 1 个 stream 中可以下发 n 个 task；
- task 之间可以插入 event，用于同步不同 stream 之间的 task。

### 参考资料

- [<u>CUDA 基础</u>](https://www.cnblogs.com/LLW-NEU/p/16219611.html)；
- [<u>CUDA 介绍</u>](https://juniorprincewang.github.io/2018/01/12/CUDA-logic/)；
- [<u>算子开发基本概念</u>](https://www.hiascend.com/doc_center/source/zh/CANNCommunityEdition/80RC3alpha001/devguide/appdevg/aclpythondevg/aclpythondevg_0004.html)；

## 单算子开发

> AscendCL（Ascend Computing Language）是一套用于在昇腾平台上开发深度神经网络应用的 C 语言 API 库，提供运行资源管理、内存管理、模型加载与执行、算子加载与执行、媒体数据处理等 API，能够实现利用昇腾硬件计算资源、在昇腾 CANN 平台上进行深度学习推理计算、图形图像预处理、单算子加速计算等能力。简单来说，就是统一的 API 框架，实现对所有资源的调用。
> AscendCL 应用开发包含 C/C++ & Python 两套 API。
>
> 面向算子开发场景的编程语言 Ascend C，原生支持 C/C++ 标准规范，最大化匹配用户开发习惯；通过多层接口抽象、自动并行计算、孪生调试等关键技术，极大提高算子开发效率，助力 AI 开发者低成本完成算子开发和模型调优部署。

单算子调用方式：

- 单算子 API 执行：
  - 直接调用算子 API；
  - AscendC 自定义算子开发。
- 单算子模型执行。

### 单算子 API 执行

- NN 算子；
- DVPP 算子；
- 融合算子。

> 融合算子：将多个独立的“小算子”融合成一个“大算子”，多个小算子的功能和大算子的功能等价，但融合算子在性能或者内存等方面优于独立的小算子。

单算子 API 执行时，针对每个算子，都需要依次调用 `aclxxXxxGetWorkspaceSize()` 接口获取算子执行需要的 workspace 内存大小、调用 `aclxxXxx()` 接口执行算子。

示例代码（加法算子）：

```cpp
#include <iostream>
#include <vector>
#include "acl/acl.h"
#include "aclnnop/aclnn_add.h"

#define CHECK_RET(cond, return_expr) \
  do {                               \
    if (!(cond)) {                   \
      return_expr;                   \
    }                                \
  } while (0)

#define LOG_PRINT(message, ...)     \
  do {                              \
    printf(message, ##__VA_ARGS__); \
  } while (0)

int64_t GetShapeSize(const std::vector<int64_t>& shape) {
  int64_t shape_size = 1;
  for (auto i : shape) {
    shape_size *= i;
  }
  return shape_size;
}

int Init(int32_t deviceId, aclrtStream* stream) {
  // 固定写法，AscendCL初始化
  auto ret = aclInit(nullptr);
  CHECK_RET(ret == ACL_SUCCESS, LOG_PRINT("aclInit failed. ERROR: %d\n", ret); return ret);
  ret = aclrtSetDevice(deviceId);
  CHECK_RET(ret == ACL_SUCCESS, LOG_PRINT("aclrtSetDevice failed. ERROR: %d\n", ret); return ret);
  ret = aclrtCreateStream(stream);
  CHECK_RET(ret == ACL_SUCCESS, LOG_PRINT("aclrtCreateStream failed. ERROR: %d\n", ret); return ret);
  return 0;
}

template <typename T>
int CreateAclTensor(const std::vector<T>& hostData, const std::vector<int64_t>& shape, void** deviceAddr,
                    aclDataType dataType, aclTensor** tensor) {
  auto size = GetShapeSize(shape) * sizeof(T);
  // 调用aclrtMalloc申请device侧内存
  auto ret = aclrtMalloc(deviceAddr, size, ACL_MEM_MALLOC_HUGE_FIRST);
  CHECK_RET(ret == ACL_SUCCESS, LOG_PRINT("aclrtMalloc failed. ERROR: %d\n", ret); return ret);
  // 调用aclrtMemcpy将host侧数据拷贝到device侧内存上
  ret = aclrtMemcpy(*deviceAddr, size, hostData.data(), size, ACL_MEMCPY_HOST_TO_DEVICE);
  CHECK_RET(ret == ACL_SUCCESS, LOG_PRINT("aclrtMemcpy failed. ERROR: %d\n", ret); return ret);
  // 计算连续tensor的strides
  std::vector<int64_t> strides(shape.size(), 1);
  for (int64_t i = shape.size() - 2; i >= 0; i--) {
    strides[i] = shape[i + 1] * strides[i + 1];
  }
  // 调用aclCreateTensor接口创建aclTensor
  *tensor = aclCreateTensor(shape.data(), shape.size(), dataType, strides.data(), 0, aclFormat::ACL_FORMAT_ND,
                            shape.data(), shape.size(), *deviceAddr);
  return 0;
}

int main() {
  // 1. （固定写法）device/stream初始化, 参考AscendCL对外接口列表
  // 根据自己的实际device填写deviceId
  int32_t deviceId = 0;
  aclrtStream stream;
  auto ret = Init(deviceId, &stream);
  // check根据自己的需要处理
  CHECK_RET(ret == 0, LOG_PRINT("Init acl failed. ERROR: %d\n", ret); return ret);

  // 2. 构造输入与输出，需要根据API的接口自定义构造
  std::vector<int64_t> selfShape = {4, 2};
  std::vector<int64_t> otherShape = {4, 2};
  std::vector<int64_t> outShape = {4, 2};
  void* selfDeviceAddr = nullptr;
  void* otherDeviceAddr = nullptr;
  void* outDeviceAddr = nullptr;
  aclTensor* self = nullptr;
  aclTensor* other = nullptr;
  aclScalar* alpha = nullptr;
  aclTensor* out = nullptr;
  std::vector<float> selfHostData = {0, 1, 2, 3, 4, 5, 6, 7};
  std::vector<float> otherHostData = {1, 1, 1, 2, 2, 2, 3, 3};
  std::vector<float> outHostData = {0, 0, 0, 0, 0, 0, 0, 0};
  float alphaValue = 1.2f;
  // 创建self aclTensor
  ret = CreateAclTensor(selfHostData, selfShape, &selfDeviceAddr, aclDataType::ACL_FLOAT, &self);
  CHECK_RET(ret == ACL_SUCCESS, return ret);
  // 创建other aclTensor
  ret = CreateAclTensor(otherHostData, otherShape, &otherDeviceAddr, aclDataType::ACL_FLOAT, &other);
  CHECK_RET(ret == ACL_SUCCESS, return ret);
  // 创建alpha aclScalar
  alpha = aclCreateScalar(&alphaValue, aclDataType::ACL_FLOAT);
  CHECK_RET(alpha != nullptr, return ret);
  // 创建out aclTensor
  ret = CreateAclTensor(outHostData, outShape, &outDeviceAddr, aclDataType::ACL_FLOAT, &out);
  CHECK_RET(ret == ACL_SUCCESS, return ret);

  // 3. 调用CANN算子库API，需要修改为具体的算子接口
  uint64_t workspaceSize = 0;
  aclOpExecutor* executor;
  // 调用aclnnAdd第一段接口
  ret = aclnnAddGetWorkspaceSize(self, other, alpha, out, &workspaceSize, &executor);
  CHECK_RET(ret == ACL_SUCCESS, LOG_PRINT("aclnnAddGetWorkspaceSize failed. ERROR: %d\n", ret); return ret);
  // 根据第一段接口计算出的workspaceSize申请device内存
  void* workspaceAddr = nullptr;
  if (workspaceSize > 0) {
    ret = aclrtMalloc(&workspaceAddr, workspaceSize, ACL_MEM_MALLOC_HUGE_FIRST);
    CHECK_RET(ret == ACL_SUCCESS, LOG_PRINT("allocate workspace failed. ERROR: %d\n", ret); return ret;);
  }
  // 调用aclnnAdd第二段接口
  ret = aclnnAdd(workspaceAddr, workspaceSize, executor, stream);
  CHECK_RET(ret == ACL_SUCCESS, LOG_PRINT("aclnnAdd failed. ERROR: %d\n", ret); return ret);

  // 4.（固定写法）同步等待任务执行结束
  ret = aclrtSynchronizeStream(stream);
  CHECK_RET(ret == ACL_SUCCESS, LOG_PRINT("aclrtSynchronizeStream failed. ERROR: %d\n", ret); return ret);

  // 5. 获取输出的值，将device侧内存上的结果拷贝至host侧，需要根据具体API的接口定义修改
  auto size = GetShapeSize(outShape);
  std::vector<float> resultData(size, 0);
  ret = aclrtMemcpy(resultData.data(), resultData.size() * sizeof(resultData[0]), outDeviceAddr, size * sizeof(float),
                    ACL_MEMCPY_DEVICE_TO_HOST);
  CHECK_RET(ret == ACL_SUCCESS, LOG_PRINT("copy result from device to host failed. ERROR: %d\n", ret); return ret);
  for (int64_t i = 0; i < size; i++) {
    LOG_PRINT("result[%ld] is: %f\n", i, resultData[i]);
  }

  // 6. 释放aclTensor和aclScalar，需要根据具体API的接口定义修改
  aclDestroyTensor(self);
  aclDestroyTensor(other);
  aclDestroyScalar(alpha);
  aclDestroyTensor(out);
 
  // 7. 释放device资源，需要根据具体API的接口定义修改
  aclrtFree(selfDeviceAddr);
  aclrtFree(otherDeviceAddr);
  aclrtFree(outDeviceAddr);
  if (workspaceSize > 0) {
    aclrtFree(workspaceAddr);
  }
  aclrtDestroyStream(stream);
  aclrtResetDevice(deviceId);
  aclFinalize();
  return 0;
}
```

> C++ 语法说明：
>
> - [<u>auto 关键字</u>](https://blog.csdn.net/xiaoquantouer/article/details/51647865)；
> - [<u>vector</u>](https://blog.csdn.net/u014779536/article/details/111239643)：
>   - `size()`：返回元素个数；
>   - `data()`：返回指向第一个元素的指针。
>
> 参数说明：
>
> - `strides`：描述 Tensor 维度上相邻两个元素的间隔，详见[<u>非连续的 Tensor</u>](https://www.hiascend.com/document/detail/zh/CANNCommunityEdition/80RC3alpha003/apiref/aolapi/context/common/%E9%9D%9E%E8%BF%9E%E7%BB%AD%E7%9A%84Tensor.md)；
> - `workspace`：在 device 侧申请的 workspace 内存地址；
> - `workspaceSize`：在 device 侧申请的 workspace 大小；
> - `executor`：算子执行器，实现了算子的计算流程；
> - `ret`：[<u>aclnn 返回码</u>](https://www.hiascend.com/document/detail/zh/CANNCommunityEdition/80RC3alpha003/apiref/aolapi/context/common/aclnn%E8%BF%94%E5%9B%9E%E7%A0%81.md)。

### API 参考

**单算子 API：**

- 加法算子：[<u>aclnnAdd</u>](https://www.hiascend.com/document/detail/zh/CANNCommunityEdition/80RC3alpha003/apiref/aolapi/context/common/aclnn_domains.md?sub_id=%2Fzh%2FCANNCommunityEdition%2F80RC3alpha003%2Fapiref%2Faolapi%2Fcontext%2FaclnnAdd%26aclnnInplaceAdd.md)；
- 乘法算子：[<u>aclnnMatmul</u>](https://www.hiascend.com/document/detail/zh/CANNCommunityEdition/80RC3alpha003/apiref/aolapi/context/aclnnMatmul.md)。

> [!NOTE]
>
> - 多个输入数据之间，数据类型需要满足[<u>互推导关系</u>](https://www.hiascend.com/document/detail/zh/CANNCommunityEdition/80RC3alpha003/apiref/aolapi/context/common/%E4%BA%92%E6%8E%A8%E5%AF%BC%E5%85%B3%E7%B3%BB.md)：当一个 API（如 `aclnnAdd()`、`aclnnMul()` 等）输入的 Tensor 数据类型不一致时，API 内部会推导出一个数据类型，将输入数据转换成该数据类型进行计算；
> - 多个输入数据之间，shape 需要满足[<u>广播关系</u>](https://www.hiascend.com/document/detail/zh/CANNCommunityEdition/80RC3alpha003/apiref/aolapi/context/common/broadcast%E5%85%B3%E7%B3%BB.md)：在某些情况下，较小的数组可以“广播至”较大的数组，使两者shape互相兼容；
> - 算子 API 查询：`CANN 社区版 -> 8.0.RC3.alpha003 -> API 参考 -> 算子加速库接口 -> NN 算子接口`。

**同步 API：**

- `acl.rt.synchronize_device()`：设备同步，阻塞当前主机线程直到 Device 上所有显式或隐式创建的 Stream 都完成所有先前下发的任务；
- `acl.rt.synchronize_stream()`：阻塞当前主机线程直到指定的 Stream 中完成所有下发的任务；
- `acl.rt.synchronize_event()`：阻塞当前主机线程直到指定的 Event 事件完成。

### 参考资料

- [<u>Ascend 算子开发指南</u>](https://github.com/wangshuai09/Notebook/blob/main/Ascend%E7%AE%97%E5%AD%90%E5%BC%80%E5%8F%91%E6%8C%87%E5%8D%97/aclnn%E7%AE%97%E5%AD%90%E5%BC%80%E5%8F%91%E6%8C%87%E5%8D%97.md)；
- [<u>CANN 开发文档</u>](https://www.hiascend.com/document/detail/zh/CANNCommunityEdition/80RC3alpha003/quickstart/quickstart/quickstart_18_0001.html)；
- [<u>单算子调用基础知识</u>](https://www.hiascend.com/document/detail/zh/CANNCommunityEdition/80RC3alpha003/devguide/appdevg/aclcppdevg/aclcppdevg_000016.html)；
- [<u>调用 NN 算子接口示例代码</u>](https://www.hiascend.com/document/detail/zh/CANNCommunityEdition/80RC3alpha003/devguide/appdevg/aclcppdevg/aclcppdevg_000019.html)；
- [<u>算子加速库接口</u>](https://www.hiascend.com/document/detail/zh/CANNCommunityEdition/80RC3alpha003/apiref/aolapi/operatorlist_0001.html)；
- [<u>开源融合算子</u>](https://gitee.com/ascend/cann-ops-adv)。

## 疑问

- aclnn 和 aclrt 有什么区别？

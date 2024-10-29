# 一、概述

**什么是算子？**

在 AI 框架中，算子一般指一些最基本的代数运算（如：矩阵加法、矩阵乘法等），多个算子之间也可以根据需要组合成更加复杂的融合算子（如：flash-attention 算子等）。算子的输入和输出都是 Tensor（张量）。

> 融合算子：将多个独立的“小算子”融合成一个“大算子”，多个小算子的功能和大算子的功能等价，但融合算子在性能或者内存等方面优于独立的小算子。

另外，算子更多地是 AI 框架中的一个概念，在硬件底层算子具体的执行部分，一般叫做 Kernel（核函数）。

下面将首先对算子开发中涉及的一些基本概念进行介绍（可以用 CUDA 作为参考，大部分概念都是相似的），然后会以具体的矩阵加法和乘法算子的代码实现为例进行讲解。

# 二、基本概念

## 2.1 Device

- Host：一般指 CPU（负责调度）；
- Device：一般指 GPU、NPU（负责计算）。

## 2.2 Context

Context 主要负责管理线程中各项资源的生命周期。

一般来说，Context 与其它概念之间具有以下关系：

- 一个进程可以创建多个 Context；
- 一个线程只能同时使用一个 Context，该 Context 对应一个唯一的 Device，线程可以通过切换 Context 来切换要使用的 Device；
- 一个 Device 可以拥有多个 Context，但同时只能使用一个 Context。

每一个线程都具有一个默认的 Context，无需手动创建，也无法被删除。我们也可以手动创建更多的 Context，使用后需要及时释放。另外，在线程中，默认使用最后一次创建的 Context。

## 2.3 Stream

Stream 主要负责维护一些异步操作的执行顺序，这些操作包括：

- Host 到 Device 的数据传输；
- 调用 Kernel；
- 其它由 Host 发起并由 Device 执行的动作。

> 说明：在 GPU/NPU 上调用的函数，被称为核函数（Kernel function）。核函数使用 `__global__` 关键字进行定义，会被 GPU/NPU 上的多个线程执行。

同一个 Stream 里的操作是严格串行的（顺序执行），而不同 Stream 之间则可以并行执行。来自不同 Stream 的 Kernel 可以共享 GPU/NPU 的内核并发执行。

一般来说，Context 与其它概念之间具有以下关系：

- 一个线程或 Context 中可以创建多个 Stream；
- 不同线程或 Context 间的 Stream 在 Device 上相互隔离。

每一个 Context 都具有一个默认的 Stream，无需手动创建，也无法被删除。我们也可以手动创建更多的 Stream，并将多个操作分配到不同的 Stream 上，这样就可以实现多个操作的并行，Stream 使用后需要及时释放。

## 2.4 Task

Task 或 Kernel，是 Device 上真正的任务执行体。

一般来说，Task 与其它概念之间具有以下关系：

- 一个 Stream 中可以下发多个 Task；
- 多个 Task 之间可以插入 Event，用于同步不同 Stream 之间的 Task。

![1](./images/算子开发基本概念.png)

## 2.5 参考资料

- [<u>Ascend 算子开发基本概念</u>](https://www.hiascend.com/doc_center/source/zh/CANNCommunityEdition/80RC3alpha001/devguide/appdevg/aclpythondevg/aclpythondevg_0004.html)；
- [<u>CUDA 基础</u>](https://www.cnblogs.com/LLW-NEU/p/16219611.html)；
- [<u>CUDA 介绍</u>](https://juniorprincewang.github.io/2018/01/12/CUDA-logic/)。

# 三、单算子开发

AscendCL 官方介绍：

> AscendCL（Ascend Computing Language）是一套用于在昇腾平台上开发深度神经网络应用的 C 语言 API 库，提供运行资源管理、内存管理、模型加载与执行、算子加载与执行、媒体数据处理等 API，能够实现利用昇腾硬件计算资源、在昇腾 CANN 平台上进行深度学习推理计算、图形图像预处理、单算子加速计算等能力。简单来说，就是统一的 API 框架，实现对所有资源的调用。
> 面向算子开发场景的编程语言 Ascend C，原生支持 C/C++ 标准规范，最大化匹配用户开发习惯；通过多层接口抽象、自动并行计算、孪生调试等关键技术，极大提高算子开发效率，助力 AI 开发者低成本完成算子开发和模型调优部署。

## 3.1 单算子调用方式

- 单算子 API 执行：
  - 直接调用 CANN 已经提供的算子 API；
  - 使用 Ascend C 开发并调用自定义算子。
- 单算子模型执行。

## 3.2 单算子 API 执行

- NN 算子；
- DVPP 算子；
- 融合算子；
- ……

> 更详细的算子 API 文档可以参考：[<u>算子加速库接口</u>](https://www.hiascend.com/document/detail/zh/CANNCommunityEdition/80RC3alpha003/apiref/aolapi/operatorlist_0001.html)。

**两段式接口**：单算子 API 执行时，针对每个算子，都需要依次先调用 `aclxxXxxGetWorkspaceSize()` 接口获取算子执行需要的 workspace 内存大小、再调用 `aclxxXxx()` 接口执行算子。

## 3.3 参考资料

- [<u>单算子调用基础知识</u>](https://www.hiascend.com/document/detail/zh/CANNCommunityEdition/80RC3alpha003/devguide/appdevg/aclcppdevg/aclcppdevg_000016.html)；
- [<u>Ascend 开源融合算子</u>](https://gitee.com/ascend/cann-ops-adv)。

# 四、代码实现

本小节将以 `aclnnAdd` 和 `aclnnMatmul` 算子为例，实现具体的代码。

更详细的 API 文档可以参考：

- 加法算子：[<u>aclnnAdd</u>](https://www.hiascend.com/document/detail/zh/CANNCommunityEdition/80RC3alpha003/apiref/aolapi/context/common/aclnn_domains.md?sub_id=%2Fzh%2FCANNCommunityEdition%2F80RC3alpha003%2Fapiref%2Faolapi%2Fcontext%2FaclnnAdd%26aclnnInplaceAdd.md)；
- 乘法算子：[<u>aclnnMatmul</u>](https://www.hiascend.com/document/detail/zh/CANNCommunityEdition/80RC3alpha003/apiref/aolapi/context/aclnnMatmul.md)。

## 4.1 环境搭建

- [<u>快速安装昇腾环境</u>](https://ascend.github.io/docs/sources/ascend/quick_install.html)；
- [<u>基于 EulerOS & Ascend NPU 搭建 PyTorch 远程开发环境</u>](https://blog.csdn.net/weixin_44162047/article/details/142502025?spm=1001.2014.3001.5502)。

## 4.2 单算子开发流程

![2](./images/单算子调用功能开发流程.png)

![3](./images/单算子API调用流程.png)

## 4.3 常见参数说明

- `strides`：描述 Tensor 维度上相邻两个元素的间隔，详见[<u>非连续的 Tensor</u>](https://www.hiascend.com/document/detail/zh/CANNCommunityEdition/80RC3alpha003/apiref/aolapi/context/common/%E9%9D%9E%E8%BF%9E%E7%BB%AD%E7%9A%84Tensor.md)；
- `workspace`：在 device 侧申请的 workspace 内存地址；
- `workspaceSize`：在 device 侧申请的 workspace 大小；
- `executor`：算子执行器，实现了算子的计算流程；
- `aclnnStatus`：详见[<u>aclnn 返回码</u>](https://www.hiascend.com/document/detail/zh/CANNCommunityEdition/80RC3alpha003/apiref/aolapi/context/common/aclnn%E8%BF%94%E5%9B%9E%E7%A0%81.md)。

**注意事项**：

- 多个输入数据之间，数据类型需要满足[<u>互推导关系</u>](https://www.hiascend.com/document/detail/zh/CANNCommunityEdition/80RC3alpha003/apiref/aolapi/context/common/%E4%BA%92%E6%8E%A8%E5%AF%BC%E5%85%B3%E7%B3%BB.md)：当一个 API（如 `aclnnAdd()`、`aclnnMul()` 等）输入的 Tensor 数据类型不一致时，API 内部会推导出一个数据类型，将输入数据转换成该数据类型进行计算；
- 多个输入数据之间，shape 需要满足[<u>广播关系</u>](https://www.hiascend.com/document/detail/zh/CANNCommunityEdition/80RC3alpha003/apiref/aolapi/context/common/broadcast%E5%85%B3%E7%B3%BB.md)：在某些情况下，较小的数组可以“广播至”较大的数组，使两者shape互相兼容；
- 更多算子 API 信息详见：[<u>CANN 社区版开发文档</u>](https://www.hiascend.com/document/detail/zh/CANNCommunityEdition/80RC3alpha003/quickstart/quickstart/quickstart_18_0001.html)，位置：【CANN 社区版 -> 8.0.RC3.alpha003 -> API 参考 -> 算子加速库接口 -> NN 算子接口】。

## 4.4 矩阵加法算子

目录结构：

```bash
sss@xxx:~/xxx/add$ tree
.
|-- CMakeLists.txt
|-- build
`-- test_add.cpp
```

CMakeLists：

```bash
# Copyright (c) Huawei Technologies Co., Ltd. 2019. All rights reserved.

# CMake lowest version requirement
cmake_minimum_required(VERSION 3.14)

# 设置工程名
project(ACLNN_EXAMPLE)

# Compile options
add_compile_options(-std=c++11)

# 设置编译选项
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY  "./bin")
set(CMAKE_CXX_FLAGS_DEBUG "-fPIC -O0 -g -Wall")
set(CMAKE_CXX_FLAGS_RELEASE "-fPIC -O2 -Wall")

# 设置可执行文件名（如opapi_test），并指定待运行算子文件*.cpp所在目录
add_executable(opapi_add_test
               ../test_add.cpp)

# 设置ASCEND_PATH（CANN软件包目录，请根据实际路径修改）和INCLUDE_BASE_DIR（头文件目录）
if(NOT "$ENV{ASCEND_CUSTOM_PATH}" STREQUAL "")
    set(ASCEND_PATH $ENV{ASCEND_CUSTOM_PATH})
else()
    set(ASCEND_PATH "/home/sss/Ascend/ascend-toolkit/latest")  # 示例：/usr/local/Ascend/ascend-toolkit/latest
endif()
set(INCLUDE_BASE_DIR "${ASCEND_PATH}/include")
include_directories(
    ${INCLUDE_BASE_DIR}
    ${INCLUDE_BASE_DIR}/aclnn
)

# 设置链接的动态库文件路径
# arch表示操作系统架构，os表示操作系统
target_link_libraries(opapi_test PRIVATE
                      ${ASCEND_PATH}/lib64/libascendcl.so
                      ${ASCEND_PATH}/lib64/libnnopbase.so
                      ${ASCEND_PATH}/lib64/libopapi.so)
# 可执行文件在CMakeLists文件所在目录的bin目录下
install(TARGETS opapi_test DESTINATION ${CMAKE_RUNTIME_OUTPUT_DIRECTORY})
```

编译构建：

```bash
mkdir build
cd build
cmake .. -DCMAKE_CXX_COMPILER=g++ -DCMAKE_SKIP_RPATH=TRUE
make
```

`test_add` 代码：

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

  // 4.（ 固定写法）同步等待任务执行结束
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

运行程序：

```bash
./opapi_add_test
```

运行结果：

```bash
sss@xxx:~/xxx/add/build/bin$ ./opapi_test 
result[0] is: 1.200000
result[1] is: 2.200000
result[2] is: 3.200000
result[3] is: 5.400000
result[4] is: 6.400000
result[5] is: 7.400000
result[6] is: 9.600000
result[7] is: 10.600000
```

## 4.5 矩阵乘法算子

目录结构：

```bash
sss@xxx:~/xxx/mul$ tree
.
|-- CMakeLists.txt
|-- build
`-- test_mul.cpp
```

CMakeLists：

```bash
# Copyright (c) Huawei Technologies Co., Ltd. 2019. All rights reserved.

# CMake lowest version requirement
cmake_minimum_required(VERSION 3.14)

# 设置工程名
project(ACLNN_EXAMPLE)

# Compile options
add_compile_options(-std=c++11)

# 设置编译选项
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY  "./bin")
set(CMAKE_CXX_FLAGS_DEBUG "-fPIC -O0 -g -Wall")
set(CMAKE_CXX_FLAGS_RELEASE "-fPIC -O2 -Wall")

# 设置可执行文件名（如opapi_test），并指定待运行算子文件*.cpp所在目录
add_executable(opapi_mul_test
               ../test_mul.cpp)

# 设置ASCEND_PATH（CANN软件包目录，请根据实际路径修改）和INCLUDE_BASE_DIR（头文件目录）
if(NOT "$ENV{ASCEND_CUSTOM_PATH}" STREQUAL "")
    set(ASCEND_PATH $ENV{ASCEND_CUSTOM_PATH})
else()
    set(ASCEND_PATH "/home/sss/Ascend/ascend-toolkit/latest")  # 示例：/usr/local/Ascend/ascend-toolkit/latest
endif()
set(INCLUDE_BASE_DIR "${ASCEND_PATH}/include")
include_directories(
    ${INCLUDE_BASE_DIR}
    ${INCLUDE_BASE_DIR}/aclnn
)

# 设置链接的动态库文件路径
# arch表示操作系统架构，os表示操作系统
target_link_libraries(opapi_test PRIVATE
                      ${ASCEND_PATH}/lib64/libascendcl.so
                      ${ASCEND_PATH}/lib64/libnnopbase.so
                      ${ASCEND_PATH}/lib64/libopapi.so)
# 可执行文件在CMakeLists文件所在目录的bin目录下
install(TARGETS opapi_test DESTINATION ${CMAKE_RUNTIME_OUTPUT_DIRECTORY})
```

编译构建：

```bash
mkdir build
cd build
cmake .. -DCMAKE_CXX_COMPILER=g++ -DCMAKE_SKIP_RPATH=TRUE
make
```

`test_mul` 代码：

```cpp
#include <iostream>
#include <vector>
#include "acl/acl.h"
#include "aclnnop/aclnn_matmul.h"

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
  // 1.初始化
  int32_t deviceId = 0;
  aclrtStream stream;
  auto ret = Init(deviceId, &stream);
  CHECK_RET(ret == 0, LOG_PRINT("Init acl failed. ERROR: %d\n", ret); return ret);

  // 2.准备数据
  // 矩阵 1
  std::vector<float> mat1HostData = {1, 2, 3, 4, 5, 6};
  std::vector<int64_t> mat1Shape = {3, 2};
  void* mat1DeviceAddr = nullptr;
  aclTensor* mat1 = nullptr;
  ret = CreateAclTensor(mat1HostData, mat1Shape, &mat1DeviceAddr, aclDataType::ACL_FLOAT, &mat1);
  CHECK_RET(ret == ACL_SUCCESS, LOG_PRINT("CreateAclTensor for mat1 failed. ERROR: %d\n", ret); return ret);
  // 矩阵 2
  std::vector<float> mat2HostData = {1, 2, 3, 4, 5, 6};
  std::vector<int64_t> mat2Shape = {2, 3};
  void* mat2DeviceAddr = nullptr;
  aclTensor* mat2 = nullptr;
  ret = CreateAclTensor(mat2HostData, mat2Shape, &mat2DeviceAddr, aclDataType::ACL_FLOAT, &mat2);
  CHECK_RET(ret == ACL_SUCCESS, LOG_PRINT("CreateAclTensor for mat2 failed. ERROR: %d\n", ret); return ret);
  // 结果矩阵
  std::vector<float> outHostData = {0, 0, 0, 0, 0, 0, 0, 0, 0};
  std::vector<int64_t> outShape = {3, 3};
  void* outDeviceAddr = nullptr;
  aclTensor* out = nullptr;
  ret = CreateAclTensor(outHostData, outShape, &outDeviceAddr, aclDataType::ACL_FLOAT, &out);
  CHECK_RET(ret == ACL_SUCCESS, LOG_PRINT("CreateAclTensor for out failed. ERROR: %d\n", ret); return ret);

  // 3.调用 CANN 算子库 API
  uint64_t workspaceSize = 0;
  aclOpExecutor* executor;
  int8_t cubeMathType = 1;
  // 计算 device 内存
  ret = aclnnMatmulGetWorkspaceSize(mat1, mat2, out, cubeMathType, &workspaceSize, &executor);
  CHECK_RET(ret == ACL_SUCCESS, LOG_PRINT("aclnnMatmulGetWorkspaceSize failed. ERROR: %d\n", ret); return ret);
  // 申请 device 内存
  void* workspaceAddr = nullptr;
  if (workspaceSize > 0) {
    ret = aclrtMalloc(&workspaceAddr, workspaceSize, ACL_MEM_MALLOC_HUGE_FIRST);
    CHECK_RET(ret == ACL_SUCCESS, LOG_PRINT("allocate workspace failed. ERROR: %d\n", ret); return ret;);
  }
  // 执行计算过程
  ret = aclnnMatmul(workspaceAddr, workspaceSize, executor, stream);
  CHECK_RET(ret == ACL_SUCCESS, LOG_PRINT("aclnnMatmul failed. ERROR: %d\n", ret); return ret);

  // 4.等待计算结果
  ret = aclrtSynchronizeStream(stream);
  CHECK_RET(ret == ACL_SUCCESS, LOG_PRINT("aclrtSynchronizeStream failed. ERROR: %d\n", ret); return ret);

  // 5.将 device 侧内存上的结果拷贝至 host 侧
  auto size = GetShapeSize(outShape);
  std::vector<float> resultData(size, 0);
  ret = aclrtMemcpy(resultData.data(), resultData.size() * sizeof(resultData[0]), outDeviceAddr, size * sizeof(float),
                    ACL_MEMCPY_DEVICE_TO_HOST);
  CHECK_RET(ret == ACL_SUCCESS, LOG_PRINT("copy result from device to host failed. ERROR: %d\n", ret); return ret);
  for (int64_t i = 0; i < size; i++) {
    LOG_PRINT("result[%ld] is: %f\n", i, resultData[i]);
  }

  // 6.释放 aclTensor
  aclDestroyTensor(mat1);
  aclDestroyTensor(mat1);
  aclDestroyTensor(out);

  // 7.释放 device 资源
  aclrtFree(mat1DeviceAddr);
  aclrtFree(mat2DeviceAddr);
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

运行程序：

```bash
./opapi_mul_test
```

运行结果：

```bash
sss@xxx:~/xxx/mul/build/bin$ ./opapi_test 
result[0] is: 9.000000
result[1] is: 12.000000
result[2] is: 15.000000
result[3] is: 19.000000
result[4] is: 26.000000
result[5] is: 33.000000
result[6] is: 29.000000
result[7] is: 40.000000
result[8] is: 51.000000
```

## 4.6 参考资料

- [<u>Ascend 算子开发指南</u>](https://github.com/wangshuai09/Notebook/blob/main/Ascend%E7%AE%97%E5%AD%90%E5%BC%80%E5%8F%91%E6%8C%87%E5%8D%97/aclnn%E7%AE%97%E5%AD%90%E5%BC%80%E5%8F%91%E6%8C%87%E5%8D%97.md)；
- [<u>CANN 社区版开发文档</u>](https://www.hiascend.com/document/detail/zh/CANNCommunityEdition/80RC3alpha003/quickstart/quickstart/quickstart_18_0001.html)；
- [<u>调用 NN 算子接口示例代码</u>](https://www.hiascend.com/document/detail/zh/CANNCommunityEdition/80RC3alpha003/devguide/appdevg/aclcppdevg/aclcppdevg_000019.html)；
- [<u>算子加速库接口</u>](https://www.hiascend.com/document/detail/zh/CANNCommunityEdition/80RC3alpha003/apiref/aolapi/operatorlist_0001.html)。

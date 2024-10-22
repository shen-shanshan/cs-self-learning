# Ascend aclnn 算子开发入门

## 概述

**什么是算子？**

在 AI 框架中，算子一般指一些最基本的代数运算（如：矩阵加法、矩阵乘法等），多个算子之间也可以根据需要组合成更加复杂的融合算子（如：flash-attention 算子等）。算子的输入和输出都是 Tensor（张量）。

另外，算子更多地是 AI 框架中的一个概念，在硬件底层算子具体的执行部分，一般叫做 kernel（核函数）。

下面将首先对算子开发中涉及的一些基本概念进行介绍（可以用 CUDA 作为参考，大部分概念都是相似的），然后会以具体的矩阵加法和乘法算子的代码实现为例进行讲解。

## 基本概念

### Device

- Host：一般指 CPU（负责调度）；
- Device：一般指 GPU、NPU（负责计算）。

### Context

Context 主要负责管理线程中各项资源的生命周期，一般来说，Context 与其它概念之间具有以下关系：

- 一个进程可以创建多个 Context；
- 一个线程只能同时使用一个 Context，该 Context 对应一个唯一的 Device，线程可以通过切换 Context 来切换要使用的 Device；
- 一个 Device 可以拥有多个 Context，但同时只能使用一个 Context。

每一个线程都具有一个默认的 Context，无需手动创建，也无法被删除。我们也可以手动创建更多的 Context，使用后需要及时释放。另外，在线程中，默认使用最后一次创建的 Context。

### Stream

### Task

### Event

### 总结

画图说明。

> 参考资料：
>
> - [<u>Ascend 算子开发基本概念</u>](https://www.hiascend.com/doc_center/source/zh/CANNCommunityEdition/80RC3alpha001/devguide/appdevg/aclpythondevg/aclpythondevg_0004.html)；
> - [<u>CUDA 基础</u>](https://www.cnblogs.com/LLW-NEU/p/16219611.html)；
> - [<u>CUDA 介绍</u>](https://juniorprincewang.github.io/2018/01/12/CUDA-logic/)。

## 代码示例

### 环境搭建

CANN 环境安装。

### 矩阵加法算子

...

### 矩阵乘法算子

...

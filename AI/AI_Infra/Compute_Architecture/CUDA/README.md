# CUDA 编程学习路线

## 基础知识入门

- 樊哲勇《CUDA 编程：基础与实践》⭐
- [<u>谭升的博客</u>](https://face2ai.com/program-blog/#GPU%E7%BC%96%E7%A8%8B%EF%BC%88CUDA%EF%BC%89)
- [<u>zhang's Blog</u>](https://www.armcvai.cn/categories.html)
- [<u>CUDA stream 和 event 模块详解</u>](https://www.armcvai.cn/2025-03-21/cuda-stream-event.html)

## 算子编程练习

- [<u>Modern CUDA Learn Notes with PyTorch for Beginners</u>](https://github.com/xlite-dev/CUDA-Learn-Notes) ⭐ (由易到难自己从零实现一遍)
- [<u>CUDA Kernel Samples</u>](https://github.com/Tongkaio/CUDA_Kernel_Samples)

**重点算子（面试会手撕）：**

- Reduce
- softmax
- Rms_norm
- layler_norm
- Transpose

## 进阶内容

**常用 CUDA 库：**

- cuDNN
- cuBLAS（线性代数库）
- nsight（性能分析库）
- cuTLASS

**其它内容：**

- bank conflict、roofline 分析、fusion、tiling 策略
- Flash attention v1 v2 v3
- naive softmax -> safe softmax -> online softmax -> FA1 FA2 FA3
- CUDA -> PTX -> SASS

## 经验分享

- [<u>有没有一本讲解 gpu 和 CUDA 编程的经典入门书籍？ - JerryYin777 的回答 - 知乎</u>](https://www.zhihu.com/question/26570985/answer/3465784970)
- [<u>CUDA 学习资料及路线图</u>](https://zhuanlan.zhihu.com/p/273607744)
- [<u>推荐几个不错的 CUDA 入门教程</u>](https://zhuanlan.zhihu.com/p/346910129?utm_psn=1891290780615820759)
- [<u>熬了几个通宵，我写了份 CUDA 新手入门代码（pytorch 自定义算子）</u>](https://zhuanlan.zhihu.com/p/360441891?utm_psn=1891290523299472507)

## 学习资料

- [<u>CUDA C++ Programming Guide - NVIDIA 官方教程</u>](https://docs.nvidia.com/cuda/cuda-c-programming-guide/index.html)
- [<u>CUDA-Programming-Guide-in-Chinese - 官方教程中文版</u>](https://github.com/HeKun-NVIDIA/CUDA-Programming-Guide-in-Chinese?tab=readme-ov-file)
- [<u>Learn CUDA Programming</u>](https://github.com/PacktPublishing/Learn-CUDA-Programming)
- [<u>CUDA Runtime API</u>](https://docs.nvidia.com/cuda/cuda-runtime-api/index.html)

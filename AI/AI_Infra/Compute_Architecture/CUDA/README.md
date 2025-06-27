# CUDA 编程学习路线

## 基础知识入门

- 樊哲勇《CUDA 编程：基础与实践》⭐
- 程润伟《CUDA C 编程权威指南》
- [<u>zhang's Blog</u>](https://www.armcvai.cn/categories.html)
- [<u>谭升的博客</u>](https://face2ai.com/program-blog/#GPU%E7%BC%96%E7%A8%8B%EF%BC%88CUDA%EF%BC%89)

## 算子编程练习

- [<u>Modern CUDA Learn Notes with PyTorch for Beginners ⭐</u>](https://github.com/xlite-dev/CUDA-Learn-Notes) (由易到难自己从零实现一遍)
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

## 参考资料

- [<u>CUDA C++ Programming Guide - NVIDIA 官方教程</u>](https://docs.nvidia.com/cuda/cuda-c-programming-guide/index.html)
- [<u>CUDA-Programming-Guide-in-Chinese - 官方教程中文版</u>](https://github.com/HeKun-NVIDIA/CUDA-Programming-Guide-in-Chinese?tab=readme-ov-file)
- [<u>Learn CUDA Programming</u>](https://github.com/PacktPublishing/Learn-CUDA-Programming)
- [<u>CUDA Runtime API</u>](https://docs.nvidia.com/cuda/cuda-runtime-api/index.html)

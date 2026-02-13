# CUDA 编程学习路线

## 基础知识入门

- 樊哲勇《CUDA 编程：基础与实践》⭐
- 程润伟《CUDA C 编程权威指南》
- [<u>NVIDIA CUDA C++ Programming Guide ⭐</u>](https://docs.nvidia.com/cuda/cuda-c-programming-guide/index.html#)
- [<u>Zhang's Blog</u>](https://www.armcvai.cn/categories.html)
- [<u>谭升的博客</u>](https://face2ai.com/program-blog/#GPU%E7%BC%96%E7%A8%8B%EF%BC%88CUDA%EF%BC%89)

## 算子编程练习

- [<u>LeetCUDA ⭐</u>](https://github.com/xlite-dev/LeetCUDA)
- [<u>CUDA Kernel Samples</u>](https://github.com/Tongkaio/CUDA_Kernel_Samples)

**重点算子（面试会手撕）：**

- Reduce
- Softmax
- Rms_Norm
- Layer_Norm
- Transpose

## 进阶内容

**常用 CUDA 库：**

- cuBLAS（线性代数库）
- Nsight（性能分析库）
- cuTLASS（进阶必会）
- cuDNN

**其它内容：**

- bank conflict、roofline 分析、fusion、tiling 策略
- Flash attention v1 v2 v3
- naive softmax -> safe softmax -> online softmax -> FA1 FA2 FA3
- CUDA -> PTX -> SASS

**学习资料：**

- PTX 指令集手册

## 参考资料

- [<u>GPU MODE ⭐</u>](https://github.com/gpu-mode)
- [<u>Learn CUDA Programming</u>](https://github.com/PacktPublishing/Learn-CUDA-Programming)
- [<u>CUDA Runtime API</u>](https://docs.nvidia.com/cuda/cuda-runtime-api/index.html)

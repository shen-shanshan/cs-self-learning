# 卷积优化

## 卷积基本原理

**convolution**：每一次卷积都是在做数乘（对应位置的元素相乘并相加），最后多个通道的卷积结果再叠加（求和）。

![](./images/conv_1.png)

> 注意：卷积核一般都使用正方形（在二维空间上），kernel_size 即正方形的边长（如上图中的 kernel_size 为 3）。

每个输出通道都有独立的三维卷积核（filter）。

![](./images/conv_2.png)

卷积层的基本参数有 4 个（4 维 Tensor）：卷积核个数 m、输入图像的 C、卷积核的 W（=kernel_size）、卷积核的 H（=kernel_size）。

总结：

- 输入的通道数，决定了卷积核的层数（C）；
- 卷积核（3 维）的个数，决定了输出的通道数。

**padding**：使用 0 填充图像的边框，从而调整卷积后的输出大小。

## vllm 卷积优化

img2col 算法。

## Benchmark

算子介绍 & 性能对比

PR：https://github.com/vllm-project/vllm/pull/28455

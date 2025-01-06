# FlashAttention

## Attention 计算方式演进过程

- Standard Self-Attention (3-pass)
- safe-softmax (3-pass)
- online-softmax (2-pass)
- FlashAttention (1-pass)

**safe-softmax**：需要对 `[1, N]` 重复遍历 3 次。

1. 找每个位置中 `Q*K^T` 的最大值；
2. 累加每个位置，得到分母；
3. 每个位置依次除以分母。

## 参考资料

- [[Attention优化][2w字]🔥原理篇: 从Online-Softmax到FlashAttention V1/V2/V3](https://zhuanlan.zhihu.com/p/668888063?utm_psn=1750869971390193665)

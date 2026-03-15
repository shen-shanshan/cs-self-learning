# From Models to Operators: Rethinking Autoscaling Granularity for Large Generative Models

## What is the paper about?

We propose an **operator-level autoscaling framework**, which allocates resources at finer (operator)-granularity, optimizing the scaling, batching, and placement based on individual operator profiles.

## What is new compared to prior work?

**Contributions:**

- **Characterization of diverse operator** compute–memory sensitivities under varying workload conditions.
- An **operator-level autoscaling strategy** that exploits operator heterogeneity for dynamic resource allocation.
- A **contention-aware operator placement strategy** that balances execution efficiency and cost.

**Analysis:**

- **Compute Characteristics:**
  - Prefill attention remains the key scaling challenge for generative model serving under **long sequence lengths (quadratic complexity)**, regardless of model architecture.
  - During decoding, similar trend (but with smaller slopes), the cached KV pairs reduce the computational cost of attention.
  - Feed-forward layers, layer norms, and embedding lookups — **scale linearly** with sequence length.
  - Softmax, fill, and sigmoid show **nearly flat curves** in compute sensitivity to sequence length.
  - Most operators exhibit **roughly linear scaling** with batch size.
- **Memory Characteristics:**
  - Attention operators **dominate memory growth** due to their **O(L2) scaling** with sequence length.
  - FlashAttention has **linear memory complexity** to sequence length.
  - Most other operators **grow roughly linearly**.
  - Lightweight operators like index-select and activation kernels show **flatter growth**.

总结：Attention 在 prefill 阶段是计算密集型（随着 seq_len 呈二次增长），在 decode 阶段是访存密集型（使用 KV Cache）；element-wise 类型的算子，如激活，是访存密集型；线性矩阵乘在小 batch 时也是访存密集型，随着 batch_size 增大达到 roofline 变为计算密集型，对于 MoE 模型，达到 roofline 所需的 batch_size 更大（更平缓，对 batch_size 更不敏感）。

**Key Insights:**

1. Operator compute sensitivity varies widely, with attention dominating across model architectures due to quadratic complexity. MoE and Encoder-LLM models exhibit more operators with flat scaling curves.
2. Memory sensitivity is more evenly distributed across operators compared to compute sensitivity, consistent across model architectures. The two dimensions are uncorrelated—operators may be intensive in both, one, or neither. Unlike compute, memory sensitivity is bounded by linear scaling with FlashAttention.
3. ...

## What experiments were run to support the arguments in this paper?

...

## What are the shortcomings/limitations of this paper?

多模态：VL 模型？diffusion 模型？
实际落地：缺少开源工程实现？

## What is a reasonable next step to build upon this paper?

分析 AFD 的弹性影响因素 -> 设计调度策略？

## Basic Concepts (Related Knowledge)

...

## References (Related Works)

...

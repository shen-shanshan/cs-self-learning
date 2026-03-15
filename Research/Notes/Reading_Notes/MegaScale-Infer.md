# MegaScale-Infer: Serving Mixture-of-Experts at Scale with Disaggregated Expert Parallelism

## What is the paper about?

MegaScale-Infer disaggregates attention and FFN modules within each model layer, enabling independent scaling, tailored parallelism strategies, and heterogeneous deployment for both modules.

**Key advantages:**

- Independent scaling
- Heterogeneous deployment

**Motivation:**

FFN 在 batch_size 较小时，是**访存密集型**；随着 batch_size 增大，逐渐逼近 roofline，FFN 变为**计算密集型**。
对于 MoE 模型，FFN（expert）达到 roofline 所需的 batch_size 更大。

**为什么不能无限增大 batch_size？**

1. batch_size 过大，会影响在线服务单个请求的延迟，影响用户体验；
2. Attention KV Cache 占用大量显存，限制了 batch_size 的增大；
3. 通过并行策略可以增大 batch_size，但是会引入额外的通信开销。

## What is new compared to prior work?

- **Ping-pong pipeline parallelism** -> 掩盖 A 和 F 互相等待的时间；
- **High-performance M2N communication library** -> 定制化的 P2P 通信库，解决通信的性能问题。

**Disaggregated expert parallelism:**

- Attention: DP + TP
- Expert: EP + TP

## What experiments were run to support the arguments in this paper?

**Testbed:**

- Attention 集群：8 机（8 卡/机）、同构 GPU；
- Expert 集群：异构 GPU。

**Metrics:**

Focus on maximizing the cost-normalized **decoding throughput**, subject to a specified **time-between-tokens (TBT)** latency constraint (**150 ms**).

- Homogeneous deployment: **per-GPU** decoding throughput.
- Heterogeneous deployment: **per-cost** decoding throughput.

## What are the shortcomings/limitations of this paper?

**专家负载不均：**

- **Across layer:** There is a significant imbalance in the load distribution, with some experts being substantially hotter than others.
- **Across batch:** During the decoding phase, expert load remains relatively stable across batches, whereas in the prefill phase, it fluctuates more significantly.

**Attention 变长序列：**

Due to variations in sequence lengths, attention nodes can experience significantly different computation times even when processing batches of the same size.

Such imbalances can introduce bubbles and degrade overall system efficiency.

We then compose the batch on each attention node to match a predefined target execution time, thereby balancing the workload across nodes.

## What is a reasonable next step to build upon this paper?

AFD 弹性与容错。

## Basic Concepts (Related Knowledge)

...

## References (Related Works)

...

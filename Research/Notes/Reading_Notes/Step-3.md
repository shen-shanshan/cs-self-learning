# Step-3 is Large yet Affordable: Model-system Co-design for Cost-effective Decoding

## What is the paper about?

...

## What is new compared to prior work?

**DeepSeek DeepEP 的局限性：**

1. **batchsize 要足够大**：如果不够大，expert 不能完全激活，那么此时 MFU（model float util）会很低，这个其实增加 expert 的激活数就可以解决，但是 h800 通信受限，为了不受通信带宽的影响，被迫做得高度稀疏，减少 all2all 通信量；
2. **EP 负载均衡**：deepseek 特别强调了要 EP 负载均衡，因为稍有不慎，系统性能将会被多干活的 expert card 拖累；
3. **大规模 EP**：这个方法足够 general 吗？成也大规模或许"瓶颈"也在于大规模 EP，在 deepseek r1/v3 上或许可行，假设后面专家数更多了，达到了 1024 甚至 2048 了，那么这个时候 batchsize 还能够大到把每个专家都激活？并且这个时候还有负载均衡的挑战，那这个就未必了。

**Step3 的改进点：**

1. **部署规模降低**：DSv3 需要 320 块 GPU 运行一个 decoding 实例，而 Step3 仅需 32 块；
2. **Attention-FFN 分离（AFD）**：Attention 和 FFN 具有不同的计算访存特点，FFN 与 context length 无关，attention 与 context length 相关，那么分离开来，解除了 context length 对 FFN 的影响，我专注于堆 batchsize 就可以打满 FFN 阶段的 MFU；
3. **负载均衡得到高效缓解**：EP 存在众所周知的负载不平衡问题，而 AFD 可以结合 TP-EP 混合策略来平衡计算效率、通信流量和负载均衡；
4. **AFD 支持更加灵活的硬件部署，为更多芯片带来了机会**：AFD 支持把 attention 和 FFN 映射到不同特点的芯片来计算，然而大规模 EP 强制同构芯片。

**Step3 的研究结论：**

1. decoding 阶段的最大开销来自于 attention 的设计，而不是总参数量或者 moe 的激活参数量；
2. attention 的 decoding 开销不仅仅来自于 kv cache，还有计算访存比，有的对于某些硬件来说太高了，导致计算量严重拖累了 decoding 速度，因此需要根据不同的硬件特点设计 attention，比如 MLA 512 的计算访存比在 h800 上面性能很不错，但是一到 h20 或者 a800 就会存在计算访存比过高的情况。为此 step3 设计出了 MFA，可以较为均衡的支持多种硬件；
3. AFD 部署，attention 和 FFN 在 decoding 阶段存在不同的特点，前者和 context length 相关，后者和 context length 无关，因此可以分而治之，使得后者在足够 batchsize 的情况下可以实现高 MFU，并且 attention 实例可以通过很容易的 scaling 来 handle 动态 context length，不影响到 FFN。

**总结：**

AFD 架构比大规模 EP 架构在 decoding 阶段更加接近于问题本质（attention 和 FFN 的计算访存特点不同），具有更强的伸缩性，使得在一些非旗舰显卡也能低成本部署大模型，这可以说是 deepseek 之后又一架构非常新奇的推理系统。

## What experiments were run to support the arguments in this paper?

...

## What are the shortcomings/limitations of this paper?

1. AFD 只适用于推理的 decoding 阶段，不适用于 prefill 和 training 阶段；
2. 该方案可能只与 Step3 的模型配合得较好，在其它模型上的效果不一定好。

## What is a reasonable next step to build upon this paper?

...

## Basic Concepts (Related Knowledge)

**arithmetic intensity (计算强度)**

The ratio of the arithmetic operations needed for each byte of KV accessed from memory. Different batch sizes or context lengths do not change the arithmetic intensity.

Hardware’s **computation-bandwidth ratio (roofline)** = **computation-to-memory access ratio (计算访存比)** ?

The better the match between attention’s arithmetic intensity and a hardware’s “computation-bandwidth ratio” (or
referred to as roofline), the more likely it is to achieve good efficiency on that hardware.

**hardware affinities/friendliness (硬件亲和)**

**floating-point operations (FLOPs)**

**MFU (Model FLOPS Utilization)**

**the sparsity of MoE (denoted as 'S')**

- If 2 experts are chosen from 8, then S = **1/4**.
- If 8 experts are chosen from 256 plus one **shared expert**, then S = **9/256**.

For MoE models, the ideal batch size (denoted as 'B') for high MFU is:

<center>
    B<sub>MoE</sub> * S = B<sub>dense</sub>
</center>

**the network traffic volume (网络流量)**

## References (Related Works)

...

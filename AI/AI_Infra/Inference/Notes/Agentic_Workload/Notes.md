# Notes

## Decode Context Parallelism 的执行流程

标准的 Decode Context Parallelism 通信模式很简单，遵循 AllGather Q → Compute → AllGather + ReduceScatter 这个节奏。

• **AllGather Q**：每张 GPU 只算出了 query 的一个片段，但 attention 需要完整的 query 向量才能与任意 key 做打分。在 DCP 组内做一次 all-gather，就能在每张 GPU 上拼出完整的 query 副本。decode 阶段这一步很便宜，因为 query 只有一个 token。作为 MLA 的可选替代方案，vLLM #45964 可以在加载时把（很小的）query projection 在每个 DCP 组内复制一份，从而让 decode 完全跳过这次 query all-gather（VLLM_DCP_Q_REPLICATE=1）。
• **Compute**：每张 GPU 用拼好的 query 与自己本地那份 KV cache 做 attention。在 vLLM 里，MLA 走的是 k_up，GQA 走的是 tensor_broadcast。
• **AllGather + ReduceScatter（cp_lse_ag_out_rs）**：把各自的部分结果合成真正的输出。AllGather 把每张 GPU 的部分输出与 LSE 共享出去；LSE 值负责对这些部分结果重新加权并合并（即 online-softmax 技巧），ReduceScatter 则把它们求和，并只把属于自己的那一段 head 切片还给每张 GPU。

> online-softmax ?

## 后续工作

我们会为 TP 与 DCP 支持更细粒度的并行规模，让用户对并行布局有更精确的控制，把过度切分白白损失掉的效率拿回来。我们也在开发更好的 DCP all-to-all（A2A）通信 kernel，覆盖多机与单机两种场景，随着上下文长度和设备数增长，减少暴露出来的通信、提升与计算的重叠度。我们还在完善对 MTP 与投机解码的支持，让 DCP 在拿到效率收益的同时不牺牲投机方法带来的延迟优势；并加固 prefill/decode（PD 分离）场景下的支持，使 DCP 在 PD 分离部署中足够稳健。最后，我们希望扩大 DCP 的覆盖面：支持更多后端，并把它与混合模型以及 Dynamic Chunked Pipeline Parallelism 结合起来，让更广泛的负载都能享受到 context parallel 带来的效率提升。

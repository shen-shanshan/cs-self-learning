# Agentic Workload

不断增长的 token 消耗从两个维度给推理基础设施施压：成本与延迟。

- 成本效率决定固定硬件预算下能容纳多少并发 agent；
- 延迟决定每个 agent 走完推理与工具调用循环的速度。

因此，优化 agentic 推理部署，意味着把延迟与成本的整条前沿一起往前推。

## Agentic Workload 的特征

- 长时间运行的多轮会话。 每个会话的轮数中位数为 43。
- 长上下文、短输出。 输入长度中位数为 142K token，输出长度中位数为 444 token。
- 大量 prefix 复用。 prefix cache 命中率超过 96%。
- 子 agent 密集。 44% 的会话至少包含一个子 agent（subagent），这些会话中子 agent rollout 次数的中位数为 4。

每一轮都把最新的工具结果追加到累积的上下文之后，再把整段内容送回模型，因此输入持续增长，而每轮只新增一小段 prefill，请求的绝大部分都是引擎已经见过的 prefix。子 agent 要么从该上下文分叉出去，要么从头开始，其结果在给出最终答案之前会合并回父级。

## Agentic 优化的后续工作

在控制平面，我们可以让路由更明确地区分首轮请求与第 2 轮及之后的请求：前者往往需要长的全新 prefill 来填充 prefix cache，后者则有很高的 cache 复用率与相对较短的追加 prefill。这种分离既能避免队头阻塞，也让我们能在两侧分别配置引擎与并行策略，例如分别使用 PCP 与 CPP，让两侧的效率都最大化。

在执行平面与数据平面，我们正与社区一起推进以下支持：

- Agent 提示信息（agent hints）。 agentic 框架或 harness 可以随请求附带提示信息，例如会话结构、潜在的分叉点与 cache 位置、工具调用延迟或会话生命周期。第一步是通过标准化 API 接收这些提示，然后用它们指导引擎的调度、cache 淘汰策略及其他优化。
- 可编程 KV cache。 不同工作负载需要不同的放置、保留、复制与淘汰策略。一个可编程接口将让用户按自身工作负载模式控制 KV cache 的预取、淘汰或软固定（soft-pinning）。
- 基于会话的 KV cache 管理。 轮次间隔提供了一个机会，可以把保留的 KV 状态提前挪到最可能服务下一轮的 worker 上。在这段空闲时间内预取，能够隐藏传输延迟、减少冷启动式的恢复。

## Decode Context Parallelism

### DCP 的执行流程

标准的 Decode Context Parallelism 通信模式很简单，遵循 AllGather Q → Compute → AllGather + ReduceScatter 这个节奏。

• **AllGather Q**：每张 GPU 只算出了 query 的一个片段，但 attention 需要完整的 query 向量才能与任意 key 做打分。在 DCP 组内做一次 all-gather，就能在每张 GPU 上拼出完整的 query 副本。decode 阶段这一步很便宜，因为 query 只有一个 token。作为 MLA 的可选替代方案，vLLM #45964 可以在加载时把（很小的）query projection 在每个 DCP 组内复制一份，从而让 decode 完全跳过这次 query all-gather（VLLM_DCP_Q_REPLICATE=1）。
• **Compute**：每张 GPU 用拼好的 query 与自己本地那份 KV cache 做 attention。在 vLLM 里，MLA 走的是 k_up，GQA 走的是 tensor_broadcast。
• **AllGather + ReduceScatter（cp_lse_ag_out_rs）**：把各自的部分结果合成真正的输出。AllGather 把每张 GPU 的部分输出与 LSE 共享出去；LSE 值负责对这些部分结果重新加权并合并（即 online-softmax 技巧），ReduceScatter 则把它们求和，并只把属于自己的那一段 head 切片还给每张 GPU。

> Q：online-softmax ?

### DCP 的后续工作

我们会为 TP 与 DCP 支持更细粒度的并行规模，让用户对并行布局有更精确的控制，把过度切分白白损失掉的效率拿回来。我们也在开发更好的 DCP all-to-all（A2A）通信 kernel，覆盖多机与单机两种场景，随着上下文长度和设备数增长，减少暴露出来的通信、提升与计算的重叠度。我们还在完善对 MTP 与投机解码的支持，让 DCP 在拿到效率收益的同时不牺牲投机方法带来的延迟优势；并加固 prefill/decode（PD 分离）场景下的支持，使 DCP 在 PD 分离部署中足够稳健。最后，我们希望扩大 DCP 的覆盖面：支持更多后端，并把它与混合模型以及 Dynamic Chunked Pipeline Parallelism 结合起来，让更广泛的负载都能享受到 context parallel 带来的效率提升。

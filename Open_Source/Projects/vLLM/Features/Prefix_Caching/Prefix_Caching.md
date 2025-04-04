# Prefix Caching

## 基本原理

大模型推理中的 Prefix Cache（前缀缓存）是一种优化技术，主要用于减少重复计算并加速生成过程中的注意力机制，尤其在处理相同或相似输入前缀的场景中效果显著。以下是其核心要点：

### 背景：传统 KVCache 的局限性

- **KVCache**：在自回归生成中，模型会缓存已生成 Token 的键值向量（Key-Value），避免重复计算历史 Token 的注意力。
- **问题**：当多个请求包含相同的前缀（如相同提示词、重复问题）时，每个请求需独立计算并存储重复的前缀 KVCache，浪费计算资源和显存。

### Prefix Cache的核心思想

- **共享缓存**：将相同输入前缀的 KVCache 存储为共享缓存，供后续相同前缀的请求直接复用，避免重复计算。
- **适用场景**：
  - 多个用户输入相同提示（如 FAQ 回答、通用指令）。
  - 同一用户多次请求中重复使用部分上下文（如多轮对话中的固定引导语）。

### 实现方式

- **缓存管理**：
  - 识别输入前缀的哈希值或唯一标识，匹配已缓存的 KVCache。
  - 动态缓存热门前缀（如 LRU 策略），平衡显存占用与命中率。
- **计算融合**：
  - 对匹配前缀的请求，仅计算新输入部分的 KVCache，与缓存的旧部分拼接。
  - 解码时，注意力机制直接读取拼接后的全局 KVCache。

### 核心优势

- **减少计算量**：避免重复计算相同前缀的注意力，提升吞吐量（尤其高并发场景）。
- **降低显存占用**：多个请求共享同一份前缀缓存，减少冗余存储。
- **加速响应**：命中缓存的请求可直接跳过部分计算，降低延迟。

### 挑战与注意事项

- **前缀匹配精度**：需保证前缀完全一致（包括分词、位置编码等），否则缓存失效。
- **动态更新**：若前缀对应的生成结果依赖外部数据（如实时信息），需设计缓存刷新机制。
- **显存权衡**：缓存过多前缀可能挤占生成所需的显存，需动态淘汰策略。

### 应用场景

- **高频重复请求**：客服机器人回答标准化问题、API 服务中的通用提示词。
- **多轮对话系统**：固定引导语（如“请生成一首关于春天的诗”）的缓存复用。
- **批量生成任务**：并行生成多个相似输入的结果（如广告文案批量生成）。

### 对比 Chunked Prefill

- **Chunked Prefill**：解决单次长序列处理的显存和计算问题，分块填充 KVCache。
- **Prefix Cache**：解决跨请求重复前缀的冗余计算问题，通过共享缓存提升效率。

### 总结

Prefix Cache 通过复用相同前缀的注意力计算结果，显著降低大模型推理的计算开销和显存占用，尤其适合高并发、多重复请求的业务场景，是推理服务降本增效的关键技术之一。

## 参考资料

- [Automatic Prefix Caching - Example](https://docs.vllm.ai/en/stable/features/automatic_prefix_caching.html)
- [Automatic Prefix Caching - V0](https://docs.vllm.ai/en/stable/design/automatic_prefix_caching.html)
- [Automatic Prefix Caching - V1](https://docs.vllm.ai/en/stable/design/v1/prefix_caching.html)
- [SGLang RadixAttention](https://lmsys.org/blog/2024-01-17-sglang/)

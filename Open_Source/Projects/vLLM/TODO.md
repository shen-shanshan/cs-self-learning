# TODO

## vllm-ascend

## vllm

- Structured Output：将 `validate_grammar()` 抽象到 Processor 的一个类中，放到 so 目录下
- Structured Output：guided decoding 支持 outlines，[details](https://docs.vllm.ai/en/stable/getting_started/v1_user_guide.html#features-to-be-supported)
- Structured Output: XgrammarGramma (TODO), support max_rollback_tokens for jump-forward decoding, [details](https://xgrammar.mlc.ai/docs/api/python/index.html#xgrammar.GrammarMatcher.find_jump_forward_string)
- V1 特性支持：投机解码（draft model）
- V1 代码中的 `TODO`

## 技术学习

- [vLLM PD 分离方案浅析](https://zhuanlan.zhihu.com/p/1889243870430201414?utm_psn=1889596220076426760)
- MLA
- MTP
- 空泡
- pipeline parallel
- moe
- torch.compile
- logprobs and prompt logprobs

## 技术博客

- V1 推理流程
- V1 整体架构
- V1 guided decoding
- speculative decoding
- chunked prefills
- prefix caching
- 硬件插件机制
- 新模型支持

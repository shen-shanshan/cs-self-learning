# TODO

## vllm-ascend

## vllm

- Structured Output：guided decoding 支持 outlines，[details](https://docs.vllm.ai/en/stable/getting_started/v1_user_guide.html#features-to-be-supported)
- Structured Output: XgrammarGramma (TODO), support max_rollback_tokens for jump-forward decoding, [details](https://xgrammar.mlc.ai/docs/api/python/index.html#xgrammar.GrammarMatcher.find_jump_forward_string)
- **Bugfix**: [backend_xgrammar.py:148] Failed to advance FSM for request 0 for tokens 4086. Please file an issue.
- **Bugfix:** nanobind: leaked 1 instances! 1 types! 9 functions! nanobind: this is likely caused by a reference counting issue in the binding code.
- Make `xgr.apply_token_bitmask_inplace()` compatibility with spec decode.
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
- speculative decoding
- chunked prefills
- prefix caching
- 硬件插件机制
- 新模型支持

# TODO

## vllm-ascend

- https://github.com/vllm-project/vllm/pull/20355
- 适配 Spec Decode 最新 PR：
  - https://github.com/vllm-project/vllm/pull/20238
  - https://github.com/vllm-project/vllm/pull/20240
- [优化多模态 dummy profile](https://github.com/vllm-project/vllm-ascend/issues/1465)
- 补充文档：https://github.com/vllm-project/vllm-ascend/issues/1248
- 完善 V1 Spec Decode：
  - [N-GRAM](https://github.com/vllm-project/vllm-ascend/pull/874/files)
  - [Adapt mtp with graph mode in v1](https://github.com/vllm-project/vllm-ascend/pull/1023)
  - EAGLE：……
- 适配 Structured Output 叠加 Spec Decode，并补充 UT

## vllm

- [Multi-modality Support on vLLM](https://github.com/vllm-project/vllm/issues/4194)
  - [Multi Modal] V1 spec decode for VLMs
  - [Multi Modal] Better profiling strategy

## 技术学习

- DeepSeek 相关：MLA、MTP、DeepEP
- 分布式并行技术

## 技术博客

- V1 调度机制
- KVCache 管理
- speculative decoding
- chunked prefills（V1 默认启用）
- prefix caching（V1 默认启用）
- continuous batching
- 硬件插件
- 模型支持 & 注册
- 多模态推理（推理 pipeline，以 `Qwen2.5-VL` 为例）
- 性能调优
- PD 分离
- PP 并行

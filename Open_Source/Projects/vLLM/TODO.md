# TODO

## vllm-ascend

- 优化多模态 dummy profile：https://github.com/vllm-project/vllm-ascend/issues/1465
- 适配 Structured Output 叠加 Spec Decode，并补充 UT

## vllm

- Elastic EP:
  - pull: 20775 / issue: 20323
- [Multi-modality Support on vLLM](https://github.com/vllm-project/vllm/issues/4194)
  - [Multi Modal] V1 spec decode for VLMs
  - [Multi Modal] Better profiling strategy

**structured outputs 的 bug：**

- https://github.com/vllm-project/vllm/issues/19493
- https://github.com/vllm-project/vllm/pull/19565
- https://github.com/vllm-project/vllm/pull/18780
- vllm-ascend 支持 outlines 后端

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

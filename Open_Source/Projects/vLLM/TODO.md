# TODO

## vllm-ascend

**New Model：**

**Structured Output：**

- 适配 Structured Output 叠加 Spec Decode，并补充 UT；

**Multi-Modal：**

**其它：**

- 补充文档：https://github.com/vllm-project/vllm-ascend/issues/1248
- 开发 V1 Spec Decode 特性：
  - [N-GRAM](https://github.com/vllm-project/vllm-ascend/pull/874/files)
  - [Adapt mtp with graph mode in v1](https://github.com/vllm-project/vllm-ascend/pull/1023)
  - EAGLE：……

## vllm

- 学习 [Multi-Modal Support](https://docs.vllm.ai/en/latest/contributing/model/multimodal.html)
- 学习 [Multimodal Inputs](https://docs.vllm.ai/en/stable/features/multimodal_inputs.html)
- 了解 [torchcodec](https://github.com/pytorch/torchcodec)；

**待看 PR：**

- https://github.com/vllm-project/vllm/pull/19756
- https://github.com/vllm-project/vllm/pull/19789
- https://github.com/vllm-project/vllm/pull/19715

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

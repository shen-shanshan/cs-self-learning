# TODO

## vllm-ascend

- 跟踪并适配 Structured Output 最新动态；
- 多模态适配：https://github.com/vllm-project/vllm-ascend/issues/673
- 引入 pre-commit 工具
- 创建多模态进展跟踪 Issue

## vllm

- 参与 Spec Decode 特性开发；
- 了解并适配 [torchcodec](https://github.com/pytorch/torchcodec)（多模态相关）；

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

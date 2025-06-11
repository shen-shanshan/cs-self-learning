# TODO

## vllm-ascend

**Structured Output：**

- 适配 Structured Output 叠加 Spec Decode，并补充 UT；

**Multi-Modal：**

- 处理用户 Issue；

**其它：**

- 了解图模式用法 & v1 spec decode PR: http://github.com/vllm-project/vllm-ascend/releases
- 参与 Spec Decode 特性开发；
- 引入 pre-commit 工具；

## vllm

- 学习 [Multi-Modal Support](https://docs.vllm.ai/en/latest/contributing/model/multimodal.html)
- 学习 [Multimodal Inputs](https://docs.vllm.ai/en/stable/features/multimodal_inputs.html)
- 了解 [torchcodec](https://github.com/pytorch/torchcodec)；

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

# TODO

## vllm-ascend

- 完成性能调优文档；
- 开发 V1 Scheduler；
- 跟踪并适配 Structured Output 最新动态；
- 了解并适配 Torchcodec（多模态相关）；
- 多模态适配：https://github.com/vllm-project/vllm-ascend/issues/673

## vllm

- 优化 Spec Decode 相关代码；

## 技术学习

- [vLLM PD 分离方案浅析](https://zhuanlan.zhihu.com/p/1889243870430201414?utm_psn=1889596220076426760)
- DeepSeek 相关：MLA、MTP、DeepEP
- 分布式并行技术
- MoE

## 技术博客

- V1 调度机制
- KVCache 管理
- speculative decoding
- chunked prefills（V1 默认启用）
- prefix caching（V1 默认启用）
- continuous batching
- 硬件插件
- 模型支持
- 多模态（以 Qwen2.5-VL 为例）

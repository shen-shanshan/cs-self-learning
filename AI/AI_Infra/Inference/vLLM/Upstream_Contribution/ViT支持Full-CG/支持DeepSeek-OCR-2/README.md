任务目标：
为 DeepSeek-OCR-2 的 ViT（多模态编码模块）支持 Full CUDA graph，提高其图像推理性能。

前置知识：
1.熟悉 vLLM 基本使用方法、了解其大致代码结构；
2.了解 DeepSeek-OCR 模型结构，了解其多模态输入的处理流程；
3.了解 CUDA graph 的原理与使用方法；
4.了解 GitHub 开源社区贡献基本方法与流程。

参考资料：
1.特性集成 RFC：https://github.com/vllm-project/vllm/issues/38175；
2.PR（https://github.com/vllm-project/vllm/pull/43586）已经为 DeepSeek-OCR 的 ViT 支持了 Full CUDA graph，只需参考该 PR 为 DeepSeek-OCR-2 做实现。

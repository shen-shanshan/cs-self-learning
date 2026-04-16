# 支持 Mixed Inputs

## Prompt

背景：vLLM PR https://github.com/vllm-project/vllm/pull/35963 和 https://github.com/vllm-project/vllm/pull/38061 分别为 Qwen3-VL 的 ViT 模块支持了 Full CUDA Graph 下的图像、视频推理，但是目前并不支持同一请求中既有image、又有video的混合输入。
需求：基于以上两个 PR，继续为 Qwen3-VL 的 ViT 模块设计并实现 image+video 混合输入时的 CUDA Graph 支持。

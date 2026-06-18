RFC 标题：
Support ViT Full CUDA Graph (Tracker)

主要内容：
Add full CUDA graph for the ViT to reduce kernel launch overheads.
需要支持图像、视频推理，需要支持 Qwen3-VL/Qwen3.5/GLM-V/Kimi K2.5 等主流 VLM 模型。

相关 PR：
https://github.com/vllm-project/vllm/pull/35963
https://github.com/vllm-project/vllm/pull/37914
https://github.com/vllm-project/vllm/pull/38040
https://github.com/vllm-project/vllm/pull/38061

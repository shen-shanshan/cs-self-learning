# Step3-VL & XXX-OCR

## Preliminary Study

- [NVIDIA Docs - Getting Started with CUDA Graphs](https://developer.nvidia.com/blog/cuda-graphs/?utm_source=chatgpt.com)
- [PyTorch Docs - CUDA Graphs](https://docs.pytorch.org/docs/2.12/notes/cuda.html#cuda-graphs)

## TODO

- [x] 看 Step3-VL AI 总结
- [x] 看 vLLM 中 Step3-VL 源码及其 ViT CG 实现
- [x] 看 DeepSeek-OCR AI 总结
- [x] 看 DeepSeek-OCR 论文
- [x] 看 vLLM 中 DeepSeek-OCR 源码（DeepEncoder）
- [x] Vibe-Coding ViT CG 新接口设计

## Background

https://github.com/vllm-project/vllm/pull/42288

Backbone: EVA/CLIP/SigLIP/SigLIP-2 ???

## Step3-VL

[STEP3-VL-10B Technical Report](https://arxiv.org/abs/2601.09668)

Resolution: Multi-crop strategy consisting of a 728×728 global view and multiple 504×504 local crops.

## DeepSeek-OCR

[DeepSeek-OCR Technical Report](https://arxiv.org/pdf/2510.18234) -> TODO: 1~3.2 section.

视觉 token 的顺序是 local → global → separator，这确保了局部细节（高分辨率文字）在序列中先出现，全局上下文（文档结构理解）作为补充。

DeepSeek-OCR 官方推荐关闭 prefix caching 和 mm_processor_cache，这是因为其动态分块（Gundam mode）导致每次请求的图像处理结果随图像尺寸变化，缓存命中率极低且浪费显存。

DeepSeek-OCR presents the most complex pattern: SAM → CLIP dual encoder for both global images and local patches, with per-image merge based on variable `crop_shape`.

**Key Features:**

- **Dual encoder:** SAM-B + DeepCLIP in series for both global images and local patches.
- **Multi-input:** pixel_values (global images) + images_crop (local patches) + images_spatial_crop (tile layout metadata).
- **Two-pass ViT:** Global and local each go through SAM→CLIP→projector separately.
- **Per-image merge:** Output combines local patches + global image + view_separator with newline tokens interspersed.

**Strategy:**

Batch the vision encoder forward passes in one CUDA graph, perform CPU-side merge in `postprocess_encoder_output` (mirroring Step3-VL's pattern).

## ViT CG Interface Design

需求背景：
vllm中为多个VLM模型的ViT部分支持了cuda graph，目前的设计只适合于qwen style的ViT（即ViT的输入仅包含pixel_values），但不太适合推广到非mrope的ViT上（比如：DeepSeek-OCR和step-3vl都有点特例），并且ViT cuda graph的接口设计也需要稍微简化一下（目前为一个新模型适配该特性需要实现10+接口方法，实现比较复杂）。
需求设计：
请基于Qwen3-VL的实现，优化当前ViT cuda graph的接口，使其更加适配DeepSeek-OCR和step-3vl这类模型（DeepSeek-OCR的DeepCLIPVisionTransformer需要同时输入互相关联的pixel_values和patch_embeds，DeepSeek-OCR和step-3vl都需要分别处理global image和local patches）。
注意事项：
1.请重新设计EncoderCudaGraphManager、SupportsEncoderCudaGraph等顶层接口和类，并考虑如何兼容现有的模型（Qwen3-VL等）以及目标的DeepSeek-OCR和step-3vl这类模型；
2.尽可能减少ViT cuda graph的接口数量，从而简化新模型适配的工作量；
3.先做整体的设计，不需要考虑怎么写UT和测试。
参考资料：
https://github.com/vllm-project/vllm/issues/38175
https://github.com/vllm-project/vllm/pull/35963
https://github.com/vllm-project/vllm/pull/42224
https://github.com/vllm-project/vllm/pull/41234
https://github.com/vllm-project/vllm/pull/42288#discussion_r3227698497

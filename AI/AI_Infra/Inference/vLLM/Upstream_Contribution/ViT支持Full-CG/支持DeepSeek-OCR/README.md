# Step3-VL & XXX-OCR

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

## PR Desc

At first, I have tried to contain the `local_patch` encoding into the ViT cuda graph, but I find this solution has some drawbacks:

1. The grid and number of `local_patch` is dynamic according to the input images, and it also requires to add newline tokens to the end of each raw in the grid, which makes the tensor shape dynamic and unpredictable. Thus, we have to only put the encoding of the raw `local_patch` into the cuda graph, then add newline tokens in the `postprocess_encoder_output()`.
2. To make sure images with max number of `local_patch` could be correctly repalyed, we have to capture `max_crops` buffers for each input, even for images with size < `(640, 640)`, which will not be tiled and will not have local patches. In this case, the performance is worse than the eager execution, since it can lead to additional and redundant dummy `local_patch` replay.

Thus, I decide to only contain `global_image` encoding into the ViT cuda graph, with eager executing `local_patch` encoding, then assemble them in `postprocess_encoder_output()`. This is a tradeoff of performance for both images < `(640, 640)` and larger images.

Future Plan (in following PRs): I want to explore dual-path ViT cuda graph budget selecting mechanism for DeepSeek-OCR and Step3-VL to decouple the `global_image` replay and `local_patch` replay.

## Dual-Path Graph Select for DeepSeek-OCR ViT CUDA Graph

背景：我在 vllm 上基于 https://github.com/vllm-project/vllm/issues/38175 为 DeepSeek-OCR 实现了 ViT Cuda graph 的支持。https://github.com/vllm-project/vllm/pull/43586 目前只支持对 global image 入图，对于较大 size 的输入（包含 local patch），开启 ViT Cuda graph 的收益很小。
需求：请在 43586 这个 PR 的基础上重构 ViT Cuda graph 相关代码，使 DeepSeek-OCR 能够支持 Dual-Path Graph Select。
1.根据不同模型决定是否开启 Dual-Path Graph Select，可以给模型的 SupportsEncoderCudaGraph 协议新增一个借口，模型返回是否启用 Dual-Path Graph Select；
2.在 ViT Cuda graph 捕获阶段分别为 global image 和 local patch 捕获两套 graph，dummy inputs 的大小由 max_batch_size 和 global image/local patch 各自的 token budget 决定；
3.在实际推理时，EncoderCudaGraphManager 在 _execute_local() 中，根据排序后的每个图像输入，分别累加 global image tokens 和 local patch tokens，并判断当前 batch 中的 global image tokens 是否小于等于 max global image budget、当前 batch 的 local patch tokens 是否小于等于 max local patch budget、以及 batch size 是否小于等于 max batch size。然后，对 global image 和 local patch 分别 _find_smallest_fitting_budget_given_tokens() 并分别 _run_budget_graph()，最后在 postprocess_encoder_output() 中完成最终 embedding 的组装（包括：为 loacl patch 添加 newline token，拼接 local patch embedding 和 global image embedding，添加 view separator token），即 Dual-Path Graph Select。

请按如下要求调整设计方案：
1.不需要 get_encoder_cudagraph_dual_path_config() 这个方法，直接将 dual_path_config 的属性添加为 EncoderCudaGraphConfig 的属性，并添加一个 enable_dual_patch_graph 属性，默认返回 False，DeepSeek-OCR 返回 True；
2.在 _execute_local() 中，如果 global_budget 或 local_budget 其中一个为 None，则只对为 None 的这个 path 回退到 eager 执行，另一条 path 还是走 graph replay。

对于 SupportsEncoderCudaGraph 中新增的接口，能不能将所有新增的 xxx_local() 接口合并到原有的接口中，并通过传入的 path 字段区分执行路径。

## TODO

Done. The change is minimal — just _generate_budgets in encoder_cudagraph.py:

What changed:

- Added optional token_per_item parameter to _generate_budgets. When provided, budgets are aligned to multiples of token_per_item and density controls the item-count step factor instead of the budget step factor. This guarantees every
budget actually increases batch capacity (budget // token_per_item).
- Dual-path init calls pass density=1.5, token_per_item=... for both global and local paths.

Effect on DeepSeek-OCR:

┌────────────────┬──────────────────────────────────────────────────┬─────────────────────────────────────────────────────────────────────────┐
│                │                   Before (2×)                    │                              After (1.5×)                               │
├────────────────┼──────────────────────────────────────────────────┼─────────────────────────────────────────────────────────────────────────┤
│ Global budgets │ [272, 544, 1088, 2176, 4352, 8192] (6)           │ [272, 544, 816, 1088, 1632, 2448, 3536, 5168, 7616, 8192] (10)          │
├────────────────┼──────────────────────────────────────────────────┼─────────────────────────────────────────────────────────────────────────┤
│ Local budgets  │ [100, 200, 400, 800, 1600, 3200, 6400, 8192] (8) │ [100, 200, 300, 400, 600, 900, 1300, 1900, 2800, 4200, 6300, 8192] (12) │
├────────────────┼──────────────────────────────────────────────────┼─────────────────────────────────────────────────────────────────────────┤
│ Total graphs   │ 14                                               │ 22                                                                      │
└────────────────┴──────────────────────────────────────────────────┴─────────────────────────────────────────────────────────────────────────┘

For a batch of 10 tiled images (6 patches each, 2720 global + 6000 local tokens):

- Before: selects global budget 4352 (6 extra dummy images through SAM+CLIP) + local budget 6400 (4 extra dummy patches)
- After: selects global budget 3536 (3 extra images) + local budget 6300 (3 extra patches)

Cuts padded compute waste roughly in half. Single-path models are unaffected.

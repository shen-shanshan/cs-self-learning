Following https://github.com/vllm-project/vllm/pull/35963 (only support image inference), this PR continues to work on it to support video inference for video inference.

---

**🤖 AI Summary:**

### System Overview

Before This Change:

```
┌─────────────────────────────────────────────────────────────────────┐
│                      _execute_mm_encoder()                          │
│                                                                     │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  for modality, mm_kwargs_batch in grouped_inputs:           │   │
│  │                                                             │   │
│  │    if manager.supports_modality(modality):                  │   │
│  │        ┌──────────────────────────────┐                     │   │
│  │        │  modality == "image"  ✅     │ ──► CUDA graph path │   │
│  │        └──────────────────────────────┘                     │   │
│  │                                                             │   │
│  │    else:                                                    │   │
│  │        ┌──────────────────────────────┐                     │   │
│  │        │  modality == "video"  ❌     │ ──► embed_multimodal│   │
│  │        │  (not in modalities list)    │     (eager, slow)   │   │
│  │        └──────────────────────────────┘                     │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
```

After This Change:

```
┌─────────────────────────────────────────────────────────────────────┐
│                      _execute_mm_encoder()                          │
│                                                                     │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  for modality, mm_kwargs_batch in grouped_inputs:           │   │
│  │                                                             │   │
│  │    if manager.supports_modality(modality):                  │   │
│  │        ┌──────────────────────────────┐                     │   │
│  │        │  modality == "image"  ✅     │ ──► CUDA graph path │   │
│  │        └──────────────────────────────┘                     │   │
│  │        ┌──────────────────────────────┐                     │   │
│  │        │  modality == "video"  ✅     │ ──► CUDA graph path │   │
│  │        │  (EVS disabled, default)     │     (fast, new!)    │   │
│  │        └──────────────────────────────┘                     │   │
│  │                                                             │   │
│  │    else (EVS enabled):                                      │   │
│  │        ┌──────────────────────────────┐                     │   │
│  │        │  modality == "video"  ⚠️    │ ──► embed_multimodal│   │
│  │        │  (EVS needs pruning after)   │     (fallback)      │   │
│  │        └──────────────────────────────┘                     │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
```

> [!NOTE]
> **Why video is excluded from ViT CUDA graph when EVS is enabled?**
>
> 1. **Token count mismatch (correctness bug):** The preprocessor plans the LLM sequence with `pruned_count` placeholder token slots (computed deterministically from `compute_retained_tokens_count(tokens_per_frame, num_frames, q)`). The CUDA graph path (`gpu_model_runner.py:2671–2683`) calls `encoder_cudagraph_forward` directly and returns the result — it completely bypasses `embed_multimodal`, so `_postprocess_video_embeds_evs` is never called. The graph returns all `full_count = t*(h//m)*(w//m)` raw ViT tokens. Feeding `full_count` embeddings into `pruned_count` LLM placeholder slots corrupts the LLM input.
> 2. **Content-dependent dynamic indexing (graph incompatibility):** `compute_retention_mask` computes frame dissimilarity scores, runs `torch.argsort`, and selects which tokens to keep via `emb[retention_mask]`. The which indices to retain changes per video based on actual pixel content (**temporal frame similarity**). CUDA graphs record a fixed sequence of GPU operations with fixed tensor shapes and memory addresses — a data-dependent gather operation that produces different indices for every input cannot be captured or correctly replayed.
>
> **Why images are safe and video without EVS is safe?**
>
> - **Images:** EVS is video-only (no temporal frames to compare), so image token counts are always (h//m)*(w//m) — fully static, safe for CUDA graphs.
> - **Video without EVS:** token counts are determined entirely by grid_thw, the ViT output is used as-is, no dynamic pruning occurs — safe for CUDA graphs.

### Key Architectural Insight: Graph Reuse

The central insight that makes this change simple and efficient is that **images and videos share the exact same ViT computation path**:

```
self.visual(pixel_values, grid_thw, encoder_metadata=buffers)
```

The ViT does not distinguish between image and video at the computational graph level. The only differences are:

| Aspect | Image | Video |
|---|---|---|
| Input tensor key | `pixel_values` | `pixel_values_videos` |
| Grid key | `image_grid_thw` | `video_grid_thw` |
| Grid type | `list[list[int]]` | `torch.Tensor [N, 3]` |
| Temporal dim `t` | always `1` | `num_frames / temporal_patch_size` |
| Patch column size | `C × T_patch × P × P` | **identical** |

Since the patch embedding column size is **identical** for images and videos, the captured CUDA graph input buffer is compatible with both modalities. No additional graph captures are needed.

```
                        Patch Embedding Column Size
                ┌─────────────────────────────────────────┐
                │  in_channels × temporal_patch_size × P² │
                │      3       ×         2        × 14²   │
                │                  = 1176                 │
                │                                         │
                │  ← same for BOTH image and video! →    │
                └─────────────────────────────────────────┘

 Image patches:  [N×1×H×W,  1176]   (t=1 in grid_thw)
 Video patches:  [N×T×H×W,  1176]   (t=num_frames/temporal_patch_size)
```

### Data Flow

```
mm_kwargs (video)                        mm_kwargs (image)
────────────────────                     ─────────────────────────
pixel_values_videos: Tensor              pixel_values: Tensor
video_grid_thw: Tensor [N, 3]           image_grid_thw: list[list[int]]
  [[t1,h1,w1],                            [[1,h1,w1],
   [t2,h2,w2], ...]                        [1,h2,w2], ...]

          │                                        │
          │    _get_input_key()                    │    _get_input_key()
          │    detects "video_grid_thw"            │    no "video_grid_thw"
          │    → "pixel_values_videos"             │    → "pixel_values"
          │                                        │
          ▼                                        ▼
    ┌──────────────────────────────────────────────────┐
    │              BudgetGraphMetadata                 │
    │                                                  │
    │  input_buffer  ← zero(), then [:n].copy_(src)   │
    │  (shared buffer for image & video patch data)    │
    │                                                  │
    │  metadata_buffers:                               │
    │    pos_embeds       ← from prepare_encoder_      │
    │    cu_seqlens            metadata(grid_thw_list) │
    │    rotary_pos_emb_cos    (same computation for   │
    │    rotary_pos_emb_sin     t=1 or t=num_frames)   │
    │    max_seqlen                                    │
    │    sequence_lengths                              │
    │                                                  │
    │  graph.replay() → output_buffer                  │
    └──────────────────────────────────────────────────┘
```

### Unit Tests

Added `SimpleMockVideoViTModel` (mirrors `SimpleMockViTModel` with video keys) and `TestVideoEncoderCudaGraphCaptureReplay` with 10 test cases:

| Test | What it verifies |
|---|---|
| `test_capture_creates_one_graph_per_budget` | Budget graphs are shared (same graphs) |
| `test_video_execute_returns_one_tensor_per_video` | Output list length == num videos |
| `test_video_execute_output_tokens_single_frame` | t=1 → same as image |
| `test_video_execute_output_tokens_multi_frame` | t=2 → 2× tokens of single frame |
| `test_video_execute_mixed_frame_counts` | Different t values in same batch |
| `test_video_eager_fallback_when_tokens_exceed_all_budgets` | Graceful eager fallback |
| `test_video_greedy_packing_multiple_short_videos` | Greedy packing works for videos |
| `test_video_chunking_when_exceeds_max_batch` | Chunking across batch size limit |
| `test_video_hit_counter_increments` | Hit counter tracks video graph hits |
| `test_image_and_video_routes_are_independent` | Both modalities work in same manager |

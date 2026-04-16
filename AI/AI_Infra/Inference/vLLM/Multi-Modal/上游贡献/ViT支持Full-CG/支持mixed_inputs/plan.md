# Plan: Mixed Image+Video CUDA Graph Support for Qwen3-VL ViT

## Context

PR #35963 and #38061 added CUDA Graph support for image-only and video-only ViT inference in Qwen3-VL respectively. Currently, when a request contains both images and videos, `group_and_batch_mm_kwargs()` splits them into separate modality batches, each requiring its own CUDA graph replay. This is confirmed as a known limitation at `docs/design/cuda_graphs_multimodal.md:62`:

> "Currently, we only support image-only or video-only inputs when enabling CUDA graph, mixed inputs (image + video) are not supported yet"

**Goal**: Enable processing mixed image+video inputs in a single CUDA graph replay, reducing the number of graph replays and improving GPU utilization for multi-modal requests.

**Key insight**: The ViT (`Qwen3_VisionTransformer.forward()`) already handles mixed grids natively -- images are just 1-frame entries `[1, H, W]` while videos are `[T, H, W]`. Both produce patches with identical shape `[num_patches, C*T_patch*P*P]`. The captured CUDA graph (with cu_seqlens sized by `max_frames_per_batch`) can accommodate any mix.

---

## Implementation Steps

### Step 1: Update `get_input_modality()` to detect mixed input

**File**: `vllm/model_executor/models/qwen3_vl.py` (line 1778)

```python
def get_input_modality(self, mm_kwargs):
    has_image = "image_grid_thw" in mm_kwargs
    has_video = "video_grid_thw" in mm_kwargs
    if has_image and has_video:
        return "mixed"
    if has_image:
        return "image"
    return "video"
```

### Step 2: Add `merge_mixed_mm_kwargs()` to Qwen3-VL model

**File**: `vllm/model_executor/models/qwen3_vl.py`

Add a new method that merges separate image and video mm_kwargs into a single unified dict. Images come first in the merged representation:

```python
def merge_mixed_mm_kwargs(
    self,
    image_mm_kwargs: dict[str, Any],
    video_mm_kwargs: dict[str, Any],
) -> dict[str, Any]:
    img_pv = image_mm_kwargs["pixel_values"]
    vid_pv = video_mm_kwargs["pixel_values_videos"]
    merged_pv = torch.cat([img_pv, vid_pv], dim=0)
    
    img_grid = image_mm_kwargs["image_grid_thw"]
    vid_grid = video_mm_kwargs["video_grid_thw"]
    # Normalize to lists
    if not isinstance(img_grid, list):
        img_grid = img_grid.tolist()
    if not isinstance(vid_grid, list):
        vid_grid = vid_grid.tolist()
    
    return {
        "pixel_values": merged_pv,
        "image_grid_thw": img_grid + vid_grid,  # Merged grid (images first)
        "video_grid_thw": vid_grid,              # Keep for get_input_modality detection
    }
```

When both `image_grid_thw` and `video_grid_thw` are present, `get_input_modality()` returns "mixed". The merged grid uses `image_grid_thw` as the unified key.

### Step 3: Update model helper methods for mixed modality

**File**: `vllm/model_executor/models/qwen3_vl.py`

**`_get_pixel_values_by_modality()`** (line 1798):
```python
def _get_pixel_values_by_modality(self, mm_kwargs):
    modality = self.get_input_modality(mm_kwargs)
    if modality == "image" or modality == "mixed":
        return mm_kwargs["pixel_values"]
    return mm_kwargs["pixel_values_videos"]
```

**`_get_grid_thw_by_modality()`** (line 1808):
```python
def _get_grid_thw_by_modality(self, mm_kwargs):
    modality = self.get_input_modality(mm_kwargs)
    if modality == "mixed" or modality == "image":
        grid_thw = mm_kwargs["image_grid_thw"]
    else:
        grid_thw = mm_kwargs["video_grid_thw"]
    if not isinstance(grid_thw, list):
        grid_thw = grid_thw.tolist()
    return grid_thw
```

For "mixed", the merged grid is under `image_grid_thw` and the merged pixel values under `pixel_values`. This allows `encoder_cudagraph_forward()`, `encoder_eager_forward()`, and all other dependent methods to work without changes.

### Step 4: Update `select_encoder_cudagraph_items()` for mixed

**File**: `vllm/model_executor/models/qwen3_vl.py` (line 1839)

For mixed input, items are from the merged grid (images first, then videos). Selected sub-batches use image-format keys since the data is already merged:

```python
def select_encoder_cudagraph_items(self, mm_kwargs, indices):
    modality = self.get_input_modality(mm_kwargs)
    grid_thw = self._get_grid_thw_by_modality(mm_kwargs)
    pixel_values = self._get_pixel_values_by_modality(mm_kwargs)

    if len(indices) == 0:
        if modality == "video":
            return {"pixel_values_videos": pixel_values[:0], "video_grid_thw": []}
        return {"pixel_values": pixel_values[:0], "image_grid_thw": []}

    patches_per_item = [t * h * w for t, h, w in grid_thw]
    cum_patches = [0]
    for p in patches_per_item:
        cum_patches.append(cum_patches[-1] + p)

    selected_pv = torch.cat(
        [pixel_values[cum_patches[i] : cum_patches[i + 1]] for i in indices]
    )
    selected_grid = [grid_thw[i] for i in indices]

    if modality == "video":
        return {"pixel_values_videos": selected_pv, "video_grid_thw": selected_grid}
    # "image" or "mixed" -> use image keys (merged format)
    return {"pixel_values": selected_pv, "image_grid_thw": selected_grid}
```

### Step 5: Update `prepare_encoder_cudagraph_replay_buffers()` for mixed

**File**: `vllm/model_executor/models/qwen3_vl.py` (line 1962)

Mixed batches need `max_frames_per_batch` for cu_seqlens padding (total frames = num_images + sum(T_i for videos)):

```python
def prepare_encoder_cudagraph_replay_buffers(self, mm_kwargs, max_batch_size, max_frames_per_batch):
    modality = self.get_input_modality(mm_kwargs)
    grid_thw_list = self._get_grid_thw_by_modality(mm_kwargs)

    if modality == "image":
        buffers = self.visual.prepare_encoder_metadata(
            grid_thw_list, max_batch_size=max_batch_size,
        )
    else:  # "video" or "mixed"
        buffers = self.visual.prepare_encoder_metadata(
            grid_thw_list, max_frames_per_batch=max_frames_per_batch,
        )
    return EncoderCudaGraphReplayBuffers(buffers=buffers)
```

### Step 6: Update `get_encoder_cudagraph_config()` to include "mixed"

**File**: `vllm/model_executor/models/qwen3_vl.py` (line 1747)

```python
def get_encoder_cudagraph_config(self):
    modalities = ["image"]
    if not self.is_multimodal_pruning_enabled:
        modalities.append("video")
        modalities.append("mixed")  # <-- NEW
    return EncoderCudaGraphConfig(...)
```

### Step 7: Add `merge_mixed_mm_kwargs()` to protocol

**File**: `vllm/model_executor/models/interfaces.py` (in `SupportsEncoderCudaGraph`)

Add the new method to the protocol:

```python
def merge_mixed_mm_kwargs(
    self,
    image_mm_kwargs: dict[str, Any],
    video_mm_kwargs: dict[str, Any],
) -> dict[str, Any]:
    """Merge separate image and video mm_kwargs for mixed CUDA graph execution.
    
    Returns unified mm_kwargs with images first, videos second.
    """
    ...
```

### Step 8: Update manager's `supports_modality()` and `_run_budget_graph()`

**File**: `vllm/v1/worker/encoder_cudagraph.py`

**`supports_modality()`** (line 131): Simply check if "mixed" is in the modalities list (it will be from Step 6):
```python
def supports_modality(self, modality: str) -> bool:
    return modality in self.config.modalities
```
No change needed -- "mixed" is now in the list.

**`_run_budget_graph()`** (line 252): Handle mixed input key lookup. Mixed batches use image-format keys (since `merge_mixed_mm_kwargs` normalizes to image keys):
```python
modality = self.model.get_input_modality(mm_kwargs)
if modality == "mixed":
    input_key = self.config.input_key_by_modality["image"]
else:
    input_key = self.config.input_key_by_modality[modality]
```

### Step 9: Add frame budget constraint to greedy packer

**File**: `vllm/v1/worker/encoder_cudagraph.py` (in `_execute_local()`)

The greedy packer currently checks total tokens and max_batch_size, but NOT total frames. For mixed batches, total_frames = sum(T_i). If total_frames > max_frames_per_batch, cu_seqlens buffer overflows.

Add per-item frame counts and a frame budget constraint:

```python
def _execute_local(self, mm_kwargs):
    num_items = self.model.get_encoder_cudagraph_num_items(mm_kwargs)
    max_budget = self.token_budgets[-1]
    per_item_out_tokens = self._get_per_item_out_tokens(mm_kwargs)
    
    # NEW: Get per-item frame counts for frame budget constraint
    per_item_frames = self._get_per_item_frames(mm_kwargs)
    
    sorted_indices = sorted(range(num_items), key=lambda i: per_item_out_tokens[i])
    
    batches = []
    current_batch = []
    current_batch_tokens = 0
    current_batch_frames = 0  # NEW
    
    for orig_idx in sorted_indices:
        item_tokens = per_item_out_tokens[orig_idx]
        item_frames = per_item_frames[orig_idx]  # NEW
        if (
            current_batch_tokens + item_tokens <= max_budget
            and len(current_batch) < self.max_batch_size
            and current_batch_frames + item_frames <= self.max_frames_per_batch  # NEW
        ):
            current_batch.append(orig_idx)
            current_batch_tokens += item_tokens
            current_batch_frames += item_frames  # NEW
        else:
            if current_batch:
                batches.append((current_batch, self._find_smallest_fitting_budget_given_tokens(current_batch_tokens)))
            current_batch = [orig_idx]
            current_batch_tokens = item_tokens
            current_batch_frames = item_frames  # NEW
    ...
```

Add `_get_per_item_frames()` helper and protocol method:

```python
# In manager:
def _get_per_item_frames(self, mm_kwargs):
    return [int(f) for f in self.model.get_encoder_cudagraph_per_item_frames(mm_kwargs)]

# In protocol (interfaces.py):
def get_encoder_cudagraph_per_item_frames(self, mm_kwargs) -> list[int]:
    """Return per-item frame count (T from grid_thw)."""
    ...

# In Qwen3-VL:
def get_encoder_cudagraph_per_item_frames(self, mm_kwargs):
    grid_thw = self._get_grid_thw_by_modality(mm_kwargs)
    return [t for t, h, w in grid_thw]
```

### Step 10: Add merge logic in model runner

**File**: `vllm/v1/worker/gpu_model_runner.py` (around line 2809)

Change the encoder execution loop to merge consecutive image+video batches when the CUDA graph manager supports "mixed":

```python
# Collect all batches first
all_batches = list(group_and_batch_mm_kwargs(mm_kwargs, device=..., pin_memory=...))

i = 0
while i < len(all_batches):
    modality, num_items, mm_kwargs_batch = all_batches[i]
    
    # Attempt mixed merge with next batch for CUDA graph
    merged = False
    if (
        self.encoder_cudagraph_manager is not None
        and self.encoder_cudagraph_manager.supports_modality("mixed")
        and i + 1 < len(all_batches)
        and not self.is_multimodal_pruning_enabled
        and not self.requires_sequential_video_encoding
    ):
        next_mod, next_num, next_kwargs = all_batches[i + 1]
        if {modality, next_mod} == {"image", "video"}:
            # Identify image vs video
            if modality == "image":
                img_kwargs, vid_kwargs = mm_kwargs_batch, next_kwargs
                img_count, vid_count = num_items, next_num
                reorder = False  # images first in original order
            else:
                img_kwargs, vid_kwargs = next_kwargs, mm_kwargs_batch
                img_count, vid_count = next_num, num_items
                reorder = True   # videos first in original order
            
            raw_model = cast(SupportsEncoderCudaGraph, self.get_model())
            merged_kwargs = raw_model.merge_mixed_mm_kwargs(img_kwargs, vid_kwargs)
            
            cudagraph_output = self.encoder_cudagraph_manager.execute(merged_kwargs)
            
            if cudagraph_output is not None:
                if reorder:
                    # Original order: video first, image second
                    # CUDA graph output: image first, video second
                    encoder_outputs.extend(cudagraph_output[img_count:])  # videos
                    encoder_outputs.extend(cudagraph_output[:img_count])  # images
                else:
                    encoder_outputs.extend(cudagraph_output)
                current_item_idx += img_count + vid_count
                i += 2
                merged = True
    
    if not merged:
        # ... existing single-modality logic ...
        i += 1
```

### Step 11: Add tests for mixed image+video CUDA graph

**File**: `tests/v1/cudagraph/test_encoder_cudagraph.py`

Add `SimpleMockViTMixedModel` extending `SimpleMockViTVideoModel` with mixed support. Add test class `TestEncoderCudaGraphMixedReplay`:

- `test_mixed_execute_returns_correct_count` -- 2 images + 1 video → 3 outputs
- `test_mixed_output_tokens_correct` -- verify per-item output shapes
- `test_mixed_hit_counter` -- all items counted as hits
- `test_mixed_select_items` -- sub-batch selection preserves ordering
- `test_mixed_eager_fallback` -- oversized items fall back
- `test_frame_budget_constraint` -- items split when frames exceed budget
- `test_get_input_modality_mixed` -- modality detection with both keys present

### Step 12: Update design documentation

**File**: `docs/design/cuda_graphs_multimodal.md` (line 62)

Replace the limitation note with documentation of mixed input support.

---

## Files to Modify

| File | Changes |
|------|---------|
| `vllm/model_executor/models/qwen3_vl.py` | Update `get_input_modality`, helpers, `select_encoder_cudagraph_items`, `prepare_encoder_cudagraph_replay_buffers`, `get_encoder_cudagraph_config`; add `merge_mixed_mm_kwargs`, `get_encoder_cudagraph_per_item_frames` |
| `vllm/model_executor/models/interfaces.py` | Add `merge_mixed_mm_kwargs()`, `get_encoder_cudagraph_per_item_frames()` to protocol |
| `vllm/v1/worker/encoder_cudagraph.py` | Update `_run_budget_graph()` for mixed input key; add frame budget constraint to `_execute_local()` |
| `vllm/v1/worker/gpu_model_runner.py` | Add merge logic for consecutive image+video batches before CUDA graph execution |
| `tests/v1/cudagraph/test_encoder_cudagraph.py` | Add mixed modality mock model and test class |
| `docs/design/cuda_graphs_multimodal.md` | Update limitation note |

## Verification

1. **Unit tests**: Run `pytest tests/v1/cudagraph/test_encoder_cudagraph.py -v` to verify all existing and new tests pass
2. **Linting**: Run `pre-commit run --all-files` to verify code quality
3. **Integration test**: If GPU available, test with actual Qwen3-VL model:
   ```bash
   # Mixed image+video inference with CUDA graph
   vllm serve Qwen/Qwen3-VL-7B --compilation-config '{"cudagraph_mm_encoder": true}'
   # Send request with both image and video
   ```
4. **Regression**: Verify image-only and video-only inputs still work correctly through CUDA graph

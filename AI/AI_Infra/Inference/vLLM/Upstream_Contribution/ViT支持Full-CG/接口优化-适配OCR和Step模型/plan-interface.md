# ViT CUDA Graph Interface Refactoring Plan

## Context

vLLM currently supports CUDA graph capture/replay for vision encoders (ViT), enabling significant performance improvements for multimodal models. However, the current design has three fundamental limitations:

1. **Single-input assumption**: `EncoderCudaGraphConfig.input_key_by_modality` maps each modality to exactly one input tensor key. Models like DeepSeek-OCR (needs `pixel_values` + `patch_embeds`) and Step-3VL (needs `pixel_values` + `patch_pixel_values`) require multiple input tensors.

2. **Buffer semantics confusion**: Step-3VL works around the single-input limitation by placing `patch_pixel_values` (a real input tensor) into `buffer_keys` (intended for metadata like `cu_seqlens`, `pos_embeds`), blurring the distinction between batch data and precomputed metadata.

3. **Excessive protocol surface**: 13 methods per model — too many for new model adapters, especially when several are trivial one-liners.

This refactoring generalizes the interface to support multi-input ViT patterns (DeepSeek-OCR's dual encoder with separate global/local passes, Step-3VL's image + patches), reduces the protocol from 13 to 9 methods, and establishes a clean separation between input tensors and metadata buffers.

References:
- https://github.com/vllm-project/vllm/issues/38175
- https://github.com/vllm-project/vllm/pull/35963
- https://github.com/vllm-project/vllm/pull/42224

---

## Design Overview

### Core Insight

CUDA graphs capture GPU operations at fixed memory addresses. During replay, the manager copies new data into those same addresses, and the GPU replays the captured kernels. The model's Python dispatch code only runs during capture — during replay, the GPU blindly re-executes the recorded operations. This means:

- The manager does NOT need modality awareness — it just copies all `input_keys` from `mm_kwargs` into the captured buffers
- The model handles modality dispatch in `select_encoder_cudagraph_items` by normalizing keys
- Multiple input tensors are managed uniformly via `input_buffers: dict[str, Tensor]` instead of a single `input_buffer`

### Architecture Diagram

```
┌─────────────────────────────────────────────────────┐
│                EncoderCudaGraphManager               │
│                                                     │
│  capture():                                         │
│    for each token_budget:                           │
│      mm_kwargs, buffers = model.prepare_capture()   │
│      output = model.cudagraph_forward(mm_kwargs)    │
│      graph = CUDAGraph()                            │
│      with graph: model.cudagraph_forward(mm_kwargs) │
│      input_buffers = {k: mm_kwargs[k] ∀ input_keys} │
│                                                     │
│  _run_budget_graph():                               │
│    for key in input_keys:                           │
│      input_buffers[key][:n].copy_(mm_kwargs[key])   │
│    for key in buffer_keys:                          │
│      metadata_buffers[key][:n].copy_(replay[key])   │
│    graph.replay() → output_buffer                   │
└─────────────────────────────────────────────────────┘
```

### Key Interface Changes

```
                    BEFORE                          AFTER
┌─────────────────────────────────┐  ┌─────────────────────────────────┐
│ EncoderCudaGraphConfig          │  │ EncoderCudaGraphConfig          │
│  modalities: list[str]          │  │  modalities: list[str]          │
│  input_key_by_modality: dict    │  │  input_keys: list[str]  ← NEW   │
│  buffer_keys: list[str]         │  │  buffer_keys: list[str]         │
│  out_hidden_size: int           │  │  out_hidden_size: int           │
│                                 │  │  max_frames_per_video: int ← NEW│
└─────────────────────────────────┘  └─────────────────────────────────┘

┌─────────────────────────────────┐  ┌─────────────────────────────────┐
│ BudgetGraphMetadata             │  │ BudgetGraphMetadata             │
│  input_buffer: Tensor           │  │  input_buffers: dict[str,Tensor]│
│  metadata_buffers: dict         │  │  metadata_buffers: dict         │
│  output_buffer: Tensor          │  │  output_buffer: Tensor          │
└─────────────────────────────────┘  └─────────────────────────────────┘

┌─────────────────────────────────┐  ┌─────────────────────────────────┐
│ Protocol (13 methods)           │  │ Protocol (9 methods)            │
│  get_config()                   │  │  get_config()                   │
│  get_input_modality()    ← REM  │  │  get_budget_range()             │
│  get_max_frames_per_video← REM  │  │  get_item_info()        ← NEW   │
│  get_budget_range()             │  │  select_items()                 │
│  get_num_items()         ← REM  │  │  postprocess_output()           │
│  get_per_item_out_tokens ← REM  │  │  prepare_capture_inputs()       │
│  get_per_item_in_sizes   ← REM  │  │  prepare_replay_buffers()       │
│  select_items()                 │  │  cudagraph_forward()            │
│  postprocess_output()           │  │  eager_forward()                │
│  prepare_capture_inputs()       │  │                                 │
│  prepare_replay_buffers()       │  │                                 │
│  cudagraph_forward()            │  │                                 │
│  eager_forward()                │  │                                 │
└─────────────────────────────────┘  └─────────────────────────────────┘
```

### New Dataclass: `EncoderCudaGraphItemInfo`

```python
@dataclass
class EncoderCudaGraphItemInfo:
    num_output_tokens: int  # was get_per_item_output_tokens
    input_size: int         # was get_per_item_input_sizes
```

`get_encoder_cudagraph_item_info(mm_kwargs) -> list[EncoderCudaGraphItemInfo]` replaces `get_num_items`, `get_per_item_output_tokens`, and `get_per_item_input_sizes`. The list length = number of items.

---

## Implementation Phases

### Phase 1: Update Definitions (`encoder_cudagraph_defs.py`)

1. Add `input_keys: list[str]` to `EncoderCudaGraphConfig`, remove `input_key_by_modality: dict[str, str]`
2. Add `max_frames_per_video: int = 0` to config
3. Add `EncoderCudaGraphItemInfo` dataclass with `num_output_tokens` and `input_size` fields

### Phase 2: Update Protocol (`interfaces.py`)

1. Remove 4 methods: `get_input_modality`, `get_max_frames_per_video`, `get_encoder_cudagraph_num_items`, `get_encoder_cudagraph_per_item_output_tokens`, `get_encoder_cudagraph_per_item_input_sizes`
2. Add 1 method: `get_encoder_cudagraph_item_info(mm_kwargs) -> list[EncoderCudaGraphItemInfo]`
3. Update docstrings on remaining methods

### Phase 3: Update Manager (`encoder_cudagraph.py`)

1. **`BudgetGraphMetadata`**: Replace `input_buffer: torch.Tensor` with `input_buffers: dict[str, torch.Tensor]`
2. **`__init__`**: Replace `model.get_max_frames_per_video()` call with `self.config.max_frames_per_video`
3. **`_get_per_item_out_tokens`** → **`_get_item_info`**: Return `list[EncoderCudaGraphItemInfo]`, delegate to `model.get_encoder_cudagraph_item_info()`
4. **`_capture_budget_graph`**: Store all input tensors from mm_kwargs into `input_buffers` dict by iterating `self.config.input_keys`
5. **`_run_budget_graph`**: Iterate `self.config.input_keys`, copy each from `mm_kwargs` into corresponding `input_buffers[key]`. Remove modality-specific lookup
6. **`_execute_local`**: Use `_get_item_info()` for per-item tokens; derive num_items from list length
7. **`_dp_shard`**: Use `_get_item_info()` for input sizes

### Phase 4: Migrate Qwen3-VL (`qwen3_vl.py`)

Key change: **normalize keys in `select_encoder_cudagraph_items`** so the manager always sees the same input key regardless of modality.

1. **`get_encoder_cudagraph_config`**: 
   - Replace `input_key_by_modality={"image": "pixel_values", "video": "pixel_values_videos"}` with `input_keys=["pixel_values"]`
   - Add `max_frames_per_video` computed from `MULTIMODAL_REGISTRY.get_processing_info()` (previously in `get_max_frames_per_video`)
2. **Remove** `get_input_modality` and `get_max_frames_per_video` from protocol methods. Keep as private helpers if needed
3. **Replace** `get_encoder_cudagraph_num_items`, `get_encoder_cudagraph_per_item_output_tokens`, `get_encoder_cudagraph_per_item_input_sizes` with `get_encoder_cudagraph_item_info(mm_kwargs)`
4. **`select_encoder_cudagraph_items`**: Always return `{"pixel_values": ..., "image_grid_thw": ...}`, normalizing video inputs into the image-key format
5. **`encoder_cudagraph_forward`**: Always read `mm_kwargs["pixel_values"]` and `mm_kwargs["image_grid_thw"]` (no modality dispatch — key already normalized by `select_items`)
6. **`prepare_encoder_cudagraph_replay_buffers`**: Detect video from grid values (`max(t) > 1`) instead of from mm_kwargs keys
7. **`encoder_eager_forward`**: Unchanged — still uses modality dispatch since this is the non-graph path

### Phase 5: Migrate Qwen2.5-VL and Qwen2-VL

Apply the same changes as Qwen3-VL (Phase 4).

### Phase 6: Migrate Step3-VL (`step3_vl.py`)

Key change: **move `patch_pixel_values` from `buffer_keys` to `input_keys`** — it's an input tensor, not metadata.

1. **`get_encoder_cudagraph_config`**: 
   - `input_keys=["pixel_values", "patch_pixel_values"]`
   - `buffer_keys=[]` (no metadata buffers needed — absolute pos embeddings are computed inside the ViT)
   - `max_frames_per_video=0`
2. **Remove** `get_input_modality`, `get_max_frames_per_video`
3. **Replace** three item methods with `get_encoder_cudagraph_item_info`
4. **`encoder_cudagraph_forward`**: Read `patch_pixel_values` from `mm_kwargs["patch_pixel_values"]` instead of `buffers["patch_pixel_values"]`
5. **`prepare_encoder_cudagraph_capture_inputs`**: Put both `pixel_values` and `patch_pixel_values` in `mm_kwargs`; `buffers` can be `{}`
6. **`prepare_encoder_cudagraph_replay_buffers`**: Return `EncoderCudaGraphReplayBuffers(buffers={})` — all inputs flow through mm_kwargs
7. **`postprocess_encoder_output`**: Unchanged — CPU-side merge based on `batch_mm_kwargs["num_patches"]`

### Phase 7: DeepSeek-OCR Implementation Sketch (future PR)

DeepSeek-OCR presents the most complex pattern: SAM → CLIP dual encoder for both global images and local patches, with per-image merge based on variable `crop_shape`.

**Strategy**: Batch the vision encoder forward passes in one CUDA graph, perform CPU-side merge in `postprocess_encoder_output` (mirroring Step3-VL's pattern).

```
CUDA graph captures:
  ┌─────────────────────────────────────────┐
  │ 1. SAM(global_images) → global_feat_1   │
  │ 2. CLIP(global_images, global_feat_1)   │
  │    → concat → project → global_flat     │
  │ 3. SAM(local_patches) → local_feat_1    │
  │ 4. CLIP(local_patches, local_feat_1)    │
  │    → concat → project → local_flat      │
  │ 5. return [global_flat, local_flat]     │
  └─────────────────────────────────────────┘

CPU-side postprocess:
  For each image, split global_flat[i], collect local patches[i],
  add newline tokens, merge with view_separator
```

**Config**:
```python
EncoderCudaGraphConfig(
    modalities=["image"],
    input_keys=["pixel_values", "images_crop"],
    buffer_keys=[],
    out_hidden_size=projector_config.n_embed,
    max_frames_per_video=0,
)
```

Note: `images_spatial_crop` stays in `mm_kwargs` but NOT in `input_keys` — it's small metadata used only in CPU-side postprocessing, not in the GPU kernels captured by the graph.

---

## Model-Specific Input Configurations

| Model | `input_keys` | `buffer_keys` | Normalization |
|-------|-------------|--------------|---------------|
| Qwen3-VL | `["pixel_values"]` | `["pos_embeds", "rotary_pos_emb_cos", "rotary_pos_emb_sin", "cu_seqlens", "max_seqlen", "sequence_lengths"]` | Video → normalized to `pixel_values` key in select_items |
| Qwen2.5-VL | `["pixel_values"]` | `["rotary_pos_emb_cos", "rotary_pos_emb_sin", "window_index", "reverse_indices", "cu_seqlens", "cu_window_seqlens", "max_seqlen_full", "max_seqlen_window"]` | Same |
| Qwen2-VL | `["pixel_values"]` | `["rotary_pos_emb_cos", "rotary_pos_emb_sin", "cu_seqlens", "max_seqlen"]` | Same |
| Step3-VL | `["pixel_values", "patch_pixel_values"]` | `[]` | N/A |
| DeepSeek-OCR | `["pixel_values", "images_crop"]` | `[]` | N/A |

---

## Files to Modify

| File | Phase | Changes Summary |
|------|-------|----------------|
| `vllm/v1/worker/encoder_cudagraph_defs.py` | 1 | Add `EncoderCudaGraphItemInfo`, update `EncoderCudaGraphConfig` |
| `vllm/model_executor/models/interfaces.py` | 2 | Protocol update: remove 4 methods, add 1 |
| `vllm/v1/worker/encoder_cudagraph.py` | 3 | Multi-input buffers, item info consolidation |
| `vllm/model_executor/models/qwen3_vl.py` | 4 | Key normalization, item info, config update |
| `vllm/model_executor/models/qwen2_5_vl.py` | 5 | Same as Qwen3-VL |
| `vllm/model_executor/models/qwen2_vl.py` | 5 | Same as Qwen3-VL |
| `vllm/model_executor/models/step3_vl.py` | 6 | Move patch_pixel_values to input_keys |
| `tests/v1/cudagraph/test_encoder_cudagraph.py` | 7 | Update mock models |
| `tests/models/multimodal/generation/test_vit_cudagraph.py` | 7 | Update test configs |

---

## Verification

1. **Type checker**: Run `pre-commit run mypy-3.10 --all-files` to verify protocol conformance
2. **Existing unit tests**: Run `pytest tests/v1/cudagraph/test_encoder_cudagraph.py -v` to verify manager logic
3. **Existing integration tests**: Run `pytest tests/models/multimodal/generation/test_vit_cudagraph.py -v` to verify Qwen models still work
4. **Step3-VL test**: Run `VLLM_TEST_VISION_MODEL="step3_vl" pytest tests/models/multimodal/generation/test_vision.py -v -k "step3"` to verify Step3-VL CUDA graph still works
5. **Manual verification**: For Qwen3-VL, capture and replay a video batch to verify the key normalization approach produces identical outputs to eager execution

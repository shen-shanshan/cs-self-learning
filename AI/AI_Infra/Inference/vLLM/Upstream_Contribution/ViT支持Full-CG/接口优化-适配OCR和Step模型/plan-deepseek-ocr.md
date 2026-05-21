# Phase 7: DeepSeek-OCR ViT CUDA Graph Implementation

## Context

Phases 1-6 have already refactored the `SupportsEncoderCudaGraph` protocol and `EncoderCudaGraphManager` to support multi-input ViT patterns. Now we need to implement the protocol for DeepSeek-OCR (`DeepseekOCRForCausalLM`), which has the most complex ViT pattern in the codebase:

- **Dual encoder**: SAM-B + DeepCLIP in series for both global images and local patches
- **Multi-input**: `pixel_values` (global images) + `images_crop` (local patches) + `images_spatial_crop` (tile layout metadata)
- **Two-pass ViT**: Global and local each go through SAM→CLIP→projector separately
- **Per-image merge**: Output combines local patches + global image + `view_separator` with newline tokens interspersed

## Architecture Analysis

### Current Eager Flow

```
_pixel_values_to_embedding():
  for each image in batch:
    1. SAM(global_image) → SAM output [1, 1024, 16, 16]
    2. CLIP(global_image, SAM_output) → CLIP output
       → concat(CLIP[:,1:], SAM_flat) → [1, 256, 2048]
       → projector → [1, 64, n_embed]
       → reshape [8, 8, n_embed] → add newline col → [8, 9, n_embed]
       → flatten → [72, n_embed]
    3. SAM(local_patches) → SAM output [n, 1024, 10, 10]
    4. CLIP(local_patches, SAM_output)
       → concat → projector → [n, 25, n_embed]
       → arrange tiles per crop_shape → add newline row → flatten
    5. concat(local_features, global_features, view_separator)
```

### Key Derived Constants (for 1024x1024 global, 640x640 patches)

| Constant | Value | Derivation |
|----------|-------|------------|
| `global_side` | 8 | base_size=1024, SAM patch=16 → 64, net_2 stride=2 → 32, net_3 stride=2 → 16. CLIP: 16²=256 tokens. Projector ds_ratio=2: 256/4=64, side=√64=8 |
| `global_tokens_raw` | 64 | `global_side²` |
| `global_tokens_with_nl` | 72 | `global_side * (global_side + 1)` |
| `patch_side` | 5 | image_size=640, SAM patch=16 → 40, net_2 stride=2 → 20, net_3 stride=2 → 10. CLIP: 10²=100. Projector: 100/4=25, side=√25=5 |
| `patch_tokens_raw` | 25 | `patch_side²` |
| `patch_tokens_with_nl` | 30 | `patch_side * (patch_side + 1)` |

### CUDA Graph Strategy (mirroring Step3-VL's pattern)

**In-graph** (deterministic shapes): SAM + CLIP + projector + per-patch newline insertion for both global and local branches.

**CPU-side postprocess**: Tile arrangement for local patches (depends on `crop_shape`), per-image merge (local + global + view_separator).

CUDA graph captures:
```
1. SAM(pixel_values) → [B, 1024, 16, 16]
2. CLIP(pixel_values, SAM_global) → concat → project → [B, 64, n_embed]
3. Reshape [B, 8, 8, n_embed] → add newline col → [B, 8, 9, n_embed]
4. SAM(images_crop) → [P, 1024, 10, 10]
5. CLIP(images_crop, SAM_local) → concat → project → [P, 25, n_embed]  
6. Reshape [P, 5, 5, n_embed] → add newline col → [P, 5, 6, n_embed]
7. Return [global_flat, local_flat] = [B*72 + P*30, n_embed]
```

CPU-side postprocess:
```
For each image i:
  1. Extract global[i] → [72, n_embed]
  2. Extract local patches for image i → arrange tiles per crop_shape
  3. Merge: local_tiled + global + view_separator
```

## Implementation

### File: `vllm/model_executor/models/deepseek_ocr.py`

#### 1. Add `SupportsEncoderCudaGraph` to class and imports

Add to imports:
```python
from vllm.v1.worker.encoder_cudagraph_defs import (
    EncoderCudaGraphCaptureInputs,
    EncoderCudaGraphConfig,
    EncoderCudaGraphItemInfo,
    EncoderCudaGraphReplayBuffers,
)
from .interfaces import SupportsEncoderCudaGraph  # already imported as SupportsMultiModal etc.
```

Change class declaration:
```python
class DeepseekOCRForCausalLM(nn.Module, SupportsMultiModal, SupportsPP, SupportsLoRA, SupportsEncoderCudaGraph):
```

Add class var:
```python
supports_encoder_cudagraph = True
```

#### 2. `get_encoder_cudagraph_config()`

```python
def get_encoder_cudagraph_config(self):
    return EncoderCudaGraphConfig(
        modalities=["image"],
        input_keys=["pixel_values", "images_crop"],
        buffer_keys=[],
        out_hidden_size=self.projector_config.n_embed,
        max_frames_per_video=0,
    )
```

#### 3. `get_encoder_cudagraph_budget_range(vllm_config)`

```python
def get_encoder_cudagraph_budget_range(self, vllm_config):
    # Min: one image without patches
    min_budget = global_tokens_with_nl + 1  # 72 + 1 view_separator = 73
    max_budget = min(
        vllm_config.scheduler_config.max_num_batched_tokens,
        self.model_config.max_model_len,
    )
    return (min_budget, max_budget)
```

#### 4. `get_encoder_cudagraph_item_info(mm_kwargs)`

```python
def get_encoder_cudagraph_item_info(self, mm_kwargs):
    images_spatial_crop = mm_kwargs["images_spatial_crop"]
    is_tiled = (images_spatial_crop[:, 0] > 1) | (images_spatial_crop[:, 1] > 1)
    num_patches = torch.where(is_tiled, images_spatial_crop.prod(dim=-1), 0)
    
    return [
        EncoderCudaGraphItemInfo(
            num_output_tokens=global_tokens_with_nl + int(np) * patch_tokens_with_nl + 1,
            input_size=global_tokens_raw + int(np) * patch_tokens_raw,
        )
        for np in num_patches
    ]
```

#### 5. `select_encoder_cudagraph_items(mm_kwargs, indices)`

Slice `pixel_values` by indices (batched), slice `images_crop` by cumulative patch counts, subset `images_spatial_crop`:

```python
def select_encoder_cudagraph_items(self, mm_kwargs, indices):
    pixel_values = mm_kwargs["pixel_values"]        # [B, 3, H, W]
    images_crop = mm_kwargs["images_crop"]           # [P, 3, 640, 640]
    images_spatial_crop = mm_kwargs["images_spatial_crop"]  # [B, 2]
    
    if len(indices) == 0:
        return {
            "pixel_values": pixel_values[:0],
            "images_crop": images_crop[:0],
            "images_spatial_crop": images_spatial_crop[:0],
        }
    
    # Compute cumulative patch counts
    is_tiled = (images_spatial_crop[:, 0] > 1) | (images_spatial_crop[:, 1] > 1)
    patches_per_image = torch.where(is_tiled, images_spatial_crop.prod(dim=-1), 0)
    cum_patches = [0]
    for p in patches_per_image:
        cum_patches.append(cum_patches[-1] + int(p))
    
    selected_pv = pixel_values[indices]
    selected_sp = images_spatial_crop[indices]
    selected_ic = torch.cat(
        [images_crop[cum_patches[i]:cum_patches[i+1]] for i in indices]
    )
    
    return {
        "pixel_values": selected_pv,
        "images_crop": selected_ic,
        "images_spatial_crop": selected_sp,
    }
```

#### 6. `prepare_encoder_cudagraph_capture_inputs(...)`

Create dummy pixel_values and images_crop sized for max_batch_size and max_patches:

```python
def prepare_encoder_cudagraph_capture_inputs(self, token_budget, max_batch_size, max_frames_per_batch, device, dtype):
    # Compute max_num_patches from budget
    max_num_patches = max(0, (token_budget - max_batch_size * global_tokens_with_nl) // patch_tokens_with_nl)
    
    dummy_pixel_values = torch.randn(max_batch_size, 3, BASE_SIZE, BASE_SIZE, device=device, dtype=dtype)
    dummy_images_crop = torch.randn(max_num_patches, 3, IMAGE_SIZE, IMAGE_SIZE, device=device, dtype=dtype)
    
    mm_kwargs = {
        "pixel_values": dummy_pixel_values,
        "images_crop": dummy_images_crop,
    }
    
    return EncoderCudaGraphCaptureInputs(mm_kwargs=mm_kwargs, buffers={})
```

#### 7. `prepare_encoder_cudagraph_replay_buffers(...)`

No metadata buffers needed:
```python
def prepare_encoder_cudagraph_replay_buffers(self, mm_kwargs, max_batch_size, max_frames_per_batch):
    return EncoderCudaGraphReplayBuffers(buffers={})
```

#### 8. `encoder_cudagraph_forward(mm_kwargs, buffers)` — THE CRITICAL METHOD

Batched SAM + CLIP + projector + newline insertion:

```python
def encoder_cudagraph_forward(self, mm_kwargs, buffers):
    pixel_values = mm_kwargs["pixel_values"]   # [B, 3, H, W]
    images_crop = mm_kwargs["images_crop"]      # [P, 3, 640, 640]
    
    n_embed = self.projector_config.n_embed
    
    # --- Global branch ---
    global_feat_1 = self.sam_model(pixel_values)                      # [B, 1024, H_sam, W_sam]
    global_feat_2 = self.vision_model(pixel_values, global_feat_1)    # CLIP with SAM output
    global_feat = torch.cat(
        (global_feat_2[:, 1:], global_feat_1.flatten(2).permute(0, 2, 1)),
        dim=-1,
    )
    global_proj = self.projector(global_feat)                         # [B, global_tokens_raw, n_embed]
    
    # Add newline column to each global image
    _, hw, _ = global_proj.shape
    side = int(hw ** 0.5)
    global_proj = global_proj.view(-1, side, side, n_embed)
    newline = self.image_newline[None, None, :].expand(global_proj.shape[0], side, 1, n_embed)
    global_with_nl = torch.cat([global_proj, newline], dim=2)         # [B, side, side+1, n_embed]
    global_flat = global_with_nl.reshape(-1, n_embed)
    
    # --- Local branch ---
    has_patches = images_crop.shape[0] > 0
    if has_patches:
        local_feat_1 = self.sam_model(images_crop)
        local_feat_2 = self.vision_model(images_crop, local_feat_1)
        local_feat = torch.cat(
            (local_feat_2[:, 1:], local_feat_1.flatten(2).permute(0, 2, 1)),
            dim=-1,
        )
        local_proj = self.projector(local_feat)                       # [P, patch_tokens_raw, n_embed]
        
        # Add newline column to each patch
        _, hw, _ = local_proj.shape
        patch_side = int(hw ** 0.5)
        local_proj = local_proj.view(-1, patch_side, patch_side, n_embed)
        newline = self.image_newline[None, None, :].expand(local_proj.shape[0], patch_side, 1, n_embed)
        local_with_nl = torch.cat([local_proj, newline], dim=2)       # [P, patch_side, patch_side+1, n_embed]
        local_flat = local_with_nl.reshape(-1, n_embed)
        
        return torch.cat([global_flat, local_flat], dim=0)
    
    return global_flat
```

#### 9. `encoder_eager_forward(mm_kwargs)`

Use the existing eager path:
```python
def encoder_eager_forward(self, mm_kwargs):
    image_input = DeepseekOCRImagePixelInputs(
        type="pixel_values",
        data=mm_kwargs["pixel_values"],
        images_crop=mm_kwargs["images_crop"],
        images_spatial_crop=mm_kwargs["images_spatial_crop"],
    )
    vision_embeddings = self._process_image_input(image_input)
    return torch.cat(vision_embeddings, dim=0)
```

#### 10. `postprocess_encoder_output(...)` — CPU-side tile arrangement and merge

```python
def postprocess_encoder_output(self, output, indices, per_item_out_tokens, dest, clone, batch_mm_kwargs):
    images_spatial_crop = batch_mm_kwargs["images_spatial_crop"]  # [bsz, 2]
    n_embed = output.shape[-1]
    bsz = len(indices)
    
    # Compute num_patches per image
    is_tiled = (images_spatial_crop[:, 0] > 1) | (images_spatial_crop[:, 1] > 1)
    num_patches = [int(np) for np in torch.where(is_tiled, images_spatial_crop.prod(dim=-1), 0)]
    total_patches = sum(num_patches)
    
    global_tokens = bsz * global_tokens_with_nl     # 72 per image
    patch_tokens = total_patches * patch_tokens_with_nl  # 30 per patch
    
    global_part = output[:global_tokens].reshape(bsz, global_tokens_with_nl, n_embed)
    local_part = output[global_tokens:global_tokens + patch_tokens] if patch_tokens > 0 else None
    
    merged = {}
    cur_patch = 0
    for i, idx in enumerate(indices):
        np = num_patches[i]
        parts = []
        
        # Local patches: arrange into tiles, add newline row, flatten
        if np > 0:
            crop_shape = images_spatial_crop[i]
            width_tiles = int(crop_shape[0].item())
            height_tiles = int(crop_shape[1].item())
            
            patches = local_part[cur_patch:cur_patch + np]  # [np, patch_tokens_with_nl, n_embed]
            cur_patch += np
            
            # Each patch is [patch_side, patch_side+1, n_embed] = [5, 6, n_embed]
            patches = patches.reshape(np, patch_side, patch_side + 1, n_embed)
            # Arrange into grid: [height_tiles, width_tiles, patch_side, patch_side+1, n_embed]
            patches = patches.reshape(height_tiles, width_tiles, patch_side, patch_side + 1, n_embed)
            # Permute and reshape to [height_tiles*patch_side, width_tiles*(patch_side+1), n_embed]
            patches = patches.permute(0, 2, 1, 3, 4).reshape(
                height_tiles * patch_side, width_tiles * (patch_side + 1), n_embed
            )
            # Add newline row
            newline_row = self.image_newline[None, None, :].expand(
                height_tiles * patch_side, 1, n_embed
            )
            patches = torch.cat([patches, newline_row], dim=1)  # [H_merged, W_merged+1, n_embed]
            patches = patches.reshape(-1, n_embed)
            parts.append(patches)
        
        # Global
        parts.append(global_part[i])  # [global_tokens_with_nl, n_embed]
        # View separator
        parts.append(self.view_seperator[None, :])  # [1, n_embed]
        
        merged[idx] = torch.cat(parts, dim=0)
    
    for i, idx in enumerate(indices):
        dest[idx] = merged[idx]
```

## Constants Caching

The derived constants (`global_side`, `patch_side`, `global_tokens_raw`, etc.) are computed from the input shapes and model architecture. For CUDA graph correctness, these must be constant at capture time. They depend on:
- `BASE_SIZE` = 1024 (fixed constant from processor)
- `IMAGE_SIZE` = 640 (fixed constant from processor)
- SAM architecture (stride=2 for net_2, net_3)
- `MlpProjectorConfig.downsample_ratio` = 2

These are all invariant for a given model instance. We can compute them once in `get_encoder_cudagraph_config()` or cache them as instance attributes.

## Verification

1. Run eager forward to capture pixel_values, images_crop, images_spatial_crop shapes
2. Verify CUDA graph captures without errors
3. Verify CUDA graph replay outputs match eager outputs numerically
4. Run existing DeepSeek-OCR tests to verify no regression

## Files Modified

| File | Changes |
|------|---------|
| `vllm/model_executor/models/deepseek_ocr.py` | Add `SupportsEncoderCudaGraph`, implement 9 protocol methods |

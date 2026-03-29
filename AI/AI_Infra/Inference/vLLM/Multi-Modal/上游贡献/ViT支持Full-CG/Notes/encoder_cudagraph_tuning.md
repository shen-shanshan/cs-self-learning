# Tuning `encoder_cudagraph_token_budgets` and `encoder_cudagraph_max_images_per_batch`

## Background

These two compilation config fields control the CUDA graph capture/replay for the
vision encoder (ViT) in multimodal models like Qwen3-VL:

- **`encoder_cudagraph_token_budgets`**: list of token budgets; one CUDA graph is
  captured per budget. At inference the greedy packer picks the smallest fitting
  budget for each batch.
- **`encoder_cudagraph_max_images_per_batch`**: maximum number of video/image items
  per encoder call. The greedy packer enforces both this limit and the token budget.

---

## Step 1 — Compute `tokens_per_item` for your bucket config

For Qwen3-VL with bucket entry `(h, w, num_frames)`:

```
t            = num_frames / temporal_patch_size   # temporal_patch_size = 2
h_out        = h / patch_size / spatial_merge     # patch_size = 14, spatial_merge = 2
w_out        = w / patch_size / spatial_merge
tokens_per_item = t × h_out × w_out
```

### Example: `(336, 336, 8)`

```
t       = 8 / 2 = 4
h_out   = 336 / 14 / 2 = 12
w_out   = 336 / 14 / 2 = 12
tokens  = 4 × 12 × 12 = 576
```

---

## Step 2 — Set aligned token budgets

The key rule: **each budget should be an exact multiple of `tokens_per_item`**.

Misaligned budgets cause two problems:
1. **Wasted token slots** — the CUDA graph runs flash-attention over empty
   (zero-length) phantom patches between the real tokens and the budget ceiling.
2. **Unused captures** — a budget smaller than `tokens_per_item` can never be
   used; it wastes warmup time capturing a graph that is never replayed.

### General formula

```python
tokens_per_item = t * (h // patch_size // m) * (w // patch_size // m)

budgets = [tokens_per_item * n for n in range(1, max_items_per_batch + 1)]
```

### Example: `(336, 336, 8)` with `max_images_per_batch = 8`

```python
budgets = [576, 1152, 1728, 2304, 2880, 3456, 4032, 4608]
```

| Budget | Items that fit | Token waste |
|--------|---------------|-------------|
| 576    | 1             | 0 (0%)      |
| 1152   | 2             | 0 (0%)      |
| 1728   | 3             | 0 (0%)      |
| 2304   | 4             | 0 (0%)      |
| 2880   | 5             | 0 (0%)      |
| 3456   | 6             | 0 (0%)      |
| 4032   | 7             | 0 (0%)      |
| 4608   | 8             | 0 (0%)      |

Compare with a misaligned budget list like `[512, 1024, 1536, 2048, ..., 4864]`:

| Budget | Items that fit | Token waste |
|--------|---------------|-------------|
| 512    | **0** — never used, wastes a graph capture | — |
| 1024   | 1              | 448 (44%)   |
| 1536   | 2              | 384 (25%)   |
| 2048   | 3              | 320 (16%)   |
| 4864   | 8              | 256 (6%)    |

---

## Step 3 — Set `encoder_cudagraph_max_images_per_batch`

Higher values → fewer encoder calls → better GPU utilization. The practical ceiling
is `floor(max_model_len / tokens_per_item)`.

With `max_model_len = 8192` and `tokens_per_item = 576`:

```
max_items = floor(8192 / 576) = 14
```

To use all 14:

```json
"encoder_cudagraph_token_budgets": [576, 1152, 1728, 2304, 2880, 3456, 4032, 4608, 5184, 5760, 6336, 6912, 7488, 8064],
"encoder_cudagraph_max_images_per_batch": 14
```

**Trade-off:** more budgets = more CUDA graphs captured = longer warmup.
8–10 budgets is usually a good balance between coverage and warmup time.

---

## Final recommended config for `(336, 336, 8)` videos

```json
"compilation_config": {
  "cudagraph_mm_encoder": true,
  "encoder_cudagraph_token_budgets": [576, 1152, 1728, 2304, 2880, 3456, 4032, 4608],
  "encoder_cudagraph_max_images_per_batch": 8
}
```

Or with higher throughput (longer warmup):

```json
"compilation_config": {
  "cudagraph_mm_encoder": true,
  "encoder_cudagraph_token_budgets": [576, 1152, 1728, 2304, 2880, 3456, 4032, 4608, 5184, 5760, 6336, 6912, 7488, 8064],
  "encoder_cudagraph_max_images_per_batch": 14
}
```

---

## Quick reference: `tokens_per_item` for common bucket sizes (Qwen3-VL)

| Bucket `(h, w, frames)` | t | h_out | w_out | tokens |
|-------------------------|---|-------|-------|--------|
| (224, 224, 2)           | 1 | 8     | 8     | 64     |
| (336, 336, 2)           | 1 | 12    | 12    | 144    |
| (448, 448, 2)           | 1 | 16    | 16    | 256    |
| (336, 336, 8)           | 4 | 12    | 12    | 576    |
| (448, 448, 8)           | 4 | 16    | 16    | 1024   |
| (336, 336, 16)          | 8 | 12    | 12    | 1152   |
| (448, 448, 16)          | 8 | 16    | 16    | 2048   |

*patch_size = 14, spatial_merge_size = 2, temporal_patch_size = 2*

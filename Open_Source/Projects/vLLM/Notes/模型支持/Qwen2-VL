# Qwen2-VL

## Layers

---

Qwen2VisionMLP:

- fc1 = ColumnParallelLinear
- act = QuickGELU
- fc2 = RowParallelLinear

---

Qwen2VisionAttention:

- qkv = ColumnParallelLinear
- proj = RowParallelLinear
- rotary_pos_emb
- attention 3 types

---

Qwen2VisionBlock:

- norm1 = norm_layer
- norm2 = norm_layer
- attn = Qwen2VisionAttention
- mlp = Qwen2VisionMLP

---

Qwen2VisionPatchEmbed:

- proj = nn.Conv3d

---

Qwen2VisionPatchMerger:

- ln_q = norm_layer
- mlp = ColumnParallelLinear, nn.GELU(), RowParallelLinear

---

Qwen2VisionRotaryEmbedding:

---

Qwen2VisionTransformer:

- patch_embed = Qwen2VisionPatchEmbed
- rotary_pos_emb = Qwen2VisionRotaryEmbedding
- blocks = Qwen2VisionBlock * n
- merger = Qwen2VisionPatchMerger

---

**Different from Qwen2.5-VL:**

norm_layer = partial(nn.LayerNorm, eps=norm_eps)

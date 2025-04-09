# Qwen2.5-VL

```
Layer:
- Qwen 方法 = vLLM 算子
```

---

## Layers

**Qwen2_5_VisionTransformer:**

- **patch_embed** = Qwen2_5_VisionPatchEmbed
- **rotary_pos_emb** = Qwen2_5_VisionRotaryEmbedding
- **blocks** = Qwen2_5_VisionBlock * layer_num
- **merger** = Qwen2_5_VisionPatchMerger

---
**Qwen2_5_VisionPatchEmbed:**

- **proj** = nn.Conv3d

---

**Qwen2_5_VisionRotaryEmbedding:**

---

**Qwen2_5_VisionBlock:**

- **norm1** = RMSNorm
- **attn** = Qwen2_5_VisionAttention
- **norm2** = RMSNorm
- **mlp** = Qwen2_5_VisionMLP

**Qwen2_5_VisionAttention:**

- **qkv** = ColumnParallelLinear
- **rotary_emb**
  - apply_rotary_emb_torch
  - from flash_attn.layers.rotary import apply_rotary_emb
- **attention**
  - FLASH_ATTN: flash_attn_varlen_func
  - TORCH_SDPA: scaled_dot_product_attention
  - XFORMERS: memory_efficient_attention_forward
- **proj** = RowParallelLinear

**Qwen2_5_VisionMLP:**

- **gate_proj** = ColumnParallelLinear
- **act_fn** = F.silu
- **up_proj** = ColumnParallelLinear
- **down_proj** = RowParallelLinear

---

**Qwen2_5_VisionPatchMerger:**

- **ln_q** = RMSNorm
- **mlp** = nn.ModuleList([ColumnParallelLinear, nn.GELU(), RowParallelLinear])

---

## Forward pipeline

x ->

1. patchify: **Qwen2_5_VisionPatchEmbed**
2. compute position embedding: **Qwen2_5_VisionRotaryEmbedding**
3. windows attention
4. compute cu_seqlens
5. transformers: **Qwen2_5_VisionBlock * layer_num**
6. adapter: **Qwen2_5_VisionPatchMerger**

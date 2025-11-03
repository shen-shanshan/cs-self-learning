# Qwen2.5-VL 差异对比

## Qwen2_5_VLForConditionalGeneration

### \_\_init\_\_()

```python
# vllm
self.use_data_parallel = multimodal_config.mm_encoder_tp_mode == "data"

if multimodal_config.get_limit_per_prompt(
    "image"
) or multimodal_config.get_limit_per_prompt("video"):
    attn_backend_override = (
        multimodal_config.mm_encoder_attn_backend
        if multimodal_config is not None
        else None
    )
    self.visual = Qwen2_5_VisionTransformer(
        vision_config=config.vision_config,
        norm_eps=getattr(config, "rms_norm_eps", 1e-6),
        quant_config=self.quant_config,
        prefix=maybe_prefix(prefix, "visual"),
        use_data_parallel=self.use_data_parallel,
        attn_backend_override=attn_backend_override,
    )
else:
    self.visual = None


# vllm-ascend
self.visual = AscendQwen2_5_VisionTransformer(
    vision_config=config.vision_config,
    norm_eps=getattr(config, "rms_norm_eps", 1e-6),
    quant_config=quant_config,
    prefix=maybe_prefix(prefix, "visual"),
)
```

### _process_image_input()

```python
# vllm
if image_input["type"] == "image_embeds":
    ...
else:
    pixel_values = image_input["pixel_values"]
    with set_forward_context(None, self.vllm_config):
        if self.use_data_parallel:
            return run_dp_sharded_mrope_vision_model(
                self.visual, pixel_values, grid_thw_list, rope_type="rope_3d"
            )
        else:
            image_embeds = self.visual(pixel_values, grid_thw=grid_thw_list)


# vllm-ascend
if image_input["type"] == "image_embeds":
    ...
else:
    pixel_values = image_input["pixel_values"].type(self.visual.dtype)
    image_embeds = self.visual(pixel_values, grid_thw=grid_thw)
```

### _process_image_input()

- 差异点和 `_process_image_input()` 相同

## Qwen2_5_VisionTransformer

### \_\_init\_\_()

```python
# vllm
self.patch_embed = Qwen2_5_VisionPatchEmbed()
self.blocks = [Qwen2_5_VisionBlock(), ...]
self.merger = Qwen2_5_VisionPatchMerger()


# vllm-ascend
self.patch_embed = AscendQwen2_5_VisionPatchEmbed()
self.blocks = [AscendQwen2_5_VisionBlock(), ...]
# padding 处理
```

### load_weights()

- `stacked_params_mapping` 不同
- param padding 处理

```python
# vllm

# vllm-ascend
pad_qkv_weight_scale_offset()
pad_qkv_deq_scale_quant_bias()
pad_proj_weight()
pad_qkv_weight()
pad_qkv_bias()
```

### forward()

```python
# vllm
for t, h, w in grid_thw: ...
# compute reverse indices
# pre-compute seqlens for window/full attn to reduce cuMemcpy operations
hidden_states = blk(
    hidden_states,
    cu_seqlens=cu_seqlens_now,
    rotary_pos_emb=rotary_pos_emb,
    max_seqlen=max_seqlen_now,
    seqlens=seqlens_now,
)

# vllm-ascend
x = blk(x, cu_seqlens=cu_seqlens_now, cos=cos, sin=sin)
```

### 其它 Ascend 自定义方法

- 各种 `pad_xxx()`
- `cal_cos_sin()`
- `rot_pos_emb()`，vllm 中是 `rotary_pos_emb_thw()`
- `get_window_index()`，vllm 中是 `get_window_index_thw()`

## Qwen2_5_VisionRotaryEmbedding

### \_\_init\_\_()

```python
# vllm
inv_freq = 1.0 / (
    theta ** (torch.arange(0, dim, 2, dtype=torch.float, device="cpu") / dim)
)
self.register_buffer("inv_freq", inv_freq, persistent=False)


# vllm-ascend
inv_freq = 1.0 / (theta ** (torch.arange(0, dim, 2, dtype=torch.float) / dim))
self.inv_freq = inv_freq
```

## Qwen2_5_VisionPatchEmbed

### forward()

```python
# vllm
x = self.proj(x)  # ReplicatedLinear


# vllm-ascend
x = x.matmul(self.proj.weight.data.view(self.hidden_size, -1).transpose(0, 1))
```

## Qwen2_5_VisionBlock

- 将 `Qwen2_5_VisionAttention` 替换为 `AscendQwen2_5_VisionAttention`
- `forward()` 参数不兼容，计算流程不同？

## Qwen2_5_VisionAttention

### \_\_init\_\_()

- Ascend：padding 逻辑处理

### split_qkv()

- Ascend：没有对 TP 的特殊处理

### forward()

- 参数不兼容
- 算子替换：`torch_npu._npu_flash_attention_unpad()`

---

```python
# vllm
# vllm-ascend
```

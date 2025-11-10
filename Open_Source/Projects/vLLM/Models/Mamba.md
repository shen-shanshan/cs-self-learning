# Mamba in vllm

[How to support models that use Mamba?](https://docs.vllm.ai/en/stable/contributing/model/basic.html?h=mamba#how-to-support-models-that-use-mamba)

- Layer: `vllm.model_executor.layers.mamba`
- Attention: `vllm.v1.attention.backends.mamba_attn`
- Utils: `vllm.model_executor.layers.mamba.mamba_utils`

Call `get_attn_backend()` method:

- MiniMaxText01LinearAttention
- MambaMixer
- MambaMixer2
- ShortConv

class Attention:

1. `get_attn_backend()` in `selector.py`
2. `attention_cls = current_platform.get_attn_backend_cls()` -> `resolve_obj_by_qualname(attention_cls)`

## 继承关系

AttentionLayerBase:

- Attention
- MLAAttention
- **MambaBase:**
  - **MiniMaxText01LinearAttention:**
    - LinearAttentionBackend
    - `mamba_type`: `linear_attention`
  - **MambaMixer:**
    - Mamba1AttentionBackend
    - `mamba_type`: `mamba1`
  - **MambaMixer2:**
    - Mamba2AttentionBackend
    - `mamba_type`: `mamba2`
  - **ShortConv:**
    - ShortConvAttentionBackend
    - `mamba_type`: `short_conv`
  - **Plamo2MambaMixer:**
    - Mamba2AttentionBackend
    - `mamba_type`: `mamba2`
  - **Qwen3NextGatedDeltaNet:**
    - GDNAttentionBackend
    - `mamba_type`: `linear_attention`

修改点：

```python
class XxxAttention:
    def __init__(self, ...):
        if attn_backend is None:
            self.attn_backend = get_attn_backend(
                head_size,
                dtype,
                kv_cache_dtype,
                block_size,
                use_mla=False,
                has_sink=self.has_sink,
            )
        else:
            self.attn_backend = attn_backend

    def get_attn_backend(self) -> type[AttentionBackend]:
        return self.attn_backend
```

增加接口 `get_mamba_backend()`，位置：

- `selector.py`
- `xxx_platform.py`

```python
# selector.py
def get_mamba_backend(
    mamba_type: str = "",
    selected_backend: Optional[str] = None,
) -> type[AttentionBackend]:
    """..."""
    return _cached_get_mamba_backend(mamba_type, selected_backend)

@cache
def _cached_get_mamba_backend(
    mamba_type: str = "",
    selected_backend: Optional[str] = None,
) -> type[AttentionBackend]:
    """..."""

    # 1.Get device-specific mamba_backend.
    mamba_cls = current_platform.get_mamba_backend_cls(mamba_type, selected_backend)
    if mamba_cls is not None:
        return resolve_obj_by_qualname(mamba_cls)
    
    # 2.Get selected mamba_backend for specific model, e.g., Qwen3-Next.
    if selected_backend is not None:
        return resolve_obj_by_qualname(selected_backend)
    
    # 3.Get default mamba_backend according to mamba_type.
    mamba_backend_map = {
        "linear_attention": "vllm.v1.attention.backends.linear_attn.LinearAttentionBackend",
        "mamba1": "vllm.v1.attention.backends.mamba1_attn.Mamba1AttentionBackend",
        "mamba2": "vllm.v1.attention.backends.mamba2_attn.Mamba2AttentionBackend",
        "short_conv": "vllm.v1.attention.backends.short_conv_attn.ShortConvAttentionBackend",
    }
    if mamba_type not in mamba_backend_map.keys():
        raise ValueError(f"Invalid mamba type: {mamba_type}.")
    
    return resolve_obj_by_qualname(mamba_backend_map[mamba_type])
```

```python
# platform/interface.py

```

`attn_backend` 优先级：

1. 从 platform 获取；
2. 指定值，如：`GDNAttentionBackend`；
3. 根据 `mamba_type` 从 map 获取。

## `attn_backend` 初始化

ModelRunner:

1. `initialize_attn_backend(...)`
2. `get_attn_backends_for_group(...)`
3. `attn_backend = layers[layer_name].get_attn_backend()`

---

Kimi:

```python
from vllm.model_executor.layers.kda import KimiDeltaAttention
class KimiDeltaAttention
```

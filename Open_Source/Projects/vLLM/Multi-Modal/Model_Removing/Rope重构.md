apply_rotary_pos_emb_vision
dispatch_rotary_emb_function

```python
from vllm.model_executor.layers.rotary_embedding.common import (
    ApplyRotaryEmb,
)

# Qwen2-VL
def apply_rotary_pos_emb_vision(
    t: torch.Tensor, cos: torch.Tensor, sin: torch.Tensor
) -> torch.Tensor:
    rotary_emb_function = dispatch_rotary_emb_function(
        default=partial(apply_rotary_emb_torch, is_neox_style=True)
    )
    output = rotary_emb_function(t, cos, sin).type_as(t)
    return output
```

apply_rotary_emb_dispatch

```python
self.apply_rotary_emb = ApplyRotaryEmb(
    is_neox_style=self.is_neox_style,
    is_unsqueeze=True,
)
```

apply_rotary_emb_torch

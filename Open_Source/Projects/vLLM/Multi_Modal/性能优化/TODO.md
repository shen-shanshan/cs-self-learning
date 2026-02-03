- [ ] reshape to 4D 在上游 MM encoder 算子加入开关
- [ ] 加入模型权重 padding 能力

```
class Qwen3OmniMoeAudioEncoder(nn.Module):
        cu_seqlens = torch.tensor(cu_chunk_lens, device=aftercnn_lens.device).cumsum(
            -1, dtype=torch.int32
        )


class Qwen3_VisionTransformer(nn.Module):
        cu_seqlens = cu_seqlens.to(self.device, non_blocking=True)
```

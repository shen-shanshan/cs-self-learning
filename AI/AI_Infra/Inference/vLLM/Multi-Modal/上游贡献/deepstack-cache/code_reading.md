```python
# class Qwen3_VisionTransformer(nn.Module):

# init
self.deepstack_visual_indexes
self.out_hidden_size = vision_config.out_hidden_size * (
    1 + len(self.deepstack_visual_indexes)
)

# forward
deepstack_feature_lists = []
for layer_num, blk in enumerate(self.blocks):
    hidden_states = blk(
        hidden_states,
        cu_seqlens=encoder_metadata["cu_seqlens"],
        rotary_pos_emb_cos=encoder_metadata["rotary_pos_emb_cos"],
        rotary_pos_emb_sin=encoder_metadata["rotary_pos_emb_sin"],
        max_seqlen=encoder_metadata["max_seqlen"],
        sequence_lengths=encoder_metadata.get("sequence_lengths"),
    )
    if layer_num in self.deepstack_visual_indexes:
        deepstack_merger_idx = self.deepstack_visual_indexes.index(layer_num)
        deepstack_feature = self.deepstack_merger_list[deepstack_merger_idx](
            hidden_states
        )
        deepstack_feature_lists.append(deepstack_feature)
hidden_states = self.merger(hidden_states)
hidden_states = torch.cat(
    [hidden_states] + deepstack_feature_lists, dim=1
)  # [seq_len, hidden_size * (1 + depth_of_deepstack)]
return hidden_states
```

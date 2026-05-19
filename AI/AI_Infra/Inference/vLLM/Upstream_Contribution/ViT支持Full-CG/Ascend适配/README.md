vllm-ascend:

`update_full_graph_params()` -> attn_impl: `update_graph_params()`:

```python
torch.npu.graph_task_update_begin(update_stream, handle)
torch.npu.graph_task_update_end(update_stream)
```

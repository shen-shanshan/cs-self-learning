```
topk_weights, topk_ids, _ = torch_npu.npu_moe_gating_top_k_softmax

torch_npu.npu_moe_init_routing
torch_npu.npu_moe_init_routing_v2

topk_weights:
torch.Size([2048, 8])
topk_ids:
torch.Size([2048, 8])

TokenDispatcherWithAllGather: torch_npu.npu_moe_init_routing_v2
hidden_states:
torch.Size([2048, 2048])
topk_ids:
torch.Size([2048, 8])
```

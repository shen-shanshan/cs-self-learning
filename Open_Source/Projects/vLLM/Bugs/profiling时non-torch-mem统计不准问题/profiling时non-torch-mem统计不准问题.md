```bash
before init_snapshot, non_torch_memory: 0.35 G
after init_snapshot, non_torch_memory: 0.36 G

Qwen3 ViT input: shape=torch.Size([131072, 1536]), dtype=torch.bfloat16, device=npu:0

finish Qwen3_VisionTransformer:
non_torch_memory: 1.06 G

after profiling, non_torch_memory: 0.90 G
after profiling, non_torch_increase: 0.54 G
# disable current_platform.empty_cache()
after profiling, non_torch_memory: 1.07 G
after profiling, non_torch_increase: 0.72 G

# first request:
Qwen3 ViT input: shape=torch.Size([2304, 1536]), dtype=torch.bfloat16, device=npu:0
before vision blocks:
non_torch_memory: 0.90 G
Qwen3_VisionBlock attn:
non_torch_memory increased from 0.90 G to 1.08 G, diff: 182.00 M
```

init_distributed_environment
init_ascend_model_parallel

gc.collect()可以回收部分host内存
torch_npu.npu.empty_cache()可以回收部分显存

在 Ascend / torch-npu 上：
torch.npu.empty_cache() 不仅仅是 PyTorch cache 操作，
它会间接触发 Ascend Runtime / ACL 的内存回收路径，
从而 释放部分 non-torch（runtime / kernel / workspace）显存。

```bash
vllm serve /root/.cache/modelscope/hub/models/deepseek-ai/DeepSeek-V2-Lite \
--max-model-len 16384 \
--enforce-eager
```

```bash
# Cuda:
after profile, non_torch_memory: 0.52 G
after empty cache, non_torch_memory: 0.52 G

# NPU:
after profile, non_torch_memory: 1.07 G
after empty cache, non_torch_memory: 0.89 G
```

before:
Available KV cache memory: 44.36 GiB

after:
Available KV cache memory: 43.84 GiB

diff:
0.52 G

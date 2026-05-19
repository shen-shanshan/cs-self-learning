请优化 vllm/tests/v1/cudagraph/test_encoder_cudagraph.py
1.请根据 xxx 最新的代码
2.SimpleMockVideoViTModel 与 SimpleMockViTModel 有大量重复代码（_forward、select_encoder_cudagraph_items 等逻辑几乎一样），可以考虑提取一个 BaseMockViTModel 基类，子类只覆写模态相关的差异部分。
TODO：
全部删了，让AI直接重新生成。

---
分析 FLASH_ATTN mean 劣化的原因，可能是 CUDA Graph 捕获/回放的固定开销在小规模输入时不划算
考虑增加阈值判断：当 token 数很少时 fallback 到 eager 模式

---
请帮我实现如下功能（qwen3_vl.py:L1848）：
避免每次 replay 都重算 cu_seqlens 和 rotary embeddings
当前每次 replay 都调用 prepare_encoder_metadata 重新计算所有 buffer：

```python
# qwen3_vl.py - prepare_encoder_cudagraph_replay_buffers
buffers = self.visual.prepare_encoder_metadata(
    grid_thw_list, max_frames_per_batch=max_frames_per_batch)
prepare_encoder_metadata 内部做了大量 CPU 侧计算（NumPy 拼接、padding、转 tensor），这些都是同步操作，会阻塞 GPU pipeline。
```

优化思路: 对相同 grid_thw 模式缓存 replay buffers。视频推理中很多视频分辨率相同（如统一 resize 到 256×256），相同 grid_thw 的 replay buffers 完全可以复用：

```python
# 伪代码
_replay_cache: dict[tuple, EncoderCudaGraphReplayBuffers] = {}

def prepare_encoder_cudagraph_replay_buffers(self, mm_kwargs, ...):
    cache_key = tuple(tuple(x) for x in grid_thw_list)
    if cache_key in self._replay_cache:
        return self._replay_cache[cache_key]
    # ... 计算 ...
    self._replay_cache[cache_key] = result
    return result
```

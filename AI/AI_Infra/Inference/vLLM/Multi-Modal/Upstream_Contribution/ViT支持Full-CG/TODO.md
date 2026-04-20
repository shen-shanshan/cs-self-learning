# TODO

## Future Works

## Support Video Inference

- [x] 用 AI 重新生成 PR 描述（写 skill）
- [x] cuda graph replay buffer 增加驱逐策略，设置长度上限
- [x] 用 AI 重新生成测试用例
- [x] 测试多卡 vit dp mode 性能
- [x] 更新对应文档
- [x] 理解视频 dummy grid_thw 构造策略
- [x] 更新注释：当 EVS（Efficient Video Sampling）剪枝启用时，token 数量是数据依赖的（需根据帧间差异动态选择保留的 token），无法被 CUDA Graph 捕获，因此仅在 EVS 关闭时启用视频 CUDA Graph。

---

请为 PR https://github.com/vllm-project/vllm/pull/38061/changes 中的改动在 tests/v1/cudagraph/test_encoder_cudagraph.py 中生成对应的测试用例，要求尽可能复用 SimpleMockViTModel 中的代码，减少重复代码。

```bash
pytest tests/v1/cudagraph/test_encoder_cudagraph.py -v
```

cache:

```python
def _maybe_get_cached_replay_buffers(
    self,
    cache_key: tuple,
) -> EncoderCudaGraphReplayBuffers | None:
    # Cache keyed by (modality, grid_thw_list): identical grid shapes
    # produce identical cu_seqlens, rotary embeddings, and positional
    # embeddings, so the expensive CPU-side NumPy work inside
    # prepare_encoder_metadata need only run once per unique shape.
    self._replay_buffer_cache: dict[tuple, EncoderCudaGraphReplayBuffers] = {}
    cached = self._replay_buffer_cache.get(cache_key, None)
    return cached

def _maybe_set_cached_replay_buffers(
    self,
    cache_key: tuple,
    buffers: EncoderCudaGraphReplayBuffers,
) -> None:
    # Update cuda graph replay buffer cache with the newly computed buffers, so
    # that subsequent replays with the same grid_thw pattern can reuse them.
    self._replay_buffer_cache[cache_key] = buffers

    # TODO(shen-shanshan): Design more appropriate cache eviction strategy.
    if len(self._replay_buffer_cache) > 10:
        self._replay_buffer_cache.popitem(last=False)

def prepare_encoder_cudagraph_replay_buffers(
    self,
    mm_kwargs: dict[str, Any],
    max_batch_size: int,
    max_frames_per_batch: int,
):
    modality = self.get_input_modality(mm_kwargs)
    grid_thw_list = self._get_grid_thw_by_modality(mm_kwargs)

    cache_key = (
        modality,
        tuple(tuple(thw) for thw in grid_thw_list),
    )
    cached = self._maybe_get_cached_replay_buffers(cache_key)
    if cached is not None:
        return cached

    if modality == "image":
        buffers = self.visual.prepare_encoder_metadata(
            grid_thw_list,
            max_batch_size=max_batch_size,
        )
    else:
        buffers = self.visual.prepare_encoder_metadata(
            grid_thw_list,
            max_frames_per_batch=max_frames_per_batch,
        )

    result = EncoderCudaGraphReplayBuffers(buffers=buffers)
    self._maybe_set_cached_replay_buffers(cache_key, result)
    return result
```

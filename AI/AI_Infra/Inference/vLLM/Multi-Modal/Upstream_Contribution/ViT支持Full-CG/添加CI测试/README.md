Prompt:

背景：https://github.com/vllm-project/vllm/issues/38175 为 vllm 的 VLM 模型支持了 vit cuda graph。
需求：请为该特性添加一个 CI e2e 测试用例，模型使用 Qwen3-VL-2B-Instruct，测试场景需要覆盖：图像输入、视频输入，通过判断开启 cudagraph_mm_encoder 的结果和不开启 cudagraph_mm_encoder 的结果是否相等，作为测试是否通过的依据。

---

```python
MODEL = "/shared/models/modelscope/models/Qwen/Qwen3-VL-8B-Instruct"

def _compilation_config(*, cudagraph_mm_encoder: bool) -> dict:
    return {
        "cudagraph_mm_encoder": cudagraph_mm_encoder,
        "encoder_cudagraph_max_vision_items_per_batch": 1,
    }
```

---
Test:

```bash
pytest -sv tests/models/multimodal/generation/test_qwen3_vl_vit_cudagraph.py::test_vit_cudagraph_image[qwen3_vl]
pytest -sv tests/models/multimodal/generation/test_qwen3_vl_vit_cudagraph.py::test_vit_cudagraph_video
```

Results:

```bash
# Image test:


# Video test:
EncoderCudaGraphManager initialized with budgets=[64, 128, 256, 512, 1024, 2048, 4096], max_batch_size=1, max_frames_per_batch=2, use_dp=False
FAILED tests/models/multimodal/generation/test_qwen3_vl_vit_cudagraph.py::test_vit_cudagraph_video[qwen3_vl] - vllm.v1.engine.exceptions.EngineDeadError: ('EngineCore encountered an issue. See stack trace (above) for the root cause.', 'EngineCore encountered an issue. See stack trace (above) for the root cause.')
```

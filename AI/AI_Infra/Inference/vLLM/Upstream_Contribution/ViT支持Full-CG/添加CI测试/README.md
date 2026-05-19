Prompt:

背景：https://github.com/vllm-project/vllm/issues/38175 为 vllm 的 VLM 模型支持了 vit cuda graph。
需求：请为该特性添加一个 CI e2e 测试用例，模型使用 Qwen3-VL-2B-Instruct，测试场景需要覆盖：图像输入、视频输入，通过判断开启 cudagraph_mm_encoder 的结果和不开启 cudagraph_mm_encoder 的结果是否相等，作为测试是否通过的依据。

---

```python
MODEL = "/shared/models/modelscope/models/Qwen/Qwen3-VL-8B-Instruct"
```

---
Test:

```bash
export VLLM_USE_MODELSCOPE=False
export HF_ENDPOINT="https://hf-mirror.com"
export CUDA_VISIBLE_DEVICES=7

# [qwen3_vl]
pytest -sv tests/models/multimodal/generation/test_vit_cudagraph.py::test_vit_cudagraph_image
pytest -sv tests/models/multimodal/generation/test_vit_cudagraph.py::test_vit_cudagraph_video

pytest tests/models/multimodal/generation/test_vit_cudagraph.py
```

Results:

```bash
# Image test:
✅

# Video test:
EncoderCudaGraphManager initialized with budgets=[64, 128, 256, 512, 1024, 2048, 4096], max_batch_size=1, max_frames_per_batch=2, use_dp=False ?
EncoderCudaGraphManager initialized with budgets=[64, 128, 256, 512, 1024, 2048, 4096], max_batch_size=1, max_frames_per_batch=16, use_dp=False

2 passed, 18 warnings in 112.08s (0:01:52)
```

full logs:

```
pytest tests/models/multimodal/generation/test_vit_cudagraph.py
============================================================================================================ test session starts ============================================================================================================
platform linux -- Python 3.12.3, pytest-9.0.3, pluggy-1.6.0
rootdir: /shared/sss/github/vllm
configfile: pyproject.toml
plugins: asyncio-1.3.0, rerunfailures-16.1, forked-1.6.0, cov-7.1.0, anyio-4.13.0, timeout-2.4.0, shard-0.1.2
asyncio: mode=Mode.STRICT, debug=False, asyncio_default_fixture_loop_scope=None, asyncio_default_test_loop_scope=function
collected 2 items                                                                                                                                                                                                                           
Running 2 items in this shard

tests/models/multimodal/generation/test_vit_cudagraph.py ..                                                                                                                                                                           [100%]

============================================================================================================= warnings summary ==============================================================================================================
<frozen importlib._bootstrap>:488
  <frozen importlib._bootstrap>:488: DeprecationWarning: builtin type SwigPyPacked has no __module__ attribute

<frozen importlib._bootstrap>:488
  <frozen importlib._bootstrap>:488: DeprecationWarning: builtin type SwigPyObject has no __module__ attribute

.venv/lib/python3.12/site-packages/torch/jit/_script.py:365: 14 warnings
  /shared/sss/github/vllm/.venv/lib/python3.12/site-packages/torch/jit/_script.py:365: DeprecationWarning: `torch.jit.script_method` is deprecated. Please switch to `torch.compile` or `torch.export`.
    warnings.warn(

tests/models/multimodal/generation/test_vit_cudagraph.py::test_vit_cudagraph_image[qwen3_vl]
tests/models/multimodal/generation/test_vit_cudagraph.py::test_vit_cudagraph_video[qwen3_vl]
  /shared/sss/github/vllm/tests/utils.py:1280: DeprecationWarning: This process (pid=143524) is multi-threaded, use of fork() may lead to deadlocks in the child.
    pid = os.fork()

-- Docs: https://docs.pytest.org/en/stable/how-to/capture-warnings.html
================================================================================================ 2 passed, 18 warnings in 108.84s (0:01:48) =================================================================================================
```

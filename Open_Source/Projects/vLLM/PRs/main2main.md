```bash
TypeError: MMEncoderAttention.__init__() got an unexpected keyword argument 'multimodal_config'
```

https://github.com/vllm-project/vllm/pull/31972

```bash
# 共享专家为空：
FAILED tests/e2e/multicard/2-cards/test_data_parallel.py::test_qwen3_inference_dp2[32-Qwen/Qwen3-30B-A3B]
FAILED tests/e2e/multicard/2-cards/test_data_parallel.py::test_qwen3_inference_dp2[32-vllm-ascend/Qwen3-30B-A3B-W8A8]
# pytest -sv --durations=0 tests/e2e/multicard/2-cards/test_data_parallel.py
FAILED tests/e2e/multicard/2-cards/test_full_graph_mode.py::test_qwen3_moe_full_decode_only_tp2
# pytest -sv --durations=0 tests/e2e/multicard/2-cards/test_full_graph_mode.py

# FIA：
FAILED tests/e2e/singlecard/test_models.py::test_whisper[openai-mirror/whisper-large-v3-turbo]
# pytest -sv --durations=0 tests/e2e/singlecard/test_models.py
```

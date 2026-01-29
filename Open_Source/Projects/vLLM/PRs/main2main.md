```bash
TypeError: MMEncoderAttention.__init__() got an unexpected keyword argument 'multimodal_config'
```

https://github.com/vllm-project/vllm/pull/31972

```bash
# 共享专家为空 ✅
FAILED tests/e2e/multicard/2-cards/test_data_parallel.py::test_qwen3_inference_dp2[32-Qwen/Qwen3-30B-A3B]
FAILED tests/e2e/multicard/2-cards/test_data_parallel.py::test_qwen3_inference_dp2[32-vllm-ascend/Qwen3-30B-A3B-W8A8]
# pytest -sv --durations=0 tests/e2e/multicard/2-cards/test_data_parallel.py

# AssertionError: Can't unpack a tensor of 512 rows into a tuple of 2 elements.

# pytest -sv --durations=0 tests/e2e/multicard/2-cards/test_full_graph_mode.py
FAILED tests/e2e/multicard/2-cards/test_full_graph_mode.py::test_qwen3_moe_full_decode_only_tp2

# AssertionError: Error in memory profiling. Initial free memory 39909924864, current free memory 47541829632. This happens when the NPU memory was not properly cleaned up before initializing the vLLM instance.
FAILED tests/e2e/multicard/2-cards/test_external_launcher.py::test_qwen3_external_launcher_with_sleepmode

# FIA 算子报错 ✅
# pytest -sv --durations=0 tests/e2e/singlecard/test_models.py
FAILED tests/e2e/singlecard/test_models.py::test_whisper[openai-mirror/whisper-large-v3-turbo]
# pytest -sv tests/e2e/singlecard/test_models.py::test_whisper
# pytest -sv tests/e2e/singlecard/test_aclgraph_accuracy.py::test_piecewise_res_consistency[cur_case0]

# AttributeError: '_OpNamespace' '_C' object has no attribute 'get_cuda_view_from_cpu_tensor'
Failed tests/e2e/singlecard/model_runner_v2/test_basic.py::test_qwen3_dense_eager_mode[True-32-Qwen/Qwen3-0.6B]
Failed tests/e2e/singlecard/model_runner_v2/test_basic.py::test_egale_spec_decoding[True-32-vllm-ascend/EAGLE-LLaMA3.1-Instruct-8B-LLM-Research/Meta-Llama-3.1-8B-Instruct]
# pytest -sv --durations=0 tests/e2e/singlecard/model_runner_v2/test_basic.py

FAILED tests/e2e/multicard/2-cards/test_full_graph_mode.py::test_qwen3_moe_full_decode_only_tp2
```

unified_kv_cache_update(custom_op)
-> attn_layer.impl.do_kv_cache_update()
-> reshape_and_cache_flash()
-> torch_npu._npu_reshape_and_cache

reshape_and_cache

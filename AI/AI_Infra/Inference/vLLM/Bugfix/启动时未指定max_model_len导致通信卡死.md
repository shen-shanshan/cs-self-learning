# 启动时未指定 `max_model_len` 导致通信卡死

## 排查记录

```bash
vllm serve /root/.cache/modelscope/hub/models/Qwen/Qwen3-VL-30B-A3B-Instruct \
--tensor-parallel-size 4 \
--enable-expert-parallel \
--enforce-eager \
--max_model_len 131072
```

```bash
INFO 10-25 02:18:40 [parallel_state.py:1208] rank 0 in world size 4 is assigned as DP rank 0, PP rank 0, TP rank 0, EP rank 0
INFO 10-25 02:18:40 [parallel_state.py:1208] rank 1 in world size 4 is assigned as DP rank 0, PP rank 0, TP rank 1, EP rank 1
INFO 10-25 02:18:40 [parallel_state.py:1208] rank 3 in world size 4 is assigned as DP rank 0, PP rank 0, TP rank 3, EP rank 3
INFO 10-25 02:18:40 [parallel_state.py:1208] rank 2 in world size 4 is assigned as DP rank 0, PP rank 0, TP rank 2, EP rank 2
```

vllm 中 `max_model_len` 的设置逻辑:

```
try_verify_and_update_config()
XxxModelConfig: verify_and_update_config()
recalculate_max_model_len()
get_and_verify_max_len()
_get_and_verify_max_len()


EngineCore: __init__()
_initialize_kv_caches()
get_kv_cache_configs()
check_enough_kv_cache_memory()
estimate_max_model_len()
```

测试记录：

- TP4 + EP: Using max model len 262144 ❌
- TP4 + EP: Using max model len 131072 ❌

修改：

```python
elif current_platform.device_name == "npu" and max_model_len > 65536:
    logger.warning(
        "max_model_len is not specified and the default value"
        "derived from the model config is %d, which is too large"
        "and will make the engine stuck in initializing distributed"
        "communication. Set max_model_len to 65536.",
        max_model_len,
    )
    max_model_len = 65536
```

"max_position_embeddings": 262144

## PR info

When we launch vllm without specifying `--max-model-len`, it will use the default value derived from the model config, e.g., `max_position_embeddings`.

```bash
vllm serve /root/.cache/modelscope/hub/models/Qwen/Qwen3-VL-30B-A3B-Instruct \
--tensor-parallel-size 4 \
--enable-expert-parallel \
--enforce-eager
```

With regard to `Qwen3-VL-30B-A3B`, its default `max_model_len` is `262144`, which is too large and will make the engine stuck in initializing distributed communication.

```bash
...
Using max model len 262144
...
INFO 10-25 02:18:40 [parallel_state.py:1208] rank 0 in world size 4 is assigned as DP rank 0, PP rank 0, TP rank 0, EP rank 0
INFO 10-25 02:18:40 [parallel_state.py:1208] rank 1 in world size 4 is assigned as DP rank 0, PP rank 0, TP rank 1, EP rank 1
INFO 10-25 02:18:40 [parallel_state.py:1208] rank 3 in world size 4 is assigned as DP rank 0, PP rank 0, TP rank 3, EP rank 3
INFO 10-25 02:18:40 [parallel_state.py:1208] rank 2 in world size 4 is assigned as DP rank 0, PP rank 0, TP rank 2, EP rank 2
# Get stuck in here
```

Thus, we need to set a limited default value when `--max-model-len` is not specified. After testing, I find that the engine will get stuck when `max_model_len` > **65536** on Ascend A2 devices. Thus, we set it to **65536** when the default value is too large.

Note: this change is safe since it only affect the usage on Ascend NPU devices.

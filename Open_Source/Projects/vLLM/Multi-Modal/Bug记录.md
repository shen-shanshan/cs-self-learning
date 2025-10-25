# Bug 记录

## 启动时通信卡死

```
INFO 10-25 02:18:40 [parallel_state.py:1208] rank 0 in world size 4 is assigned as DP rank 0, PP rank 0, TP rank 0, EP rank 0
INFO 10-25 02:18:40 [parallel_state.py:1208] rank 1 in world size 4 is assigned as DP rank 0, PP rank 0, TP rank 1, EP rank 1
INFO 10-25 02:18:40 [parallel_state.py:1208] rank 3 in world size 4 is assigned as DP rank 0, PP rank 0, TP rank 3, EP rank 3
INFO 10-25 02:18:40 [parallel_state.py:1208] rank 2 in world size 4 is assigned as DP rank 0, PP rank 0, TP rank 2, EP rank 2
```

vllm:

```
max_model_len

EngineCore: __init__()
_initialize_kv_caches()
get_kv_cache_configs()
check_enough_kv_cache_memory()
estimate_max_model_len()
```

# Forward context is not set

```bash
# vllm
git fetch --tags
git tag -l | grep v0.11.0
git checkout -b v0.11.0 v0.11.0

python
> import vllm
> print(vllm.__version__)
```

```bash
export PYTORCH_NPU_ALLOC_CONF="max_split_size_mb:250"
export CPU_AFFINITY_CONF=2
export MALLOC_CONF='background_thread:true,metadata_thp:auto,dirty_decay_ms:30000,muzzy_decay_ms:30000,abort_conf:true,percpu_arena:phycpu'
export VLLM_RPC_TIMEOUT=120
export TASK_QUEUE_ENABLE=1
export HCCL_OP_EXPANSION_MODE=AIV
export VLLM_ASCEND_ENABLE_FLASHCOMM=1
export VLLM_ASCEND_ENABLE_PREFETCH_MLP=1
export VLLM_ASCEND_MLP_GATE_UP_PREFETCH_SIZE=$((18*1024*1024))
export VLLM_ASCEND_MLP_DOWN_PREFETCH_SIZE=$((18*1024*1024))
export VLLM_LOGGING_LEVEL=WARNING
export USE_OPTIMIZED_MODEL=1
export ASCEND_LAUNCH_BLOCKING=1

vllm serve /root/.cache/modelscope/hub/models/Qwen/Qwen2.5-VL-7B-Instruct \
--max_model_len 16384 \
--max-num-batched-tokens 16384 \
--tensor-parallel-size 4 \
--enforce-eager \
--max-num-seqs 16 \
--dtype bfloat16 \
--trust-remote-code \
--gpu-memory-utilization 0.9

curl -X POST http://localhost:8000/v1/chat/completions \
    -H "Content-Type: application/json" \
    -d '{
        "model": "/root/.cache/modelscope/hub/models/Qwen/Qwen2.5-VL-7B-Instruct",
        "messages": [
            {"role": "system", "content": "You are a helpful assistant."},
            {"role": "user", "content": [
                {"type": "image_url", "image_url": {"url": "https://modelscope.oss-cn-beijing.aliyuncs.com/resource/qwen.png"}},
                {"type": "text", "text": "What is the text in the illustrate? How does it look?"}
            ]}
        ],
        "max_tokens": 100
    }'
```

log:

```bash
(Worker_TP0 pid=51886) ERROR 11-14 08:13:55 [multiproc_executor.py:671] WorkerProc hit an exception.
(Worker_TP0 pid=51886) ERROR 11-14 08:13:55 [multiproc_executor.py:671] Traceback (most recent call last):
(Worker_TP0 pid=51886) ERROR 11-14 08:13:55 [multiproc_executor.py:671]   File "/vllm-workspace/vllm/vllm/v1/executor/multiproc_executor.py", line 666, in worker_busy_loop
(Worker_TP0 pid=51886) ERROR 11-14 08:13:55 [multiproc_executor.py:671]     output = func(*args, **kwargs)
(Worker_TP0 pid=51886) ERROR 11-14 08:13:55 [multiproc_executor.py:671]   File "/vllm-workspace/vllm-ascend/vllm_ascend/worker/worker_v1.py", line 227, in determine_available_memory
(Worker_TP0 pid=51886) ERROR 11-14 08:13:55 [multiproc_executor.py:671]     self.model_runner.profile_run()
(Worker_TP0 pid=51886) ERROR 11-14 08:13:55 [multiproc_executor.py:671]   File "/vllm-workspace/vllm-ascend/vllm_ascend/worker/model_runner_v1.py", line 2557, in profile_run
(Worker_TP0 pid=51886) ERROR 11-14 08:13:55 [multiproc_executor.py:671]     hidden_states = hidden_states[logit_indices]
(Worker_TP0 pid=51886) ERROR 11-14 08:13:55 [multiproc_executor.py:671] RuntimeError: copy_between_host_and_device_opapi:build/CMakeFiles/torch_npu.dir/compiler_depend.ts:56 NPU function error: SUSPECT REMOTE ERROR, error code is 507057
```

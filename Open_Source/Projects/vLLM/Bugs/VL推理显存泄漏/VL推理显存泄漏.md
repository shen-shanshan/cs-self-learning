```bash
# server
export VLLM_SERVER_DEV_MODE=1
export ASCEND_RT_VISIBLE_DEVICES=2,3

vllm serve Qwen/Qwen3-VL-32B-Instruct \
--tensor-parallel-size 2 \
--load-format=dummy \
--max-model-len 162000

python -m vllm.entrypoints.openai.api_server \
--model Qwen/Qwen3-VL-32B-Instruct \
--host 0.0.0.0 \
--port 8000 \
--tensor-parallel-size 2 \
--load-format=dummy \
--max-model-len 163000

vllm serve \
Qwen/Qwen3-VL-32B-Instruct \
--tensor-parallel-size 2 \
--load-format=dummy \
--dtype bfloat16 \

--max-model-len 16384 \
--gpu-memory-utilization 0.97 \
--limit-mm-per-prompt.video=0 \
--max-num-seqs 4 \

# dataset
opendatalab/OmniDocBench

# client
export VLLM_USE_MODELSCOPE=False
export HF_ENDPOINT="https://hf-mirror.com"
vllm bench serve \
--backend openai-chat \
--endpoint /v1/chat/completions \
--model Qwen/Qwen3-VL-32B-Instruct \
--dataset-name hf \
--dataset-path lmarena-ai/VisionArena-Chat \
--num-prompts 1000 \
--request-rate 20

# 实时监视显存占用
# -n 1：每 1 秒刷新一次
watch -n 0.2 npu-smi info

curl -X POST http://localhost:8000/reset_mm_cache
curl -X POST http://localhost:8000/reset_prefix_cache

curl http://localhost:8000/server_info
curl http://localhost:8000/is_sleeping
```

服务启动前：3400/65536
服务启动后：57766/65536
压测之后：61548/65536 (增长 3.7 G)
调用 reset_mm_cache 后：61548/65536

```bash
Loading model weights took 31.4592 GB
Encoder cache will be initialized with a budget of 16384 tokens, and profiled with 1 image items of the maximum feature size.

Available memory: 21540531712
    total memory: 65452113920
```

---

准备数据：

```bash
scp *.jpg root@139.9.155.20:/home/sss/images

# large size
scp pexels-bertellifotografia-16094061.jpg root@139.9.155.20:/home/sss/large_images
```

启动服务：

```bash
vllm serve /root/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct \
--tensor-parallel-size 2 \
--max-model-len 65536 \
--max-num-seqs 8 \
--max-num-batched-tokens 32768 \
--gpu-memory-utilization 0.95
```

旧 profiling 方法：

```bash
comm buffer memory used: 6.103515625e-05 GB.
Loading model weights took 9.1948 GB
Encoder cache will be initialized with a budget of 32768 tokens, and profiled with 2 image items of the maximum feature size.
Available memory: 43.39572334289551 GiB, total memory: 60.95703125 GiB
```

新 profiling 方法：

服务启动前：3409 / 65536
服务启动后：53566 / 65536
peak：65160

```bash
Loading model weights took 9.1948 GB
Encoder cache will be initialized with a budget of 32768 tokens, and profiled with 2 image items of the maximum feature size.
Qwen3-VL dummy_mm_data target_width: 4096.
Qwen3-VL dummy_mm_data target_height: 4096.
Available KV cache memory: 38.41 GiB
GPU KV cache size: 559,232 tokens
encoder_compute_budget: 32768
encoder_cache_size: 32768
```

---

旧版本：

```bash
conda create -n vllm-v0.12.0 python=3.11 -y

cd vllm
git fetch --tags
git tag -l | grep v0.12.0
git checkout -b v0.12.0 v0.12.0

cd vllm-ascend
git fetch --tags
git tag -l | grep v0.12.0rc1
git checkout -b v0.12.0rc1 v0.12.0rc1
```

```bash
NPU out of memory. NPUWorkspaceAllocator tried to allocate 180.00 MiB(NPU 1; 60.96 GiB total capacity; 86.81 MiB free). If you want to reduce memory usage, take a try to set the environment variable TASK_QUEUE_ENABLE=1.

ERR00006 PTA memory error

Memory_Allocation_Failure(EL0004): Failed to allocate memory

alloc device memory failed, runtime result = 207001[FUNC:ReportCallError][FILE:log_inner.cpp][LINE:162]

No available shared memory broadcast block found in 60 seconds. This typically happens when some processes are hanging or doing some time-consuming work (e.g. compilation, weight/kv cache quantization).
```

---

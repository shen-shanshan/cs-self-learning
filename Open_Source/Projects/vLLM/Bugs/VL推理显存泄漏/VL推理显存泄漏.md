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

怀疑对象：
reset_mm_cache
clear_mm_cache

```python
class InputProcessor:
    self.mm_processor_cache = processor_cache_from_config(vllm_config, mm_registry)
    self.input_preprocessor = InputPreprocessor(
        self.model_config,
        tokenizer,
        mm_registry,
        mm_processor_cache=self.mm_processor_cache,
    )

class GPUModelRunner:
    # mm_hash -> encoder_output
    self.encoder_cache: dict[str, torch.Tensor] = {}

    def reset_mm_cache(self) -> None:
        if self.mm_budget:
            self.mm_budget.reset_cache()

class MultiModalBudget:
    self.cache = cache = processor_only_cache_from_config(model_config, mm_registry)
    self.cache.clear_cache()

class MultiModalProcessorOnlyCache:
    self._cache = MultiModalCache.get_lru_cache()

class LRUCache
```

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
--gpu-memory-utilization 0.97
```

启动前：3409
启动后：55441
推理后：

---

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

Qwen3-VL dummy_mm_data target_width: 4096.
Qwen3-VL dummy_mm_data target_height: 4096.

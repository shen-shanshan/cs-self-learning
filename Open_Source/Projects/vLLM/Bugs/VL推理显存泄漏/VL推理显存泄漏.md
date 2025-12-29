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
--gpu-memory-utilization 0.8 \
--limit-mm-per-prompt.video=0 \
--max-num-seqs 4 \

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

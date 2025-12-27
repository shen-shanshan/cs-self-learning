```bash
# server
export ASCEND_RT_VISIBLE_DEVICES=2,3
vllm serve Qwen/Qwen3-VL-32B-Instruct \
--tensor-parallel-size 2 \
--load-format=dummy \
--max-model-len 160000

128000

# export VLLM_USE_MODELSCOPE=False
# export HF_ENDPOINT="https://hf-mirror.com"
# vllm bench serve \
# --backend openai-chat \
# --model /root/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct \
# --endpoint /v1/chat/completions \
# --dataset-name hf \
# --dataset-path lmarena-ai/VisionArena-Chat \
# --hf-split train \
# --num-prompts 1000

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

# -n 1：每 1 秒刷新一次
watch -n 0.2 npu-smi info
```

Qwen3-VL-8B-Instruct OK

怀疑对象：
reset_mm_cache
clear_mm_cache

```
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

```
Loading model weights took 31.4592 GB
Encoder cache will be initialized with a budget of 16384 tokens, and profiled with 1 image items of the maximum feature size.

Available memory: 21540531712
    total memory: 65452113920

ValueError: To serve at least one request with the models's max seq len (262144), (32.00 GiB KV cache is needed, which is larger than the available KV cache memory (20.06 GiB). Based on the available memory, the estimated maximum model length is 164224. Try increasing `gpu_memory_utilization` or decreasing `max_model_len` when initializing the engine. See https://docs.vllm.ai/en/latest/configuration/conserving_memory/ for more details.
```

_update_states

Unknown vLLM environment variable detected: VLLM_USE_V1

---

(EngineCore_DP0 pid=1151762) WARNING 03-03 07:59:13 [multiproc_executor.py:921] Reducing Torch parallelism from 192 threads to 1 to avoid unnecessary CPU contention. Set OMP_NUM_THREADS in the external environment to tune this value as needed.

---

NotImplementedError: Shard id with multiple indices is not supported in weight_loader, please use weight_loader_v2 instead.

WEIGHT_LOADER_V2_SUPPORTED
weight_loader_v2

AscendUnquantizedLinearMethod

RuntimeError: NPU out of memory. Tried to allocate 5.79 GiB (NPU 0; 60.96 GiB total capacity; 60.05 GiB already allocated; 60.05 GiB current active; 503.86 MiB free; 60.07 GiB reserved in total by PyTorch) If reserved memory is >> allocated memory try setting max_split_size_mb to avoid fragmentation.

(APIServer pid=1194498) [2026-03-03 12:20:54] INFO patch_mamba_config.py:69: Setting attention block size to 640 tokens to ensure that attention page size is >= mamba page size.
(APIServer pid=1194498) [2026-03-03 12:20:54] INFO patch_mamba_config.py:87: Padding mamba page size by 22.14% to ensure that mamba page size and attention page size are exactly equal.

Linear layer sharding
Linear layer sharding enabled with config: None. Note: This feature works optimally with FLASHCOMM2 and DSA-CP enabled; using it without these features may result in significant performance degradation.
Linear layer sharding enabled with config: None.

```bash
vllm serve /root/.cache/modelscope/hub/models/Qwen/Qwen3___5-4B \
-tp 2 \
--reasoning-parser qwen3 \
--max_model_len 4096 \
--language-model-only \
--enforce-eager \
--gpu-memory-utilization 0.5

vllm serve /root/.cache/modelscope/hub/models/Qwen/Qwen3___5-4B \
-tp 2 \
--reasoning-parser qwen3 \
--max_model_len 4096 \
--mm-encoder-tp-mode data \
--mm-processor-cache-type shm \
--enforce-eager \
--gpu-memory-utilization 0.5
```

```bash
curl -X POST http://localhost:8000/v1/chat/completions \
    -H "Content-Type: application/json" \
    -d '{
        "model": "/root/.cache/modelscope/hub/models/Qwen/Qwen3___5-4B",
        "messages": [
            {"role": "system", "content": "You are a helpful assistant."},
            {"role": "user", "content": [
                {"type": "text", "text": "What is the future of AI?"}
            ]}
        ],
        "max_completion_tokens": 100
    }'

curl -X POST http://localhost:8000/v1/chat/completions \
    -H "Content-Type: application/json" \
    -d '{
        "model": "/root/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct",
        "messages": [
            {"role": "system", "content": "You are a helpful assistant."},
            {"role": "user", "content": [
                {"type": "image_url", "image_url": {"url": "https://modelscope.oss-cn-beijing.aliyuncs.com/resource/qwen.png"}},
                {"type": "text", "text": "What is the text in the illustrate? How does it look?"}
            ]}
        ],
        "max_completion_tokens": 100
    }'
```

```python
import time
from openai import OpenAI

client = OpenAI(
    api_key="EMPTY",
    base_url="http://localhost:8000/v1",
    timeout=3600
)

messages = [
    {
        "role": "user",
        "content": [
            {
                "type": "image_url",
                "image_url": {
                    "url": "https://ofasys-multimodal-wlcb-3-toshanghai.oss-accelerate.aliyuncs.com/wpf272043/keepme/image/receipt.png"
                }
            },
            {
                "type": "text",
                "text": "Read all the text in the image."
            }
        ]
    }
]

start = time.time()
response = client.chat.completions.create(
    model="/root/.cache/modelscope/hub/models/Qwen/Qwen3___5-4B",
    messages=messages,
    max_tokens=2048
)
print(f"Response costs: {time.time() - start:.2f}s")
print(f"Generated text: {response.choices[0].message.content}")
```

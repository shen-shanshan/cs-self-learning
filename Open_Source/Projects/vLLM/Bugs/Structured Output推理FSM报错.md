文档：https://docs.vllm.ai/en/stable/features/structured_outputs/

- 离线 ✅
- 在线 ✅
- 客户

```bash
# 通用场景
vllm serve /root/.cache/modelscope/hub/models/Qwen/Qwen3-8B \
--max_model_len 16384 \
--enforce-eager

# 客户场景
export ASCEND_RT_VISIBLE_DEVICES=2,3
export ASCEND_RT_VISIBLE_DEVICES=6,7
vllm serve /root/.cache/modelscope/hub/models/Qwen/Qwen3-8B \
--tensor-parallel-size 2 \
--enable-auto-tool-choice \
--tool-call-parser hermes \
--enforce-eager
```

```python
from openai import OpenAI

client = OpenAI(
    base_url="http://localhost:8000/v1",
    api_key="-",
)
model = client.models.list().data[0].id

completion = client.chat.completions.create(
    model=model,
    messages=[
        {"role": "user", "content": "Classify this sentiment: vLLM is wonderful!"}
    ],
    extra_body={"structured_outputs": {"choice": ["positive", "negative"]}},
)
print(completion.choices[0].message.content)
```

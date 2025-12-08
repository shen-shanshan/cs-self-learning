# MMEncoder 抽取

## PR 描述

```
Signed-off-by: shen-shanshan <467638484@qq.com>
Co-authored-by: Isotr0py <mozf@mail2.sysu.edu.cn>
Co-authored-by: tjtanaa <tunjian.tan@embeddedllm.com>
```

## ✅ Ascend A2 NPU

```bash
vllm serve /root/.cache/modelscope/hub/models/Qwen/Qwen2.5-VL-7B-Instruct \
--max_model_len 16384 \
--max-num-batched-tokens 16384 \
--tensor-parallel-size 2 \
--enforce-eager
```

```
{"id":"chatcmpl-b4e3053f30ab2442","object":"chat.completion","created":1764922950,"model":"/root/.cache/modelscope/hub/models/Qwen/Qwen2.5-VL-7B-Instruct","choices":[{"index":0,"message":{"role":"assistant","content":"The text in the image is \"TONGYI Qwen.\" The word \"TONGYI\" is written in blue, and \"Qwen\" is written in gray. The font appears to be modern and clean, with \"TONGYI\" being slightly larger than \"Qwen.\" The design includes a geometric, abstract shape on the left side of the logo, which complements the text.","refusal":null,"annotations":null,"audio":null,"function_call":null,"tool_calls":[],"reasoning":null,"reasoning_content":null},"logprobs":null,"finish_reason":"stop","stop_reason":null,"token_ids":null}],"service_tier":null,"system_fingerprint":null,"usage":{"prompt_tokens":78,"total_tokens":162,"completion_tokens":84,"prompt_tokens_details":null},"prompt_logprobs":null,"prompt_token_ids":null,"kv_transfer_params":null}
```

```bash
vllm serve /root/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct \
--max_model_len 16384 \
--tensor-parallel-size 2 \
--enforce-eager
```

```
{"id":"chatcmpl-97571fbda8267bd1","object":"chat.completion","created":1764923306,"model":"/root/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct","choices":[{"index":0,"message":{"role":"assistant","content":"The text in the illustration is **“TONGYI Qwen”**.\n\n### How it looks:\n- **“TONGYI”** is written in **uppercase letters** in a **bold, modern sans-serif font**, colored **blue**.\n- **“Qwen”** is written in **lowercase letters** in a **slightly thinner, elegant sans-serif font**, colored **dark gray**.\n- The two lines of text are stacked vertically, with “TONG","refusal":null,"annotations":null,"audio":null,"function_call":null,"tool_calls":[],"reasoning":null,"reasoning_content":null},"logprobs":null,"finish_reason":"length","stop_reason":null,"token_ids":null}],"service_tier":null,"system_fingerprint":null,"usage":{"prompt_tokens":112,"total_tokens":212,"completion_tokens":100,"prompt_tokens_details":null},"prompt_logprobs":null,"prompt_token_ids":null,"kv_transfer_params":null}
```

## NVIDIA A100 GPU

---

from vllm.attention.layers.mm_encoder_attention import maybe_get_vit_flash_attn_backend
maybe_get_vit_flash_attn_backend

```bash
vllm serve /root/.cache/modelscope/hub/models/Qwen/Qwen2.5-VL-7B-Instruct \
--max_model_len 16384 \
--max-num-batched-tokens 16384 \
--tensor-parallel-size 2 \
--enforce-eager

{"id":"chatcmpl-b7b4829b15334ca9a28d2a44e6dc8e1b","object":"chat.completion","created":1763719620,"model":"/root/.cache/modelscope/hub/models/Qwen/Qwen2.5-VL-7B-Instruct","choices":[{"index":0,"message":{"role":"assistant","content":"The text in the image appears to be \"TOYOTA\" repeated multiple times in a pattern. The text looks like it is stylized with a geometric, maze-like design, where each letter is composed of interconnected lines forming a complex, intricate pattern. The letters are blue and set against a white background. The style gives the impression of a maze or a puzzle, which could be part of a logo or a decorative element.","refusal":null,"annotations":null,"audio":null,"function_call":null,"tool_calls":[],"reasoning_content":null},"logprobs":null,"finish_reason":"stop","stop_reason":null,"token_ids":null}],"service_tier":null,"system_fingerprint":null,"usage":{"prompt_tokens":78,"total_tokens":165,"completion_tokens":87,"prompt_tokens_details":null},"prompt_logprobs":null,"prompt_token_ids":null,"kv_transfer_params":null}
```

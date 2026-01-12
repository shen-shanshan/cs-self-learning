# Merge Q/K split to simplify AscendApplyRotaryEmb for better performance

Main changes:

- Use upstream util function (`_pre_process()` and `_post_process()`) to reduce redundant codes.
- Merge Q/K split to simplify the logic of calling `torch_npu.npu_rotary_mul()` for better performance (TPOT has been reduced **6.22%**).

✅ Functional test:

Launch the server:

```bash
export VLLM_USE_MODELSCOPE=True
vllm serve /root/.cache/modelscope/hub/models/Qwen/Qwen2.5-VL-7B-Instruct \
--dtype bfloat16 \
--limit-mm-per-prompt '{"image": 1}' \
--max-model-len 16384 \
--max-num-batched-tokens 16384
```

Query the server:

```bash
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

Output:

```
{"id":"chatcmpl-b2911ab6989ef098","object":"chat.completion","created":1768202780,"model":"/root/.cache/modelscope/hub/models/Qwen/Qwen2.5-VL-7B-Instruct","choices":[{"index":0,"message":{"role":"assistant","content":"The text in the illustration is \"TONGYI Qwen.\" The word \"TONGYI\" is written in blue, and \"Qwen\" is written in gray. The text appears to be part of a logo or branding design, with \"TONGYI\" being more prominent and \"Qwen\" being slightly smaller and positioned below it. The font style is modern and clean, with \"TONGYI\" having a slightly bolder appearance compared to \"Qwen.\"","refusal":null,"annotations":null,"audio":null,"function_call":null,"tool_calls":[],"reasoning":null,"reasoning_content":null},"logprobs":null,"finish_reason":"length","stop_reason":null,"token_ids":null}],"service_tier":null,"system_fingerprint":null,"usage":{"prompt_tokens":78,"total_tokens":178,"completion_tokens":100,"prompt_tokens_details":null},"prompt_logprobs":null,"prompt_token_ids":null,"kv_transfer_params":null}
```

✅ Benchmark:

Run:

```bash
export VLLM_USE_MODELSCOPE=False
export HF_ENDPOINT="https://hf-mirror.com"
vllm bench serve \
--model /root/.cache/modelscope/hub/models/Qwen/Qwen2.5-VL-7B-Instruct \
--backend openai-chat \
--endpoint /v1/chat/completions \
--dataset-name hf \
--hf-split train \
--dataset-path lmarena-ai/vision-arena-bench-v0.1 \
--num-prompts 10 \
--no-stream
```

Before this PR:

```
============ Serving Benchmark Result ============
Successful requests:                     10        
Failed requests:                         0         
Benchmark duration (s):                  5.96      
Total input tokens:                      7191      
Total generated tokens:                  996       
Request throughput (req/s):              1.68      
Output token throughput (tok/s):         167.05    
Peak output token throughput (tok/s):    261.00    
Peak concurrent requests:                10.00     
Total token throughput (tok/s):          1373.16   
---------------Time to First Token----------------
Mean TTFT (ms):                          964.43    
Median TTFT (ms):                        858.48    
P99 TTFT (ms):                           1691.45   
-----Time per Output Token (excl. 1st token)------
Mean TPOT (ms):                          63.08     
Median TPOT (ms):                        40.86     
P99 TPOT (ms):                           241.30    
---------------Inter-token Latency----------------
Mean ITL (ms):                           40.16     
Median ITL (ms):                         33.61     
P99 ITL (ms):                            250.30    
==================================================
```

After this PR:

```
============ Serving Benchmark Result ============
Successful requests:                     10        
Failed requests:                         0         
Benchmark duration (s):                  5.71      
Total input tokens:                      7191      
Total generated tokens:                  996       
Request throughput (req/s):              1.75      
Output token throughput (tok/s):         174.45    
Peak output token throughput (tok/s):    279.00    
Peak concurrent requests:                10.00     
Total token throughput (tok/s):          1433.95   
---------------Time to First Token----------------
Mean TTFT (ms):                          992.14    
Median TTFT (ms):                        938.30    
P99 TTFT (ms):                           1728.71   
-----Time per Output Token (excl. 1st token)------
Mean TPOT (ms):                          59.16     
Median TPOT (ms):                        37.65     
P99 TPOT (ms):                           234.89    
---------------Inter-token Latency----------------
Mean ITL (ms):                           36.55     
Median ITL (ms):                         30.73     
P99 ITL (ms):                            170.72    
==================================================
```

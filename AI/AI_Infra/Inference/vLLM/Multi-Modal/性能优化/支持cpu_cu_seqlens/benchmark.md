Before this PR:

```
============ Serving Benchmark Result ============
Successful requests:                     500       
Failed requests:                         0         
Request rate configured (RPS):           10.00     
Benchmark duration (s):                  82.61     
Total input tokens:                      33418     
Total generated tokens:                  61527     
Request throughput (req/s):              6.05      
Output token throughput (tok/s):         744.75    
Peak output token throughput (tok/s):    3000.00   
Peak concurrent requests:                400.00    
Total token throughput (tok/s):          1149.25   
---------------Time to First Token----------------
Mean TTFT (ms):                          9814.74   
Median TTFT (ms):                        5816.90   
P99 TTFT (ms):                           21827.11  
-----Time per Output Token (excl. 1st token)------
Mean TPOT (ms):                          265.95    
Median TPOT (ms):                        277.73    
P99 TPOT (ms):                           508.91    
---------------Inter-token Latency----------------
Mean ITL (ms):                           256.36    
Median ITL (ms):                         94.64     
P99 ITL (ms):                            1934.30   
==================================================
```

After this PR:

```
============ Serving Benchmark Result ============
Successful requests:                     500       
Failed requests:                         0         
Request rate configured (RPS):           10.00     
Benchmark duration (s):                  81.45     
Total input tokens:                      33418     
Total generated tokens:                  61470     
Request throughput (req/s):              6.14      
Output token throughput (tok/s):         754.67    
Peak output token throughput (tok/s):    2877.00   
Peak concurrent requests:                399.00    
Total token throughput (tok/s):          1164.95   
---------------Time to First Token----------------
Mean TTFT (ms):                          9587.50   
Median TTFT (ms):                        5349.06   
P99 TTFT (ms):                           21584.52  
-----Time per Output Token (excl. 1st token)------
Mean TPOT (ms):                          259.08    
Median TPOT (ms):                        276.45    
P99 TPOT (ms):                           602.15    
---------------Inter-token Latency----------------
Mean ITL (ms):                           253.68    
Median ITL (ms):                         97.55     
P99 ITL (ms):                            1882.26   
==================================================
```

TTFT: 2.32% ⬇️
Throughput: 1.37% ⬆️

---

```bash
vllm serve /root/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct \
--max-model-len 65536 \
--max-num-batched-tokens 65536 \
--tensor-parallel-size 2 \
--dtype bfloat16 \
--gpu-memory-utilization 0.8 \
--enforce-eager

curl -X POST http://localhost:8000/v1/chat/completions \
    -H "Content-Type: application/json" \
    -d '{
        "model": "/root/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct",
        "messages": [
            {"role": "system", "content": "You are a helpful assistant."},
            {"role": "user", "content": [
                {"type": "image_url", "image_url": {"url": "https://s41.ax1x.com/2026/02/13/pZqlADU.jpg"}},
                {"type": "text", "text": "Describe this image."}
            ]}
        ],
        "max_tokens": 100
    }'

{"id":"chatcmpl-ac04bebce4d9ce15","object":"chat.completion","created":1773113285,"model":"/root/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct","choices":[{"index":0,"message":{"role":"assistant","content":"The text in the image reads:\n\n**TONGYI Qwen**\n\n- **TONGYI** is written in uppercase letters in a light blue color.\n- **Qwen** is written in lowercase letters in a dark gray color.\n\nThe logo also features a stylized geometric icon to the left of the text, which appears to be composed of interlocking shapes forming a hexagonal or star-like pattern, also in light blue. This is likely the logo for the Tongyi Qwen series","refusal":null,"annotations":null,"audio":null,"function_call":null,"tool_calls":[],"reasoning":null},"logprobs":null,"finish_reason":"length","stop_reason":null,"token_ids":null}],"service_tier":null,"system_fingerprint":null,"usage":{"prompt_tokens":112,"total_tokens":212,"completion_tokens":100,"prompt_tokens_details":null},"prompt_logprobs":null,"prompt_token_ids":null,"kv_transfer_params":null}
```

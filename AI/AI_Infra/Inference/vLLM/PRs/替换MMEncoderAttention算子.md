# Use `npu_fusion_attention()` in AscendMMEncoderAttention for better stability and higher performance

As mentioned in https://github.com/vllm-project/vllm-ascend/issues/2239, `torch_npu._npu_flash_attention()` may lead to OOM issues. Thus, I suppose to use `torch_npu.npu_fusion_attention()` in `AscendMMEncoderAttention` for better stability and higher performance. After testing, TPOT has been reduced by **6.87%** during multi-modal inference.

#### ✅ Functional test

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
{"id":"chatcmpl-9420b1328c344eb8","object":"chat.completion","created":1768207323,"model":"/root/.cache/modelscope/hub/models/Qwen/Qwen2.5-VL-7B-Instruct","choices":[{"index":0,"message":{"role":"assistant","content":"The text in the illustration is \"TONGYI Qwen.\" The word \"TONGYI\" is written in blue, and \"Qwen\" is written in gray. The text appears to be part of a logo or branding design, with \"TONGYI\" being more prominent and \"Qwen\" being slightly smaller and positioned below it. The font style is modern and clean, with \"TONGYI\" having a slightly bolder appearance compared to \"Qwen.\"","refusal":null,"annotations":null,"audio":null,"function_call":null,"tool_calls":[],"reasoning":null,"reasoning_content":null},"logprobs":null,"finish_reason":"length","stop_reason":null,"token_ids":null}],"service_tier":null,"system_fingerprint":null,"usage":{"prompt_tokens":78,"total_tokens":178,"completion_tokens":100,"prompt_tokens_details":null},"prompt_logprobs":null,"prompt_token_ids":null,"kv_transfer_params":null}
```

#### ✅ Benchmark

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
--num-prompts 100 \
--no-stream
```

Before this PR:

```
============ Serving Benchmark Result ============
Successful requests:                     10        
Failed requests:                         0         
Benchmark duration (s):                  5.89      
Total input tokens:                      7191      
Total generated tokens:                  1004      
Request throughput (req/s):              1.70      
Output token throughput (tok/s):         170.33    
Peak output token throughput (tok/s):    274.00    
Peak concurrent requests:                10.00     
Total token throughput (tok/s):          1390.26   
---------------Time to First Token----------------
Mean TTFT (ms):                          966.07    
Median TTFT (ms):                        989.33    
P99 TTFT (ms):                           1669.55   
-----Time per Output Token (excl. 1st token)------
Mean TPOT (ms):                          72.22     
Median TPOT (ms):                        38.97     
P99 TPOT (ms):                           328.81    
---------------Inter-token Latency----------------
Mean ITL (ms):                           39.62     
Median ITL (ms):                         32.77     
P99 ITL (ms):                            276.39    
==================================================
```

After this PR:

```
============ Serving Benchmark Result ============
Successful requests:                     10        
Failed requests:                         0         
Benchmark duration (s):                  5.77      
Total input tokens:                      7191      
Total generated tokens:                  1008      
Request throughput (req/s):              1.73      
Output token throughput (tok/s):         174.58    
Peak output token throughput (tok/s):    274.00    
Peak concurrent requests:                10.00     
Total token throughput (tok/s):          1419.99   
---------------Time to First Token----------------
Mean TTFT (ms):                          1044.03   
Median TTFT (ms):                        1097.02   
P99 TTFT (ms):                           1639.30   
-----Time per Output Token (excl. 1st token)------
Mean TPOT (ms):                          67.26     
Median TPOT (ms):                        36.64     
P99 TPOT (ms):                           301.27    
---------------Inter-token Latency----------------
Mean ITL (ms):                           37.62     
Median ITL (ms):                         32.41     
P99 ITL (ms):                            149.69    
==================================================
```

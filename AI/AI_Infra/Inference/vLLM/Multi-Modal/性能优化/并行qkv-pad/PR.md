# Parallelize qkv padding in AscendMMEncoderAttention for better performance

Currently, we pad the last dim of qkv to 128 before AscendMMEncoderAttention to get better performance on Ascend NPU. However, the qkv padding is executed serially, which may lead to more overhead when launching `aclnnConstantPadNd` (launch 3 times). Since the three operations are mutually independent, we stack qkv first and then pad them in one kernel launch.

With this optimization, TTFT has been reduced by **3.15%**, peak throughput has been increased by **4.20%**.

Before this PR:

After this PR:

---

✅ Benchmark

Launch the server:

```bash
vllm serve /root/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct \
--dtype bfloat16 \
--limit-mm-per-prompt '{"image": 1}' \
--max-model-len 16384 \
--max-num-batched-tokens 16384
```

Run benchmark:

```bash
vllm bench serve \
--model /root/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct \
--backend openai-chat \
--endpoint /v1/chat/completions \
--dataset-name hf \
--hf-split train \
--dataset-path lmarena-ai/vision-arena-bench-v0.1 \
--num-prompts 1000 \
--no-stream
```

Before this PR:

```
============ Serving Benchmark Result ============
Successful requests:                     1000      
Failed requests:                         0         
Benchmark duration (s):                  122.33    
Total input tokens:                      66638     
Total generated tokens:                  122845    
Request throughput (req/s):              8.17      
Output token throughput (tok/s):         1004.18   
Peak output token throughput (tok/s):    3073.00   
Peak concurrent requests:                1000.00   
Total token throughput (tok/s):          1548.90   
---------------Time to First Token----------------
Mean TTFT (ms):                          51757.16  
Median TTFT (ms):                        44853.42  
P99 TTFT (ms):                           110700.14 
-----Time per Output Token (excl. 1st token)------
Mean TPOT (ms):                          226.06    
Median TPOT (ms):                        206.85    
P99 TPOT (ms):                           935.31    
---------------Inter-token Latency----------------
Mean ITL (ms):                           208.82    
Median ITL (ms):                         96.37     
P99 ITL (ms):                            2183.13   
==================================================
```

After this PR:

```
============ Serving Benchmark Result ============
Successful requests:                     1000      
Failed requests:                         0         
Benchmark duration (s):                  121.47    
Total input tokens:                      66638     
Total generated tokens:                  122860    
Request throughput (req/s):              8.23      
Output token throughput (tok/s):         1011.47   
Peak output token throughput (tok/s):    3202.00   
Peak concurrent requests:                1000.00   
Total token throughput (tok/s):          1560.08   
---------------Time to First Token----------------
Mean TTFT (ms):                          50125.08  
Median TTFT (ms):                        46270.85  
P99 TTFT (ms):                           108107.12 
-----Time per Output Token (excl. 1st token)------
Mean TPOT (ms):                          227.11    
Median TPOT (ms):                        205.13    
P99 TPOT (ms):                           816.08    
---------------Inter-token Latency----------------
Mean ITL (ms):                           204.60    
Median ITL (ms):                         92.66     
P99 ITL (ms):                            2219.02   
==================================================
```

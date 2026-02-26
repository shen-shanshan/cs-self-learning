# Use `seq_lens` CPU cache to avoid frequent d2h copy for better performance

Currently, the performance of multi-modal encoding (i.e., `AscendMMEncoderAttention` forward) is considerably bounded by the heavy host pre-process operations.

We can see from the profiling results below, before the real computation of Attention, there are long free time in the device, which will lead to extremely low NPU utilization.

To opitimize this, this PR has proposed four changes:

1. Use `seq_lens` CPU cache to avoid frequent d2h copy. Before this PR, `AscendMMEncoderAttention` will copy the `cu_seqlens` from NPU to CPU in every forward, since the op `_npu_flash_attention_unpad()` requires CPU `cu_seqlens` (otherwise it will crash). Thus, we use `seq_lens_cpu_cache` to cache this tensor, since it's shared between all layers, but may change in different forward step. When current `cu_seqlens` is different from the last one, we update the cache, otherwise we directly use the cache to avoid frequent diff and copy operations, which are costful.
2. Use `out_buffer` to avoid frequent tensor creating and allocation, which will benefit both performance and memory. It will only be updated when the shape of Q changes.
3. Pre-compute the scale value to avoid caculating it in every forward.
4. Move the judgment of `enable_pad` from forward to the `__init__` method.

After these optimization, the **TTFT** has been reduced by **4.62%** and **peak throughput** has increased by **2.14%**.

---

✅ Benchmark

Launch the server:

```bash
vllm serve /root/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct \
--dtype bfloat16 \
--limit-mm-per-prompt '{"image": 1}' \
--max-model-len 16384 \
--max-num-batched-tokens 16384 \
--no-async-scheduling
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
--num-prompts 200 \
--no-stream
```

Before this PR:

```
============ Serving Benchmark Result ============
Successful requests:                     200       
Failed requests:                         0         
Benchmark duration (s):                  36.31     
Total input tokens:                      20026     
Total generated tokens:                  24472     
Request throughput (req/s):              5.51      
Output token throughput (tok/s):         673.99    
Peak output token throughput (tok/s):    2246.00   
Peak concurrent requests:                200.00    
Total token throughput (tok/s):          1225.52   
---------------Time to First Token----------------
Mean TTFT (ms):                          14624.41  
Median TTFT (ms):                        13783.19  
P99 TTFT (ms):                           25442.95  
-----Time per Output Token (excl. 1st token)------
Mean TPOT (ms):                          172.38    
Median TPOT (ms):                        175.88    
P99 TPOT (ms):                           513.32    
---------------Inter-token Latency----------------
Mean ITL (ms):                           164.17    
Median ITL (ms):                         89.34     
P99 ITL (ms):                            1641.10   
==================================================

VLLM_VERSION="0.16.0":

============ Serving Benchmark Result ============
Successful requests:                     500       
Failed requests:                         0         
Request rate configured (RPS):           10.00     
Benchmark duration (s):                  82.23     
Total input tokens:                      33418     
Total generated tokens:                  61543     
Request throughput (req/s):              6.08      
Output token throughput (tok/s):         748.45    
Peak output token throughput (tok/s):    3203.00   
Peak concurrent requests:                402.00    
Total token throughput (tok/s):          1154.86   
---------------Time to First Token----------------
Mean TTFT (ms):                          10275.37  
Median TTFT (ms):                        6297.88   
P99 TTFT (ms):                           22918.26  
-----Time per Output Token (excl. 1st token)------
Mean TPOT (ms):                          263.02    
Median TPOT (ms):                        277.61    
P99 TPOT (ms):                           483.56    
---------------Inter-token Latency----------------
Mean ITL (ms):                           257.31    
Median ITL (ms):                         94.83     
P99 ITL (ms):                            1773.90   
==================================================
```

After this PR:

```
============ Serving Benchmark Result ============
Successful requests:                     200       
Failed requests:                         0         
Benchmark duration (s):                  36.13     
Total input tokens:                      20026     
Total generated tokens:                  24588     
Request throughput (req/s):              5.54      
Output token throughput (tok/s):         680.53    
Peak output token throughput (tok/s):    2294.00   
Peak concurrent requests:                200.00    
Total token throughput (tok/s):          1234.80   
---------------Time to First Token----------------
Mean TTFT (ms):                          13971.41  
Median TTFT (ms):                        13234.78  
P99 TTFT (ms):                           25114.50  
-----Time per Output Token (excl. 1st token)------
Mean TPOT (ms):                          175.35    
Median TPOT (ms):                        169.98    
P99 TPOT (ms):                           554.85    
---------------Inter-token Latency----------------
Mean ITL (ms):                           163.46    
Median ITL (ms):                         86.03     
P99 ITL (ms):                            1590.72   
==================================================

VLLM_VERSION="0.16.0":

============ Serving Benchmark Result ============
Successful requests:                     500       
Failed requests:                         0         
Request rate configured (RPS):           10.00     
Benchmark duration (s):                  81.20     
Total input tokens:                      33418     
Total generated tokens:                  61509     
Request throughput (req/s):              6.16      
Output token throughput (tok/s):         757.54    
Peak output token throughput (tok/s):    2562.00   
Peak concurrent requests:                395.00    
Total token throughput (tok/s):          1169.11   
---------------Time to First Token----------------
Mean TTFT (ms):                          9511.91   
Median TTFT (ms):                        5479.78   
P99 TTFT (ms):                           21427.21  
-----Time per Output Token (excl. 1st token)------
Mean TPOT (ms):                          261.12    
Median TPOT (ms):                        276.03    
P99 TPOT (ms):                           446.99    
---------------Inter-token Latency----------------
Mean ITL (ms):                           254.04    
Median ITL (ms):                         97.71     
P99 ITL (ms):                            1516.67   
==================================================
```

TTFT 7.43%
Peak throughput 11.68%

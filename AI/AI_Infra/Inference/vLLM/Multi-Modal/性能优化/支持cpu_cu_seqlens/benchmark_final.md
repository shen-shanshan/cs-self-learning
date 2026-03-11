请帮我修改 vllm-benchmark-summary 这个 skill，让他能够满足我的输入输出格式。

我的输入格式如下：

Before this PR:

```
============ Serving Benchmark Result ============
Successful requests:                     500       
Failed requests:                         0         
Request rate configured (RPS):           10.00     
Benchmark duration (s):                  78.79     
Total input tokens:                      33418     
Total generated tokens:                  61562     
Request throughput (req/s):              6.35      
Output token throughput (tok/s):         781.30    
Peak output token throughput (tok/s):    2816.00   
Peak concurrent requests:                384.00    
Total token throughput (tok/s):          1205.42   
---------------Time to First Token----------------
Mean TTFT (ms):                          8629.91   
Median TTFT (ms):                        5443.81   
P99 TTFT (ms):                           19204.26  
-----Time per Output Token (excl. 1st token)------
Mean TPOT (ms):                          242.99    
Median TPOT (ms):                        257.68    
P99 TPOT (ms):                           439.13    
---------------Inter-token Latency----------------
Mean ITL (ms):                           240.95    
Median ITL (ms):                         97.49     
P99 ITL (ms):                            1467.92   
==================================================
```

After this PR:

```
============ Serving Benchmark Result ============
Successful requests:                     500       
Failed requests:                         0         
Request rate configured (RPS):           10.00     
Benchmark duration (s):                  78.53     
Total input tokens:                      33418     
Total generated tokens:                  61571     
Request throughput (req/s):              6.37      
Output token throughput (tok/s):         784.04    
Peak output token throughput (tok/s):    2560.00   
Peak concurrent requests:                378.00    
Total token throughput (tok/s):          1209.58   
---------------Time to First Token----------------
Mean TTFT (ms):                          8328.36   
Median TTFT (ms):                        5585.51   
P99 TTFT (ms):                           18813.79  
-----Time per Output Token (excl. 1st token)------
Mean TPOT (ms):                          240.06    
Median TPOT (ms):                        253.22    
P99 TPOT (ms):                           359.47    
---------------Inter-token Latency----------------
Mean ITL (ms):                           239.94    
Median ITL (ms):                         100.19    
P99 ITL (ms):                            1423.15   
==================================================
```

我的输出格式如下：

Comparison 需要精确到小数点后两位，并且根据数据的正负，标出上下箭头

```markdown
# vLLM Benchmark Comparison Report

> Generated: 2026-03-11 17:12
> vLLM Benchmark Comparison

---

## Performance Metrics

| Metric | Before this PR | After this PR | Comparison |
| :----- | :------------- | :------------ | :--------- |
| **Throughput** | | | |
| Request throughput (req/s) | 6.35 | 6.37 | +0.54% ↑ |
| Output token throughput (tok/s) | 781.30 | 784.04 | +0.68% ↑ |
| Total token throughput (tok/s) | 1,205.42 | 1,209.58 | +0.56% ↑ |
| Peak output token throughput (tok/s) | 2,816 | 2,560 | -9.86% ↓ |
| **Latency** | | | |
| Benchmark duration (s) | 78.79 | 78.53 | +0.85% ↑ |
| Mean TTFT (ms) | 8,629.91 | 8,328.36 | -3.85% ↓ |
| Median TTFT (ms) | 5,443.81 | 5,585.51 | +3.45% ↑ |
| P99 TTFT (ms) | 19,204.26 | 18,813.79 | -2.45% ↓ |
| Mean TPOT (ms) | 242.99 | 240.06 | -1.12% ↓ |
| Median TPOT (ms) | 257.68 | 253.22 | -2.78% ↓ |
| P99 TPOT (ms) | 439.13 | 359.47 | -18.45% ↓ |
| Mean ITL (ms) | 240.95 | 239.94 | +0.78% ↑ |
| Median ITL (ms) | 97.49 | 100.19 | +3.96% ↑ |
| P99 ITL (ms) | 1,467.92 | 1,423.15 | -3.78% ↓ |

---

## Summary

- The standout improvement is P99 TPOT **-18.14%** (439ms → 359ms) — tail latency for token generation is significantly better.
- Mean TTFT improved **3.49%** and P99 TTFT improved **2.03%**, meaning the worst-case wait for first token is slightly better.
- Throughput metrics are essentially flat (**~+0.3%**).
- Two minor regressions: Median TTFT **+2.60%** and Median ITL **+2.77%** — small and likely within noise.
- Peak output token throughput dropped **9%** (2,816 → 2,560), but this is a burst/peak metric and may reflect scheduling differences rather than a real regression.

---
```

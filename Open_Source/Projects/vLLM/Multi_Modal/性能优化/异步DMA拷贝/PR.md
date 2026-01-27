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
```

After this PR:

```
```

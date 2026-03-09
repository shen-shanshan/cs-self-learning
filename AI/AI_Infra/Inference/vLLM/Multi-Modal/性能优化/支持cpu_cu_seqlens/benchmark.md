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
Benchmark duration (s):                  81.63     
Total input tokens:                      33418     
Total generated tokens:                  61467     
Request throughput (req/s):              6.13      
Output token throughput (tok/s):         753.01    
Peak output token throughput (tok/s):    2823.00   
Peak concurrent requests:                397.00    
Total token throughput (tok/s):          1162.39   
---------------Time to First Token----------------
Mean TTFT (ms):                          9655.92   
Median TTFT (ms):                        5808.78   
P99 TTFT (ms):                           21696.67  
-----Time per Output Token (excl. 1st token)------
Mean TPOT (ms):                          255.64    
Median TPOT (ms):                        274.45    
P99 TPOT (ms):                           433.03    
---------------Inter-token Latency----------------
Mean ITL (ms):                           255.07    
Median ITL (ms):                         98.74     
P99 ITL (ms):                            1694.67   
==================================================
```

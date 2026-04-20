Before this PR:

```
============ Serving Benchmark Result ============
Successful requests:                     500       
Failed requests:                         0         
Request rate configured (RPS):           10.00     
Benchmark duration (s):                  78.58     
Total input tokens:                      33418     
Total generated tokens:                  61431     
Request throughput (req/s):              6.36      
Output token throughput (tok/s):         781.78    
Peak output token throughput (tok/s):    2475.00   
Peak concurrent requests:                383.00    
Total token throughput (tok/s):          1207.07   
---------------Time to First Token----------------
Mean TTFT (ms):                          7116.24   
Median TTFT (ms):                        4295.84   
P99 TTFT (ms):                           18370.87  
-----Time per Output Token (excl. 1st token)------
Mean TPOT (ms):                          245.78    
Median TPOT (ms):                        264.03    
P99 TPOT (ms):                           334.38    
---------------Inter-token Latency----------------
Mean ITL (ms):                           246.99    
Median ITL (ms):                         117.71    
P99 ITL (ms):                            1327.55   
==================================================
```

After this PR:

```
============ Serving Benchmark Result ============
Successful requests:                     500       
Failed requests:                         0         
Request rate configured (RPS):           10.00     
Benchmark duration (s):                  77.44     
Total input tokens:                      33418     
Total generated tokens:                  61522     
Request throughput (req/s):              6.46      
Output token throughput (tok/s):         794.40    
Peak output token throughput (tok/s):    2691.00   
Peak concurrent requests:                369.00    
Total token throughput (tok/s):          1225.91   
---------------Time to First Token----------------
Mean TTFT (ms):                          6888.64   
Median TTFT (ms):                        4128.82   
P99 TTFT (ms):                           17487.94  
-----Time per Output Token (excl. 1st token)------
Mean TPOT (ms):                          240.14    
Median TPOT (ms):                        259.18    
P99 TPOT (ms):                           313.15    
---------------Inter-token Latency----------------
Mean ITL (ms):                           241.84    
Median ITL (ms):                         121.08    
P99 ITL (ms):                            1470.33   
==================================================
```

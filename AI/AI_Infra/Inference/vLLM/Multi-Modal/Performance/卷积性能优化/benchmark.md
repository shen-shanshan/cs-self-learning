# Benchmark

## Conv img2col

conv:

```
============ Serving Benchmark Result ============
Successful requests:                     500       
Failed requests:                         0         
Request rate configured (RPS):           10.00     
Benchmark duration (s):                  81.05     
Total input tokens:                      33418     
Total generated tokens:                  61403     
Request throughput (req/s):              6.17      
Output token throughput (tok/s):         757.57    
Peak output token throughput (tok/s):    2816.00   
Peak concurrent requests:                400.00    
Total token throughput (tok/s):          1169.87   
---------------Time to First Token----------------
Mean TTFT (ms):                          9546.96   
Median TTFT (ms):                        5090.59   
P99 TTFT (ms):                           21744.79  
-----Time per Output Token (excl. 1st token)------
Mean TPOT (ms):                          260.71    
Median TPOT (ms):                        279.57    
P99 TPOT (ms):                           602.40    
---------------Inter-token Latency----------------
Mean ITL (ms):                           254.55    
Median ITL (ms):                         96.60     
P99 ITL (ms):                            2164.31   
==================================================

Ascend CustomOp:
============ Serving Benchmark Result ============
Successful requests:                     500       
Failed requests:                         0         
Request rate configured (RPS):           10.00     
Benchmark duration (s):                  81.37     
Total input tokens:                      33418     
Total generated tokens:                  61492     
Request throughput (req/s):              6.14      
Output token throughput (tok/s):         755.69    
Peak output token throughput (tok/s):    2945.00   
Peak concurrent requests:                408.00    
Total token throughput (tok/s):          1166.37   
---------------Time to First Token----------------
Mean TTFT (ms):                          9594.02   
Median TTFT (ms):                        5216.01   
P99 TTFT (ms):                           21642.81  
-----Time per Output Token (excl. 1st token)------
Mean TPOT (ms):                          266.32    
Median TPOT (ms):                        277.68    
P99 TPOT (ms):                           668.50    
---------------Inter-token Latency----------------
Mean ITL (ms):                           253.41    
Median ITL (ms):                         95.96     
P99 ITL (ms):                            1661.24   
==================================================
```

linear:

```
============ Serving Benchmark Result ============
Successful requests:                     500       
Failed requests:                         0         
Request rate configured (RPS):           10.00     
Benchmark duration (s):                  81.90     
Total input tokens:                      33418     
Total generated tokens:                  61545     
Request throughput (req/s):              6.11      
Output token throughput (tok/s):         751.48    
Peak output token throughput (tok/s):    2689.00   
Peak concurrent requests:                397.00    
Total token throughput (tok/s):          1159.52   
---------------Time to First Token----------------
Mean TTFT (ms):                          9641.77   
Median TTFT (ms):                        5379.53   
P99 TTFT (ms):                           21427.09  
-----Time per Output Token (excl. 1st token)------
Mean TPOT (ms):                          259.78    
Median TPOT (ms):                        276.78    
P99 TPOT (ms):                           505.88    
---------------Inter-token Latency----------------
Mean ITL (ms):                           254.84    
Median ITL (ms):                         98.29     
P99 ITL (ms):                            1485.40   
==================================================

Ascend CustomOp:
============ Serving Benchmark Result ============
Successful requests:                     500       
Failed requests:                         0         
Request rate configured (RPS):           10.00     
Benchmark duration (s):                  82.27     
Total input tokens:                      33418     
Total generated tokens:                  61483     
Request throughput (req/s):              6.08      
Output token throughput (tok/s):         747.35    
Peak output token throughput (tok/s):    3077.00   
Peak concurrent requests:                400.00    
Total token throughput (tok/s):          1153.57   
---------------Time to First Token----------------
Mean TTFT (ms):                          9966.16   
Median TTFT (ms):                        6085.19   
P99 TTFT (ms):                           22112.22  
-----Time per Output Token (excl. 1st token)------
Mean TPOT (ms):                          256.71    
Median TPOT (ms):                        276.71    
P99 TPOT (ms):                           416.92    
---------------Inter-token Latency----------------
Mean ITL (ms):                           256.67    
Median ITL (ms):                         99.44     
P99 ITL (ms):                            1928.00   
==================================================
```

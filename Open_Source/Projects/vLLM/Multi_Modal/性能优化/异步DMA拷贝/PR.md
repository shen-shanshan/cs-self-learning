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
============ Serving Benchmark Result ============
Successful requests:                     200       
Failed requests:                         0         
Benchmark duration (s):                  53.54     
Total input tokens:                      20026     
Total generated tokens:                  24500     
Request throughput (req/s):              3.74      
Output token throughput (tok/s):         457.63    
Peak output token throughput (tok/s):    972.00    
Peak concurrent requests:                200.00    
Total token throughput (tok/s):          831.69    
---------------Time to First Token----------------
Mean TTFT (ms):                          15159.42  
Median TTFT (ms):                        15046.56  
P99 TTFT (ms):                           25905.49  
-----Time per Output Token (excl. 1st token)------
Mean TPOT (ms):                          308.70    
Median TPOT (ms):                        297.39    
P99 TPOT (ms):                           928.36    
---------------Inter-token Latency----------------
Mean ITL (ms):                           291.26    
Median ITL (ms):                         230.51    
P99 ITL (ms):                            2111.61   
==================================================
```

直接改上游模型：

```
============ Serving Benchmark Result ============
Successful requests:                     200       
Failed requests:                         0         
Benchmark duration (s):                  35.97     
Total input tokens:                      20026     
Total generated tokens:                  24549     
Request throughput (req/s):              5.56      
Output token throughput (tok/s):         682.52    
Peak output token throughput (tok/s):    2340.00   
Peak concurrent requests:                200.00    
Total token throughput (tok/s):          1239.29   
---------------Time to First Token----------------
Mean TTFT (ms):                          13738.43  
Median TTFT (ms):                        13130.31  
P99 TTFT (ms):                           25287.40  
-----Time per Output Token (excl. 1st token)------
Mean TPOT (ms):                          175.02    
Median TPOT (ms):                        172.94    
P99 TPOT (ms):                           458.64    
---------------Inter-token Latency----------------
Mean ITL (ms):                           165.90    
Median ITL (ms):                         86.72     
P99 ITL (ms):                            1498.89   
==================================================
```

with patch:

```
============ Serving Benchmark Result ============
Successful requests:                     500       
Failed requests:                         0         
Request rate configured (RPS):           10.00     
Benchmark duration (s):                  81.22     
Total input tokens:                      33418     
Total generated tokens:                  61521     
Request throughput (req/s):              6.16      
Output token throughput (tok/s):         757.50    
Peak output token throughput (tok/s):    2679.00   
Peak concurrent requests:                395.00    
Total token throughput (tok/s):          1168.97   
---------------Time to First Token----------------
Mean TTFT (ms):                          9462.07   
Median TTFT (ms):                        5341.99   
P99 TTFT (ms):                           21237.95  
-----Time per Output Token (excl. 1st token)------
Mean TPOT (ms):                          263.86    
Median TPOT (ms):                        274.07    
P99 TPOT (ms):                           568.52    
---------------Inter-token Latency----------------
Mean ITL (ms):                           253.45    
Median ITL (ms):                         97.23     
P99 ITL (ms):                            1844.25   
==================================================
```

without patch:

```
============ Serving Benchmark Result ============
Successful requests:                     500       
Failed requests:                         0         
Request rate configured (RPS):           10.00     
Benchmark duration (s):                  163.48    
Total input tokens:                      33418     
Total generated tokens:                  61685     
Request throughput (req/s):              3.06      
Output token throughput (tok/s):         377.33    
Peak output token throughput (tok/s):    2587.00   
Peak concurrent requests:                498.00    
Total token throughput (tok/s):          581.75    
---------------Time to First Token----------------
Mean TTFT (ms):                          52111.56  
Median TTFT (ms):                        45768.77  
P99 TTFT (ms):                           102667.98 
-----Time per Output Token (excl. 1st token)------
Mean TPOT (ms):                          546.38    
Median TPOT (ms):                        568.12    
P99 TPOT (ms):                           2356.59   
---------------Inter-token Latency----------------
Mean ITL (ms):                           478.77    
Median ITL (ms):                         101.00    
P99 ITL (ms):                            6584.86   
==================================================
```

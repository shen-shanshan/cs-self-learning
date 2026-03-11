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

开启异步调度 + 新版本：
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
Benchmark duration (s):                  81.45     
Total input tokens:                      33418     
Total generated tokens:                  61470     
Request throughput (req/s):              6.14      
Output token throughput (tok/s):         754.67    
Peak output token throughput (tok/s):    2877.00   
Peak concurrent requests:                399.00    
Total token throughput (tok/s):          1164.95   
---------------Time to First Token----------------
Mean TTFT (ms):                          9587.50   
Median TTFT (ms):                        5349.06   
P99 TTFT (ms):                           21584.52  
-----Time per Output Token (excl. 1st token)------
Mean TPOT (ms):                          259.08    
Median TPOT (ms):                        276.45    
P99 TPOT (ms):                           602.15    
---------------Inter-token Latency----------------
Mean ITL (ms):                           253.68    
Median ITL (ms):                         97.55     
P99 ITL (ms):                            1882.26   
==================================================

开启异步调度 + 新版本：
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

---

Skill

请帮我创建一个skill，名字叫做 benchmark-summary，用于对比做了某个改动后，vllm前后的性能差异。
1.需要对比每项数据前后的差异，比如：TTFT下降了xx.xx%，并生成一个markdown表格到当前目录的results目录下；
2.生成的markdown文件中，需要有一些关键变化特征的总结；
3.生成的报告全部使用英文，不要有中文。

| Metric | Before this PR | After this PR | Comparison |
| :----- | :------------- | :------------ | :--------- |
| **Throughput** | | | |
| Request throughput (req/s)|1.79|2.63|+47%|
| Output token throughput (tok/s)|206.66|302.89|+47%|
| Total token throughput (tok/s)|375.53|551.31|+47%|
| **Latency** | | | |
| Benchmark duration (s)|558.57|379.70|-32%|
| Mean TTFT (ms)|4,584.50|2,062.35|-55%|
| Median TTFT (ms)|2,280.17|1,513.51|-34%|
| P99 TTFT (ms)|25,097.62|10,313.05|-59%|
| Mean TPOT (ms)|285.83|198.03|-31%|
| Median TPOT (ms)|283.86|203.93|-28%|
| P99 TPOT (ms)|515.32|274.71|-47%|
| Mean ITL (ms)|337.49|249.15|-26%|
| Median ITL (ms)|33.11|131.26|+296%|
| P99 ITL (ms)|3,699.28|1,291.46|-65%|

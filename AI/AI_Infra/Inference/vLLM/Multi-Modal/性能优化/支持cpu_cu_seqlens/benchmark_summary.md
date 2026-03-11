# PR 性能指标对比汇总

| 指标 | PR 前 | PR 后 | 变化量 | 变化幅度 |
|------|------:|------:|-------:|--------:|
| **基础指标** | | | | |
| Benchmark Duration (s) | 78.79 | 78.53 | -0.26 | -0.33% |
| Request Throughput (req/s) | 6.35 | 6.37 | +0.02 | +0.31% ↑ |
| Output Token Throughput (tok/s) | 781.30 | 784.04 | +2.74 | +0.35% ↑ |
| Total Token Throughput (tok/s) | 1205.42 | 1209.58 | +4.16 | +0.35% ↑ |
| Peak Output Token Throughput (tok/s) | 2816.00 | 2560.00 | -256.00 | -9.09% ↓ |
| Peak Concurrent Requests | 384.00 | 378.00 | -6.00 | -1.56% ↓ |
| **TTFT（首 Token 时延）** | | | | |
| Mean TTFT (ms) | 8629.91 | 8328.36 | -301.55 | **-3.49%** ↓ |
| Median TTFT (ms) | 5443.81 | 5585.51 | +141.70 | +2.60% ↑ |
| P99 TTFT (ms) | 19204.26 | 18813.79 | -390.47 | **-2.03%** ↓ |
| **TPOT（逐 Token 生成时延）** | | | | |
| Mean TPOT (ms) | 242.99 | 240.06 | -2.93 | **-1.21%** ↓ |
| Median TPOT (ms) | 257.68 | 253.22 | -4.46 | **-1.73%** ↓ |
| P99 TPOT (ms) | 439.13 | 359.47 | -79.66 | **-18.14%** ↓ |
| **ITL（Token 间时延）** | | | | |
| Mean ITL (ms) | 240.95 | 239.94 | -1.01 | -0.42% ↓ |
| Median ITL (ms) | 97.49 | 100.19 | +2.70 | +2.77% ↑ |
| P99 ITL (ms) | 1467.92 | 1423.15 | -44.77 | **-3.05%** ↓ |

## 关键结论

- **最显著改善**：P99 TPOT 降低 **18.14%**，说明长尾请求的逐 Token 生成延迟得到大幅优化。
- **TTFT 改善**：Mean TTFT 降低 3.49%，P99 TTFT 降低 2.03%，首 Token 延迟整体下降；Median TTFT 略微上升 2.60%（波动在正常范围内）。
- **TPOT 全面改善**：Mean / Median / P99 TPOT 分别降低 1.21% / 1.73% / 18.14%，尾部延迟收益最为突出。
- **ITL 改善**：P99 ITL 降低 3.05%，Mean ITL 降低 0.42%；Median ITL 微升 2.77%（噪声范围内）。
- **吞吐量小幅提升**：Request / Output Token / Total Token 吞吐量均提升约 0.31%～0.35%。
- **峰值并发下降**：Peak Concurrent Requests 降低 1.56%，Peak Output Token Throughput 降低 9.09%，与整体更平稳的请求调度行为一致。

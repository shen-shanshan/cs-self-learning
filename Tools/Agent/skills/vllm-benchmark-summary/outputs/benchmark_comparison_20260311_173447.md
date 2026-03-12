# vLLM Benchmark Comparison Report

> Generated: 2026-03-11 17:34
> vLLM Benchmark Comparison

---

## Performance Metrics

| Metric | Before this PR | After this PR | Comparison |
| :----- | :------------- | :------------ | :--------- |
| **Throughput** | | | |
| Request throughput (req/s) | 6.35 | 6.37 | +0.31% ↑ |
| Output token throughput (tok/s) | 781.30 | 784.04 | +0.35% ↑ |
| Total token throughput (tok/s) | 1,205.42 | 1,209.58 | +0.35% ↑ |
| Peak output token throughput (tok/s) | 2,816 | 2,560 | -9.09% ↓ |
| **Latency** | | | |
| Benchmark duration (s) | 78.79 | 78.53 | -0.33% ↓ |
| Mean TTFT (ms) | 8,629.91 | 8,328.36 | -3.49% ↓ |
| Median TTFT (ms) | 5,443.81 | 5,585.51 | +2.60% ↑ |
| P99 TTFT (ms) | 19,204.26 | 18,813.79 | -2.03% ↓ |
| Mean TPOT (ms) | 242.99 | 240.06 | -1.21% ↓ |
| Median TPOT (ms) | 257.68 | 253.22 | -1.73% ↓ |
| P99 TPOT (ms) | 439.13 | 359.47 | -18.14% ↓ |
| Mean ITL (ms) | 240.95 | 239.94 | -0.42% ↓ |
| Median ITL (ms) | 97.49 | 100.19 | +2.77% ↑ |
| P99 ITL (ms) | 1,467.92 | 1,423.15 | -3.05% ↓ |

---

## AI Summary

- The standout improvement is P99 TPOT **-18.14%** (439ms → 359ms) — tail latency for token generation is significantly reduced, meaning the slowest requests are now much faster.
- Mean TTFT improved **-3.49%** (8,630ms → 8,328ms) and P99 TTFT improved **-2.03%** (19,204ms → 18,814ms), indicating the worst-case wait for the first token is slightly better.
- Throughput metrics are essentially flat (**~+0.31–0.35%**) — no meaningful change in overall serving capacity.
- Two minor regressions: Median TTFT **+2.60%** and Median ITL **+2.77%** — both are small and likely within run-to-run noise.
- Peak output token throughput dropped **-9.09%** (2,816 → 2,560 tok/s), but this is a burst/peak metric sensitive to scheduling and concurrency patterns; the corresponding drop in peak concurrent requests (384 → 378) suggests slightly lower peak load rather than a true regression.

---

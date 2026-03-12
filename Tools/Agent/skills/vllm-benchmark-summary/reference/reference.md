# vLLM Benchmark Comparison Report

> Generated: 2026-03-11 16:41
> 支持 cpu_cu_seqlens 性能优化

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

## AI Summary

- **P99 TPOT improved significantly (-18%):** the most notable improvement, with tail latency clearly reduced.
- **Slight regressions:** Median TTFT (+3%) and Median ITL (+3%) increased slightly, but the magnitude is within the noise range.
- **Throughput remains essentially unchanged:** overall throughput shows no noticeable change (<1%).
- **Peak concurrency decreased slightly:** 384 → 378, with peak output throughput correspondingly dropping from 2816 → 2560 (-9%).

---

# 多模态预处理 CPU 并行度控制

## Background

- 多实例导致的cpu占用过高 [#29078](https://github.com/vllm-project/vllm/issues/29078)
- vLLM 3x slower than SGLang serving Qwen3-VL [#29869](https://github.com/vllm-project/vllm/issues/29869)

- [How to set OMP_NUM_THREADS for distruted training?](https://discuss.pytorch.org/t/how-to-set-omp-num-threads-for-distruted-training/143234)

PR:

- Set default torch num threads for input processing [#31879](https://github.com/vllm-project/vllm/pull/31879)

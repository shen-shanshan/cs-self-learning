# TODO

- [ ] 基于 V1 目录打通 NPUWorker、ModelRunner 等组件，完成模型加载：
  - [x] 实现 V1 Model Runner 中的 `run_profile()` 函数；
- [ ] Guided Decoding 特性支持：
  - [ ] 解决 Reasoning Model 使用 `outlines` 后端生成结果为 `None` 的问题；

## Bugs

fused_moe 问题，给 vllm 提 PR
[details](https://github.com/vllm-project/vllm-ascend/pull/164)
contribute to vllm to make the fused_moe code compatible with non-triton supported hardware.
vllm.model_executor.layers.fused_moe.fused_moe

给 vllm 提 PR:
https://github.com/vllm-project/vllm-ascend/pull/236/files

V1 EP DP ATTENTION：
PR：13931

# TODO

## vllm Tasks

- [ATTN-FFN Disaggregation for MoE Models](https://github.com/vllm-project/vllm/issues/22799)
- 弹性/容错 AFD

## vllm-ascend Tasks

- Torchair 代码重构
- Structured Output 特性适配：重构 apply_grammar_bitmask() 方法

## 源码阅读

- [ ] vllm.distributed (learn `torch.distributed` first)
- [ ] Data Parallel Attention and Expert Parallel MoEs [#16037](https://github.com/vllm-project/vllm/issues/16037)
- [x] EPLB [#18343](https://github.com/vllm-project/vllm/pull/18343)
- [ ] FusedMoE
- [ ] External DPLB [#19790](https://github.com/vllm-project/vllm/pull/19790)
- [ ] Internal DPLB [#21238](https://github.com/vllm-project/vllm/pull/21238)
- [ ] vllm/v1/engine/coordinator.py (DPCoordinator)
- [ ] vllm/v1/engine/core_client.py/self.resources.engine_manager (CoreEngineActorManager for RayDPClient)
- [ ] DPEngineCoreActor
- [ ] PD 分离
- [ ] forward_context

## 技术博客

- [ ] vLLM 分离式架构 - 从 PD 分离到 AF 分离
- [ ] vLLM 推理可靠性 - 弹性伸缩与容错
- [ ] EPLB/DPLB
- [ ] ModelRunner Forward
- [x] V1 整体流程
- [x] Guided Decoding V0/V1

# Elastic-AFD Todo List

## Basic

- [ ] [一文深度全面解析大模型分布式并行策略：DP/TP/PP/CP/EP/SP](https://zhuanlan.zhihu.com/p/1937826285264011929)
- [ ] [一文详解 MoE 模型](https://zhuanlan.zhihu.com/p/1940373778589802523)
- [ ] EP/ETP/EPLB
- [ ] torch.distributed, ProcessGroup
- [ ] Cudagraphs and torch.compile
- [ ] physical_experts/logical_experts
- [ ] python: async/await/future

## Paper

- [x] 看 Step3 AFD 论文
- [ ] 看其它 AFD 论文
- [ ] 看 PD 分离论文
- [ ] 看 serverlessLLM 论文

## vLLM

- [x] 看 Issue：ATTN-FFN Disaggregation for MoE Models - [#22799](https://github.com/vllm-project/vllm/issues/22799)
- [x] 看 Issue：Elastic Expert Parallelism - [#20323](https://github.com/vllm-project/vllm/issues/20323)
- [ ] 看 PR：Elastic Expert Parallel Initial Support - [#20775](https://github.com/vllm-project/vllm/pull/20775)
- [ ] 看 PR 学习 vllm 中分布式并行的代码 - [#16037](https://github.com/vllm-project/vllm/issues/16037)
- [ ] 看代码：vllm/v1/engine/coordinator.py
- [ ] 看代码：vllm/v1/engine/core_client.py/self.resources.engine_manager (CoreEngineActorManager for RayDPClient)
- [ ] 看代码：DPEngineCoreActor
- [ ] 看代码：PD 分离代码实现
- [ ] 适配 Elastic Expert Parallelism 到 vllm-ascend

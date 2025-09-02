# Elastic-AFD Todo List

## Basic

- [ ] [一文深度全面解析大模型分布式并行策略：DP/TP/PP/CP/EP/SP](https://zhuanlan.zhihu.com/p/1937826285264011929)
- [ ] [一文详解 MoE 模型](https://zhuanlan.zhihu.com/p/1940373778589802523)
- [ ] EP/ETP/EPLB
- [ ] torch.distributed, ProcessGroup
- [ ] Cudagraphs and torch.compile
- [ ] python: async/await/future
- [ ] copy.deepcopy
- [ ] Ray 原理与使用
- [ ] 看视频：DP、MoE 分享视频

## Paper

- [x] 看 Step3 AFD 论文
- [ ] 看其它 AFD 论文
- [ ] 看 PD 分离论文
- [ ] 看 serverlessLLM 论文

## vLLM

- [x] 看 Issue：ATTN-FFN Disaggregation for MoE Models - [#22799](https://github.com/vllm-project/vllm/issues/22799)
- [x] 看 Issue：Elastic Expert Parallelism - [#20323](https://github.com/vllm-project/vllm/issues/20323)
- [x] 看 PR：Elastic Expert Parallel Initial Support - [#20775](https://github.com/vllm-project/vllm/pull/20775)
- [ ] 开发：适配 Elastic Expert Parallelism 到 vllm-ascend
- [ ] 开发：将 EEP 与 worker 和 model_runner 解耦

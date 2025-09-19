# Elastic-AFD Todo List

## Basic

python:

- [ ] python: async/await/future
- [ ] copy.deepcopy

torch:

- [x] [torch.distributed](https://docs.pytorch.org/docs/stable/distributed.html)
- [x] [PyTorch Distributed Overview](https://docs.pytorch.org/tutorials/beginner/dist_overview.html)
- [x] [Writing Distributed Applications with PyTorch](https://docs.pytorch.org/tutorials/intermediate/dist_tuto.html)
- [x] [torchrun (Elastic Launch)](https://docs.pytorch.org/docs/stable/elastic/run.html)
- [x] [Torch Distributed Elastic](https://docs.pytorch.org/docs/stable/distributed.elastic.html)
- [ ] Cudagraphs and `torch.compile`

分布式并行：

- [x] EP/ETP/EPLB
- [x] Ray
- [x] AFD 设计文档

## Paper

- [x] 看 Step3 AFD 论文
- [x] 看 serverlessLLM 论文
- [ ] 看 serverlessLLM 源码 - [GitHub](https://github.com/ServerlessLLM/ServerlessLLM)

## vLLM

- [x] 看 Issue：ATTN-FFN Disaggregation for MoE Models - [#22799](https://github.com/vllm-project/vllm/issues/22799)
- [x] 看 Issue：Elastic Expert Parallelism - [#20323](https://github.com/vllm-project/vllm/issues/20323)
- [x] 看 PR：Elastic Expert Parallel Initial Support - [#20775](https://github.com/vllm-project/vllm/pull/20775)
- [ ] 看 PR：draft AFD implementation for step3 [#25162](https://github.com/vllm-project/vllm/pull/25162)

## Idea

- [ ] [ALaaS](https://github.com/HuaizhengZhang/Active-Learning-as-a-Service)

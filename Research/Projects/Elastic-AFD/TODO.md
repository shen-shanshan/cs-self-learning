# Elastic-AFD Todo List

## Basic

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
- [ ] 看 StepMesh 源码 - [GitHub](https://github.com/stepfun-ai/StepMesh)
  - [x] [中文介绍](https://github.com/stepfun-ai/StepMesh/blob/main/Introduction_cn.md)
  - [ ] GPU Direct RDMA 技术

## vLLM

- [x] 看 Issue：ATTN-FFN Disaggregation for MoE Models - [#22799](https://github.com/vllm-project/vllm/issues/22799)
- [x] 看 Issue：Elastic Expert Parallelism - [#20323](https://github.com/vllm-project/vllm/issues/20323)
- [x] 看 PR：Elastic Expert Parallel Initial Support - [#20775](https://github.com/vllm-project/vllm/pull/20775)
- [ ] 看 PR：draft AFD implementation for step3 [#25162](https://github.com/vllm-project/vllm/pull/25162)

## Idea

- [ ] [ALaaS](https://github.com/HuaizhengZhang/Active-Learning-as-a-Service)
- [ ] copy `~/.cache/vllm/torch_compile_cache` - [doc](https://docs.vllm.ai/en/latest/design/torch_compile.html#compilation-cache)

## Paper List

- [ ] [大模型推理加速技术的学习路线是什么?](https://www.zhihu.com/question/591646269/answer/93480081073)
- [ ] [请问一下，将来如果打算做AI infra的工作需要在读博前学习什么呢，以及这个方向的前景如何？](https://www.zhihu.com/question/3627356519/answer/1954947783241994585)

## Notes

弹性扩缩容：

- **Attention** 模块可根据 **Seq Length** 动态调整规模；
- **FFN** 模块则依据 **Batch Size** 实现弹性扩缩容。

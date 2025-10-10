# Elastic-AFD Research Notes

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

- [ ] [大模型推理加速技术的学习路线是什么?](https://www.zhihu.com/question/591646269/answer/93480081073)
- [ ] [请问一下，将来如果打算做AI infra的工作需要在读博前学习什么呢，以及这个方向的前景如何？](https://www.zhihu.com/question/3627356519/answer/1954947783241994585)
- [ ] [ALaaS](https://github.com/HuaizhengZhang/Active-Learning-as-a-Service)

## Notes

**状态：**

- **Stateful Attention Layers:** These layers maintain the Key-Value (KV) cache, which stores the **contextual state** for each individual generation sequence. This component is inherently stateful and session-specific.
- **Stateless MoE Layers:** The expert networks within MoE layers perform a pure, idempotent function. They take a token’s hidden state as input and produce an output, without retaining any memory or state from previous tokens in the same sequence.

**弹性：**

- **Attention 模块**：根据 **Seq Length** 动态伸缩；
- **FFN 模块**：根据 **Batch Size** 动态伸缩。

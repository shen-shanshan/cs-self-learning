# AI 训练框架

## 分布式训练

分布式训练框架：

- Deepspeed；
- Megatron-LM。

> 目前 LLM 训练有两大主流框架：Deepspeed 和 Megatron-LM。前者的主要提出和维护者是微软的工程师，后者是英伟达的工程师。两个框架从底层原理到设计语言可以说是大相径庭。训练框架的主要目标有二：一是在有限的GPU中尽可能地塞入一个大号模型，二是高效地利用多 GPU 进行训练。完成第一个目标主要依赖的是模型切分，或者更笼统地说是降低单卡显存占用。完成第二个目标依赖的是异步、高重叠度、高带宽的数据通信。
>
> Deepspeed 在降低显存这方面应用的技术主要有 Zero-1/2/3、序列并行、CPU Offload，高效通信方面主要是依赖 register_hook 回调函数的异步通信、多 cuda 事件流、GPU 计算重叠、连续锁页缓存。Megatron 在降低显存方面的主要技术有 Distributed Optimizer、Tensor Model Parallel、Pipeline Model Parallel、序列并行，在高效通信方面主要的技术有 P2P 通信、重叠流水线并行、梯度缓存。
>
> 在设计语言上，DeepSpeed 属于外挂框架，框架并不介入模型前向的计算图，因此对模型结构一般没有特殊要求，核心代码通过大量回调函数和 torch 派生类封装，并不暴露给用户。Megatron 则是属于内嵌框架，直接改变模型计算图，因此限制模型结构必须是类 Transformer 的结构，代码全部暴露在外，不怎么依赖回调函数。外挂框架的好处是兼容性好，对新手友好，缺点是启动慢、计算速度略低，适合数据量不大、不关注训练加速技术，专注于模型和数据迭代的人。内嵌框架的优点是启动、训练效率高一点，对于想魔改底层的人更友好，但是对于想轻微修改训练逻辑的不太友好，适合大规模训练和想要了解、改动并行训练代码的人。

**参考资料：**

- [<u>LLM 实践--支线：分布式训练框架的编程基础</u>](https://zhuanlan.zhihu.com/p/10091011992)
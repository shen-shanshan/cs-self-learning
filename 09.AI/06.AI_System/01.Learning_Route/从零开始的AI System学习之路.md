# 从零开始的 AI System 学习之路

## 概述

AI System（AI 系统）是指为上层的 AI 算法应用提供支持的 AI 全栈底层技术，通过合理利用计算机体系结构，可以实现 AI 计算的加速和部署。

AI System 主要包括以下内容：

- AI 训练框架 & 推理引擎；

- AI 编译 & 计算架构；
- AI 硬件 & 体系结构。

![image-20240825162712686](images/image-20240825162712686.png)

> 参考资料：
>
> - [AI System (chenzomi12.github.io)](https://chenzomi12.github.io/)；
> - [GitHub - chenzomi12/AISystem: AISystem](https://github.com/chenzomi12/AISystem)。

## AI 算法应用

### 机器学习

……

### 深度学习

- 深度学习入门笔记：[GitHub - shen-shanshan/cs-self-learning/09.AI/05.Deep_Learning/PyTorch/PyTorch_Tutorials/01.Notes](https://github.com/shen-shanshan/cs-self-learning/tree/master/09.AI/05.Deep_Learning/PyTorch/PyTorch_Tutorials/01.Notes)。

#### 数据预处理

- One-Hot 编码：[机器学习：数据预处理之独热编码（One-Hot）详解-CSDN博客](https://blog.csdn.net/zyc88888/article/details/103819604)。

#### 激活函数

- 基础知识：[机器学习中的数学——激活函数：基础知识_神经网络中激活函数的数学基础-CSDN博客](https://blog.csdn.net/hy592070616/article/details/120616475)；
- Sigmoid 函数：[机器学习中的数学——激活函数（一）：Sigmoid函数-CSDN博客](https://blog.csdn.net/hy592070616/article/details/120617176)；
- ReLU 函数：[机器学习中的数学——激活函数（三）： 线性整流函数（ReLU函数）-CSDN博客](https://blog.csdn.net/hy592070616/article/details/120617706)；
- Softmax 函数：[机器学习中的数学——激活函数（七）：Softmax函数_softmax激活函数的公式-CSDN博客](https://blog.csdn.net/hy592070616/article/details/120618490)。

### LLM

#### 基本概念

**token**：文本中最小的语义单元，如：单词、符号等（tokenization：分词）。

**编码（encoding）**：将子词序列转换为数值向量。

**解码（decoding）**：将每个数值编码替换成其对应的子词，然后将相邻的子词合并成最长的匹配单词，从而得到一个文本。

**嵌入（embedding）**：子词 -> 特征向量，表示该子词的语义。基于互联网上大量的文本资料，统计出两个词语在相邻/句子/文章中共同出现的概率并通过权重来汇总计算，就能分析出某个词语与另外一个词语的亲密度的数值，并将这个数值作为特征向量来描述这个词语。通过嵌入，我们就可以把每个子词看作是高维空间中的一个点，而这些点之间的距离和方向，就可以表示出子词之间的相似度和差异度（词义相似时，在空间上也相近）。

**预测（prediction）**：根据给定的文本，计算出下一个子词出现的概率。下一个子词出现概率的计算，就是基于特征向量表进行的。

> 小结：通过嵌入和预测，我们就可以实现从文本到数字，或者从数字到文本的转换。

**训练 & 推理**：

- [科普 | 深度学习训练和推理有何不同？ - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/66705645)；
- [训练（training）和推理\推断（inference）的关系？_模型训练和推理的区别-CSDN博客](https://blog.csdn.net/weixin_43135178/article/details/117885165)。

**生成**：指根据给定的文本来生成新的文本的过程。生成可以分为两种模式：自回归（autoregressive）和自编码（autoencoding），GPT 系列主要采用了自回归模式。

> 参考资料：
>
> - [关于深度学习和大模型的基础认知 (huawei.com)](https://xinsheng.huawei.com/next/#/detail?uuid=977766950833111040)；
> - [科普神文，一次性讲透AI大模型的核心概念 (qq.com)](https://mp.weixin.qq.com/s?__biz=Mzg2ODA5NTM1OA==&mid=2247484300&idx=1&sn=fe457ff384f4da83fa212fdf7b9f0c02&chksm=ceb0c79df9c74e8b39f80f4ed2fcfdeec743b258ec11343734896182bbbaae82c8425f7a7588&token=377673956&lang=zh_CN#rd)；
> - [解读AI大模型，从了解token开始 - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/664813833)。

#### Transformer

- [【超详细】【原理篇&实战篇】一文读懂Transformer-CSDN博客](https://blog.csdn.net/weixin_42475060/article/details/121101749)；
- [The Illustrated Transformer – Jay Alammar – Visualizing machine learning one concept at a time. (jalammar.github.io)](https://jalammar.github.io/illustrated-transformer/)。

#### Embedding

……

### 学习资料

- [von Neumann-CSDN博客](https://machinelearning.blog.csdn.net/?type=blog)；
- [Jay Alammar – Visualizing machine learning one concept at a time. (jalammar.github.io)](https://jalammar.github.io/)。

## AI 开发体系

### 编程语言

- Python 学习笔记：[GitHub - shen-shanshan/cs-self-learning/01.Languages/Python/01.Notes](https://github.com/shen-shanshan/cs-self-learning/tree/master/01.Languages/Python/01.Notes)；
- C++ 学习笔记：[GitHub - shen-shanshan/cs-self-learning/01.Languages/C&C++/02.Notes](https://github.com/shen-shanshan/cs-self-learning/tree/master/01.Languages/C%26C%2B%2B/02.Notes)。

## AI 训练框架 & 推理引擎

### PyTorch

- 总览：[Welcome to PyTorch Tutorials — PyTorch Tutorials 2.4.0+cu121 documentation](https://pytorch.org/tutorials/)；
- 入门教程（old）：[Learning PyTorch with Examples — PyTorch Tutorials 2.4.0+cu121 documentation](https://pytorch.org/tutorials/beginner/pytorch_with_examples.html#tensors)；
- 入门教程（new）：[Learn the Basics — PyTorch Tutorials 2.4.0+cu121 documentation](https://pytorch.org/tutorials/beginner/basics/intro.html)；
- Tensor 常用操作：[torch.Tensor详解和常用操作-CSDN博客](https://blog.csdn.net/sazass/article/details/109304327)。

### Llama

……

## AI 编译 & 计算架构

### CUDA

……

### CANN

- 开发文档：[CANN社区版8.0.RC3.alpha001开发文档-昇腾社区 (hiascend.com)](https://www.hiascend.com/document/detail/zh/CANNCommunityEdition/80RC3alpha001/quickstart/quickstart/quickstart_18_0001.html)。

## AI 硬件 & 体系结构

### INVIDIA GPU

- GPU 架构学习笔记：[【AI System】INVIDIA GPU 架构 & CUDA 平台入门学习-CSDN博客](https://blog.csdn.net/weixin_44162047/article/details/141569571?spm=1001.2014.3001.5501)。


### Ascend NPU

……
# LLM 配置文件

## 概述

大语言模型的配置文件是理解模型架构和特性的重要入口，通过配置文件，我们可以快速了解模型的设计理念、参数规模和功能特性。

代码示例：

```cpp
struct ModelConfig {
  int32_t dim = 0;
  int32_t hidden_dim = 0;
  int32_t layer_num = 0;
  int32_t head_num = 0;
  int32_t kv_head_num = 0;
  int32_t vocab_size = 0;
  int32_t seq_len = 0;
};
```

## 各参数含义

`architectures`：模型的核心架构类型。

**基于因果语言建模的模型（Causal Language Modeling）**：模型在生成下一个词时只能依赖当前词及之前的上下文，无法使用未来的信息。它被广泛用于文本生成任务，比如 GPT 系列模型。（示例：`Gemma2ForCausalLM`）

其他常见的语言模型架构：

- **Masked Language Modeling (MLM)**：用于填补文本中的空白（如：BERT）；
- **Sequence-to-Sequence (Seq2Seq)**：用于翻译、摘要等任务（如：T5、BART）。

`hidden_size`：模型每一层的隐藏状态维度。

`num_hidden_layers`：模型中有几层 Transformer Block。

> 隐藏层（Hidden Layer）是人工神经网络中的中间层，位于输入层和输出层之间。作用是对输入数据进行特征提取和变换，为最终的输出层提供高层次特征。隐藏层之所以称为“隐藏”，是因为其输出对外界不可见，只在网络内部流通。隐藏层的主要任务是通过线性变换和激活函数来捕捉数据中的复杂模式和特征。通过多层隐藏层的堆叠，网络可以逐渐提取出数据中越来越抽象的特征，也是深度学习的核心思想。
>
> `hidden_size`：隐藏层中神经元的数量。
>
> 上面两个参数共同决定了模型的规模：`num_hidden_layers` * `hidden_size`（计算开销的主要来源）。

`num_attention_heads`：模型每一层的注意力头数量。

`head_dim`：每个注意力头的维度。

> 多头注意力能够捕获不同的上下文关系，提高模型对复杂模式的学习能力。注意力头的总维度 = `num_attention_heads` * `head_dim`（不等于 `hidden_size`，`hidden_size` 包含的是整个 transformer 块中的参数，一般会比 attention head 的参数更多）。

`max_position_embeddings`：模型支持的最大序列长度。

## 参考资料

- [解析大模型的配置文件（config.json）：以 Gemma-2-2B 为例](https://blog.csdn.net/shizheng_Li/article/details/144866526)

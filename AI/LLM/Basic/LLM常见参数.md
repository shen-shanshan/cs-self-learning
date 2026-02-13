# LLM 常见参数

## Top-k & Top-p

解码策略（LLM 输出时采样 token 的方法）：Top-k 采样、Top-p 采样、……

- **贪心解码**：选择可能性最高的 token。缺点：输出可能一直循环同一段文本；
- **Top-k**：从 Top-k tokens 中挑选。优点：允许其它高分 tokens 有机会被选中，引入的随机性有助于提高生成的质量；
- **Top-p**：动态设置 tokens 候选列表的大小，将可能性之和不超过特定值的 top tokens 列入候选名单。Top-p 通常设置为较高的值（如：0.75，即从概率加起来不超过 75% 的 top tokens 中挑选）。

> 注意：如果 Top-k 和 Top-p 都启用，则 Top-p 在 Top-k 之后起作用。

**Likelihood（似然、可能性）**：一个 token 的 Likelihood 可以被认为是一个数字（通常在 -15 和 0 之间），它量化模型对句子中使用该 token 的意外程度（越接近 0，可能性就越高）。

## Temperature

**temperature**：用于调整模型生成结果的随机程度。

- 较低的 Temperature 意味着较少的随机性。Temperature 为 0 将始终产生相同的输出。执行具有“正确”答案的任务（如问题回答或总结）时，较低的 Temperature（< 1）更合适。如果模型开始自我重复，则表明温度过低；
- 较高的 Temperature 意味着更多的随机性，这可以帮助模型给出更有创意的输出。如果模型开始偏离主题或给出无意义的输出，则表明 Temperature 过高。

## Number of Generations

**num_generations**：在一次调用中生成多个结果。

## 参考资料

- [<u>Top-k & Top-p, Temperature</u>](https://zhuanlan.zhihu.com/p/613428710)

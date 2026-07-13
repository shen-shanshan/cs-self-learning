这是一个非常好的问题。现在（2025～2026）的 LLM 后训练（Post-training）几乎都是 **SFT + RL** 的流程，而 RL 的训练已经与推理框架（vLLM、SGLang、TensorRT-LLM 等）高度耦合。

如果你正在阅读 **vLLM、OpenRLHF、verl、TRL、SkyRL、OpenRL** 等代码，理解 **rollout** 是最重要的一步。

下面按照整个流程讲。

---

# 一、LLM 为什么需要 RL（Post-training）

一个 LLM 的训练一般分三步：

```
Pretraining
      │
      ▼
SFT (Supervised Fine-tuning)
      │
      ▼
RL (Reinforcement Learning)
```

例如 GPT-4、DeepSeek-R1、Qwen3、Kimi 等基本都是类似流程。

各阶段作用：

| 阶段          | 学什么                 |
| ----------- | ------------------- |
| Pretraining | 学语言能力               |
| SFT         | 学会按照 instruction 回答 |
| RL          | 学会"什么回答更好"          |

RL 的目标不是学习知识，而是**优化输出策略（policy）**。

例如：

问题：

> 9.11 和 9.9 哪个大？

SFT 可能输出：

```
9.9 更大
```

RL 希望模型学到：

```
9.11 = 9.110
9.9 = 9.900

因此 9.9 更大。
```

不仅答案正确，而且 reasoning 更完整。

---

# 二、RL 的基本思想

RL 可以理解成：

```
Prompt
   │
   ▼
LLM 生成回答
   │
   ▼
Reward Model 打分
   │
   ▼
RL Algorithm 更新模型
```

循环无数次。

也就是说：

```
Question
      │
      ▼
Policy (LLM)
      │
      ▼
Answer
      │
      ▼
Reward
      │
      ▼
Update Policy
```

因此 RL 本质上就是：

> **让 reward 高的回答以后出现得更多。**

---

# 三、RL 中几个重要角色

训练时通常有四种模型。

```
                Prompt
                   │
                   ▼
             Policy Model
                   │
            generate answer
                   │
         ┌─────────┴─────────┐
         ▼                   ▼
 Reward Model          Reference Model
         │                   │
         └─────────┬─────────┘
                   ▼
                PPO / GRPO
                   │
                   ▼
             更新 Policy
```

分别解释。

---

## 1）Policy Model

就是要训练的模型。

例如：

```
Qwen3-32B
DeepSeek-R1
Llama3
```

它负责：

```
输入 prompt

↓

输出 response
```

RL 最终更新的就是它。

---

## 2）Reward Model

负责评分。

例如：

```
Prompt:

Explain CUDA Graph.
```

模型回答：

```
CUDA Graph is...
```

Reward Model：

```
score = 0.83
```

如果回答很好：

```
score = 0.96
```

如果回答很差：

```
score = 0.12
```

RL 就利用这个 score 更新参数。

很多新方法（例如 DeepSeek-R1 的 GRPO）甚至不用单独的 Reward Model，而是直接利用规则或可验证奖励（Rule-based Reward、Verifier）。

---

## 3）Reference Model

Reference Model 是：

> SFT 模型的冻结版本。

为什么？

因为 RL 容易把模型训练坏。

例如：

训练目标：

```
输出越长 reward 越高
```

模型很快变成：

```
aaaaaaaaaaaaaaaaaaaaaaaaaaa
```

为了避免 policy 偏离太远：

```
KL(policy || reference)
```

这就是 PPO 中经典的 KL penalty。

---

# 四、RL 的训练流程（最经典）

整个 RL 一轮通常如下：

```
Dataset
   │
   ▼
sample prompts
   │
   ▼
Policy Generate
   │
   ▼
得到 response
   │
   ▼
Reward
   │
   ▼
Compute Advantage
   │
   ▼
PPO / GRPO
   │
   ▼
Update Policy
```

这里最耗时间的是：

```
Generate
```

为什么？

因为：

假设：

```
batch=512

每个 prompt

生成1024 token
```

那么：

```
512 × 1024

≈ 52 万 token
```

全部需要 autoregressive decoding。

因此：

> **RL 的瓶颈几乎都是 inference，而不是 backward。**

所以 RL 非常依赖 vLLM。

---

# 五、什么是 Rollout（最重要）

Rollout 可以理解成：

> **让当前 Policy 真正去跑一遍推理，生成完整 trajectory（轨迹）的过程。**

RL 里：

```
Prompt
↓

LLM

↓

Token1

↓

Token2

↓

Token3

↓

...

↓

EOS
```

整个过程就是：

```
rollout
```

也就是：

```
trajectory collection
```

例如：

Prompt:

```
What is CUDA Graph?
```

Rollout：

```
CUDA
↓

Graph

↓

is

↓

...

↓

EOS
```

得到：

```
(prompt,
 response,
 logprobs,
 tokens,
 reward)
```

这一整条数据。

RL 就靠 rollout 收集训练数据。

---

# 六、为什么 Rollout 非常耗时？

因为 rollout 本质就是 inference。

例如：

```
batch=512

max_new_tokens=2048
```

模型需要真正生成：

```
512 × 2048

≈100 万 token
```

这就是：

GPU 利用率最高的一部分。

所以很多 RL Framework 会把：

```
Rollout
```

单独交给：

```
vLLM
```

而不是 PyTorch generate。

---

# 七、RL Framework 与 vLLM 如何结合？

目前基本都是下面这种架构：

```
           RL Trainer

        (PPO / GRPO)

               │

               ▼

          Rollout Engine

        (vLLM Server)

               │

               ▼

        GPU Inference
```

也就是说：

RL Framework：

负责：

* sampling
* reward
* advantage
* optimizer

而：

vLLM：

负责：

```
generate()
```

---

流程如下：

```
Prompt

↓

发送给 vLLM

↓

vLLM generate

↓

返回：

response

tokens

logprobs

↓

RL Framework

↓

reward

↓

loss

↓

backward
```

所以：

> **vLLM 在 RL 中就是一个超高速 rollout engine。**

---

# 八、为什么要用 vLLM？

因为 PyTorch 原生 generate 太慢。

vLLM 有：

* PagedAttention
* Continuous Batching
* KV Cache 管理
* CUDA Graph
* Prefix Cache
* Chunked Prefill
* Speculative Decoding（部分场景）

RL rollout 几乎就是：

```
不停 generate

不停 generate

不停 generate
```

因此：

如果 rollout 提速：

```
训练速度
≈
直接提升
```

很多 RL 项目：

```
80%以上时间

都花在 rollout。
```

---

# 九、RL Framework 的整体架构

目前主流 RL 框架（如 OpenRLHF、verl）的整体结构通常如下：

```text
                 RL Trainer
        (PPO / GRPO / DPO ...)
                     │
      ┌──────────────┼──────────────┐
      │              │              │
      ▼              ▼              ▼
 Policy         Reward         Reference
  Model          Model            Model
      │
      ▼
 Rollout Engine
 (vLLM / SGLang)
      │
      ▼
 GPU Inference
```

其中：

* **RL Trainer**：负责采样、计算奖励、优势估计（Advantage）、损失函数和参数更新。
* **Policy Model**：当前正在训练的模型。
* **Rollout Engine**：负责高吞吐量生成（Generation），通常使用 vLLM。
* **Reward/Reference Model**：分别负责打分和提供 KL 正则化参考（不同算法略有差异）。

---

# 十、一轮 RL（以 GRPO/PPO 为例）的完整执行流程

```text
               Prompt Dataset
                     │
                     ▼
              Sample Prompts
                     │
                     ▼
          Rollout (vLLM Generate)
                     │
                     ▼
     responses + tokens + logprobs
                     │
                     ▼
           Reward / Verifier 打分
                     │
                     ▼
        Compute Advantage (GAE 或 Group Advantage)
                     │
                     ▼
          PPO / GRPO Loss 计算
                     │
                     ▼
          Backward + Optimizer Step
                     │
                     ▼
             更新 Policy Model
                     │
                     ▼
             下一轮 Rollout
```

整个流程会不断循环，直到模型达到目标性能。

---

## 总结

可以把现代大模型 RL 后训练理解为一条职责清晰的流水线：

* **Policy Model**：负责生成回答，是最终要优化的模型。
* **Rollout**：使用当前 Policy 对一批 Prompt 完整执行推理，收集回答、Token、Log Probability 等训练轨迹（Trajectory）。它本质上就是一次批量推理，因此通常是 RL 训练中最耗时的阶段。
* **Reward**：对生成结果进行评分，来源可以是奖励模型（Reward Model）、规则奖励（Rule-based Reward）或可验证器（Verifier）。
* **RL Algorithm（PPO、GRPO 等）**：根据 Reward 计算优势（Advantage）和损失，更新 Policy 参数。
* **vLLM 的作用**：充当高性能 **Rollout Engine**。它专注于高吞吐量推理（Generation），而 RL 框架负责采样、奖励计算和梯度更新，两者通过生成结果和 Log Probability 等信息进行协作。

因此，在现代 LLM 强化学习训练中，经常会看到一句话：

> **RL = Training Framework（PPO/GRPO） + Rollout Engine（vLLM）**

这也是为什么近年来 vLLM 不仅是一个推理框架，也逐渐成为大模型 RL 后训练生态中的关键基础设施。

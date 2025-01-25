# SGLang 洞察报告

## 项目背景

SGLang 是一个用于大语言模型 & 视觉模型高效推理的服务框架。

[<u>SGLang GitHub 地址</u>](https://github.com/sgl-project/sglang)。

### 主要特性

- 提供了基于 RadixAttention 的多项推理加速技术，使模型能够更加高效地完成推理，包括：prefix caching、jump-forward、constrained decoding、continuous batching 等；
- 提供了一种更加直观的前端接口，使 LLM 与用户的交互变得更加简洁和容易；
- 支持大部分生成式模型，且易于扩展对更多模型的支持。

与其它产品的对比：
TensorRT-LLM, vLLM, MLC-LLM, and Hugging Face TGI：**hard to use, difficult to customize, or lacking in performance**.
SGLang：**not only user-friendly and easily modifiable but also delivers top-tier performance**.

### 支持的模型

- generative models (Llama, Gemma, Mistral, QWen, DeepSeek, LLaVA, etc.)
- embedding models (e5-mistral)

### 系统及硬件支持

- 操作系统：Windows、Linux；
- 底层硬件：CPU、GPU。

### 研发机构

该项目由 `LMSYS` 组织研发。

> Large Model Systems Organization (LMSYS Org) is an open research organization founded by students and faculty from UC Berkeley in collaboration with Stanford, UCSD, and CMU.

[<u>LMSYS Org 主页</u>](https://lmsys.org/about/)。

主要贡献者：

- Lianmin Zheng，xAI，伯克利 phd
- Ying Sheng，斯坦福 phd，伯克利访学
- Liangsheng Yin，伯克利 research assistant
- Yineng Zhang，a software engineer on the Model Performance Team at Baseten
- Byron Hsu，linkedin
- Ke Bao
- Cody Yu，Staff Software Engineer at Anyscale
- ……

## 版本发布策略

- 2024/10: `Release v0.3.4.post1`，Hosted the first LMSYS online meetup，Added support for Intel XPU
- 2024/10: `Release v0.3.2`
- 2024/09: `Release v0.3.0`
- 2024/09: `Release v0.2.13`
- 2024/08: `Release v0.2.9`
- 2024/07: `Release v0.2.5`
- 2024/07: `Release v0.2.0`
- 2024/07: `Release v0.1.20`
- 2024/07: `Release v0.1.18`
- 2024/06: `Release v0.1.17`，论文发表，Add speculative execution for OpenAI API
- 2024/05: `v0.1.16`，Add OpenAI-compatible API server
- 2024/03: `Release v0.1.13`
- 2024/02: `Release v0.1.12`
- 2024/02: `Release v0.1.11`
- 2024/01: `Release v0.1.6`
- 2024/01: `Release v0.1.5`
- 2024/01: 第一行代码

总结：基本上每月都会发布新版本。

## 依赖

attention:

```
triton
triton.language
```

fused_moe/model_executor/models:

```
vllm.distributed
vllm.model_executor
vllm.config
```

quantization:

```
vllm.model_executor.layers.quantization
```

hf_transformers_utils:

```
vllm.transformers_utils.configs
```

utils:

```
triton.runtime.cache
```

总结：模型具体的执行部分，大量依赖于 vLLM 的 `distributed`/`model_executor`/`config` 等模块，attention 的部分实现依赖于使用 triton 编写的算子。

## 昇腾接入方式（TODO）

```
建议后面可以重点看下如果要昇腾支持sglang，gap在哪。
可以有重点的看看，不必要全看，比如layer或者model实现的代码里，是不是大量依赖triton。
昇腾适配最大可能得gap就是算子这层。
至于他和vllm，可以先看下二者是个啥关系，他在哪些地方用了vllm。
```

- 需要优先完成 vLLM 的适配工作；
- triton？
- 算子？

## 紧要程度及工作量

- star 5.8k，无客户需求；
- 依赖于 vLLM，可能需要先投入 vLLM 的昇腾适配工作。

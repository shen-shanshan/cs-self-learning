# vLLM Beijing Meetup Notes

## Summary

2025 年 3 月 16 日，vLLM Meetup 在北京中关村顺利举办了。vLLM Meetup 是一个我们从项目诞生之初就一直延续的传统，在此之前，我们已经和 a16z、IBM、Roblox、Cloudflare & BentoML、AWS、NVIDIA、Snowflake、Google Cloud 以及 Meta 等公司举办了共 9 次 Meetup，而本次 Meetup 我们邀请到了来自华为、字节等公司的工程师们，以及清华、北航的研究者们作为演讲者，分享他们在 vLLM 社区中的贡献与开发经验。另外，由于 vLLM 在工业界和学术界的广泛关注度，本次 Meetup 还吸引到了来自蚂蚁、招商银行等公司的用户，共同探讨他们关于 vLLM 的使用体验与未来的诉求等内容。

值得注意的是，本次 Meetup 是 vLLM 社区首次在中国举办的交流会，不仅为中国的各大企业和高校提供了一个很好的交流平台，也加深了 vLLM 社区与中国开发者的链接。在未来，我们希望在 vLLM 社区与中国用户的共同努力下，能够将 vLLM 打造得更加完善、高效和易用。

## vLLM Update

vLLM 的 Maintainer 之一张晨分享了 vLLM 社区中近期的工作与未来 v0.8.0 版本的 Release 计划。另外，她还重点分享了 vLLM V1 Engine 的新特性与使用方法。相比与 V0 版本，V1 Engine 将会更加简洁和高效，在未来，V1 也将会成为 vLLM 的默认选项。

vLLM 的 Maintainer 之一游凯超还分享了 vLLM 项目目前在业界的生态发展状况，以及 vLLM 社区在中国的交流频道，包括知乎和微信公众号等，方便了 vLLM 团队更好地与中国开发者建立连接。

## vLLM 硬件插件化机制与昇腾最佳实践

华为工程师王玺源，同时也是 vllm-ascend 项目的核心 maintainer，以昇腾 NPU 为例分享了华为在 vLLM 硬件插件化机制上的工作，讲解了 vLLM 轻松实现多设备后端支持背后的技术。除此之外，他还介绍了华为的昇腾 AI 芯片以及 CANN 计算架构的原理与特性，以及今后华为准备在 vLLM 社区开展的工作计划。

## VeRL：基于 Hybrid Controller 的 RLHF 框架

字节跳动工程师张驰，同时也是 VeRL 项目的核心开发者，分享了字节跳动在强化学习微调框架领域的研究和工作。他着重介绍了 VeRL 目前针对的痛点问题以及 Hybrid Controller 的核心工作原理。

## 大模型高效微调框架 LLaMA-Factory 配合 vLLM 的最佳实践

北航的研究者郑耀威，同时也是 LLaMA-Factory 项目的核心 Maintainer，分享了大模型微调领域当前的发展状况，以及 LLaMA-Factory 最新推出的图形化界面。另外，他还介绍了 LLaMA-Factory 是如何配合 vLLM 等框架进行使用，从而为开发者提供极致的易用性的。

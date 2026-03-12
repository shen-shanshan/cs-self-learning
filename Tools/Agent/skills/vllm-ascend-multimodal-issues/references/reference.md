# vllm-ascend 多模态相关 Open Issues 报告

> **仓库**: [vllm-project/vllm-ascend](https://github.com/vllm-project/vllm-ascend)
> **生成时间**: 2026-03-04 02:21 UTC
> **统计**: 共 **42** 个多模态相关 open issue

## 概览

| 问题类型 | 数量 |
|----------|------|
| Bug | 28 |
| Feature Request | 4 |
| Performance | 1 |
| Documentation | 1 |
| RFC | 1 |
| New Model Support | 1 |
| Usage/Question | 5 |
| Other | 1 |

---

## Bug (28)

- [#6934](https://github.com/vllm-project/vllm-ascend/issues/6934) **[Bug]: [kimi-k2.5] Attempted to assign 4225 = 4225 multimodal tokens to 1 placeholder**
  > 作者: @Levi-JQ | 时间: 2026-03-02 | 模型: `multimodal 通用` | Labels: `bug`

- [#6853](https://github.com/vllm-project/vllm-ascend/issues/6853) **[Bug]: Failed to apply Qwen2_5_VLProcessor for qwen2.5-vl-72b**
  > 作者: @nuclearwu | 时间: 2026-02-27 | 模型: `Qwen2.5-VL` | Labels: `bug`

- [#5890](https://github.com/vllm-project/vllm-ascend/issues/5890) **[Bug]: 部署qwen2.5-vl-7B，推理时有时候会输出很多个！**
  > 作者: @MrGu11 | 时间: 2026-01-14 | 模型: `Qwen2.5-VL` | Labels: `bug`

- [#5745](https://github.com/vllm-project/vllm-ascend/issues/5745) **[Bug]: 加载qwen2.5-vl-7B-Instruct有时候会输出一堆感叹号或者是引用一些莫名其妙的图片**
  > 作者: @MrGu11 | 时间: 2026-01-09 | 模型: `Qwen2.5-VL` | Labels: `bug`

- [#5571](https://github.com/vllm-project/vllm-ascend/issues/5571) **[Bug]: in v0.13.0rc1 version，During multimodal deployment, an error occurs: NPUAttributeError: 'NPUModelRunner' object has no attribute 'm_budget'**
  > 作者: @NewZxy | 时间: 2026-01-04 | 模型: `multimodal 通用` | Labels: `bug`

- [#5349](https://github.com/vllm-project/vllm-ascend/issues/5349) **[Bug]: Failed to run MiniCPM-V-4.5**
  > 作者: @gcanlin | 时间: 2025-12-25 | 模型: `MiniCPM-V` | Labels: `bug`

- [#4887](https://github.com/vllm-project/vllm-ascend/issues/4887) **[Bug][v0.11.0-dev]: The qwen2.5-vl-72b-bf16 model repetition issues when acync-scheduling is enabled.**
  > 作者: @Levi-JQ | 时间: 2025-12-10 | 模型: `Qwen2.5-VL` | Labels: `bug`

- [#4880](https://github.com/vllm-project/vllm-ascend/issues/4880) **[Bug]: Qwen2.5-VL-32B-instruct使用lm_eval进行精度测试时报错**
  > 作者: @MrZ20 | 时间: 2025-12-10 | 模型: `Qwen2.5-VL` | Labels: `bug`

- [#4548](https://github.com/vllm-project/vllm-ascend/issues/4548) **[Bug]: Qwen2.5-VL-3B-Instruct-W8A8量化模型拉起失败**
  > 作者: @Shirley125 | 时间: 2025-11-28 | 模型: `Qwen2.5-VL` | Labels: `bug`

- [#4435](https://github.com/vllm-project/vllm-ascend/issues/4435) **[Bug]: qwen2.5-vl-72b often reports `Internal Server Error ` and  request cause to application shutdown.**
  > 作者: @nuclearwu | 时间: 2025-11-25 | 模型: `Qwen2.5-VL` | Labels: `bug` `module:multimodal`

- [#4344](https://github.com/vllm-project/vllm-ascend/issues/4344) **[Bug]: 部署qwen vl视觉类模型引起物理机宕机**
  > 作者: @wzh5516 | 时间: 2025-11-21 | 模型: `其他多模态` | Labels: `bug`

- [#3965](https://github.com/vllm-project/vllm-ascend/issues/3965) **[Bug]: Why does the model Qwen2.5-VL 72B request encounter Out Of Memory (OOM) errors？**
  > 作者: @nuclearwu | 时间: 2025-11-04 | 模型: `Qwen2.5-VL` | Labels: `bug`

- [#3895](https://github.com/vllm-project/vllm-ascend/issues/3895) **[Bug]: Qwen_Qwen3-VL-30B-A3B-Instruct aclgraph piecewise模式部署报错**
  > 作者: @tt545571022 | 时间: 2025-10-30 | 模型: `其他多模态` | Labels: `bug` `module:multimodal`

- [#3701](https://github.com/vllm-project/vllm-ascend/issues/3701) **[Bug]: Qwen2.5-Omni model would report 500 internal error when infer request with video multimodal data in second time**
  > 作者: @Semmer2 | 时间: 2025-10-24 | 模型: `Qwen-Omni` | Labels: `bug`

- [#3452](https://github.com/vllm-project/vllm-ascend/issues/3452) **[Bug]: Repeated token in 0.9.1 of Qwen 2.5-VL**
  > 作者: @yxh-y | 时间: 2025-10-14 | 模型: `Qwen2.5-VL` | Labels: `bug`

- [#3261](https://github.com/vllm-project/vllm-ascend/issues/3261) **[Bug]: vllm 0.7.3 和 vllm-ascend 0.7.3 启动qwen2.5-vl-7b-insruct报错**
  > 作者: @design365 | 时间: 2025-09-29 | 模型: `Qwen2.5-VL` | Labels: `bug`

- [#3217](https://github.com/vllm-project/vllm-ascend/issues/3217) **[Compatibility]: Multi-modal support post-v0.11.0**
  > 作者: @DarkLight1337 | 时间: 2025-09-27 | 模型: `multimodal 通用` | Labels: `bug`

- [#3198](https://github.com/vllm-project/vllm-ascend/issues/3198) **[Bug]: Qwen2.5-vl-7b vllm0.10.2rc1 多次synthetic或textVqa测试后显存占满，长期不释放**
  > 作者: @Hjasongit | 时间: 2025-09-26 | 模型: `Qwen2.5-VL` | Labels: `bug`

- [#2997](https://github.com/vllm-project/vllm-ascend/issues/2997) **[Bug]: On 910B3, Qwen2.5-VL 72B with w8a8 quantization crashes when concurrency reaches 8**
  > 作者: @moguizhizi | 时间: 2025-09-18 | 模型: `Qwen2.5-VL` | Labels: `bug`

- [#2990](https://github.com/vllm-project/vllm-ascend/issues/2990) **[Bug]: llava-1.5-7b-hf, 910B2C, single-card,offline, hava error"not support kernel named rotary_embedding"**
  > 作者: @lyj-jjj | 时间: 2025-09-17 | 模型: `LLaVA` | Labels: `bug`

- [#2965](https://github.com/vllm-project/vllm-ascend/issues/2965) **[Bug]: Error when deploying Qwen2.5-VL 72B with pipeline parallelism**
  > 作者: @moguizhizi | 时间: 2025-09-16 | 模型: `Qwen2.5-VL` | Labels: `bug`

- [#2948](https://github.com/vllm-project/vllm-ascend/issues/2948) **[Bug]: Qwen2.5-VL 72B inference engine crashes when concurrency reaches 15**
  > 作者: @moguizhizi | 时间: 2025-09-16 | 模型: `Qwen2.5-VL` | Labels: `bug`

- [#2267](https://github.com/vllm-project/vllm-ascend/issues/2267) **[Bug]: 8张910B部署qwen2.5_vl_72b显存不足**
  > 作者: @Qiiuqh | 时间: 2025-08-07 | 模型: `Qwen2.5-VL` | Labels: `bug`

- [#2259](https://github.com/vllm-project/vllm-ascend/issues/2259) **[Bug]: microsoft/Florence-2-base failed to start in enage and graph model**
  > 作者: @zhangxinyuehfad | 时间: 2025-08-07 | 模型: `Florence` | Labels: `bug`

- [#2040](https://github.com/vllm-project/vllm-ascend/issues/2040) **[Bug]: Llava-1.5-7b-hf bad accuracy with image input via 0.9.2rc1 under offline mod**
  > 作者: @TaoZhang-Work | 时间: 2025-07-26 | 模型: `LLaVA` | Labels: `bug`

- [#1465](https://github.com/vllm-project/vllm-ascend/issues/1465) **[Bug]: OOM Error may become TimeoutError when using Qwen2.5_vl in vllm v1**
  > 作者: @ChenTaoyu-SJTU | 时间: 2025-06-26 | 模型: `Qwen2.5-VL` | Labels: `bug` `module:multimodal`

- [#1045](https://github.com/vllm-project/vllm-ascend/issues/1045) **[Bug]: Attempted to assign 58 = 58 multimodal tokens to 59 placeholders**
  > 作者: @wenba0 | 时间: 2025-06-03 | 模型: `multimodal 通用` | Labels: `bug`

- [#968](https://github.com/vllm-project/vllm-ascend/issues/968) **[Bug]: Qwen2.5-VL-7B-Instruct accuarcy test omm when tp=2 and accuracy decreased by 20% when tp=4**
  > 作者: @zhangxinyuehfad | 时间: 2025-05-27 | 模型: `Qwen2.5-VL` | Labels: `bug`

## Feature Request (4)

- [#6698](https://github.com/vllm-project/vllm-ascend/issues/6698) **[Feature]: Verify / Support `Phi-4-multimodal`**
  > 作者: @wjunLu | 时间: 2026-02-11 | 模型: `Phi-4-multimodal` | Labels: `feature request` `Intern`

- [#6694](https://github.com/vllm-project/vllm-ascend/issues/6694) **[Feature]: Verify / Support `Phi-4-multimodal`**
  > 作者: @wjunLu | 时间: 2026-02-11 | 模型: `Phi-4-multimodal` | Labels: `feature request` `Intern`

- [#2607](https://github.com/vllm-project/vllm-ascend/issues/2607) **[Feature]: [performance] enable DP for ViT in VLM**
  > 作者: @hsliuustc | 时间: 2025-08-28 | 模型: `VLM 通用` | Labels: `feature request`

- [#1134](https://github.com/vllm-project/vllm-ascend/issues/1134) **[Feature]: vllm-ascend quantization support multimodal model deployment**
  > 作者: @Enlion91 | 时间: 2025-06-09 | 模型: `multimodal 通用` | Labels: `feature request` `module:quantization`

## Performance (1)

- [#4659](https://github.com/vllm-project/vllm-ascend/issues/4659) **[Performance]: v0.11.0rc2版本qwen2.5-vl-3b-w8a8量化模型性能比qwen2.5-vl-3b非量化模型劣化**
  > 作者: @Shirley125 | 时间: 2025-12-03 | 模型: `Qwen2.5-VL` | Labels: `performance` `module:quantization`

## Documentation (1)

- [#4696](https://github.com/vllm-project/vllm-ascend/issues/4696) **[Doc]: Qwen2.5-Omni 教程中 “full_cuda_graph” 无法开启 / 不适用于 NPU**
  > 作者: @yooooo00 | 时间: 2025-12-04 | 模型: `Qwen-Omni` | Labels: `documentation`

## RFC (1)

- [#3508](https://github.com/vllm-project/vllm-ascend/issues/3508) **[RFC]: Multi-Modal Tasks**
  > 作者: @shen-shanshan | 时间: 2025-10-16 | 模型: `multimodal 通用` | Labels: `RFC` `module:multimodal`

## New Model Support (1)

- [#553](https://github.com/vllm-project/vllm-ascend/issues/553) **[Model]: LLaVA 1.6**
  > 作者: @MengqingCao | 时间: 2025-04-17 | 模型: `LLaVA` | Labels: `new model`

## Usage/Question (5)

- [#5695](https://github.com/vllm-project/vllm-ascend/issues/5695) **[Usage]: deploy GME-Qwen2-VL-2B-Instruct with vllm ascend error, error no is 561103 & error msg is ERR00100 PTA call acl api failed. EZ9999: Inner Error!**
  > 作者: @TaoZC1996 | 时间: 2026-01-07 | 模型: `Qwen2-VL`

- [#3917](https://github.com/vllm-project/vllm-ascend/issues/3917) **[Usage]: Questions about offline deployment and model compatibility in vllm-ascend, Is vllm-ascend customized for specific models like Qwen2.5-VL-7B-Instruct?**
  > 作者: @anurnomeru | 时间: 2025-10-30 | 模型: `Qwen2.5-VL`

- [#3867](https://github.com/vllm-project/vllm-ascend/issues/3867) **[Usage]: vllm-ascend 0.11.0拉起MiniCPM-V-4_5-int4 报错**
  > 作者: @ZCG12345 | 时间: 2025-10-29 | 模型: `MiniCPM-V`

- [#2586](https://github.com/vllm-project/vllm-ascend/issues/2586) **[Usage]: 测试qwen2.5-vl-7b 服务化模型会经常遇到模型输出特别长的感叹号，而且特别耗时。**
  > 作者: @chenhongming | 时间: 2025-08-28 | 模型: `Qwen2.5-VL`

- [#1975](https://github.com/vllm-project/vllm-ascend/issues/1975) **[Usage]: The token speed is too slow during inference. How can I improve the inference performance and speed of the Qwen series multimodal models?**
  > 作者: @Chan-Developer | 时间: 2025-07-23 | 模型: `multimodal 通用`

## Other (1)

- [#3493](https://github.com/vllm-project/vllm-ascend/issues/3493) **Add e2e test case for Qwen2.5-VL quantification**
  > 作者: @elilzhu | 时间: 2025-10-16 | 模型: `Qwen2.5-VL`

---

## 按多模态模型汇总

### Qwen2.5-VL (21)

- [Bug] [#6853](https://github.com/vllm-project/vllm-ascend/issues/6853) [Bug]: Failed to apply Qwen2_5_VLProcessor for qwen2.5-vl-72b
- [Bug] [#5890](https://github.com/vllm-project/vllm-ascend/issues/5890) [Bug]: 部署qwen2.5-vl-7B，推理时有时候会输出很多个！
- [Bug] [#5745](https://github.com/vllm-project/vllm-ascend/issues/5745) [Bug]: 加载qwen2.5-vl-7B-Instruct有时候会输出一堆感叹号或者是引用一些莫名其妙的图片
- [Bug] [#4887](https://github.com/vllm-project/vllm-ascend/issues/4887) [Bug][v0.11.0-dev]: The qwen2.5-vl-72b-bf16 model repetition issues when acync-scheduling is enabled.
- [Bug] [#4880](https://github.com/vllm-project/vllm-ascend/issues/4880) [Bug]: Qwen2.5-VL-32B-instruct使用lm_eval进行精度测试时报错
- [Performance] [#4659](https://github.com/vllm-project/vllm-ascend/issues/4659) [Performance]: v0.11.0rc2版本qwen2.5-vl-3b-w8a8量化模型性能比qwen2.5-vl-3b非量化模型劣化
- [Bug] [#4548](https://github.com/vllm-project/vllm-ascend/issues/4548) [Bug]: Qwen2.5-VL-3B-Instruct-W8A8量化模型拉起失败
- [Bug] [#4435](https://github.com/vllm-project/vllm-ascend/issues/4435) [Bug]: qwen2.5-vl-72b often reports `Internal Server Error ` and  request cause to application shutdown.
- [Bug] [#3965](https://github.com/vllm-project/vllm-ascend/issues/3965) [Bug]: Why does the model Qwen2.5-VL 72B request encounter Out Of Memory (OOM) errors？
- [Usage/Question] [#3917](https://github.com/vllm-project/vllm-ascend/issues/3917) [Usage]: Questions about offline deployment and model compatibility in vllm-ascend, Is vllm-ascend customized for specific models like Qwen2.5-VL-7B-Instruct?
- [Other] [#3493](https://github.com/vllm-project/vllm-ascend/issues/3493) Add e2e test case for Qwen2.5-VL quantification
- [Bug] [#3452](https://github.com/vllm-project/vllm-ascend/issues/3452) [Bug]: Repeated token in 0.9.1 of Qwen 2.5-VL
- [Bug] [#3261](https://github.com/vllm-project/vllm-ascend/issues/3261) [Bug]: vllm 0.7.3 和 vllm-ascend 0.7.3 启动qwen2.5-vl-7b-insruct报错
- [Bug] [#3198](https://github.com/vllm-project/vllm-ascend/issues/3198) [Bug]: Qwen2.5-vl-7b vllm0.10.2rc1 多次synthetic或textVqa测试后显存占满，长期不释放
- [Bug] [#2997](https://github.com/vllm-project/vllm-ascend/issues/2997) [Bug]: On 910B3, Qwen2.5-VL 72B with w8a8 quantization crashes when concurrency reaches 8
- [Bug] [#2965](https://github.com/vllm-project/vllm-ascend/issues/2965) [Bug]: Error when deploying Qwen2.5-VL 72B with pipeline parallelism
- [Bug] [#2948](https://github.com/vllm-project/vllm-ascend/issues/2948) [Bug]: Qwen2.5-VL 72B inference engine crashes when concurrency reaches 15
- [Usage/Question] [#2586](https://github.com/vllm-project/vllm-ascend/issues/2586) [Usage]: 测试qwen2.5-vl-7b 服务化模型会经常遇到模型输出特别长的感叹号，而且特别耗时。
- [Bug] [#2267](https://github.com/vllm-project/vllm-ascend/issues/2267) [Bug]: 8张910B部署qwen2.5_vl_72b显存不足
- [Bug] [#1465](https://github.com/vllm-project/vllm-ascend/issues/1465) [Bug]: OOM Error may become TimeoutError when using Qwen2.5_vl in vllm v1
- [Bug] [#968](https://github.com/vllm-project/vllm-ascend/issues/968) [Bug]: Qwen2.5-VL-7B-Instruct accuarcy test omm when tp=2 and accuracy decreased by 20% when tp=4

### Qwen-Omni (2)

- [Documentation] [#4696](https://github.com/vllm-project/vllm-ascend/issues/4696) [Doc]: Qwen2.5-Omni 教程中 “full_cuda_graph” 无法开启 / 不适用于 NPU
- [Bug] [#3701](https://github.com/vllm-project/vllm-ascend/issues/3701) [Bug]: Qwen2.5-Omni model would report 500 internal error when infer request with video multimodal data in second time

### Qwen2-VL (1)

- [Usage/Question] [#5695](https://github.com/vllm-project/vllm-ascend/issues/5695) [Usage]: deploy GME-Qwen2-VL-2B-Instruct with vllm ascend error, error no is 561103 & error msg is ERR00100 PTA call acl api failed. EZ9999: Inner Error!

### LLaVA (3)

- [Bug] [#2990](https://github.com/vllm-project/vllm-ascend/issues/2990) [Bug]: llava-1.5-7b-hf, 910B2C, single-card,offline, hava error"not support kernel named rotary_embedding"
- [Bug] [#2040](https://github.com/vllm-project/vllm-ascend/issues/2040) [Bug]: Llava-1.5-7b-hf bad accuracy with image input via 0.9.2rc1 under offline mod
- [New Model Support] [#553](https://github.com/vllm-project/vllm-ascend/issues/553) [Model]: LLaVA 1.6

### MiniCPM-V (2)

- [Bug] [#5349](https://github.com/vllm-project/vllm-ascend/issues/5349) [Bug]: Failed to run MiniCPM-V-4.5
- [Usage/Question] [#3867](https://github.com/vllm-project/vllm-ascend/issues/3867) [Usage]: vllm-ascend 0.11.0拉起MiniCPM-V-4_5-int4 报错

### Phi-4-multimodal (2)

- [Feature Request] [#6698](https://github.com/vllm-project/vllm-ascend/issues/6698) [Feature]: Verify / Support `Phi-4-multimodal`
- [Feature Request] [#6694](https://github.com/vllm-project/vllm-ascend/issues/6694) [Feature]: Verify / Support `Phi-4-multimodal`

### Florence (1)

- [Bug] [#2259](https://github.com/vllm-project/vllm-ascend/issues/2259) [Bug]: microsoft/Florence-2-base failed to start in enage and graph model

### multimodal 通用 (7)

- [Bug] [#6934](https://github.com/vllm-project/vllm-ascend/issues/6934) [Bug]: [kimi-k2.5] Attempted to assign 4225 = 4225 multimodal tokens to 1 placeholder
- [Bug] [#5571](https://github.com/vllm-project/vllm-ascend/issues/5571) [Bug]: in v0.13.0rc1 version，During multimodal deployment, an error occurs: NPUAttributeError: 'NPUModelRunner' object has no attribute 'm_budget'
- [RFC] [#3508](https://github.com/vllm-project/vllm-ascend/issues/3508) [RFC]: Multi-Modal Tasks
- [Bug] [#3217](https://github.com/vllm-project/vllm-ascend/issues/3217) [Compatibility]: Multi-modal support post-v0.11.0
- [Usage/Question] [#1975](https://github.com/vllm-project/vllm-ascend/issues/1975) [Usage]: The token speed is too slow during inference. How can I improve the inference performance and speed of the Qwen series multimodal models?
- [Feature Request] [#1134](https://github.com/vllm-project/vllm-ascend/issues/1134) [Feature]: vllm-ascend quantization support multimodal model deployment
- [Bug] [#1045](https://github.com/vllm-project/vllm-ascend/issues/1045) [Bug]: Attempted to assign 58 = 58 multimodal tokens to 59 placeholders

### VLM 通用 (1)

- [Feature Request] [#2607](https://github.com/vllm-project/vllm-ascend/issues/2607) [Feature]: [performance] enable DP for ViT in VLM

### 其他多模态 (2)

- [Bug] [#4344](https://github.com/vllm-project/vllm-ascend/issues/4344) [Bug]: 部署qwen vl视觉类模型引起物理机宕机
- [Bug] [#3895](https://github.com/vllm-project/vllm-ascend/issues/3895) [Bug]: Qwen_Qwen3-VL-30B-A3B-Instruct aclgraph piecewise模式部署报错

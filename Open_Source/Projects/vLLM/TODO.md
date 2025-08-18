# TODO

---

## vllm-ascend

- Torchair 代码重构
- Sliding Window 特性适配
- Structured Output 特性适配：
  - 适配 Structured Output 叠加 Spec Decode，并补充 UT
  - 清除异常 leak：GrammarMatcher、CompiledGrammar
- 梳理 vllm-ascend 模型支持标准（issue：1608）

## vllm

- [ATTN-FFN Disaggregation for MoE Models](https://github.com/vllm-project/vllm/issues/22799)
- forward context

---

## 技术学习

- DeepSeek 推理优化技术（MLA、MTP、DeepEP）
- 分布式并行（TP、DP、PP、SP、……）
- 分布式部署（PD 分离、AF 分离）

## 技术博客

- [ ] Graph Mode
- [ ] 算子调用方式
- [ ] ModelRunner Forward 与 PagedAttention([Reference](https://github.com/vllm-project/vllm-ascend/pull/1493)、sliding window)
- [ ] Speculative Decoding
- [ ] 多模态推理（推理 Pipeline，以 `Qwen2.5-VL` 为例）
- [ ] PD 分离（AF 分离）
- [x] V1 整体流程
- [x] Guided Decoding
- [ ] Version Release

---

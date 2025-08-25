# TODO

---

## vllm-ascend

- Torchair 代码重构
- Structured Output 特性适配：
  - 重构 apply_grammar_bitmask() 方法
  - 适配 Structured Output 叠加 Spec Decode，并补充 UT
  - 思考性能优化方案（图模式？并行？）

## vllm

- [ATTN-FFN Disaggregation for MoE Models](https://github.com/vllm-project/vllm/issues/22799)
- forward_context ?
- TODO(woosuk)

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

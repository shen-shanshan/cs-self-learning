# TODO

- [x] 用 AI 重新生成 PR 描述（写 skill）
- [x] cuda graph replay buffer 增加驱逐策略，设置长度上限
- [x] 用 AI 重新生成测试用例
- [ ] 测试多卡 vit dp mode 性能
- [ ] 更新对应文档
- [x] 理解视频 dummy grid_thw 构造策略
- [x] 更新注释：当 EVS（Efficient Video Sampling）剪枝启用时，token 数量是数据依赖的（需根据帧间差异动态选择保留的 token），无法被 CUDA Graph 捕获，因此仅在 EVS 关闭时启用视频 CUDA Graph。

---

请为 PR https://github.com/vllm-project/vllm/pull/38061/changes 中的改动在 tests/v1/cudagraph/test_encoder_cudagraph.py 中生成对应的测试用例，要求尽可能复用 SimpleMockViTModel 中的代码，减少重复代码。

```bash
pytest tests/v1/cudagraph/test_encoder_cudagraph.py -v
```

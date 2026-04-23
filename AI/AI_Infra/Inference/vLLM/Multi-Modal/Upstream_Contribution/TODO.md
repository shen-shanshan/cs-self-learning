# TODO

**ViT CG:**

- [x] Qwen3-VL ViT CG 支持 mixed inputs -> update doc
- [ ] VLM example add `--enable-cuda-graph` param
- [x] update default `max_frames_per_batch` according to model config `_MAX_FRAMES_PER_VIDEO` -> add new CG interface (WIP)
- [ ] Add e2e tests
- [ ] 测试 Qwen3.5 并更新文档
- [x] 梳理 model list 放到 issue
- [ ] 优化 `encoder_cudagraph_token_budgets` max value auto-infer

**EPD:**

- [ ] EPD 支持 Mooncake 传输后端

**OOT:**

- [ ] Linear 算子列表 OOT 扩展支持：https://github.com/vllm-project/vllm/pull/39538

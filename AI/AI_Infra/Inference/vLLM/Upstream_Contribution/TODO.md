# TODO

**ViT CG:**

- [x] Qwen3-VL ViT CG 支持 mixed inputs -> update doc
- [x] VLM example add `--enable-cuda-graph` param
- [x] update default `max_frames_per_batch` according to model config `_MAX_FRAMES_PER_VIDEO` -> add new CG interface (WIP)
- [x] Add e2e tests
- [x] 梳理 model list 放到 issue
- [x] 测试 Qwen3.5 并更新文档
- [x] 支持 DeepSeek-OCR 模型（支持 dual-path graph）
- [x] 为 Step3-VL 支持 dual-path graph
- [ ] 为 dual-path graph 支持分别配置 global 和 local 的 token budget
- [ ] 优化 `encoder_cudagraph_token_budgets` max value auto-infer
- [ ] 修复 `max_frames_per_batch` auto-infer 上限过少（=2）的 bug
- [ ] 支持 DeepSeek-OCR-2 模型（安排给 Barry Shen 做）
- [ ] 完善 `get_num_graphs_to_capture()` 方法的测试（`test_num_graphs_to_capture_tracks_budgets()` 增加双路径测试）

**ModelRunner V2:**

- [ ] 支持 AMD GPU
- [ ] 支持 ViT CUDA graph

**AMD ROCm:**

...

**Others:**

...

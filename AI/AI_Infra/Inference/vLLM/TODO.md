# TODO

- Multi-Modal Tasks [#3508](https://github.com/vllm-project/vllm-ascend/issues/3508)
- vLLM Ascend On Call Dashboard [#4748](https://github.com/vllm-project/vllm-ascend/issues/4748)
- Elegant main2main scrolling upgrade [#4709](https://github.com/vllm-project/vllm-ascend/issues/4709)

## 开发任务

- 拆分 vllm 多模态编译配置
- profling 阶段加入 merge 操作
- 替换 mm encoder attention 算子 (做 benchmark) https://github.com/vllm-project/vllm-ascend/issues/2239

## 查漏补缺

- 了解 `@support_torch_compile` 原理
- 了解 torch compile mode 和 CUDA Graphs mode 的区别？

## 源码学习

### Parallelism

- [ ] [Parallelism and Scaling](https://docs.vllm.ai/en/latest/serving/parallelism_scaling.html)
- [ ] vllm.distributed (✅ learn `torch.distributed` first)
- [ ] Data Parallel Attention and Expert Parallel MoEs [#16037](https://github.com/vllm-project/vllm/issues/16037)

### Data Parallel

- [ ] [Data Parallel Deployment](https://docs.vllm.ai/en/latest/serving/data_parallel_deployment.html)
- [ ] DP Server [Design Doc](https://docs.google.com/document/d/10jhCNxJYvsUhtMtiMAaW2MxU5LU8HVje2pGDnj49gH4/edit?tab=t.0#heading=h.4yilyuecj4k)
- [ ] vllm/v1/engine/coordinator.py (DPCoordinator、DPLB、多 API Server)
- [ ] vllm/v1/engine/core_client.py/self.resources.engine_manager (CoreEngineActorManager for RayDPClient)
- [ ] DPEngineCoreActor
- [ ] DPEngineCoreProc
- [ ] DP scale-out (1/N): Use zmq ROUTER/DEALER sockets for input queue [#15906](https://github.com/vllm-project/vllm/pull/15906)
- [ ] DP scale-out (2/N): Decouple engine process management and comms [#15977](https://github.com/vllm-project/vllm/pull/15977)
- [ ] DP Wave: More robust DP/EP dummy request coordination [#16277](https://github.com/vllm-project/vllm/pull/16277)
- [ ] DP Coordinator: API-server scaleout with many-to-many server-engine comms [#17546](https://github.com/vllm-project/vllm/pull/17546)
- [ ] External DPLB [#19790](https://github.com/vllm-project/vllm/pull/19790)
- [ ] Internal DPLB [#21238](https://github.com/vllm-project/vllm/pull/21238)
- [ ] Enable DP-aware routing in OpenAI API requests [#24945](https://github.com/vllm-project/vllm/pull/24945)

### Expert Parallel

- [ ] [Expert Parallel Deployment](https://docs.vllm.ai/en/latest/serving/expert_parallel_deployment.html)
- [ ] [Fused MoE Modular Kernel](https://docs.vllm.ai/en/latest/design/fused_moe_modular_kernel.html)
- [ ] FusedMoE in vllm-ascend
- [x] EPLB [#18343](https://github.com/vllm-project/vllm/pull/18343)

### Async Scheduling

- [x] Restructure the core loop to allow more asynchrony [#23233](https://github.com/vllm-project/vllm/issues/23233)
- [ ] Fully overlap model execution [#23569](https://github.com/vllm-project/vllm/pull/23569)

```
vllm_config.scheduler_config.async_scheduling
```

### Dual Batch Overlap

- [x] [Dual Batch Overlap](https://docs.vllm.ai/en/latest/design/dbo.html?h=dual+batch+overlap)
- [x] Add support for "splitting" the CommonAttentionMetadata [#21153](https://github.com/vllm-project/vllm/pull/21153)
- [ ] Add Dual-Batch Overlap mechanism to VLLM [#23693](https://github.com/vllm-project/vllm/pull/23693)
- [ ] Dual-Batch Overlap add DeepEP High Throughput support and Prefill support [#24845](https://github.com/vllm-project/vllm/pull/24845)
- [ ] Reduce the Cuda Graph memory footprint when running with DBO [#25779](https://github.com/vllm-project/vllm/pull/25779)

### CUDA Graph

- [ ] [torch.compile integration](https://docs.vllm.ai/en/latest/design/torch_compile.html)
- [ ] Allow full cudagraph with separate attention routines and orthogonal to compilation [#20059](https://github.com/vllm-project/vllm/pull/20059)

### PD Disaggregation

- [ ] [Disaggregated Prefilling](https://docs.vllm.ai/en/latest/features/disagg_prefill.html)

### Multi-Modal

- [ ] [Multi-Modal Data Processing](https://docs.vllm.ai/en/stable/design/mm_processing/)
- [ ] Add PrefixLM support to FlexAttention backend [#27938](https://github.com/vllm-project/vllm/pull/27938)
- [ ] Use non-blocking CPU-GPU copy of multimodal data [#28141](https://github.com/vllm-project/vllm/pull/28141)
- [ ] Move multimodal_cpu_fields definition to field config [#30181](https://github.com/vllm-project/vllm/pull/30181)

### Others

- [ ] [Profiling vLLM](https://docs.vllm.ai/en/latest/contributing/profiling.html)
- [ ] [Integration with Hugging Face](https://docs.vllm.ai/en/latest/design/huggingface_integration.html)
- [ ] [Hybrid KV Cache Manager](https://docs.vllm.ai/en/latest/design/hybrid_kv_cache_manager.html)
- [ ] [Paged Attention](https://docs.vllm.ai/en/latest/design/paged_attention.html)
- [ ] [Plugin System](https://docs.vllm.ai/en/latest/design/plugin_system.html)
- [x] forward_context
- [ ] model_runner input_batch
- [ ] DeviceMemoryProfiler
- [ ] [GPU Model Runner V2](https://github.com/vllm-project/vllm/pull/25266)
- [ ] Support MP Executor for multi node distributed inference [#23691](https://github.com/vllm-project/vllm/pull/23691)

## 技术博客

- [ ] vLLM 分离式架构 - 从 PD 分离到 AF 分离
- [ ] vLLM 推理可靠性 - 弹性伸缩与容错
- [ ] vLLM 多模态推理流程
- [x] vLLM CustomOp 文档
- [x] 卷积计算加速
- [x] V1 整体流程
- [x] Guided Decoding V0/V1

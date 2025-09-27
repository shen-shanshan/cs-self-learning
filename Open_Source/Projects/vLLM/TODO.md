# TODO

## vllm Tasks

- [ATTN-FFN Disaggregation for MoE Models](https://github.com/vllm-project/vllm/issues/22799)
- EEP
- Serverless

## vllm-ascend Tasks

- Mamba attn backend plugin

## 文档阅读

**User Guide:**

- [ ] [Data Parallel Deployment](https://docs.vllm.ai/en/latest/serving/data_parallel_deployment.html)
- [ ] [Expert Parallel Deployment](https://docs.vllm.ai/en/latest/serving/expert_parallel_deployment.html)
- [ ] [Parallelism and Scaling](https://docs.vllm.ai/en/latest/serving/parallelism_scaling.html)
- [ ] [Disaggregated Prefilling](https://docs.vllm.ai/en/latest/features/disagg_prefill.html)

**Developer Guide:**

- [ ] [Profiling vLLM](https://docs.vllm.ai/en/latest/contributing/profiling.html)
- [ ] [Fused MoE Modular Kernel](https://docs.vllm.ai/en/latest/design/fused_moe_modular_kernel.html)
- [ ] [Integration with Hugging Face](https://docs.vllm.ai/en/latest/design/huggingface_integration.html)
- [ ] [Hybrid KV Cache Manager](https://docs.vllm.ai/en/latest/design/hybrid_kv_cache_manager.html)
- [ ] [Paged Attention](https://docs.vllm.ai/en/latest/design/paged_attention.html)
- [ ] [Plugin System](https://docs.vllm.ai/en/latest/design/plugin_system.html)
- [ ] [torch.compile integration](https://docs.vllm.ai/en/latest/design/torch_compile.html)

## 源码阅读

- [ ] vllm.distributed (learn `torch.distributed` first)
- [ ] Data Parallel Attention and Expert Parallel MoEs [#16037](https://github.com/vllm-project/vllm/issues/16037)
- [ ] FusedMoE
- [x] EPLB [#18343](https://github.com/vllm-project/vllm/pull/18343)
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
- [ ] PD 分离
- [ ] forward_context
- [ ] Fully overlap model execution [#23569](https://github.com/vllm-project/vllm/pull/23569)
- [ ] model_runner input_batch
- [ ] DeviceMemoryProfiler

## 技术博客

- [ ] vLLM 分离式架构 - 从 PD 分离到 AF 分离
- [ ] vLLM 推理可靠性 - 弹性伸缩与容错
- [x] V1 整体流程
- [x] Guided Decoding V0/V1

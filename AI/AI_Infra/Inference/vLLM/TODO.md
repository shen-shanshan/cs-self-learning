# TODO List

## Tasks

**ViT support CUDA graph:**
ViT Full CUDA Graph [#35963](https://github.com/vllm-project/vllm/pull/35963)
**TODO：**
1.昇腾适配 ACLGraph 适配；
2.支持更多模型，比如：Qwen3-Omni；
3.支持更多模态，比如：视频、音频；
4.移除 CustomOp 中遗留的 comment；

deepstack 支持 cache?

支持 fish-speech 模型？

## 待看 PR/Issue/源码

### 未分类

- [Disaggregated-encoder 🌟](https://docs.vllm.ai/projects/ascend/en/latest/user_guide/feature_guide/epd_disaggregation.html)
- [Ascend PyTorch Profiler](https://docs.vllm.ai/projects/ascend/en/latest/developer_guide/performance_and_debug/service_profiling_guide.html#ascend-pytorch-profiler)

**Critical RFC:**

- [ ] Disaggregated Everything - Token In <> Token Out API Server [#22817](https://github.com/vllm-project/vllm/issues/22817)
- [ ] Unified Input Formatting and Processing via Renderer [#22880](https://github.com/vllm-project/vllm/issues/22880)

### Multi-Modal 🌟

- [ ] [Multi-Modal Data Processing](https://docs.vllm.ai/en/stable/design/mm_processing/)
- [x] Schedule failure due to wrong `get_image_size_with_most_features` [#29692](https://github.com/vllm-project/vllm/pull/29692)

**CUDA Graph:**

- [x] Enable supports_torch_compile on generic nn.Module and demonstrate speedup on Qwen Vision model [#23207](https://github.com/vllm-project/vllm/pull/23207)
- [x] Add torch.compile support for Qwen3VL [#27741](https://github.com/vllm-project/vllm/pull/27741)
- [x] Compile LLaMa Vision Encoder [#30709](https://github.com/vllm-project/vllm/pull/30709)
- [ ] Enable Piecewise CUDA Graphs for Qwen3-VL and Qwen2.5-VL ViT to Improve Performance [#33232](https://github.com/vllm-project/vllm/pull/33232)
- [ ] ViT Full CUDA Graph [#35963](https://github.com/vllm-project/vllm/pull/35963)
- [ ] Modify cudagraph callable to check for is_forward_context_set [#36288](https://github.com/vllm-project/vllm/pull/36288)
- [ ] Remove requirement to set_model_tag to avoid cache conflict [#36555](https://github.com/vllm-project/vllm/pull/36555)

**EPD:**

- [ ] SHMConnector: Share Memory based EC Connector [#33714](https://github.com/vllm-project/vllm/pull/33714)

**vLLM-Omni/SGLang:**

- [ ] Support torch compile for diffusers backend [#19673](https://github.com/sgl-project/sglang/pull/19673)

### CUDA Graph

- [ ] [torch.compile integration](https://docs.vllm.ai/en/latest/design/torch_compile.html)
- [ ] Allow full cudagraph with separate attention routines and orthogonal to compilation [#20059](https://github.com/vllm-project/vllm/pull/20059)

### vLLM IR

- [ ] [Fusion torch.compile passes](https://docs.vllm.ai/en/latest/design/fusions/)
- [ ] vLLM IR: A Functional Intermediate Representation for vLLM [#32358](https://github.com/vllm-project/vllm/issues/32358)
- [ ] Central fusion work tracker [#36066](https://github.com/vllm-project/vllm/issues/36066)

### Triton

- [ ] Add `fused_sigmoid_gating_delta_rule_update` kernel for Qwen3 Next [#35777](https://github.com/vllm-project/vllm/pull/35777)

### 性能优化

- [ ] Overlap shared experts with send/recv [#23273](https://github.com/vllm-project/vllm/pull/23273)
- [ ] Enable dual stream execution of input projection for Qwen3 Next 🌟 [#36795](https://github.com/vllm-project/vllm/pull/36795)

**vllm-ascend:**

- [ ] Optimize split_qkv_rmsnorm_rope operator. [#6827](https://github.com/vllm-project/vllm-ascend/pull/6827)
- [ ] Implement global CPU slicing and improve IRQ binding for Ascend NPUs, ensuring non-overlapping CPU partitions and better resource management. [#6945](https://github.com/vllm-project/vllm-ascend/pull/6945)
- [ ] Optimize MTP execution by reordering state update operation. [#6844](https://github.com/vllm-project/vllm-ascend/pull/6844)
- [ ] Avoid CPU sync in mrope_positions copy by using full tensor copy. [#7014](https://github.com/vllm-project/vllm-ascend/pull/7014)
- [ ] Remove H2D synchronization for expert_map in MoE models. [#7000](https://github.com/vllm-project/vllm-ascend/pull/7000)


**RFC:**

- [ ] Optimizations for MOE models (GLM4.7, DeepSeek series) [#31755](https://github.com/vllm-project/vllm/issues/31755)
- [ ] Performance optimation of decode in DeepSeek Large EP situation [#2905](https://github.com/vllm-project/vllm-ascend/issues/2905)

### Parallelism

- [ ] [Parallelism and Scaling](https://docs.vllm.ai/en/latest/serving/parallelism_scaling.html)
- [ ] vllm.distributed (✅ learn `torch.distributed` first)
- [ ] Data Parallel Attention and Expert Parallel MoEs [#16037](https://github.com/vllm-project/vllm/issues/16037)

**Data Parallel:**

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

**Expert Parallel:**

- [ ] [Expert Parallel Deployment](https://docs.vllm.ai/en/latest/serving/expert_parallel_deployment.html)
- [ ] [Fused MoE Modular Kernel](https://docs.vllm.ai/en/latest/design/fused_moe_modular_kernel.html)
- [ ] FusedMoE in vllm-ascend
- [x] EPLB [#18343](https://github.com/vllm-project/vllm/pull/18343)

### Async Scheduling

- [x] Restructure the core loop to allow more asynchrony [#23233](https://github.com/vllm-project/vllm/issues/23233)
- [ ] Fully overlap model execution [#23569](https://github.com/vllm-project/vllm/pull/23569)

### Dual Batch Overlap

- [x] [Dual Batch Overlap](https://docs.vllm.ai/en/latest/design/dbo.html?h=dual+batch+overlap)
- [x] Add support for "splitting" the CommonAttentionMetadata [#21153](https://github.com/vllm-project/vllm/pull/21153)
- [ ] Add Dual-Batch Overlap mechanism to VLLM [#23693](https://github.com/vllm-project/vllm/pull/23693)
- [ ] Dual-Batch Overlap add DeepEP High Throughput support and Prefill support [#24845](https://github.com/vllm-project/vllm/pull/24845)
- [ ] Reduce the Cuda Graph memory footprint when running with DBO [#25779](https://github.com/vllm-project/vllm/pull/25779)

### PD Disaggregation

- [ ] [Disaggregated Prefilling](https://docs.vllm.ai/en/latest/features/disagg_prefill.html)

### Model Runner V2

- [ ] Remove UvaBufferPool for cpu->gpu copy [#33055](https://github.com/vllm-project/vllm/pull/33055)
- [ ] Use a different stream for grammar bitmask h2d copy [#33059](https://github.com/vllm-project/vllm/pull/33059)
- [ ] Add dummy profile_cudagraph_memory API [#36520](https://github.com/vllm-project/vllm/pull/36520)
- [ ] Add model_state inputs to CUDA graph capture [#36544](https://github.com/vllm-project/vllm/pull/36544)

### Others

- [x] [Profiling vLLM](https://docs.vllm.ai/en/latest/contributing/profiling.html)
- [ ] [Integration with Hugging Face](https://docs.vllm.ai/en/latest/design/huggingface_integration.html)
- [ ] [Hybrid KV Cache Manager](https://docs.vllm.ai/en/latest/design/hybrid_kv_cache_manager.html)
- [ ] [Paged Attention](https://docs.vllm.ai/en/latest/design/paged_attention.html)
- [x] [Plugin System](https://docs.vllm.ai/en/latest/design/plugin_system.html)
- [x] forward_context
- [ ] model_runner input_batch
- [ ] Support MP Executor for multi node distributed inference [#23691](https://github.com/vllm-project/vllm/pull/23691)

## 写技术博客

- [ ] vLLM 推理可靠性 - 弹性伸缩与容错
- [ ] vLLM 多模态推理 - EPD 分离
- [ ] vLLM 多模态推理 - Encoder Cache & 显存管理
- [x] vLLM 多模态推理 - ViT 性能优化
- [x] vLLM 算力多样性 - 硬件插件与 CustomOp
- [x] vLLM 多模态推理 - 卷积计算加速
- [x] vLLM V1 整体流程
- [x] Guided Decoding V1
- [x] Guided Decoding V0

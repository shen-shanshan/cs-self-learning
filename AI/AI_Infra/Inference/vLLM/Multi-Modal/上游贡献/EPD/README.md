# Support Mooncake Based ECConnector for EPD

- [vLLM EPD Blog](https://vllm.ai/blog/vllm-epd)
- [vLLM EPD Example](https://docs.vllm.ai/en/stable/examples/online_serving/disaggregated_encoder/)

## Idea

Elastic encoder?
centralized encoder cache?
支持 mooncake 传输引擎
~~抽取 encoder model runner~~
优化 proxy 调度策略 - 招行
支持 CUDA Graph
GPU 显存利用率管理，`--mm-encoder-only`，E 实例的显存没有利用起来？

## Questions

vllm-ascend KVCache Pool？
Mooncake vs NCCL connector 本质区别？
block table 如何跨节点保持一致？
GPUDirect RDMA 是怎么绕过 CPU page table 的？
为什么 zero-copy 会限制内存管理？
Mooncake 的 session / endpoint 是怎么管理 RDMA 连接的？

ViT CUDA Graph support？

Load Balancing？dynamic scaling

Fault tolerance？error handling

## Background & Motivation

https://github.com/vllm-project/vllm/pull/33714

From my perspective, the SHM connector, while simple and useful, could potentially be subsumed by a more general connector as a fallback mode (for example, in Mooncake). Therefore, at this stage, I prefer to focus on providing **a Mooncake-based ECConnector** implementation that supports multiple transport backends. This would allow us to cover different throughput and deployment requirements in a unified, production-ready transfer stack.

- `ExampleECConnector` can serve as a lightweight implementation for debugging and experimentation.
- `MooncakeECConnector` can serve as the industrial-grade solution for real-world deployments.

One technical difference compared to KV Cache transfer is that **encoder cache tensors do not have fixed, pre-allocated memory addresses**. Because of this, we may need to explicitly **allocate a dedicated transmission buffer for encoder cache transfer**. To keep overall memory usage under control, this buffer space might need to be carved out from the encoder cache memory budget itself, ensuring that total memory consumption remains bounded.

## Mooncake in vLLM

在 vLLM 的 mooncake connector 里，KV cache 是这样处理的：取每个 tensor 的 `data_ptr`（GPU 地址），传输用的是裸指针（地址）。

---
**在 vLLM 的 PD 分离场景下，Mooncake 是怎么应用的？**

Mooncake 是以 KV Connector 插件的形式接入 vLLM 的（抽象层：KV Connector）。

MooncakeConnector (实现 KVConnector)：

1. 注册 KV cache 内存：在 Prefill / Decode 初始化时，`batch_register_memory(kv_ptrs, kv_sizes)` 把 GPU KV cache 注册成 RDMA 可访问内存，建立 address → rkey 映射；
2. 建立 session（P ↔ D）：管理远程内存权限、建立连接（类似 RDMA QP）；
3. 执行 KV 传输（核心）：`batch_transfer_sync_write(src_ptrs, dst_ptrs, lengths)` 直接把 P 的 GPU 内存 copy 到 D 的 GPU 内存（绕过 CPU）。Decode 侧：`recv_kv(...)`， 没有“接收 buffer copy”这个动作，而是直接写入 Decode 的 KV cache 内存（可以优化为 async pipeline？）。

KV cache 是 block 化的（PagedAttention）：每个 block → 一个固定地址，远程直接 copy block。
地址是稳定的：vLLM 提前分配 KV cache arena，不做 relocation。
不走“逻辑 KV”，而是“物理 KV”，vLLM 用 block table 维护逻辑关系：request → block_ids → addr。
vLLM 管“索引”，Mooncake 管“内存搬运”。

- 传统方案：KV → serialize → network → deserialize → GPU；
- Mooncake：KV (GPU addr) → RDMA → KV (GPU addr)，少了三层开销：CPU copy、编解码、框架参与。

> Mooncake API:
> [batch-register-memory](https://kvcache-ai.github.io/Mooncake/python-api-reference/transfer-engine.html#batch-register-memory)
> [batch-transfer-sync-write](https://kvcache-ai.github.io/Mooncake/python-api-reference/transfer-engine.html#batch-transfer-sync-write)

---
[MooncakeConnector 源码](https://github.com/vllm-project/vllm/blob/a435e3108d82eb96d9b3954c1935afbbf4c5f69b/vllm/distributed/kv_transfer/kv_connector/v1/mooncake/mooncake_connector.py#L330C1-L330C45)

## Prompt

**第一版：**

需求背景：https://github.com/vllm-project/vllm/pull/33714#issuecomment-3882716972
请你参考 vLLM 中 MooncakeConnector 在 PD 分离场景（传输 KV Cache）中的注册和使用方式，实现 MooncakeECConnector，用于支持 EPD 特性下的 Encoder Cache 传输，你只需要考虑 SHM 通信方式，先不要考虑基于 RDMA 的实现。
你不需要写测试用例，只写核心代码实现，并生成一份 markdown 格式的设计文档，放到当前项目的根目录下。

需求背景：“请你参考 vLLM 中 MooncakeConnector 在 PD 分离场景（传输 KV Cache）中的注册和使用方式，实现 MooncakeECConnector，用于支持 EPD 特性下的 Encoder Cache 传输，你只需要考虑 SHM 通信方式，先不要考虑基于 RDMA 的实现。你不需要写测试用例，只写核心代码实现，并生成一份 markdown 格式的设计文档，放到当前项目的根目录下。详情：https://github.com/vllm-project/vllm/pull/33714#issuecomment-3882716972”。

目前，你已经根据需求背景实现了基于 SHM 的 MooncakeECConnector，现在请把 mooncake TransferEngine（只用 TCP）也集成进来，作为 SHM 的 fallback，并更新设计文档。

/plan continue
Continue applying the previous plan. You stopped midway—resume from where you left off without re-planning.

用户通过配置指定传输后端。
只实现 TCP 传输后端，但需要考虑将来支持更多的传输后端，比如：RDMA、SHM 等。

we need to rethink how we manage the embeddings internally inside vLLM and might introduce unnecessary complexity (we might need to introduce "embed blocks" based on hash + position).

参考资料：
https://kvcache-ai.github.io/Mooncake/getting_started/supported-protocols.html#quick-reference

---
**第二版：**

需求背景：
vLLM 的 EPD 特性目前只支持 ExampleECConnector，我们希望集成 MooncakeECConnector，并通过 Mooncake 来统一管理多种传输后端，比如：TCP/RDMA/SHM。
https://github.com/vllm-project/vllm/pull/33714#issuecomment-3882716972

设计目标：
请你参考 vLLM 中 MooncakeConnector 在 PD 分离场景（传输 KV Cache）中的注册和使用方式，实现 MooncakeECConnector，用于支持 EPD 特性下的 Encoder Cache 传输。
目前，你只需要实现 TCP 传输方式，但需要考虑将来可能支持更多的传输后端，比如：RDMA、SHM 等（保证接口的可扩展性），比如：你可以实现 MooncakeECConnector 基类，TCPMooncakeECConnector 继承该基类，未来需要实现其它传输后端时，可以直接添加 RDMAMooncakeECConnector、SHMMooncakeECConnector 等子类。
用户通过 vLLM 启动配置来指定使用哪种 Mooncake 传输后端，不支持启动后切换传输方式，默认使用 TCP 传输。
You also need to rethink how to manage the multi-modal embeddings internally inside vLLM (You might need to introduce "embed blocks" based on hash + position).

注意事项：
你不需要写测试用例，只写核心代码实现，并生成一份 markdown 格式的设计文档，放到当前项目的根目录下。

参考资料：
https://kvcache-ai.github.io/Mooncake/getting_started/supported-protocols.html
https://kvcache-ai.github.io/Mooncake/python-api-reference/transfer-engine.html

---
**第三版：**

请参考 SGLang 项目中 EPD 集成 Mooncake transfer backend 和 Encoder Global Cache Manager 的实现，继续优化当前方案。
Encoder Global Cache Manager, introducing a Mooncake-powered global multimodal embedding cache that enables cross-instance sharing of ViT embeddings to avoid redundant GPU computation.

参考资料：
https://github.com/sgl-project/sglang/pull/12263
https://www.lmsys.org/blog/2026-01-12-epd/
https://github.com/sgl-project/sglang/pull/16137

注意事项：
Do NOT infer missing details beyond what is necessary.

<!--
SGLang introduces Encode-Prefill-Decode (EPD) Disaggregation with Mooncake as a transfer backend. This integration allows decoupling compute-intensive multimodal encoders (e.g., Vision Transformers) from language model nodes, utilizing Mooncake's RDMA engine for zero-copy transfer of large multimodal embeddings.

SGLang merged Encoder Global Cache Manager, introducing a Mooncake-powered global multimodal embedding cache that enables cross-instance sharing of ViT embeddings to avoid redundant GPU computation.
参考资料：
https://github.com/sgl-project/sglang/pull/16137
-->

---
CPU buffer（普通 RDMA）-> DRAM
GPU buffer（GDR：GPU Direct RDMA）-> VRAM

通常在 CPU 上注册 buffer，原因：GPU 显存紧张、需要跨节点共享、更容易管理。

---
ZMQ:

ROUTER/DEALER
PUB/SUB
send_multipart/recv_multipart

ZMQ 的多种消息模式（PUB/SUB、ROUTER/DEALER）分别是什么意思？应用场景分别是什么？send_multipart/recv_multipart有什么用？

Prompt:

请分析 `vllm/distributed/ec_transfer/ec_connector/mooncake/` 目录下所有关于 zmq/msgspec 相关的代码，包括：交互流程、API 用法、为什么要这么用等。
将总结的内容保存为一份 markdown 格式的报告，要求图文并貌，并放到当前项目的根目录下。

---
profiling:

减去 EmbedBlockManager buffer_size

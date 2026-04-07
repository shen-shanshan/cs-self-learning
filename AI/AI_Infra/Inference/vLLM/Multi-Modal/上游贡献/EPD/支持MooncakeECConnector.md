# Support Mooncake Based ECConnector for EPD

## Questions

vllm-ascend KVCache Pool？
Mooncake vs NCCL connector 本质区别？
block table 如何跨节点保持一致？
GPUDirect RDMA 是怎么绕过 CPU page table 的？
为什么 zero-copy 会限制内存管理？
Mooncake 的 session / endpoint 是怎么管理 RDMA 连接的？

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

需求背景：https://github.com/vllm-project/vllm/pull/33714#issuecomment-3882716972
请你参考 vLLM 中 MooncakeConnector 在 PD 分离场景（传输 KV Cache）中的注册和使用方式，实现 MooncakeECConnector，用于支持 EPD 特性下的 Encoder Cache 传输，你只需要考虑 SHM 通信方式，先不要考虑基于 RDMA 的实现。
你不需要写测试用例，只写核心代码实现，并生成一份 markdown 格式的设计文档，放到当前项目的根目录下。

需求背景：“请你参考 vLLM 中 MooncakeConnector 在 PD 分离场景（传输 KV Cache）中的注册和使用方式，实现 MooncakeECConnector，用于支持 EPD 特性下的 Encoder Cache 传输，你只需要考虑 SHM 通信方式，先不要考虑基于 RDMA 的实现。你不需要写测试用例，只写核心代码实现，并生成一份 markdown 格式的设计文档，放到当前项目的根目录下。详情：https://github.com/vllm-project/vllm/pull/33714#issuecomment-3882716972”。

目前，你已经根据需求背景实现了基于 SHM 的 MooncakeECConnector，现在请把 mooncake TransferEngine（只用 TCP）也集成进来，作为 SHM 的 fallback，并更新设计文档。

/plan continue
Continue applying the previous plan. You stopped midway—resume from where you left off without re-planning.

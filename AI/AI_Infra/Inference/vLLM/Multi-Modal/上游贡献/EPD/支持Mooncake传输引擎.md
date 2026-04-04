# Support Mooncake Based ECConnector for EPD

## Background & Motivation

https://github.com/vllm-project/vllm/pull/33714

From my perspective, the SHM connector, while simple and useful, could potentially be subsumed by a more general connector as a fallback mode (for example, in Mooncake). Therefore, at this stage, I prefer to focus on providing **a Mooncake-based ECConnector** implementation that supports multiple transport backends. This would allow us to cover different throughput and deployment requirements in a unified, production-ready transfer stack.

- `ExampleECConnector` can serve as a lightweight implementation for debugging and experimentation.
- `MooncakeECConnector` can serve as the industrial-grade solution for real-world deployments.

One technical difference compared to KV Cache transfer is that **encoder cache tensors do not have fixed, pre-allocated memory addresses**. Because of this, we may need to explicitly **allocate a dedicated transmission buffer for encoder cache transfer**. To keep overall memory usage under control, this buffer space might need to be carved out from the encoder cache memory budget itself, ensuring that total memory consumption remains bounded.

---
**SHM？**

## Mooncake in vLLM

在 vLLM 的 mooncake connector 里，KV cache 是这样处理的：
取每个 tensor 的 `data_ptr`（GPU 地址），传输用的是裸指针（地址）。

远程共享内存系统：
“zero-copy + 远程内存访问”
GPU A memory  <–RDMA–>  GPU B

---
**zero-copy？**

指数据从源头到目的地，中间不经过 CPU 内存的“中转拷贝”。

- 传统路径：GPU A -copy-> CPU 内存 -socket send-> 内核 buffer -网络-> 对端内核 buffer -> CPU 内存 -copy-> GPU B；
- zero-copy：GPU A memory -> GPU B memory，数据不经过 CPU 内存，CPU 只负责“发指令”，数据由 DMA / RDMA NIC / GPU copy engine 直接搬运。

zero-copy 并不是“完全没有 copy”，而是：copy 仍然发生，但不经过 CPU 和额外 buffer。

- DMA（Direct Memory Access）：Device 直接读写内存，不经过 CPU（CPU 不参与数据搬运）；
- RDMA（Remote Direct Memory Access）：机器 A 内存 -> 机器 B 内存，绕过 CPU + kernel；
- GPUDirect：GPU memory -> NIC -> GPU memory。

要实现 zero-copy，必须满足：

1. 内存已注册（memory registration）：GPU memory 被 pin、NIC 知道物理地址；
2. 使用物理地址访问；
3. 硬件支持：RDMA NIC、GPUDirect、PCIe / NVLink。

总结：zero-copy 的本质是让“数据搬运”从 CPU 软件行为，变成硬件 DMA 行为。

> 参考资料：[RDMA blog](https://zhuanlan.zhihu.com/p/648805252)。

---
**为什么必须“固定地址 + 预分配”？**

1️⃣ RDMA / GPU Direct 的核心要求：

Mooncake 底层依赖的机制（典型是 RDMA / CUDA IPC / GPUDirect）有一个硬约束：

1. 必须提前注册 memory region（MR）；
2. 地址在注册后不能变。

因为 NIC / DMA 需要“物理地址映射表”，当你 register memory 时，系统会：

1. 把虚拟地址 → 物理地址固定下来（pin memory）；
2. 建立 NIC 可访问的映射表；
3. 返回一个 key（lkey/rkey）。

NIC 直接 DMA 读写，不经过 CPU / runtime。

2️⃣ 如果地址不固定会发生什么？

旧地址申请 -> 旧地址释放 -> 新地址分配
RDMA 还在用旧地址，从而导致读到脏数据。
所以必须：地址稳定（stable pointer）

3️⃣ 为什么要“提前分配（pre-allocate）？

RDMA register 很贵（甚至是 ms 级），需要：pin memory、更新 page table、通知 NIC。
不能每个请求都 register/unregister
所以设计变成：

1. 启动时：分配一大块 KV cache buffer，register 一次；
2. 运行时：只在这块内存里复用（block / paging）。

4️⃣ Mooncake 的传输模型决定必须用“地址”

Mooncake ≈ “GPU RDMA KV cache 共享”

---
**在 vllm 的 PD 分离场景下，mooncake 是怎么应用的？**

Mooncake 是以 KV Connector 插件的形式接入 vLLM 的（抽象层：KV Connector）。

MooncakeConnector (实现 KVConnector)：

1. 注册 KV cache 内存：在 Prefill / Decode 初始化时，`batch_register_memory(kv_ptrs, kv_sizes)` 把 GPU KV cache 注册成 RDMA 可访问内存，建立 address → rkey 映射；
2. 建立 session（P ↔ D）：管理远程内存权限、建立连接（类似 RDMA QP）；
3. 执行 KV 传输（核心）：`batch_transfer_sync_write(src_ptrs, dst_ptrs, lengths)` 直接把 P 的 GPU 内存 copy 到 D 的 GPU 内存（绕过 CPU）。Decode 侧：`recv_kv(...)`， 没有“接收 buffer copy”这个动作，而是直接写入 Decode 的 KV cache 内存（可以优化为 async pipeline？）。

> Mooncake API:
> [batch-register-memory](https://kvcache-ai.github.io/Mooncake/python-api-reference/transfer-engine.html#batch-register-memory)
> [batch-transfer-sync-write](https://kvcache-ai.github.io/Mooncake/python-api-reference/transfer-engine.html#batch-transfer-sync-write)

KV cache 是 block 化的（PagedAttention）：每个 block → 一个固定地址，远程直接 copy block。
地址是稳定的：vLLM 提前分配 KV cache arena，不做 relocation。
不走“逻辑 KV”，而是“物理 KV”，vLLM 用 block table 维护逻辑关系：request → block_ids → addr。
vLLM 管“索引”，Mooncake 管“内存搬运”。

- 传统方案：KV → serialize → network → deserialize → GPU；
- Mooncake：KV (GPU addr) → RDMA → KV (GPU addr)，少了三层开销：CPU copy、编解码、框架参与。

局限性：

1. 强依赖固定内存布局：无法灵活扩容 KV cache；
2. 同步传输：Prefill / Decode overlap 不充分；
3. 强耦合 block size：不同模型/层不灵活；
4. 不支持 KV compaction：内存碎片问题。

---

Mooncake vs NCCL connector 本质区别？
block table 如何跨节点保持一致？
RDMA 的 lkey / rkey 机制到底怎么工作？
GPUDirect RDMA 是怎么绕过 CPU page table 的？
为什么 zero-copy 会限制内存管理？

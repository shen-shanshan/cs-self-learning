# Support Mooncake Based ECConnector for EPD

## TODO

整理笔记：RDMA Mooncake(看 API) SHM
请介绍 Mooncake store 和 Mooncake transfer engine 的原理是什么？具体的应用场景是什么？

Mooncake 的 session / endpoint 是怎么管理 RDMA 连接的？
Mooncake vs NCCL connector 本质区别？
block table 如何跨节点保持一致？
GPUDirect RDMA 是怎么绕过 CPU page table 的？
为什么 zero-copy 会限制内存管理？
pickle？

## Background & Motivation

https://github.com/vllm-project/vllm/pull/33714

From my perspective, the SHM connector, while simple and useful, could potentially be subsumed by a more general connector as a fallback mode (for example, in Mooncake). Therefore, at this stage, I prefer to focus on providing **a Mooncake-based ECConnector** implementation that supports multiple transport backends. This would allow us to cover different throughput and deployment requirements in a unified, production-ready transfer stack.

- `ExampleECConnector` can serve as a lightweight implementation for debugging and experimentation.
- `MooncakeECConnector` can serve as the industrial-grade solution for real-world deployments.

One technical difference compared to KV Cache transfer is that **encoder cache tensors do not have fixed, pre-allocated memory addresses**. Because of this, we may need to explicitly **allocate a dedicated transmission buffer for encoder cache transfer**. To keep overall memory usage under control, this buffer space might need to be carved out from the encoder cache memory budget itself, ensuring that total memory consumption remains bounded.

---
**SHM？**

Shared Memory = 多个进程/设备可以“直接访问同一块内存”的机制。

- 普通进程通信（非 SHM）：进程 A → 拷贝 → 内核 → 拷贝 → 进程 B（数据被复制多次）；
- Shared Memory：进程 A ↔ 同一块内存 ↔ 进程 B（没有中间 copy）。

SHM 在不同层的含义：

- 操作系统：POSIX / System V SHM。OS 分配一块内存，映射到多个进程的虚拟地址空间。特点：零拷贝（进程间）、需要同步机制（锁、信号量）。比如：`from multiprocessing import shared_memory`；
- GPU / CUDA：GPU SM 内部的片上共享内存，给 thread block 用。特点：超快（比 global memory 快很多）。比如：`__shared__ float buf[256]`；
- 容器 / Docker：是一个 tmpfs（内存文件系统），用来做进程间共享内存。比如 `docker run --shm-size=8g`，对应 `/dev/shm`。

在 vLLM / Mooncake 里的 SHM：跨进程 / 跨 worker 的共享内存，用来避免 KV cache 或 metadata 的复制。

如果在同一台机器上：KV cache → 放在 shared memory。Prefill worker 写，Decode worker 直接读。

|     | SHM    | Mooncake    |
| --- | ------ | ----------- |
| 范围  | 单机     | 跨机器         |
| 机制  | 共享内存映射 | RDMA        |
| 地址  | 虚拟地址映射 | 物理地址 + rkey |
| 拷贝  | 0 copy | 0 copy      |
| 复杂度 | 低      | 高           |

SHM 为什么快？

- 避免数据复制；
- 避免内核参与。

因此：延迟低、带宽高、CPU 占用低。

SHM 的代价？

- 需要同步：两个进程同时写 → 数据竞争；
- 生命周期管理复杂：谁分配？谁释放？
- 内存不可随意移动：地址被多个进程引用，不能 realloc / compact。

总结：SHM / RDMA / Mooncake 本质都是让“多个执行单元共享同一块物理数据”，而不是复制数据。

## Mooncake in vLLM & RDMA 原理

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
**RDMA 的内存注册是什么意思？**

内存注册 = 把一段内存“锁定 + 映射 + 授权”给网卡（NIC），让它可以直接通过 RDMA 访问这段内存。

网卡（NIC）怎么知道你这块内存在哪？

- 普通程序看到的是：虚拟地址（virtual address）；
- 但 NIC 做 DMA 时需要的是：物理地址（physical address）。

CPU 有 page table（虚拟 → 物理），而 NIC 没有！-> 需要内存注册

内存注册到底做了什么？

1. 固定内存（pin / lock）：这块内存不能被换出（swap）、不能被移动；
2. 建立地址映射（给 NIC 用）：虚拟地址 → 物理页 → DMA 映射表，并把这份 mapping 下发给 NIC；
3. 生成访问凭证（lkey / rkey）：注册成功后会得到 lkey（local key）和 rkey（remote key），用于权限控制、指示 NIC 可以访问哪块内存。

RDMA 访问时发生什么？

1. NIC（本机）；
2. 用 rkey 验证权限；
3. 根据 addr 查 mapping；
4. DMA 读取远端物理内存。

整个过程不经过远端 CPU、不经过远端 kernel。

为什么必须“提前注册”？

注册非常昂贵（μs～ms级），需要：pin 内存、建 page table 映射、通知 NIC。

所以实际系统（包括 vLLM + Mooncake）都会在启动时分配一大块 buffer，注册一次，后续反复复用这块内存。

为什么地址必须“固定”？

注册之后：(addr, length, rkey) → 绑定关系

如果 free / realloc / 移动内存，NIC 还在用旧 mapping，直接读错数据 or crash。

**RDMA 的“内存注册”本质是：把一段内存从“普通进程内存”升级为“网卡可直接访问的 DMA 内存”，从而实现真正的 zero-copy 远程访问。**

---
**RDMA 的 lkey / rkey 机制到底怎么工作？**

lkey / rkey = 网卡访问这块内存的“通行证 + 地址翻译入口”。

lkey / rkey 是 memory region（MR）的访问 key，NIC 用它来：校验权限 + 定位内存映射。

```
MR table（在 NIC / 驱动中）：

key        →   (addr, length, permissions, page_table)
----------------------------------------------------
lkey       →   本地访问
rkey       →   远程访问
```

lkey 是干嘛的？（本地用）

用于本机发起 RDMA 操作，此时 NIC 做什么？

1. 查 MR table：这个 lkey 是否存在？
2. 校验权限（读/写）；
3. 确认 addr 是否在合法范围内；
4. 用 page_table 做 DMA。

lkey = 告诉 NIC：“这块本地内存是合法可 DMA 的”。

rkey 是干嘛的？（远程用）

用于远程机器访问你的内存，你这边的 NIC 收到请求后：

1. 用 rkey 查 MR table；
2. 检查权限（是否允许 remote read/write）；
3. 校验 addr 是否越界；
4. 执行 DMA（完全绕过 CPU）。

rkey = 远程访问你内存的“能力令牌（capability token）”。

NIC 不认识虚拟地址！

- NIC 只能做：物理地址 + DMA；
- 但你传的是：virtual_addr + key。

所以 lkey / rkey 的真正作用是：

1. (key, virtual_addr)；
2. NIC 查表；
3. 找到 page table；
4. 转成 physical address；
5. DMA。

为什么需要 key，而不是直接地址？

- 如果只用地址，远端可以读你任意内存，不安全；
- 加上 rkey：只有拿到 rkey 才能访问。

**lkey / rkey 本质是：让 NIC 在“没有 CPU / page table 参与”的情况下，既能安全地验证访问权限，又能完成虚拟地址 → 物理地址的转换，从而实现真正的 zero-copy RDMA 访问。**

---
**在 vLLM 的 PD 分离场景下，Mooncake 是怎么应用的？**

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

## 参考资料

- [一文读懂｜RDMA 原理](https://zhuanlan.zhihu.com/p/648805252)

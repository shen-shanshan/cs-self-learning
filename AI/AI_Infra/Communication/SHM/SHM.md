# SHM

**Shared Memory = 多个进程/设备可以“直接访问同一块内存”的机制。**

- 普通进程通信（非 SHM）：进程 A → 拷贝 → 内核 → 拷贝 → 进程 B（数据被复制多次）；
- Shared Memory：进程 A ↔ 同一块内存 ↔ 进程 B（没有中间 copy）。

**SHM 在不同层的含义：**

- **操作系统**：POSIX / System V SHM。OS 分配一块内存，映射到多个进程的虚拟地址空间。特点：零拷贝（进程间）、需要同步机制（锁、信号量）。比如：`from multiprocessing import shared_memory`；
- **GPU / CUDA**：GPU SM 内部的片上共享内存，给 thread block 用。特点：超快（比 global memory 快很多）。比如：`__shared__ float buf[256]`；
- **容器 / Docker**：是一个 tmpfs（内存文件系统），用来做进程间共享内存。比如 `docker run --shm-size=8g`，对应 `/dev/shm`。

在 vLLM / Mooncake 里的 SHM：跨进程 / 跨 worker 的共享内存，用来避免 KV cache 或 metadata 的复制。

如果在同一台机器上：KV cache → 放在 shared memory。Prefill worker 写，Decode worker 直接读。

||SHM|Mooncake|
|---|---|---|
|范围|单机|跨机器|
|机制|共享内存映射|RDMA|
|地址|虚拟地址映射|物理地址 + rkey|
|拷贝|0 copy|0 copy|
|复杂度|低|高|

**SHM 为什么快？**

- 避免数据复制；
- 避免内核参与。

因此：延迟低、带宽高、CPU 占用低。

**SHM 的代价？**

- 需要同步：两个进程同时写 → 数据竞争；
- 生命周期管理复杂：谁分配？谁释放？
- 内存不可随意移动：地址被多个进程引用，不能 realloc / compact。

**总结：SHM / RDMA / Mooncake 本质都是让“多个执行单元共享同一块物理数据”，而不是复制数据。**

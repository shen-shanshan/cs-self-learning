# SGLang MooncakeStore 架构分析与 EPD 场景解析

> 源码路径：`python/sglang/srt/mem_cache/storage/mooncake_store/`  
> 生成日期：2026-04-09

---

## 目录

1. [背景概览](#1-背景概览)
2. [目录结构与文件职责](#2-目录结构与文件职责)
3. [核心类架构图](#3-核心类架构图)
4. [技术原理深度解析](#4-技术原理深度解析)
5. [HiCache 三级存储体系](#5-hicache-三级存储体系)
6. [MooncakeStore 核心工作流](#6-mooncakestore-核心工作流)
7. [EPD 场景使用详解](#7-epd-场景使用详解)
8. [配置与部署](#8-配置与部署)
9. [性能优势分析](#9-性能优势分析)
10. [总结](#10-总结)

---

## 1. 背景概览

### 1.1 什么是 Mooncake

Mooncake 是一个面向大语言模型推理加速的分布式 KV 缓存系统，其核心目标是：

- 将集群中多台机器的 DRAM/SSD 聚合为一个大型高速缓存池
- 利用 **(GPUDirect) RDMA** 技术实现零拷贝的高带宽数据传输
- 最大化利用单机多网卡资源，降低跨节点传输延迟

### 1.2 为什么 SGLang 需要 MooncakeStore

SGLang 内置了 HiCache 分层缓存体系（L1 GPU VRAM → L2 CPU DRAM → L3 远端存储）。对于长上下文、多轮对话和高并发场景，L1/L2 很快达到容量瓶颈。MooncakeStore 作为 L3 存储后端，提供了近乎无限的、可集群内共享的 KV 缓存空间。

在 EPD（Encode-Prefill-Decode）三阶段解耦架构中，MooncakeStore 还承担了另一个关键角色：**跨节点多模态 Embedding 缓存**，避免重复的 ViT 前向计算。

---

## 2. 目录结构与文件职责

```
python/sglang/srt/mem_cache/storage/mooncake_store/
├── mooncake_store.py             # KV 缓存存储核心（L3 backend）
├── mooncake_embedding_store.py   # 多模态 Embedding 分布式存储
├── embedding_cache_controller.py # Embedding 缓存控制器（多级缓存调度）
├── test_mooncake_store.py        # 接口功能测试
└── README.md                     # 官方部署文档
```

| 文件 | 核心类 | 职责 |
|------|--------|------|
| `mooncake_store.py` | `MooncakeStore`, `MooncakeBaseStore`, `MooncakeHostTensorAllocator`, `MooncakeStoreConfig` | HiCache L3 KV 缓存后端，实现 KV page 的分布式存取 |
| `mooncake_embedding_store.py` | `MooncakeEmbeddingStore` | 多模态 Embedding 向量的分布式 put/get |
| `embedding_cache_controller.py` | `EmbeddingCacheController`, `ContiguousMemoryAllocator`, `EmbeddingPrefetchOperation`, `EmbeddingInsertOperation` | 多级 Embedding 缓存控制，包含本地 pinned buffer 管理和后台异步 I/O |

---

## 3. 核心类架构图

### 3.1 类继承与依赖关系

```mermaid
classDiagram
    class HiCacheStorage {
        <<abstract>>
        +register_mem_pool_host()
        +batch_get_v1()
        +batch_set_v1()
        +batch_exists()
        +get()
        +set()
        +batch_get()
        +batch_set()
        +exists()
        +get_stats()
    }

    class MooncakeBaseStore {
        +store: MooncakeDistributedStore
        +config: MooncakeStoreConfig
        +_import_mooncake_store()
        +_load_config()
        +register_buffer(tensor)
    }

    class MooncakeStore {
        +mem_pool_host: HostKVCache
        +is_mla_backend: bool
        +split_factor: int
        +mha_suffix / mla_suffix: str
        +batch_get_v1()
        +batch_set_v1()
        +batch_exists()
        +_put_batch_zero_copy_impl()
        +_get_batch_zero_copy_impl()
        +_batch_exist()
        +get_stats()
    }

    class MooncakeEmbeddingStore {
        +batch_get()
        +batch_put()
        +batch_is_exist()
        +get_key(image_hash)
    }

    class MooncakeHostTensorAllocator {
        +allocator: MooncakeHostMemAllocator
        +allocate(dims, dtype, device)
    }

    class HostTensorAllocator {
        <<abstract>>
        +allocate()
    }

    class MooncakeStoreConfig {
        +local_hostname: str
        +metadata_server: str
        +global_segment_size: int
        +protocol: str
        +device_name: str
        +master_server_address: str
        +from_file()$
        +load_from_env()$
        +load_from_extra_config()$
    }

    class EmbeddingCacheController {
        +mooncake_store: MooncakeEmbeddingStore
        +cpu_pool: Tensor (pinned)
        +allocator: ContiguousMemoryAllocator
        +prefetch_queue: Queue
        +insert_queue: Queue
        +prefetch()
        +insert_batch()
        +check_prefetch_progress()
        +get_embeddings()
        +batch_is_exist()
    }

    class ContiguousMemoryAllocator {
        +total_size: int
        +free_blocks: list
        +allocate(size_bytes)
        +free(offset, size_bytes)
    }

    HiCacheStorage <|-- MooncakeStore
    MooncakeBaseStore <|-- MooncakeStore
    MooncakeBaseStore <|-- MooncakeEmbeddingStore
    HostTensorAllocator <|-- MooncakeHostTensorAllocator
    MooncakeStore ..> MooncakeStoreConfig : uses
    MooncakeStore ..> MooncakeHostTensorAllocator : uses (standalone mode)
    EmbeddingCacheController *-- MooncakeEmbeddingStore
    EmbeddingCacheController *-- ContiguousMemoryAllocator
```

### 3.2 存储后端注册关系

```mermaid
graph LR
    Factory["StorageBackendFactory"] -->|register 'mooncake'| MS["MooncakeStore"]
    Factory -->|register 'file'| FS["HiCacheFile"]
    Factory -->|register 'nixl'| NS["HiCacheNixl"]
    Factory -->|register 'hf3fs'| HS["HiCacheHF3FS"]
    Factory -->|register 'eic'| ES["EICStorage"]

    HiCache["HiRadixCache\n(HiCache 控制层)"] -->|create_backend| Factory
    HiCache -->|注册 mem_pool| MS
```

---

## 4. 技术原理深度解析

### 4.1 RDMA 零拷贝传输

传统 TCP/IP 数据传输路径：

```
CPU Memory → Kernel Buffer → Network Stack → NIC → 网络 → 对端 NIC → Kernel Buffer → CPU Memory
                ↑ 多次数据拷贝，占用大量 CPU 资源
```

RDMA 零拷贝传输路径：

```
CPU Memory (已注册 RDMA MR) ──────────────────────────────→ 对端 CPU Memory
                               NIC 直接 DMA，无需 CPU 介入
```

MooncakeStore 中零拷贝的关键代码路径：

```python
# 1. 分配 Mooncake 管理的宿主内存（自动完成 RDMA 注册）
class MooncakeHostTensorAllocator(HostTensorAllocator):
    def allocate(self, dims, dtype, device):
        ptr_int = self.allocator.alloc(size)          # 底层调用 mooncake 分配器
        c_array = ctypes.c_byte * size .from_address(ptr_int)
        tensor = torch.frombuffer(c_array, ...)       # 包装为 PyTorch tensor，零拷贝
        return tensor

# 2. 注册缓冲区到 MooncakeDistributedStore
def register_mem_pool_host(self, mem_pool_host):
    buffer = self.mem_pool_host.kv_buffer
    self.store.register_buffer(buffer.data_ptr(), buffer.numel() * buffer.element_size())

# 3. 零拷贝 put/get（直接操作内存指针）
def _put_batch_zero_copy_impl(self, key_strs, buffer_ptrs, buffer_sizes):
    return self.store.batch_put_from(key_strs, buffer_ptrs, buffer_sizes)

def _get_batch_zero_copy_impl(self, key_strs, buffer_ptrs, buffer_sizes):
    return self.store.batch_get_into(key_strs, buffer_ptrs, buffer_sizes)
```

### 4.2 MooncakeDistributedStore 架构

Mooncake 后端由三类服务构成：

```mermaid
graph TB
    subgraph Mooncake集群
        M["Master Service\n(元数据 + 空间管理)"]
        MD["Metadata Service\n（可选，或P2P握手）"]
        S1["Store Service 1\n(贡献内存到全局池)"]
        S2["Store Service 2"]
        SN["Store Service N"]
    end

    subgraph SGLang节点
        SGL1["SGLang Server\n(Mooncake Client)"]
        SGL2["SGLang Server\n(Mooncake Client)"]
    end

    M <-->|"分配/释放空间\n驱逐策略"| S1
    M <-->|分配/释放空间| S2
    M <-->|分配/释放空间| SN
    MD <-->|连接握手元数据| SGL1
    MD <-->|连接握手元数据| SGL2

    SGL1 <-->|"RDMA 直传\n(bypass master)"| S1
    SGL1 <-->|RDMA 直传| S2
    SGL2 <-->|RDMA 直传| SN
    SGL1 <-->|"KV 共享\n(RDMA)"| SGL2
```

> **关键点**：数据传输直接发生在 SGLang 节点与 Store Service 之间，Master Service 只负责空间的元数据管理，不经过数据平面，确保高吞吐。

### 4.3 KV 缓存 Key 命名规则

MooncakeStore 通过 key 的命名约定区分不同的 TP rank、PP rank 和注意力头：

```python
# MHA (Multi-Head Attention) 模式 —— 每个 TP rank 分别存 K 和 V
key_{tp_rank}_k      # Key 分量
key_{tp_rank}_v      # Value 分量

# MHA + Pipeline Parallelism
key_{tp_rank}_{pp_rank}_k
key_{tp_rank}_{pp_rank}_v

# MLA (Multi-head Latent Attention, e.g. DeepSeek) 模式 —— 只有一个潜变量
key__{pp_rank}_k     # 合并的 KV 潜空间

# Split-Heads 模式（TP LCM 扩展）
key_{base_rank+i}_{pp_rank}_k  for i in range(split_factor)
```

### 4.4 ContiguousMemoryAllocator（Embedding 存储的首次适配分配器）

```
初始状态: [  free_block(0, total_size)  ]

alloc(A):  [A | free_block(A_size, total-A_size)]
alloc(B):  [A | B | free_block(A_size+B_size, total-A-B_size)]
free(A):   merge adjacent free blocks → 内存碎片整理
```

采用首次适配（First-Fit）策略，`free()` 时合并相邻空闲块防止碎片化，配合线程锁保证并发安全。

---

## 5. HiCache 三级存储体系

```mermaid
graph TB
    subgraph "L1 — GPU VRAM（最快，容量最小）"
        GPU["GPU KV Cache Pool\n(MHATokenToKVPool / MLATokenToKVPool)"]
    end

    subgraph "L2 — CPU DRAM（快，中等容量）"
        CPU["Host KV Cache Pool\n(MHATokenToKVPoolHost)\n默认 2× GPU VRAM 大小"]
    end

    subgraph "L3 — 分布式 Mooncake（慢但近乎无限）"
        MC["MooncakeStore\n(RDMA 零拷贝，跨节点共享)"]
    end

    REQ["新请求"] -->|"前缀命中查询\n(HiRadixCache)"| GPU
    GPU -->|L1 miss| CPU
    CPU -->|L2 miss，prefetch| MC
    GPU -->|"evict (write-through/write-back)"| CPU
    CPU -->|"evict (backup)"| MC
    MC -->|"load back"| CPU
    CPU -->|"load back"| GPU
```

**HiCacheController** 负责调度以上所有层间的异步数据迁移：

- **write-through**: 写入 L1 同时写入 L2/L3
- **write-back**: eviction 触发时写入 L2/L3
- **prefetch**: 请求到来前提前从 L3 加载到 L2

---

## 6. MooncakeStore 核心工作流

### 6.1 初始化流程

```mermaid
flowchart TD
    A["MooncakeStore.__init__()"] --> B["_import_mooncake_store()"]
    B --> C["MooncakeDistributedStore()"]
    C --> D["_load_config()\n优先级: extra_config > JSON文件 > 环境变量"]
    D --> E{standalone_storage?}
    E -->|Yes| F["store.setup_dummy()\n通过本地 RPC 连接独立 Store Service"]
    E -->|No| G{"共享 TransferEngine?"}
    G -->|Yes, 配置相同| H["复用已有 mooncake_transfer_engine"]
    G -->|No| I["创建新 transfer_engine"]
    H --> J["store.setup()"]
    I --> J
    F --> K["warmup()\n写入+读取4KB数据验证连通性"]
    J --> K
    K --> L["register_mem_pool_host()\n注册 HostKVCache buffer 到 RDMA"]
    L --> M["Ready"]
```

### 6.2 batch_set（KV 写入 L3）完整流程

```mermaid
flowchart TD
    A["batch_set_v1(keys, host_indices)"] --> B["_batch_preprocess()\n生成 key_strs, buffer_ptrs, buffer_sizes"]
    B --> C{is_mla_backend?}
    C -->|Yes| D["_get_mla_buffer_meta()\n每个 key 对应一个 KV 合并分量"]
    C -->|No| E{should_split_heads?}
    E -->|Yes| F["_get_mha_split_heads_buffer_meta()\n按 split_factor 展开为多个 rank 的 key"]
    E -->|No| G["_get_mha_buffer_meta()\n每 key → k + v 两个分量"]
    D --> H["_batch_exist(key_strs)\n批量检查哪些 key 已存在"]
    F --> H
    G --> H
    H --> I["过滤出未存在的 key\n跳过已有数据（幂等写入）"]
    I --> J["_put_batch_zero_copy_impl()\nbatch_put_from(ptrs, sizes)\nRDMA 零拷贝写入 Mooncake"]
    J --> K["_batch_postprocess()\n解析每对 k/v 的成功状态"]
    K --> L["返回 List[bool]"]
```

### 6.3 batch_get（KV 从 L3 读取）完整流程

```mermaid
flowchart TD
    A["batch_get_v1(keys, host_indices)"] --> B["_batch_preprocess()"]
    B --> C["_get_batch_zero_copy_impl()\nbatch_get_into(ptrs, sizes)\nRDMA 零拷贝读入 HostKVCache"]
    C --> D["_batch_postprocess()\n检查返回字节数 > 0 表示成功"]
    D --> E["返回 List[bool]"]

    B2["batch_exists(keys)"] --> F["构造 query_keys（加 _k/_v 后缀）"]
    F --> G["_batch_exist(query_keys)\nbatch_is_exist()"]
    G --> H["返回连续命中页数（整数）"]
```

---

## 7. EPD 场景使用详解

EPD = **Encode-Prefill-Decode**，是 SGLang 针对多模态大模型的三阶段计算解耦架构：

```mermaid
graph LR
    subgraph "Encode 节点\n(MMEncoder)"
        ViT["视觉/音频编码器\n(ViT / Audio Encoder)"]
        EC["EmbeddingCacheController\n(本地 pinned buffer + Mooncake 全局缓存)"]
    end
    subgraph "Prefill 节点\n(PrefillWorker)"
        PF["LLM Prefill\n(Attention + FFN)"]
        HC["HiRadixCache (HiCache)\n(L1/L2/L3 KV 缓存)"]
        MS["MooncakeStore\n(L3 KV 缓存后端)"]
    end
    subgraph "Decode 节点\n(DecodeWorker)"
        DC["LLM Decode\n(KV Cache 复用)"]
    end

    USER["用户请求\n(含图/音)"] -->|路由| EncodeNode
    ViT -->|"Embedding 向量\n(RDMA 传输)"| PF
    EC <-->|"RDMA put/get\n(MooncakeEmbeddingStore)"| MCCluster["Mooncake 集群\n分布式内存池"]
    HC <-->|"KV page put/get\n(MooncakeStore)"| MCCluster
    PF -->|"KV Transfer\n(Mooncake RDMA)"| DC
    DC -->|生成结果| USER
```

### 7.1 EPD 场景一：多模态 Embedding 全局缓存

**问题**：同一张图片可能被多个请求重复编码，ViT 前向计算代价极高（数百 ms）。

**解决方案**：`EmbeddingCacheController` + `MooncakeEmbeddingStore` 实现两级 Embedding 缓存：

```
Level 1（本地内存）：当前节点 pinned CPU buffer（4GB 可配）
Level 2（全局 Mooncake）：跨节点分布式 Embedding 存储
```

**完整请求处理流程**：

```mermaid
sequenceDiagram
    participant User as 用户请求
    participant Enc as MMEncoder (rank 0)
    participant Local as 本地 hash_to_metadata
    participant MC as Mooncake 集群
    participant ViT as ViT 模型

    User->>Enc: 含图/音的多模态请求

    Note over Enc: Step 1: 批量检查缓存命中
    Enc->>Local: batch_is_exist(image_hashes)
    alt 本地命中
        Local-->>Enc: hit = True (无需网络)
    else 本地未命中
        Enc->>MC: batch_is_exist(missing_hashes) [async]
        MC-->>Enc: 全局命中情况
    end
    Note over Enc: Rank 0 广播命中 mask 到所有 TP ranks

    Note over Enc: Step 2: 所有 TP ranks 并行计算 Cache Miss
    Enc->>ViT: _encode_missing(missing_indices)
    ViT-->>Enc: new_embedding_slices

    Note over Enc: Step 3: Rank 0 预取 Cache Hit 的 Embedding
    Enc->>MC: prefetch(hit_hashes, ...) → 放入 prefetch_queue
    Note over Enc: 后台 IO 线程执行 batch_get_into (RDMA)
    MC-->>Enc: embeddings 写入 pinned cpu_pool

    Note over Enc: Step 4: TP 同步预取状态
    Enc->>Enc: broadcast prefetch_status

    Note over Enc: Step 5: 组装最终 Embedding
    Enc->>Enc: cat(cached_slices + new_slices)

    Note over Enc: Step 6: 后台异步写入新 Embedding 到 Mooncake
    Enc->>MC: insert_batch(new_hashes, ptrs, sizes) [background]

    Enc->>User: 返回 mm_embedding → 传给 Prefill 节点
```

**关键实现细节**：

```python
class EmbeddingCacheController:
    def __init__(self, ...):
        # 1. Mooncake 后端 + 固定大小的 pinned 内存池
        self.mooncake_store = MooncakeEmbeddingStore()
        self.cpu_pool = torch.empty(total_pool_size_bytes, dtype=torch.uint8, pin_memory=True)
        self.mooncake_store.register_buffer(self.cpu_pool)  # RDMA 注册
        
        # 2. 变长 Embedding 的内存管理
        self.allocator = ContiguousMemoryAllocator(total_pool_size_bytes)
        self.hash_to_metadata = {}  # hash → (offset, num_tokens, dim, size)
        
        # 3. 后台 IO 线程（一个线程同时处理 GET 和 PUT 队列）
        self.io_thread = threading.Thread(target=self._io_loop, daemon=True)
        self.io_thread.start()
```

### 7.2 EPD 场景二：PD 解耦下的 KV Cache 共享（HiCache L3）

**问题**：Prefill 节点计算了大量 KV cache，但节点间无法共享，导致相同前缀被反复计算。

**解决方案**：在 Prefill Worker 上启用 HiCache + Mooncake 后端，将 KV cache 写入 Mooncake 全局池供集群内所有 SGLang 实例复用。

```mermaid
sequenceDiagram
    participant Router as Router
    participant P1 as Prefill Worker 1\n(HiCache enabled)
    participant P2 as Prefill Worker 2\n(HiCache enabled)
    participant MC as Mooncake 集群
    participant D as Decode Worker

    Note over P1: 请求 A，新前缀 [sys_prompt + user_turn_1]
    Router->>P1: 请求 A
    P1->>P1: HiCache miss → 全量计算 KV
    P1->>MC: batch_set_v1(keys, host_indices)\n[write-through / write-back]

    Note over P2: 请求 B，相同前缀 [sys_prompt + user_turn_1]
    Router->>P2: 请求 B
    P2->>P2: L1/L2 miss
    P2->>MC: batch_exists(keys) → 命中!
    P2->>MC: batch_get_v1(keys, host_indices)\n[RDMA 零拷贝加载到 CPU DRAM]
    MC-->>P2: KV pages → CPU DRAM (L2)
    P2->>P2: transfer L2→L1 (GPU)
    P2->>D: KV Transfer (RDMA)
    D->>D: Decode 阶段
```

**参数示例**（README 中的 Prefill Worker 启动命令）：

```bash
MOONCAKE_MASTER=127.0.0.1:50051 \
MOONCAKE_PROTOCOL="rdma" \
MOONCAKE_GLOBAL_SEGMENT_SIZE=4294967296 \
python -m sglang.launch_server \
    --model-path [model_path] \
    --enable-hierarchical-cache \
    --hicache-storage-backend mooncake \
    --hicache-storage-prefetch-policy timeout \
    --disaggregation-mode prefill \
    --port 30000
```

### 7.3 EPD 场景三：Expert Parallelism（MooncakeEPDispatcher）

**注意**：这是 Mooncake 在 SGLang 中的第三个用途，使用的是 `mooncake_ep_buffer`（非本目录的 mooncake_store），专为 MoE（Mixture-of-Experts）模型的 Expert Parallelism token 调度设计：

```python
# python/sglang/srt/layers/moe/token_dispatcher/mooncake.py
from mooncake.mooncake_ep_buffer import Buffer

class MooncakeEPDispatcher(BaseDispatcher):
    """使用 Mooncake Buffer 进行 low-latency EP token dispatch"""
```

这属于 EP（Expert Parallelism）而非 KV 存储范畴，与本目录代码独立。

---

## 8. 配置与部署

### 8.1 部署组件关系图

```mermaid
graph TB
    subgraph 基础设施层
        MASTER["mooncake_master\n(空间管理 + 驱逐)"]
        META["http_metadata_server\n(可选，或 P2PHANDSHAKE)"]
        STORE["mooncake_store_service\n(贡献内存到池, 可选)"]
    end
    subgraph SGLang 层
        SGL["SGLang Server\n(MooncakeStore client)\n也可同时作为 store service"]
    end
    subgraph Standalone 模式（实验性）
        CLIENT["mooncake_client\n(Real Client, 管理 RDMA 和内存)"]
        DUMMY["SGLang Server\n(Dummy Client, 通过 RPC 连接)"]
    end

    MASTER <-->|分配/驱逐| STORE
    MASTER <-->|分配/驱逐| SGL
    META <-->|握手元数据| SGL
    SGL <-->|"RDMA 零拷贝"| STORE
    MASTER <-->|分配/驱逐| CLIENT
    CLIENT <-->|"RPC/IPC"| DUMMY
```

### 8.2 配置优先级

```
1. --hicache-storage-backend-extra-config 中指定 master_server_address/client_server_address
        ↓（未设置时）
2. SGLANG_HICACHE_MOONCAKE_CONFIG_PATH 指定的 JSON 文件
        ↓（未设置时）
3. 环境变量（MOONCAKE_MASTER, MOONCAKE_PROTOCOL, MOONCAKE_DEVICE, ...）
```

### 8.3 TP 并行下的内存分配

当 `tp_size > 1` 时，每个 TP rank 独立初始化一个 `MooncakeStore` 实例，各自贡献 `global_segment_size / tp_size` 的内存到全局池：

```python
per_tp_global_segment_size = self.config.global_segment_size // tp_scale_factor
ret_code = self.store.setup(..., per_tp_global_segment_size, ...)
```

这使得总贡献内存量等于 `global_segment_size`，不会因 TP 增大而成倍增长。

---

## 9. 性能优势分析

### 9.1 KV Cache L3 存储优势

| 特性 | 传统方案 | MooncakeStore |
|------|---------|---------------|
| 存储范围 | 单节点 GPU VRAM | 集群级分布式内存池 |
| 传输方式 | PCIe（CPU DRAM） | RDMA 零拷贝 |
| 跨实例共享 | 不支持 | 支持（hash key 共享） |
| 写入开销 | — | 幂等写入（已存在则跳过） |
| TP 感知 | — | 自动按 rank 分片存储 |
| MLA/MHA 支持 | — | 自适应（不同 key 后缀） |

### 9.2 Embedding 缓存优势（EPD 场景）

| 场景 | 无缓存 | 有 MooncakeEmbeddingStore |
|------|--------|--------------------------|
| 相同图片第 N 次请求 | 每次 ViT forward（~100-500ms） | RDMA 读取（~μs 级） |
| 跨节点 Embedding 共享 | 不支持 | 支持 |
| 内存效率 | 每次 GPU 计算 | CPU pinned buffer 复用 |
| TP 一致性 | 需要每 rank 各自计算 | Rank 0 预取，广播状态同步 |

### 9.3 关键路径延迟对比（理论值）

```
ViT Forward Pass (图像编码):
  └── ~200ms-500ms (取决于图像尺寸和模型大小)

MooncakeStore RDMA 读取（100KB KV page）:
  └── ~10-50μs (RDMA 延迟)
  └── 带宽：~100 GB/s (IB HDR/NDR)

Mooncake Embedding RDMA 读取（1MB embedding）:
  └── ~50-200μs
```

### 9.4 零拷贝的内存节省

```
传统路径（batch_put 需要中间 buffer）：
GPU VRAM → CPU DRAM (copy A) → Local Buffer (copy B) → Network

MooncakeStore 零拷贝路径：
CPU DRAM (已 RDMA 注册) ──────RDMA──────→ 远端节点

节省：
- 减少 1 次 CPU-side 内存拷贝
- 无需 kernel buffer 参与
- 释放 CPU cycles 用于计算
```

---

## 10. 总结

### 10.1 架构总览图

```mermaid
graph TB
    subgraph "SGLang 推理引擎"
        direction TB
        LLM["LLM Forward\n(Prefill + Decode)"]
        HRC["HiRadixCache\n(前缀感知 KV 管理)"]
        CC["HiCacheController\n(异步 I/O 调度)"]
        L1["L1: GPU VRAM\nMHATokenToKVPool"]
        L2["L2: CPU DRAM\nMHATokenToKVPoolHost"]
        MS["L3: MooncakeStore\nHiCacheStorage"]
        ENC["MMEncoder\n(EPD Encode 节点)"]
        EC["EmbeddingCacheController\n(多级 Embedding 缓存)"]
        MES["MooncakeEmbeddingStore"]
    end

    subgraph "Mooncake 集群"
        MCM["Master Service"]
        MCDM["Metadata Service"]
        MCP1["Store Service 1"]
        MCP2["Store Service 2"]
    end

    LLM <-->|"读写 KV"| L1
    HRC -->|"evict/load"| CC
    CC <-->|"GPU↔CPU 传输"| L1
    CC <-->|"L2↔L3 传输"| L2
    CC <-->|"batch_set/batch_get\n(RDMA 零拷贝)"| MS
    MS <-->|"RDMA"| MCP1
    MS <-->|"RDMA"| MCP2
    MCM -->|"管理"| MCP1
    MCM -->|"管理"| MCP2
    MCDM <-->|"握手"| MS

    ENC -->|"图像/音频"| EC
    EC -->|"batch_get/put"| MES
    MES <-->|"RDMA"| MCP1
    EC -->|"Embedding 向量"| LLM
```

### 10.2 mooncake_store 目录的两大核心价值

| 用途 | 类 | 场景 | 核心收益 |
|------|-----|------|---------|
| **KV Cache L3 后端** | `MooncakeStore` | PD 解耦 / 长上下文 / 高并发 | 突破单节点内存限制，跨实例 KV 共享，RDMA 高带宽 |
| **Embedding 全局缓存** | `MooncakeEmbeddingStore` + `EmbeddingCacheController` | EPD 多模态 | 消除重复 ViT 计算，跨节点 Embedding 共享，后台异步 I/O |

### 10.3 设计亮点

1. **零拷贝优先**：通过 `MooncakeHostTensorAllocator` 将 CPU DRAM 直接纳入 RDMA 管理，所有数据传输均通过指针完成，无额外内存拷贝。

2. **幂等写入**：`batch_set` 先检查 key 是否存在，已存在则跳过写入，避免重复写入和竞争条件。

3. **TP/PP 感知的命名空间**：Key 后缀自动编码 tp_rank、pp_rank 信息，不同并行配置的模型可以在同一 Mooncake 集群中共存而不冲突。

4. **异步后台 I/O**：`EmbeddingCacheController` 使用独立后台线程处理 prefetch_queue 和 insert_queue，不阻塞主请求处理流程。

5. **多级降级容错**：Embedding 缓存支持完整降级链路（全局缓存命中 → 本地缓存命中 → ViT 重算），预取超时自动回退到 ViT forward，系统不会因缓存问题崩溃。

6. **共享 TransferEngine 复用**：当 PD 解耦模式和 HiCache 均使用 Mooncake 时，自动检测并复用已初始化的 `MooncakeTransferEngine`，减少重复初始化开销。

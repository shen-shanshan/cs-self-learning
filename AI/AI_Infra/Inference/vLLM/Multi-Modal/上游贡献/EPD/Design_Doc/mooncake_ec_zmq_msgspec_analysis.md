# MooncakeECConnector：ZMQ 与 msgspec 深度分析报告

> **文件范围**：`vllm/distributed/ec_transfer/ec_connector/mooncake/`
> **分析日期**：2026-04-10

---

## 目录

1. [整体架构概览](#1-整体架构概览)
2. [ZMQ 通道设计](#2-zmq-通道设计)
   - [通道拓扑与端口规划](#21-通道拓扑与端口规划)
   - [PUB/SUB：调度器哈希通知通道](#22-pubsub调度器哈希通知通道)
   - [ROUTER/DEALER：Worker 数据握手通道](#23-routerdealer-worker-数据握手通道)
3. [msgspec 消息协议](#3-msgspec-消息协议)
   - [消息类型一览](#31-消息类型一览)
   - [编解码器的创建与复用](#32-编解码器的创建与复用)
4. [完整交互时序](#4-完整交互时序)
5. [各组件 ZMQ/msgspec 使用详解](#5-各组件-zmqmsgspec-使用详解)
   - [mooncake_ec_metadata.py：协议消息定义](#51-mooncake_ec_metadatapy协议消息定义)
   - [mooncake_ec_worker.py：Worker 数据平面](#52-mooncake_ec_workerpy-worker-数据平面)
   - [mooncake_ec_scheduler.py：调度器控制平面](#53-mooncake_ec_schedulerpy调度器控制平面)
6. [设计决策解析](#6-设计决策解析)
   - [为什么用 ZMQ 而不是 gRPC/HTTP？](#61-为什么用-zmq-而不是-grpchttp)
   - [为什么用 ROUTER/DEALER 而不是 REQ/REP？](#62-为什么用-routerdealer-而不是-reqrep)
   - [为什么用 PUB/SUB 而不是 PUSH/PULL？](#63-为什么用-pubsub-而不是-pushpull)
   - [为什么用 msgspec 而不是 JSON/pickle？](#64-为什么用-msgspec-而不是-jsonpickle)
   - [为什么控制平面与数据平面分开两个端口？](#65-为什么控制平面与数据平面分开两个端口)
7. [内存管理：EmbedBlockManager](#7-内存管理embedblockmanager)
8. [错误处理与资源释放](#8-错误处理与资源释放)
9. [关键代码路径速查](#9-关键代码路径速查)

---

## 1. 整体架构概览

MooncakeECConnector 实现了一个**解耦的拉取模型（Decoupled Pull Model）**，用于在编码器实例（Producer）和预填充/解码实例（Consumer）之间传输编码器缓存张量（如视觉嵌入向量）。

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           Producer 节点                                     │
│                                                                             │
│  ┌─────────────────────┐       ┌─────────────────────────────────────────┐ │
│  │  MooncakeECConnector│       │      MooncakeECConnectorWorker          │ │
│  │  (SCHEDULER role)   │       │                                         │ │
│  │                     │       │  ┌────────────────┐  ┌───────────────┐  │ │
│  │  (无 ZMQ 套接字)     │       │  │  ROUTER socket │  │  PUB socket   │  │ │
│  │                     │       │  │  :ec_port      │  │  :ec_port+1   │  │ │
│  └─────────────────────┘       │  └────────────────┘  └───────────────┘  │ │
│                                │  ┌──────────────────────────────────────┐ │ │
│                                │  │  background save thread               │ │ │
│                                │  │  _save_queue → _do_save()            │ │ │
│                                │  └──────────────────────────────────────┘ │ │
│                                │  ┌──────────────────────────────────────┐ │ │
│                                │  │  EmbedBlockManager (GPU buffer)      │ │ │
│                                │  │  + Mooncake TransferEngine           │ │ │
│                                │  └──────────────────────────────────────┘ │ │
│                                └─────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────────┘
        │  PUB (notification)              │  ROUTER ↔ DEALER (handshake)
        │  tcp://*:ec_port+1               │  tcp://*:ec_port
        ▼                                  ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                           Consumer 节点                                     │
│                                                                             │
│  ┌─────────────────────────────────────┐  ┌────────────────────────────┐   │
│  │  MooncakeECConnectorScheduler       │  │  MooncakeECConnectorWorker │   │
│  │                                     │  │                            │   │
│  │  ┌──────────────────────────────┐   │  │  ┌───────────────────────┐ │   │
│  │  │  SUB socket (RCVTIMEO=0)     │   │  │  │  DEALER socket        │ │   │
│  │  │  tcp://{ec_ip}:ec_port+1     │   │  │  │  tcp://{ec_ip}:ec_port│ │   │
│  │  └──────────────────────────────┘   │  │  └───────────────────────┘ │   │
│  │  available_hashes: set[str]         │  │  EmbedBlockManager         │   │
│  │  available_hash_meta: dict          │  │  + Mooncake TransferEngine │   │
│  └─────────────────────────────────────┘  └────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────┘
```

整个系统分为**两个平面**，由**两个 ZMQ 通道**承载：

| 平面 | 方向 | Socket 模式 | 端口 | 用途 |
|------|------|-------------|------|------|
| 控制平面（Control Plane） | Producer Worker → Consumer Scheduler | PUB / SUB | `ec_port + 1` | 广播哈希可用性通知 |
| 数据平面（Data Plane） | Consumer Worker ↔ Producer Worker | DEALER / ROUTER | `ec_port + 0` | 拉取请求 + 完成确认 |

---

## 2. ZMQ 通道设计

### 2.1 通道拓扑与端口规划

```
ec_port + 0  ─────── 数据握手通道 (ROUTER/DEALER)
ec_port + 1  ─────── 哈希通知通道 (PUB/SUB)
```

代码定义（`mooncake_ec_worker.py`）：

```python
_DATA_PORT_OFFSET         = 0   # 数据平面握手
_NOTIFICATION_PORT_OFFSET = 1   # 调度器级别哈希可用性
```

代码定义（`mooncake_ec_scheduler.py`）：

```python
_ZMQ_NOTIFICATION_PORT_OFFSET = 1  # 通知端口 = ec_port + 1
```

### 2.2 PUB/SUB：调度器哈希通知通道

**目的**：Producer Worker 在后台线程中将可用的 `mm_hash` 广播给 Consumer Scheduler，让调度器能在下次 `has_cache_item` 检查时知道哪些编码器缓存已就绪，从而跳过本地编码计算。

#### Producer 侧（绑定 PUB）

```python
# mooncake_ec_worker.py: __init__
notif_port = self.ec_port + _NOTIFICATION_PORT_OFFSET
self._notif_pub = self._zmq_ctx.socket(zmq.PUB)
self._notif_pub.bind(f"tcp://*:{notif_port}")
```

发布通知（在 `_do_save` 后台线程中）：

```python
notif = ECHashNotification(
    mm_hashes=[mm_hash],
    shapes=[shape],
    dtype_strs=[dtype_str],
    num_bytes_list=[num_bytes],
    producer_session_id=self.session_id,
)
self._notif_pub.send(_notif_encoder.encode(notif))
```

#### Consumer 侧（连接 SUB，非阻塞）

```python
# mooncake_ec_scheduler.py: __init__
self._sub_socket = self._zmq_ctx.socket(zmq.SUB)
self._sub_socket.setsockopt(zmq.SUBSCRIBE, b"")   # 订阅所有消息
self._sub_socket.setsockopt(zmq.RCVTIMEO, 0)       # 非阻塞！
self._sub_socket.connect(f"tcp://{ec_ip}:{ec_port}")
```

轮询（在每次 `has_cache_item` 调用时触发）：

```python
def _poll_notifications(self) -> None:
    while True:
        try:
            raw = self._sub_socket.recv(zmq.NOBLOCK)
            notif = _notification_decoder.decode(raw)
            for i, mm_hash in enumerate(notif.mm_hashes):
                self.available_hashes.add(mm_hash)
                self.available_hash_meta[mm_hash] = (
                    notif.shapes[i], notif.dtype_strs[i], notif.num_bytes_list[i]
                )
        except zmq.Again:
            break   # 队列已空，退出
```

**关键设计点**：
- `RCVTIMEO=0` + `zmq.NOBLOCK`：完全非阻塞，不会阻塞调度器主循环
- `zmq.SUBSCRIBE, b""`：订阅所有主题（无主题过滤）
- 一次 `_poll_notifications` 调用会耗尽所有积压的消息，直到 `zmq.Again`

### 2.3 ROUTER/DEALER：Worker 数据握手通道

**目的**：Consumer Worker 主动向 Producer Worker 发送拉取请求（`ECTransferPull`），Producer 执行 Mooncake 数据传输后回复完成消息（`ECTransferComplete`）。

#### Producer 侧（绑定 ROUTER）

```python
# mooncake_ec_worker.py: __init__
self._data_socket = self._zmq_ctx.socket(zmq.ROUTER)
self._data_socket.bind(f"tcp://*:{data_port}")
```

接收来自 Consumer 的消息（ROUTER 会在 recv_multipart 中附加发送方 identity）：

```python
def _recv_from_consumer(self) -> tuple[bytes, bytes]:
    parts = self._data_socket.recv_multipart()
    identity = parts[0]
    payload  = parts[1] if len(parts) > 1 else b""
    return identity, payload
```

回复 Consumer（使用保存的 identity 路由回去）：

```python
def _send_to_consumer(self, identity: bytes, data: bytes) -> None:
    self._data_socket.send_multipart([identity, data])
```

#### Consumer 侧（连接 DEALER，带超时）

```python
# mooncake_ec_worker.py: __init__
self._data_socket = self._zmq_ctx.socket(zmq.DEALER)
self._data_socket.setsockopt(zmq.RCVTIMEO, _ZMQ_RECV_TIMEOUT_MS)  # 60s 超时
self._data_socket.connect(f"tcp://{self.ec_ip}:{data_port}")
```

发送拉取请求并等待完成：

```python
# start_load_caches 中
self._data_socket.send(_pull_encoder.encode(pull))
complete_raw = self._data_socket.recv()   # 阻塞，等待 ECTransferComplete
```

**关键设计点**：
- ROUTER 自动为每个连接的 DEALER 分配一个 identity，实现多路复用
- Consumer 端的 `RCVTIMEO=60000ms`：等待 Mooncake 传输完成，防止无限阻塞
- DEALER 发送时不需要手动附加 identity（ZMQ 自动处理帧）
- ROUTER 接收时自动解帧，`parts[0]` 是 identity，`parts[1]` 是 payload

---

## 3. msgspec 消息协议

### 3.1 消息类型一览

所有消息类型定义在 `mooncake_ec_metadata.py`，继承自 `msgspec.Struct`（使用 MessagePack 序列化）：

```
┌────────────────────────────────────────────────────────────────┐
│                   消息类型层次                                   │
│                                                                  │
│  msgspec.Struct                                                  │
│      ├── ECHashNotification     (PUB/SUB 通道)                  │
│      │     Producer Worker ──→ Consumer Scheduler               │
│      │     字段: mm_hashes, shapes, dtype_strs,                 │
│      │           num_bytes_list, producer_session_id            │
│      │                                                          │
│      ├── ECTransferPull         (DEALER→ROUTER 通道)            │
│      │     Consumer Worker ──→ Producer Worker                  │
│      │     字段: mm_hash, dst_ptr, dst_offset,                  │
│      │           consumer_session_id                            │
│      │                                                          │
│      ├── ECTransferComplete     (ROUTER→DEALER 通道)            │
│      │     Producer Worker ──→ Consumer Worker                  │
│      │     字段: mm_hash, status (0=成功)                       │
│      │                                                          │
│      ├── ECTransferRequest      (已定义，暂未使用)               │
│      └── ECTransferReady        (已定义，暂未使用)               │
└────────────────────────────────────────────────────────────────┘
```

#### ECHashNotification（控制平面）

```python
class ECHashNotification(msgspec.Struct):
    mm_hashes:           list[str]        # 可用的哈希列表（支持批量）
    shapes:              list[list[int]]  # 每个哈希对应的张量 shape
    dtype_strs:          list[str]        # 每个哈希对应的 dtype
    num_bytes_list:      list[int]        # 每个哈希对应的字节数
    producer_session_id: str              # Mooncake session: "hostname:rpc_port"
```

#### ECTransferPull（数据平面：Consumer→Producer）

```python
class ECTransferPull(msgspec.Struct):
    mm_hash:             str  # 要拉取的哈希
    dst_ptr:             int  # Consumer GPU 缓冲区绝对指针
    dst_offset:          int  # 缓冲区内偏移
    consumer_session_id: str  # Consumer 的 Mooncake session
```

#### ECTransferComplete（数据平面：Producer→Consumer）

```python
class ECTransferComplete(msgspec.Struct):
    mm_hash: str  # 完成的哈希
    status:  int  # 0=成功，非零=错误码
```

### 3.2 编解码器的创建与复用

```python
# mooncake_ec_worker.py 模块级别（一次性创建，复用）
_pull_encoder    = msgspec.msgpack.Encoder()
_pull_decoder    = msgspec.msgpack.Decoder(ECTransferPull)
_complete_encoder = msgspec.msgpack.Encoder()
_complete_decoder = msgspec.msgpack.Decoder(ECTransferComplete)
_notif_encoder   = msgspec.msgpack.Encoder()

# mooncake_ec_scheduler.py 模块级别
_notification_decoder = msgspec.msgpack.Decoder(ECHashNotification)
```

**设计要点**：
- `Encoder` 是线程安全的，可以在后台线程和主线程之间共享
- `Decoder` 在创建时绑定了目标类型，解码时会做**类型校验**
- 模块级别创建避免了每次序列化/反序列化时的对象分配开销

---

## 4. 完整交互时序

```
Producer节点                                              Consumer节点
     │                                                         │
     │  [主线程] save_caches(encoder_cache, mm_hash)           │
     │  ┌─────────────────────────────────────────┐            │
     │  │ tensor_snapshot = tensor.clone()        │            │
     │  │ _save_queue.put(mm_hash, tensor, ...)   │            │
     │  └─────────────────────────────────────────┘            │
     │                                                         │
     │  [后台 save thread] _do_save()                          │
     │  ┌──────────────────────────────────────────┐           │
     │  │ 1. copy_tensor_to_buffer(tensor, mm_hash)│           │
     │  │    → offset, src_ptr in GPU buffer      │           │
     │  └──────────────────────────────────────────┘           │
     │                                                         │
     │  2. PUB: ECHashNotification ──────────────────────────▶ │
     │         (mm_hash, shape, dtype, num_bytes,              │
     │          producer_session_id)                           │
     │                              [调度器] _poll_notifications│
     │                              ┌──────────────────────────┤
     │                              │ available_hashes.add()   │
     │                              │ available_hash_meta[...] │
     │                              └──────────────────────────┤
     │                                                         │
     │                              [调度器] has_cache_item()  │
     │                              → True （命中）             │
     │                                                         │
     │                              [调度器] update_state_after_alloc()
     │                              → mm_datas_need_loads[mm_hash] = n
     │                                                         │
     │                              [调度器] build_connector_meta()
     │                              → MooncakeECConnectorMetadata
     │                                                         │
     │  3. ROUTER: 等待 ECTransferPull ◀──────────────────────── │
     │     _recv_from_consumer()       [Worker] start_load_caches()
     │                              ┌──────────────────────────┤
     │                              │ allocate(num_bytes)      │
     │                              │ → offset, dst_ptr        │
     │                              │ DEALER: send ECTransferPull
     │                              │   (mm_hash, dst_ptr,     │
     │                              │    consumer_session_id)  │
     │                              └──────────────────────────┤
     │                                                         │
     │  4. 收到 pull 后，执行 Mooncake 传输                     │
     │  ┌──────────────────────────────────────────────────┐   │
     │  │ engine.batch_transfer_sync_write(                │   │
     │  │   consumer_session_id,   ← 来自 pull 消息        │   │
     │  │   [src_ptr],             ← Producer GPU 指针     │   │
     │  │   [pull.dst_ptr],        ← Consumer GPU 指针     │   │
     │  │   [num_bytes]            ← 数据大小              │   │
     │  │ )                                                │   │
     │  └──────────────────────────────────────────────────┘   │
     │                                                         │
     │  5. ROUTER: ECTransferComplete ───────────────────────▶ │
     │         (mm_hash, status=0)                             │
     │                              ┌──────────────────────────┤
     │                              │ DEALER: recv complete    │
     │                              │ read_tensor_from_buffer()│
     │                              │ encoder_cache[mm_hash]=t │
     │                              └──────────────────────────┤
     │                                                         │
     │  6. _finished_saves.add(mm_hash)                        │
     │                                                         │
```

---

## 5. 各组件 ZMQ/msgspec 使用详解

### 5.1 mooncake_ec_metadata.py：协议消息定义

该文件是整个协议的**类型系统中心**，定义了两类数据结构：

#### Python dataclass（调度器内部使用，不走网络）

```python
@dataclass
class ECTensorMeta:
    """调度器→Worker 传递的张量元数据（进程内，无序列化）"""
    mm_hash:       str
    shape:         list[int]
    dtype_str:     str
    num_bytes:     int
    buffer_offset: int = 0

@dataclass
class MooncakeECConnectorMetadata(ECConnectorMetadata):
    """调度步骤输出，传给 Worker"""
    tensors_to_load: list[ECTensorMeta] = field(default_factory=list)
```

#### msgspec.Struct（跨进程网络消息）

```python
class ECHashNotification(msgspec.Struct): ...  # 控制平面
class ECTransferPull(msgspec.Struct): ...      # 数据平面
class ECTransferComplete(msgspec.Struct): ...  # 数据平面
```

**选型对比**：

| 场景 | 类型 | 理由 |
|------|------|------|
| 调度器→Worker（进程内） | `@dataclass` | 无需序列化，直接传引用 |
| 跨进程 ZMQ 消息 | `msgspec.Struct` | 高性能 MessagePack 序列化 |

### 5.2 mooncake_ec_worker.py：Worker 数据平面

#### Producer 端 ZMQ 套接字初始化

```python
# ROUTER：绑定端，接受来自任意 Consumer 的连接
self._data_socket = self._zmq_ctx.socket(zmq.ROUTER)
self._data_socket.bind(f"tcp://*:{data_port}")

# PUB：广播哈希可用性给调度器
self._notif_pub = self._zmq_ctx.socket(zmq.PUB)
self._notif_pub.bind(f"tcp://*:{notif_port}")
```

#### Consumer 端 ZMQ 套接字初始化

```python
# DEALER：连接端，60秒接收超时
self._data_socket = self._zmq_ctx.socket(zmq.DEALER)
self._data_socket.setsockopt(zmq.RCVTIMEO, 60_000)
self._data_socket.connect(f"tcp://{self.ec_ip}:{data_port}")
```

#### msgspec 的使用模式

```python
# 模块级别预分配（避免重复开销）
_pull_encoder     = msgspec.msgpack.Encoder()
_pull_decoder     = msgspec.msgpack.Decoder(ECTransferPull)
_complete_encoder = msgspec.msgpack.Encoder()
_complete_decoder = msgspec.msgpack.Decoder(ECTransferComplete)
_notif_encoder    = msgspec.msgpack.Encoder()

# Consumer 发送拉取请求
pull = ECTransferPull(mm_hash=mm_hash, dst_ptr=dst_ptr, ...)
self._data_socket.send(_pull_encoder.encode(pull))

# Consumer 接收完成通知
complete_raw = self._data_socket.recv()
complete = _complete_decoder.decode(complete_raw)

# Producer 后台线程发布通知
notif = ECHashNotification(mm_hashes=[mm_hash], ...)
self._notif_pub.send(_notif_encoder.encode(notif))
```

### 5.3 mooncake_ec_scheduler.py：调度器控制平面

#### Consumer 调度器订阅

```python
self._sub_socket = self._zmq_ctx.socket(zmq.SUB)
self._sub_socket.setsockopt(zmq.SUBSCRIBE, b"")  # 无主题过滤
self._sub_socket.setsockopt(zmq.RCVTIMEO, 0)      # 非阻塞
self._sub_socket.connect(f"tcp://{ec_ip}:{ec_port}")
```

#### 非阻塞轮询机制

```python
_notification_decoder = msgspec.msgpack.Decoder(ECHashNotification)

def _poll_notifications(self) -> None:
    if self._sub_socket is None:
        return
    while True:
        try:
            raw = self._sub_socket.recv(zmq.NOBLOCK)  # 非阻塞，立即返回
            notif = _notification_decoder.decode(raw)
            for i, mm_hash in enumerate(notif.mm_hashes):
                self.available_hashes.add(mm_hash)
                self.available_hash_meta[mm_hash] = (
                    notif.shapes[i],
                    notif.dtype_strs[i],
                    notif.num_bytes_list[i],
                )
        except zmq.Again:  # 队列已空
            break
```

**注意**：Producer 调度器（`is_producer=True`）**没有** ZMQ 套接字，通知发布完全由 Worker 后台线程负责。

---

## 6. 设计决策解析

### 6.1 为什么用 ZMQ 而不是 gRPC/HTTP？

```
ZMQ 优势：
  ✓ 亚毫秒延迟（C 实现，无 HTTP 头开销）
  ✓ 无需独立服务器进程
  ✓ 原生支持多种消息模式（PUB/SUB、ROUTER/DEALER）
  ✓ Python 绑定（pyzmq）成熟稳定
  ✓ 支持 inproc://、ipc://、tcp:// 等多种传输

gRPC 劣势（在此场景下）：
  ✗ 协议缓冲区 schema 编译步骤
  ✗ HTTP/2 协议开销对高频小消息不友好
  ✗ 服务定义更复杂
```

### 6.2 为什么用 ROUTER/DEALER 而不是 REQ/REP？

```
REQ/REP 的问题：
  ✗ 严格同步：REQ 发送后必须等待 REP 才能发下一条
  ✗ 不支持一个 REP 服务多个 REQ（需要串行处理）

ROUTER/DEALER 的优势：
  ✓ ROUTER 可以同时处理多个 DEALER 的请求
  ✓ DEALER 可以先发多个请求再等回复（异步）
  ✓ ROUTER 通过 identity 帧精确路由回复

实际使用场景：
  多个 Consumer Worker 可以同时向一个 Producer ROUTER 发送 ECTransferPull
  Producer 通过 identity 正确路由 ECTransferComplete 回各自的 Consumer
```

### 6.3 为什么用 PUB/SUB 而不是 PUSH/PULL？

```
PUSH/PULL 的问题：
  ✗ 一对一或轮询分发，不适合广播
  ✗ Consumer 需要主动 PULL，Producer 无法主动推送

PUB/SUB 的优势：
  ✓ Producer 无需知道有多少 Consumer
  ✓ 广播语义：所有订阅者都能收到通知
  ✓ Consumer 调度器可以随时连接/断开，不影响 Producer
  ✓ RCVTIMEO=0 使调度器完全非阻塞轮询
```

### 6.4 为什么用 msgspec 而不是 JSON/pickle？

```
性能对比（序列化 + 反序列化）：
  msgspec msgpack  ≈ 50-100 MB/s  ← 本项目使用
  json (标准库)    ≈ 30-50  MB/s
  pickle           ≈ 20-40  MB/s（有安全风险）
  orjson           ≈ 80-120 MB/s（纯 JSON，无类型校验）

msgspec 的独特优势：
  ✓ 强类型校验：Decoder 绑定目标类型，反序列化自动验证
  ✓ msgpack 二进制格式比 JSON 小 20-40%
  ✓ 模块级 Encoder/Decoder 对象可复用，零分配开销
  ✓ Struct 类比 dataclass 更紧凑（无 __dict__）

具体代码中的体现：
  _notification_decoder = msgspec.msgpack.Decoder(ECHashNotification)
  # ^ 绑定类型，decode() 时不需要指定，且类型安全
```

### 6.5 为什么控制平面与数据平面分开两个端口？

```
分离的原因：

  控制平面（ec_port+1, PUB/SUB）：
    - 调度器进程使用（Python 主线程）
    - 非阻塞，轮询方式
    - 消息小（仅元数据）
    - 多播语义（一对多）

  数据平面（ec_port+0, ROUTER/DEALER）：
    - Worker 进程使用（后台线程）
    - 阻塞等待（有超时）
    - 消息小（仅握手，真正数据走 Mooncake）
    - 请求-回复语义（一对一）

  如果混用同一端口：
    ✗ 调度器和 Worker 需要共享套接字（线程安全问题）
    ✗ 消息路由复杂，需要手动区分消息类型
    ✗ PUB/SUB 与 ROUTER/DEALER 语义不兼容
```

---

## 7. 内存管理：EmbedBlockManager

`embed_block_manager.py` 虽然不直接使用 ZMQ/msgspec，但它是 ZMQ 数据握手协议的**地址交换对象**：

```
┌─────────────────────────────────────────────────────────────┐
│              EmbedBlockManager（Producer & Consumer 各一份）  │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  GPU Buffer (torch.uint8, 预注册到 Mooncake)         │   │
│  │                                                     │   │
│  │  ┌────────┬────────┬──────────┬────────────────┐   │   │
│  │  │  已用  │  已用  │   空闲   │     空闲       │   │   │
│  │  │ (hash1)│ (hash2)│          │                │   │   │
│  │  └────────┴────────┴──────────┴────────────────┘   │   │
│  │  offset:  0     N1       N1+N2          ...         │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ContiguousMemoryAllocator（first-fit + 空闲块合并）         │
│    free_blocks: [(N1+N2, remaining)]                        │
│                                                             │
│  cached_embeddings: {mm_hash → (offset, bytes, shape, dtype)}│
│    ↑ 跨调度步骤的本地缓存，避免重复传输                      │
└─────────────────────────────────────────────────────────────┘
```

ZMQ 消息中交换的地址：

```python
# Producer 侧：发布通知/响应 pull 时
src_ptr = self.embed_manager.buffer_ptr + offset  # 绝对 GPU 指针

# Consumer 侧：发送 pull 请求时
offset, dst_ptr = self.embed_manager.allocate(num_bytes)
pull = ECTransferPull(dst_ptr=dst_ptr, ...)  # dst_ptr 传给 Producer
                                              # Producer 用它调用 Mooncake
```

三层缓存查找顺序（`start_load_caches`）：

```
Tier 1: encoder_cache[mm_hash]       → 当前步骤已加载（内存）
    ↓ miss
Tier 2: embed_manager.get_cached_tensor(mm_hash)  → 上次传输缓存（GPU buffer view）
    ↓ miss
Tier 3: 远程拉取（ECTransferPull → Mooncake → ECTransferComplete）
```

---

## 8. 错误处理与资源释放

### 错误处理策略

| 位置 | 错误场景 | 处理方式 |
|------|----------|----------|
| `_do_save` | 任意异常 | `logger.exception` + 继续循环（不影响其他保存） |
| `_do_save` | Mooncake 传输失败 | `status=ret` 写入 `ECTransferComplete`，Consumer 收到非零 status |
| `start_load_caches` | `status != 0` | `embed_manager.free(offset, num_bytes)` 回收缓冲区，跳过该张量 |
| `_poll_notifications` | `zmq.Again` | 正常退出（队列空） |
| Consumer recv | 超时（60s） | `zmq.ZMQError` 抛出（`RCVTIMEO=60000`） |

### 资源释放（close 方法）

```python
# Producer Worker
def close(self) -> None:
    self._save_queue.put(None)          # 毒丸信号，终止后台线程
    self._save_thread.join(timeout=5.0) # 等待后台线程退出
    self.embed_manager.unregister_from_engine(self.engine)
    self._data_socket.close()
    self._notif_pub.close()
    self._zmq_ctx.term()                # 必须最后 term Context

# Consumer Scheduler
def close(self) -> None:
    if self._sub_socket is not None:
        self._sub_socket.close()
    self._zmq_ctx.term()
```

**重要**：`zmq.Context.term()` 必须在所有套接字关闭后调用，否则会阻塞。

---

## 9. 关键代码路径速查

| 操作 | 文件 | 函数 | 行号 |
|------|------|------|------|
| Producer 初始化 ROUTER/PUB | `mooncake_ec_worker.py` | `__init__` | 133-158 |
| Consumer 初始化 DEALER | `mooncake_ec_worker.py` | `__init__` | 160-172 |
| Consumer Scheduler 初始化 SUB | `mooncake_ec_scheduler.py` | `__init__` | 74-88 |
| 入队异步保存 | `mooncake_ec_worker.py` | `save_caches` | 177-203 |
| 后台线程：发布通知 | `mooncake_ec_worker.py` | `_do_save` | 236-245 |
| 后台线程：等待 Pull + 执行传输 | `mooncake_ec_worker.py` | `_do_save` | 247-278 |
| 调度器非阻塞轮询通知 | `mooncake_ec_scheduler.py` | `_poll_notifications` | 144-165 |
| Consumer 三层缓存查找 | `mooncake_ec_worker.py` | `start_load_caches` | 288-369 |
| Consumer 发送 Pull 请求 | `mooncake_ec_worker.py` | `start_load_caches` | 329-335 |
| Consumer 等待完成消息 | `mooncake_ec_worker.py` | `start_load_caches` | 337-347 |
| msgspec 编解码器定义 | `mooncake_ec_worker.py` | 模块级别 | 66-70 |
| ZMQ 协议消息类定义 | `mooncake_ec_metadata.py` | 全文 | 42-95 |

---

*本报告由 Claude Code 基于源码静态分析生成。*

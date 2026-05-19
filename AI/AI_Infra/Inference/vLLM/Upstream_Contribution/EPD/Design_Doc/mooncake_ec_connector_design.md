# MooncakeECConnector Design Document

## 1. Background

vLLM's **EPD (Encoder-Prefill-Decode) disaggregation** feature allows running vision encoders on separate instances from the language model prefill/decode stages. This enables independent scaling, lower TTFT for text-only requests, and cross-process reuse of encoder outputs.

The current EPD implementation uses `ECExampleConnector`, a file-based connector suitable for debugging and experimentation. For production deployments, we need a high-performance connector that supports multiple network transports (TCP, RDMA, SHM/NVLink).

**MooncakeECConnector** addresses this by leveraging the [Mooncake TransferEngine](https://github.com/kvcache-ai/Mooncake), which provides a unified API across multiple transport backends.

### Key Challenge

Unlike KV Cache transfer (where block memory is pre-allocated at fixed addresses), encoder cache tensors are **dynamically allocated with variable sizes**. Each multimodal input produces an encoder output of different dimensions depending on image resolution, model architecture, etc. Mooncake's TransferEngine requires pre-registered memory for efficient transfers, so we introduce an **EmbedBlockManager** with a pre-registered GPU buffer.

## 2. Architecture Overview

```
                    EPD System Architecture
                    
    +-----------------+                  +------------------+
    | Encoder Instance|                  | PD Instance      |
    | (Producer)      |                  | (Consumer)       |
    |                 |                  |                  |
    | +-------------+ |   ZMQ PUB/SUB   | +-------------+  |
    | | Worker      |>|--- (hash avail.)|>| Scheduler   |  |
    | | (publishes) | |  (ec_port+1)    | | (subscribes)|  |
    | +-------------+ |                  | +-------------+  |
    |                 |                  |                  |
    | +-------------+ |   ZMQ + Mooncake| +-------------+  |
    | | Worker      |<|--- ROUTER/DEALER|>| Worker      |  |
    | | (bg thread) | |   (pull model)  | |             |  |
    | | +---------+ | |                  | | +---------+ | |
    | | | Embed   | | |  Mooncake TCP/  | | | Embed   | | |
    | | | Block   |<|-|--- RDMA/EFA --->|-|>| Block   | | |
    | | | Manager | | |  (data plane)   | | | Manager | | |
    | | +---------+ | |                  | | +---------+ | |
    | +-------------+ |                  | +-------------+  |
    +-----------------+                  +------------------+
```

### Component Separation

| Component | Role | Responsibility |
|-----------|------|----------------|
| `MooncakeECConnector` | Facade | Dispatches to scheduler or worker based on role |
| `MooncakeECConnectorScheduler` | Scheduler | Tracks hash availability via ZMQ SUB, builds metadata with real tensor info |
| `MooncakeECConnectorWorker` | Worker | TransferEngine operations, ZMQ handshake, async save, buffer management |
| `EmbedBlockManager` | Worker | Pre-registered GPU buffer with first-fit allocator and local embedding cache |
| `MooncakeECConnectorMetadata` | Both | Serializable metadata passed from scheduler to worker |

## 3. EmbedBlockManager Design

### Problem

Mooncake TransferEngine requires memory regions to be registered before transfer. Encoder cache tensors are dynamically sized (e.g., a 224x224 image might produce a [196, 4096] tensor while a 1024x1024 image produces a [1024, 4096] tensor). We cannot register each tensor individually due to overhead.

### Solution: Pre-registered Buffer with First-Fit Allocator + Local Cache

```
EmbedBlockManager Buffer Layout (1GB default)
+----------------------------------------------------------+
|  Tensor A  |  free  |  Tensor C  |  Tensor D  |  free   |
|  (5.2 MB)  | (3 MB) |  (8.0 MB)  |  (2.1 MB)  |         |
+----------------------------------------------------------+
^                      ^                          ^
cached: hash_a         cached: hash_c, hash_d    allocator tracks free blocks
```

- **Single contiguous allocation**: One `torch.empty(buffer_size, dtype=uint8)` on GPU.
- **Registered once**: `engine.register_memory(buffer_ptr, buffer_size)` at init.
- **First-fit allocator** (`ContiguousMemoryAllocator`): Maintains a sorted list of `(offset, size)` free blocks. Allocations are 64-byte aligned. On `free()`, adjacent blocks are merged to prevent fragmentation.
- **Local embedding cache**: `cached_embeddings` maps `mm_hash → (offset, num_bytes, shape, dtype_str)`. Embeddings persist across scheduling steps, avoiding redundant remote transfers for the same hash.
- **Individual eviction**: `evict(mm_hash)` removes a specific entry from the cache and frees its buffer region.

### Why First-Fit Allocator vs Alternatives

| Approach | Pros | Cons |
|----------|------|------|
| Fixed blocks | Predictable, simple free | Internal fragmentation with variable-size tensors |
| Bump allocator | Zero fragmentation, simple, fast O(1) alloc | Cannot free individual allocations; must reset every step |
| **First-fit + merge** | Individual free, cross-step caching, handles variable sizes | Slightly more complex |
| Per-tensor registration | No buffer needed | High registration overhead per transfer |

The first-fit allocator with block merging (pattern from SGLang PR #16137) is the best fit because it supports individual `free()` calls, enabling embeddings to persist in the buffer as a local cache across scheduling steps.

### Buffer Sizing

Default: 1GB (`ec_buffer_size` in `ECTransferConfig`).

Typical vision encoder outputs:
- ViT-L/14 @ 224px: ~196 * 4096 * 2 bytes = ~1.6 MB per image
- ViT-G @ 384px: ~576 * 4096 * 2 bytes = ~4.7 MB per image
- SigLIP @ 1024px: ~4096 * 4096 * 2 bytes = ~33.5 MB per image

With 1GB buffer: supports 30-600+ concurrent encoder outputs depending on resolution.

## 4. Transfer Protocol

### Control Plane: ZMQ

Two ZMQ channels:

1. **Data handshake** (ROUTER/DEALER on `ec_port`):
   - Producer binds ROUTER, consumer connects DEALER.
   - Consumer-initiated pull: ECTransferPull -> ECTransferComplete.

2. **Hash availability** (PUB/SUB on `ec_port+1`):
   - Producer **worker** publishes `ECHashNotification` (with tensor metadata) when a tensor is saved.
   - Consumer **scheduler** subscribes to learn which mm_hashes are available and stores their metadata for `build_connector_meta`.
   - Note: Only the producer worker has a PUB socket. The producer scheduler has no PUB socket (avoids port conflict).

### Data Plane: Mooncake TransferEngine

Decoupled pull model (consumer-initiated transfer):

```
Producer Worker (bg thread)              Consumer Worker
     |                                       |
     |  1. Clone tensor, enqueue             |
     |     (save_caches returns immediately) |
     |                                       |
     |  2. Copy tensor to buffer             |
     |     (with local cache registration)   |
     |                                       |
     |  3. PUB ECHashNotification  --------> |  (scheduler receives via SUB,
     |     (mm_hash, shape, dtype,           |   stores metadata, populates
     |      num_bytes, producer_session_id)  |   ECTensorMeta with real values)
     |                                       |
     |   --- scheduler step boundary ---     |
     |                                       |
     |                                       |  1. Check encoder_cache (tier 1)
     |                                       |  2. Check local buffer cache (tier 2)
     |  <-----------  3. ECTransferPull      |  3. Cache miss: allocate buffer,
     |     (mm_hash, dst_ptr, dst_offset,    |     send pull request
     |      consumer_session_id)             |
     |                                       |
     |  4. engine.batch_transfer_sync_write  |
     |     (consumer_session_id,             |
     |      [src_ptr], [dst_ptr], [len])     |
     |                                       |
     |  5. ECTransferComplete  ----------->  |
     |     (mm_hash, status=0)               |  4. Reconstruct tensor,
     |                                       |     register in local cache
```

### 3-Tier Lookup (Consumer)

When `start_load_caches` is called for an mm_hash:

1. **Tier 1 — encoder_cache**: Already loaded in a prior step → skip.
2. **Tier 2 — local buffer cache**: Previously received via Mooncake, still in the pre-registered buffer → return view (no copy, no network).
3. **Tier 3 — remote transfer**: Pull from producer via Mooncake TransferEngine.

### Message Types (msgspec for efficient serialization)

```python
class ECTransferPull(msgspec.Struct):
    """Consumer -> Producer: request to pull a specific embedding."""
    mm_hash: str
    dst_ptr: int
    dst_offset: int
    consumer_session_id: str  # "hostname:rpc_port"

class ECTransferComplete(msgspec.Struct):
    """Producer -> Consumer: data transfer done."""
    mm_hash: str
    status: int  # 0 = success

class ECHashNotification(msgspec.Struct):
    """Producer worker -> Consumer scheduler: mm_hashes now available."""
    mm_hashes: list[str]
    shapes: list[list[int]]        # one per mm_hash
    dtype_strs: list[str]          # one per mm_hash
    num_bytes_list: list[int]      # one per mm_hash
    producer_session_id: str       # producer's Mooncake session
```

### Session ID Exchange

Mooncake's `batch_transfer_sync_write` requires the remote peer's session ID (`"hostname:rpc_port"`). Session IDs are exchanged via ZMQ messages:

- **Producer session ID**: Included in `ECHashNotification` and `ECTransferRequest`.
- **Consumer session ID**: Included in `ECTransferPull` and `ECTransferReady`.

This eliminates the need for a separate session discovery mechanism.

## 5. Asynchronous Save Pipeline

Producer-side `save_caches()` is **non-blocking**. When the model runner calls `save_caches()`:

1. The tensor is cloned immediately (snapshot before encoder_cache evicts it).
2. The clone and metadata are enqueued to a background thread via `queue.Queue`.
3. `save_caches()` returns immediately — the model runner proceeds with the next step.

The background thread (`_save_worker_loop`) processes items sequentially:
- Copies tensor to the registered buffer (with local cache registration).
- Publishes `ECHashNotification` via ZMQ PUB.
- Waits for `ECTransferPull` from the consumer.
- Performs the Mooncake transfer.
- Sends `ECTransferComplete`.
- Tracks the mm_hash in `_finished_saves` for `get_finished()`.

## 6. Integration with vLLM

### Scheduler Integration

The scheduler creates the connector with `ECConnectorRole.SCHEDULER`:

```python
# In scheduler.__init__:
self.ec_connector = ECConnectorFactory.create_connector(config, ECConnectorRole.SCHEDULER)

# During scheduling:
if self.ec_connector.has_cache_item(mm_hash):
    # Skip local encoder computation, load from remote
    self.ec_connector.update_state_after_alloc(request, index)

# After scheduling:
ec_meta = self.ec_connector.build_connector_meta(scheduler_output)
scheduler_output.ec_connector_metadata = ec_meta
```

### Worker Integration

The worker creates the connector with `ECConnectorRole.WORKER`:

```python
# In model runner:
with self.maybe_get_ec_connector_output(scheduler_output, encoder_cache) as output:
    # This calls connector.start_load_caches() for consumers
    # Execute model...
    
# After encoder execution (producer):
self.maybe_save_ec_to_connector(encoder_cache, mm_hash)
# This calls connector.save_caches() — returns immediately (async)
```

### Factory Registration

```python
ECConnectorFactory.register_connector(
    "MooncakeECConnector",
    "vllm.distributed.ec_transfer.ec_connector.mooncake.mooncake_ec_connector",
    "MooncakeECConnector",
)
```

## 7. Configuration

### Basic Usage

```bash
# Producer (Encoder instance)
vllm serve MODEL --ec-transfer-config '{
    "ec_connector": "MooncakeECConnector",
    "ec_role": "ec_producer",
    "ec_ip": "10.0.0.1",
    "ec_port": 14579,
    "ec_buffer_size": 1073741824,
    "ec_connector_extra_config": {
        "mooncake_ec_protocol": "tcp"
    }
}'

# Consumer (Prefill/Decode instance)
vllm serve MODEL --ec-transfer-config '{
    "ec_connector": "MooncakeECConnector",
    "ec_role": "ec_consumer",
    "ec_ip": "10.0.0.1",
    "ec_port": 14579,
    "ec_buffer_size": 1073741824,
    "ec_connector_extra_config": {
        "mooncake_ec_protocol": "tcp"
    }
}'
```

### Configuration Reference

| Field | Default | Description |
|-------|---------|-------------|
| `ec_connector` | - | Must be `"MooncakeECConnector"` |
| `ec_role` | - | `"ec_producer"`, `"ec_consumer"`, or `"ec_both"` |
| `ec_ip` | `"127.0.0.1"` | IP address for ZMQ and Mooncake connection |
| `ec_port` | `14579` | Base port (data on `ec_port`, notifications on `ec_port+1`) |
| `ec_buffer_size` | `1e9` | Transfer buffer size in bytes |
| `ec_buffer_device` | `"cuda"` | Device for transfer buffer |
| `mooncake_ec_protocol` | `"tcp"` | Mooncake transport: `"tcp"`, `"rdma"`, or `"efa"` |

## 8. Extensibility: Supporting Multiple Transport Backends

The Mooncake TransferEngine abstracts transport details behind a unified API. The `protocol` parameter passed to `engine.initialize()` selects the backend:

| Protocol | Use Case | Configuration |
|----------|----------|---------------|
| `tcp` | General purpose, works everywhere | `"mooncake_ec_protocol": "tcp"` |
| `rdma` | High-performance with InfiniBand/RoCE | `"mooncake_ec_protocol": "rdma"` |
| `efa` | AWS EFA-enabled instances | `"mooncake_ec_protocol": "efa"` |

**No code changes are needed** to switch protocols -- it's purely a configuration change. Mooncake automatically falls back to TCP if RDMA hardware is not detected.

The `MooncakeECConnector` class is intentionally kept as a single class (not split into per-protocol subclasses) because Mooncake handles the transport abstraction. If future transports require fundamentally different control-plane logic, subclasses can be introduced at that point.

## 9. Comparison with Existing Connectors

### vs ECExampleConnector

| Aspect | ECExampleConnector | MooncakeECConnector |
|--------|-------------------|---------------------|
| Transport | Local filesystem (safetensors) | Network (TCP/RDMA/EFA) |
| Performance | Low (disk I/O, CPU serialization) | High (direct GPU memory transfer) |
| Save model | Synchronous (blocking) | Asynchronous (non-blocking) |
| Use case | Debugging, single-node testing | Production, multi-node |
| Buffer management | None (new files per tensor) | Pre-registered GPU buffer with local cache |
| Discovery | Shared filesystem path | ZMQ PUB/SUB for availability |
| Cross-step reuse | None (re-read from disk) | Local buffer cache (tier 2 lookup) |

### vs MooncakeConnector (KV Cache)

| Aspect | KV MooncakeConnector | MooncakeECConnector |
|--------|---------------------|---------------------|
| Data type | KV cache blocks (fixed size) | Encoder outputs (variable size) |
| Memory model | Pre-allocated KV cache pages | Dynamic tensors -> first-fit buffer |
| Buffer | KV cache itself (registered) | Separate EmbedBlockManager with local cache |
| Topology | Heterogeneous TP, PP support | 1:1 producer-consumer |
| Transfer model | Push | Decoupled pull (consumer-initiated) |
| Complexity | ~1600 lines | ~500 lines |

## 10. File Structure

```
vllm/distributed/ec_transfer/ec_connector/mooncake/
    __init__.py                    # Package marker
    mooncake_ec_connector.py       # MooncakeECConnector facade class
    mooncake_ec_worker.py          # Worker: TransferEngine + ZMQ + async save
    mooncake_ec_scheduler.py       # Scheduler: ZMQ SUB + state tracking
    embed_block_manager.py         # Pre-registered GPU buffer + first-fit allocator + local cache
    mooncake_ec_metadata.py        # Metadata + ZMQ message types

vllm/distributed/ec_transfer/ec_connector/factory.py  # Registration added
```

## 11. Future Work

- **Multi-producer support**: Extend the ZMQ topology and bootstrap to support multiple encoder instances.
- **Compression**: Optionally compress encoder outputs before transfer (useful for large vision models).
- **P2P mode**: Direct GPU-to-GPU transfer without the intermediate buffer copy when using NVLink/NVSwitch.
- **Buffer auto-sizing**: Dynamically size the transfer buffer based on model's encoder output dimensions and expected batch size.
- **LRU eviction**: Automatically evict least-recently-used embeddings from the local cache when the buffer is full.

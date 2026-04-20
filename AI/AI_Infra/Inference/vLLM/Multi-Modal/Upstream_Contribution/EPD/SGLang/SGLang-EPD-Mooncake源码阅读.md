# SGLang EPD

## Mooncake Transfer Engine

**MooncakeTransferEngine:**

封装 `from mooncake.engine import TransferEngine` 的 API：

- 初始化；
- 内存注册；
- 数据传输。

通过 `engine = init_mooncake_transfer_engine()` 对外暴露初始化接口。

**EPD using mooncake:**

```python
# encoder_transfer_backend == "mooncake"
class MMEncoder:

    def __init__(...):
        self.mm_global_cache = EmbeddingCacheController()
        self.engine = init_mooncake_transfer_engine()
        self.embedding_to_send = dict()

    async def encode(...):
        mm_data = EmbeddingData()
        self.embedding_to_send[req_id] = mm_data

    async def _send(...):
        # 每次都临时申请，而不是提前分配？
        self.engine.register(embedding.data_ptr(), embedding.nbytes)
        self.engine.transfer_sync(
            session_id, embedding.data_ptr(), buffer_address, embedding.nbytes
        )
        self.engine.deregister(embedding.data_ptr())
        mm_data.embedding = None、
        # 序列化
        serialized_data = pickle.dumps(mm_data)
        buffer = None
        # 如果是 mooncake，socket 就只发 mm_data: EmbeddingData，不发 buffer


# encoder_transfer_backend == "mooncake"
class MMReceiverBase:

    def __init__(...):
        self.embeddings_engine = init_mooncake_transfer_engine()
        self.embeddings_buffer = dict()
    
    async def recv_mm_data(...):
        embedding_port, recv_socket = get_zmq_socket_on_host()
        mm_data = self._extract_url_data(request_obj)
        # 1. Send mm_items to E node
        asyncio.create_task(self.encode(req_id, mm_data, embedding_port, "encode", "send"))
        # 2. Receive mm_embed from E node
        return await asyncio.wait_for(self._recv_mm_data(req_id, recv_socket, mm_processor, prompt))
        if error:
            self._cleanup_mooncake_buffer(req_id)
    
    async def encode(...):
        for ...:
            encode_requests.append(
                {
                    "encoder_idx": idx,
                    "mm_items": [
                        mm_item.get("url")
                        for mm_item in mm_data_modality[
                            cum_num_items : cum_num_items + assigned_num
                        ]
                    ],
                    "num_parts": total_num_parts,
                    "part_idx": part_idx,
                    "req_id": part_req_id,  # use part_req_id to avoid key collision
                    "modality": modality.name,  # convert enum to string for json serialization
                    "prefill_host": self.host,
                    "embedding_port": embedding_port,
                }
            )
        # Send encode requests
        async with aiohttp.ClientSession(...) as session:
        # mooncake backend: send bootstrap info
        buffer_address = await self.allocate_embedding_buffer(
            req_id,
            total_embedding_bytes,
        )
        for ...:
            buffer_address_adjust = offset + buffer_address
            response_json.update(
                {
                    "session_id": self.embeddings_engine.session_id,
                    "buffer_address": buffer_address_adjust,
                }
            )

    async def allocate_embedding_buffer(...):
        embeddings = torch.empty(total_bytes, dtype=torch.uint8)
        self.embeddings_engine.register(
            embeddings.data_ptr(),
            embeddings.nbytes,
        )
        self.embeddings_buffer[req_id] = embeddings
        return embeddings.data_ptr()

    async def _recv_mm_data(...):
        recv_embedding = None
        recv_embedding_data: MultiModalEmbeddingData = None
        while ...:
            parts = await recv_socket.recv_multipart(copy=False)
            recv_obj: EmbeddingData = pickle.loads(parts[0])
            if error:
                self._cleanup_mooncake_buffer(req_id)
        raw_buffer = self.embeddings_buffer.pop(req_id)
        self.embeddings_engine.deregister(raw_buffer.data_ptr())
        recv_embedding = recv_embedding_data.get_embedding(is_concat=True)
        # convert recv_embedding to mm_inputs
        return mm_inputs
    
    def _cleanup_mooncake_buffer(req_id):
        embeddings = self.embeddings_buffer.pop(req_id, None)
        self.embeddings_engine.deregister(embeddings.data_ptr())
```

## Mooncake Store

请分析 `python/sglang/srt/mem_cache/storage/mooncake_store/` 目录下的代码的整体架构和功能，并阐述它在 sglang 的 EPD 场景下是怎么使用的？有什么好处？
要求图文并貌（流程图、架构图），并解释涉及的一些技术原理，生成一份markdown格式的报告放到当前根目录下。

当使用 mooncake 作为 EPD 的传输后端时，请分析 `python/sglang/srt/disaggregation/` 目录下的 `encode_server` 和 `encode_receiver` 是怎么进行交互的（比如：消息同步、buffer 分配、数据传输等）？
要求图文并貌（流程图、架构图），并解释涉及的一些技术原理，生成一份markdown格式的报告放到当前根目录下。

DMA 是什么（Direct Memory Access）
不经过 CPU 参与的数据搬运机制
pinned memory 是异步 DMA 的前提

---

参考代码：

```python
class CpuStagingBuffer:
    def __init__(self, shape, dtype):
        self.buf = torch.empty(
            shape, dtype=dtype, device="cpu", pin_memory=True
        )

    def copy_from_gpu(self, x: torch.Tensor):
        self.buf.copy_(x, non_blocking=True)
        return self.buf


def async_copy_to_cpu(
    x: torch.Tensor,
    out: torch.Tensor | None = None,
) -> torch.Tensor:
    assert x.is_cuda

    # 如果没给 out，就创建一个 pinned CPU tensor
    if out is None:
        out = torch.empty_like(x, device="cpu", pin_memory=True)
    else:
        assert out.device.type == "cpu"
        assert out.is_pinned()

    # GPU → pinned CPU（真正 async）
    out.copy_(x, non_blocking=True)
    return out


self.copy_stream = torch.cuda.Stream()

...

# Asynchronously copy the mapping to GPU.
with torch.cuda.stream(self.copy_stream):
    logits_indices = torch.tensor(
        mapping, dtype=torch.int32, device="cpu", pin_memory=True
    )
    logits_indices = self.logits_indices[: len(mapping)].copy_(
        logits_indices, non_blocking=True
    )

# Ensure all async copies are complete before launching the kernel.
current_stream = torch.cuda.current_stream()
current_stream.wait_stream(self.copy_stream)

# call kernel ...

# Ensure the copy stream waits for the device tensors to finish being used
# before it re-uses or deallocates them
self.copy_stream.wait_stream(current_stream)
```

---

query.dim(): 4.
origin cu_seqlens: tensor([  0, 312], device='npu:0', dtype=torch.int32).
diff cu_seqlens: tensor([312], dtype=torch.int32).
MM cache hit rate: 50.0%
[2432, 3072, 3200,  720,  360, 2176, 3072, 4096, 1900, 4096]
dtype=torch.int32
shape: torch.Size([1])
origin cu_seqlens: tensor([   0, 1800, 2864], device='npu:0', dtype=torch.int32).
seq_lens_npu: tensor([1800, 1064], device='npu:0', dtype=torch.int32).
self.buffer.copy_(x, non_blocking=True)
(EngineCore_DP0 pid=626364) RuntimeError: The expanded size of the tensor (128) must match the existing size (2) at non-singleton dimension 0.  Target sizes: [128].  Tensor sizes: [2]

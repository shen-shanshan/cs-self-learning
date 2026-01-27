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

```

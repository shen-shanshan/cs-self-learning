# TODO

Add `torch_npu.npu.synchronize()`.

> In summary, copying data from CPU to GPU is safe when using `non_blocking=True`, but for any other direction, `non_blocking=True` can still be used but the user must make sure that a device synchronization is executed before the data is accessed.

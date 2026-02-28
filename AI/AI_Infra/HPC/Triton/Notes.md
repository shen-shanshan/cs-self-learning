# Notes

Triton requires us to perform operations on vectors.
Also, we don't need to and are not able to manage the shared memory. Triton does that automatically.

`block_id` -> `pid` (program id)
`grid = (n_blocks,)`
How many blocks do we have? can be 1d/2d/3d-tuple.

Launch kernel: `kernel_fn[grid](...)`

Debug -> simulation mode
When NOT simulating, we can't print or use breakpoints, as these don't exist on the GPU.

```python
os.environ['TRITON_INTERPRET'] = '1'  # Needs to be set before triton is imported.
```

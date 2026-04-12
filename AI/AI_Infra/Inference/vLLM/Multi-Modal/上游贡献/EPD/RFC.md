# Support Mooncake Based ECConnector for EPD

## Motivation

Find more details at the [design doc](https://docs.google.com/document/d/1mtvUWnzvJlc2CS6igbB2dTZ2UkjimMlBkp3dWWNfQYE/edit?usp=sharing).

> 参考：https://docs.google.com/document/d/13WD5A03CgPYbuY0fi1wNnp93fcgzSLeIeM_1ot2nIh8/edit?tab=t.0#heading=h.s97lelsb4s5p

## Proposed Change

E 实例后台线程：

- 异步保存；
- 监听 PD 实例发送请求，调用 mooncake transfer；

关键机制：当 E 节点的 buffer 不够需要清理（需要存入新的 EC）时，要先判断当前需要/正在传输的 mm_hash 有哪些（还不能被清理）。

buffer 的清理机制是什么？TODO：看代码。

## Future Work

Encoder Global Cache Manager https://github.com/sgl-project/sglang/pull/16137
Support Share Memory based EC Connector https://github.com/vllm-project/vllm/pull/33714
Configurable encoder compute and cache budget https://github.com/vllm-project/vllm/pull/34051
Add score encoder cache manager https://github.com/vllm-project/vllm/pull/38330
Support ViT Full CUDA Graph https://github.com/vllm-project/vllm/issues/38175

## Others

OOT compatible:

```python
ret_value = self.engine.initialize(
    hostname,
    "P2PHANDSHAKE",
    "tcp/rdma/ascend/...",
    device_name if device_name is not None else "",
)
```

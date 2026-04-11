# Support Mooncake Based ECConnector for EPD

## Motivation

Background:

Support EPD disaggregation https://github.com/sgl-project/sglang/pull/12263

## Proposed Change

Roadmap:

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

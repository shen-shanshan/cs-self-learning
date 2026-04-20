# 支持 Mixed Inputs

## Prompt

背景：vLLM PR https://github.com/vllm-project/vllm/pull/35963 和 https://github.com/vllm-project/vllm/pull/38061 分别为 Qwen3-VL 的 ViT 模块支持了 Full CUDA Graph 下的图像、视频推理，但是目前并不支持同一请求中既有image、又有video的混合输入。
需求：基于以上两个 PR，继续为 Qwen3-VL 的 ViT 模块设计并实现 image+video 混合输入时的 CUDA Graph 支持。

## Code Reading

```python
# vllm/vllm/v1/worker/gpu_model_runner.py
def _execute_mm_encoder(...):
    # ...
    for modality, num_items, mm_kwargs_batch in group_and_batch_mm_kwargs(...):
        # ...
        if enable_vit_cuda_graph:
            batch_outputs = self.encoder_cudagraph_manager.execute(mm_kwargs_batch)
        else:
            batch_outputs = model.embed_multimodal(**mm_kwargs_batch)


# vllm/vllm/multimodal/utils.py
def group_and_batch_mm_kwargs(mm_kwargs, ...):
    for modality, group in groupby(mm_kwargs, key=lambda x: x[0]):
        ...
        for num_items, mm_kwargs_batch in group_and_batch_mm_items(...):
            yield modality, num_items, mm_kwargs_batch
```

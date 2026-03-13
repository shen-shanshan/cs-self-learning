# torch.compile for ViT

## Test

```bash
# Unit Test
with-proxy pytest tests/compile/fullgraph/test_multimodal_compile.py::test_mllama4_vit_compilation
# Offline Test
with-proxy VLLM_USE_V1=1 python examples/offline_inference/vision_language.py -m llama4

# E2E Test
vllm serve ...\
--compilation-config='{"compile_mm_encoder":"true"}'
```

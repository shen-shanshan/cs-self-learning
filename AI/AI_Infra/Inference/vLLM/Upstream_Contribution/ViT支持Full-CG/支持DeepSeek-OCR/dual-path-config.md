# Add user-configurable global/local token budgets for dual-path ViT CUDA graph

## Prompts

背景：https://github.com/vllm-project/vllm/pull/43586 为 vLLM 中的 DeepSeek-OCR 模型支持了 dual-path ViT cuda graph，但是目前只支持根据 token_budgets 去自动生成 global_token_budgets 和 local_token_budgets，并不支持让用户可以根据自身需求分别设置 global_token_budgets 和 local_token_budgets。
需求：请为 vllm_config.compilation_config 新增两个用户配置（类型为 int list）：encoder_cudagraph_global_token_budgets 和 encoder_cudagraph_local_token_budgets，用于分别配置 global_token_budgets 和 local_token_budgets。当用户没有提供这两个配置时，EncoderCudaGraphManager 才调用 _generate_budgets() 去分别自动生成 global_token_budgets 和 local_token_budgets。

2.修改完代码之后，请根据最新的代码，更新相关的文档和测试用例。

## Test Results

```bash
source .venv/bin/activate

export VLLM_USE_MODELSCOPE=False
export HF_ENDPOINT="https://hf-mirror.com"
export VLLM_WORKER_MULTIPROC_METHOD=spawn
export PYTORCH_CUDA_ALLOC_CONF=expandable_segments:True
export CUDA_VISIBLE_DEVICES=7

# /shared/models/modelscope/models/deepseek-ai/DeepSeek-OCR
pytest -sv tests/models/multimodal/generation/test_vit_cudagraph.py::test_vit_cudagraph_image[deepseek_ocr]

pytest -sv tests/v1/cudagraph/test_encoder_cudagraph.py
# 60 passed, 17 warnings in 15.75s
```

```bash
# Before:
# Set {"encoder_cudagraph_token_budgets": [1152]} -> only support auto-infer for global/local token budgets
EncoderCudaGraphManager dual-path mode: global_budgets=[272, 544, 1088, 1152], local_budgets=[0, 100, 200, 400, 800, 1152]

# After:
# Set {"encoder_cudagraph_global_token_budgets": [272], "encoder_cudagraph_local_token_budgets": [0, 100, 200, 400, 800]}
EncoderCudaGraphManager dual-path mode: global_budgets=[272], local_budgets=[0, 100, 200, 400, 800]
```

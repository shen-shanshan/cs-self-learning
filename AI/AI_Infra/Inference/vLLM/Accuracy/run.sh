#!/usr/bin/env bash

# nohup python run.py \
# --model Qwen/Qwen3-8B \
# --output Qwen3-8B.md \
# --vllm_ascend_version v0.8.5rc1 \
# --cann_version 8.0.0 \
# --torch_npu_version 2.5.1 \
# --torch_version 2.5.1 \
# --vllm_version v0.8.5 > accuracy_1.log 2>&1 &

python run_accuracy.py \
--model Qwen/Qwen2.5-VL-7B-Instruct \
--output accuracy_result.md \
--vllm_ascend_version pr/736 \
--cann_version 8.1.RC1 \
--torch_npu_version 2.5.1 \
--torch_version 2.5.1 \
--vllm_version 0.9.0

export MODEL_ARGS='pretrained=Qwen/Qwen2.5-VL-7B-Instruct,max_model_len=8192,dtype=auto,tensor_parallel_size=2,max_images=2,enforce_eager=True'
lm_eval \
--model vllm-vlm \
--model_args $MODEL_ARGS \
--tasks mmmu_val \
--apply_chat_template \
--fewshot_as_multiturn \
--batch_size 1

#!/usr/bin/env bash

nohup python run.py \
--model Qwen/Qwen3-8B \
--output Qwen3-8B.md \
--vllm_ascend_version v0.8.5rc1 \
--cann_version 8.0.0 \
--torch_npu_version 2.5.1 \
--torch_version 2.5.1 \
--vllm_version v0.8.5 > accuracy_1.log 2>&1 &

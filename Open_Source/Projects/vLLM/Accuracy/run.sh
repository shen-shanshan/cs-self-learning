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

Traceback (most recent call last):
  File "/home/sss/software/miniconda3/envs/vllm-v1/bin/lm_eval", line 8, in <module>
    sys.exit(cli_evaluate())
  File "/home/sss/software/miniconda3/envs/vllm-v1/lib/python3.10/site-packages/lm_eval/__main__.py", line 389, in cli_evaluate
    results = evaluator.simple_evaluate(
  File "/home/sss/software/miniconda3/envs/vllm-v1/lib/python3.10/site-packages/lm_eval/utils.py", line 422, in _wrapper
    return fn(*args, **kwargs)
  File "/home/sss/software/miniconda3/envs/vllm-v1/lib/python3.10/site-packages/lm_eval/evaluator.py", line 209, in simple_evaluate
    lm = lm_eval.api.registry.get_model(model).create_from_arg_string(
  File "/home/sss/software/miniconda3/envs/vllm-v1/lib/python3.10/site-packages/lm_eval/api/model.py", line 151, in create_from_arg_string
    return cls(**args, **args2)
  File "/home/sss/software/miniconda3/envs/vllm-v1/lib/python3.10/site-packages/lm_eval/models/vllm_vlms.py", line 52, in __init__
    super().__init__(
  File "/home/sss/software/miniconda3/envs/vllm-v1/lib/python3.10/site-packages/lm_eval/models/vllm_causallms.py", line 107, in __init__
    self.model = LLM(**self.model_args)
NameError: name 'LLM' is not defined. Did you mean: 'VLLM'?

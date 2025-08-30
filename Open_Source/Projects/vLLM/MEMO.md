# vLLM MEMO

## Args

```bash
# 环境变量
VLLM_USE_V1=xxx
VLLM_USE_MODELSCOPE=xxx

# 启动参数（离线）
model="Qwen/QwQ-32B"
tensor_parallel_size=2
pipeline_parallel_size=2
distributed_executor_backend="mp"
max_model_len=4096  # Limit context window
max_num_seqs=4  # Limit batch size
enforce_eager=True
trust_remote_code=True
gpu_memory_utilization=0.9

# 启动参数（在线）
vllm serve Qwen/Qwen3-8B \
--max_model_len 16384 \
--max-num-batched-tokens 16384 \
--dtype bfloat16 \
--enforce-eager \
--trust-remote-code \

# vllm-ascend format
yapf -i <file>
isort <file>
ruff check <file>

# Clear process
ps -ef | grep vllm | cut -c 9-16 | xargs kill -9
ps -ef | grep python | cut -c 9-16 | xargs kill -9
```

## Structured Output

```bash
# e2e test
pytest -sv \
tests/v1/entrypoints/llm/test_struct_output_generate.py::test_structured_output
pytest -sv \
tests/e2e/singlecard/test_guided_decoding.py::test_guided_regex
# v0.9.1
pytest -sv tests/singlecard/test_guided_decoding.py


# Benchmark (with thinking disabled)
# 1.
vllm serve Qwen/Qwen3-1.7B \
--no-enable-prefix-caching
# 2.
python benchmarks/benchmark_serving_structured_output.py \
--backend vllm \
--model Qwen/Qwen3-1.7B \
--dataset json \
--structured-output-ratio 1.0 \
--request-rate 1000 \
--num-prompts 2000
# or:
python3 benchmarks/benchmark_serving_structured_output.py \
--backend vllm \
--model Qwen/Qwen3-1.7B \
--structured-output-ratio 1.0 \
--request-rate 100 \
--num-prompts 2000 \
--json-schema-path ./test3.json \
--output-len 2048


vllm serve /root/.cache/modelscope/hub/models/Qwen/Qwen2.5-0.5B-Instruct \
--max_model_len 26240 \
--pipeline-parallel-size 2

curl http://localhost:8000/v1/completions \
    -H "Content-Type: application/json" \
    -d '{
        "model": "/root/.cache/modelscope/hub/models/Qwen/Qwen2.5-0.5B-Instruct",
        "prompt": "Hello, my name is",
        "max_tokens": 7,
        "temperature": 0
    }'
```

## Spec Decode

```bash
pytest -sv \
tests/long_term/spec_decode/e2e/test_v1_spec_decode.py::test_ngram_correctness

VLLM_USE_V1=0 pytest -sv \
tests/e2e/long_term/spec_decode_v0/e2e/test_ngram_correctness.py::test_ngram_e2e_greedy_correctness

# Ngram
python /home/sss/github/vllm/examples/offline_inference/spec_decode.py \
--method ngram \
--num-spec-tokens 2 \
--prompt-lookup-max 5 \
--prompt-lookup-min 3 \
--model-dir /shared/cache/modelscope/hub/models/LLM-Research/Meta-Llama-3.1-8B-Instruct \
-tp 2
# Online
vllm serve /shared/cache/modelscope/hub/models/LLM-Research/Meta-Llama-3.1-8B-Instruct \
--max-model-len 1024 \
--speculative-config '{"method": "ngram", "num_speculative_tokens": 3, "prompt_lookup_max": 5, "prompt_lookup_min": 3}' \
--gpu_memory_utilization 0.9 \
--trust-remote-code \
--enforce-eager \
-tp 2

# Eagel 3
python /home/sss/github/vllm/examples/offline_inference/spec_decode.py \
--method eagle3 \
--num-spec-tokens 2 \
--model-dir /shared/cache/modelscope/hub/models/LLM-Research/Meta-Llama-3.1-8B-Instruct \
--eagle-dir /home/sss/models/models/models/vllm-ascend/EAGLE3-LLaMA3.1-Instruct-8B
# ValueError: Speculative tokens > 2 are not supported yet.
# Online
vllm serve /shared/cache/modelscope/hub/models/LLM-Research/Meta-Llama-3.1-8B-Instruct \
--max-model-len 1024 \
--speculative-config '{"method": "eagle3", "num_speculative_tokens": 2, "max_model_len": 128, "model": "/home/sss/models/models/models/vllm-ascend/EAGLE3-LLaMA3.1-Instruct-8B"}' \
--gpu_memory_utilization 0.9 \
--trust-remote-code \
--enforce-eager \
-tp 2

vllm serve /shared/cache/modelscope/hub/models/LLM-Research/Meta-Llama-3.1-8B-Instruct \
--max-model-len 1024 \
--speculative-config '{"method": "eagle", "num_speculative_tokens": 2, "model": "/home/sss/models/models/models/vllm-ascend/EAGLE3-LLaMA3.1-Instruct-8B"}' \
--gpu_memory_utilization 0.9 \
--trust-remote-code \
--enforce-eager \
-tp 2

# MTP
# Doc: https://vllm-ascend.readthedocs.io/en/latest/tutorials/multi_node.html
vllm serve /mnt/sfs_turbo/ascend-ci-share-nv-action-vllm-benchmarks/modelscope/hub/models/vllm-ascend/DeepSeek-V3-W8A8 \
--max-model-len 1024 \
--max-num-seqs 16 \
--no-enable-prefix-caching \
--tensor-parallel-size 4 \
--data_parallel_size 4 \
--enable_expert_parallel \
--speculative-config '{"method":"deepseek_mtp", "num_speculative_tokens": 1}' \
--quantization ascend \
--additional-config '{"ascend_scheduler_config": {"enabled": true, "enable_chunked_prefill": false}, "torchair_graph_config": {"enabled": true, "graph_batch_sizes": [16]}, "enable_weight_nz_layout": true}' \
--gpu_memory_utilization 0.9 \
--trust-remote-code
```

```bash
curl http://localhost:8000/v1/completions \
    -H "Content-Type: application/json" \
    -d '{
        "model": "/shared/cache/modelscope/hub/models/LLM-Research/Meta-Llama-3.1-8B-Instruct",
        "prompt": "The future of AI is",
        "max_tokens": 100,
        "temperature": 0
    }'
```

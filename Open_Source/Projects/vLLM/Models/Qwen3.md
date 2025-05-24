# Qwen3

---

## Tasks

**TODO:**

- 模型表示支持（v0.7.3），适配 `qwen3.py` 和 `qwen3_moe.py`，完成 ModelRegistry 注册；
- 端到端适配：kv_cache、block_table、attention_metadata；
- moe 算子调用方式替换；
- Qwen3 dense 模型 + mindie-turbo 打通。

**目标：**

- Qwen3 性能持平 Qwen2；
- Qwen3 moe 端到端可用。

vllm PR: https://github.com/vllm-project/vllm/pull/15289

---

## Qwen3

**思路：**

对比 vllm `v0.7.3` 和 `main` 的 `Qwen2` 实现，调整从 vllm main copy 过来的 `Qwen3` 实现以适配 `v0.7.3`。

**改动点：**

- Sampler
- kv_cache
- attn_metadata
- Qwen2: decoder_layer_type X

**测试模型：**

Qwen/Qwen3-0.6B (`/home/sss/.cache/modelscope/hub/models/Qwen/Qwen3-0.6B`)

**测试记录：**

```bash
export VLLM_USE_V1=0
export VLLM_USE_MODELSCOPE=true

# Offline inference
...

# Online inference
vllm serve /home/sss/.cache/modelscope/hub/models/Qwen/Qwen3-0.6B --max_model_len 26240
curl http://localhost:8000/v1/completions \
    -H "Content-Type: application/json" \
    -d '{
        "model": "/home/sss/.cache/modelscope/hub/models/Qwen/Qwen3-0.6B",
        "prompt": "The future of AI is",
        "max_tokens": 7,
        "temperature": 0
    }'

# Benchmark
pip install "modelscope<1.23.0" pandas datasets

Qwen/Qwen2-7B
Qwen/Qwen3-8B

python -m vllm.entrypoints.openai.api_server \
--model Qwen/Qwen3-8B \
--tensor-parallel-size 1 \
--swap-space 16 \
--disable-log-stats \
--disable-log-requests \
--load-format dummy

cd /home/sss/github/vllm-v0.7.3/vllm/benchmarks
python benchmark_serving.py \
--model Qwen/Qwen3-8B \
--dataset-name random \
--random-input-len 200 \
--num-prompts 200 \
--request-rate 1 \
--save-result --result-dir ./

# Accuracy test
pip install lm-eval
# 必需在这里面执行
/home/sss/github/vllm-v0.7.3/vllm-ascend
# tasks ceval-valid_computer_network gsm8k
lm_eval \
--model vllm \
--model_args pretrained=Qwen/Qwen3-8B,max_model_len=4096,dtype=auto,tensor_parallel_size=1,gpu_memory_utilization=0.8 \
--tasks ceval-valid_computer_network \
--apply_chat_template \
--fewshot_as_multiturn \
--batch_size auto \
--num_fewshot 5 \
--output ./
```

---

## Qwen3-moe

测试模型：Qwen/Qwen3-30B-A3B (qwen3_moe, path: `/home/sss/.cache/modelscope/hub/models/Qwen/Qwen3-30B-A3B`)

**Test:**

```bash
export VLLM_USE_V1=0

# Offline
Processed prompts: 100%|█████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████| 2/2 [00:02<00:00,  1.11s/it, est. speed input: 4.50 toks/s, output: 14.40 toks/s]
Prompt: 'Hello, my name is', Generated text: " Lucy and I'm from the UK. I'm 11 years old and"
Prompt: 'The future of AI is', Generated text: ' not a singular, monolithic entity but a mosaic of possibilities, each shaped by'

# Online
vllm serve /home/sss/.cache/modelscope/hub/models/Qwen/Qwen3-30B-A3B \
--max_model_len 26240 \
-tp 2 \
--distributed-executor-backend mp

curl http://localhost:8000/v1/completions \
    -H "Content-Type: application/json" \
    -d '{
        "model": "/home/sss/.cache/modelscope/hub/models/Qwen/Qwen3-30B-A3B",
        "prompt": "The future of AI is",
        "max_tokens": 7,
        "temperature": 0
    }'

{"id":"cmpl-b54fca60ff7744e29875ddfac9a1a9cc","object":"text_completion","created":1747812724,"model":"/home/sss/.cache/modelscope/hub/models/Qwen/Qwen3-30B-A3B","choices":[{"index":0,"text":" not just about the technology itself,","logprobs":null,"finish_reason":"length","stop_reason":null,"prompt_logprobs":null}],"usage":{"prompt_tokens":5,"total_tokens":12,"completion_tokens":7,"prompt_tokens_details":null}}

git diff c362709093f7ea36cf89c40512f419cc8cd350c7 3ba54fc3d23e90bc65758a5b43b9adf15179ce8a
```

---

## TODO

- [ ] 更新 vllm-ascend v0.7.3 Supported Models 文档：增加 Qwen3 and Qwen3-Moe；

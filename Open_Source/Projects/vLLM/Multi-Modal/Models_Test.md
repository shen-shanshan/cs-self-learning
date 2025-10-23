# Multi-Modal Models Test

## Preparation

```bash
git fetch --tags
git tag -l
# vllm
git checkout -b v0.11.0rc3 v0.11.0rc3
# vllm-ascend
git checkout -b v0.11.0rc0 v0.11.0rc0

tmux attach -t download

eval "$(/root/miniconda3/bin/conda shell.bash hook)"
conda activate vllm
```

## Acc Test

```bash
pip install lm-eval

export VLLM_USE_MODELSCOPE=False
export HF_ENDPOINT="https://hf-mirror.com"
lm_eval \
--model vllm-vlm \
--model_args pretrained=/root/.cache/modelscope/hub/models/Qwen/Qwen2.5-VL-7B-Instruct,max_model_len=4096 \
--tasks mmmu_val \
--batch_size auto
```

报错：

```bash
2025-10-20:09:40:15 INFO     [evaluator:574] Running generate_until requests
Adding requests:   0%|                                                                                                               | 0/900 [00:04<?, ?it/s]
Traceback (most recent call last):                                                                                                   | 0/900 [00:00<?, ?it/s]
  File "/root/miniconda3/envs/vllm/bin/lm_eval", line 7, in <module>
    sys.exit(cli_evaluate())
  File "/root/miniconda3/envs/vllm/lib/python3.10/site-packages/lm_eval/__main__.py", line 455, in cli_evaluate
    results = evaluator.simple_evaluate(
  File "/root/miniconda3/envs/vllm/lib/python3.10/site-packages/lm_eval/utils.py", line 456, in _wrapper
    return fn(*args, **kwargs)
  File "/root/miniconda3/envs/vllm/lib/python3.10/site-packages/lm_eval/evaluator.py", line 357, in simple_evaluate
    results = evaluate(
  File "/root/miniconda3/envs/vllm/lib/python3.10/site-packages/lm_eval/utils.py", line 456, in _wrapper
    return fn(*args, **kwargs)
  File "/root/miniconda3/envs/vllm/lib/python3.10/site-packages/lm_eval/evaluator.py", line 585, in evaluate
    resps = getattr(lm, reqtype)(cloned_reqs)
  File "/root/miniconda3/envs/vllm/lib/python3.10/site-packages/lm_eval/models/vllm_vlms.py", line 299, in generate_until
    cont = self._multimodal_model_generate(
  File "/root/miniconda3/envs/vllm/lib/python3.10/site-packages/lm_eval/models/vllm_vlms.py", line 154, in _multimodal_model_generate
    outputs = self.model.generate(
  File "/vllm-workspace/vllm/vllm/entrypoints/llm.py", line 393, in generate
    self._validate_and_add_requests(
  File "/vllm-workspace/vllm/vllm/entrypoints/llm.py", line 1516, in _validate_and_add_requests
    self._add_request(
  File "/vllm-workspace/vllm/vllm/entrypoints/llm.py", line 1569, in _add_request
    self.llm_engine.add_request(
  File "/vllm-workspace/vllm/vllm/v1/engine/llm_engine.py", line 230, in add_request
    prompt_str, request = self.processor.process_inputs(
  File "/vllm-workspace/vllm/vllm/v1/engine/processor.py", line 377, in process_inputs
    processed_inputs: ProcessorInputs = self.input_preprocessor.preprocess(
  File "/vllm-workspace/vllm/vllm/inputs/preprocess.py", line 644, in preprocess
    return self._process_decoder_only_prompt(
  File "/vllm-workspace/vllm/vllm/inputs/preprocess.py", line 614, in _process_decoder_only_prompt
    prompt_comps = self._prompt_to_llm_inputs(
  File "/vllm-workspace/vllm/vllm/inputs/preprocess.py", line 393, in _prompt_to_llm_inputs
    return self._process_text(
  File "/vllm-workspace/vllm/vllm/inputs/preprocess.py", line 343, in _process_text
    inputs = self._process_multimodal(
  File "/vllm-workspace/vllm/vllm/inputs/preprocess.py", line 242, in _process_multimodal
    mm_input = mm_processor.apply(
  File "/vllm-workspace/vllm/vllm/multimodal/processing.py", line 2045, in apply
    prompt_ids, prompt, mm_placeholders = self._maybe_apply_prompt_updates(
  File "/vllm-workspace/vllm/vllm/multimodal/processing.py", line 1997, in _maybe_apply_prompt_updates
    ) = self._apply_prompt_updates(
  File "/vllm-workspace/vllm/vllm/multimodal/processing.py", line 1919, in _apply_prompt_updates
    assert update_idx is not None, (
AssertionError: Failed to apply prompt replacement for mm_items['image'][0]
[ERROR] 2025-10-20-09:40:20 (PID:31615, Device:-1, RankID:-1) ERR99999 UNKNOWN applicaiton exception
```

## Qwen2-Audio-7B-Instruct

```python
from vllm.assets.audio import AudioAsset

audio_url = AudioAsset("winning_call").url
print(audio_url)
# https://vllm-public-assets.s3.us-west-2.amazonaws.com/multimodal_asset/winning_call.ogg
```

```bash
vllm serve /root/.cache/modelscope/models/Qwen/Qwen2-Audio-7B-Instruct \
--max_model_len 4096 \
--trust-remote-code \
--tensor-parallel-size 2 \
--limit-mm-per-prompt '{"audio":2}' \
--chat-template /home/sss/vllm/template_qwen2_audio.jinja \
--enforce-eager

curl -X POST http://localhost:8000/v1/chat/completions \
    -H "Content-Type: application/json" \
    -d '{
        "model": "/root/.cache/modelscope/models/Qwen/Qwen2-Audio-7B-Instruct",
        "messages": [
            {"role": "system", "content": "You are a helpful assistant."},
            {"role": "user", "content": [
                {"type": "audio_url", "audio_url": {"url": "https://vllm-public-assets.s3.us-west-2.amazonaws.com/multimodal_asset/winning_call.ogg"}},
                {"type": "text", "text": "What is in this audio? How does it sound?"}
            ]}
        ],
        "max_tokens": 100
    }'

# output:
{"id":"chatcmpl-31f5f698f6734a4297f6492a830edb3f","object":"chat.completion","created":1761097383,"model":"/root/.cache/modelscope/models/Qwen/Qwen2-Audio-7B-Instruct","choices":[{"index":0,"message":{"role":"assistant","content":"The audio contains a background of a crowd cheering, a ball bouncing, and an object being hit. A man speaks in English saying 'and the o one pitch on the way to edgar martinez swung on and lined out.' The speech has a happy mood.","refusal":null,"annotations":null,"audio":null,"function_call":null,"tool_calls":[],"reasoning_content":null},"logprobs":null,"finish_reason":"stop","stop_reason":null,"token_ids":null}],"service_tier":null,"system_fingerprint":null,"usage":{"prompt_tokens":689,"total_tokens":743,"completion_tokens":54,"prompt_tokens_details":null},"prompt_logprobs":null,"prompt_token_ids":null,"kv_transfer_params":null}
```

## Qwen2.5-VL-7B-Instruct

```bash
vllm serve /root/.cache/modelscope/hub/models/Qwen/Qwen2.5-VL-7B-Instruct \
--max_model_len 16384 \
--max-num-batched-tokens 16384 \
--tensor-parallel-size 2 \
--enforce-eager

curl -X POST http://localhost:8000/v1/chat/completions \
    -H "Content-Type: application/json" \
    -d '{
        "model": "/root/.cache/modelscope/hub/models/Qwen/Qwen2.5-VL-7B-Instruct",
        "messages": [
            {"role": "system", "content": "You are a helpful assistant."},
            {"role": "user", "content": [
                {"type": "image_url", "image_url": {"url": "https://modelscope.oss-cn-beijing.aliyuncs.com/resource/qwen.png"}},
                {"type": "text", "text": "What is the text in the illustrate? How does it look?"}
            ]}
        ],
        "max_tokens": 100
    }'

export VLLM_USE_MODELSCOPE=False
export HF_ENDPOINT="https://hf-mirror.com"
lm_eval \
--model vllm-vlm \
--model_args pretrained=/root/.cache/modelscope/hub/models/Qwen/Qwen2.5-VL-7B-Instruct,max_model_len=4096 \
--tasks mmmu_val \
--batch_size auto
```

## Qwen3-VL-8B-Instruct

```bash
vllm serve /root/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct \
--max_model_len 16384 \
--max-num-batched-tokens 16384 \
--tensor-parallel-size 2 \
--enforce-eager

curl -X POST http://localhost:8000/v1/chat/completions \
    -H "Content-Type: application/json" \
    -d '{
        "model": "/root/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct",
        "messages": [
            {"role": "system", "content": "You are a helpful assistant."},
            {"role": "user", "content": [
                {"type": "image_url", "image_url": {"url": "https://modelscope.oss-cn-beijing.aliyuncs.com/resource/qwen.png"}},
                {"type": "text", "text": "What is the text in the illustrate? How does it look?"}
            ]}
        ],
        "max_tokens": 100
    }'
```

## Qwen3-VL-30B-A3B-Instruct

```bash
vllm serve /root/.cache/modelscope/hub/models/Qwen/Qwen3-VL-30B-A3B-Instruct \
--max_model_len 16384 \
--max-num-batched-tokens 16384 \
--tensor-parallel-size 2 \
--enable-expert-parallel \
--enforce-eager

curl -X POST http://localhost:8000/v1/chat/completions \
    -H "Content-Type: application/json" \
    -d '{
        "model": "/root/.cache/modelscope/hub/models/Qwen/Qwen3-VL-30B-A3B-Instruct",
        "messages": [
            {"role": "system", "content": "You are a helpful assistant."},
            {"role": "user", "content": [
                {"type": "image_url", "image_url": {"url": "https://modelscope.oss-cn-beijing.aliyuncs.com/resource/qwen.png"}},
                {"type": "text", "text": "What is the text in the illustrate? How does it look?"}
            ]}
        ],
        "max_tokens": 100
    }'
```

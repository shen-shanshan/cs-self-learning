# What does this PR do?

This PR is a refactoring of model runner, to decouple it from the classes specifically designed for GPU.

## Changes

The changes of model runner are generally showed below:

## Testing

I have used `AI-ModelScope/gpt2` for testing `examples/offline_inference_npu.py`, and the results showed that it worked well.

The test logs are showed below:

```bash
INFO 02-05 09:08:46 __init__.py:30] Available plugins for group vllm.platform_plugins:
INFO 02-05 09:08:46 __init__.py:32] name=ascend, value=vllm_ascend:register
INFO 02-05 09:08:46 __init__.py:34] all available plugins for group vllm.platform_plugins will be loaded.
INFO 02-05 09:08:46 __init__.py:36] set environment variable VLLM_PLUGINS to control which plugins to load.
INFO 02-05 09:08:46 __init__.py:44] plugin ascend loaded.
INFO 02-05 09:08:46 __init__.py:177] Platform plugin ascend is activated
INFO 02-05 09:08:48 config.py:2383] Downcasting torch.float32 to torch.float16.
INFO 02-05 09:08:59 config.py:542] This model supports multiple tasks: {'generate', 'score', 'embed', 'reward', 'classify'}. Defaulting to 'generate'.
INFO 02-05 09:08:59 llm_engine.py:234] Initializing a V0 LLM engine (v0.1.dev1+gb3a0d01) with config: model='/home/sss/models/AI-ModelScope/gpt2', speculative_config=None, tokenizer='/home/sss/models/AI-ModelScope/gpt2', skip_tokenizer_init=False, tokenizer_mode=auto, revision=None, override_neuron_config=None, tokenizer_revision=None, trust_remote_code=False, dtype=torch.float16, max_seq_len=1024, download_dir=None, load_format=auto, tensor_parallel_size=1, pipeline_parallel_size=1, disable_custom_all_reduce=False, quantization=None, enforce_eager=False, kv_cache_dtype=auto,  device_config=npu, decoding_config=DecodingConfig(guided_decoding_backend='xgrammar'), observability_config=ObservabilityConfig(otlp_traces_endpoint=None, collect_model_forward_time=False, collect_model_execute_time=False), seed=0, served_model_name=/home/sss/models/AI-ModelScope/gpt2, num_scheduler_steps=1, multi_step_stream_outputs=True, enable_prefix_caching=False, chunked_prefill_enabled=False, use_async_output_proc=True, disable_mm_preprocessor_cache=False, mm_processor_kwargs=None, pooler_config=None, compilation_config={"splitting_ops":[],"compile_sizes":[],"cudagraph_capture_sizes":[256,248,240,232,224,216,208,200,192,184,176,168,160,152,144,136,128,120,112,104,96,88,80,72,64,56,48,40,32,24,16,8,4,2,1],"max_capture_size":256}, use_cached_outputs=False, 
WARNING 02-05 09:09:01 _custom_ops.py:21] Failed to import from vllm._C with ModuleNotFoundError("No module named 'vllm._C'")
INFO 02-05 09:09:01 importing.py:16] Triton not installed or not compatible; certain GPU-related functions will not be available.
Loading safetensors checkpoint shards:   0% Completed | 0/1 [00:00<?, ?it/s]
Loading safetensors checkpoint shards: 100% Completed | 1/1 [00:00<00:00,  3.18it/s]
Loading safetensors checkpoint shards: 100% Completed | 1/1 [00:00<00:00,  3.18it/s]

INFO 02-05 09:09:11 executor_base.py:110] # CPU blocks: 98557, # CPU blocks: 7281
INFO 02-05 09:09:11 executor_base.py:115] Maximum concurrency for 1024 tokens per request: 1539.95x
INFO 02-05 09:09:12 llm_engine.py:431] init engine (profile, create kv cache, warmup model) took 2.13 seconds
Processed prompts: 100%|██████████████████████████████████████████████████████████████████████████████████████| 4/4 [00:02<00:00,  1.53it/s, est. speed input: 8.41 toks/s, output: 152.97 toks/s]
Prompt: 'Hello, my name is', Generated text: " John. I'm a writer, and I'm a writer. I'm a writer. I'm a writer. I'm a writer. I'm a writer. I'm a writer. I'm a writer. I'm a writer. I'm a writer. I'm a writer. I'm a writer. I'm a writer. I'm a writer. I'm a writer. I'm a writer. I'm a writer. I'm a writer. I'm a writer. I'm"
Prompt: 'The president of the United States is', Generated text: ' States president. He is the president of the United States. He is the president of the United States. He is the president of the United States. He is the president of the United States. He is the president of the United States. He is the president of the United States. He is the president of the United States. He is the president of the United States. He is the president of the United States. He is the president of the United States. He is the president of the United'
Prompt: 'The capital of France is', Generated text: ' the capital of the French Republic, and the capital of the French Republic is the capital of the French Republic.\n\nThe French Republic is the capital of the French Republic.\n\nThe French Republic is the capital of the French Republic.\n\nThe French Republic is the capital of the French Republic.\n\nThe French Republic is the capital of the French Republic.\n\nThe French Republic is the capital of the French Republic.\n\nThe French Republic is the capital of the French Republic.'
Prompt: 'The future of AI is', Generated text: '\n\nThe future of AI is a question of how to make it work.\n\nThe future of AI is a question of how to make it work.\n\nThe future of AI is a question of how to make it work.\n\nThe future of AI is a question of how to make it work.\n\nThe future of AI is a question of how to make it work.\n\nThe future of AI is a question of how to make it work.\n\nThe future'
```

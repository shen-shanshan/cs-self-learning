# What does this PR do?

This PR is a refactoring of model runner, to avoid using class specifically designed for GPU.

## Changes

The changes of model runner are generally showed below:

## Testing

I have used `AI-ModelScope/gpt2` for testing `examples/offline_inference_npu.py`, and the results showed that it worked well.

The test logs are showed below:

```bash
INFO 01-20 13:11:55 __init__.py:179] Automatically detected platform cpu.
INFO 01-20 13:11:55 config.py:2285] Downcasting torch.float32 to torch.float16.
INFO 01-20 13:12:06 config.py:517] This model supports multiple tasks: {'score', 'generate', 'embed', 'reward', 'classify'}. Defaulting to 'generate'.
WARNING 01-20 13:12:06 config.py:651] Async output processing is not supported on the current platform type cpu.
WARNING 01-20 13:12:06 cpu.py:60] CUDA graph is not supported on CPU, fallback to the eager mode.
WARNING 01-20 13:12:06 cpu.py:75] Environment variable VLLM_CPU_KVCACHE_SPACE (GB) for CPU backend is not set, using 4 by default.
INFO 01-20 13:12:06 llm_engine.py:233] Initializing an LLM engine (v0.1.dev4047+g29b0919) with config: model='/home/sss/models/AI-ModelScope/gpt2', speculative_config=None, tokenizer='/home/sss/models/AI-ModelScope/gpt2', skip_tokenizer_init=False, tokenizer_mode=auto, revision=None, override_neuron_config=None, tokenizer_revision=None, trust_remote_code=False, dtype=torch.float16, max_seq_len=1024, download_dir=None, load_format=auto, tensor_parallel_size=1, pipeline_parallel_size=1, disable_custom_all_reduce=False, quantization=None, enforce_eager=True, kv_cache_dtype=auto, quantization_param_path=None, device_config=cpu, decoding_config=DecodingConfig(guided_decoding_backend='xgrammar'), observability_config=ObservabilityConfig(otlp_traces_endpoint=None, collect_model_forward_time=False, collect_model_execute_time=False), seed=0, served_model_name=/home/sss/models/AI-ModelScope/gpt2, num_scheduler_steps=1, multi_step_stream_outputs=True, enable_prefix_caching=False, chunked_prefill_enabled=False, use_async_output_proc=False, disable_mm_preprocessor_cache=False, mm_processor_kwargs=None, pooler_config=None, compilation_config={"splitting_ops":["vllm.unified_attention","vllm.unified_attention_with_output"],"candidate_compile_sizes":[],"compile_sizes":[],"capture_sizes":[256,248,240,232,224,216,208,200,192,184,176,168,160,152,144,136,128,120,112,104,96,88,80,72,64,56,48,40,32,24,16,8,4,2,1],"max_capture_size":256}, use_cached_outputs=False, 
INFO 01-20 13:12:08 cpu.py:36] Cannot use None backend on CPU.
INFO 01-20 13:12:08 cpu.py:37] Using Torch SDPA backend.
INFO 01-20 13:12:08 importing.py:14] Triton not installed or not compatible; certain GPU-related functions will not be available.
WARNING 01-20 13:12:08 custom_op.py:88] Custom op NewGELU is not supported on CPU, falling back to native.
Loading safetensors checkpoint shards:   0% Completed | 0/1 [00:00<?, ?it/s]
Loading safetensors checkpoint shards: 100% Completed | 1/1 [00:00<00:00,  3.06it/s]
Loading safetensors checkpoint shards: 100% Completed | 1/1 [00:00<00:00,  3.05it/s]

INFO 01-20 13:12:09 executor_base.py:80] # CPU blocks: 7281, # CPU blocks: 0
INFO 01-20 13:12:09 executor_base.py:85] Maximum concurrency for 1024 tokens per request: 113.77x
INFO 01-20 13:12:09 llm_engine.py:430] init engine (profile, create kv cache, warmup model) took 0.34 seconds
Processed prompts:   0%|                                                                                             | 0/4 [00:00<?, ?it/s, est. speed input: 0.00 toks/s, output: 0.00 toks/s]WARNING 01-20 13:12:10 cpu.py:141] Pin memory is not supported on CPU.
Processed prompts: 100%|████████████████████████████████████████████████████████████████████████████████████| 4/4 [00:13<00:00,  3.27s/it, est. speed input: 1.68 toks/s, output: 30.61 toks/s]
Prompt: 'Hello, my name is', Generated text: " John. I'm a writer, and I'm a writer. I'm a writer. I'm a writer. I'm a writer. I'm a writer. I'm a writer. I'm a writer. I'm a writer. I'm a writer. I'm a writer. I'm a writer. I'm a writer. I'm a writer. I'm a writer. I'm a writer. I'm a writer. I'm a writer. I'm a writer. I'm"
Prompt: 'The president of the United States is', Generated text: ' not a member of the Council of Europe, and he is not a member of the Council of the United Nations. He is not a member of the Council of the United Nations. He is not a member of the Council of the United Nations. He is not a member of the Council of the United Nations. He is not a member of the Council of the United Nations. He is not a member of the Council of the United Nations. He is not a member of the Council of the United Nations'
Prompt: 'The capital of France is', Generated text: ' the capital of the French Republic, and the capital of the French Republic is the capital of the French Republic.\n\nThe French Republic is the capital of the French Republic.\n\nThe French Republic is the capital of the French Republic.\n\nThe French Republic is the capital of the French Republic.\n\nThe French Republic is the capital of the French Republic.\n\nThe French Republic is the capital of the French Republic.\n\nThe French Republic is the capital of the French Republic.'
Prompt: 'The future of AI is', Generated text: ' in the hands of the next generation of AI.\n\nThe future of AI is in the hands of the next generation of AI.\n\nThe future of AI is in the hands of the next generation of AI.\n\nThe future of AI is in the hands of the next generation of AI.\n\nThe future of AI is in the hands of the next generation of AI.\n\nThe future of AI is in the hands of the next generation of AI.\n\nThe future of'
```

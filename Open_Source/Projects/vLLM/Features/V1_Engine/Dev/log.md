```bash
(vllm-v1) sss@ascend-01:~/github/vllm-project/vllm-ascend$ vllm serve Qwen/Qwen2.5-7B-Instruct --max_model_len 26240
INFO 03-11 06:17:27 [__init__.py:30] Available plugins for group vllm.platform_plugins:
INFO 03-11 06:17:27 [__init__.py:32] name=ascend, value=vllm_ascend:register
INFO 03-11 06:17:27 [__init__.py:34] all available plugins for group vllm.platform_plugins will be loaded.
INFO 03-11 06:17:27 [__init__.py:36] set environment variable VLLM_PLUGINS to control which plugins to load.
INFO 03-11 06:17:27 [__init__.py:44] plugin ascend loaded.
INFO 03-11 06:17:27 [__init__.py:247] Platform plugin ascend is activated
INFO 03-11 06:17:30 [__init__.py:30] Available plugins for group vllm.general_plugins:
INFO 03-11 06:17:30 [__init__.py:32] name=ascend_enhanced_model, value=vllm_ascend:register_model
INFO 03-11 06:17:30 [__init__.py:34] all available plugins for group vllm.general_plugins will be loaded.
INFO 03-11 06:17:30 [__init__.py:36] set environment variable VLLM_PLUGINS to control which plugins to load.
INFO 03-11 06:17:30 [__init__.py:44] plugin ascend_enhanced_model loaded.
WARNING 03-11 06:17:30 [_custom_ops.py:21] Failed to import from vllm._C with ModuleNotFoundError("No module named 'vllm._C'")
INFO 03-11 06:17:30 [importing.py:16] Triton not installed or not compatible; certain GPU-related functions will not be available.
WARNING 03-11 06:17:30 [registry.py:362] Model architecture Qwen2VLForConditionalGeneration is already registered, and will be overwritten by the new model class vllm_ascend.models.qwen2_vl:CustomQwen2VLForConditionalGeneration.
INFO 03-11 06:17:30 [api_server.py:912] vLLM API server version 0.7.4.dev360+gc91b64f7
INFO 03-11 06:17:30 [api_server.py:913] args: Namespace(subparser='serve', model_tag='Qwen/Qwen2.5-7B-Instruct', config='', host=None, port=8000, uvicorn_log_level='info', allow_credentials=False, allowed_origins=['*'], allowed_methods=['*'], allowed_headers=['*'], api_key=None, lora_modules=None, prompt_adapters=None, chat_template=None, chat_template_content_format='auto', response_role='assistant', ssl_keyfile=None, ssl_certfile=None, ssl_ca_certs=None, enable_ssl_refresh=False, ssl_cert_reqs=0, root_path=None, middleware=[], return_tokens_as_token_ids=False, disable_frontend_multiprocessing=False, enable_request_id_headers=False, enable_auto_tool_choice=False, tool_call_parser=None, tool_parser_plugin='', model='Qwen/Qwen2.5-7B-Instruct', task='auto', tokenizer=None, hf_config_path=None, skip_tokenizer_init=False, revision=None, code_revision=None, tokenizer_revision=None, tokenizer_mode='auto', trust_remote_code=False, allowed_local_media_path=None, download_dir=None, load_format='auto', config_format=<ConfigFormat.AUTO: 'auto'>, dtype='auto', kv_cache_dtype='auto', max_model_len=26240, guided_decoding_backend='xgrammar', logits_processor_pattern=None, model_impl='auto', distributed_executor_backend=None, pipeline_parallel_size=1, tensor_parallel_size=1, enable_expert_parallel=False, max_parallel_loading_workers=None, ray_workers_use_nsight=False, block_size=None, enable_prefix_caching=None, disable_sliding_window=False, use_v2_block_manager=True, num_lookahead_slots=0, seed=None, swap_space=4, cpu_offload_gb=0, gpu_memory_utilization=0.9, num_gpu_blocks_override=None, max_num_batched_tokens=None, max_num_partial_prefills=1, max_long_partial_prefills=1, long_prefill_token_threshold=0, max_num_seqs=None, max_logprobs=20, disable_log_stats=False, quantization=None, rope_scaling=None, rope_theta=None, hf_overrides=None, enforce_eager=False, max_seq_len_to_capture=8192, disable_custom_all_reduce=False, tokenizer_pool_size=0, tokenizer_pool_type='ray', tokenizer_pool_extra_config=None, limit_mm_per_prompt=None, mm_processor_kwargs=None, disable_mm_preprocessor_cache=False, enable_lora=False, enable_lora_bias=False, max_loras=1, max_lora_rank=16, lora_extra_vocab_size=256, lora_dtype='auto', long_lora_scaling_factors=None, max_cpu_loras=None, fully_sharded_loras=False, enable_prompt_adapter=False, max_prompt_adapters=1, max_prompt_adapter_token=0, device='auto', num_scheduler_steps=1, use_tqdm_on_load=True, multi_step_stream_outputs=True, scheduler_delay_factor=0.0, enable_chunked_prefill=None, speculative_model=None, speculative_model_quantization=None, num_speculative_tokens=None, speculative_disable_mqa_scorer=False, speculative_draft_tensor_parallel_size=None, speculative_max_model_len=None, speculative_disable_by_batch_size=None, ngram_prompt_lookup_max=None, ngram_prompt_lookup_min=None, spec_decoding_acceptance_method='rejection_sampler', typical_acceptance_sampler_posterior_threshold=None, typical_acceptance_sampler_posterior_alpha=None, disable_logprobs_during_spec_decoding=None, model_loader_extra_config=None, ignore_patterns=[], preemption_mode=None, served_model_name=None, qlora_adapter_name_or_path=None, show_hidden_metrics_for_version=None, otlp_traces_endpoint=None, collect_detailed_traces=None, disable_async_output_proc=False, scheduling_policy='fcfs', scheduler_cls='vllm.core.scheduler.Scheduler', override_neuron_config=None, override_pooler_config=None, compilation_config=None, kv_transfer_config=None, worker_cls='auto', worker_extension_cls='', generation_config='auto', override_generation_config=None, enable_sleep_mode=False, calculate_kv_scales=False, additional_config=None, enable_reasoning=False, reasoning_parser=None, disable_log_requests=False, max_log_len=None, disable_fastapi_docs=False, enable_prompt_tokens_details=False, dispatch_function=<function ServeSubcommand.cmd at 0xfffd4c4de4d0>)
WARNING 03-11 06:17:38 [arg_utils.py:1473] Setting max_num_batched_tokens to 2048 for OPENAI_API_SERVER usage context.
Downloading Model to directory: /home/sss/cache/modelscope/models/Qwen/Qwen2.5-7B-Instruct
2025-03-11 06:17:39,510 - modelscope - WARNING - Using branch: master as version is unstable, use with caution
2025-03-11 06:17:39,857 - modelscope - INFO - Target directory already exists, skipping creation.
Downloading Model to directory: /home/sss/cache/modelscope/models/Qwen/Qwen2.5-7B-Instruct
2025-03-11 06:17:40,215 - modelscope - WARNING - Using branch: master as version is unstable, use with caution
2025-03-11 06:17:40,638 - modelscope - INFO - Target directory already exists, skipping creation.
INFO 03-11 06:17:52 [config.py:576] This model supports multiple tasks: {'score', 'embed', 'classify', 'generate', 'reward'}. Defaulting to 'generate'.
INFO 03-11 06:17:52 [config.py:1666] Chunked prefill is enabled with max_num_batched_tokens=2048.
[V1][NPU] Disable prefix caching
Downloading Model to directory: /home/sss/cache/modelscope/models/Qwen/Qwen2.5-7B-Instruct
2025-03-11 06:17:53,715 - modelscope - INFO - Target directory already exists, skipping creation.
Downloading Model to directory: /home/sss/cache/modelscope/models/Qwen/Qwen2.5-7B-Instruct
2025-03-11 06:17:54,715 - modelscope - WARNING - Using branch: master as version is unstable, use with caution
2025-03-11 06:17:55,164 - modelscope - INFO - Target directory already exists, skipping creation.
INFO 03-11 06:18:03 [__init__.py:30] Available plugins for group vllm.platform_plugins:
INFO 03-11 06:18:03 [__init__.py:32] name=ascend, value=vllm_ascend:register
INFO 03-11 06:18:03 [__init__.py:34] all available plugins for group vllm.platform_plugins will be loaded.
INFO 03-11 06:18:03 [__init__.py:36] set environment variable VLLM_PLUGINS to control which plugins to load.
INFO 03-11 06:18:03 [__init__.py:44] plugin ascend loaded.
INFO 03-11 06:18:03 [__init__.py:247] Platform plugin ascend is activated
INFO 03-11 06:18:06 [core.py:51] Initializing a V1 LLM engine (v0.7.4.dev360+gc91b64f7) with config: model='Qwen/Qwen2.5-7B-Instruct', speculative_config=None, tokenizer='Qwen/Qwen2.5-7B-Instruct', skip_tokenizer_init=False, tokenizer_mode=auto, revision=None, override_neuron_config=None, tokenizer_revision=None, trust_remote_code=False, dtype=torch.bfloat16, max_seq_len=26240, download_dir=None, load_format=auto, tensor_parallel_size=1, pipeline_parallel_size=1, disable_custom_all_reduce=False, quantization=None, enforce_eager=False, kv_cache_dtype=auto,  device_config=npu, decoding_config=DecodingConfig(guided_decoding_backend='xgrammar', reasoning_backend=None), observability_config=ObservabilityConfig(show_hidden_metrics=False, otlp_traces_endpoint=None, collect_model_forward_time=False, collect_model_execute_time=False), seed=None, served_model_name=Qwen/Qwen2.5-7B-Instruct, num_scheduler_steps=1, multi_step_stream_outputs=True, enable_prefix_caching=False, chunked_prefill_enabled=True, use_async_output_proc=True, disable_mm_preprocessor_cache=False, mm_processor_kwargs=None, pooler_config=None, compilation_config={"level":0,"custom_ops":["none"],"splitting_ops":["vllm.unified_attention","vllm.unified_attention_with_output"],"use_inductor":true,"compile_sizes":[],"use_cudagraph":true,"cudagraph_num_of_warmups":1,"cudagraph_capture_sizes":[512,504,496,488,480,472,464,456,448,440,432,424,416,408,400,392,384,376,368,360,352,344,336,328,320,312,304,296,288,280,272,264,256,248,240,232,224,216,208,200,192,184,176,168,160,152,144,136,128,120,112,104,96,88,80,72,64,56,48,40,32,24,16,8,4,2,1],"max_capture_size":512}
INFO 03-11 06:18:06 [__init__.py:30] Available plugins for group vllm.general_plugins:
INFO 03-11 06:18:06 [__init__.py:32] name=ascend_enhanced_model, value=vllm_ascend:register_model
INFO 03-11 06:18:06 [__init__.py:34] all available plugins for group vllm.general_plugins will be loaded.
INFO 03-11 06:18:06 [__init__.py:36] set environment variable VLLM_PLUGINS to control which plugins to load.
INFO 03-11 06:18:06 [__init__.py:44] plugin ascend_enhanced_model loaded.
WARNING 03-11 06:18:06 [_custom_ops.py:21] Failed to import from vllm._C with ModuleNotFoundError("No module named 'vllm._C'")
INFO 03-11 06:18:06 [importing.py:16] Triton not installed or not compatible; certain GPU-related functions will not be available.
WARNING 03-11 06:18:06 [registry.py:362] Model architecture Qwen2VLForConditionalGeneration is already registered, and will be overwritten by the new model class vllm_ascend.models.qwen2_vl:CustomQwen2VLForConditionalGeneration.
WARNING 03-11 06:18:06 [utils.py:2304] Methods add_lora,cache_config,determine_available_memory,determine_num_available_blocks,device_config,get_cache_block_size_bytes,list_loras,load_config,pin_lora,remove_lora,scheduler_config,speculative_config not implemented in <vllm_ascend.v1.npu_worker.NPUWorker object at 0xfffe673357e0>
INFO 03-11 06:18:18 [__init__.py:30] Available plugins for group vllm.platform_plugins:
INFO 03-11 06:18:18 [__init__.py:32] name=ascend, value=vllm_ascend:register
INFO 03-11 06:18:18 [__init__.py:34] all available plugins for group vllm.platform_plugins will be loaded.
INFO 03-11 06:18:18 [__init__.py:36] set environment variable VLLM_PLUGINS to control which plugins to load.
INFO 03-11 06:18:18 [__init__.py:44] plugin ascend loaded.
INFO 03-11 06:18:18 [__init__.py:247] Platform plugin ascend is activated
INFO 03-11 06:18:25 [parallel_state.py:948] rank 0 in world size 1 is assigned as DP rank 0, PP rank 0, TP rank 0
WARNING 03-11 06:18:25 [topk_topp_sampler.py:46] FlashInfer is not available. Falling back to the PyTorch-native implementation of top-p & top-k sampling. For the best performance, please install FlashInfer.
Downloading Model to directory: /home/sss/cache/modelscope/models/Qwen/Qwen2.5-7B-Instruct
2025-03-11 06:18:26,783 - modelscope - INFO - Target directory already exists, skipping creation.
Loading safetensors checkpoint shards:   0% Completed | 0/4 [00:00<?, ?it/s]
Loading safetensors checkpoint shards:  25% Completed | 1/4 [00:00<00:01,  1.81it/s]
Loading safetensors checkpoint shards:  50% Completed | 2/4 [00:01<00:01,  1.39it/s]
Loading safetensors checkpoint shards:  75% Completed | 3/4 [00:02<00:00,  1.31it/s]
Loading safetensors checkpoint shards: 100% Completed | 4/4 [00:03<00:00,  1.24it/s]
Loading safetensors checkpoint shards: 100% Completed | 4/4 [00:03<00:00,  1.30it/s]

INFO 03-11 06:18:30 [loader.py:429] Loading weights took 3.40 seconds
INFO 03-11 06:18:32 [kv_cache_utils.py:537] GPU KV cache size: 744,048 tokens
INFO 03-11 06:18:32 [kv_cache_utils.py:540] Maximum concurrency for 26,240 tokens per request: 28.36x
npu not support graph capture. current compilation level : CompilationLevel.NO_COMPILATION
INFO 03-11 06:18:32 [core.py:120] init engine (profile, create kv cache, warmup model) took 2.06 seconds
Downloading Model to directory: /home/sss/cache/modelscope/models/Qwen/Qwen2.5-7B-Instruct
2025-03-11 06:18:33,454 - modelscope - INFO - Target directory already exists, skipping creation.
Downloading Model to directory: /home/sss/cache/modelscope/models/Qwen/Qwen2.5-7B-Instruct
2025-03-11 06:18:36,350 - modelscope - WARNING - Using branch: master as version is unstable, use with caution
2025-03-11 06:18:36,698 - modelscope - INFO - Target directory already exists, skipping creation.
INFO 03-11 06:18:36 [serving_chat.py:114] Using default chat sampling params from model: {'repetition_penalty': 1.05, 'temperature': 0.7, 'top_k': 20, 'top_p': 0.8}
Downloading Model to directory: /home/sss/cache/modelscope/models/Qwen/Qwen2.5-7B-Instruct
2025-03-11 06:18:37,137 - modelscope - WARNING - Using branch: master as version is unstable, use with caution
2025-03-11 06:18:37,454 - modelscope - INFO - Target directory already exists, skipping creation.
INFO 03-11 06:18:37 [serving_completion.py:60] Using default completion sampling params from model: {'repetition_penalty': 1.05, 'temperature': 0.7, 'top_k': 20, 'top_p': 0.8}
INFO 03-11 06:18:37 [api_server.py:958] Starting vLLM API server on http://0.0.0.0:8000
INFO 03-11 06:18:37 [launcher.py:26] Available routes are:
INFO 03-11 06:18:37 [launcher.py:34] Route: /openapi.json, Methods: GET, HEAD
INFO 03-11 06:18:37 [launcher.py:34] Route: /docs, Methods: GET, HEAD
INFO 03-11 06:18:37 [launcher.py:34] Route: /docs/oauth2-redirect, Methods: GET, HEAD
INFO 03-11 06:18:37 [launcher.py:34] Route: /redoc, Methods: GET, HEAD
INFO 03-11 06:18:37 [launcher.py:34] Route: /health, Methods: GET
INFO 03-11 06:18:37 [launcher.py:34] Route: /ping, Methods: GET, POST
INFO 03-11 06:18:37 [launcher.py:34] Route: /tokenize, Methods: POST
INFO 03-11 06:18:37 [launcher.py:34] Route: /detokenize, Methods: POST
INFO 03-11 06:18:37 [launcher.py:34] Route: /v1/models, Methods: GET
INFO 03-11 06:18:37 [launcher.py:34] Route: /version, Methods: GET
INFO 03-11 06:18:37 [launcher.py:34] Route: /v1/chat/completions, Methods: POST
INFO 03-11 06:18:37 [launcher.py:34] Route: /v1/completions, Methods: POST
INFO 03-11 06:18:37 [launcher.py:34] Route: /v1/embeddings, Methods: POST
INFO 03-11 06:18:37 [launcher.py:34] Route: /pooling, Methods: POST
INFO 03-11 06:18:37 [launcher.py:34] Route: /score, Methods: POST
INFO 03-11 06:18:37 [launcher.py:34] Route: /v1/score, Methods: POST
INFO 03-11 06:18:37 [launcher.py:34] Route: /v1/audio/transcriptions, Methods: POST
INFO 03-11 06:18:37 [launcher.py:34] Route: /rerank, Methods: POST
INFO 03-11 06:18:37 [launcher.py:34] Route: /v1/rerank, Methods: POST
INFO 03-11 06:18:37 [launcher.py:34] Route: /v2/rerank, Methods: POST
INFO 03-11 06:18:37 [launcher.py:34] Route: /invocations, Methods: POST
INFO:     Started server process [402008]
INFO:     Waiting for application startup.
INFO:     Application startup complete.
INFO 03-11 06:18:48 [loggers.py:80] Avg prompt throughput: 0.0 tokens/s, Avg generation throughput: 0.0 tokens/s, Running: 0 reqs, Waiting: 0 reqs, GPU KV cache usage: 0.0%, Prefix cache hit rate: 0.0%
INFO 03-11 06:18:50 [logger.py:39] Received request cmpl-02f7ed907eda4ad590c0fa925b945247-0: prompt: 'The future of AI is', params: SamplingParams(n=1, presence_penalty=0.0, frequency_penalty=0.0, repetition_penalty=1.05, temperature=0.0, top_p=1.0, top_k=-1, min_p=0.0, seed=None, stop=[], stop_token_ids=[], bad_words=[], include_stop_str_in_output=False, ignore_eos=False, max_tokens=7, min_tokens=0, logprobs=None, prompt_logprobs=None, skip_special_tokens=True, spaces_between_special_tokens=True, truncate_prompt_tokens=None, guided_decoding=None, extra_args=None), prompt_token_ids: [785, 3853, 315, 15235, 374], lora_request: None, prompt_adapter_request: None.
INFO 03-11 06:18:50 [async_llm.py:169] Added request cmpl-02f7ed907eda4ad590c0fa925b945247-0.
INFO:     127.0.0.1:46928 - "POST /v1/completions HTTP/1.1" 200 OK
INFO 03-11 06:18:58 [loggers.py:80] Avg prompt throughput: 0.5 tokens/s, Avg generation throughput: 0.7 tokens/s, Running: 0 reqs, Waiting: 0 reqs, GPU KV cache usage: 0.0%, Prefix cache hit rate: 0.0%
INFO 03-11 06:19:08 [loggers.py:80] Avg prompt throughput: 0.0 tokens/s, Avg generation throughput: 0.0 tokens/s, Running: 0 reqs, Waiting: 0 reqs, GPU KV cache usage: 0.0%, Prefix cache hit rate: 0.0%
INFO 03-11 06:19:18 [loggers.py:80] Avg prompt throughput: 0.0 tokens/s, Avg generation throughput: 0.0 tokens/s, Running: 0 reqs, Waiting: 0 reqs, GPU KV cache usage: 0.0%, Prefix cache hit rate: 0.0%
INFO 03-11 06:19:28 [loggers.py:80] Avg prompt throughput: 0.0 tokens/s, Avg generation throughput: 0.0 tokens/s, Running: 0 reqs, Waiting: 0 reqs, GPU KV cache usage: 0.0%, Prefix cache hit rate: 0.0%
INFO 03-11 06:19:38 [loggers.py:80] Avg prompt throughput: 0.0 tokens/s, Avg generation throughput: 0.0 tokens/s, Running: 0 reqs, Waiting: 0 reqs, GPU KV cache usage: 0.0%, Prefix cache hit rate: 0.0%
INFO 03-11 06:19:48 [loggers.py:80] Avg prompt throughput: 0.0 tokens/s, Avg generation throughput: 0.0 tokens/s, Running: 0 reqs, Waiting: 0 reqs, GPU KV cache usage: 0.0%, Prefix cache hit rate: 0.0%
INFO 03-11 06:19:58 [loggers.py:80] Avg prompt throughput: 0.0 tokens/s, Avg generation throughput: 0.0 tokens/s, Running: 0 reqs, Waiting: 0 reqs, GPU KV cache usage: 0.0%, Prefix cache hit rate: 0.0%
INFO 03-11 06:20:08 [loggers.py:80] Avg prompt throughput: 0.0 tokens/s, Avg generation throughput: 0.0 tokens/s, Running: 0 reqs, Waiting: 0 reqs, GPU KV cache usage: 0.0%, Prefix cache hit rate: 0.0%
INFO 03-11 06:20:18 [loggers.py:80] Avg prompt throughput: 0.0 tokens/s, Avg generation throughput: 0.0 tokens/s, Running: 0 reqs, Waiting: 0 reqs, GPU KV cache usage: 0.0%, Prefix cache hit rate: 0.0%


(base) sss@ascend-01:~/github/vllm-project$ curl http://localhost:8000/v1/completions \
    -H "Content-Type: application/json" \
    -d '{
        "model": "Qwen/Qwen2.5-7B-Instruct",
        "prompt": "The future of AI is",
        "max_tokens": 7,
        "temperature": 0
    }'
{"id":"cmpl-02f7ed907eda4ad590c0fa925b945247","object":"text_completion","created":1741673930,"model":"Qwen/Qwen2.5-7B-Instruct","choices":[{"index":0,"text":" here. It’s not just a","logprobs":null,"finish_reason":"length","stop_reason":null,"prompt_logprobs":null}],"usage":{"prompt_tokens":5
(base)
```

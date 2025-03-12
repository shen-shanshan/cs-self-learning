```bash
(vllm-v1) sss@ascend-01:~/github/vllm-project/vllm/tests/core/block/e2e$ pytest test_correctness_sliding_window.py -sv
INFO 03-12 02:36:54 [__init__.py:30] Available plugins for group vllm.platform_plugins:
INFO 03-12 02:36:54 [__init__.py:32] name=ascend, value=vllm_ascend:register
INFO 03-12 02:36:54 [__init__.py:34] all available plugins for group vllm.platform_plugins will be loaded.
INFO 03-12 02:36:54 [__init__.py:36] set environment variable VLLM_PLUGINS to control which plugins to load.
INFO 03-12 02:36:54 [__init__.py:44] plugin ascend loaded.
INFO 03-12 02:36:54 [__init__.py:247] Platform plugin ascend is activated
================================================================================= test session starts ==================================================================================
platform linux -- Python 3.10.16, pytest-8.3.5, pluggy-1.5.0 -- /home/sss/software/miniconda3/envs/vllm-v1/bin/python
cachedir: .pytest_cache
rootdir: /home/sss/github/vllm-project/vllm
configfile: pyproject.toml
plugins: anyio-4.8.0
collected 6 items                                                                                                                                                                      

test_correctness_sliding_window.py::test_sliding_window_retrival[FLASH_ATTN-1-5-test_llm_kwargs0-baseline_llm_kwargs0-per_test_common_llm_kwargs0-common_llm_kwargs0] INFO 03-12 02:36:55 [__init__.py:30] Available plugins for group vllm.general_plugins:
INFO 03-12 02:36:55 [__init__.py:32] name=ascend_enhanced_model, value=vllm_ascend:register_model
INFO 03-12 02:36:55 [__init__.py:34] all available plugins for group vllm.general_plugins will be loaded.
INFO 03-12 02:36:55 [__init__.py:36] set environment variable VLLM_PLUGINS to control which plugins to load.
INFO 03-12 02:36:55 [__init__.py:44] plugin ascend_enhanced_model loaded.
WARNING 03-12 02:36:55 [_custom_ops.py:21] Failed to import from vllm._C with ModuleNotFoundError("No module named 'vllm._C'")
INFO 03-12 02:36:56 [importing.py:16] Triton not installed or not compatible; certain GPU-related functions will not be available.
WARNING 03-12 02:36:57 [registry.py:362] Model architecture Qwen2VLForConditionalGeneration is already registered, and will be overwritten by the new model class vllm_ascend.models.qwen2_vl:CustomQwen2VLForConditionalGeneration.
config.json: 700B [00:00, 3.01MB/s]                                                                                                                                                     
INFO 03-12 02:36:58 [config.py:2563] Downcasting torch.float32 to torch.float16.
INFO 03-12 02:37:10 [config.py:576] This model supports multiple tasks: {'generate', 'classify', 'embed', 'score', 'reward'}. Defaulting to 'generate'.
INFO 03-12 02:37:10 [llm_engine.py:235] Initializing a V0 LLM engine (v0.7.4.dev360+gc91b64f7) with config: model='bigcode/starcoder2-3b', speculative_config=None, tokenizer='bigcode/starcoder2-3b', skip_tokenizer_init=False, tokenizer_mode=auto, revision=None, override_neuron_config=None, tokenizer_revision=None, trust_remote_code=False, dtype=torch.float16, max_seq_len=16384, download_dir=None, load_format=auto, tensor_parallel_size=1, pipeline_parallel_size=1, disable_custom_all_reduce=False, quantization=None, enforce_eager=True, kv_cache_dtype=auto,  device_config=npu, decoding_config=DecodingConfig(guided_decoding_backend='xgrammar', reasoning_backend=None), observability_config=ObservabilityConfig(show_hidden_metrics=False, otlp_traces_endpoint=None, collect_model_forward_time=False, collect_model_execute_time=False), seed=None, served_model_name=bigcode/starcoder2-3b, num_scheduler_steps=1, multi_step_stream_outputs=True, enable_prefix_caching=False, chunked_prefill_enabled=False, use_async_output_proc=True, disable_mm_preprocessor_cache=False, mm_processor_kwargs=None, pooler_config=None, compilation_config={"splitting_ops":[],"compile_sizes":[],"cudagraph_capture_sizes":[],"max_capture_size":0}, use_cached_outputs=False, 
tokenizer_config.json: 7.88kB [00:00, 18.6MB/s]                                                                                                                                         
vocab.json: 777kB [00:00, 2.05MB/s]
merges.txt: 442kB [00:00, 1.18MB/s]
tokenizer.json: 2.06MB [00:00, 3.13MB/s]
special_tokens_map.json: 958B [00:00, 4.85MB/s]                                                                                                                                         
WARNING 03-12 02:37:15 [utils.py:2304] Methods add_lora,add_prompt_adapter,cache_config,compilation_config,current_platform,list_loras,list_prompt_adapters,load_config,pin_lora,pin_prompt_adapter,remove_lora,remove_prompt_adapter not implemented in <vllm_ascend.worker.worker.NPUWorker object at 0xfffd1aec3c10>
INFO 03-12 02:37:23 [parallel_state.py:948] rank 0 in world size 1 is assigned as DP rank 0, PP rank 0, TP rank 0
INFO 03-12 02:37:24 [weight_utils.py:257] Using model weights format ['*.safetensors']
model.safetensors: 100%|███████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████| 12.1G/12.1G [30:29<00:00, 6.62MB/s]
INFO 03-12 03:07:56 [weight_utils.py:273] Time spent downloading weights for bigcode/starcoder2-3b: 1831.535092 seconds
INFO 03-12 03:07:57 [weight_utils.py:307] No model.safetensors.index.json found in remote.
Loading safetensors checkpoint shards:   0% Completed | 0/1 [00:00<?, ?it/s]
Loading safetensors checkpoint shards: 100% Completed | 1/1 [00:01<00:00,  1.22s/it]
Loading safetensors checkpoint shards: 100% Completed | 1/1 [00:01<00:00,  1.23s/it]

INFO 03-12 03:07:59 [loader.py:429] Loading weights took 2.18 seconds
INFO 03-12 03:08:03 [llm_engine.py:430] Overriding num_gpu_blocks=103766 with num_gpu_blocks_override=6250
INFO 03-12 03:08:03 [executor_base.py:111] # npu blocks: 6250, # CPU blocks: 8738
INFO 03-12 03:08:03 [executor_base.py:116] Maximum concurrency for 16384 tokens per request: 6.10x
INFO 03-12 03:08:03 [llm_engine.py:441] init engine (profile, create kv cache, warmup model) took 3.93 seconds
Processed prompts: 100%|█████████████████████████████████████████████████████████████████████████| 5/5 [00:36<00:00,  7.27s/it, est. speed input: 1168.13 toks/s, output: 140.94 toks/s]
WARNING 03-12 03:08:41 [parallel_state.py:1087] torch._C._host_emptyCache() only available in Pytorch >=2.5
FAILED
test_correctness_sliding_window.py::test_sliding_window_retrival[FLASHINFER-1-5-test_llm_kwargs0-baseline_llm_kwargs0-per_test_common_llm_kwargs0-common_llm_kwargs0] FAILED
test_correctness_sliding_window.py::test_sliding_window_retrival[XFORMERS-1-5-test_llm_kwargs0-baseline_llm_kwargs0-per_test_common_llm_kwargs0-common_llm_kwargs0] INFO 03-12 03:08:42 [config.py:2563] Downcasting torch.float32 to torch.float16.
INFO 03-12 03:08:42 [config.py:576] This model supports multiple tasks: {'generate', 'classify', 'embed', 'score', 'reward'}. Defaulting to 'generate'.
INFO 03-12 03:08:42 [llm_engine.py:235] Initializing a V0 LLM engine (v0.7.4.dev360+gc91b64f7) with config: model='bigcode/starcoder2-3b', speculative_config=None, tokenizer='bigcode/starcoder2-3b', skip_tokenizer_init=False, tokenizer_mode=auto, revision=None, override_neuron_config=None, tokenizer_revision=None, trust_remote_code=False, dtype=torch.float16, max_seq_len=16384, download_dir=None, load_format=auto, tensor_parallel_size=1, pipeline_parallel_size=1, disable_custom_all_reduce=False, quantization=None, enforce_eager=True, kv_cache_dtype=auto,  device_config=npu, decoding_config=DecodingConfig(guided_decoding_backend='xgrammar', reasoning_backend=None), observability_config=ObservabilityConfig(show_hidden_metrics=False, otlp_traces_endpoint=None, collect_model_forward_time=False, collect_model_execute_time=False), seed=None, served_model_name=bigcode/starcoder2-3b, num_scheduler_steps=1, multi_step_stream_outputs=True, enable_prefix_caching=False, chunked_prefill_enabled=False, use_async_output_proc=True, disable_mm_preprocessor_cache=False, mm_processor_kwargs=None, pooler_config=None, compilation_config={"splitting_ops":[],"compile_sizes":[],"cudagraph_capture_sizes":[],"max_capture_size":0}, use_cached_outputs=False, 
WARNING 03-12 03:08:43 [utils.py:2304] Methods add_lora,add_prompt_adapter,cache_config,compilation_config,current_platform,list_loras,list_prompt_adapters,load_config,pin_lora,pin_prompt_adapter,remove_lora,remove_prompt_adapter not implemented in <vllm_ascend.worker.worker.NPUWorker object at 0xfffca219a2c0>
INFO 03-12 03:08:44 [parallel_state.py:948] rank 0 in world size 1 is assigned as DP rank 0, PP rank 0, TP rank 0
INFO 03-12 03:08:44 [weight_utils.py:257] Using model weights format ['*.safetensors']
INFO 03-12 03:08:45 [weight_utils.py:307] No model.safetensors.index.json found in remote.
Loading safetensors checkpoint shards:   0% Completed | 0/1 [00:00<?, ?it/s]
Loading safetensors checkpoint shards: 100% Completed | 1/1 [00:01<00:00,  1.29s/it]
Loading safetensors checkpoint shards: 100% Completed | 1/1 [00:01<00:00,  1.29s/it]

INFO 03-12 03:08:47 [loader.py:429] Loading weights took 2.79 seconds
INFO 03-12 03:08:49 [llm_engine.py:430] Overriding num_gpu_blocks=104172 with num_gpu_blocks_override=6250
INFO 03-12 03:08:49 [executor_base.py:111] # npu blocks: 6250, # CPU blocks: 8738
INFO 03-12 03:08:49 [executor_base.py:116] Maximum concurrency for 16384 tokens per request: 6.10x
INFO 03-12 03:08:49 [llm_engine.py:441] init engine (profile, create kv cache, warmup model) took 1.24 seconds
Processed prompts: 100%|█████████████████████████████████████████████████████████████████████████| 5/5 [00:36<00:00,  7.35s/it, est. speed input: 1154.46 toks/s, output: 139.29 toks/s]
WARNING 03-12 03:09:27 [parallel_state.py:1087] torch._C._host_emptyCache() only available in Pytorch >=2.5
[(38, (36, 36)), (87, (49, 49)), (62, (83, 83)), (69, (67, 67)), (69, (75, 75))]
Num OK: 5/5 1.0
Getting token ids from block manager v2
INFO 03-12 03:09:28 [config.py:2563] Downcasting torch.float32 to torch.float16.
INFO 03-12 03:09:28 [config.py:576] This model supports multiple tasks: {'generate', 'classify', 'embed', 'score', 'reward'}. Defaulting to 'generate'.
INFO 03-12 03:09:28 [llm_engine.py:235] Initializing a V0 LLM engine (v0.7.4.dev360+gc91b64f7) with config: model='bigcode/starcoder2-3b', speculative_config=None, tokenizer='bigcode/starcoder2-3b', skip_tokenizer_init=False, tokenizer_mode=auto, revision=None, override_neuron_config=None, tokenizer_revision=None, trust_remote_code=False, dtype=torch.float16, max_seq_len=16384, download_dir=None, load_format=auto, tensor_parallel_size=1, pipeline_parallel_size=1, disable_custom_all_reduce=False, quantization=None, enforce_eager=True, kv_cache_dtype=auto,  device_config=npu, decoding_config=DecodingConfig(guided_decoding_backend='xgrammar', reasoning_backend=None), observability_config=ObservabilityConfig(show_hidden_metrics=False, otlp_traces_endpoint=None, collect_model_forward_time=False, collect_model_execute_time=False), seed=None, served_model_name=bigcode/starcoder2-3b, num_scheduler_steps=1, multi_step_stream_outputs=True, enable_prefix_caching=False, chunked_prefill_enabled=False, use_async_output_proc=True, disable_mm_preprocessor_cache=False, mm_processor_kwargs=None, pooler_config=None, compilation_config={"splitting_ops":[],"compile_sizes":[],"cudagraph_capture_sizes":[],"max_capture_size":0}, use_cached_outputs=False, 
WARNING 03-12 03:09:29 [utils.py:2304] Methods add_lora,add_prompt_adapter,cache_config,compilation_config,current_platform,list_loras,list_prompt_adapters,load_config,pin_lora,pin_prompt_adapter,remove_lora,remove_prompt_adapter not implemented in <vllm_ascend.worker.worker.NPUWorker object at 0xfffd3c0ee740>
INFO 03-12 03:09:30 [parallel_state.py:948] rank 0 in world size 1 is assigned as DP rank 0, PP rank 0, TP rank 0
INFO 03-12 03:09:30 [weight_utils.py:257] Using model weights format ['*.safetensors']
INFO 03-12 03:09:33 [weight_utils.py:273] Time spent downloading weights for bigcode/starcoder2-3b: 3.516371 seconds
INFO 03-12 03:09:33 [weight_utils.py:307] No model.safetensors.index.json found in remote.
Loading safetensors checkpoint shards:   0% Completed | 0/1 [00:00<?, ?it/s]
Loading safetensors checkpoint shards: 100% Completed | 1/1 [00:01<00:00,  1.32s/it]
Loading safetensors checkpoint shards: 100% Completed | 1/1 [00:01<00:00,  1.32s/it]

INFO 03-12 03:09:36 [loader.py:429] Loading weights took 2.28 seconds
INFO 03-12 03:09:37 [llm_engine.py:430] Overriding num_gpu_blocks=104530 with num_gpu_blocks_override=6250
INFO 03-12 03:09:37 [executor_base.py:111] # npu blocks: 6250, # CPU blocks: 8738
INFO 03-12 03:09:37 [executor_base.py:116] Maximum concurrency for 16384 tokens per request: 6.10x
INFO 03-12 03:09:37 [llm_engine.py:441] init engine (profile, create kv cache, warmup model) took 1.25 seconds
Processed prompts: 100%|█████████████████████████████████████████████████████████████████████████| 5/5 [00:36<00:00,  7.30s/it, est. speed input: 1162.15 toks/s, output: 140.22 toks/s]
WARNING 03-12 03:10:15 [parallel_state.py:1087] torch._C._host_emptyCache() only available in Pytorch >=2.5
[(38, (36, 36)), (87, (49, 49)), (62, (83, 83)), (69, (67, 67)), (69, (75, 75))]
Num OK: 5/5 1.0
[True, True, True, True, True]
PASSED
test_correctness_sliding_window.py::test_sliding_window_chunked_prefill[FLASH_ATTN-1-5-test_llm_kwargs0-per_test_common_llm_kwargs0-common_llm_kwargs0] INFO 03-12 03:10:16 [config.py:2563] Downcasting torch.float32 to torch.float16.
INFO 03-12 03:10:16 [config.py:576] This model supports multiple tasks: {'generate', 'classify', 'embed', 'score', 'reward'}. Defaulting to 'generate'.
INFO 03-12 03:10:16 [config.py:1666] Chunked prefill is enabled with max_num_batched_tokens=2048.
INFO 03-12 03:10:16 [llm_engine.py:235] Initializing a V0 LLM engine (v0.7.4.dev360+gc91b64f7) with config: model='bigcode/starcoder2-3b', speculative_config=None, tokenizer='bigcode/starcoder2-3b', skip_tokenizer_init=False, tokenizer_mode=auto, revision=None, override_neuron_config=None, tokenizer_revision=None, trust_remote_code=False, dtype=torch.float16, max_seq_len=16384, download_dir=None, load_format=auto, tensor_parallel_size=1, pipeline_parallel_size=1, disable_custom_all_reduce=False, quantization=None, enforce_eager=True, kv_cache_dtype=auto,  device_config=npu, decoding_config=DecodingConfig(guided_decoding_backend='xgrammar', reasoning_backend=None), observability_config=ObservabilityConfig(show_hidden_metrics=False, otlp_traces_endpoint=None, collect_model_forward_time=False, collect_model_execute_time=False), seed=None, served_model_name=bigcode/starcoder2-3b, num_scheduler_steps=1, multi_step_stream_outputs=True, enable_prefix_caching=False, chunked_prefill_enabled=True, use_async_output_proc=True, disable_mm_preprocessor_cache=False, mm_processor_kwargs=None, pooler_config=None, compilation_config={"splitting_ops":[],"compile_sizes":[],"cudagraph_capture_sizes":[],"max_capture_size":0}, use_cached_outputs=False, 
WARNING 03-12 03:10:17 [utils.py:2304] Methods add_lora,add_prompt_adapter,cache_config,compilation_config,current_platform,list_loras,list_prompt_adapters,load_config,pin_lora,pin_prompt_adapter,remove_lora,remove_prompt_adapter not implemented in <vllm_ascend.worker.worker.NPUWorker object at 0xfffd3c09ce20>
INFO 03-12 03:10:18 [parallel_state.py:948] rank 0 in world size 1 is assigned as DP rank 0, PP rank 0, TP rank 0
INFO 03-12 03:10:18 [weight_utils.py:257] Using model weights format ['*.safetensors']
INFO 03-12 03:10:18 [weight_utils.py:307] No model.safetensors.index.json found in remote.
Loading safetensors checkpoint shards:   0% Completed | 0/1 [00:00<?, ?it/s]
Loading safetensors checkpoint shards: 100% Completed | 1/1 [00:01<00:00,  1.27s/it]
Loading safetensors checkpoint shards: 100% Completed | 1/1 [00:01<00:00,  1.28s/it]

INFO 03-12 03:10:20 [loader.py:429] Loading weights took 2.16 seconds
INFO 03-12 03:10:21 [llm_engine.py:430] Overriding num_gpu_blocks=106373 with num_gpu_blocks_override=6250
INFO 03-12 03:10:21 [executor_base.py:111] # npu blocks: 6250, # CPU blocks: 8738
INFO 03-12 03:10:21 [executor_base.py:116] Maximum concurrency for 16384 tokens per request: 6.10x
INFO 03-12 03:10:22 [llm_engine.py:441] init engine (profile, create kv cache, warmup model) took 0.79 seconds
Processed prompts:   0%|                                                                                      | 0/5 [00:00<?, ?it/s, est. speed input: 0.00 toks/s, output: 0.00 toks/s][rank0]:[W312 03:10:23.570175597 compiler_depend.ts:133] Warning: Warning: Device do not support double dtype now, dtype cast repalce with float. (function operator())
FAILED
test_correctness_sliding_window.py::test_sliding_window_chunked_prefill[FLASHINFER-1-5-test_llm_kwargs0-per_test_common_llm_kwargs0-common_llm_kwargs0] FAILED
test_correctness_sliding_window.py::test_sliding_window_chunked_prefill[XFORMERS-1-5-test_llm_kwargs0-per_test_common_llm_kwargs0-common_llm_kwargs0] INFO 03-12 03:10:24 [config.py:2563] Downcasting torch.float32 to torch.float16.
INFO 03-12 03:10:24 [config.py:576] This model supports multiple tasks: {'generate', 'classify', 'embed', 'score', 'reward'}. Defaulting to 'generate'.
INFO 03-12 03:10:24 [config.py:1666] Chunked prefill is enabled with max_num_batched_tokens=2048.
INFO 03-12 03:10:24 [llm_engine.py:235] Initializing a V0 LLM engine (v0.7.4.dev360+gc91b64f7) with config: model='bigcode/starcoder2-3b', speculative_config=None, tokenizer='bigcode/starcoder2-3b', skip_tokenizer_init=False, tokenizer_mode=auto, revision=None, override_neuron_config=None, tokenizer_revision=None, trust_remote_code=False, dtype=torch.float16, max_seq_len=16384, download_dir=None, load_format=auto, tensor_parallel_size=1, pipeline_parallel_size=1, disable_custom_all_reduce=False, quantization=None, enforce_eager=True, kv_cache_dtype=auto,  device_config=npu, decoding_config=DecodingConfig(guided_decoding_backend='xgrammar', reasoning_backend=None), observability_config=ObservabilityConfig(show_hidden_metrics=False, otlp_traces_endpoint=None, collect_model_forward_time=False, collect_model_execute_time=False), seed=None, served_model_name=bigcode/starcoder2-3b, num_scheduler_steps=1, multi_step_stream_outputs=True, enable_prefix_caching=False, chunked_prefill_enabled=True, use_async_output_proc=True, disable_mm_preprocessor_cache=False, mm_processor_kwargs=None, pooler_config=None, compilation_config={"splitting_ops":[],"compile_sizes":[],"cudagraph_capture_sizes":[],"max_capture_size":0}, use_cached_outputs=False, 
WARNING 03-12 03:10:26 [utils.py:2304] Methods add_lora,add_prompt_adapter,cache_config,compilation_config,current_platform,list_loras,list_prompt_adapters,load_config,pin_lora,pin_prompt_adapter,remove_lora,remove_prompt_adapter not implemented in <vllm_ascend.worker.worker.NPUWorker object at 0xfff6f5a9b880>
INFO 03-12 03:10:26 [weight_utils.py:257] Using model weights format ['*.safetensors']
INFO 03-12 03:10:26 [weight_utils.py:307] No model.safetensors.index.json found in remote.

Loading safetensors checkpoint shards:   0% Completed | 0/1 [00:00<?, ?it/s]
Loading safetensors checkpoint shards: 100% Completed | 1/1 [00:01<00:00,  1.26s/it]

INFO 03-12 03:10:28 [loader.py:429] Loading weights took 2.14 seconds
Processed prompts:   0%|                                                                                      | 0/5 [00:06<?, ?it/s, est. speed input: 0.00 toks/s, output: 0.00 toks/s]
FAILED

======================================================================================= FAILURES =======================================================================================
__________________________ test_sliding_window_retrival[FLASH_ATTN-1-5-test_llm_kwargs0-baseline_llm_kwargs0-per_test_common_llm_kwargs0-common_llm_kwargs0] ___________________________

baseline_llm_generator = <generator object create_llm_generator at 0xfffdc130d690>, test_llm_generator = <generator object create_llm_generator at 0xfffdc130d700>, batch_size = 5
seed = 1, backend = 'FLASH_ATTN', monkeypatch = <_pytest.monkeypatch.MonkeyPatch object at 0xfffdc1309e10>

    @pytest.mark.parametrize(
        "common_llm_kwargs",
        [{
            "model": MODEL,
    
            # skip cuda graph creation for fast test.
            "enforce_eager": True,
            "block_size": BLOCK_SIZE,
            # needed due to https://github.com/vllm-project/vllm/issues/1908#issuecomment-2101122008
            "num_gpu_blocks_override": 100000 // BLOCK_SIZE,
        }])
    @pytest.mark.parametrize("per_test_common_llm_kwargs", [{}])
    @pytest.mark.parametrize("baseline_llm_kwargs", [{}])
    @pytest.mark.parametrize("test_llm_kwargs", [{}])
    @pytest.mark.parametrize("batch_size", [5])
    @pytest.mark.parametrize("seed", [1])
    @pytest.mark.parametrize("backend", ["FLASH_ATTN", "FLASHINFER", "XFORMERS"])
    def test_sliding_window_retrival(baseline_llm_generator, test_llm_generator,
                                     batch_size, seed, backend, monkeypatch):
        """
        The test does a bunch of assignments "x1 = 10\nx2 = 33\n..." and then
        asks for value of one of them (which is outside the sliding window).
        If we tell it upfront which we are going to be looking for, then
        it answers correctly (mostly).
    
        Additionally, we compare the results of the v1 and v2 managers.
        """
        if backend == "FLASHINFER" and current_platform.is_rocm():
            pytest.skip("Flashinfer does not support ROCm/HIP.")
        if backend == "XFORMERS" and current_platform.is_rocm():
            pytest.skip("Xformers does not support ROCm/HIP.")
    
        override_backend_env_variable(monkeypatch, backend)
    
        sampling_params = SamplingParams(
            max_tokens=1024,
            ignore_eos=True,
            temperature=0.0,
        )
    
        prompts, answer, indices = prep_prompts(batch_size)
    
        baseline_texts = get_text_from_llm_generator(baseline_llm_generator,
                                                     prompts,
                                                     sampling_params,
                                                     llm_cb=check_window(prompts))
    
>       check_answers(indices, answer, baseline_texts)

test_correctness_sliding_window.py:65: 
_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 
test_correctness_sliding_window.py:161: in check_answers
    answer2 = [int(text[0:2].strip()) for text in outputs]
_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 

.0 = <list_iterator object at 0xfffcdb281480>

>   answer2 = [int(text[0:2].strip()) for text in outputs]
E   ValueError: invalid literal for int() with base 10: ''

test_correctness_sliding_window.py:161: ValueError
__________________________ test_sliding_window_retrival[FLASHINFER-1-5-test_llm_kwargs0-baseline_llm_kwargs0-per_test_common_llm_kwargs0-common_llm_kwargs0] ___________________________

baseline_llm_generator = <generator object create_llm_generator at 0xfffd1aedd8c0>, test_llm_generator = <generator object create_llm_generator at 0xfffd1aedd3f0>, batch_size = 5
seed = 1, backend = 'FLASHINFER', monkeypatch = <_pytest.monkeypatch.MonkeyPatch object at 0xfffcb00e9a50>

    @pytest.mark.parametrize(
        "common_llm_kwargs",
        [{
            "model": MODEL,
    
            # skip cuda graph creation for fast test.
            "enforce_eager": True,
            "block_size": BLOCK_SIZE,
            # needed due to https://github.com/vllm-project/vllm/issues/1908#issuecomment-2101122008
            "num_gpu_blocks_override": 100000 // BLOCK_SIZE,
        }])
    @pytest.mark.parametrize("per_test_common_llm_kwargs", [{}])
    @pytest.mark.parametrize("baseline_llm_kwargs", [{}])
    @pytest.mark.parametrize("test_llm_kwargs", [{}])
    @pytest.mark.parametrize("batch_size", [5])
    @pytest.mark.parametrize("seed", [1])
    @pytest.mark.parametrize("backend", ["FLASH_ATTN", "FLASHINFER", "XFORMERS"])
    def test_sliding_window_retrival(baseline_llm_generator, test_llm_generator,
                                     batch_size, seed, backend, monkeypatch):
        """
        The test does a bunch of assignments "x1 = 10\nx2 = 33\n..." and then
        asks for value of one of them (which is outside the sliding window).
        If we tell it upfront which we are going to be looking for, then
        it answers correctly (mostly).
    
        Additionally, we compare the results of the v1 and v2 managers.
        """
        if backend == "FLASHINFER" and current_platform.is_rocm():
            pytest.skip("Flashinfer does not support ROCm/HIP.")
        if backend == "XFORMERS" and current_platform.is_rocm():
            pytest.skip("Xformers does not support ROCm/HIP.")
    
        override_backend_env_variable(monkeypatch, backend)
    
        sampling_params = SamplingParams(
            max_tokens=1024,
            ignore_eos=True,
            temperature=0.0,
        )
    
        prompts, answer, indices = prep_prompts(batch_size)
    
>       baseline_texts = get_text_from_llm_generator(baseline_llm_generator,
                                                     prompts,
                                                     sampling_params,
                                                     llm_cb=check_window(prompts))

test_correctness_sliding_window.py:60: 
_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 
conftest.py:54: in get_text_from_llm_generator
    for llm in llm_generator:
conftest.py:44: in create_llm_generator
    for llm in generator_inner():
conftest.py:36: in generator_inner
    llm = LLM(**kwargs)
../../../../vllm/utils.py:1053: in inner
    return fn(*args, **kwargs)
../../../../vllm/entrypoints/llm.py:244: in __init__
    self.llm_engine = self.engine_class.from_engine_args(
../../../../vllm/engine/llm_engine.py:491: in from_engine_args
    engine_config = engine_args.create_engine_config(usage_context)
../../../../vllm/engine/arg_utils.py:1204: in create_engine_config
    model_config = self.create_model_config()
../../../../vllm/engine/arg_utils.py:1130: in create_model_config
    return ModelConfig(
_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 

self = <vllm.config.ModelConfig object at 0xfffcb00e9090>, model = 'bigcode/starcoder2-3b', task = 'auto', tokenizer = 'bigcode/starcoder2-3b', tokenizer_mode = 'auto'
trust_remote_code = False, dtype = 'auto', seed = None, hf_config_path = None, allowed_local_media_path = '', revision = None, code_revision = None, rope_scaling = None
rope_theta = None, tokenizer_revision = None, max_model_len = None, spec_target_max_model_len = None, quantization = None, enforce_eager = True, max_seq_len_to_capture = 8192
max_logprobs = 20, disable_sliding_window = False, skip_tokenizer_init = False, served_model_name = None, limit_mm_per_prompt = None, use_async_output_proc = True
config_format = <ConfigFormat.AUTO: 'auto'>, hf_overrides = {}, mm_processor_kwargs = None, disable_mm_preprocessor_cache = False, override_neuron_config = None
override_pooler_config = None, logits_processor_pattern = None, generation_config = 'auto', enable_sleep_mode = False, override_generation_config = None, model_impl = 'auto'

    def __init__(
        self,
        model: str,
        task: Union[TaskOption, Literal["draft"]],
        tokenizer: str,
        tokenizer_mode: str,
        trust_remote_code: bool,
        dtype: Union[str, torch.dtype],
        seed: int,
        hf_config_path: Optional[str] = None,
        allowed_local_media_path: str = "",
        revision: Optional[str] = None,
        code_revision: Optional[str] = None,
        rope_scaling: Optional[dict[str, Any]] = None,
        rope_theta: Optional[float] = None,
        tokenizer_revision: Optional[str] = None,
        max_model_len: Optional[int] = None,
        spec_target_max_model_len: Optional[int] = None,
        quantization: Optional[str] = None,
        enforce_eager: Optional[bool] = None,
        max_seq_len_to_capture: Optional[int] = None,
        max_logprobs: int = 20,
        disable_sliding_window: bool = False,
        skip_tokenizer_init: bool = False,
        served_model_name: Optional[Union[str, list[str]]] = None,
        limit_mm_per_prompt: Optional[Mapping[str, int]] = None,
        use_async_output_proc: bool = True,
        config_format: ConfigFormat = ConfigFormat.AUTO,
        hf_overrides: Optional[HfOverrides] = None,
        mm_processor_kwargs: Optional[dict[str, Any]] = None,
        disable_mm_preprocessor_cache: bool = False,
        override_neuron_config: Optional[dict[str, Any]] = None,
        override_pooler_config: Optional["PoolerConfig"] = None,
        logits_processor_pattern: Optional[str] = None,
        generation_config: str = "auto",
        enable_sleep_mode: bool = False,
        override_generation_config: Optional[dict[str, Any]] = None,
        model_impl: Union[str, ModelImpl] = ModelImpl.AUTO,
    ) -> None:
        self.model = model
        self.hf_config_path = hf_config_path
        self.tokenizer = tokenizer
        self.tokenizer_mode = tokenizer_mode
        self.trust_remote_code = trust_remote_code
        self.allowed_local_media_path = allowed_local_media_path
        self.seed = seed
        self.revision = revision
        self.code_revision = code_revision
        self.rope_scaling = rope_scaling
        self.rope_theta = rope_theta
        self.model_impl = model_impl
    
        if hf_overrides is None:
            hf_overrides = {}
    
        if callable(hf_overrides):
            hf_overrides_kw = {}
            hf_overrides_fn = hf_overrides
        else:
            hf_overrides_kw = hf_overrides
            hf_overrides_fn = None
    
        if rope_scaling is not None:
            hf_override: dict[str, Any] = {"rope_scaling": rope_scaling}
            hf_overrides_kw.update(hf_override)
            msg = ("`--rope-scaling` will be removed in a future release. "
                   f"'Please instead use `--hf-overrides '{hf_override!r}'`")
            warnings.warn(DeprecationWarning(msg), stacklevel=2)
        if rope_theta is not None:
            hf_override = {"rope_theta": rope_theta}
            hf_overrides_kw.update(hf_override)
            msg = ("`--rope-theta` will be removed in a future release. "
                   f"'Please instead use `--hf-overrides '{hf_override!r}'`")
            warnings.warn(DeprecationWarning(msg), stacklevel=2)
    
        self.maybe_pull_model_tokenizer_for_s3(model, tokenizer)
    
        if (backend := envs.VLLM_ATTENTION_BACKEND
            ) and backend == "FLASHINFER" and find_spec("flashinfer") is None:
>           raise ValueError(
                "VLLM_ATTENTION_BACKEND is set to FLASHINFER, but flashinfer "
                "module was not found."
                "See https://github.com/vllm-project/vllm/blob/main/Dockerfile"
                "for instructions on how to install it.")
E           ValueError: VLLM_ATTENTION_BACKEND is set to FLASHINFER, but flashinfer module was not found.See https://github.com/vllm-project/vllm/blob/main/Dockerfilefor instructions on how to install it.

../../../../vllm/config.py:303: ValueError
_________________________________ test_sliding_window_chunked_prefill[FLASH_ATTN-1-5-test_llm_kwargs0-per_test_common_llm_kwargs0-common_llm_kwargs0] __________________________________

test_llm_generator = <generator object create_llm_generator at 0xfffd1a4d7d10>, batch_size = 5, seed = 1, backend = 'FLASH_ATTN'
monkeypatch = <_pytest.monkeypatch.MonkeyPatch object at 0xfffd3c09c7c0>

    @pytest.mark.parametrize(
        "common_llm_kwargs",
        [{
            "model": MODEL,
    
            # skip cuda graph creation for fast test.
            "enforce_eager": True,
            "block_size": BLOCK_SIZE,
            "num_gpu_blocks_override": 100000 // BLOCK_SIZE,
        }])
    @pytest.mark.parametrize("per_test_common_llm_kwargs", [{}])
    @pytest.mark.parametrize("test_llm_kwargs", [{"enable_chunked_prefill": True}])
    @pytest.mark.parametrize("batch_size", [5])
    @pytest.mark.parametrize("seed", [1])
    @pytest.mark.parametrize("backend", ["FLASH_ATTN", "FLASHINFER", "XFORMERS"])
    def test_sliding_window_chunked_prefill(test_llm_generator, batch_size, seed,
                                            backend, monkeypatch):
        """
        This is similar to test_sliding_window_retrival, however, it doesn't
        compare against the v1 block manager since v1 doesn't support
        chunked prefill with sliding window.
    
        The results with and without chunked prefill are not the same due to
        numerical instabilities.
        """
        if backend == "FLASHINFER" and current_platform.is_rocm():
            pytest.skip("Flashinfer does not support ROCm/HIP.")
        if backend == "XFORMERS" and current_platform.is_rocm():
            pytest.skip("Xformers does not support ROCm/HIP.")
        override_backend_env_variable(monkeypatch, backend)
    
        sampling_params = SamplingParams(
            max_tokens=10,
            ignore_eos=True,
            temperature=0.0,
        )
    
        prompts, answer, indices = prep_prompts(batch_size)
    
        # We don't compare with the baseline model here, since the results
        # slightly different due to different tailing in attention.
>       test_texts = get_text_from_llm_generator(test_llm_generator,
                                                 prompts,
                                                 sampling_params,
                                                 llm_cb=check_window(prompts))

test_correctness_sliding_window.py:125: 
_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 
conftest.py:57: in get_text_from_llm_generator
    outputs = llm.generate(prompts, sampling_params, use_tqdm=True)
../../../../vllm/utils.py:1088: in inner
    return fn(*args, **kwargs)
../../../../vllm/entrypoints/llm.py:473: in generate
    outputs = self._run_engine(use_tqdm=use_tqdm)
../../../../vllm/entrypoints/llm.py:1380: in _run_engine
    step_outputs = self.llm_engine.step()
../../../../vllm/engine/llm_engine.py:1407: in step
    outputs = self.model_executor.execute_model(
../../../../vllm/executor/executor_base.py:139: in execute_model
    output = self.collective_rpc("execute_model",
../../../../vllm/executor/uniproc_executor.py:56: in collective_rpc
    answer = run_method(self.driver_worker, method, args, kwargs)
../../../../vllm/utils.py:2238: in run_method
    return func(*args, **kwargs)
../../../../vllm/worker/worker_base.py:420: in execute_model
    output = self.model_runner.execute_model(
/home/sss/software/miniconda3/envs/vllm-v1/lib/python3.10/site-packages/torch/utils/_contextlib.py:116: in decorate_context
    return func(*args, **kwargs)
../../../../../vllm-ascend/vllm_ascend/worker/model_runner.py:1132: in execute_model
    hidden_or_intermediate_states = model_executable(
/home/sss/software/miniconda3/envs/vllm-v1/lib/python3.10/site-packages/torch/nn/modules/module.py:1736: in _wrapped_call_impl
    return self._call_impl(*args, **kwargs)
/home/sss/software/miniconda3/envs/vllm-v1/lib/python3.10/site-packages/torch/nn/modules/module.py:1747: in _call_impl
    return forward_call(*args, **kwargs)
../../../../vllm/model_executor/models/starcoder2.py:299: in forward
    hidden_states = self.model(input_ids, positions, intermediate_tensors,
../../../../vllm/compilation/decorators.py:172: in __call__
    return self.forward(*args, **kwargs)
../../../../vllm/model_executor/models/starcoder2.py:253: in forward
    hidden_states = layer(positions, hidden_states)
/home/sss/software/miniconda3/envs/vllm-v1/lib/python3.10/site-packages/torch/nn/modules/module.py:1736: in _wrapped_call_impl
    return self._call_impl(*args, **kwargs)
/home/sss/software/miniconda3/envs/vllm-v1/lib/python3.10/site-packages/torch/nn/modules/module.py:1747: in _call_impl
    return forward_call(*args, **kwargs)
../../../../vllm/model_executor/models/starcoder2.py:189: in forward
    hidden_states = self.self_attn(
/home/sss/software/miniconda3/envs/vllm-v1/lib/python3.10/site-packages/torch/nn/modules/module.py:1736: in _wrapped_call_impl
    return self._call_impl(*args, **kwargs)
/home/sss/software/miniconda3/envs/vllm-v1/lib/python3.10/site-packages/torch/nn/modules/module.py:1747: in _call_impl
    return forward_call(*args, **kwargs)
../../../../vllm/model_executor/models/starcoder2.py:125: in forward
    attn_output = self.attn(q, k, v)
/home/sss/software/miniconda3/envs/vllm-v1/lib/python3.10/site-packages/torch/nn/modules/module.py:1736: in _wrapped_call_impl
    return self._call_impl(*args, **kwargs)
/home/sss/software/miniconda3/envs/vllm-v1/lib/python3.10/site-packages/torch/nn/modules/module.py:1747: in _call_impl
    return forward_call(*args, **kwargs)
../../../../vllm/attention/layer.py:222: in forward
    return self.impl.forward(self, query, key, value,
_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 

self = <vllm_ascend.attention.AscendAttentionBackendImpl object at 0xfffd3c09ec50>
layer = Attention(head_size=128, num_heads=24, num_kv_heads=2, scale=0.08838834764831845, backend=AscendAttentionBackendImpl)
query = tensor([[[ 5.5615e-01,  2.5039e+00, -1.0459e+00,  ...,  3.1934e-01,
           8.2153e-02,  4.7241e-01],
         [-2....2.3125e+00,  2.0776e-01,  ...,  3.4473e-01,
          -2.0361e-01,  2.3804e-01]]], device='npu:0', dtype=torch.float16)
key = tensor([[[  2.7812,  -0.6069,  -3.7266,  ...,   6.7344, -23.1719,   1.3857],
         [ -5.5469,   2.1836,  -2.0430,  ...      [ -2.0117,  -1.1875,   2.6094,  ...,  -0.4209,   0.0775,   0.3936]]],
       device='npu:0', dtype=torch.float16)
value = tensor([[[ 0.2820,  0.0981, -0.0483,  ..., -0.3020, -0.1639, -0.0419],
         [ 0.3201,  0.2042,  0.2311,  ...,  0.0...],
         [-0.1501,  0.1112, -0.0812,  ...,  0.0191, -0.1202, -0.2313]]],
       device='npu:0', dtype=torch.float16)
kv_cache = tensor([[[[[ 2.7812e+00, -6.0693e-01, -3.7266e+00,  ...,  6.7344e+00,
            -2.3172e+01,  1.3857e+00],
         ...00e+00,  0.0000e+00,  ...,  0.0000e+00,
             0.0000e+00,  0.0000e+00]]]]], device='npu:0', dtype=torch.float16)
attn_metadata = AscendMetadata(num_prefills=1, num_prefill_tokens=2048, num_decode_tokens=0, slot_mapping=tensor([   0,    1,    2,  ....0., 0., 0.,  ..., 0., 0., 0.]], device='npu:0', dtype=torch.float16), cross_slot_mapping=None, cross_block_tables=None)
attn_type = 'decoder'
output = tensor([[[ 0.3601, -1.3809,  0.0557,  ...,  0.3242, -0.5610,  0.2012],
         [ 0.8677,  0.3618,  0.6240,  ..., -0.4...],
         [ 0.3176,  0.1187,  0.1307,  ...,  0.2705, -0.4136, -0.2759]]],
       device='npu:0', dtype=torch.float16)

    def forward(
        self,
        layer: AttentionLayer,
        query: torch.Tensor,
        key: torch.Tensor,
        value: torch.Tensor,
        kv_cache: torch.Tensor,
        attn_metadata: AscendMetadata,
        attn_type: str = AttentionType.DECODER,
        output: Optional[torch.Tensor] = None,
    ) -> torch.Tensor:
        """Forward pass with Ascend attention.
        Args:
            query: shape = [num_tokens, num_heads * head_size]
                   num_tokens = batch_size * seq_len
            key: shape = [num_tokens, num_kv_heads * head_size]
            value: shape = [num_tokens, num_kv_heads * head_size]
            kv_cache: shape = [2, num_blocks, block_size,
                               num_kv_heads * head_size]
                      key_cache = [num_blocks, block_size,
                                   num_kv_heads * head_size]
                      value_cache = [num_blocks, block_size,
                                     num_kv_heads * head_size]
            attn_metadata: Metadata for attention.
        Returns:
            shape = [batch_size, seq_len * num_heads * head_size]
        """
        assert layer._k_scale_float == 1.0 and layer._v_scale_float == 1.0
        # View q k v to BSH.
        num_tokens = query.shape[0]
        query = query.view(-1, self.num_heads, self.head_size)
        key = key.view(-1, self.num_kv_heads, self.head_size)
        value = value.view(-1, self.num_kv_heads, self.head_size)
        # TODO: Remove this contiguous in the future.
        value = value.contiguous()
    
        output = torch.empty(num_tokens,
                             self.num_heads,
                             self.head_size,
                             dtype=query.dtype,
                             device=query.device)
    
        if kv_cache.numel() > 0:
            if self.key_cache is None:
                self.key_cache, self.value_cache = kv_cache[0], kv_cache[1]
            slots = attn_metadata.slot_mapping
    
        if hasattr(layer, 'quant_method'):
            isPrefill = True if attn_metadata.num_prefills > 0 else False
            if isPrefill:
                assert attn_metadata.prefill_metadata is not None
                self.seq_lens_tensor_cpu = torch.from_numpy(
                    np.array(attn_metadata.prefill_metadata.seq_lens).astype(
                        np.int32))
            else:
                assert attn_metadata.decode_metadata is not None
                self.seq_lens_tensor_cpu = torch.from_numpy(
                    np.array(attn_metadata.decode_metadata.seq_lens).astype(
                        np.int32))
            block_tables = attn_metadata.decode_metadata.block_tables if attn_metadata.decode_metadata else None
            # Details of kv_cache arrangement in attention quantization
            # are implemented by quant_method.
            layer.quant_method.apply(layer, query, key, value, self.key_cache,
                                     self.value_cache, self.scale,
                                     self.seq_lens_tensor_cpu, block_tables,
                                     isPrefill, attn_metadata, output)
        else:
            if self.key_cache is not None:
                torch_npu._npu_reshape_and_cache(key=key,
                                                 value=value,
                                                 key_cache=self.key_cache,
                                                 value_cache=self.value_cache,
                                                 slot_indices=slots)
    
            if attn_metadata.num_prefills > 0:
    
                if (attn_metadata.block_tables is None
                        or attn_metadata.block_tables.numel() == 0):
                    assert attn_metadata.attn_mask is not None
                    mask = attn_metadata.attn_mask
                    assert attn_metadata.prefill_metadata is not None
                    self.seq_lens_tensor_cpu = torch.from_numpy(
                        np.array(
                            attn_metadata.prefill_metadata.seq_lens).astype(
                                np.int32))
                    torch_npu._npu_flash_attention(
                        query=query,
                        key=key,
                        value=value,
                        mask=mask,
                        seq_len=self.seq_lens_tensor_cpu,
                        scale_value=self.scale,
                        num_heads=self.num_heads,
                        num_kv_heads=self.num_kv_heads,
                        out=output)
                else:
                    # TODO: Will support prefix cache and chunked prefill soon.
>                   raise RuntimeError(
                        "Prefix cache and chunked prefill are currently not supported."
E                       RuntimeError: Prefix cache and chunked prefill are currently not supported.

../../../../../vllm-ascend/vllm_ascend/attention.py:780: RuntimeError
_________________________________ test_sliding_window_chunked_prefill[FLASHINFER-1-5-test_llm_kwargs0-per_test_common_llm_kwargs0-common_llm_kwargs0] __________________________________

test_llm_generator = <generator object create_llm_generator at 0xfff6f5a27bc0>, batch_size = 5, seed = 1, backend = 'FLASHINFER'
monkeypatch = <_pytest.monkeypatch.MonkeyPatch object at 0xfff6f5715120>

    @pytest.mark.parametrize(
        "common_llm_kwargs",
        [{
            "model": MODEL,
    
            # skip cuda graph creation for fast test.
            "enforce_eager": True,
            "block_size": BLOCK_SIZE,
            "num_gpu_blocks_override": 100000 // BLOCK_SIZE,
        }])
    @pytest.mark.parametrize("per_test_common_llm_kwargs", [{}])
    @pytest.mark.parametrize("test_llm_kwargs", [{"enable_chunked_prefill": True}])
    @pytest.mark.parametrize("batch_size", [5])
    @pytest.mark.parametrize("seed", [1])
    @pytest.mark.parametrize("backend", ["FLASH_ATTN", "FLASHINFER", "XFORMERS"])
    def test_sliding_window_chunked_prefill(test_llm_generator, batch_size, seed,
                                            backend, monkeypatch):
        """
        This is similar to test_sliding_window_retrival, however, it doesn't
        compare against the v1 block manager since v1 doesn't support
        chunked prefill with sliding window.
    
        The results with and without chunked prefill are not the same due to
        numerical instabilities.
        """
        if backend == "FLASHINFER" and current_platform.is_rocm():
            pytest.skip("Flashinfer does not support ROCm/HIP.")
        if backend == "XFORMERS" and current_platform.is_rocm():
            pytest.skip("Xformers does not support ROCm/HIP.")
        override_backend_env_variable(monkeypatch, backend)
    
        sampling_params = SamplingParams(
            max_tokens=10,
            ignore_eos=True,
            temperature=0.0,
        )
    
        prompts, answer, indices = prep_prompts(batch_size)
    
        # We don't compare with the baseline model here, since the results
        # slightly different due to different tailing in attention.
>       test_texts = get_text_from_llm_generator(test_llm_generator,
                                                 prompts,
                                                 sampling_params,
                                                 llm_cb=check_window(prompts))

test_correctness_sliding_window.py:125: 
_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 
conftest.py:54: in get_text_from_llm_generator
    for llm in llm_generator:
conftest.py:44: in create_llm_generator
    for llm in generator_inner():
conftest.py:36: in generator_inner
    llm = LLM(**kwargs)
../../../../vllm/utils.py:1053: in inner
    return fn(*args, **kwargs)
../../../../vllm/entrypoints/llm.py:244: in __init__
    self.llm_engine = self.engine_class.from_engine_args(
../../../../vllm/engine/llm_engine.py:491: in from_engine_args
    engine_config = engine_args.create_engine_config(usage_context)
../../../../vllm/engine/arg_utils.py:1204: in create_engine_config
    model_config = self.create_model_config()
../../../../vllm/engine/arg_utils.py:1130: in create_model_config
    return ModelConfig(
_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 

self = <vllm.config.ModelConfig object at 0xfff6f5714d30>, model = 'bigcode/starcoder2-3b', task = 'auto', tokenizer = 'bigcode/starcoder2-3b', tokenizer_mode = 'auto'
trust_remote_code = False, dtype = 'auto', seed = None, hf_config_path = None, allowed_local_media_path = '', revision = None, code_revision = None, rope_scaling = None
rope_theta = None, tokenizer_revision = None, max_model_len = None, spec_target_max_model_len = None, quantization = None, enforce_eager = True, max_seq_len_to_capture = 8192
max_logprobs = 20, disable_sliding_window = False, skip_tokenizer_init = False, served_model_name = None, limit_mm_per_prompt = None, use_async_output_proc = True
config_format = <ConfigFormat.AUTO: 'auto'>, hf_overrides = {}, mm_processor_kwargs = None, disable_mm_preprocessor_cache = False, override_neuron_config = None
override_pooler_config = None, logits_processor_pattern = None, generation_config = 'auto', enable_sleep_mode = False, override_generation_config = None, model_impl = 'auto'

    def __init__(
        self,
        model: str,
        task: Union[TaskOption, Literal["draft"]],
        tokenizer: str,
        tokenizer_mode: str,
        trust_remote_code: bool,
        dtype: Union[str, torch.dtype],
        seed: int,
        hf_config_path: Optional[str] = None,
        allowed_local_media_path: str = "",
        revision: Optional[str] = None,
        code_revision: Optional[str] = None,
        rope_scaling: Optional[dict[str, Any]] = None,
        rope_theta: Optional[float] = None,
        tokenizer_revision: Optional[str] = None,
        max_model_len: Optional[int] = None,
        spec_target_max_model_len: Optional[int] = None,
        quantization: Optional[str] = None,
        enforce_eager: Optional[bool] = None,
        max_seq_len_to_capture: Optional[int] = None,
        max_logprobs: int = 20,
        disable_sliding_window: bool = False,
        skip_tokenizer_init: bool = False,
        served_model_name: Optional[Union[str, list[str]]] = None,
        limit_mm_per_prompt: Optional[Mapping[str, int]] = None,
        use_async_output_proc: bool = True,
        config_format: ConfigFormat = ConfigFormat.AUTO,
        hf_overrides: Optional[HfOverrides] = None,
        mm_processor_kwargs: Optional[dict[str, Any]] = None,
        disable_mm_preprocessor_cache: bool = False,
        override_neuron_config: Optional[dict[str, Any]] = None,
        override_pooler_config: Optional["PoolerConfig"] = None,
        logits_processor_pattern: Optional[str] = None,
        generation_config: str = "auto",
        enable_sleep_mode: bool = False,
        override_generation_config: Optional[dict[str, Any]] = None,
        model_impl: Union[str, ModelImpl] = ModelImpl.AUTO,
    ) -> None:
        self.model = model
        self.hf_config_path = hf_config_path
        self.tokenizer = tokenizer
        self.tokenizer_mode = tokenizer_mode
        self.trust_remote_code = trust_remote_code
        self.allowed_local_media_path = allowed_local_media_path
        self.seed = seed
        self.revision = revision
        self.code_revision = code_revision
        self.rope_scaling = rope_scaling
        self.rope_theta = rope_theta
        self.model_impl = model_impl
    
        if hf_overrides is None:
            hf_overrides = {}
    
        if callable(hf_overrides):
            hf_overrides_kw = {}
            hf_overrides_fn = hf_overrides
        else:
            hf_overrides_kw = hf_overrides
            hf_overrides_fn = None
    
        if rope_scaling is not None:
            hf_override: dict[str, Any] = {"rope_scaling": rope_scaling}
            hf_overrides_kw.update(hf_override)
            msg = ("`--rope-scaling` will be removed in a future release. "
                   f"'Please instead use `--hf-overrides '{hf_override!r}'`")
            warnings.warn(DeprecationWarning(msg), stacklevel=2)
        if rope_theta is not None:
            hf_override = {"rope_theta": rope_theta}
            hf_overrides_kw.update(hf_override)
            msg = ("`--rope-theta` will be removed in a future release. "
                   f"'Please instead use `--hf-overrides '{hf_override!r}'`")
            warnings.warn(DeprecationWarning(msg), stacklevel=2)
    
        self.maybe_pull_model_tokenizer_for_s3(model, tokenizer)
    
        if (backend := envs.VLLM_ATTENTION_BACKEND
            ) and backend == "FLASHINFER" and find_spec("flashinfer") is None:
>           raise ValueError(
                "VLLM_ATTENTION_BACKEND is set to FLASHINFER, but flashinfer "
                "module was not found."
                "See https://github.com/vllm-project/vllm/blob/main/Dockerfile"
                "for instructions on how to install it.")
E           ValueError: VLLM_ATTENTION_BACKEND is set to FLASHINFER, but flashinfer module was not found.See https://github.com/vllm-project/vllm/blob/main/Dockerfilefor instructions on how to install it.

../../../../vllm/config.py:303: ValueError
__________________________________ test_sliding_window_chunked_prefill[XFORMERS-1-5-test_llm_kwargs0-per_test_common_llm_kwargs0-common_llm_kwargs0] ___________________________________

test_llm_generator = <generator object create_llm_generator at 0xfff6f5c635a0>, batch_size = 5, seed = 1, backend = 'XFORMERS'
monkeypatch = <_pytest.monkeypatch.MonkeyPatch object at 0xfff6f5a993c0>

    @pytest.mark.parametrize(
        "common_llm_kwargs",
        [{
            "model": MODEL,
    
            # skip cuda graph creation for fast test.
            "enforce_eager": True,
            "block_size": BLOCK_SIZE,
            "num_gpu_blocks_override": 100000 // BLOCK_SIZE,
        }])
    @pytest.mark.parametrize("per_test_common_llm_kwargs", [{}])
    @pytest.mark.parametrize("test_llm_kwargs", [{"enable_chunked_prefill": True}])
    @pytest.mark.parametrize("batch_size", [5])
    @pytest.mark.parametrize("seed", [1])
    @pytest.mark.parametrize("backend", ["FLASH_ATTN", "FLASHINFER", "XFORMERS"])
    def test_sliding_window_chunked_prefill(test_llm_generator, batch_size, seed,
                                            backend, monkeypatch):
        """
        This is similar to test_sliding_window_retrival, however, it doesn't
        compare against the v1 block manager since v1 doesn't support
        chunked prefill with sliding window.
    
        The results with and without chunked prefill are not the same due to
        numerical instabilities.
        """
        if backend == "FLASHINFER" and current_platform.is_rocm():
            pytest.skip("Flashinfer does not support ROCm/HIP.")
        if backend == "XFORMERS" and current_platform.is_rocm():
            pytest.skip("Xformers does not support ROCm/HIP.")
        override_backend_env_variable(monkeypatch, backend)
    
        sampling_params = SamplingParams(
            max_tokens=10,
            ignore_eos=True,
            temperature=0.0,
        )
    
        prompts, answer, indices = prep_prompts(batch_size)
    
        # We don't compare with the baseline model here, since the results
        # slightly different due to different tailing in attention.
>       test_texts = get_text_from_llm_generator(test_llm_generator,
                                                 prompts,
                                                 sampling_params,
                                                 llm_cb=check_window(prompts))

test_correctness_sliding_window.py:125: 
_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 
conftest.py:54: in get_text_from_llm_generator
    for llm in llm_generator:
conftest.py:44: in create_llm_generator
    for llm in generator_inner():
conftest.py:36: in generator_inner
    llm = LLM(**kwargs)
../../../../vllm/utils.py:1053: in inner
    return fn(*args, **kwargs)
../../../../vllm/entrypoints/llm.py:244: in __init__
    self.llm_engine = self.engine_class.from_engine_args(
../../../../vllm/engine/llm_engine.py:494: in from_engine_args
    engine = cls(
../../../../vllm/engine/llm_engine.py:277: in __init__
    self._initialize_kv_caches()
../../../../vllm/engine/llm_engine.py:426: in _initialize_kv_caches
    self.model_executor.determine_num_available_blocks())
../../../../vllm/executor/executor_base.py:102: in determine_num_available_blocks
    results = self.collective_rpc("determine_num_available_blocks")
../../../../vllm/executor/uniproc_executor.py:56: in collective_rpc
    answer = run_method(self.driver_worker, method, args, kwargs)
../../../../vllm/utils.py:2238: in run_method
    return func(*args, **kwargs)
/home/sss/software/miniconda3/envs/vllm-v1/lib/python3.10/site-packages/torch/utils/_contextlib.py:116: in decorate_context
    return func(*args, **kwargs)
_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ 

self = <vllm_ascend.worker.worker.NPUWorker object at 0xfff6f5a9b880>

    @NPUPlatform.inference_mode()
    def determine_num_available_blocks(self) -> Tuple[int, int]:
        """Profiles the peak memory usage of the model to determine how many
        KV blocks may be allocated without OOMs.
        The engine will first conduct a profiling of the existing memory usage.
        Then, it calculate the maximum possible number of NPU and CPU blocks
        that can be allocated with the remaining free memory.
        .. tip::
            You may limit the usage of NPU memory
            by adjusting the `gpu_memory_utilization` parameter.
        """
        # Profile the memory usage of the model and get the maximum number of
        # cache blocks that can be allocated with the remaining free memory.
        NPUPlatform.empty_cache()
    
        # Execute a forward pass with dummy inputs to profile the memory usage
        # of the model.
        self.model_runner.profile_run()
    
        # Calculate the number of blocks that can be allocated with the
        # profiled peak memory.
        free_npu_memory, total_npu_memory = NPUPlatform.mem_get_info()
        # NOTE(woosuk): Here we assume that the other processes using the same
        # GPU did not change their memory usage during the profiling.
        peak_memory = self.init_npu_memory - free_npu_memory
>       assert peak_memory > 0, (
            "Error in memory profiling. "
            f"Initial free memory {self.init_npu_memory}, current free memory"
            f" {free_npu_memory}. This happens when the NPU memory was "
            "not properly cleaned up before initializing the vLLM instance.")
E       AssertionError: Error in memory profiling. Initial free memory 55271010304, current free memory 57590951936. This happens when the NPU memory was not properly cleaned up before initializing the vLLM instance.

../../../../../vllm-ascend/vllm_ascend/worker/worker.py:227: AssertionError
=================================================================================== warnings summary ===================================================================================
tests/core/block/e2e/test_correctness_sliding_window.py::test_sliding_window_retrival[FLASH_ATTN-1-5-test_llm_kwargs0-baseline_llm_kwargs0-per_test_common_llm_kwargs0-common_llm_kwargs0]
  /home/sss/software/miniconda3/envs/vllm-v1/lib/python3.10/site-packages/torch_npu/dynamo/torchair/__init__.py:8: DeprecationWarning: pkg_resources is deprecated as an API. See https://setuptools.pypa.io/en/latest/pkg_resources.html
    import pkg_resources

-- Docs: https://docs.pytest.org/en/stable/how-to/capture-warnings.html
=============================================================================== short test summary info ================================================================================
FAILED test_correctness_sliding_window.py::test_sliding_window_retrival[FLASH_ATTN-1-5-test_llm_kwargs0-baseline_llm_kwargs0-per_test_common_llm_kwargs0-common_llm_kwargs0] - ValueError: invalid literal for int() with base 10: ''
FAILED test_correctness_sliding_window.py::test_sliding_window_retrival[FLASHINFER-1-5-test_llm_kwargs0-baseline_llm_kwargs0-per_test_common_llm_kwargs0-common_llm_kwargs0] - ValueError: VLLM_ATTENTION_BACKEND is set to FLASHINFER, but flashinfer module was not found.See https://github.com/vllm-project/vllm/blob/main/Dockerfilefor instructions on how t...
FAILED test_correctness_sliding_window.py::test_sliding_window_chunked_prefill[FLASH_ATTN-1-5-test_llm_kwargs0-per_test_common_llm_kwargs0-common_llm_kwargs0] - RuntimeError: Prefix cache and chunked prefill are currently not supported.
FAILED test_correctness_sliding_window.py::test_sliding_window_chunked_prefill[FLASHINFER-1-5-test_llm_kwargs0-per_test_common_llm_kwargs0-common_llm_kwargs0] - ValueError: VLLM_ATTENTION_BACKEND is set to FLASHINFER, but flashinfer module was not found.See https://github.com/vllm-project/vllm/blob/main/Dockerfilefor instructions on how t...
FAILED test_correctness_sliding_window.py::test_sliding_window_chunked_prefill[XFORMERS-1-5-test_llm_kwargs0-per_test_common_llm_kwargs0-common_llm_kwargs0] - AssertionError: Error in memory profiling. Initial free memory 55271010304, current free memory 57590951936. This happens when the NPU memory was not properly cleaned up before in...
================================================================= 5 failed, 1 passed, 1 warning in 2014.88s (0:33:34) ==================================================================
```

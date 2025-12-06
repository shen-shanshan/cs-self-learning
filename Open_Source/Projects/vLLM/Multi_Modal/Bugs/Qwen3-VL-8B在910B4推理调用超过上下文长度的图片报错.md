```bash
(APIServer pid=3858160) DEBUG 10-28 10:45:18 [v1/metrics/loggers.py:127] Engine 000: Avg prompt throughput: 0.0 tokens/s, Avg generation throughput: 0.0 tokens/s, Running: 0 reqs, Waiting: 0 reqs, GPU KV cache usage: 0.0%, Prefix cache hit rate: 0.0%
(APIServer pid=3858160) INFO 10-28 10:45:22 [entrypoints/chat_utils.py:560] Detected the chat template content format to be 'openai'. You can set `--chat-template-content-format` to override this.
(EngineCore_DP0 pid=3859350) DEBUG 10-28 10:45:25 [v1/engine/core.py:743] EngineCore loop active.
(APIServer pid=3858160) DEBUG 10-28 10:45:28 [v1/metrics/loggers.py:127] Engine 000: Avg prompt throughput: 0.0 tokens/s, Avg generation throughput: 0.0 tokens/s, Running: 0 reqs, Waiting: 0 reqs, GPU KV cache usage: 0.0%, Prefix cache hit rate: 0.0%
(APIServer pid=3858160) DEBUG 10-28 10:45:38 [v1/metrics/loggers.py:127] Engine 000: Avg prompt throughput: 0.0 tokens/s, Avg generation throughput: 0.0 tokens/s, Running: 0 reqs, Waiting: 0 reqs, GPU KV cache usage: 0.0%, Prefix cache hit rate: 0.0%
(EngineCore_DP0 pid=3859350) DEBUG 10-28 10:45:46 [model_runner_v1.py:1904] num_tokens: 2048, moe_comm_type: MoECommType.NAIVE_MULTICAST
(EngineCore_DP0 pid=3859350) DEBUG 10-28 10:45:47 [model_runner_v1.py:1904] num_tokens: 2048, moe_comm_type: MoECommType.NAIVE_MULTICAST
(APIServer pid=3858160) DEBUG 10-28 10:45:48 [v1/metrics/loggers.py:127] Engine 000: Avg prompt throughput: 0.0 tokens/s, Avg generation throughput: 0.0 tokens/s, Running: 1 reqs, Waiting: 0 reqs, GPU KV cache usage: 1.0%, Prefix cache hit rate: 0.0%
[rank0]:[E1028 10:45:49.568958992 compiler_depend.ts:429] operator():build/CMakeFiles/torch_npu.dir/compiler_depend.ts:34 NPU function error: call aclnnRmsNorm failed, error code is 507015
[ERROR] 2025-10-28-10:45:49 (PID:3859350, Device:0, RankID:-1) ERR00100 PTA call acl api failed
[Error]: The aicore execution is abnormal.
        Rectify the fault based on the error information in the ascend log.
EZ9903: [PID: 3859350] 2025-10-28-10:45:49.351.502 rtKernelLaunchWithHandleV2 failed: 507015
        Solution: In this scenario, collect the plog when the fault occurs and locate the fault based on the plog.
        TraceBack (most recent call last):
        The error from device(chipId:2, dieId:0), serial number is 8, there is an exception of fftsplus aivector error, core id is 20, error code = 0x800000, dump info: pc start: 0x124400dd7f74, current: 0x124400dd9b8c, vec error info: 0x4316519a81, mte error info: 0x6d0600005f, ifu error info: 0x577f659408080, ccu error info: 0xb83484153f07684a, cube error info: 0, biu error info: 0, aic error mask: 0x6500020bd00028c, para base: 0x12c11508c080.[FUNC:ProcessStarsCoreErrorInfo][FILE:device_error_core_proc.cc][LINE:303]
        The extend info: errcode:(0x800000, 0, 0) errorStr: The DDR address of the MTE instruction is out of range. fixp_error0 info: 0x600005f, fixp_error1 info: 0x6d, fsmId:0, tslot:0, thread:0, ctxid:0, blk:0, sublk:0, subErrType:4.[FUNC:ProcessStarsCoreErrorInfo][FILE:device_error_core_proc.cc][LINE:322]
        Kernel task happen error, retCode=0x26, [aicore exception].[FUNC:PreCheckTaskErr][FILE:davinci_kernel_task.cc][LINE:1539]
        AICORE Kernel task happen error, retCode=0x26.[FUNC:GetError][FILE:stream.cc][LINE:1183]
        [AIC_INFO] after execute:args print end[FUNC:GetError][FILE:stream.cc][LINE:1183]
        [AIC_INFO] after execute:mixCtx print end[FUNC:GetError][FILE:stream.cc][LINE:1183]
        Aicore kernel execute failed, device_id=2, stream_id=1110, report_stream_id=1110, task_id=5176, flip_num=0, fault kernel_name=unpad_flashattention_bf16_1_mix_aic, fault kernel info ext=none, program id=336, hash=133261665648915813.[FUNC:GetError][FILE:stream.cc][LINE:1183]
        Failed to submit kernel task, retCode=0x7150026.[FUNC:LaunchKernelSubmit][FILE:context.cc][LINE:1206]
        kernel launch submit failed.[FUNC:LaunchKernelWithHandle][FILE:context.cc][LINE:1460]
        rtKernelLaunchWithHandleV2 execute failed, reason=[aicore exception][FUNC:FuncErrorReason][FILE:error_message_manage.cc][LINE:53]
        rtKernelLaunchWithHandleV2 failed: 507015
        #### KernelLaunch failed: /usr/local/Ascend/ascend-toolkit/8.2.RC1/opp/built-in/op_impl/ai_core/tbe//kernel/ascend910b/slice/Slice_ee29dafe4fe21e12bb8d56ae91626cba_high_performance.o
        Kernel Run failed. opType: 19, Slice
        launch failed for Slice, errno:361001.
        Check aclnnContiguous(inWorkspace, inContWorkspaceSize, aclInExecutor, stream) failed

Exception raised from operator() at build/CMakeFiles/torch_npu.dir/compiler_depend.ts:34 (most recent call first):
frame #0: c10::Error::Error(c10::SourceLocation, std::__cxx11::basic_string<char, std::char_traits<char>, std::allocator<char> >) + 0xd4 (0xffffafd33ea4 in /usr/local/python3.11.13/lib/python3.11/site-packages/torch/lib/libc10.so)
frame #1: c10::detail::torchCheckFail(char const*, char const*, unsigned int, std::__cxx11::basic_string<char, std::char_traits<char>, std::allocator<char> > const&) + 0xe4 (0xffffafcd3e44 in /usr/local/python3.11.13/lib/python3.11/site-packages/torch/lib/libc10.so)
frame #2: <unknown function> + 0x1796888 (0xfffe05dd6888 in /usr/local/python3.11.13/lib/python3.11/site-packages/torch_npu/lib/libtorch_npu.so)
frame #3: <unknown function> + 0x22887d4 (0xfffe068c87d4 in /usr/local/python3.11.13/lib/python3.11/site-packages/torch_npu/lib/libtorch_npu.so)
frame #4: <unknown function> + 0x8fb170 (0xfffe04f3b170 in /usr/local/python3.11.13/lib/python3.11/site-packages/torch_npu/lib/libtorch_npu.so)
frame #5: <unknown function> + 0x8fd504 (0xfffe04f3d504 in /usr/local/python3.11.13/lib/python3.11/site-packages/torch_npu/lib/libtorch_npu.so)
frame #6: <unknown function> + 0x8f9e2c (0xfffe04f39e2c in /usr/local/python3.11.13/lib/python3.11/site-packages/torch_npu/lib/libtorch_npu.so)
frame #7: <unknown function> + 0xd31fc (0xffffafb431fc in /lib/aarch64-linux-gnu/libstdc++.so.6)
frame #8: <unknown function> + 0x7d5b8 (0xffffbc0bd5b8 in /lib/aarch64-linux-gnu/libc.so.6)
frame #9: <unknown function> + 0xe5edc (0xffffbc125edc in /lib/aarch64-linux-gnu/libc.so.6)

(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [logging_utils/dump_input.py:69] Dumping input data for V1 LLM engine (v0.11.0) with config: model='/data/model-cache/Qwen3-VL-8B-Instruct/', speculative_config=None, tokenizer='/data/model-cache/Qwen3-VL-8B-Instruct/', skip_tokenizer_init=False, tokenizer_mode=auto, revision=None, tokenizer_revision=None, trust_remote_code=False, dtype=torch.bfloat16, max_seq_len=128000, download_dir=None, load_format=auto, tensor_parallel_size=1, pipeline_parallel_size=1, data_parallel_size=1, disable_custom_all_reduce=True, quantization=None, enforce_eager=False, kv_cache_dtype=auto, device_config=npu, structured_outputs_config=StructuredOutputsConfig(backend='auto', disable_fallback=False, disable_any_whitespace=False, disable_additional_properties=False, reasoning_parser=''), observability_config=ObservabilityConfig(show_hidden_metrics_for_version=None, otlp_traces_endpoint=None, collect_detailed_traces=None), seed=0, served_model_name=Qwen3-vl-8b, enable_prefix_caching=True, chunked_prefill_enabled=True, pooler_config=None, compilation_config={"level":3,"debug_dump_path":"","cache_dir":"/root/.cache/vllm/torch_compile_cache/271f9a1543","backend":"","custom_ops":["all"],"splitting_ops":["vllm.unified_attention","vllm.unified_attention_with_output","vllm.mamba_mixer2","vllm.mamba_mixer","vllm.short_conv","vllm.linear_attention","vllm.plamo2_mamba_mixer","vllm.gdn_attention","vllm.sparse_attn_indexer","vllm.unified_ascend_attention_with_output","vllm.mla_forward","vllm.unified_ascend_attention_with_output","vllm.mla_forward"],"use_inductor":false,"compile_sizes":[],"inductor_compile_config":{"enable_auto_functionalized_v2":false},"inductor_passes":{},"cudagraph_mode":1,"use_cudagraph":true,"cudagraph_num_of_warmups":1,"cudagraph_capture_sizes":[512,504,488,480,464,456,448,432,424,408,400,392,376,368,352,344,336,320,312,296,288,280,264,256,240,232,216,208,200,184,176,160,152,144,128,120,104,96,88,72,64,48,40,32,16,8,2,1],"cudagraph_copy_inputs":false,"full_cuda_graph":false,"use_inductor_graph_partition":false,"pass_config":{},"max_capture_size":512,"local_cache_dir":"/root/.cache/vllm/torch_compile_cache/271f9a1543/rank_0_0/backbone"},
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [logging_utils/dump_input.py:76] Dumping scheduler output for model execution: SchedulerOutput(scheduled_new_reqs=[], scheduled_cached_reqs=CachedRequestData(req_ids=['chatcmpl-5e1446682c6543a5a8549e3c449ff6d6'], resumed_from_preemption=[false], new_token_ids=[], new_block_ids=[[[17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32]]], num_computed_tokens=[2048]), num_scheduled_tokens={chatcmpl-5e1446682c6543a5a8549e3c449ff6d6: 2048}, total_num_scheduled_tokens=2048, scheduled_spec_decode_tokens={}, scheduled_encoder_inputs={}, num_common_prefix_blocks=[32], finished_req_ids=[], free_encoder_mm_hashes=[], structured_output_request_ids={}, grammar_bitmask=null, kv_connector_metadata=null)
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [logging_utils/dump_input.py:79] Dumping scheduler stats: SchedulerStats(num_running_reqs=1, num_waiting_reqs=0, step_counter=0, current_wave=0, kv_cache_usage=0.020539152759948665, prefix_cache_stats=PrefixCacheStats(reset=False, requests=0, queries=0, hits=0), spec_decoding_stats=None, kv_connector_stats=None, num_corrupted_reqs=0)
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710] EngineCore encountered a fatal error.
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710] Traceback (most recent call last):
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]   File "/opt/vllm/vllm/v1/engine/core.py", line 701, in run_engine_core
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]     engine_core.run_busy_loop()
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]   File "/opt/vllm/vllm/v1/engine/core.py", line 728, in run_busy_loop
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]     self._process_engine_step()
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]   File "/opt/vllm/vllm/v1/engine/core.py", line 754, in _process_engine_step
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]     outputs, model_executed = self.step_fn()
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]                               ^^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]   File "/opt/vllm/vllm/v1/engine/core.py", line 284, in step
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]     model_output = self.execute_model_with_error_logging(
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]                    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]   File "/opt/vllm/vllm/v1/engine/core.py", line 270, in execute_model_with_error_logging
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]     raise err
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]   File "/opt/vllm/vllm/v1/engine/core.py", line 261, in execute_model_with_error_logging
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]     return model_fn(scheduler_output)
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]            ^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]   File "/opt/vllm/vllm/v1/executor/abstract.py", line 103, in execute_model
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]     output = self.collective_rpc("execute_model",
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]   File "/opt/vllm/vllm/executor/uniproc_executor.py", line 83, in collective_rpc
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]     return [run_method(self.driver_worker, method, args, kwargs)]
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]             ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]   File "/opt/vllm/vllm/utils/__init__.py", line 3122, in run_method
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]     return func(*args, **kwargs)
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]            ^^^^^^^^^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]   File "/usr/local/python3.11.13/lib/python3.11/site-packages/vllm_ascend/worker/worker_v1.py", line 254, in execute_model
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]     output = self.model_runner.execute_model(scheduler_output,
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]   File "/usr/local/python3.11.13/lib/python3.11/site-packages/torch/utils/_contextlib.py", line 116, in decorate_context
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]     return func(*args, **kwargs)
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]            ^^^^^^^^^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]   File "/usr/local/python3.11.13/lib/python3.11/site-packages/vllm_ascend/worker/model_runner_v1.py", line 2030, in execute_model
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]     sampler_output = self.sampler(
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]                      ^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]   File "/usr/local/python3.11.13/lib/python3.11/site-packages/torch/nn/modules/module.py", line 1751, in _wrapped_call_impl
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]     return self._call_impl(*args, **kwargs)
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]   File "/usr/local/python3.11.13/lib/python3.11/site-packages/torch/nn/modules/module.py", line 1762, in _call_impl
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]     return forward_call(*args, **kwargs)
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]   File "/opt/vllm/vllm/v1/sample/sampler.py", line 100, in forward
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]     sampled, processed_logprobs = self.sample(logits, sampling_metadata)
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]                                   ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]   File "/opt/vllm/vllm/v1/sample/sampler.py", line 180, in sample
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]     random_sampled, processed_logprobs = self.topk_topp_sampler(
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]                                          ^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]   File "/usr/local/python3.11.13/lib/python3.11/site-packages/torch/nn/modules/module.py", line 1751, in _wrapped_call_impl
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]     return self._call_impl(*args, **kwargs)
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]   File "/usr/local/python3.11.13/lib/python3.11/site-packages/torch/nn/modules/module.py", line 1762, in _call_impl
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]     return forward_call(*args, **kwargs)
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]   File "/usr/local/python3.11.13/lib/python3.11/site-packages/vllm_ascend/sample/sampler.py", line 70, in forward_native
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]     logits = self._apply_top_k_top_p(logits, k, p)
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]   File "/usr/local/python3.11.13/lib/python3.11/site-packages/vllm_ascend/sample/sampler.py", line 32, in _apply_top_k_top_p
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]     if not is_310p() and p is not None and k is not None and 1 <= int(
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]                                                                   ^^^^
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710] RuntimeError: The Inner error is reported as above. The process exits for this inner error, and the current working operator name is aclnnRmsNorm.
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710] Since the operator is called asynchronously, the stacktrace may be inaccurate. If you want to get the accurate stacktrace, please set the environment variable ASCEND_LAUNCH_BLOCKING=1.
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710] Note: ASCEND_LAUNCH_BLOCKING=1 will force ops to run in synchronous mode, resulting in performance degradation. Please unset ASCEND_LAUNCH_BLOCKING in time after debugging.
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710] [ERROR] 2025-10-28-10:45:49 (PID:3859350, Device:0, RankID:-1) ERR00100 PTA call acl api failed.
(EngineCore_DP0 pid=3859350) ERROR 10-28 10:45:49 [v1/engine/core.py:710]
(APIServer pid=3858160) ERROR 10-28 10:45:49 [v1/engine/async_llm.py:480] AsyncLLM output_handler failed.
(APIServer pid=3858160) ERROR 10-28 10:45:49 [v1/engine/async_llm.py:480] Traceback (most recent call last):
(APIServer pid=3858160) ERROR 10-28 10:45:49 [v1/engine/async_llm.py:480]   File "/opt/vllm/vllm/v1/engine/async_llm.py", line 439, in output_handler
(APIServer pid=3858160) ERROR 10-28 10:45:49 [v1/engine/async_llm.py:480]     outputs = await engine_core.get_output_async()
(APIServer pid=3858160) ERROR 10-28 10:45:49 [v1/engine/async_llm.py:480]               ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(APIServer pid=3858160) ERROR 10-28 10:45:49 [v1/engine/async_llm.py:480]   File "/opt/vllm/vllm/v1/engine/core_client.py", line 846, in get_output_async
(APIServer pid=3858160) ERROR 10-28 10:45:49 [v1/engine/async_llm.py:480]     raise self._format_exception(outputs) from None
(APIServer pid=3858160) ERROR 10-28 10:45:49 [v1/engine/async_llm.py:480] vllm.v1.engine.exceptions.EngineDeadError: EngineCore encountered an issue. See stack trace (above) for the root cause.
(APIServer pid=3858160) INFO:     10.129.118.3:46216 - "POST /v1/chat/completions HTTP/1.1" 500 Internal Server Error
(APIServer pid=3858160) INFO:     Shutting down
(EngineCore_DP0 pid=3859350) Process EngineCore_DP0:
(EngineCore_DP0 pid=3859350) Traceback (most recent call last):
(EngineCore_DP0 pid=3859350)   File "/usr/local/python3.11.13/lib/python3.11/multiprocessing/process.py", line 314, in _bootstrap
(EngineCore_DP0 pid=3859350)     self.run()
(EngineCore_DP0 pid=3859350)   File "/usr/local/python3.11.13/lib/python3.11/multiprocessing/process.py", line 108, in run
(EngineCore_DP0 pid=3859350)     self._target(*self._args, **self._kwargs)
(EngineCore_DP0 pid=3859350)   File "/opt/vllm/vllm/v1/engine/core.py", line 712, in run_engine_core
(EngineCore_DP0 pid=3859350)     raise e
(EngineCore_DP0 pid=3859350)   File "/opt/vllm/vllm/v1/engine/core.py", line 701, in run_engine_core
(EngineCore_DP0 pid=3859350)     engine_core.run_busy_loop()
(EngineCore_DP0 pid=3859350)   File "/opt/vllm/vllm/v1/engine/core.py", line 728, in run_busy_loop
(EngineCore_DP0 pid=3859350)     self._process_engine_step()
(EngineCore_DP0 pid=3859350)   File "/opt/vllm/vllm/v1/engine/core.py", line 754, in _process_engine_step
(EngineCore_DP0 pid=3859350)     outputs, model_executed = self.step_fn()
(EngineCore_DP0 pid=3859350)                               ^^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350)   File "/opt/vllm/vllm/v1/engine/core.py", line 284, in step
(EngineCore_DP0 pid=3859350)     model_output = self.execute_model_with_error_logging(
(EngineCore_DP0 pid=3859350)                    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350)   File "/opt/vllm/vllm/v1/engine/core.py", line 270, in execute_model_with_error_logging
(EngineCore_DP0 pid=3859350)     raise err
(EngineCore_DP0 pid=3859350)   File "/opt/vllm/vllm/v1/engine/core.py", line 261, in execute_model_with_error_logging
(EngineCore_DP0 pid=3859350)     return model_fn(scheduler_output)
(EngineCore_DP0 pid=3859350)            ^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350)   File "/opt/vllm/vllm/v1/executor/abstract.py", line 103, in execute_model
(EngineCore_DP0 pid=3859350)     output = self.collective_rpc("execute_model",
(EngineCore_DP0 pid=3859350)              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350)   File "/opt/vllm/vllm/executor/uniproc_executor.py", line 83, in collective_rpc
(EngineCore_DP0 pid=3859350)     return [run_method(self.driver_worker, method, args, kwargs)]
(EngineCore_DP0 pid=3859350)             ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350)   File "/opt/vllm/vllm/utils/__init__.py", line 3122, in run_method
(EngineCore_DP0 pid=3859350)     return func(*args, **kwargs)
(EngineCore_DP0 pid=3859350)            ^^^^^^^^^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350)   File "/usr/local/python3.11.13/lib/python3.11/site-packages/vllm_ascend/worker/worker_v1.py", line 254, in execute_model
(EngineCore_DP0 pid=3859350)     output = self.model_runner.execute_model(scheduler_output,
(EngineCore_DP0 pid=3859350)              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350)   File "/usr/local/python3.11.13/lib/python3.11/site-packages/torch/utils/_contextlib.py", line 116, in decorate_context
(EngineCore_DP0 pid=3859350)     return func(*args, **kwargs)
(EngineCore_DP0 pid=3859350)            ^^^^^^^^^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350)   File "/usr/local/python3.11.13/lib/python3.11/site-packages/vllm_ascend/worker/model_runner_v1.py", line 2030, in execute_model
(EngineCore_DP0 pid=3859350)     sampler_output = self.sampler(
(EngineCore_DP0 pid=3859350)                      ^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350)   File "/usr/local/python3.11.13/lib/python3.11/site-packages/torch/nn/modules/module.py", line 1751, in _wrapped_call_impl
(EngineCore_DP0 pid=3859350)     return self._call_impl(*args, **kwargs)
(EngineCore_DP0 pid=3859350)            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350)   File "/usr/local/python3.11.13/lib/python3.11/site-packages/torch/nn/modules/module.py", line 1762, in _call_impl
(EngineCore_DP0 pid=3859350)     return forward_call(*args, **kwargs)
(EngineCore_DP0 pid=3859350)            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350)   File "/opt/vllm/vllm/v1/sample/sampler.py", line 100, in forward
(EngineCore_DP0 pid=3859350)     sampled, processed_logprobs = self.sample(logits, sampling_metadata)
(EngineCore_DP0 pid=3859350)                                   ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350)   File "/opt/vllm/vllm/v1/sample/sampler.py", line 180, in sample
(EngineCore_DP0 pid=3859350)     random_sampled, processed_logprobs = self.topk_topp_sampler(
(EngineCore_DP0 pid=3859350)                                          ^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350)   File "/usr/local/python3.11.13/lib/python3.11/site-packages/torch/nn/modules/module.py", line 1751, in _wrapped_call_impl
(EngineCore_DP0 pid=3859350)     return self._call_impl(*args, **kwargs)
(EngineCore_DP0 pid=3859350)            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350)   File "/usr/local/python3.11.13/lib/python3.11/site-packages/torch/nn/modules/module.py", line 1762, in _call_impl
(EngineCore_DP0 pid=3859350)     return forward_call(*args, **kwargs)
(EngineCore_DP0 pid=3859350)            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350)   File "/usr/local/python3.11.13/lib/python3.11/site-packages/vllm_ascend/sample/sampler.py", line 70, in forward_native
(EngineCore_DP0 pid=3859350)     logits = self._apply_top_k_top_p(logits, k, p)
(EngineCore_DP0 pid=3859350)              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore_DP0 pid=3859350)   File "/usr/local/python3.11.13/lib/python3.11/site-packages/vllm_ascend/sample/sampler.py", line 32, in _apply_top_k_top_p
(EngineCore_DP0 pid=3859350)     if not is_310p() and p is not None and k is not None and 1 <= int(
(EngineCore_DP0 pid=3859350)                                                                   ^^^^
(EngineCore_DP0 pid=3859350) RuntimeError: The Inner error is reported as above. The process exits for this inner error, and the current working operator name is aclnnRmsNorm.
(EngineCore_DP0 pid=3859350) Since the operator is called asynchronously, the stacktrace may be inaccurate. If you want to get the accurate stacktrace, please set the environment variable ASCEND_LAUNCH_BLOCKING=1.
(EngineCore_DP0 pid=3859350) Note: ASCEND_LAUNCH_BLOCKING=1 will force ops to run in synchronous mode, resulting in performance degradation. Please unset ASCEND_LAUNCH_BLOCKING in time after debugging.
(EngineCore_DP0 pid=3859350) [ERROR] 2025-10-28-10:45:49 (PID:3859350, Device:0, RankID:-1) ERR00100 PTA call acl api failed.
(EngineCore_DP0 pid=3859350)
[rank0]:[W1028 10:45:50.110482296 compiler_depend.ts:528] Warning: NPU warning, error code is 507015[Error]:
[Error]: The aicore execution is abnormal.
        Rectify the fault based on the error information in the ascend log.
EH9999: Inner Error!
        rtDeviceSynchronizeWithTimeout execute failed, reason=[aicore exception][FUNC:FuncErrorReason][FILE:error_message_manage.cc][LINE:53]
EH9999: [PID: 3859350] 2025-10-28-10:45:50.895.865 wait for compute device to finish failed, runtime result = 507015.[FUNC:ReportCallError][FILE:log_inner.cpp][LINE:161]
        TraceBack (most recent call last):
 (function npuSynchronizeUsedDevices)
[W1028 10:45:50.117644468 compiler_depend.ts:510] Warning: NPU warning, error code is 507015[Error]:
[Error]: The aicore execution is abnormal.
        Rectify the fault based on the error information in the ascend log.
EH9999: Inner Error!
        rtDeviceSynchronizeWithTimeout execute failed, reason=[aicore exception][FUNC:FuncErrorReason][FILE:error_message_manage.cc][LINE:53]
EH9999: [PID: 3859350] 2025-10-28-10:45:50.903.639 wait for compute device to finish failed, runtime result = 507015.[FUNC:ReportCallError][FILE:log_inner.cpp][LINE:161]
        TraceBack (most recent call last):
 (function npuSynchronizeDevice)
[W1028 10:45:50.122667383 compiler_depend.ts:227] Warning: NPU warning, error code is 507015[Error]:
[Error]: The aicore execution is abnormal.
        Rectify the fault based on the error information in the ascend log.
EH9999: Inner Error!
        rtDeviceSynchronizeWithTimeout execute failed, reason=[aicore exception][FUNC:FuncErrorReason][FILE:error_message_manage.cc][LINE:53]
EH9999: [PID: 3859350] 2025-10-28-10:45:50.908.923 wait for compute device to finish failed, runtime result = 507015.[FUNC:ReportCallError][FILE:log_inner.cpp][LINE:161]
        TraceBack (most recent call last):
 (function empty_cache)
(APIServer pid=3858160) INFO:     Waiting for application shutdown.
(APIServer pid=3858160) INFO:     Application shutdown complete.
(APIServer pid=3858160) INFO:     Finished server process [3858160]
(EngineCore_DP0 pid=3859350) [rank0]: Traceback (most recent call last):
(EngineCore_DP0 pid=3859350) [rank0]:   File "/usr/local/Ascend/ascend-toolkit/latest/python/site-packages/tbe/common/repository_manager/utils/repository_manager_log.py", line 60, in get_py_exception_dict
(EngineCore_DP0 pid=3859350) [rank0]:     if isinstance(err_msg, tuple) and err_msg[0] and isinstance(err_msg[0], dict):
(EngineCore_DP0 pid=3859350) [rank0]:                                       ~~~~~~~^^^
(EngineCore_DP0 pid=3859350) [rank0]: IndexError: tuple index out of range
(EngineCore_DP0 pid=3859350) [ERROR] 2025-10-28-10:45:52 (PID:3859350, Device:0, RankID:-1) ERR99999 UNKNOWN applicaiton exception
```

```bash
base64 -w 0 ./1.png > image_base64.txt
```

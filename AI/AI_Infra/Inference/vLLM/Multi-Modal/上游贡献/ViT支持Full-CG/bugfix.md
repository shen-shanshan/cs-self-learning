```bash
(EngineCore pid=177586)   File "/home/sss/github/vllm/vllm/model_executor/models/qwen3_vl.py", line 173, in forward
(EngineCore pid=177586)     x = x.view(L, -1, self.temporal_patch_size, self.patch_size, self.patch_size)
(EngineCore pid=177586)         ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=177586) RuntimeError: cannot reshape tensor of 0 elements into shape [0, -1, 2, 16, 16] because the unspecified dimension size -1 can be any value and is ambiguous
```

Fix PR: https://github.com/vllm-project/vllm/pull/38040

---

```bash
When I run:
vllm bench mm-processor \
--model /home/sss/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct \
--dataset-name random-mm \
--random-mm-base-items-per-request 1 \
--random-mm-num-mm-items-range-ratio 0.0 \
--random-mm-bucket-config '{"(336,336,8)": 1.0}' \
--num-prompts 1000 \
--num-warmups 200 \
--max-model-len 8192 \
--seed 42 \
--mm-encoder-attn-backend FLASH_ATTN \
--compilation-config '{"cudagraph_mm_encoder": true, "encoder_cudagraph_token_budgets": [512, 1024, 1536, 2048, 2560, 3072, 3584, 4096, 4864], "encoder_cudagraph_max_images_per_batch": 8}'

I get en error as shown below, please fix it:
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110] EngineCore encountered a fatal error.
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110] Traceback (most recent call last):
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]   File "/home/sss/github/vllm/vllm/v1/engine/core.py", line 1101, in run_engine_core
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]     engine_core.run_busy_loop()
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]   File "/home/sss/github/vllm/vllm/v1/engine/core.py", line 1142, in run_busy_loop
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]     self._process_engine_step()
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]   File "/home/sss/github/vllm/vllm/v1/engine/core.py", line 1181, in _process_engine_step
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]     outputs, model_executed = self.step_fn()
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]                               ^^^^^^^^^^^^^^
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]   File "/home/sss/github/vllm/vllm/v1/engine/core.py", line 451, in step_with_batch_queue
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]     exec_future = self.model_executor.execute_model(
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]                   ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]   File "/home/sss/github/vllm/vllm/v1/executor/uniproc_executor.py", line 114, in execute_model
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]     output.result()
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]   File "/usr/lib/python3.12/concurrent/futures/_base.py", line 449, in result
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]     return self.__get_result()
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]            ^^^^^^^^^^^^^^^^^^^
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]   File "/usr/lib/python3.12/concurrent/futures/_base.py", line 401, in __get_result
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]     raise self._exception
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]   File "/home/sss/github/vllm/vllm/v1/executor/uniproc_executor.py", line 84, in collective_rpc
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]     result = run_method(self.driver_worker, method, args, kwargs)
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]   File "/home/sss/github/vllm/vllm/v1/serial_utils.py", line 510, in run_method
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]     return func(*args, **kwargs)
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]            ^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]   File "/home/sss/github/vllm/vllm/v1/worker/worker_base.py", line 332, in execute_model
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]     return self.worker.execute_model(scheduler_output)
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]   File "/home/sss/github/vllm/.venv/lib/python3.12/site-packages/torch/utils/_contextlib.py", line 124, in decorate_context
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]     return func(*args, **kwargs)
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]            ^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]   File "/home/sss/github/vllm/vllm/v1/worker/gpu_worker.py", line 803, in execute_model
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]     output = self.model_runner.execute_model(
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]   File "/home/sss/github/vllm/.venv/lib/python3.12/site-packages/torch/utils/_contextlib.py", line 124, in decorate_context
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]     return func(*args, **kwargs)
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]            ^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]   File "/home/sss/github/vllm/vllm/v1/worker/gpu_model_runner.py", line 3977, in execute_model
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]     ) = self._preprocess(
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]         ^^^^^^^^^^^^^^^^^
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]   File "/home/sss/github/vllm/vllm/v1/worker/gpu_model_runner.py", line 3213, in _preprocess
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]     self._execute_mm_encoder(scheduler_output)
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]   File "/home/sss/github/vllm/vllm/v1/worker/gpu_model_runner.py", line 2864, in _execute_mm_encoder
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]     cudagraph_output = self.encoder_cudagraph_manager.execute(
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]                        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]   File "/home/sss/github/vllm/vllm/v1/worker/gpu/mm/encoder_cudagraph.py", line 560, in execute
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]     result = self._execute_local(mm_kwargs)
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]   File "/home/sss/github/vllm/vllm/v1/worker/gpu/mm/encoder_cudagraph.py", line 380, in _execute_local
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]     output = self._run_budget_graph(
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]              ^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]   File "/home/sss/github/vllm/vllm/v1/worker/gpu/mm/encoder_cudagraph.py", line 261, in _run_budget_graph
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110]     buf[:n].copy_(src)
(EngineCore pid=60691) ERROR 03-25 07:47:44 [core.py:1110] RuntimeError: The size of tensor a (9) must match the size of tensor b (13) at non-singleton dimension 0
```

Summary:

Root cause: During CUDA graph capture, `cu_seqlens` was padded to `max_batch_size` + 1 = 9 entries (for `max_batch_size=8` images with `t=1` each). For video batches with `t > 1` temporal patches per item, `sum(t_i)` can exceed `max_batch_size`: 3 videos × t=4 frames each = 12 sequences → needs 13 entries → buffer overflow.

Fix: Use `token_budget` as the padding target instead of `max_batch_size`. This is always safe because `sum(t_i)` ≤ `sum(output_tokens_i)` ≤ `token_budget` (each frame produces ≥1 output token, and greedy packing limits total tokens to token_budget).

Changes:

1. `prepare_encoder_metadata` — new `max_num_sequences` parameter, overrides `max_batch_size` for padding.
2. `prepare_encoder_cudagraph_capture_inputs` — passes `max_num_sequences=token_budget`, so the captured buffer has `token_budget` + 1 entries.
3. `prepare_encoder_cudagraph_replay_buffers` — accepts `token_budget`, pads replay `cu_seqlens` to the same size.
4. `interfaces.py` — adds `token_budget: int | None = None` to the protocol.
5. `encoder_cudagraph.py` — passes `token_budget` at the call site.
6. Test mocks updated with the new optional parameter.

Performance:

The CUDA graph still captures with `max_batch_size` real sequences of `per_image_output` tokens each (same kernel computation). At replay for video, `token_budget` - `num_seqs` zero-length "phantom" sequences are padded into the buffer. Flash attention skips phantom sequences (0-length early-exit) with negligible overhead.

---
Prompt:

qwen3_vl.py:L1735-L1789, it only consider image inputs for vit cuda graph capture. But when inputs are videos, this will lead to wrong length of cu_seqlens. I will get an error like: "RuntimeError: The size of tensor a (18) must match the size of tensor b (34) at non-singleton dimension 0" at encoder_cudagraph.py:L261. Please fix this issue and give your reason and design.

Update:

prepare_encoder_cudagraph_capture_inputs always creates a capture grid with T=1 (image-style: [1, m, per_image_output*m]). This means the cu_seqlens buffer is allocated with only max_batch_size + 1 entries — one attention sequence per batch item.

For videos, each item contributes T frames = T separate attention sequences. A batch with total frames sum(T_i) needs a cu_seqlens of length sum(T_i) + 1. When that exceeds max_batch_size + 1, the copy buf[:n].copy_(src) at encoder_cudagraph.py:261 overflows the buffer.

---
Prompt:

参考 encoder_cudagraph_max_images_per_batch 的原理和实现，我添加了一个 encoder_cudagraph_max_frames_per_batch 编译参数，用于控制每个 batch 中的最大视频帧数（控制 cu_seqlens 的 pad 长度）。请基于该变量，继续完善 ViT Encoder cuda graph 对 Qwen3-VL 视频推理的支持，并解决问题："RuntimeError: The size of tensor a (18) must match the size of tensor b (34) at non-singleton dimension 0" at encoder_cudagraph.py:L261

---
Prompt:

please modify prepare_encoder_cudagraph_capture_inputs() method in qwen3_vl.py:L1747-L1808, to support video-format grid_config and solve the issue: "RuntimeError: The size of tensor a (18) must match the size of tensor b (34) at non-singleton dimension 0" at encoder_cudagraph.py:L261 (wrong dimension 0 of cu_seqlens).

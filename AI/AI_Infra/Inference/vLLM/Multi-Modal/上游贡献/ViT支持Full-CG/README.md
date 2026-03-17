# ViT 支持 Full CG

## Background

However, this [PR](https://github.com/vllm-project/vllm/pull/33232) specifically targets scenarios with fewer images or when images are distributed via ViT DP. In these cases, the computational load per rank is smaller, and the execution time is dominated by "bubbles" caused by kernel launch overhead rather than the operator execution itself. The performance gain from using CUDA Graphs to eliminate these bubbles outweighs the slight regression introduced by torch.compile.

## Support Image

```bash
# vllm bench mm-processor \
# --model Qwen/Qwen3-VL-30B-A3B-Instruct \
# --dataset-name hf --dataset-path lmarena-ai/VisionArena-Chat \
# --num-prompts 3000 --num-warmups 300 \
# --max-model-len 32768 --seed 42 \
# --mm-encoder-attn-backend FLASHINFER \
# --compilation-config '{"cudagraph_mm_encoder": true, "encoder_cudagraph_token_budgets": [512, 1024, 1536, 2048, 2560, 3072, 3584, 4096, 4864], "encoder_cudagraph_max_images_per_batch": 8}'

export VLLM_USE_MODELSCOPE=False
export HF_ENDPOINT="https://hf-mirror.com"

vllm bench mm-processor \
--model /home/sss/.cache/modelscope/hub/models/Qwen/Qwen3-VL-4B-Instruct \
--dataset-name hf \
--dataset-path lmarena-ai/vision-arena-bench-v0.1 \
--num-prompts 500 \
--num-warmups 50 \
--max-model-len 32768 \
--seed 42 \
--mm-encoder-attn-backend FLASHINFER \
--compilation-config '{"cudagraph_mm_encoder": true, "encoder_cudagraph_token_budgets": [512, 1024, 1536, 2048, 2560, 3072, 3584, 4096, 4864], "encoder_cudagraph_max_images_per_batch": 8}'
```

### Benchmark

**ViT Eager:**

```
================================================================================
Multimodal Processor Benchmark Results
================================================================================

MM Processor Metrics:
                     Stage  Mean Median   Std  P99.0
          get_mm_hashes_ms  0.09   0.09  0.03   0.16
get_cache_missing_items_ms  0.01   0.01  0.00   0.02
     apply_hf_processor_ms 22.32  19.14 43.43 140.98
        merge_mm_kwargs_ms  0.06   0.06  0.02   0.10
   apply_prompt_updates_ms  0.25   0.12  0.81   2.18
     preprocessor_total_ms 22.73  19.66 43.52 141.52
        encoder_forward_ms 30.93  14.79 95.46 178.31
         num_encoder_calls  1.00   1.00  0.00   1.00

Summary: 496 total encoder calls across 496 requests.

End-to-End Latency (ms):
Metric Value (ms)
  Mean   27205.79
Median   26971.56
   Std    3162.91
 P99.0   31483.88
```

**ViT Full Graph:**

```
================================================================================
Multimodal Processor Benchmark Results
================================================================================

MM Processor Metrics:
                     Stage  Mean Median   Std  P99.0
          get_mm_hashes_ms  0.10   0.10  0.03   0.18
get_cache_missing_items_ms  0.01   0.01  0.00   0.03
     apply_hf_processor_ms 24.20  20.38 48.52 180.23
        merge_mm_kwargs_ms  0.06   0.06  0.01   0.12
   apply_prompt_updates_ms  0.27   0.13  0.89   2.55
     preprocessor_total_ms 24.65  20.99 48.62 180.84
        encoder_forward_ms 35.25  29.60 32.28 270.71
         num_encoder_calls  1.00   1.00  0.00   1.00

Summary: 496 total encoder calls across 496 requests.

End-to-End Latency (ms):
Metric Value (ms)
  Mean   29294.39
Median   29254.73
   Std    3344.86
 P99.0   33109.04
```

## Support Video

### Functional test

```bash
export PYTORCH_ALLOC_CONF=expandable_segments:True

# def run_qwen3_vl
# /home/sss/.cache/modelscope/hub/models/Qwen/Qwen3-VL-4B-Instruct
python examples/offline_inference/vision_language.py -m qwen3_vl --modality "video" --time-generate

# @contextmanager
# def time_counter(enable: bool):
#     if enable:
#         import time

#         start_time = time.time()
#         yield
#         elapsed_time = time.time() - start_time
#         print("-" * 50)
#         print("-- generate time = {}".format(elapsed_time))
#         print("-" * 50)
#     else:
#         yield
```

Output (with CG):

```bash
--------------------------------------------------
-- generate time = 3.1913323402404785
--------------------------------------------------
--------------------------------------------------
The video is funny because it captures a baby wearing glasses and pretending to read a book, which is an adorable and endearing sight. The baby’s serious expression and focused demeanor while holding the book, combined with the fact that they are clearly not reading, adds to the humor. The baby’s movements and interactions with the
--------------------------------------------------
The video is funny because it captures a toddler wearing glasses and pretending to read a book, which is an adorable and endearing sight. The child's serious demeanor and focused expression while flipping through the pages add to the humor, as it's clear they are not actually reading but rather engaging in a playful imitation of an adult
--------------------------------------------------
The video is funny because it captures a toddler wearing glasses and pretending to read a book with such seriousness and focus. The child’s earnestness and the way they turn the pages like a grown-up reader is endearing and absurd, especially since toddlers don’t typically engage in such behavior. The glasses add to the comedic effect
--------------------------------------------------
The video is funny because it captures a baby wearing glasses and pretending to read a book, which is an adorable and endearing sight. The baby’s serious expression and focused demeanor while “reading” the book, combined with the fact that they are clearly not actually reading, creates a humorous contrast. The baby’s movements,
--------------------------------------------------
```

Output (without CG):

```bash
--------------------------------------------------
-- generate time = 3.187770128250122
--------------------------------------------------
--------------------------------------------------
The video is funny because it captures a baby wearing glasses and pretending to read a book, which is an adorable and endearing sight. The baby’s serious expression and focused demeanor while pretending to read, combined with the fact that they are so young and unable to actually read, creates a humorous contrast. The baby’s movements
--------------------------------------------------
The video is funny because it captures a toddler wearing glasses and pretending to read a book, which is an adorable and endearing sight. The child's serious demeanor and focused expression while flipping through the pages add to the humor, as it's clear they are taking the activity very seriously. The contrast between the child's innocent
--------------------------------------------------
The video is funny because it captures a toddler wearing glasses and pretending to read a book with such seriousness and focus. The child’s earnestness and the way they turn the pages, as if they’re a grown-up reader, is comically endearing. The contrast between the child’s tiny size and the adult-like behavior
--------------------------------------------------
The video is funny because it captures a baby wearing glasses and pretending to read a book with such seriousness and focus. The baby’s tiny hands flipping through the pages and the way they’re trying to “read” the book with such concentration is adorable and endearing. The baby’s glasses add to the humor, as they
--------------------------------------------------
```

### Benchmark-1

```bash
vllm bench mm-processor \
--model /home/sss/.cache/modelscope/hub/models/Qwen/Qwen3-VL-4B-Instruct \
--dataset-name random-mm \
--num-prompts 100 \
--random-input-len 300 \
--random-output-len 40 \
--random-mm-base-items-per-request 5 \
--random-mm-limit-mm-per-prompt '{"image": 0, "video": 5}' \
--random-mm-bucket-config '{(256, 256, 16): 1}' \
--mm-encoder-attn-backend FLASHINFER \
--compilation-config '{"cudagraph_mm_encoder": true, "encoder_cudagraph_token_budgets": [512, 1024, 1536, 2048, 2560, 3072, 3584, 4096, 4864], "encoder_cudagraph_max_images_per_batch": 8}'
```

Eager:

```
random-mm-base-items-per-request=1
random-mm-bucket-config: T=8
================================================================================
Multimodal Processor Benchmark Results
================================================================================

MM Processor Metrics:
                     Stage  Mean Median   Std  P99.0
          get_mm_hashes_ms  0.47   0.43  0.14   1.04
get_cache_missing_items_ms  0.02   0.02  0.01   0.05
     apply_hf_processor_ms  4.53   4.14  1.23   9.44
        merge_mm_kwargs_ms  0.07   0.06  0.02   0.15
   apply_prompt_updates_ms  0.66   0.62  0.16   1.22
     preprocessor_total_ms  5.74   5.27  1.54  11.91
        encoder_forward_ms 13.41   2.82 22.38 100.76
         num_encoder_calls  1.00   1.00  0.00   1.00

Summary: 100 total encoder calls across 100 requests.

End-to-End Latency (ms):
Metric Value (ms)
  Mean    2825.57
Median    2639.67
   Std     837.53
 P99.0    4956.62


random-mm-base-items-per-request=5
random-mm-bucket-config: T=16
================================================================================
Multimodal Processor Benchmark Results
================================================================================

MM Processor Metrics:
                     Stage  Mean Median  Std P99.0
          get_mm_hashes_ms  3.60   3.51 0.48  5.60
get_cache_missing_items_ms  0.04   0.04 0.00  0.05
     apply_hf_processor_ms 21.14  20.59 1.94 29.27
        merge_mm_kwargs_ms  0.17   0.16 0.04  0.36
   apply_prompt_updates_ms  1.08   1.06 0.10  1.37
     preprocessor_total_ms 26.04  25.36 2.36 35.68
        encoder_forward_ms 24.38  25.32 3.64 29.98
         num_encoder_calls  1.00   1.00 0.00  1.00

Summary: 100 total encoder calls across 100 requests.

End-to-End Latency (ms):
Metric Value (ms)
  Mean    5670.41
Median    5672.30
   Std    2589.77
 P99.0   10076.41
```

CG:

```
random-mm-base-items-per-request=1
random-mm-bucket-config: T=8
================================================================================
Multimodal Processor Benchmark Results
================================================================================

MM Processor Metrics:
                     Stage Mean Median  Std P99.0
          get_mm_hashes_ms 0.43   0.42 0.02  0.51
get_cache_missing_items_ms 0.02   0.02 0.00  0.04
     apply_hf_processor_ms 4.47   4.41 0.35  5.67
        merge_mm_kwargs_ms 0.06   0.06 0.01  0.08
   apply_prompt_updates_ms 0.60   0.59 0.06  0.79
     preprocessor_total_ms 5.59   5.50 0.41  7.08
        encoder_forward_ms 6.02   5.36 1.68 16.26
         num_encoder_calls 1.00   1.00 0.00  1.00

Summary: 100 total encoder calls across 100 requests.

End-to-End Latency (ms):
Metric Value (ms)
  Mean    2400.24
Median    2007.84
   Std     949.17
 P99.0    4233.38


random-mm-base-items-per-request=5
random-mm-bucket-config: T=16
================================================================================
Multimodal Processor Benchmark Results
================================================================================

MM Processor Metrics:
                     Stage  Mean Median  Std P99.0
          get_mm_hashes_ms  3.65   3.43 1.04  6.92
get_cache_missing_items_ms  0.04   0.03 0.01  0.09
     apply_hf_processor_ms 21.26  20.38 3.07 29.98
        merge_mm_kwargs_ms  0.17   0.16 0.03  0.29
   apply_prompt_updates_ms  1.12   1.08 0.18  1.85
     preprocessor_total_ms 26.22  25.17 4.02 38.35
        encoder_forward_ms 34.86  36.24 3.15 37.89
         num_encoder_calls  1.00   1.00 0.00  1.00

Summary: 100 total encoder calls across 100 requests.

End-to-End Latency (ms):
Metric Value (ms)
  Mean    7565.73
Median    8693.90
   Std    3326.70
 P99.0   11142.58
```

### Benchmark-2

```bash
vllm bench mm-processor \
--model /home/sss/.cache/modelscope/hub/models/Qwen/Qwen3-VL-4B-Instruct \
--dataset-name random-mm \
--random-mm-base-items-per-request 5 \
--random-mm-num-mm-items-range-ratio 0.0 \
--random-mm-limit-mm-per-prompt '{"image": 0, "video": 10}' \
--random-mm-bucket-config '{"(256,256,16)": 1.0}' \
--num-prompts 100 \
--num-warmups 20 \
--max-model-len 8192 \
--seed 42 \
--mm-encoder-attn-backend FLASHINFER \
--compilation-config '{"cudagraph_mm_encoder": true, "encoder_cudagraph_token_budgets": [512, 1024, 1536, 2048, 2560, 3072, 3584, 4096, 4864], "encoder_cudagraph_max_images_per_batch": 8}'

--tensor-parallel-size 4 \
--mm-encoder-tp-mode data \
```

**Eager:**

```
random-mm-base-items-per-request = 2
================================================================================
Multimodal Processor Benchmark Results
================================================================================

MM Processor Metrics:
                     Stage  Mean Median  Std P99.0
          get_mm_hashes_ms  1.21   1.20 0.15  1.72
get_cache_missing_items_ms  0.03   0.03 0.01  0.05
     apply_hf_processor_ms 16.89  16.52 3.03 23.83
        merge_mm_kwargs_ms  0.12   0.10 0.18  0.28
   apply_prompt_updates_ms  1.57   1.62 0.17  2.13
     preprocessor_total_ms 19.82  19.48 3.15 27.08
        encoder_forward_ms 10.60   8.89 9.43 34.50
         num_encoder_calls  1.01   1.00 0.08  1.00

Summary: 1006 total encoder calls across 1000 requests.

End-to-End Latency (ms):
Metric Value (ms)
  Mean   65927.81
Median   66208.79
   Std    7500.84
 P99.0   80698.65


random-mm-base-items-per-request = 5
================================================================================
Multimodal Processor Benchmark Results
================================================================================

MM Processor Metrics:
                     Stage  Mean Median   Std  P99.0
          get_mm_hashes_ms  3.65   3.50  0.44   4.97
get_cache_missing_items_ms  0.04   0.04  0.01   0.06
     apply_hf_processor_ms 22.01  21.31  2.07  29.07
        merge_mm_kwargs_ms  0.17   0.16  0.03   0.26
   apply_prompt_updates_ms  2.65   2.62  0.21   3.41
     preprocessor_total_ms 28.51  27.79  2.42  37.57
        encoder_forward_ms 20.73  14.86 32.98 250.75
         num_encoder_calls  1.01   1.00  0.10   1.01

Summary: 101 total encoder calls across 100 requests.

End-to-End Latency (ms):
Metric Value (ms)
  Mean   11840.93
Median   11637.87
   Std    5211.33
 P99.0   20597.67
```

**Full CG:**

```
Encoder CUDA graph capture complete. Captured 18 budget graphs across 2 modality/ies.
CUDA graph pool memory: 7.46 GiB (actual), 0.58 GiB (estimated), difference: 6.88 GiB (92.2%).

random-mm-base-items-per-request = 2
================================================================================
Multimodal Processor Benchmark Results
================================================================================

MM Processor Metrics:
                     Stage  Mean Median  Std P99.0
          get_mm_hashes_ms  1.26   1.23 0.90  1.72
get_cache_missing_items_ms  0.03   0.03 0.01  0.05
     apply_hf_processor_ms 17.01  16.60 2.80 26.76
        merge_mm_kwargs_ms  0.11   0.10 0.04  0.29
   apply_prompt_updates_ms  1.62   1.65 0.32  2.10
     preprocessor_total_ms 20.03  19.66 3.10 30.93
        encoder_forward_ms 20.80  20.51 2.29 35.29
         num_encoder_calls  1.01   1.00 0.10  1.01

Summary: 1010 total encoder calls across 1000 requests.

End-to-End Latency (ms):
Metric Value (ms)
  Mean   71919.20
Median   73277.24
   Std    7479.62
 P99.0   84789.84


random-mm-base-items-per-request = 5
================================================================================
Multimodal Processor Benchmark Results
================================================================================

MM Processor Metrics:
                     Stage  Mean Median  Std P99.0
          get_mm_hashes_ms  3.65   3.53 0.46  5.91
get_cache_missing_items_ms  0.04   0.04 0.01  0.06
     apply_hf_processor_ms 22.08  21.24 2.99 31.61
        merge_mm_kwargs_ms  0.17   0.17 0.03  0.24
   apply_prompt_updates_ms  2.69   2.62 0.39  4.62
     preprocessor_total_ms 28.64  27.84 3.40 40.25
        encoder_forward_ms 27.70  25.92 5.38 45.04
         num_encoder_calls  1.01   1.00 0.10  1.01

Summary: 101 total encoder calls across 100 requests.

End-to-End Latency (ms):
Metric Value (ms)
  Mean   12211.95
Median   11923.98
   Std    5398.08
 P99.0   21493.23
```

报错：

```bash
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101] EngineCore encountered a fatal error.
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101] Traceback (most recent call last):
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]   File "/home/sss/github/vllm/vllm/v1/engine/core.py", line 1092, in run_engine_core
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]     engine_core.run_busy_loop()
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]   File "/home/sss/github/vllm/vllm/v1/engine/core.py", line 1133, in run_busy_loop
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]     self._process_engine_step()
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]   File "/home/sss/github/vllm/vllm/v1/engine/core.py", line 1172, in _process_engine_step
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]     outputs, model_executed = self.step_fn()
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]                               ^^^^^^^^^^^^^^
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]   File "/home/sss/github/vllm/vllm/v1/engine/core.py", line 449, in step_with_batch_queue
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]     exec_future = self.model_executor.execute_model(
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]                   ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]   File "/home/sss/github/vllm/vllm/v1/executor/uniproc_executor.py", line 112, in execute_model
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]     output.result()
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]   File "/usr/lib/python3.12/concurrent/futures/_base.py", line 449, in result
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]     return self.__get_result()
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]            ^^^^^^^^^^^^^^^^^^^
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]   File "/usr/lib/python3.12/concurrent/futures/_base.py", line 401, in __get_result
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]     raise self._exception
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]   File "/home/sss/github/vllm/vllm/v1/executor/uniproc_executor.py", line 82, in collective_rpc
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]     result = run_method(self.driver_worker, method, args, kwargs)
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]   File "/home/sss/github/vllm/vllm/v1/serial_utils.py", line 459, in run_method
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]     return func(*args, **kwargs)
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]            ^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]   File "/home/sss/github/vllm/vllm/v1/worker/worker_base.py", line 332, in execute_model
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]     return self.worker.execute_model(scheduler_output)
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]   File "/home/sss/github/vllm/.venv/lib/python3.12/site-packages/torch/utils/_contextlib.py", line 124, in decorate_context
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]     return func(*args, **kwargs)
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]            ^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]   File "/home/sss/github/vllm/vllm/v1/worker/gpu_worker.py", line 819, in execute_model
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]     output = self.model_runner.execute_model(
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]   File "/home/sss/github/vllm/.venv/lib/python3.12/site-packages/torch/utils/_contextlib.py", line 124, in decorate_context
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]     return func(*args, **kwargs)
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]            ^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]   File "/home/sss/github/vllm/vllm/v1/worker/gpu_model_runner.py", line 3761, in execute_model
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]     ) = self._preprocess(
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]         ^^^^^^^^^^^^^^^^^
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]   File "/home/sss/github/vllm/vllm/v1/worker/gpu_model_runner.py", line 3013, in _preprocess
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]     self._execute_mm_encoder(scheduler_output)
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]   File "/home/sss/github/vllm/vllm/v1/worker/gpu_model_runner.py", line 2659, in _execute_mm_encoder
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]     cudagraph_output = self.encoder_cudagraph_manager.execute(
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]                        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]   File "/home/sss/github/vllm/vllm/v1/worker/gpu/mm/encoder_cudagraph.py", line 626, in execute
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]     result = self._execute_local(mm_kwargs)
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]   File "/home/sss/github/vllm/vllm/v1/worker/gpu/mm/encoder_cudagraph.py", line 424, in _execute_local
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]     output = self._run_budget_graph(
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]              ^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]   File "/home/sss/github/vllm/vllm/v1/worker/gpu/mm/encoder_cudagraph.py", line 260, in _run_budget_graph
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101]     graph_meta.input_buffer[:n].copy_(src)
(EngineCore pid=291417) ERROR 03-17 09:15:28 [core.py:1101] RuntimeError: The size of tensor a (1920) must match the size of tensor b (2048) at non-singleton dimension 0
```

# Benchmark Results

## Scenario

1. no DP VIT + cudagraph vs no DP VIT + no cudagraph.
2. DP VIT + cudagraph vs DP VIT + no cudagraph.
3. With different workloads to know if it always provides performance boost.

Single GPU (Qwen3-VL-30B, 1×GB200, VisionArena-Chat, 3000 prompts)
Multi GPU (Qwen3-VL-32B, 4×GB200 TP=4 DP=4, random-mm 20img/req, 1000 prompts)

## Performance Tuning

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

```bash
# 参考配置：
vllm bench mm-processor \
--model Qwen/Qwen3-VL-32B-Instruct \
--dataset-name random-mm \
--random-mm-base-items-per-request 20 \
--random-mm-num-mm-items-range-ratio 0.0 \
--random-mm-bucket-config '{"(336,336,1)": 1.0}' \
--num-prompts 1000 \
--num-warmups 200 \
--max-model-len 8192 \
--seed 42 \
--mm-encoder-attn-backend FLASHINFER \
--tensor-parallel-size 4 \
--mm-encoder-tp-mode data \
--compilation-config '{"cudagraph_mm_encoder": true, "encoder_cudagraph_token_budgets": [512, 1024, 1536, 2048, 2560, 3072, 3584, 4096, 4864], "encoder_cudagraph_max_images_per_batch": 8}'

vllm bench mm-processor \
--model /home/sss/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct \
--dataset-name random-mm \
--num-prompts 100 \
--random-input-len 300 \
--random-output-len 40 \
--random-mm-base-items-per-request 1 \
--random-mm-limit-mm-per-prompt '{"image": 0, "video": 1}' \
--random-mm-bucket-config '{(256, 256, 8): 1}' \
--mm-encoder-attn-backend FLASHINFER \
--compilation-config '{"cudagraph_mm_encoder": true, "encoder_cudagraph_token_budgets": [512, 1024, 1536, 2048, 2560, 3072, 3584, 4096, 4864], "encoder_cudagraph_max_images_per_batch": 8, "encoder_cudagraph_max_frames_per_batch": 64}'

--mm-encoder-attn-backend FLASHINFER
--mm-encoder-attn-backend FLASH_ATTN
```

---

```bash
export VLLM_USE_MODELSCOPE=False
export HF_ENDPOINT="https://hf-mirror.com"

vllm bench mm-processor \
--model /home/sss/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct \
--dataset-name random-mm \
--num-prompts 100 \
--random-input-len 300 \
--random-output-len 40 \
--random-mm-base-items-per-request 1 \
--random-mm-limit-mm-per-prompt '{"image": 0, "video": 1}' \
--random-mm-bucket-config '{(256, 256, 8): 1}' \
--mm-encoder-attn-backend FLASHINFER \
--compilation-config '{"cudagraph_mm_encoder": true, "encoder_cudagraph_token_budgets": [512, 1024, 1536, 2048, 2560, 3072, 3584, 4096, 4864], "encoder_cudagraph_max_images_per_batch": 8, "encoder_cudagraph_max_frames_per_batch": 64}'
```

Eager (no DP VIT + no cudagraph):

```bash
# FLASH_ATTN:

# FLASHINFER:
================================================================================
Multimodal Processor Benchmark Results
================================================================================

MM Processor Metrics:
                     Stage  Mean Median   Std P99.0
          get_mm_hashes_ms  0.46   0.43  0.13  1.05
get_cache_missing_items_ms  0.02   0.02  0.01  0.06
     apply_hf_processor_ms  4.40   4.02  1.36 10.25
        merge_mm_kwargs_ms  0.07   0.06  0.03  0.16
   apply_prompt_updates_ms  0.64   0.62  0.13  1.23
     preprocessor_total_ms  5.59   5.15  1.62 12.15
        encoder_forward_ms 13.27   3.96 20.60 91.31
         num_encoder_calls  1.00   1.00  0.00  1.00

Summary: 100 total encoder calls across 100 requests.

End-to-End Latency (ms):
Metric Value (ms)
  Mean    4108.72
Median    4051.58
   Std     593.24
 P99.0    6170.69

================================================================================
Multimodal Processor Benchmark Results
================================================================================

MM Processor Metrics:
                     Stage Mean Median  Std P99.0
          get_mm_hashes_ms 0.43   0.42 0.07  0.54
get_cache_missing_items_ms 0.02   0.02 0.00  0.03
     apply_hf_processor_ms 4.02   3.92 0.55  6.59
        merge_mm_kwargs_ms 0.06   0.06 0.02  0.10
   apply_prompt_updates_ms 0.55   0.55 0.13  0.94
     preprocessor_total_ms 5.08   4.98 0.66  7.83
        encoder_forward_ms 5.47   4.00 7.97 41.53
         num_encoder_calls 1.00   1.00 0.00  1.00

Summary: 1000 total encoder calls across 1000 requests.

End-to-End Latency (ms):
Metric Value (ms)
  Mean   23242.37
Median   23166.89
   Std    2886.33
 P99.0   28097.66
```

CG (no DP VIT + cudagraph):

```bash
# FLASH_ATTN:

# FLASHINFER:
================================================================================
Multimodal Processor Benchmark Results
================================================================================

MM Processor Metrics:
                     Stage Mean Median  Std P99.0
          get_mm_hashes_ms 0.46   0.43 0.16  1.38
get_cache_missing_items_ms 0.02   0.02 0.01  0.09
     apply_hf_processor_ms 4.23   3.93 1.50 11.78
        merge_mm_kwargs_ms 0.06   0.06 0.02  0.17
   apply_prompt_updates_ms 0.60   0.58 0.15  1.34
     preprocessor_total_ms 5.38   5.02 1.83 14.76
        encoder_forward_ms 9.30   8.77 2.28 24.07
         num_encoder_calls 1.00   1.00 0.00  1.00

Summary: 100 total encoder calls across 100 requests.

End-to-End Latency (ms):
Metric Value (ms)
  Mean    3894.05
Median    3734.34
   Std     702.11
 P99.0    5768.13

================================================================================
Multimodal Processor Benchmark Results
================================================================================

MM Processor Metrics:
                     Stage Mean Median  Std P99.0
          get_mm_hashes_ms 0.41   0.42 0.06  0.56
get_cache_missing_items_ms 0.02   0.02 0.00  0.03
     apply_hf_processor_ms 3.84   3.78 0.59  5.78
        merge_mm_kwargs_ms 0.06   0.06 0.02  0.08
   apply_prompt_updates_ms 0.52   0.51 0.10  0.76
     preprocessor_total_ms 4.85   4.83 0.73  6.81
        encoder_forward_ms 9.09   8.75 1.71 13.79
         num_encoder_calls 1.00   1.00 0.00  1.00

Summary: 1000 total encoder calls across 1000 requests.

End-to-End Latency (ms):
Metric Value (ms)
  Mean   25468.70
Median   24478.94
   Std    4213.29
 P99.0   32076.79
```

---

```bash
export VLLM_USE_MODELSCOPE=False
export HF_ENDPOINT="https://hf-mirror.com"

vllm bench mm-processor \
--model /home/sss/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct \
--dataset-name random-mm \
--random-mm-base-items-per-request 1 \
--random-mm-num-mm-items-range-ratio 0.0 \
--random-mm-bucket-config '{(256, 256, 8): 1.0}' \
--random-mm-limit-mm-per-prompt '{"image": 0, "video": 1}' \
--num-prompts 1000 \
--random-input-len 300 \
--random-output-len 40 \
--seed 42 \
--mm-encoder-attn-backend FLASH_ATTN or FLASHINFER \
--compilation-config '{"cudagraph_mm_encoder": true, "encoder_cudagraph_token_budgets": [512, 1024, 1536, 2048, 2560, 3072, 3584, 4096, 4864], "encoder_cudagraph_max_images_per_batch": 8, "encoder_cudagraph_max_frames_per_batch": 64}'

--num-warmups 20 \
--max-model-len 8192 \
# Sampling input_len from [1024, 1024] and output_len from [128, 128]
```

Eager (no DP VIT + no cudagraph):

```bash
# FLASH_ATTN:
encoder_forward_ms 3.88 / 20.03  # Mean / P99.0
# 1000 request:
encoder_forward_ms 3.41 / 7.61

# FLASHINFER:
encoder_forward_ms 13.81 / 94.86
encoder_forward_ms 17.29 / 77.42
# 1000 request:
encoder_forward_ms 5.53 / 41.40
```

CG (no DP VIT + cudagraph):

```bash
# FLASH_ATTN:
encoder_forward_ms 4.50 / 16.72
encoder_forward_ms 4.75 / 15.16
# 1000 request:
encoder_forward_ms 4.52 / 15.92

# FLASHINFER:
encoder_forward_ms 9.34 / 26.11
# 1000 request:
encoder_forward_ms 9.09 / 13.70
```

| Backend | Mean | P99 |
| :-----: | :--: | :-: |
| FLASH_ATTN | -12.5% (4.00ms -> 4.50ms) | +24.3% (20.03ms -> 15.16ms) |
| FLASHINFER | +32.4% (13.81ms -> 9.34ms) | +66.9% (41.40ms -> 13.70ms) |

## Single GPU (Qwen3-VL-8B-Instruct, A100 GPU, random-mm, 100 prompts)

```bash
cd ~/github/vllm/
source .venv/bin/activate
export VLLM_USE_MODELSCOPE=False
export HF_ENDPOINT="https://hf-mirror.com"

vllm bench mm-processor \
--model /home/sss/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct \
--max-model-len 8192 \
--dataset-name random-mm \
--random-mm-base-items-per-request 1 \
--random-mm-num-mm-items-range-ratio 0.0 \
--random-mm-bucket-config '{(224, 224, 8): 1.0}' \
--random-mm-limit-mm-per-prompt '{"image": 0, "video": 1}' \
--num-prompts 1000 \
--seed 42 \
--mm-encoder-attn-backend FLASHINFER \
--compilation-config '{"cudagraph_mm_encoder": true, "encoder_cudagraph_token_budgets": [128, 256, 512, 1024, 1536, 2048], "encoder_cudagraph_max_mm_items_per_batch": 4, "encoder_cudagraph_max_frames_per_batch": 32}'

# Sampling input_len from [1024, 1024] and output_len from [128, 128]
--mm-encoder-attn-backend FLASH_ATTN or FLASHINFER \
--num-warmups 20 \

# --random-mm-bucket-config '{(224, 224, 4): 1.0}' \ "encoder_cudagraph_token_budgets": [128, 256, 512, 1024] ❌（P99 都打不过了）
# --random-mm-bucket-config '{(224, 224, 8): 1.0}' \ "encoder_cudagraph_token_budgets": [128, 256, 512, 1024, 1536, 2048] ❌（只有 Mean 打不过）
# --random-mm-bucket-config '{(448, 448, 16): 1.0}' \ "encoder_cudagraph_token_budgets": [256, 512, 1024, 2048, 4096] ❌（P99 都打不过了）
```

Eager:

```bash
# FLASH_ATTN
# (224, 224, 4) / 1000 prompts / max-model-len 8192
3.27/5.84
# (224, 224, 8) / 100 prompts / max-model-len 16384 ✅
3.67/10.62 3.99/9.92 3.60/15.77

# FLASHINFER
# (224, 224, 8) / 100 prompts / max-model-len 16384 ✅
9.11/77.83 8.38/58.62
# (224, 224, 8) / 200 prompts / max-model-len 8192
5.99/57.88
# (224, 224, 8) / 1000 prompts / max-model-len 8192
4.50/20.23
```

CG:

```bash
# FLASH_ATTN
# (224, 224, 4) / 1000 prompts / max-model-len 8192
4.32/6.35
# (224, 224, 8) / 100 prompts / max-model-len 16384 ✅
4.38/6.27 4.37/5.85 4.54/5.62 4.58/6.11(no-cache)

# FLASHINFER
# (224, 224, 8) / 100 prompts / max-model-len 16384 ✅
6.55/7.27 6.80/7.42 6.83/7.01(no-cache)
# (224, 224, 8) / 200 prompts / max-model-len 8192
6.62/8.58 6.87/7.33 6.84/8.14(no-cache)
# (224, 224, 8) / 1000 prompts / max-model-len 8192
6.63/7.72 6.80/7.19 6.89/8.18(no-cache)
```

| Backend | Mean | P99 |
| :-----: | :--: | :-: |
| FLASH_ATTN | -9.52% (3.99ms -> 4.37ms) | +41.03% (9.92ms -> 5.85ms) |
| FLASHINFER | +21.84% (8.38ms -> 6.55ms) | +87.60% (58.62ms -> 7.27ms) |

## Multi GPU

```bash
cd ~/github/vllm/
source .venv/bin/activate
export VLLM_USE_MODELSCOPE=False
export HF_ENDPOINT="https://hf-mirror.com"

vllm bench mm-processor \
--model /home/sss/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct \
--max-model-len 16384 \
--dataset-name random-mm \
--random-mm-base-items-per-request 8 \
--random-mm-num-mm-items-range-ratio 0.0 \
--random-mm-bucket-config '{(224, 224, 8): 1.0}' \
--random-mm-limit-mm-per-prompt '{"image": 0, "video": 1}' \
--num-prompts 100 \
--num-warmups 10 \
--seed 42 \
--tensor-parallel-size 4 \
--mm-encoder-tp-mode data \
--mm-encoder-attn-backend FLASH_ATTN \
--compilation-config '{"cudagraph_mm_encoder": true, "encoder_cudagraph_token_budgets": [128, 256, 512, 1024, 1536, 2048], "encoder_cudagraph_max_mm_items_per_batch": 4, "encoder_cudagraph_max_frames_per_batch": 32}'
```

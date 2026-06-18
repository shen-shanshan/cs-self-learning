# Benchmark

## Targets

在不同切分策略下对比 eager / single-path / dual-path 的性能。

## Weight Download

我想使用如下的脚本将 stepfun-ai/Step3-VL-10B 模型的权重下载到 /shared/models/modelscope/models/ 目录下，MODELSCOPE_CACHE 应该怎么设置？

```python
import os
from modelscope import snapshot_download

os.environ["MODELSCOPE_CACHE"] = "/shared/models/modelscope"

model_dir = snapshot_download("stepfun-ai/Step3-VL-10B")
```

其它模型的权重存放路径示例：/shared/models/modelscope/models/deepseek-ai/DeepSeek-OCR

## Commands

```bash
source .venv/bin/activate

export CUDA_VISIBLE_DEVICES=7
export VLLM_USE_MODELSCOPE=False
export HF_ENDPOINT="https://hf-mirror.com"
export VLLM_WORKER_MULTIPROC_METHOD=spawn
export PYTORCH_CUDA_ALLOC_CONF=expandable_segments:True
export TMPDIR=/home/sss-host/tmp
export TORCHINDUCTOR_CACHE_DIR=/home/sss-host/torchinductor_cache

vllm bench mm-processor \
--model /shared/models/modelscope/models/stepfun-ai/Step3-VL-10B \
--max-model-len 16384 \
--dataset-name random-mm \
--random-mm-base-items-per-request 1 \
--random-mm-num-mm-items-range-ratio 0.0 \
--random-mm-bucket-config '{(1024, 448, 1): 1.0}' \
--random-mm-limit-mm-per-prompt '{"image": 1, "video": 0}' \
--num-prompts 100 \
--seed 42 \
--trust-remote-code \
--compilation-config '{"cudagraph_mm_encoder": true, "encoder_cudagraph_max_vision_items_per_batch": 8}'

# --gpu-memory-utilization=0.8 \
# EncoderCudaGraphManager initialized with budgets=[169, 338, 676, 1352, 2704, 5408, 8192], max_batch_size=4, max_frames_per_batch=4, use_dp=False
```

## Results

Mean Latency:

| Input Size | Tiled | Single-Path ViT Graph | Dual-Path ViT Graph (This PR) |
| :--------: | :---: | :-------------------: | :---------------------------: |
| (448, 448) | ❌ | 109.31ms | 103.54ms (5.28% ↓) |
| (1024, 448) | ✅ | RuntimeError | 221.26ms |

P99 Latency:

| Input Size | Tiled | Single-Path ViT Graph | Dual-Path ViT Graph (This PR) |
| :--------: | :---: | :-------------------: | :---------------------------: |
| (448, 448) | ❌ | 142.16ms | 136.38ms (4.07% ↓) |
| (1024, 448) | ✅ | RuntimeError | 258.26ms |

> [!NOTE]
> When `long_side < 728` and `long_side / short_side <= 1.5`, the mm inputs will only contain global image, without generating local patches.

---

当 长边 < 728 且 长边/短边 ≤ 1.5，即图像较小且大致为正方形时，不生成任何 patch。

--random-mm-bucket-config '{(448, 448, 1): 1.0}' \
--random-mm-bucket-config '{(1024, 448, 1): 1.0}' \

---

**(448, 448):**

Eager End-to-End Latency (ms):
  Mean   18290.44
 P99.0   19430.71

Single-Path End-to-End Latency (ms):
  Mean   20622.70
 P99.0   21398.50

Dual-Path End-to-End Latency (ms):
  Mean   20098.79
 P99.0   20854.85

---

**(1024, 448):**

Eager End-to-End Latency (ms):
  Mean   26741.07
 P99.0   29618.25

Single-Path End-to-End Latency (ms):

(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]   File "/home/sss-host/github/vllm/vllm/v1/worker/encoder_cudagraph.py", line 321, in _copy_padded_buffer
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]     dst[: src.shape[0]].copy_(src)
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231] RuntimeError: The size of tensor a (0) must match the size of tensor b (2) at non-singleton dimension 0

Dual-Path End-to-End Latency (ms):
  Mean   32296.27
 P99.0   34171.91

---

Mean Latency:

| Input Size | Tiled | Eager | Single-Path ViT Graph | Dual-Path ViT Graph (This PR) |
| :--------: | :---: | :---: | :-------------------: | :---------------------------: |
| (448, 448) | ❌ | 86.14ms | 109.31ms (xx.xx% ↓) | 103.54ms (xx.xx% ↓) |
| (1024, 448) | ✅ | 164.92ms | RuntimeError | 221.26ms (xx.xx% ↓) |

P99 Latency:

| Input Size | Tiled | Eager | Single-Path ViT Graph | Dual-Path ViT Graph (This PR) |
| :--------: | :---: | :---: | :-------------------: | :---------------------------: |
| (448, 448) | ❌ | 87.22ms | 142.16ms (xx.xx% ↓) | 136.38ms (xx.xx% ↓) |
| (1024, 448) | ✅ | 187.45ms | RuntimeError | 258.26ms (xx.xx% ↓) |

---

```bash
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231] EngineCore encountered a fatal error.
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231] Traceback (most recent call last):
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]   File "/home/sss-host/github/vllm/vllm/v1/engine/core.py", line 1222, in run_engine_core
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]     engine_core.run_busy_loop()
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]   File "/home/sss-host/github/vllm/vllm/v1/engine/core.py", line 1263, in run_busy_loop
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]     self._process_engine_step()
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]   File "/home/sss-host/github/vllm/vllm/v1/engine/core.py", line 1302, in _process_engine_step
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]     outputs, model_executed = self.step_fn()
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]                               ^^^^^^^^^^^^^^
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]   File "/home/sss-host/github/vllm/vllm/v1/engine/core.py", line 549, in step_with_batch_queue
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]     exec_future = self.model_executor.execute_model(
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]                   ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]   File "/home/sss-host/github/vllm/vllm/v1/executor/uniproc_executor.py", line 120, in execute_model
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]     output.result()
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]   File "/usr/lib/python3.12/concurrent/futures/_base.py", line 449, in result
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]     return self.__get_result()
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]            ^^^^^^^^^^^^^^^^^^^
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]   File "/usr/lib/python3.12/concurrent/futures/_base.py", line 401, in __get_result
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]     raise self._exception
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]   File "/home/sss-host/github/vllm/vllm/v1/executor/uniproc_executor.py", line 98, in collective_rpc
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]     result = run_method(self.driver_worker, method, args, kwargs)
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]   File "/home/sss-host/github/vllm/vllm/v1/serial_utils.py", line 510, in run_method
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]     return func(*args, **kwargs)
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]            ^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]   File "/home/sss-host/github/vllm/vllm/v1/worker/worker_base.py", line 345, in execute_model
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]     return self.worker.execute_model(scheduler_output)
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]   File "/home/sss-host/github/vllm/.venv/lib/python3.12/site-packages/torch/utils/_contextlib.py", line 124, in decorate_context
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]     return func(*args, **kwargs)
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]            ^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]   File "/home/sss-host/github/vllm/vllm/v1/worker/gpu_worker.py", line 868, in execute_model
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]     output = self.model_runner.execute_model(
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]   File "/home/sss-host/github/vllm/.venv/lib/python3.12/site-packages/torch/utils/_contextlib.py", line 124, in decorate_context
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]     return func(*args, **kwargs)
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]            ^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]   File "/home/sss-host/github/vllm/vllm/v1/worker/gpu_model_runner.py", line 4278, in execute_model
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]     ) = self._preprocess(
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]         ^^^^^^^^^^^^^^^^^
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]   File "/home/sss-host/github/vllm/vllm/v1/worker/gpu_model_runner.py", line 3453, in _preprocess
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]     self._execute_mm_encoder(scheduler_output)
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]   File "/home/sss-host/github/vllm/vllm/v1/worker/gpu_model_runner.py", line 3078, in _execute_mm_encoder
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]     cudagraph_output = self.encoder_cudagraph_manager.execute(
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]                        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]   File "/home/sss-host/github/vllm/vllm/v1/worker/encoder_cudagraph.py", line 836, in execute
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]     result = self._execute_local(mm_kwargs)
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]   File "/home/sss-host/github/vllm/vllm/v1/worker/encoder_cudagraph.py", line 404, in _execute_local
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]     return self._execute_local_single_path(mm_kwargs)
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]   File "/home/sss-host/github/vllm/vllm/v1/worker/encoder_cudagraph.py", line 498, in _execute_local_single_path
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]     output = self._run_budget_graph(batch_mm_kwargs, token_budget)
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]   File "/home/sss-host/github/vllm/vllm/v1/worker/encoder_cudagraph.py", line 367, in _run_budget_graph
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]     padding_logic(buf, src)
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]   File "/home/sss-host/github/vllm/vllm/v1/worker/encoder_cudagraph.py", line 321, in _copy_padded_buffer
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231]     dst[: src.shape[0]].copy_(src)
(EngineCore pid=334401) ERROR 06-18 09:30:36 [core.py:1231] RuntimeError: The size of tensor a (0) must match the size of tensor b (2) at non-singleton dimension 0
```

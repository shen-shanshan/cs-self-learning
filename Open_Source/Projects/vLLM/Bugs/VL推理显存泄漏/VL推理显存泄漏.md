```bash
# server
export VLLM_SERVER_DEV_MODE=1
export ASCEND_RT_VISIBLE_DEVICES=2,3

vllm serve Qwen/Qwen3-VL-32B-Instruct \
--tensor-parallel-size 2 \
--load-format=dummy \
--max-model-len 162000

python -m vllm.entrypoints.openai.api_server \
--model Qwen/Qwen3-VL-32B-Instruct \
--host 0.0.0.0 \
--port 8000 \
--tensor-parallel-size 2 \
--load-format=dummy \
--max-model-len 163000

vllm serve \
Qwen/Qwen3-VL-32B-Instruct \
--tensor-parallel-size 2 \
--load-format=dummy \
--dtype bfloat16 \

--max-model-len 16384 \
--gpu-memory-utilization 0.97 \
--limit-mm-per-prompt.video=0 \
--max-num-seqs 4 \

# dataset
opendatalab/OmniDocBench

# client
export VLLM_USE_MODELSCOPE=False
export HF_ENDPOINT="https://hf-mirror.com"
vllm bench serve \
--backend openai-chat \
--endpoint /v1/chat/completions \
--model Qwen/Qwen3-VL-32B-Instruct \
--dataset-name hf \
--dataset-path lmarena-ai/VisionArena-Chat \
--num-prompts 1000 \
--request-rate 20

# 实时监视显存占用
# -n 1：每 1 秒刷新一次
watch -n 0.2 npu-smi info

curl -X POST http://localhost:8000/reset_mm_cache
curl -X POST http://localhost:8000/reset_prefix_cache

curl http://localhost:8000/server_info
curl http://localhost:8000/is_sleeping
```

服务启动前：3400/65536
服务启动后：57766/65536
压测之后：61548/65536 (增长 3.7 G)
调用 reset_mm_cache 后：61548/65536

怀疑对象：
reset_mm_cache
clear_mm_cache

```python
class InputProcessor:
    self.mm_processor_cache = processor_cache_from_config(vllm_config, mm_registry)
    self.input_preprocessor = InputPreprocessor(
        self.model_config,
        tokenizer,
        mm_registry,
        mm_processor_cache=self.mm_processor_cache,
    )

class GPUModelRunner:
    # mm_hash -> encoder_output
    self.encoder_cache: dict[str, torch.Tensor] = {}

    def reset_mm_cache(self) -> None:
        if self.mm_budget:
            self.mm_budget.reset_cache()

class MultiModalBudget:
    self.cache = cache = processor_only_cache_from_config(model_config, mm_registry)
    self.cache.clear_cache()

class MultiModalProcessorOnlyCache:
    self._cache = MultiModalCache.get_lru_cache()

class LRUCache
```

```bash
Loading model weights took 31.4592 GB
Encoder cache will be initialized with a budget of 16384 tokens, and profiled with 1 image items of the maximum feature size.

Available memory: 21540531712
    total memory: 65452113920
```

---

准备数据：

```bash
scp *.jpg root@139.9.155.20:/home/sss/images

# large size
scp pexels-bertellifotografia-16094061.jpg root@139.9.155.20:/home/sss/large_images
```

启动服务：

```bash
vllm serve /root/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct \
--tensor-parallel-size 2 \
--max-model-len 65536 \
--max-num-seqs 8 \
--max-num-batched-tokens 32768 \
--gpu-memory-utilization 0.95
```

启动前：3409
启动后：55441
推理后：

---

```bash
conda create -n vllm-v0.12.0 python=3.11 -y

cd vllm
git fetch --tags
git tag -l | grep v0.12.0
git checkout -b v0.12.0 v0.12.0

cd vllm-ascend
git fetch --tags
git tag -l | grep v0.12.0rc1
git checkout -b v0.12.0rc1 v0.12.0rc1
```

---

MM Encoder Cache 相关代码：

- CPU 的预处理 Cache ？
- GPU 的特征 Cache ？

```python
class SchedulerConfig:
    def __post_init__():
        self.max_num_encoder_input_tokens = self.max_num_batched_tokens
        self.encoder_cache_size = self.max_num_batched_tokens

class Scheduler:
    def __init__():
        encoder_compute_budget, encoder_cache_size = compute_encoder_budget()  # -> compute_mm_encoder_budget()
        self.encoder_cache_manager = EncoderCacheManager(cache_size=encoder_cache_size)  # cache_size = 32768


class EncoderCacheManager:
    """Manages caching of encoder outputs for multimodal models in vLLM V1."""


class MultiModalBudget:
    def __init__():
        # Compute the encoder cache budget based on the model and scheduler configurations for a multimodal model.
        encoder_compute_budget, encoder_cache_size = compute_mm_encoder_budget(
            scheduler_config,
            max_tokens_by_modality,
        )

    def get_encoder_budget(self) -> int:
        return min(self.encoder_compute_budget, self.encoder_cache_size)


class GPUModelRunner:
    def __init__():
        # mm_hash -> encoder_output
        self.encoder_cache: dict[str, torch.Tensor] = {}  # GPU 的特征 Cache
        self.mm_budget = MultiModalBudget()
    
    def profile_run():
        mm_budget = self.mm_budget
        encoder_budget := mm_budget.get_encoder_budget()  # num_tokens
        # Create dummy batch of multimodal inputs.
        batched_dummy_mm_inputs = self._get_mm_dummy_batch()  # -> get_decoder_dummy_data()
        # Run multimodal encoder.
        dummy_encoder_outputs = self.model.embed_multimodal()
        for i, output in enumerate(dummy_encoder_outputs):
            self.encoder_cache[f"tmp_{i}"] = output
        self.encoder_cache.clear()
    
    def reset_mm_cache(self) -> None:
        if self.mm_budget:
            self.mm_budget.reset_cache()  # CPU 的预处理 Cache


class MultiModalProfiler:
    # Contains code for running memory profiling for multi-modal models.
    def get_decoder_dummy_data():
        mm_inputs = self._get_dummy_mm_inputs()

    def _get_dummy_mm_inputs():
        factory = self.dummy_inputs  # BaseDummyInputsBuilder
        processor_inputs = factory.get_dummy_processor_inputs()
        # Process multi-modal inputs to be used in vLLM.
        return self.processor.apply(
            prompt=processor_inputs.prompt,
            mm_data=processor_inputs.mm_data,
            hf_processor_mm_kwargs=processor_inputs.hf_processor_mm_kwargs,
            tokenization_kwargs=processor_inputs.tokenization_kwargs,
        )


class BaseDummyInputsBuilder:
    def __init__():
        self.info = info

    def get_dummy_processor_inputs():
        dummy_text = self.get_dummy_text()
        dummy_mm_data = self.get_dummy_mm_data()
        return ProcessorInputs(
            prompt=dummy_text,
            mm_data=dummy_mm_data,
            tokenization_kwargs=tokenization_kwargs,
        )

    def get_dummy_mm_data():
        # 抽象方法，不同模型去自定义自己的


class Qwen3VLDummyInputsBuilder:
    # self.info -> Qwen3VLProcessingInfo
    def get_dummy_mm_data():
        num_images = mm_counts.get("image", 0)
        num_videos = mm_counts.get("video", 0)
        # ...
        target_width, target_height = self.info.get_image_size_with_most_features()
        # ...
        return {
            "image": self._get_dummy_images(
                width=target_width,
                height=target_height,
                num_images=num_images,
                overrides=image_overrides,
            ),
            "video": self._get_dummy_videos(
                width=width,
                height=height,
                num_frames=target_num_frames,
                num_videos=num_videos,
            ),
        }


class Qwen3VLProcessingInfo(Qwen2VLProcessingInfo):
class Qwen2VLProcessingInfo:
    def get_image_size_with_most_features():
        # Qwen3-VL dummy_mm_data target_width: 4096.
        # Qwen3-VL dummy_mm_data target_height: 4096.
```

```
GPUModelRunner 多模态处理流程：

_preprocess()

    _execute_mm_encoder()
        curr_group_outputs: MultiModalEmbeddings
            A tensor of shape (num_items, feature_size, hidden_size)
        for ... group_mm_kwargs_by_modality:
            curr_group_outputs = model.embed_multimodal(**mm_kwargs_group)
        encoder_outputs.extend(curr_group_outputs)
        for mm_hash, output in zip(mm_hashes, encoder_outputs):
            self.encoder_cache[mm_hash] = output
    
    _gather_mm_embeddings()
```

---

```bash
NPU out of memory. NPUWorkspaceAllocator tried to allocate 180.00 MiB(NPU 1; 60.96 GiB total capacity; 86.81 MiB free). If you want to reduce memory usage, take a try to set the environment variable TASK_QUEUE_ENABLE=1.

ERR00006 PTA memory error

Memory_Allocation_Failure(EL0004): Failed to allocate memory

alloc device memory failed, runtime result = 207001[FUNC:ReportCallError][FILE:log_inner.cpp][LINE:162]

No available shared memory broadcast block found in 60 seconds. This typically happens when some processes are hanging or doing some time-consuming work (e.g. compilation, weight/kv cache quantization).

export ASCEND_LAUNCH_BLOCKING=1
```

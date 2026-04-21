_MAX_FRAMES_PER_VIDEO
get_num_frames_with_most_features() -> max_frames_per_video

---
**Tests:**

```python
def run_qwen3_vl(questions: list[str], modality: str) -> ModelRequestData:
    model_name = "/shared/models/modelscope/models/Qwen/Qwen3-VL-8B-Instruct"
    engine_args = EngineArgs(
        model=model_name,
        max_model_len=4096,
        max_num_seqs=5,
        mm_processor_kwargs={
            "min_pixels": 28 * 28,
            "max_pixels": 1280 * 28 * 28,
            "fps": 1,
        },
        limit_mm_per_prompt={"image": 1, "video": 1},
        compilation_config={
            "cudagraph_mm_encoder": True,
            "encoder_cudagraph_token_budgets": [512, 1024, 1536, 2048, 2560, 3072, 3584, 4096, 4864],
            "encoder_cudagraph_max_vision_items_per_batch": 8,
            "encoder_cudagraph_max_frames_per_batch": 64,
        }
    )
```

```bash
export VLLM_USE_MODELSCOPE=False
export HF_ENDPOINT="https://hf-mirror.com"

# "encoder_cudagraph_max_frames_per_batch": 64
python examples/offline_inference/vision_language.py -m qwen3_vl --modality "image"
# Disable video modality, so max_frames_per_batch will be set to 0
EncoderCudaGraphManager initialized with ..., max_frames_per_batch=0, ...

# "encoder_cudagraph_max_frames_per_batch": 64
python examples/offline_inference/vision_language.py -m qwen3_vl --modality "video"
# Normal scenario
EncoderCudaGraphManager initialized with ..., max_frames_per_batch=64, ...

# "encoder_cudagraph_max_frames_per_batch": None
python examples/offline_inference/vision_language.py -m qwen3_vl --modality "video"
# Auto infer max_frames_per_batch according to processing_info
EncoderCudaGraphManager initialized with ..., max_frames_per_batch=64, ...
```

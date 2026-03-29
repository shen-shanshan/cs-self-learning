# ViT 支持 Full CG

## Background

However, this [PR](https://github.com/vllm-project/vllm/pull/33232) specifically targets scenarios with fewer images or when images are distributed via ViT DP. In these cases, the computational load per rank is smaller, and the execution time is dominated by "bubbles" caused by kernel launch overhead rather than the operator execution itself. The performance gain from using CUDA Graphs to eliminate these bubbles outweighs the slight regression introduced by torch.compile.

## Support Image

```bash
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

## Support Video

### Prompt

在 vllm-project/vllm 项目中，PR（https://github.com/vllm-project/vllm/pull/35963）为 ViT 图像推理支持了 full cuda graph，PR（https://github.com/vllm-project/vllm/pull/37914）为对应的设计文档。
请参考上面两个 PR 的设计与实现，帮我在 vllm 的本地代码上为 ViT 视频推理支持 full cuda graph。
你需要先分析并制定整体的修改计划，包括：files to change、exact functions、potential risks、test cases 等，设计完先给我 review，我确认没问题了，你再开始修改。

### Functional test

```bash
export VLLM_USE_MODELSCOPE=False
export HF_ENDPOINT="https://hf-mirror.com"
# export PYTORCH_ALLOC_CONF=expandable_segments:True

# def run_qwen3_vl
# /home/sss/.cache/modelscope/hub/models/Qwen/Qwen3-VL-4B-Instruct
python examples/offline_inference/vision_language.py -m qwen3_vl --modality "video" --time-generate
```

```python
def run_qwen3_vl(questions: list[str], modality: str) -> ModelRequestData:
    model_name = "/home/sss/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct"
    engine_args = EngineArgs(
        model=model_name,
        max_model_len=4096,
        max_num_seqs=5,
        mm_processor_kwargs={
            "min_pixels": 28 * 28,
            "max_pixels": 1280 * 28 * 28,
            "fps": 1,
        },
        limit_mm_per_prompt={modality: 1},
        compilation_config={
            "cudagraph_mm_encoder": True,
            "encoder_cudagraph_token_budgets": [512, 1024, 1536, 2048, 2560, 3072, 3584, 4096, 4864],
            "encoder_cudagraph_max_images_per_batch": 8,
            "encoder_cudagraph_max_frames_per_batch": 64,
        }
    )
```

Output (eager):

```bash
--------------------------------------------------
-- generate time = 3.3906428813934326
--------------------------------------------------
--------------------------------------------------
The video is funny because it captures a baby wearing glasses and pretending to read a book, which is an adorable and endearing sight. The baby’s serious expression and focused demeanor while holding the book, combined with the fact that they are clearly not reading it, creates a humorous contrast. The baby’s movements, such as
--------------------------------------------------
The video is funny because it captures a toddler wearing glasses and pretending to read a book, which is an adorable and endearing sight. The child's serious demeanor and focused expression while flipping through the pages add to the humor, as it's clear they are taking the activity very seriously. The contrast between the child's innocent
--------------------------------------------------
The video is funny because it captures a toddler wearing glasses and pretending to read a book with such seriousness and focus. The child’s earnestness and the way they turn the pages, as if they’re a grown-up reader, is endearing and absurd. The contrast between the child’s tiny size and the adult-like behavior
--------------------------------------------------
The video is funny because it captures a baby wearing glasses and pretending to read a book, which is an adorable and endearing sight. The baby’s serious expression and focused demeanor while “reading” the book, combined with the fact that they are clearly not actually reading, creates a humorous contrast. The baby’s movements,
--------------------------------------------------
4.48s
```

Output (with CG):

```bash
--------------------------------------------------
-- generate time = 3.429131507873535
--------------------------------------------------
--------------------------------------------------
The video is funny because it captures a baby wearing glasses and pretending to read a book, which is an adorable and endearing sight. The baby’s serious expression and focused demeanor while pretending to read, combined with the fact that they are so young and unable to actually read, creates a humorous contrast. The baby’s movements
--------------------------------------------------
The video is funny because it captures a baby wearing glasses and pretending to read a book, which is an adorable and endearing sight. The baby's serious expression and focused posture, combined with the fact that they are clearly not reading in the traditional sense, create a humorous contrast. The baby's attempts to turn the pages
--------------------------------------------------
The video is funny because it captures a toddler wearing glasses and pretending to read a book, which is an adorable and endearing sight. The child's focused expression and the way they turn the pages with their hands, as if they are truly engrossed in the book, adds to the humor. The fact that the
--------------------------------------------------
The video is funny because it captures a baby wearing glasses and pretending to read a book, which is an adorable and endearing sight. The baby's serious demeanor and focused expression while holding the book add to the humor, as it creates a comical contrast between the baby's innocent actions and the adult-like behavior of reading
--------------------------------------------------
4.04s
```

Online serving:

```bash

```

```bash
cd ~/github/vllm/
source .venv/bin/activate
export VLLM_USE_MODELSCOPE=False
export HF_ENDPOINT="https://hf-mirror.com"
export HF_DATASETS_CACHE=/shared/sss/datasets

vllm bench mm-processor \
--model /shared/models/modelscope/models/Qwen/Qwen3-VL-8B-Instruct \
--max-model-len 16384 \
--mm-encoder-attn-backend FLASH_ATTN \
--dataset-name hf \
--dataset-path lmarena-ai/vision-arena-bench-v0.1 \
--num-prompts 100 \

--dataset-subset Biology \
--dataset-split test \

--compilation-config '{"cudagraph_mm_encoder": true, "encoder_cudagraph_token_budgets": [128, 256, 512, 1024, 1536, 2048, 2560, 3072, 3584, 4096], "encoder_cudagraph_max_mm_items_per_batch": 4, "encoder_cudagraph_max_frames_per_batch": 32}'

--dataset-name random-mm \
--random-mm-base-items-per-request 1 \
--random-mm-num-mm-items-range-ratio 0.0 \
--random-mm-bucket-config '{(224, 224, 8): 1.0}' \
--random-mm-limit-mm-per-prompt '{"image": 0, "video": 1}' \
--seed 42 \
```

---

Before this PR:

```bash
# No CG
```

After this PR:

```bash
# No CG
```

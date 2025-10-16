# Multi-Modal Tasks

## 📌 Roadmap

**2025 Q4:**

- [ ] [P0] Fill the supporting matrix for mainstream multi-modal models
  - [ ] `Qwen/Qwen2-VL-7B`
  - [ ] `Qwen/Qwen2.5-VL-7B-Instruct`
  - [ ] `Qwen/Qwen2-Audio-7B`
  - [ ] `Qwen/Qwen3-VL-8B-Instruct`
  - [ ] `Qwen/Qwen3-VL-30B-A3B-Instruct`
  - [ ] `OpenGVLab/InternVL2-8B`
  - [ ] `OpenGVLab/InternVL2_5-8B`
  - [ ] `OpenGVLab/InternVL3_5-8B`
- [ ] [P0] Refactor vllm multi-modal models and make some backends pluggable
  - [ ] Make ViT attn backend pluggable: https://github.com/vllm-project/vllm-ascend/pull/3496
  - [ ] Make ViT rope op pluggable
  - [ ] Refactor ViT model for SP
  - [ ] Make patch merge layer pluggable
- [ ] [P1] Resolve community multi-modal related [issues](https://github.com/vllm-project/vllm-ascend/issues?q=is%3Aissue%20state%3Aopen%20VL) (long-term)
- [ ] [P2] Upstream contribution, find more details at vllm [multi-modal core tasks](https://github.com/orgs/vllm-project/projects/8/views/4) (planned)

## 🚀 Supporting Matrix for Mainstream Multi-Modal Models

| Model | Eager | Graph | Doc | CI | Accuracy | Performance | Known Issues |
|:------|:------|:------|:----|:---|:---------|:------------|:-------------|
| `Qwen/Qwen2-VL-7B` | | | | | | | |
| `Qwen/Qwen2.5-VL-7B-Instruct` | | | | | | | |
| `Qwen/Qwen2-Audio-7B` | | | | | | | |
| `Qwen/Qwen3-VL-8B-Instruct` | | | | | | | |
| `Qwen/Qwen3-VL-30B-A3B-Instruct` | | | | | | | |
| `OpenGVLab/InternVL2-8B` | | | | | | | |
| `OpenGVLab/InternVL2_5-8B` | | | | | | | |
| `OpenGVLab/InternVL3_5-8B` | | | | | | | |

## 🛠️ Known Issues

- [ ] https://github.com/vllm-project/vllm-ascend/issues/3493
- [ ] https://github.com/vllm-project/vllm-ascend/issues/3468
- [ ] https://github.com/vllm-project/vllm-ascend/issues/3452
- [ ] https://github.com/vllm-project/vllm-ascend/issues/3395
- [ ] https://github.com/vllm-project/vllm-ascend/issues/3377
- [ ] https://github.com/vllm-project/vllm-ascend/issues/3367
- [ ] https://github.com/vllm-project/vllm-ascend/issues/3286
- [ ] https://github.com/vllm-project/vllm-ascend/issues/3198
- [ ] https://github.com/vllm-project/vllm-ascend/issues/2997
- [ ] https://github.com/vllm-project/vllm-ascend/issues/2965
- [ ] https://github.com/vllm-project/vllm-ascend/issues/2948
- [ ] https://github.com/vllm-project/vllm-ascend/issues/2891
- [ ] https://github.com/vllm-project/vllm-ascend/issues/2267
- [ ] https://github.com/vllm-project/vllm-ascend/issues/1155
- [ ] https://github.com/vllm-project/vllm-ascend/issues/968
- [ ] https://github.com/vllm-project/vllm-ascend/issues/216

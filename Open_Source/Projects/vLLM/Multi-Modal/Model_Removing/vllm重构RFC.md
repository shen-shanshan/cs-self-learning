1.FA backend 基类抽象 - 杉杉
2.PAD 移动到 forward 性能收益  - 少鹏
2.卷积替换为matmal，PatchEmbed backend 基类抽象 - 杉杉
3.vllm 为什么要在 split_qkv 中加入 TP 逻辑  - 少鹏
4.cos sin提前计算，硬件无关，直接提交社区 / Rope backend 基类抽象  - 灿林
2.RL场景Qwen2.5-VL without pad 模型处理

---

# RFC: Removing Qwen2.5-VL Modeling Files

## Motivation

To avoid maintaining a variety of models, we propose to remove all modeling files in vllm-ascend. To reach this, there are some refactors need to be done for multi-modal models in both vllm and vllm-ascend.

## Roadmap

vllm:

- [ ] Extract ViT backend base class @shen-shanshan
- [ ] Extract PatchEmbed layer base class @shen-shanshan
- [ ] Remove TP logic in split_qkv @gcanlin https://github.com/vllm-project/vllm/pull/28271
- [ ] Refactor `Qwen2_5_VisionRotaryEmbedding` and `Qwen2VisionRotaryEmbedding` to use `RotaryEmbedding` as their base class @gcanlin

vllm-ascend:

- [ ] Implement ascend ViT backend and related interface in platform @shen-shanshan
- [ ] Move FA padding logic into ViT forward @shen-shanshan
- [ ] Implement ascend PatchEmbed custom op and register it @shen-shanshan

## Other Related

- [ ] Make mamba backend pluggable @shen-shanshan https://github.com/vllm-project/vllm/pull/26487

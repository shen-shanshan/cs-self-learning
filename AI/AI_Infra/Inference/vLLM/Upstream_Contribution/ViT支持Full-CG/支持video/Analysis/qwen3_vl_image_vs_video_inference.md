# Qwen3-VL：图像推理与视频推理的区别

## 1. 输入预处理

### 图像
- 输入为静态图片，经过 `image_processor` 处理
- 使用 `Conv3dLayer`（temporal_patch_size=2）对图片做 patch 嵌入，但 t=1（单帧）
- 输出 `pixel_values`（shape: `[num_patches, flattened_patch_size]`）
- grid 信息保存在 `image_grid_thw`，每条记录为 `[1, H, W]`

### 视频
- 输入为视频帧序列，经过 `video_processor` 处理
- 先均匀采样若干帧（如 16 帧），再按 `temporal_patch_size=2` 分组打包成时序 patch
- 输出 `pixel_values_videos`（shape: `[num_patches, flattened_patch_size]`）
- grid 信息保存在 `video_grid_thw`，每条记录为 `[T, H, W]`，T≥1 表示时序维度

---

## 2. 输入格式差异

| 维度 | 图像 | 视频 |
|------|------|------|
| pixel values key | `pixel_values` | `pixel_values_videos` |
| grid key | `image_grid_thw` | `video_grid_thw` |
| t 值 | 固定为 1 | ≥1（取决于帧数/temporal_patch_size） |
| 时序分组 | 无 | 每 `temporal_patch_size=2` 帧合并为 1 个时序 patch |
| cu_seqlens 大小 | = 图片数 | = 所有视频的 sum(t)（总时序帧数） |

---

## 3. ViT Encoder 差异

### 图像
- `Qwen3_VisionTransformer` 接收 `[num_patches, flat]` 的 pixel_values
- `cu_seqlens` 按图片边界划分，长度 = 图片数
- 每个 Transformer block 中的 attention 是 **图片内** 局部计算
- spatial merge：`spatial_merge_size=2`，输出 tokens = `sum(T*(H//m)*(W//m))`，T=1

### 视频
- 同样使用 `Qwen3_VisionTransformer`，但输入为多帧 patch
- `cu_seqlens` 按时序帧边界划分，长度 = `sum(t for each video)`
- 每个视频的所有时序帧在 ViT 内 **一起** 做 attention（3D 时序建模）
- spatial merge 后输出 tokens = `sum(T*(H//m)*(W//m))`，T>1

---

## 4. MRoPE（多模态旋转位置编码）差异

- 图像：位置编码维度为 `[t=1, h, w]`，t 维度贡献极少
- 视频：位置编码维度为 `[t, h, w]`，t 维度携带时序信息
- 视频的 MRoPE 中，时序位置（帧索引）对模型理解时间顺序至关重要
- LLM 侧的 `mrope_section` 将 t/h/w 三个维度的 rope 分配到 head dim 的不同区间

---

## 5. Prompt Token 结构差异

```
图像：
<|vision_start|> [image tokens] <|vision_end|>

视频：
<|vision_start|> [video tokens (多帧合并)] <|vision_end|>
```

- 图像 token 数 = `1*(H//m)*(W//m)`（单帧）
- 视频 token 数 = `T*(H//m)*(W//m)`（所有时序帧的 spatial tokens 拼接）
- 视频 token 数通常远多于图像，显存和计算开销更高

---

## 6. EVS（高效视觉采样）差异

- EVS 仅用于**视频**推理，不适用于图像
- 视频帧较多时，相邻帧内容高度相似，EVS 对冗余帧进行剪枝
- 剪枝后的视频 token 数减少，降低 LLM 侧的 prefill 计算量
- 图像没有时序冗余，无需剪枝

---

## 7. 总结对比

| 维度 | 图像 | 视频 |
|------|------|------|
| 时序建模 | 无（t=1） | 有（t ≥ 1，帧间关系建模） |
| cu_seqlens 大小 | = 图片数 | = 总时序帧数（更大） |
| MRoPE 时序维度 | 基本不携带信息 | 携带帧索引时序信息 |
| EVS 剪枝 | 不支持 | 支持（减少冗余视频 token） |
| ViT 计算量 | 低（t=1） | 高（t>1，随帧数线性增长） |
| LLM 侧 token 数 | 少 | 多（随 T 增大） |
| pixel values key | `pixel_values` | `pixel_values_videos` |
| grid thw key | `image_grid_thw` | `video_grid_thw` |

# Step3-VL 支持 Dual-Path Graph

## 背景知识

Step3-VL 采用智能的多尺度滑动窗口策略：

- **方形图** (ratio < 1.5)：仅使用全局 728×728 视图
- **细长图** (ratio ≥ 4)：窗口尺寸取 min(504, short_side)
- **中等长图** (1.5 ≤ ratio < 4)：窗口尺寸固定 504

滑动窗口使用多种 (size, step) 组合覆盖不同区域，确保不丢失细节信息。

核心逻辑在 `ImagePatcher.determine_window_size()` 方法中（`step3_vl.py` processor，第 70-73 行）：

```python
def determine_window_size(self, long: int, short: int) -> int:
    if long < 728:
        return short if long / short > 1.5 else 0
    return min(short, 504) if long / short > 4 else 504
```

其中 long = `max(width, height)`, short = `min(width, height)`。

不包含 local patch（window_size = 0）

当 长边 < 728 且 长边/短边 ≤ 1.5，即图像较小且大致为正方形时，不生成任何 patch。

例如：600×500（比值 1.2）、400×400（比值 1.0）、700×500（比值 1.4，但长边 < 728）。

包含 local patch（window_size > 0）

其余所有情况都会生成 patch，patch 的窗口大小取决于尺寸：

┌─────────────────────────┬────────────────┬───────────────────────────────────────┐
│          条件           │  window_size   │                 说明                  │
├─────────────────────────┼────────────────┼───────────────────────────────────────┤
│ 长边 ≥ 728，长/短 ≤ 4   │ 504            │ 最常见情况，如 728×728、1920×1080     │
├─────────────────────────┼────────────────┼───────────────────────────────────────┤
│ 长边 ≥ 728，长/短 > 4   │ min(短边, 504) │ 极端细长图，窗口取短边和 504 的较小值 │
├─────────────────────────┼────────────────┼───────────────────────────────────────┤
│ 长边 < 728，长/短 > 1.5 │ 短边           │ 小但细长的图，如 700×300（比值 2.3）  │
└─────────────────────────┴────────────────┴───────────────────────────────────────┘

Patch 数量

确定 window_size 后，图像会先被 pad 到 window_size 的整数倍（get_image_size_for_crop），然后用不重叠的 window_size×window_size 滑窗覆盖整个图像。patch 数量 ≈ ceil(width_padded / ws) × ceil(height_padded / ws)（最后一行如果是完整行会减去一行）。

之后每个 patch 被 resize 到 504×504，送入 ViT 处理。

## Prompt

背景：
https://github.com/vllm-project/vllm/pull/42224 为vllm中的step3-vl模型支持了vit cuda graph特性；
https://github.com/vllm-project/vllm/pull/43586 进一步扩展了vllm中的vit cuda graph框架，支持了dual-path cuda graph，使global image和local patch可以独立地做图捕获与重放，提高了buffer计算的效率。

需求：
1.请参考deepseek-ocr dual-path vit cuda graph的实现，为step3-vl也支持dual-path graph，而不采用原本的single-path graph；
2.你只需要做设计和核心代码实现，不需要更新对应的文档和测试用例。

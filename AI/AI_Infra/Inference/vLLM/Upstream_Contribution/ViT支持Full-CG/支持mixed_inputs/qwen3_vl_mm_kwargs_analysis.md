# Qwen3-VL `mm_kwargs` 多模态输入分析报告

> 分析文件：`vllm/model_executor/models/qwen3_vl.py`

---

## 核心结论

**`mm_kwargs` 不是单个视觉输入（单张图片或单个视频）的表示，而是请求级别（request-level）的聚合结构**，包含该请求中所有图像和/或视频的数据。

`mm_kwargs` 在 Qwen3-VL 中有**两种截然不同的含义**，出现在不同的调用层次：

1. **处理时（Processing-time）mm_kwargs**：用户传入的"控制参数"，指定如何对媒体数据进行预处理（分辨率、帧率等）。
2. **前向推理时（Forward-time）mm_kwargs**：经过处理后传入模型 forward 的"张量数据"，包含该请求所有图像/视频的 pixel values 和 grid 信息。

---

## 一、处理时 mm_kwargs（用户侧配置参数）

出现在 `_get_vision_info`、`_call_hf_processor`、`get_max_video_tokens` 等处理阶段，由用户在发起请求时传入，用于覆盖处理器的默认配置。

### 1.1 图像输入

| 参数键 | 类型 | 说明 |
|--------|------|------|
| `size` | `dict` | 覆盖 image_processor 的 size，格式为 `{"shortest_edge": int, "longest_edge": int}` |
| `min_pixels` | `int` | 覆盖最小像素数，等效于 `size["shortest_edge"]` |
| `max_pixels` | `int` | 覆盖最大像素数，等效于 `size["longest_edge"]` |

代码依据（`_get_vision_info` 第 884–891 行）：

```python
mm_kwargs = self.ctx.get_merged_mm_kwargs(mm_kwargs)
size = image_processor.size
if override_size := mm_kwargs.get("size"):
    size = size | override_size
if (override_min_pixels := mm_kwargs.get("min_pixels")) is not None:
    size = size | {"shortest_edge": override_min_pixels}
if (override_max_pixels := mm_kwargs.get("max_pixels")) is not None:
    size = size | {"longest_edge": override_max_pixels}
```

### 1.2 视频输入（在图像参数基础上额外支持）

| 参数键 | 类型 | 说明 |
|--------|------|------|
| `fps` | `float` | 视频采样帧率（与 `num_frames` 互斥） |
| `num_frames` | `int` | 显式指定采样帧数（与 `fps` 互斥） |
| `do_sample_frames` | `bool` | 是否在 HF Processor 内部采样帧；`False` 表示帧已由 vLLM 预采样 |
| `temporal_patch_size` | `int` | 覆盖时间维度 patch 大小 |

**注意**：`fps` 和 `num_frames` 互斥。当指定 `num_frames` 时，代码会强制将 `fps` 设为 `None`（第 1240–1241 行）：

```python
if "num_frames" in video_mm_kwargs and "fps" not in video_mm_kwargs:
    video_mm_kwargs["fps"] = None
```

---

## 二、前向推理时 mm_kwargs（模型输入张量）

出现在模型的 `forward`、`embed_multimodal`、`encoder_cudagraph_forward` 等方法中，是经过完整预处理后的**张量字典**，直接输入视觉编码器。

**关键特性：这是请求级别的聚合结构，而非单个媒体项的结构。**

### 2.1 仅有图像时（N 张图片，N ≥ 1）

所有图片的 patch 在 dim=0 顺序**拼接**成一个张量，`image_grid_thw` 有每张图片一行：

```python
{
    "pixel_values": Tensor,
    # shape: [sum_i(T_i * H_i * W_i), C * temporal_patch_size * patch_size^2]
    # 所有图片的 patch 在 dim=0 顺序拼接
    # T_i=1（图片只有 1 帧），H_i=height_i/patch_size，W_i=width_i/patch_size

    "image_grid_thw": Tensor,
    # shape: [N, 3]
    # 每行为一张图片的 3D grid：[T_i, H_i, W_i]
    # 例：N=3 张图 grid 分别为 [1,32,32]、[1,16,16]、[1,24,24] 时：
    # [[1, 32, 32],
    #  [1, 16, 16],
    #  [1, 24, 24]]
}
```

**具体示例（3 张图片）：**

```python
# 图 1: 448×448 → grid=[1,32,32] → 1024 patches
# 图 2: 224×224 → grid=[1,16,16] → 256 patches
# 图 3: 336×336 → grid=[1,24,24] → 576 patches

{
    "pixel_values": Tensor,      # shape: [1856, patch_dim]  (1024+256+576)
    "image_grid_thw": Tensor,    # shape: [3, 3]
    #                              [[1, 32, 32],
    #                               [1, 16, 16],
    #                               [1, 24, 24]]
}
```

模型通过 `image_grid_thw` 计算每张图的 patch 数，再用 `split()` 拆分（第 1975–1978 行）：

```python
# Split concatenated embeddings for each image item.
merge_size = self.visual.spatial_merge_size
sizes = (grid_thw.prod(-1) // merge_size // merge_size).tolist()
return image_embeds.split(sizes)
```

### 2.2 仅有视频时（N 个视频，N ≥ 1）

```python
{
    "pixel_values_videos": Tensor,
    # shape: [sum_i(T_i * H_i * W_i), C * temporal_patch_size * patch_size^2]
    # 所有视频的 patch 在 dim=0 顺序拼接
    # T_i = max(round_up(num_frames_i, temporal_patch_size) // temporal_patch_size, 1)

    "video_grid_thw": Tensor,
    # shape: [N, 3]
    # 每行为一个视频的 3D grid：[T_i, H_i, W_i]

    "timestamps": list[list[float]],
    # 外层 list 长度 = N（每个视频一个子列表）
    # timestamps[i] 长度 = T_i（该视频的时间步数）
    # timestamps[i][j] 表示第 i 个视频第 j 个时间步的中心时间（秒）
}
```

**具体示例（2 个视频）：**

```python
# 视频 1: 8帧, 224×224 → T=4, H=16, W=16 → 1024 patches，4 个时间戳
# 视频 2: 4帧, 224×224 → T=2, H=16, W=16 → 512 patches，2 个时间戳

{
    "pixel_values_videos": Tensor,  # shape: [1536, patch_dim]  (1024+512)
    "video_grid_thw": Tensor,       # shape: [2, 3]
    #                                 [[4, 16, 16],
    #                                  [2, 16, 16]]
    "timestamps": [
        [0.25, 0.75, 1.25, 1.75],  # 视频 1 的 4 个时间戳（秒）
        [0.25, 0.75],              # 视频 2 的 2 个时间戳（秒）
    ],
}
```

### 2.3 同一请求中既有图像又有视频时（M 张图片 + N 个视频）

**图像字段和视频字段同时存在，共 5 个 key，互不干扰：**

```python
{
    # ── 图像部分（M 张图片）────────────────────────────────────────────
    "pixel_values": Tensor,
    # shape: [sum_i(T_i*H_i*W_i), patch_dim]，M 张图片的 patch 拼接

    "image_grid_thw": Tensor,
    # shape: [M, 3]，每行为一张图片的 [T, H, W]

    # ── 视频部分（N 个视频）────────────────────────────────────────────
    "pixel_values_videos": Tensor,
    # shape: [sum_j(T_j*H_j*W_j), patch_dim]，N 个视频的 patch 拼接

    "video_grid_thw": Tensor,
    # shape: [N, 3]，每行为一个视频的 [T, H, W]

    "timestamps": list[list[float]],
    # 长度为 N，timestamps[j] 长度为 T_j
}
```

这一结构在 `_call_hf_processor`（第 1302–1320 行）中明确构建：视频和图像分开处理后合并进同一个 `BatchFeature`：

```python
video_outputs = dict(
    pixel_values_videos=torch.cat(pixel_values_videos_lst),  # 所有视频拼接
    video_grid_thw=torch.cat(video_grid_thw_lst),
    timestamps=timestamps_per_video,
)
processed_outputs = super()._call_hf_processor(...)  # 图像部分
combined_outputs = dict(processed_outputs, **video_outputs)  # 合并
```

### 2.4 预计算 Embedding 路径（可选替代形式）

若用户预先提供 embedding，可用 `image_embeds`/`video_embeds` 替换对应的 `pixel_values`：

```python
# 图像 embedding 替代形式
{
    "image_embeds": Tensor,      # shape: [sum(T_i*H_i*W_i // merge^2), hidden_size]
    "image_grid_thw": Tensor,    # shape: [N, 3]，与像素值路径相同
}

# 视频 embedding 替代形式
{
    "video_embeds": Tensor,      # shape: [sum(T_i*H_i*W_i // merge^2), hidden_size]
    "video_grid_thw": Tensor,    # shape: [N, 3]
    "timestamps": list[list[float]],
}
```

---

## 三、各字段的拼接规则

由 `_create_qwen2vl_field_factory`（`qwen2_vl.py` 第 723–760 行）定义，描述各字段如何存储以及如何按媒体条目拆分：

| 字段 | 所属模态 | 存储类型 | 含义 |
|------|----------|----------|------|
| `pixel_values` | image | `flat_from_sizes` | 所有图片 patch 在 dim=0 拼接；按 `T_i*H_i*W_i` 切分还原 |
| `image_embeds` | image | `flat_from_sizes` | 所有图片 embedding 拼接；按 `T_i*H_i*W_i // merge^2` 切分 |
| `image_grid_thw` | image | `batched`（CPU） | 每张图一行 `[T,H,W]`，stack 成 `[N_img, 3]` |
| `pixel_values_videos` | video | `flat_from_sizes` | 所有视频 patch 在 dim=0 拼接；按 `T_j*H_j*W_j` 切分还原 |
| `video_embeds` | video | `flat_from_sizes` | 所有视频 embedding 拼接 |
| `video_grid_thw` | video | `batched`（CPU） | 每个视频一行 `[T,H,W]`，stack 成 `[N_vid, 3]` |
| `timestamps` | video | `batched`（CPU） | 每个视频一个时间戳列表，外层 list 长度 = N_vid |

---

## 四、Encoder CUDA Graph 场景（仅图像）

CUDA Graph 路径只处理图像（视频走 eager 路径），`mm_kwargs` 结构与"仅图像"情形相同（第 1860–1863 行）：

```python
mm_kwargs = {
    "pixel_values": Tensor,        # shape: [total_patches, patch_dim]
    "image_grid_thw": list[list[int]],  # 每个元素为 [T, H, W]
}
```

相关方法利用 `image_grid_thw` 做 per-image 计算：

```python
# 图像数量（第 1769 行）
def get_encoder_cudagraph_num_items(self, mm_kwargs):
    return len(mm_kwargs["image_grid_thw"])

# 每张图的输出 token 数（第 1776 行）
def get_encoder_cudagraph_per_item_output_tokens(self, mm_kwargs):
    m = self.visual.spatial_merge_size
    return [t * (h // m) * (w // m) for t, h, w in mm_kwargs["image_grid_thw"]]

# 每张图的输入 patch 数（第 1782 行）
def get_encoder_cudagraph_per_item_input_sizes(self, mm_kwargs):
    return [t * h * w for t, h, w in mm_kwargs["image_grid_thw"]]
```

---

## 五、关键维度说明

模型默认配置（来自 `Qwen3VLVisionConfig`）：

- `patch_size = 14`
- `temporal_patch_size = 2`
- `spatial_merge_size = 2`

**图像（448×448）：**

```
grid_thw = [1, 32, 32]
pixel_values 片段 shape = [1*32*32, 3*2*14*14] = [1024, 1176]
LLM token 数 = 1*32*32 / (2*2) = 256
```

**视频（224×224，8帧）：**

```
padded_frames = round_up(8, 2) = 8
grid_thw = [4, 16, 16]   （T = 8/2 = 4）
pixel_values_videos 片段 shape = [4*16*16, 3*2*14*14] = [1024, 1176]
LLM token 数 = 4*16*16 / (2*2) = 256
timestamps 长度 = 4（每个 T 步一个时间戳）
```

---

## 六、数据流总结

```
用户请求（含 M 张图片 + N 个视频）
    │
    │  处理时 mm_kwargs（配置参数，对所有媒体生效）
    │  ├── 图像: {min_pixels, max_pixels, size}
    │  └── 视频: {fps | num_frames, do_sample_frames, temporal_patch_size, ...}
    │
    ▼
Qwen3VLMultiModalProcessor._call_hf_processor()
    │  ├── 对每个视频分别调用 HF Processor → 收集 pixel_values_videos, video_grid_thw
    │  └── 对所有图像调用 HF Processor → 获取 pixel_values, image_grid_thw
    │
    │  BatchFeature（单次请求，所有媒体聚合后的输出）
    │  ├── pixel_values:         [sum_img patches, patch_dim]  ← M 张图拼接
    │  ├── image_grid_thw:       [M, 3]
    │  ├── pixel_values_videos:  [sum_vid patches, patch_dim]  ← N 个视频拼接
    │  ├── video_grid_thw:       [N, 3]
    │  └── timestamps:           list of N lists
    │
    ▼
_get_mm_fields_config() → MultiModalKwargsItems
    （记录如何按 image_grid_thw / video_grid_thw 拆分回 per-item）
    │
    ▼
Qwen3VLForConditionalGeneration.embed_multimodal(**mm_kwargs)
    │  前向推理时 mm_kwargs（聚合张量）：上述 5 个 key
    │
    ├── _process_image_input()
    │     visual(pixel_values, grid_thw=image_grid_thw)  → embeds
    │     embeds.split(sizes)  → M 个 per-image embedding tuple
    │
    └── _process_video_input()
          visual(pixel_values_videos, grid_thw=video_grid_thw)  → embeds
          embeds.split(sizes)  → N 个 per-video embedding tuple
                │
                ▼
         （EVS 视频剪枝、timestamp 注入）
                │
                ▼
         语言模型（LLM）forward()
```

---

*生成日期：2026-04-13*
*分析文件：`vllm/model_executor/models/qwen3_vl.py`（共 2750 行）*

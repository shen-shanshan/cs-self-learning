# Qwen2.5-VisionTransformer 中 RoPE cos/sin 的计算流程

整个流程从 `forward(x, grid_thw)` 开始，分为以下几个阶段：

---

## 第一步：初始化时预计算 cos/sin 缓存

[qwen2_5_vl.py:608-612](vllm/model_executor/models/qwen2_5_vl.py#L608-L612)

```python
self.rotary_pos_emb = get_rope(
    head_size=head_dim,
    max_position=8192,
    is_neox_style=True,
    rope_parameters={"partial_rotary_factor": 0.5},
)
```

`get_rope` 最终创建一个 `RotaryEmbedding` 对象，关键参数：
- `rotary_dim = head_dim * 0.5` → 只使用一半的 head 维度做旋转
- 在 `_compute_cos_sin_cache` 中([base.py:83-92](vllm/model_executor/layers/rotary_embedding/base.py#L83-L92))：

```python
# inv_freq 形状: [rotary_dim // 2]
inv_freq = 1.0 / (base ** (arange(0, rotary_dim, 2) / rotary_dim))
# t = [0, 1, 2, ..., 8191]
freqs = einsum("i,j -> ij", t, inv_freq)   # [8192, rotary_dim//2]
cache = cat(freqs.cos(), freqs.sin(), dim=-1)  # [8192, rotary_dim]
```

**原理**：标准 RoPE 公式 θ_i = base^(-2i/d)，每个 head 维度对应一个旋转频率。

---

## 第二步：为每张图像/视频计算 2D 空间位置 ID

[qwen2_5_vl.py:654-697](vllm/model_executor/models/qwen2_5_vl.py#L654-L697) `rotary_pos_emb_thw(t, h, w)`

**输入**：patch grid 的时空尺寸 `(t, h, w)`，单位是 patch 数量

```python
# 每个 patch 的行号 (H轴位置): shape [h, w]
hpos_ids = torch.arange(h).unsqueeze(1).expand(-1, w)

# 每个 patch 的列号 (W轴位置): shape [h, w]
wpos_ids = torch.arange(w).unsqueeze(0).expand(h, -1)
```

然后做 **spatial merge 分组重排**（为了使相邻的 merge unit 在序列中连续）：

```python
hpos_ids = hpos_ids.reshape(
    h // spatial_merge_size,
    spatial_merge_size,
    w // spatial_merge_size,
    spatial_merge_size,
).permute(0, 2, 1, 3).flatten()
# 原理: 将 [h,w] 的 patch 按 merge unit(s×s) 分组，
#       使同一个 merge unit 内的 patch 在展平后连续
```

**原理**：`spatial_merge_size=2` 意味着相邻的 2×2=4 个 patch 最终会被合并为一个 LLM token。将它们在序列中排列连续，便于后续 PatchMerger 操作。

接着将 t 帧的位置全部拼接：

```python
pos_ids = torch.stack([hpos_ids, wpos_ids], dim=-1).repeat(t, 1)
# shape: [t * h * w, 2]
# 每行是 (H位置, W位置)
```

---

## 第三步：从 cos/sin 缓存中查表，分别编码 H 和 W

```python
cos, sin = self.rotary_pos_emb.get_cos_sin(max_size)
# max_size = max(h, w)
# cos/sin 各: [max_size, rotary_dim//2]
# get_cos_sin 就是把 cos_sin_cache 切成两半返回

cos_combined = cos[pos_ids].flatten(1)
# cos[pos_ids] 高级索引:
#   pos_ids: [t*h*w, 2]  → 为每个 patch 同时查 H 和 W 的 cos 值
#   结果:    [t*h*w, 2, rotary_dim//2]
#   flatten: [t*h*w, rotary_dim]
```

**这是 2D-RoPE 的核心**：
- `cos_combined` 的前 `rotary_dim//2` 维 = H 位置的旋转角余弦
- `cos_combined` 的后 `rotary_dim//2` 维 = W 位置的旋转角余弦

即用 H 和 W 各自独立的位置频率拼接成完整的旋转嵌入。

---

## 第四步：按 spatial merge unit 分组重塑

```python
cos_combined = cos_combined.reshape(
    cos_combined.shape[0] // spatial_merge_unit,   # t*(h/s)*(w/s) 个 merge group
    spatial_merge_unit,                             # 每组 s^2 个 patch
    rotary_dim                                      # 每个 patch 的旋转维度
)
# shape: [t*(h//s)*(w//s), s^2, rotary_dim]
```

---

## 第五步：按窗口注意力顺序重排（`get_rope_by_thw`）

[qwen2_5_vl.py:736-755](vllm/model_executor/models/qwen2_5_vl.py#L736-L755)

```python
window_index_thw, cu_seqlens_window_thw = self.get_window_index_thw(t, h, w)
cos_thw, sin_thw = self.rotary_pos_emb_thw(t, h, w)

# 按窗口注意力索引重排 merge group
cos_thw = cos_thw[window_index_thw, :, :]  # [N_groups_reordered, s^2, rotary_dim]
cos_thw = cos_thw.flatten(start_dim=0, end_dim=1)  # [t*h*w, rotary_dim]
```

**原理**：Vision Transformer 对大多数 block 使用局部窗口注意力（仅少数 fullatt_block 使用全局注意力），`get_window_index_thw` 将属于同一个窗口的 patch group 排列到连续的序列位置，使窗口注意力只需要连续的 `cu_seqlens` 划分即可。

---

## 第六步：多图/多视频拼接，搬到 GPU

[qwen2_5_vl.py:784-843](vllm/model_executor/models/qwen2_5_vl.py#L784-L843)

```python
for t, h, w in grid_thw:
    cos_thw, sin_thw, ... = self.get_rope_by_thw(t, h, w)
    rotary_pos_emb_cos.append(cos_thw)  # 每张图的 [t*h*w, rotary_dim]

rotary_pos_emb_cos = torch.cat(rotary_pos_emb_cos)  # [total_patches_all, rotary_dim]
rotary_pos_emb_cos = rotary_pos_emb_cos.to(device=self.device)
```

---

## 第七步：在每个 VisionBlock 中将 RoPE 应用到 Q/K

[qwen2_5_vl.py:380-399](vllm/model_executor/models/qwen2_5_vl.py#L380-L399)，[common.py:166-178](vllm/model_executor/layers/rotary_embedding/common.py#L166-L178)

```python
# is_neox_style=True: 把 head_dim 拆成前后两半
x1, x2 = torch.chunk(qk, 2, dim=-1)
# x1, x2: [..., head_dim//2]

o1 = x1 * cos - x2 * sin   # 旋转变换
o2 = x2 * cos + x1 * sin

output = torch.cat([o1, o2], dim=-1)  # [..., head_dim]
```

**这就是 2D 旋转矩阵**：对特征对 `(x[i], x[i + head_dim//2])` 施加角度为 `θ_i(pos)` 的平面旋转：
- 前 `rotary_dim//4` 个特征对 → 按 H 轴位置旋转
- 后 `rotary_dim//4` 个特征对 → 按 W 轴位置旋转
- 剩余 `head_dim - rotary_dim` 个特征对 → 旋转角为 0（恒等变换）

---

## 整体流程图

```
输入: pixel_values + grid_thw[(t,h,w), ...]
        │
        ▼
[初始化] 预计算 cos/sin 缓存
  inv_freq[i] = base^(-2i/rotary_dim)
  cache[pos, i] = cos(pos * inv_freq[i])  ── [8192, rotary_dim]
        │
        ▼ 对每张图像/视频
[rotary_pos_emb_thw]
  为每个 patch 生成 (H位置, W位置)
  从缓存中查: cos[H位置] + cos[W位置]
  → cos_combined: [t*h*w, rotary_dim]
    前半 = H方向旋转角余弦
    后半 = W方向旋转角余弦
        │
        ▼
[get_rope_by_thw]
  按窗口注意力顺序重排 patch
  → cos_thw: [t*h*w, rotary_dim]
        │
        ▼
[forward] 多图拼接
  → rotary_pos_emb_cos: [total_patches, rotary_dim]
  → rotary_pos_emb_sin: [total_patches, rotary_dim]
        │
        ▼ 每个 VisionBlock
[ApplyRotaryEmb]
  Q/K 每对特征 (x_i, x_{i+d/2}) 按对应位置的角度旋转
  → 位置信息编码到注意力相似度中
```

---

## 设计精髓

Qwen2.5-VL 使用 **2D-RoPE**（而非 1D 序列位置），将 H 和 W 两个空间轴的位置信息分别编码到 head 维度的不同频率组中，让 attention 的点积相似度自然地感知二维空间相对位置关系，同时通过 `partial_rotary_factor=0.5` 将 rotary 维度限制在 head_dim 的一半，节省了频率资源，使 H 和 W 各分配到 `rotary_dim//4` 个独立的旋转频率。

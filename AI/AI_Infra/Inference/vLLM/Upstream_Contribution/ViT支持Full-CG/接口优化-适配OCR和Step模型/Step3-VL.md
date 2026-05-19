> Q：请详细介绍下Step3-VL的分辨率策略（Multi-crop strategy consisting of a 728×728 global view and multiple 504×504 local crops），要求有ViT部分的处理流程图。

Step3-VL（STEP3-VL-10B）的这套分辨率策略：

> **Multi-crop strategy consisting of a 728×728 global view and multiple 504×504 local crops**

是它视觉编码（ViT）部分的核心设计之一，主要目的是：

在**不把整个输入图像无脑拉高分辨率**（否则 token 数暴涨）的情况下，同时保留：

1. **全局语义理解（global context）**

   * 图片整体布局
   * 页面结构
   * 目标之间的位置关系

2. **局部细节理解（fine-grained details）**

   * OCR小字
   * 表格
   * 数学公式
   * GUI小icon
   * counting / grounding中的小目标

Step3-VL官方 repo 和 technical report 都明确写了这一点：使用 **728×728 全局图 + 多个 504×504 局部 crop**。([Hugging Face][1])

---

# 一、为什么不用单张高分辨率图？

传统 VLM 如果直接输入：

比如：

* 1024×1024
* 1536×1536

ViT patch 数会急剧上升。

假设 patch size = 14（ViT 常见）

那么 token 数近似：

## 728×728

[
(728/14)^2 = 52^2 = 2704
]

## 1024×1024

[
(1024/14)^2 \approx 73^2 = 5329
]

几乎翻倍。

而 ViT self-attention复杂度是：

O(N^2)

token翻倍，attention计算可能接近 **4倍**。

所以 Step3-VL 的策略是：

> **不要让一张图特别大，而是拆成：**
>
> 一个全局图 + 多个局部图

这样可以：

* 保持 global understanding
* 获得 local detail
* batch 并行计算更友好

论文里明确提到：

> exploits batch-dimension parallelism, avoiding variable-length packing complexity. ([HyperAI][2])

即：

他们把不同 crop 当 batch 维度并行送进 ViT，而不是拼成长序列。

---

# 二、Step3-VL 的 Multi-crop 是怎么切的？

原图（假设任意分辨率）：

```text
Original Image
```

先做两个分支：

---

## 分支1：Global View

缩放到：

```text
728 × 728
```

作用：

保留全局布局：

比如文档：

```text
+----------------------+
| title                |
| image        table   |
| paragraph            |
+----------------------+
```

模型能理解：

* 标题在哪
* 图片在哪
* 表格在哪
* 阅读顺序是什么

---

## 分支2：Local Crops

从原图切多个局部窗口：

每个：

```text
504 × 504
```

比如：

```text
+---------------------------+
| crop1 | crop2 | crop3    |
| crop4 | crop5 | ...      |
+---------------------------+
```

这些 crop 通常覆盖：

* 左上
* 中间
* 右下
* 重叠区域

（具体 crop 数会随原图尺寸变化，推理代码动态决定）

作用：

放大看细节：

比如 OCR：

原图：

```text
Invoice #20250418
```

global里可能太小。

local crop里变成：

```text
Invoice
#20250418
```

ViT 就更容易识别。

---

# 三、ViT处理流程（Step3-VL）

下面是视觉编码完整流程：

---

## Step 1：图像切分

```text
                    Original Image
                           │
        ┌──────────────────┴──────────────────┐
        │                                     │
        ▼                                     ▼
  Global Resize                        Local Crop Generator
   728×728                              multiple 504×504
        │                                     │
        └──────────────┬──────────────────────┘
                       ▼
                Crop Batch Assembly
```

即：

最终形成：

```text
[
 global,
 crop1,
 crop2,
 crop3,
 ...
]
```

这些作为 batch 输入。

---

## Step 2：Patch Embedding

每个 crop 输入 PE-lang ViT：

论文中视觉编码器叫：

**PE-lang (Perception Encoder)**，约 1.8B 参数。([GitHub][3])

流程：

```text
504×504 crop
      │
      ▼
Patch Split
      │
      ▼
Patch Embedding
```

例如：

若 patch size = 14：

## global

```text
728/14 = 52
```

得到：

```text
52×52 patches
```

token 数：

```text
2704 tokens
```

---

## local crop

```text
504/14 = 36
```

得到：

```text
36×36 patches
```

token 数：

```text
1296 tokens
```

---

# 四、ViT Transformer Encoder

每个 crop token进入 Transformer blocks：

```text
Patch Tokens
     │
     ▼
+------------------+
| Transformer #1   |
+------------------+
     │
     ▼
+------------------+
| Transformer #2   |
+------------------+
     │
     ▼
        ...
     │
     ▼
+------------------+
| Transformer #L   |
+------------------+
```

这里做：

* Self-attention
* MLP
* LayerNorm

输出视觉特征：

```text
[T_crop, hidden_dim]
```

---

# 五、Step3-VL 的空间位置编码（很关键）

Step3-VL 没用 fancy 2D position。

而是用了：

## 1D RoPE

并通过插入 newline token 保留二维结构：

论文原文：

> Spatial structure is encoded by appending newline tokens to patch rows. ([HyperAI][2])

例如 patch grid：

原始：

```text
p1 p2 p3
p4 p5 p6
p7 p8 p9
```

展开成：

```text
p1 p2 p3 <NL>
p4 p5 p6 <NL>
p7 p8 p9 <NL>
```

这样 LLM 后续仍知道：

> 哪些 token 属于同一行。

这是 Step3-VL 非常工程化的设计。

---

# 六、多个 crop 怎么融合？

ViT输出：

```text
Global Features
Local Features 1
Local Features 2
Local Features 3
...
```

然后 concat：

```text
                +----------------+
Global -------->|                |
Local1 -------->|                |
Local2 -------->| Concatenate    |
Local3 -------->|                |
                +----------------+
```

得到：

```text
Visual Token Sequence
```

---

# 七、Projector 压缩

然后进入 projector：

Step3-VL 使用：

> two consecutive stride-2 layers

实现：

> 16× spatial downsampling ([GitHub][3])

流程：

```text
Visual Features
      │
      ▼
Conv / Projector (stride=2)
      │
      ▼
Conv / Projector (stride=2)
      │
      ▼
Compressed Visual Tokens
```

目的：

减少 token 数，方便送入 decoder。

---

# 八、最终送给LLM

最终：

```text
                 IMAGE TOKENS
                      │
                      ▼
+----------------------------------+
|      Qwen Decoder (8B)           |
+----------------------------------+
                      │
                      ▼
               Text Generation
```

---

# 九、为什么是 728 和 504？

这两个数字本质上是**token budget + batch效率 + patch grid整除性**的折中。

如果 patch=14：

## 728

[
728 = 52 \times 14
]

## 504

[
504 = 36 \times 14
]

所以：

能完美整除 patch size，不需要 padding。

对应：

* global：52×52
* local：36×36

非常适合 GPU kernel 对齐和 batch 并行。

这通常不是巧合，而是工程优化结果。

---

# 十、和传统单图ViT的区别

| 方法                  | 优点      | 缺点        |
| ------------------- | ------- | --------- |
| 单张高分辨率              | 信息完整    | token爆炸   |
| 下采样单图               | 便宜      | 丢细节       |
| Step3-VL multi-crop | 全局+局部兼顾 | crop调度更复杂 |

所以 Step3-VL 的核心思想可以概括成一句话：

> **用 global crop 保证“看懂整张图”，用 local crops 保证“看清细节”，再通过 batch-parallel ViT 编码实现高吞吐。**

这也是它在：

* OCR
* GUI
* 文档理解
* Counting
* Grounding

这些任务上表现很强的重要原因之一。

[1]: https://huggingface.co/stepfun-ai/Step3-VL-10B/blob/e6715b35930a5945f025a3d6127fa0c6d36dea31/README.md?code=true&utm_source=chatgpt.com "README.md · stepfun-ai/Step3-VL-10B at e6715b35930a5945f025a3d6127fa0c6d36dea31"
[2]: https://hyper.ai/en/papers/2601.09668?utm_source=chatgpt.com "STEP3-VL-10B Technical Report | Papers | HyperAI"
[3]: https://github.com/stepfun-ai/Step3-VL-10B?utm_source=chatgpt.com "GitHub - stepfun-ai/Step3-VL-10B: Step3-VL-10B: A compact yet frontier multimodal model achieving SOTA performance at the 10B scale, matching open-source models 10-20x its size. · GitHub"

---
> Q：local crop是怎么切分的？其数量由什么决定？

Step3-VL 论文和代码里只公开了：

> **728×728 global + multiple 504×504 local crops**

但是**没有把 local crop 的具体切分算法（如 stride、多大 overlap、最多几个 crop）直接写死公开出来**。不过从其 transformers processor 实现方式、multi-patch preprocessing，以及这类 VLM 的通用 crop pipeline，可以比较准确地推断它的机制。

结论先说：

> Step3-VL 的 local crop 本质上是 **adaptive tiling（自适应网格切分）**，crop 数量主要由：
>
> 1. 原图分辨率
> 2. 原图长宽比
> 3. 最大视觉 token budget
> 4. processor 允许的 max_num_patches / max_num_crops
>
> 共同决定。

---

# 一、local crop 怎么切？

Step3-VL 不是“目标检测式 crop”（不是先找 ROI 再裁）。

它更像：

> **uniform sliding-window tiling**

即：

把原始高分辨率图像切成多个固定大小的窗口：

每个窗口：

```text
504 × 504
```

比如原图：

假设：

```text
2016 × 1512
```

会先在原图上建立一个 grid。

可以理解成：

![Image](https://images.openai.com/static-rsc-4/a2qLNO4_cVfpCglinpNCMmVKfv3PBAuPOVTpFwQ_1W9lnmMt1i86CLC-b2Co1IVjaMVYD-3Jsdv-zaDpFdnT-F8o8RcO6OdHp4Txdm_zNGRGYsq1HS5yVVsKtEeXIl7cTnQNwDF8knpLq6_JakVmr1ATl2nzoAxya7aDGKVcx0RWPZDpoaJYw8TR6bOevDbN?purpose=fullsize)

![Image](https://images.openai.com/static-rsc-4/AKds2rlsz-7lUVr7ZxRzSaZaad3tw5gt_YqKWlWOY4b08ZVcdghKU1s-yCY2XPsDcgTUF7p5_jkxo1HfcdB2AAdoDX67YnzuDIpcKL075Kqypz1jNVcSe_SPF95cWIxZxBcQsPdF6D_60NVEElZgVld1lYH48MQjqVuIMm44sqYUrxZ3YeFNskfJex6sYfAQ?purpose=fullsize)

![Image](https://images.openai.com/static-rsc-4/pRyWoFxxKQBfn8LVWusEya3V9ElwbVcUD-q6-98FDBeut-20vRzUYbP9V3EUW1_Gywo6TqF_uO9Ejb0ujgmhgVhiW18tzizrt8SmN0OhKR7tBiO3NPUIa7aXZGSUWaXaAumCgVB2blro_VRVoFWnGV4XyxCbewlOLH6JJz-S6sPiL1CYg1KpPjppaPVUOM2M?purpose=fullsize)

![Image](https://images.openai.com/static-rsc-4/vSLGI-Gn6bAlPmXoxZKwf6QNETyVI_Q5Lrub-OckC_fGJsIjXLTsc9x7GbGwBfxEihFd0U4uAkjoOdvcsjzK5sTD95yHkohLwgKGQdb--kxNu3OHcvbgtxQQHXM9GisSrufVntiSiJaXBLAKG_zlYedZ-po8FefJkbpzhkYqFCfeJ3YHkCqpbdYXkQnjxJKZ?purpose=fullsize)

![Image](https://images.openai.com/static-rsc-4/OZ2d37iRZZsqGzwS0Aa-0PdlEBZ4uLXeCkUG2MD3Nc2ltTVqAjozHpQy2REeXS9jc5UOA152HvRAr9ufMDcS3W8gipIFjQcZ0vKjA4t8o38c1Mun5P0y41zLoZbgsKM1IX7JqjkkSW0V77VhpuIuD-B_HJ7WVKeRbVo5kSvk5eTaS4AqWZ2uytjD3NTSUpBR?purpose=fullsize)

![Image](https://images.openai.com/static-rsc-4/51K9vyuXsL8rNMO2GSI_4LjzqOIzqUrGsqYb1E-zWPPlI_OEm3x-qc4_cb5vR4I6ylWbRkkSzLaRcSrsSMyQN2c1xEEWS-L2EBy9COFZGqBd4j05Q8a6PT9H9Bc53vqXgoE9DA74wtVPp5esEir4k4UZRWxoFSTi9fEzw92Tm7kZIOd-XlSQ-Cc1tohg_chj?purpose=fullsize)

数学上：

若：

原图：

[
W \times H
]

crop size：

[
C=504
]

那么理论 grid 数：

宽方向：

[
N_w = \lceil W / 504 \rceil
]

高方向：

[
N_h = \lceil H / 504 \rceil
]

总 crop：

[
N = N_w \times N_h
]

比如：

---

## Case 1：1000×1000

宽：

[
\lceil1000/504\rceil = 2
]

高：

[
\lceil1000/504\rceil = 2
]

所以：

[
2\times2 =4
]

即：

```text
+---------+---------+
| crop1   | crop2   |
+---------+---------+
| crop3   | crop4   |
+---------+---------+
```

---

## Case 2：2000×1000

宽：

[
\lceil2000/504\rceil = 4
]

高：

[
\lceil1000/504\rceil = 2
]

得到：

[
8 crops
]

---

这和很多 document VLM 是一致的。

---

# 二、是不是完全无重叠？

通常不是。

为了避免边界信息丢失，通常会加 overlap。

即 stride 可能不是：

[
504
]

而是：

比如：

[
420\sim480
]

这样 crop 之间有 5~20% overlap。

例如：

```text
crop1:  [0,504]
crop2:  [420,924]
```

会重叠：

84 pixels。

论文虽然没写 overlap，但 multi-crop 来源参考的是 DINO-style crops。([ZNAK автошкола][1])

工程上几乎一定有 overlap，否则：

* 小字可能刚好切边
* OCR性能下降
* grounding 会不稳定

---

# 三、crop 数量由什么决定？

核心是：

## 1. 原图尺寸

这是最直接的。

图片越大：

crop 越多。

例如：

| 原图        | local crops |
| --------- | ----------- |
| 800×800   | 4           |
| 1600×1600 | 16          |
| 2500×2500 | 25          |

近似：

[
N\propto \frac{WH}{504^2}
]

---

## 2. 长宽比

长图会产生更多 crop。

例如：

---

## 横向长图

4000×1000

可能：

```text
8 × 2 =16 crops
```

---

## 纵向文档

1000×4000

可能：

```text
2 × 8 =16 crops
```

所以：

文档类任务 crop 通常比普通照片更多。

---

# 四、真正限制数量的是 token budget

这是最关键的。

因为 Step3-VL 不可能无限切。

ViT token 数：

每个 local crop：

因为 patch=14：

[
504/14=36
]

所以：

每个 crop：

[
36^2 =1296
]

global：

[
52^2=2704
]

如果有 K 个 local：

总 token：

[
2704 + 1296K
]

比如：

如果：

K=8

总：

[
13072
]

如果：

K=16

总：

[
23440
]

token 很快爆炸。

所以 processor 一般会限制：

类似：

```python
max_num_crops = K_max
```

或者：

```python
max_visual_tokens = T_max
```

Step3-VL README 提到 inference 的 processor 默认启用 multi-patch preprocessing，和 vLLM/sglang 对齐。([Hugging Face][2])

因此：

> 真正 crop 数量通常是：

[
K = \min(K_{grid}, K_{budget})
]

即：

网格理论数量 vs token预算上限。

---

# 五、如果 grid 太多怎么办？

一般会：

## 策略1：降低 grid 密度

比如：

原来：

```text
4×8 = 32 crops
```

变成：

抽样：

```text
2×4 = 8 crops
```

---

## 策略2：优先覆盖全图

例如：

先保留：

* corners
* center

像这样：

```text
A---B---C
|       |
D---E---F
|       |
G---H---I
```

只取：

```text
A C E G I
```

保证 spatial coverage。

---

# 六、Step3-VL 的实际 pipeline（推断）

完整流程大概率是：

```text
Original Image
      │
      ▼
Aspect Ratio Analysis
      │
      ▼
Generate Adaptive Grid
      │
      ▼
Cut 504×504 Tiles
      │
      ▼
Apply Budget Constraint
      │
      ▼
Keep Top-K Tiles
      │
      ▼
Batch into ViT
```

![Image](https://images.openai.com/static-rsc-4/F9PdzR5h5FLG98b2tDL-HbBfJwolwySZyW-aLJ7NcIlxWP8SofYI_WDyVyN9NQcFcPFLFBKkxrw4s3ZfgmGsLl_I9Hbf-BFvmkRPiWZzqw-8s8ImxpyT19nIYbpJv-4m1cbLxUMLQSIg51EUQqix3jPxO_nPecmBYpAeffFRmmGfvaaY1j93LEkt-XckVhqO?purpose=fullsize)

![Image](https://images.openai.com/static-rsc-4/-pniyINpmk68u2bLj6BJuRhl6tpdbguFFher6IfBazqgghOGrH0B8p37JBS4NCiQMWPToy8JAdkGW-THUkz4X6gse3-ba1cZ00xWz7FzfIMkjQ9GmBbkdDtzxW8z_ZnQUjQXs3M97xhEeiqaL9xhN8ud0tTR8XNp8AWCnu0vTlzOQkPC93N3JeRHLC5F4zqz?purpose=fullsize)

![Image](https://images.openai.com/static-rsc-4/rJ5Y-HuX1ViBBo_53RucNfecxU5fwxCWCHku5iwIOOCRDis_6uxpinrAK0BlGqmcGpFMOp2sKqvhG9K2ls33-tvNZo1AJkVH8h1EFMPG1ZDEG7lhFbdd8ETT5E_JsFRiIjvHy1IouHmR2iEpGMrekY0EAaQ8OCX5xc1sZWndGsUPjZUbzTF9CY872FL6DemT?purpose=fullsize)

![Image](https://images.openai.com/static-rsc-4/AJOllzZkOyT7LJkcUYYT8AZkAG5BayUOrTZ9WZjZwRMhUuOwDNyS9-8i-1sS3XFxVNyk92dJ6pEPNwciokl0U1F7ljbAx6MNuxGOJjggoaiYwIgdLEyh1wruwYVMw_D6y2IzmIlxqmxoWRdFJeqiW8CXAsxgQPUmkunfkEmpPEDfsYKFwTk3ptIWzUL5KrEY?purpose=fullsize)

![Image](https://images.openai.com/static-rsc-4/mr41mHdRxo8-mZkjrcD9RieidrSfFzRc0TU6sE6lTa8v8yk8bES3oW-QcO0Cjt3017pFapBPjZ8pMv60DNhitJNJKFOkZ0eBhb6M1qoMZSL3JZYR5zZln8DAGGfFuTjmBw8bDQJZ6V1yOdoL0Qwfx7QO0UTr1o1b3omIW2SlPIFdQZn8B5rRPvvUDR9elL0E?purpose=fullsize)

![Image](https://images.openai.com/static-rsc-4/dISWUIka5pakSZGi9JSGZclq0HDta1083PGCSo5MPALlgn2EgWuYvuGr7ASpisA6KevoMJVyJZGF2pdDGdE8tO5xHqiHbc9OMWKCu-1Z2Yq9yuQrnpr6EFVnjLmSwOxaDDAOWNQDcvlfbB3ASPSjYNd7Lvc3Sfunwy6FArquiS9AwKaNZqIutQWa-msCCO-1?purpose=fullsize)

![Image](https://images.openai.com/static-rsc-4/B20UBKndKOZqz3nJGWk17dkPvHwPiedU8h87z2ajdBaINC3YBBmNhi2czN11cBVYNbSYRjqQrg4sF8Xs9QmgoyxjG_BHSFRtBe7JXiwLIgrxZNPT-jBrHYFtxJXqIUlj-0MezBs-SpwUjeo-sffHE0zAqz7l0dNZ1vOyYN3zqcnPhJvf7YFscBEGSWLaOkG9?purpose=fullsize)

---

# 七、在 vLLM 中对应什么？

你前面看过 Step3-VL 的 vLLM 实现。

Step3-VL 在 vLLM 中最终会把：

```python
[
 global_feature,
 patch_feature_1,
 patch_feature_2,
 ...
]
```

通过类似：

`finalize_encoder_cudagraph_output()`

做 batch flatten，然后插回 `<im_patch>` placeholder。

所以：

> crop 数量最终决定了：

placeholder 数量：

[
1 + K
]

即：

```text
<global><patch1><patch2>...<patchK>
```

K 越大：

* encoder latency ↑
* projector latency ↑
* LLM prefill latency ↑

这就是 Step3-VL 要严格控制 crop 数的原因。

---

一句话总结：

> **Step3-VL 的 local crop 本质是“固定 504×504 的自适应网格切分”，数量先由原图尺寸/长宽比决定，再由视觉 token budget 做上限裁剪。**

即：

[
\text{num_crops}
================

\min
\left(
\lceil W/504\rceil \cdot \lceil H/504\rceil,
K_{max}
\right)
]

这是目前最符合论文、processor 和部署实现的解释。

[1]: https://www.znakschoola.ru/?_=%2Fpdf%2F2601.09668%23UE7DkG0PaBHZOakR54g1Wl8%3D&utm_source=chatgpt.com "STEP3-VL-10B Technical Report"
[2]: https://huggingface.co/stepfun-ai/Step3-VL-10B/blob/e6715b35930a5945f025a3d6127fa0c6d36dea31/README.md?code=true&utm_source=chatgpt.com "README.md · stepfun-ai/Step3-VL-10B at e6715b35930a5945f025a3d6127fa0c6d36dea31"

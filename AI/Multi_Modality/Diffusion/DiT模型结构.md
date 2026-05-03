**Diffusion Transformers（DiT）** 本质上是：**用 Transformer 替代传统 U-Net 来做 diffusion model 的噪声预测器（denoiser）**。

也就是说：

* 传统 diffusion model（如 DDPM、Stable Diffusion）常用 **U-Net**
* DiT（如 Scalable Diffusion Models with Transformers）则使用 **Vision Transformer (ViT)-style Transformer backbone**

它的核心思想非常简单：

> 把图像切成 patch → 编码成 token → 用 Transformer 做去噪 → 输出预测噪声 / velocity / x0

---

# 一、DiT 的整体结构（总览）

整体流程：

```text
x_t（加噪图像）
   ↓
Patchify（切成patch）
   ↓
Linear Projection（映射成token）
   ↓
+ Position Embedding
+ Timestep Embedding
+ Class Embedding（可选）
   ↓
N层 Transformer Block
   ↓
Final Layer
   ↓
Unpatchify
   ↓
预测 ε_theta(x_t, t)
```

即：

```text
Diffusion Model
=
Noise Schedule
+
Transformer Denoiser
```

---

# 二、详细结构拆解

---

# Step 1：输入 noisy image

输入：

```text
x_t ∈ R^{H × W × C}
```

例如：

```text
256 × 256 × 4
```

（latent diffusion 中通常是 VAE latent，不是 RGB）

这是：

```text
在时间步 t 的 noisy latent
```

模型目标：

```text
预测加入的噪声 ε
```

即：

---

# Step 2：Patchify（切块）

像 ViT 一样：

假设：

```text
patch size = p
```

则：

```text
N = (H/p)(W/p)
```

个 patch

例如：

```text
256×256
p=16

→ 16×16 = 256 tokens
```

每个 patch flatten：

```text
p × p × C
```

再线性映射到：

```text
hidden size = D
```

得到：

```text
tokens ∈ R^{N × D}
```

---

# Step 3：加入条件信息

这是 DiT 非常关键的地方。

不仅有：

## （1）Position Embedding

标准 ViT：

```text
token + pos_embed
```

---

## （2）Timestep Embedding

扩散模型必须知道：

```text
当前 diffusion step 是多少
```

先：

```text
t
→ sinusoidal embedding
→ MLP
→ timestep embedding
```

得到：

```text
e_t ∈ R^D
```

---

## （3）Class Embedding（可选）

做 class-conditional generation：

```text
label y
→ embedding
→ e_y
```

例如：

* ImageNet class
* text embedding
* prompt embedding

---

## 最终条件：

```text
c = e_t + e_y
```

然后：

```text
condition 注入每个 Transformer block
```

这一步非常重要。

---

# 三、DiT Block（核心）

这是最关键部分。

不是普通 Transformer block。

而是：

# adaLN-Zero Block

论文核心创新。

结构：

```text
x
↓
LayerNorm（no affine）
↓
condition 调制（AdaLN）
↓
MSA
↓
Residual

↓

LayerNorm
↓
condition 调制
↓
MLP
↓
Residual
```

即：

```text
x + Attention
x + FFN
```

但：

## scale / shift 由条件决定

形式：

\mathrm{AdaLN}(x,c)=\gamma(c)\cdot \mathrm{LN}(x)+\beta(c)

其中：

```text
γ(c), β(c)
```

由：

```text
timestep embedding
+
class embedding
```

生成。

这比：

* cross-attention
* concat condition

更稳定、更高效。

---

# 四、Final Layer

最后：

```text
tokens
→ linear projection
→ patch pixels
```

即：

```text
D → p×p×C
```

然后：

```text
unpatchify
```

恢复：

```text
H × W × C
```

输出：

```text
预测噪声 ε
```

或者：

* v-prediction
* x0-prediction

---

# 五、和 U-Net 的核心区别

| 项目             | U-Net  | DiT         |
| -------------- | ------ | ----------- |
| backbone       | CNN    | Transformer |
| inductive bias | 强（局部性） | 弱           |
| scaling law    | 一般     | 非常优秀        |
| FLOPs 扩展       | 较差     | 极好          |
| 长程依赖           | 一般     | 强           |
| 并行性            | 一般     | 好           |
| 大模型能力          | 一般     | 很强          |

这也是为什么：

> 大规模 diffusion 现在越来越偏向 Transformer

例如：

* Sora
* SD3
* Flux
* OpenAI 视频生成模型

基本都走向 Transformer 化。

---

# 六、DiT 为什么效果好？

论文核心结论：

> FID improvement 主要由 GFLOPs 决定

不是：

* 参数量

而是：

* 总计算量

即：

```text
More FLOPs → Better FID
```

Transformer 特别适合：

```text
大规模扩展
```

这就是 DiT 成功的根本原因。

---

# 七、一句话总结

DiT 可以理解成：

> 把 U-Net 换成 ViT，
> 再用 adaLN-Zero 注入 diffusion condition，
> 最终实现更强 scaling law 的 diffusion model。

本质上：

```text
ViT for Diffusion
```

这就是 DiT。

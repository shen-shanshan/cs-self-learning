# vLLM 多模态推理｜ViT 性能优化

多进程掩盖预处理耗时
接入融合算子，mrope/add+rms-norm/rope/swiglu
优化NpuFlashAttention算子的输入形状
conv使用img2col算法优化卷积计算
使用cos/sin cache优化Rope计算
使用CPU cache减少算子调用host侧耗时等方式
encoder dp > sp > tp

大纲：
多模态模型分类
VL模型结构、主要耗时
ViT结构、特性（计算bound）
各优化手段原理与收益
profiling指导

## 一、引言

## 二、多模态大模型概述

多模态理解模型推理：
    多模态输入预处理
    多模态编码encode
    LLM prefill
    LLM decode

多模态理解生成统一模型推理：
文本理解：
    文本 -> 文本
    Qwen3
多模态理解：
    image/video vit + ar llm
    文本/图像/视频/音频 -> 文本
    Qwen3-VL Intern-VL3 DeepSeek-OCR
多模态理解+生成统一：
    ar llm + DiT
    文本/图像/视频/音频 -> 文本/图像/视频/音频
    Qwen3-Omni Qwen-Image BAGLE
    Qwen3-Omni:
        扩展架构，包含多个 AR+DiT/ConvNet
        ar-decoder(thinker) + ar-decoder(talker) + codec(DiT)
    Qwen-Image:
        以 DiT 作为主结构用于多模态生成，AR 作为多模态理解组件
    BAGLE：
        以 AR 作为主结构，DiT 作为视觉等多模态生成的组件。
纯 DiT 支持多模态理解生成统一：
纯 AR 支持多模态理解生成统一：

## 三、多模态推理性能瓶颈

VL 模型：
chat_template：将输入整合为模型输入标准模式；
input_prepare：准备送入模型主体部分的输入；
model：ViT：图像编码、decoder-only LLM；
decode：将 token decode 为文本；

多 batch 输入、段序列输出下，耗时集中在 ViT、预处理以及 LLM prefill。 -> 饼图（3 + LLM decode、其它）

ViT结构、特性（计算bound）
patch_emb: conv3d
vision_block
merger

Vit 的实际执行：get_mm..._embed...()
prompt embed 与多模态 embed 的合并：get_input_embed...()

## 引擎级优化

多进程掩盖预处理耗时：V1 engine 图
视觉预处理 cache
多模态 embedding cache 跨请求共享
EPD 分离

以后单独写一篇推理 pipeline。

前处理：processor
进行文本与图像的前处理：
    image的归一化、resize
    文本占位符更新
    记录 token id 关键信息（图像的 token 位置长度）
指明多模态输入限制，方便 vllm 预估视觉编码部分显存占用

token 更新策略：将 image/video token 替换为 token 数 * image/video token id

## 四、ViT 性能优化手段

接入融合算子，mrope/add+rms-norm/rope/swiglu
优化NpuFlashAttention算子的输入形状
conv使用img2col算法优化卷积计算
使用cos/sin cache优化Rope计算
使用CPU cache减少算子调用host侧耗时等方式
encoder dp > sp > tp
profiling指导

### conv

vit 分块操作，将图像从二维转换为一维，是卷积操作。
卷积核大小与步长相同，不存在划窗移动时数据重叠。

性能收益？

### 接入融合算子

rope/mrope/rms-norm/swiglu

### rope cos/sin cache

性能收益？

### encoder dp mode

encoder dp > sp > tp

为什么 sp 优于 tp?

1.
tp：2*28+1 all reduce -> 单block通信耗时提升约40%
sp：2*28 all2all + 1 all gather

2.额外算子入 rms-norm、gelu 有享受到 shape 减少收益
Attn 和 mlp 输入会默认做 rms-norm

### FA padding

vit fa 原始 dim = 80，硬件不亲和
做到 128 对齐

dim     性能 diff
80
86
112
128

为什么是 128？

pad 方案：
权重pad，引入额外计算量
算子输入 pad，引入额外小算子

性能收益？

### seq_lens cpu cache

性能收益？

## 五、总结

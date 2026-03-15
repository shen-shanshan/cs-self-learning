M# Mamba: Linear-Time Sequence Modeling with Selective State Spaces

[Mamba Github](https://github.com/state-spaces/mamba)

## What is the paper about?

[Mamba Overview](./Mamba_Overview.md)

![](./images/selection.png)

**模型结构（Mamba Block）：**

- `fused_add_norm`
- `mixer`:
  - Mamba/Mamba2:
    - `mamba_inner_fn(...)`：展开成一个全局卷积，像 CNN 一样进行高效的并行计算？
    - `selective_scan_fn(...)`：硬件感知的并行扫描算法。
  - MHA
- `fused_add_norm`
- `mlp`

**模型参数（权重矩阵）：**

[Mamba Params](./Mamba_Params.md)

## What is new compared to prior work?

- Transformer block: **LN -> MHA/MLP -> Add**.
- Mamba block: **Add -> LN -> Mixer**, returning both the hidden_states (output of the mixer) and the residual. (This is purely for performance reasons, as we can fuse add and LayerNorm)

## What experiments were run to support the arguments in this paper?

...

## What are the shortcomings/limitations of this paper?

[Compare with Qwen2.5/3-Next](./Compare_with_QwenNext.md)

## What is a reasonable next step to build upon this paper?

...

## Basic Concepts (Related Knowledge)

...

## References (Related Works)

...

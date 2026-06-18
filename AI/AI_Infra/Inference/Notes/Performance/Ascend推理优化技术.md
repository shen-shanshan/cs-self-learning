# Ascend 推理优化技术

## 推理优化整体思路

先纯模型再服务化，纯模型阶段先优化 D 再优化 P，使 P 和 D 的性能极致化，然后利用 P/D QPS 来优化 PD 配比。

PD 分离配比计算方式：

```
Prefill QPS = Decode QPS

Prefill QPS = Prefill 实例数 (?) * 单实例 QPS
Decode QPS = Decode 实例数 (?) * 单实例 QPS

Prefill 单实例 QPS = BatchSize / TTFT
Decode 单实例 QPS = Avg Decode BatchSize / (TPOT * Decode Length)
```

## MoE 的六种场景

Input -> Attn (DP) -> all gather -> MoE (EP) -> reduce scatter
Input -> Attn (DP) -> MoE (EP) -> all2all
Input -> Attn (TP) -> all reduce -> MoE (EP) -> all reduce
Input -> all gather -> Attn (TP) -> reduce scatter -> MoE (EP) -> all2all

## EPLB

静态负载均衡、动态负载均衡。

## LM Head TP

LM Head 位于模型的最后一层，其权重 shape 为 `[hidden_size, vocab_size]`（7168，129280）。
此处左右矩阵计算不均衡且此处加载大约 1.8G 的权重，形成 Prefill 阶段的内存瓶颈。
词表较大，从而导致右矩阵较大，进行 TP 切分，不仅可以降低显存消耗，还可以减少右矩阵的搬运时间，从而提升模型性能。

## DCP 🌟

针对 decode，切 KV Cache 的序列维，目标是去重 KV，加大 Batch。
DCP 必须是从 TP 组里切出来的子集，`tp_size` 必须被 `dcp_size` 整除。

具体实现：

每个请求的整段 KV Cache 沿着序列维度交错切分到 N 个 DCP rank 上，每个 rank 只保留 1/N。
每生成新 token 时，Q 在所有 rank 间 all gather（gather head 轴，因为通信很小，所以便宜）。
每个 rank 用全量 Q + 本地 1/N 的 KV 算出一部分 attention 输出 + 本地 LSE。
用 LSE 加权合并得到最终输出，建议采用 all2all，head 轴切分，LSE 完整，然后 LSE 进行 merge。

## PCP 🌟

针对 prefill，切 Q/KV 的序列维，目标是降低 TTFT。

## Rope 优化

位置编码计算时，sin/cos 在一个 step 内部是常量，可以外置（使用 cache 减少重复计算）。

## Profiling Host Bound 异常

采集 Profiling 时，包含了 call stack，导致 Profiling 存在 host bound。

## MTP 🌟

正常推理：每层 Attention 算子仅计算单个输入并得到单个输出。
MTP 推理：每层 Attention 算子计算多个输入并得到多个输出。

```
token-0           -> ... -> output head -> token-1
                  -> ... -> last layer hidden_states + token-1 -> MTP -> token-2
token-1 + token-2 -> ...
```

## EAGLE-3

...

## Suffix Decoding

...

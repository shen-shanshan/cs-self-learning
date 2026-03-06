TP：
模型参数分片存储
按 hidden_size/head 维度切分
显存占用低（除 TP size）
通信量大（每层的激活值，每层两次）

适用于 low-latency/low-load 场景
适用于大模型

DP：
模型参数完全复制
Batch 维度切分
显存占用高（乘 TP size）
层间无通信，仅最后 gather

适用于 heavy-load 大量数据
适用于中等模型

在多 batch 场景下，适用 ViT DP 可以在 forward 前把数据按图片分片到多张卡上，中间过程不需要再通信，完成后最后一次再屌用 AllGather 分散到各个卡上。

ViT 测试结果：DP > SP > TP

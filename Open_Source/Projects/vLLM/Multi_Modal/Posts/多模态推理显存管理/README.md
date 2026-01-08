# vLLM 多模态推理显存管理

vllm 显存：

- torch 类显存（用 torch 模块或者 API 创建的显存）：
  - 权重（weights）
  - 激活值（activation）
  - KV Cache
  - 其他显存（Others）
- 非 torch 类显存：
  - 通信显存：缓存 buffer
  - Attention 模块：有些操作采用底层 API 实现，有单独分配/释放显存行为
  - custom ops：可能有单独分配/释放显存行为

## 参考资料

- [vLLM 显存管理详解](https://zhuanlan.zhihu.com/p/1916529253169734444?share_code=aePDPg2VonBo&utm_psn=1917144770171606655)
- [PyTorch 显存管理介绍与源码解析（一）](https://zhuanlan.zhihu.com/p/680769942)
- [PyTorch 显存可视化与 Snapshot 数据分析](https://zhuanlan.zhihu.com/p/677203832)
- [GPU 内存（显存）的理解与基本使用](https://zhuanlan.zhihu.com/p/462191421)

PyTorch：

- [PyTorch Docs: Understanding CUDA Memory Usage](https://docs.pytorch.org/docs/stable/torch_cuda_memory.html#non-pytorch-alloc)

Ascend NPU：

- [昇腾社区文档：内存快照](https://www.hiascend.com/document/detail/zh/Pytorch/720/ptmoddevg/Frameworkfeatures/featuresguide_00004.html)

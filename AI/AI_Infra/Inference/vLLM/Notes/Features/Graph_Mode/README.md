# Graph Mode

## TODO

**疑惑：**

- 需要占用一定的显存资源，vLLM 是如何管理的？
- prefill 与 decode 通信同步等待，这里 event 是 cpu 操作，cuda graph 抓不到也不好做，除非又搞子图（？）
- 了解 torch compile mode 和 CUDA Graphs mode 的区别？

## 学习资料

**CUDA Graph:**

- [ ] [一文读懂cuda stream与cuda event](https://zhuanlan.zhihu.com/p/699754357)
- [ ] [一文读懂cudagraph](https://zhuanlan.zhihu.com/p/700224642)
- [ ] [CUDA Graph 学习笔记](https://mp.weixin.qq.com/s/9SAW3kgI_G1CzvNM9_aLTg)
- [ ] [CUDA效率优化之：CUDA Graph](https://zhuanlan.zhihu.com/p/467466998?share_code=DqtX35OwcCmM&utm_psn=1995146988220597445)

**PyTorch Dynamo:**

- [ ] [Optimizing Production PyTorch Models’ Performance with Graph Transformations](https://pytorch.org/blog/optimizing-production-pytorch-performance-with-graph-transformations/)
- [x] [Accelerating PyTorch with CUDA Graphs](https://pytorch.org/blog/accelerating-pytorch-with-cuda-graphs/)
- [ ] [Dynamo Overview](https://docs.pytorch.org/docs/main/torch.compiler_dynamo_overview.html)
- [ ] [torch.compile](https://docs.pytorch.org/docs/stable/generated/torch.compile.html#torch-compile)
- [ ] [torch.fx](https://docs.pytorch.org/docs/stable/fx.html)
- [ ] [Custom Compiler Passes and Partitioners](https://docs.pytorch.org/executorch/stable/compiler-custom-compiler-passes.html)

**vLLM Graph Mode:**

- [x] [vllm PR - Allow full cudagraph with separate attention routines and orthogonal to compilation, add support for FA2 and FlashInfer](https://github.com/vllm-project/vllm/pull/20059)

## 代码练习

- [x] https://github.com/CalvinXKY/InfraTech/blob/main/llm_infer/cuda_graph.ipynb

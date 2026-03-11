# Graph Mode

## TODO

**疑惑：**

- 哪些操作能和不能被捕获入图？
- 需要占用一定的显存资源，vLLM 是如何管理的？
- 什么是动态 shape？
- prefill 与 decode 通信同步等待，这里 event 是 cpu 操作，cuda graph 抓不到也不好做，除非又搞子图（？）

## 学习资料

https://github.com/CalvinXKY/InfraTech/blob/main/llm_infer/cuda_graph.ipynb

## 参考资料

**PyTorch Dynamo:**

- [ ] [Optimizing Production PyTorch Models’ Performance with Graph Transformations](https://pytorch.org/blog/optimizing-production-pytorch-performance-with-graph-transformations/)
- [x] [Accelerating PyTorch with CUDA Graphs](https://pytorch.org/blog/accelerating-pytorch-with-cuda-graphs/)
- [ ] [Dynamo Overview](https://docs.pytorch.org/docs/main/torch.compiler_dynamo_overview.html)
- [ ] [torch.compile](https://docs.pytorch.org/docs/stable/generated/torch.compile.html#torch-compile)
- [ ] [torch.fx](https://docs.pytorch.org/docs/stable/fx.html)
- [ ] [Custom Compiler Passes and Partitioners](https://docs.pytorch.org/executorch/stable/compiler-custom-compiler-passes.html)

**vLLM Graph Mode:**

- [x] [vllm PR - Allow full cudagraph with separate attention routines and orthogonal to compilation, add support for FA2 and FlashInfer](https://github.com/vllm-project/vllm/pull/20059)

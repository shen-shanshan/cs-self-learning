# Graph Mode

## 学习笔记

- [TorchAir](../../../../../AI/AI_Infra/Compute_Architecture/CANN/TorchAir/TorchAir学习笔记.md)

## 参考资料

**PyTorch Dynamo：**

- [ ] [Optimizing Production PyTorch Models’ Performance with Graph Transformations](https://pytorch.org/blog/optimizing-production-pytorch-performance-with-graph-transformations/)
- [x] [Accelerating PyTorch with CUDA Graphs](https://pytorch.org/blog/accelerating-pytorch-with-cuda-graphs/)
- [ ] [Dynamo Overview](https://docs.pytorch.org/docs/main/torch.compiler_dynamo_overview.html)
- [ ] [torch.compile](https://docs.pytorch.org/docs/stable/generated/torch.compile.html#torch-compile)
- [ ] [torch.fx](https://docs.pytorch.org/docs/stable/fx.html)
- [ ] [Custom Compiler Passes and Partitioners](https://docs.pytorch.org/executorch/stable/compiler-custom-compiler-passes.html)

**TorchAir：**

- [x] [PyTorch 图模式使用（TorchAir）](https://www.hiascend.com/document/detail/zh/Pytorch/710/modthirdparty/torchairuseguide/torchair_00003.html)
- [ ] [reduce-overhead 模式配置](https://www.hiascend.com/document/detail/zh/Pytorch/710/modthirdparty/torchairuseguide/torchair_00038.html)
- [ ] [GE 图引擎](https://www.hiascend.com/cann/graph-engine)
- [ ] 刘逸舟 aclgraph 赋能培训

**vLLM Graph Mode：**

- [x] [vllm PR - Allow full cudagraph with separate attention routines and orthogonal to compilation, add support for FA2 and FlashInfer](https://github.com/vllm-project/vllm/pull/20059)

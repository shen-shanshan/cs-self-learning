CUDA Graph 要求被捕获的计算图中所有 tensor shapes 在 capture 和 replay 间保持一致。核心约束：

- **无 CPU 同步**: 不能在 graph 内调用 `.item()`、`.cpu()` 或任何触发 D2H copy 的操作
- **无动态控制流**: 不能在 graph 内使用依赖 tensor 值的 `if/else` 分支（依赖 shape 的 `if` 在 capture 时 bake）
- **固定 shape**: 所有中间 tensor 的 shape 必须在 capture 时确定

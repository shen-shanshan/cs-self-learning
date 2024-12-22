# vLLM 源码阅读

- **LLM**（LLMEngine）：offline batched inference
- **AsyncLLMEngine**（LLMEngine）：online serving

LLMEngine -> Executor(1) -> Worker(N) -> ModelRunner(loading and running the model) -> Model(`torch.nn.Module`)

VllmConfig：在全局传递的配置类。

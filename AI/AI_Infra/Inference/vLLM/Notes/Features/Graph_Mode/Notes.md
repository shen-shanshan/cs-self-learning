# vLLM Graph Mode

**vLLM 为什么没在 Prefill 阶段支持 Cuda Graph？**

decode 阶段的 kv cache 不也是变长的么？
vllm 用页表来管理 kv cache，页表形状固定，kv cache 整块的形状也固定，把这些参数传进 flash_attn 算子即可，flash_attn 底层也支持用页表来取 kv cache 进行计算。
kv cache 是一整段，变的是索引值。

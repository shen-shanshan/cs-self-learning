# Guided Decoding

## Backend

### Outlines

**基本原理：**

For every state within the FSM, we can calculate the permissible transitions and identify the acceptable next tokens. This allows us to track the current state during decoding and filter out invalid tokens by applying logit bias to the output.

![outlines](./images/method1.png)

**缺点：**

Since the FSM is constructed at the token level, it can transition the state by only one token at each step. Consequently, it can decode only one token at a time, which results in slow decoding.

**代码实现：**

You can install the latest version of Outlines from the repository's `main` branch:

```bash
pip install git+https://github.com/dottxt-ai/outlines.git@main
```

**参考资料：**

- 官方文档：[contributing doc](https://dottxt-ai.github.io/outlines/latest/community/contribute/)
- 论文：[Outlines](../../../../../Research/Papers/Papers/Outlines.pdf)

### XGrammar

**基本原理：**

**Persistent execution stack:** To speed up the maintenance of multiple parallel stacks during splitting and merging due to multiple possible expansion paths, we design a **tree-based data structure** that efficiently manages multiple stacks together. It can also store state from previous times and **enable efficient state rollback**, which speeds up the runtime checking of context-dependent tokens.

We also provide additional co-design APIs, to enable **rollback** (needed for speculative decoding) and **jump-forward decoding**, which further speeds up the speed of structured generation.

**参考资料：**

- [Post](https://blog.mlc.ai/2024/11/22/achieving-efficient-flexible-portable-structured-generation-with-xgrammar)
- [Docs](https://xgrammar.mlc.ai/docs/)
- [GitHub](https://github.com/mlc-ai/xgrammar)

## Inference Engine

### vLLM

**支持后端：**

- outlines
- lm-format-enforcer
- xgrammar

**代码实现：**

- 参数配置：`sampling_params.py/GuidedDecodingParams`
- 代码位置：`vllm/model_executor/guided_decoding`

**测试脚本：**

- example（online）：`examples/online_serving/openai_chat_completion_structured_outputs.py`
- example（offline）：`examples/offline_inference/structured_outputs.py`

**参考资料：**

- [vLLM 官方文档 - Structured Outputs](https://docs.vllm.ai/en/stable/features/structured_outputs.html#structured-outputs)

### SGLang

**基本原理：**

Jump-Forward Decoding With a Compressed Finite State Machine:

![Jump-Forward Decoding](./images/compare.png)

**代码实现：**

`https://github.com/sgl-project/sglang/blob/main/python/sglang/srt/constrained/outlines_backend.py`

**参考资料：**

- [Fast JSON Decoding for Local LLMs with Compressed Finite State Machine](https://lmsys.org/blog/2024-02-05-compressed-fsm/)

# vLLM 环境变量管理

- ENV 自动文档，参考：[Environment Variables](https://docs.vllm.ai/en/stable/serving/env_vars.html)
- ENV 对象化，区分人为设置和系统设置（setup？）
- ENV import？

## 自动生成文档

Sphinx 引擎可以引用文件中的内容：

```
:::{literalinclude} ../../../vllm/envs.py :end-before: end-env-vars-definition :language: python :start-after: begin-env-vars-definition :::
```

## 区分人为设置和系统设置？

```python
# envs.py
def is_set(name: str):
    """Check if an environment variable is explicitly set."""
    if name in environment_variables:
        return name in os.environ
    raise AttributeError(f"module {__name__!r} has no attribute {name!r}")


def set_vllm_use_v1(use_v1: bool):
    if is_set("VLLM_USE_V1"):
        raise ValueError(
            "Should not call set_vllm_use_v1() if VLLM_USE_V1 is set "
            "explicitly by the user. Please raise this as a Github "
            "Issue and explicitly set VLLM_USE_V1=0 or 1.")
    os.environ["VLLM_USE_V1"] = "1" if use_v1 else "0"


# llm_engine.py
if envs.is_set("VLLM_USE_V1") and envs.VLLM_USE_V1:
    from vllm.v1.engine.llm_engine import LLMEngine as V1LLMEngine
    LLMEngine = V1LLMEngine  # type: ignore


# arg_utils.py
def create_engine_config(...):
    # * If VLLM_USE_V1 is unset, we enable V1 for "supported features"
    #   and fall back to V0 for experimental or unsupported features.
    # * If VLLM_USE_V1=1, we enable V1 for supported + experimental
    #   features and raise error for unsupported features.
    # * If VLLM_USE_V1=0, we disable V1.
    use_v1 = False
    try_v1 = envs.VLLM_USE_V1 or not envs.is_set("VLLM_USE_V1")
    if try_v1 and self._is_v1_supported_oracle(model_config):
        use_v1 = True
    
    # If user explicitly set VLLM_USE_V1, sanity check we respect it.
    if envs.is_set("VLLM_USE_V1"):
        assert use_v1 == envs.VLLM_USE_V1
    # Otherwise, set the VLLM_USE_V1 variable globally.
    else:
        envs.set_vllm_use_v1(use_v1)
```

如果没有人为在系统中指定 `VLLM_USE_V1`，则需要在代码中执行 `os.environ["VLLM_USE_V1"] = "xxx"`。

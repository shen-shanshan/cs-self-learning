# vLLM

- [GitHub](https://github.com/vllm-project/vllm)
- [Documentation](https://docs.vllm.ai/en/stable/index.html)

## Contribution Guidelines

Using `-s` with `git commit` will automatically add a `Signed-off-by:` header which certifies agreement with the terms of the DCO.

格式化：`./format.sh`。

## Installation

Python: 3.9 – 3.12

```bash
# (Recommended) Create a new conda environment.
conda create -n vllm python=3.10 -y
conda activate vllm

# Install vLLM with CUDA 12.1.
pip install vllm

pip install -r requirements-npu.txt

# Python-only build (without compilation)
VLLM_USE_PRECOMPILED=1 pip install --editable .
```

## Build

[Python-only build (without compilation)](https://docs.vllm.ai/en/latest/getting_started/installation.html#python-only-build-without-compilation)

## Test

```bash
# linting and formatting
bash format.sh
# Static type checking
mypy
# Unit tests
pytest tests/
```

## NPU PR

[Add Ascend NPU backend](https://github.com/vllm-project/vllm/pull/8054)

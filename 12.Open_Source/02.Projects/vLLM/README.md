# vLLM

## 参考资料

- [GitHub](https://github.com/vllm-project/vllm)
- [Documentation](https://docs.vllm.ai/en/stable/index.html)
- [Contributing to vLLM](https://docs.vllm.ai/en/stable/contributing/overview.html)
- [Pull Requests & Code Reviews](https://docs.vllm.ai/en/latest/contributing/overview.html#pull-requests-code-reviews)
- [Installation](https://docs.vllm.ai/en/latest/getting_started/installation.html#build-from-source)

## Contribution Guidelines

Using `-s` with `git commit` will automatically add a `Signed-off-by:` header which certifies agreement with the terms of the DCO.

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

## NPU PR

[Add Ascend NPU backend](https://github.com/vllm-project/vllm/pull/8054)

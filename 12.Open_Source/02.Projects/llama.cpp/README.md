# README

## 环境准备

### Build CANN

```bash
#!/bin/bash

clear

if [ -d "./build" ];then
    rm -rf build
    echo "delete dir 'build' finished."
else
    echo "dir 'build' doesn't exit."
fi

cmake -B build -DGGML_CANN=on -DCMAKE_BUILD_TYPE=release
cmake --build build --config release

echo "build llama.cpp finished."
```

### 模型下载

```bash
pip install modelscope
modelscope download --model Qwen/Qwen1.5-0.5B README.md --local_dir ./dir

modelscope download --model Qwen/Qwen2.5-7B-Instruct --local_dir ./my_models/qwen2.5-7b-instruct

modelscope download --model Embedding-GGUF/gte-Qwen2-1.5B-instruct-Q8_0-GGUF --local_dir ./my_models/
modelscope download --model Qwen/Qwen2.5-1.5B-Instruct-GGUF --local_dir ./my_models/qwen2.5-1.5b/ qwen2.5-1.5b-instruct-q8_0.gguf

huggingface-cli download --resume-download Qwen/Qwen2.5-7B-Instruct-GGUF --local-dir ./my_models/qwen2.5-7b-instruct-gguf
```

### 模型转换

```bash
conda create --name llamacpp python=3.10
conda activate llamacpp
python3 -m pip install -r requirements.txt

# convert the model to ggml FP16 format
python3 convert_hf_to_gguf.py my_models/qwen2.5-7b-instruct/

# [Optional] quantize the model to 4-bits (using Q4_K_M method)
./llama-quantize ./my_models/ggml-model-f16.gguf ./my_models/ggml-model-Q4_K_M.gguf Q4_K_M

./llama-quantize ./my_models/qwen2.5-7b-instruct-fp16.gguf ./my_models/ggml-model-Q8_K_M.gguf q8_0
```

### Test

```bash
./build/bin/llama-cli -m <PATH_TO_MODEL> -p "Building a website can be done in 10 steps:" -ngl 32

./build/bin/llama-cli -m ./my_models/qwen1.5-1.8b/My_Models-1.8B-F16.gguf -p "程序员天天加班，怎么保持健康的身体:" -ngl 32

# 用这个：
./build/bin/llama-cli -m ./my_models/qwen2.5-7b-instruct-fp16.gguf -p "Building a website can be done in 10 steps:" -ngl 32

./build/bin/llama-cli -m ./my_models/qwen2.5-7b-instruct/qwen2.5-7B-instruct-F16.gguf -p "Building a website can be done in 10 steps:" -ngl 32
```

## 问题

### 模型胡言乱语

模型是没问题的，cann.patch 改动的东西有问题。

## 参考资料

- [llama.cpp GitHub](https://github.com/ggerganov/llama.cpp)
- [Build llama.cpp locally](https://github.com/shen-shanshan/llama.cpp/blob/master/docs/build.md)
- [quantize](https://github.com/shen-shanshan/llama.cpp/blob/master/examples/quantize/README.md)
- [llama.cpp/examples/main](https://github.com/shen-shanshan/llama.cpp/blob/master/examples/main/README.md)
- [CONTRIBUTING](https://github.com/shen-shanshan/llama.cpp/blob/master/CONTRIBUTING.md)
- [llama.cpp for CANN](https://github.com/shen-shanshan/llama.cpp/blob/master/docs/backend/CANN.md)

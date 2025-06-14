```bash
--------------------------------------------------------------------------------
# vllm test
VLLM_USE_V1=0 pytest tests
VLLM_USE_V1=0 pytest -sv tests

# Model path
/home/sss/.cache/modelscope/hub/Qwen/Qwen2.5-0.5B-Instruct
/home/sss/cache/modelscope/models/Qwen/Qwen2.5-7B-Instruct
/home/sss/cache/modelscope/models/deepseek-ai/DeepSeek-V2-Lite-Chat

# Online test
vllm serve Qwen/Qwen2.5-0.5B-Instruct --max_model_len 26240
vllm serve /home/sss/cache/modelscope/models/Qwen/Qwen2.5-7B-Instruct --max_model_len 26240

curl http://localhost:8000/v1/completions \
    -H "Content-Type: application/json" \
    -d '{
        "model": "/home/sss/cache/modelscope/models/Qwen/Qwen2.5-7B-Instruct",
        "prompt": "Hello, my name is",
        "max_tokens": 7,
        "temperature": 0
    }'

# Clear process
ps -ef | grep vllm | cut -c 9-16 | xargs kill -9
ps -ef | grep python | cut -c 9-16 | xargs kill -9

# vllm-ascend format
yapf -i <file>
isort <file>  # to solve: Imports are incorrectly sorted and/or formatted.

vllm_ascend/models/qwen3_moe.py
vllm_ascend/ops/fused_moe.py

--------------------------------------------------------------------------------
# Git 常用操作
git chekcout <commit>
git stash
git stash pop
git stash drop [stash_id]
git stash clear
git clone -b 分支名 仓库地址
git cherry-pick <commitHash>

vim ~/.gitconfig  # 修改 [alias] section.
# 快速下载 PR
pr = "!f() { git fetch -fu ${2:-$(git remote |grep ^upstream || echo origin)} refs/pull/$1/head:pr/$1 && git checkout pr/$1; }; f"
# 快速将 Pull Request ID 为 736 的代码下载到本地
git pr 736
# 参考链接：https://github.com/Yikun/yikun.github.com/issues/89

git config --global user.email "467638484@qq.com"
git config --global user.name "shen-shanshan"

--------------------------------------------------------------------------------
# Docker 常用操作
# cd /data/disk3/sss/docker
docker-compose -p sss up -d
docker exec -it <容器名或ID> /bin/bash
docker stop <容器名或ID>
docker restart <容器名或ID>
docker commit <容器名或ID> <镜像名>
docker rm <容器名或ID>

docker run --rm \
--name vllm-ascend-sss \
--device /dev/davinci6 \
--device /dev/davinci_manager \
--device /dev/devmm_svm \
--device /dev/hisi_hdc \
-v /usr/local/dcmi:/usr/local/dcmi \
-v /usr/local/bin/npu-smi:/usr/local/bin/npu-smi \
-v /usr/local/Ascend/driver/lib64/:/usr/local/Ascend/driver/lib64/ \
-v /usr/local/Ascend/driver/version.info:/usr/local/Ascend/driver/version.info \
-v /etc/ascend_install.info:/etc/ascend_install.info \
-v /root/.cache:/root/.cache \
-it m.daocloud.io/quay.io/ascend/vllm-ascend:v0.7.3 bash

docker exec -it vllm-ascend-sss /bin/bash

--------------------------------------------------------------------------------
# V1
VLLM_USE_V1=

if __name__ == "__main__":

Co-authored-by: didongli182 <didongli@huawei.com>
Co-authored-by: zouyida2002 <didongli@huawei.com>

--------------------------------------------------------------------------------
# Structured output
pytest -sv tests/v1/entrypoints/llm/test_struct_output_generate.py::test_structured_output

/root/.cache/modelscope/hub/models/Qwen/Qwen2.5-0.5B-Instruct

vllm serve /root/.cache/modelscope/hub/models/Qwen/Qwen2.5-0.5B-Instruct \
--max_model_len 26240 \
--pipeline-parallel-size 2

curl http://localhost:8000/v1/completions \
    -H "Content-Type: application/json" \
    -d '{
        "model": "/root/.cache/modelscope/hub/models/Qwen/Qwen2.5-0.5B-Instruct",
        "prompt": "Hello, my name is",
        "max_tokens": 7,
        "temperature": 0
    }'

LLM-Research/Meta-Llama-3.1-8B-Instruct
vllm-ascend/EAGLE-LLaMA3.1-Instruct-8B

--------------------------------------------------------------------------------
# 系统配置
apt-get update
apt-get install sudo
# install curl
sudo apt update
sudo apt upgrade
sudo apt install curl

source /home/sss/Ascend/ascend-toolkit/set_env.sh
source /home/sss/Ascend/nnal/atb/set_env.sh

source /usr/local/Ascend/ascend-toolkit/set_env.sh
source /usr/local/Ascend/nnal/atb/set_env.sh

--------------------------------------------------------------------------------
export USE_OPTIMIZED_MODEL=0

vllm serve /home/sss/.cache/modelscope/hub/models/Qwen/Qwen2___5-VL-7B-Instruct \
--dtype bfloat16 \
--max_model_len 32768 \
--max-num-batched-tokens 32768

curl http://localhost:8000/v1/chat/completions \
    -H "Content-Type: application/json" \
    -d '{
    "model": "/home/sss/.cache/modelscope/hub/models/Qwen/Qwen2___5-VL-7B-Instruct",
    "messages": [
    {"role": "system", "content": "You are a helpful assistant."},
    {"role": "user", "content": [
        {"type": "image_url", "image_url": {"url": "https://modelscope.oss-cn-beijing.aliyuncs.com/resource/qwen.png"}},
        {"type": "text", "text": "What is the text in the illustrate?"}
    ]}
    ]
    }'

--------------------------------------------------------------------------------
# vllm 环境变量
VLLM_USE_V1
VLLM_USE_MODELSCOPE

--------------------------------------------------------------------------------
# 开源社区常用话术
The CI is finally passed and this PR can be merged.
I have rebased on the latest main and nothing changed.
# 常用符号
🎯

--------------------------------------------------------------------------------
# SSH
ssh user@IP -p port
# 将 id_rsa.pub 公钥放在服务器上的 authorized_keys 文件中
vim /root/.ssh/authorized_keys

--------------------------------------------------------------------------------
# VSCode 快捷键
折叠所有：Ctrl/Cmd + K + 0
展开所有：Ctrl/Cmd + K + J

--------------------------------------------------------------------------------
# Spec Decode
tests/long_term/spec_decode/e2e/test_v1_spec_decode.py::test_ngram_correctness

--------------------------------------------------------------------------------
```

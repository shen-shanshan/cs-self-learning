# MEMO

## Linux

```bash
--------------------------------------------------------------------------------
# 安装 sudo
apt-get update
apt-get install sudo

# 安装 curl
sudo apt update
sudo apt upgrade
sudo apt install curl
--------------------------------------------------------------------------------
# 将自己的 SSH 公钥配置到远程服务器上
ssh user@IP -p port
# 将 id_rsa.pub 公钥放在服务器上的 authorized_keys 文件中
vim /root/.ssh/authorized_keys

# 安装 SSH 服务
sudo apt update
sudo apt install openssh-client

# 将 SSH 公钥配置到 GitHub 上
ssh-keygen -t ed25519 -C "467638484@qq.com"
eval "$(ssh-agent -s)"
ssh-add ~/.ssh/id_ed25519
cat ~/.ssh/id_ed25519.pub  # Add it to your github
--------------------------------------------------------------------------------
```

## Docker

```bash
--------------------------------------------------------------------------------
# 启动容器（Ascend 01 and 02）
cd /data/disk3/sss/docker
docker-compose -p sss up -d
--------------------------------------------------------------------------------
# 常用命令
docker exec -it <容器名或ID> /bin/bash
docker stop <容器名或ID>
docker restart <容器名或ID>
docker commit <容器名或ID> <镜像名>
docker rm <容器名或ID>
--------------------------------------------------------------------------------
```

## Git

```bash
--------------------------------------------------------------------------------
# 基本配置
git config --global user.email "467638484@qq.com"
git config --global user.name "shen-shanshan"
--------------------------------------------------------------------------------
# 常用命令
git chekcout <commit>
git stash
git stash pop
git stash drop [stash_id]
git stash clear
git clone -b 分支名 仓库地址
git cherry-pick <commitHash>
--------------------------------------------------------------------------------
# 一键下载 PR
vim ~/.gitconfig
# 修改 [alias] section
pr = "!f() { git fetch -fu ${2:-$(git remote |grep ^upstream || echo origin)} refs/pull/$1/head:pr/$1 && git checkout pr/$1; }; f"
# 快速将 Pull Request ID 为 736 的代码下载到本地
git pr 736
# 参考链接：https://github.com/Yikun/yikun.github.com/issues/89
--------------------------------------------------------------------------------
# 安装 git-lfs
curl -s https://packagecloud.io/install/repositories/github/git-lfs/script.deb.sh | sudo bash
sudo apt-get install git-lfs
git lfs install
# 官方网站：https://git-lfs.com/
--------------------------------------------------------------------------------
```

## Pip

```bash
--------------------------------------------------------------------------------
pip config set global.index-url https://mirrors.tuna.tsinghua.edu.cn/pypi/web/simple
--------------------------------------------------------------------------------
```

## CANN

```bash
--------------------------------------------------------------------------------
# set env
source /home/sss/Ascend/ascend-toolkit/set_env.sh
source /home/sss/Ascend/nnal/atb/set_env.sh
source /usr/local/Ascend/ascend-toolkit/set_env.sh
source /usr/local/Ascend/nnal/atb/set_env.sh

# show env
cat /home/sss/Ascend/ascend-toolkit/latest/aarch64-linux/ascend_toolkit_install.info
--------------------------------------------------------------------------------
```

## Model

```bash
--------------------------------------------------------------------------------
/home/sss/.cache/modelscope/hub/Qwen/Qwen2.5-0.5B-Instruct
/home/sss/cache/modelscope/models/Qwen/Qwen2.5-7B-Instruct
/home/sss/cache/modelscope/models/deepseek-ai/DeepSeek-V2-Lite-Chat
/home/sss/.cache/modelscope/hub/models/LLM-Research/Meta-Llama-3.1-8B-Instruct
/root/.cache/modelscope/hub/models/Qwen/Qwen2.5-0.5B-Instruct
--------------------------------------------------------------------------------
```

## vLLM

```bash
--------------------------------------------------------------------------------
# 环境变量
VLLM_USE_V1=xxx
VLLM_USE_MODELSCOPE=xxx
--------------------------------------------------------------------------------
# vllm-ascend format
yapf -i <file>
isort <file>
--------------------------------------------------------------------------------
# Clear process
ps -ef | grep vllm | cut -c 9-16 | xargs kill -9
ps -ef | grep python | cut -c 9-16 | xargs kill -9
--------------------------------------------------------------------------------
# Structured Output
pytest -sv \
tests/v1/entrypoints/llm/test_struct_output_generate.py::test_structured_output

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
--------------------------------------------------------------------------------
# Spec Decode
pytest -sv \
tests/long_term/spec_decode/e2e/test_v1_spec_decode.py::test_ngram_correctness

VLLM_USE_V1=0 pytest -sv \
tests/e2e/long_term/spec_decode/e2e/test_ngram_correctness.py::test_ngram_e2e_greedy_correctness

# Eagel Model
LLM-Research/Meta-Llama-3.1-8B-Instruct
vllm-ascend/EAGLE-LLaMA3.1-Instruct-8B
--------------------------------------------------------------------------------
```

## Open Source

```bash
--------------------------------------------------------------------------------
# 开源社区常用话术
The CI is finally passed and this PR can be merged.
I have rebased on the latest main and nothing changed.

# 常用符号
🎯
--------------------------------------------------------------------------------
```

## VSCode

```bash
--------------------------------------------------------------------------------
# 常用快捷键
折叠所有：Ctrl/Cmd + K + 0
展开所有：Ctrl/Cmd + K + J
--------------------------------------------------------------------------------
```

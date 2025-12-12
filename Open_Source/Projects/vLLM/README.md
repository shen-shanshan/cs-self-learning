# vLLM

## 环境搭建

### 通用环境配置

参考：[link](../../../AI/AI_Infra/Environment_Preparation/scripts/init_env_coder.sh)

Coder 配置：

```bash
# 恢复默认 bash 配置文件
cp /etc/skel/.bash_logout /etc/skel/.bashrc /etc/skel/.profile ~/
```

### NPU 环境搭建

参考：[link](../../../AI/AI_Infra/Environment_Preparation/README.md)

### GPU 环境搭建（A100 为例）

查看驱动版本：

```bash
nvidia-smi
+-----------------------------------------------------------------------------------------+
| NVIDIA-SMI 550.163.01             Driver Version: 550.163.01     CUDA Version: 12.4     |
|-----------------------------------------+------------------------+----------------------+
| GPU  Name                 Persistence-M | Bus-Id          Disp.A | Volatile Uncorr. ECC |
| Fan  Temp   Perf          Pwr:Usage/Cap |           Memory-Usage | GPU-Util  Compute M. |
|                                         |                        |               MIG M. |
|=========================================+========================+======================|
|   0  NVIDIA A100-SXM4-80GB          On  |   00000000:9D:00.0 Off |                    0 |
| N/A   33C    P0             61W /  400W |       1MiB /  81920MiB |      0%      Default |
|                                         |                        |             Disabled |
+-----------------------------------------+------------------------+----------------------+
                                                                                         
+-----------------------------------------------------------------------------------------+
| Processes:                                                                              |
|  GPU   GI   CI        PID   Type   Process name                              GPU Memory |
|        ID   ID                                                               Usage      |
|=========================================================================================|
|  No running processes found                                                             |
+-----------------------------------------------------------------------------------------+
```

查看系统 Ubuntu 版本：

```bash
cat /etc/os-release
# PRETTY_NAME="Ubuntu 24.04.2 LTS"
```

安装 CUDA Toolkit 12.4：

```bash
cd ~
mkdir cuda
cd cuda

# Download Installer for Linux Ubuntu 22.04 x86_64
wget https://developer.download.nvidia.com/compute/cuda/12.4.0/local_installers/cuda_12.4.0_550.54.14_linux.run
sudo sh cuda_12.4.0_550.54.14_linux.run
# 摁一下空格取消 Driver 安装，然后选择 Install

# 默认安装到了 /usr/local/cuda-12.4/，这里我移动到了自己的家目录下
cd ~
mkdir cuda-12.4
sudo mv /usr/local/cuda-12.4/* ~/cuda-12.4/

# 12.9
wget https://developer.download.nvidia.com/compute/cuda/12.9.0/local_installers/cuda_12.9.0_575.51.03_linux.run
sudo sh cuda_12.9.0_575.51.03_linux.run
```

配置环境变量：

```bash
vim ~/.bashrc
export CUDA_HOME=/home/sss/cuda-12.4
export PATH=$PATH:$CUDA_HOME/bin
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$CUDA_HOME/lib64
# export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$CUDA_HOME/extras/CUPTI/lib64

alias gpus='nvidia-smi'

# 使配置生效
source ~/.bashrc
```

验证安装是否成功：

```bash
nvcc --version
# nvcc: NVIDIA (R) Cuda compiler driver
# Copyright (c) 2005-2024 NVIDIA Corporation
# Built on Tue_Feb_27_16:19:38_PST_2024
# Cuda compilation tools, release 12.4, V12.4.99
# Build cuda_12.4.r12.4/compiler.33961263_0
```

安装 uv：[official doc](https://docs.astral.sh/uv/#installation)

```bash
curl -LsSf https://astral.sh/uv/install.sh | sh

# check installation
uv
```

安装 vllm：[official doc](https://docs.vllm.ai/en/stable/getting_started/installation/gpu/)

```bash
cd ~
mkdir github
cd github
git clone git@github.com:shen-shanshan/vllm.git
cd vllm

# Set up using Python-only build (without compilation)
git remote add upstream git@github.com:vllm-project/vllm.git
git sync

export UV_INDEX_URL="https://pypi.tuna.tsinghua.edu.cn/simple"
export UV_HTTP_TIMEOUT=1000000000

uv venv --python 3.12 --seed -v
source .venv/bin/activate

uv pip install pre-commit -i https://mirrors.tuna.tsinghua.edu.cn/pypi/web/simple
pre-commit install

uv pip install "modelscope>=1.18.1" -i https://mirrors.tuna.tsinghua.edu.cn/pypi/web/simple

# VLLM_USE_PRECOMPILED=1 uv pip install \
# --editable . \
# --extra-index-url https://download.pytorch.org/whl/cu124 \
# --index-strategy unsafe-best-match \
# --prerelease=allow

# -i https://pypi.tuna.tsinghua.edu.cn/simple \
# VLLM_USE_PRECOMPILED=1 uv pip install --editable . --extra-index-url https://download.pytorch.org/whl/cu124 --index-strategy unsafe-best-match --prerelease=allow

# vLLM supports different CUDA versions but you need to install pytorch that corresponds to your CUDA version and then build vLLM from source.
uv pip install -v torch torchvision torchaudio \
--index-url https://download.pytorch.org/whl/cu124  # cu121 与 CUDA 12.4 runtime 完全兼容

# Install vllm
VLLM_USE_PRECOMPILED=1 uv pip install -v --editable . \
--extra-index-url https://download.pytorch.org/whl/cu124 \
--index-strategy unsafe-best-match \
--prerelease=allow

# --index-url https://mirrors.aliyun.com/pypi/simple \
# --extra-index-url https://download.pytorch.org/whl/cu124 \

VLLM_USE_PRECOMPILED=1 uv pip install -v --editable . --extra-index-url https://download.pytorch.org/whl/cu124 --index-strategy unsafe-best-match --prerelease=allow -i https://pypi.tuna.tsinghua.edu.cn/simple

# 秒装 cuda pytorch
uv pip install torch==2.9.0 \
-i https://mirrors.tuna.tsinghua.edu.cn/pypi/web/simple \
-f https://mirrors.aliyun.com/pytorch-wheels/cu124

# https://github.com/vllm-project/vllm/issues/30464
VLLM_USE_PRECOMPILED=1 uv pip install -v --editable . \
-i https://mirrors.tuna.tsinghua.edu.cn/pypi/web/simple \
torch==2.9.0 -f https://mirrors.aliyun.com/pytorch-wheels/cu124 \
--index-strategy unsafe-best-match \
--prerelease=allow
```

RemoteForward 7890 127.0.0.1:7890
export https_proxy=http://127.0.0.1:7890 http_proxy=http://127.0.0.1:7890 all_proxy=socks5://127.0.0.1:7890
ssh coder.sss-gpu-a100.main
curl https://google.com -I

```bash
# 本地运行：
ssh -D 1080 shanshan-shen@remote-server
# 然后服务器配置：
export all_proxy="socks5://127.0.0.1:1080"


# 在远程服务器执行：
ssh -R 9999:localhost:7890 user@你的本地IP
# 服务器上设置代理：
export http_proxy="http://127.0.0.1:9999"
export https_proxy="http://127.0.0.1:9999"
export all_proxy="http://127.0.0.1:9999"

ssh -R 9999:localhost:7890 shanshan-shen@141.11.146.70
ssh -R 9999:localhost:7890 shanshan-shen@7.249.188.223
ssh shanshan-shen@7.249.188.223
```

# 开发环境搭建

## 通用环境配置

`vim ~/.bashrc`：

```bash
# vllm:
export VLLM_USE_V1=1
export VLLM_WORKER_MULTIPROC_METHOD=spawn
export VLLM_USE_MODELSCOPE=True
# pytorch:
export PYTORCH_NPU_ALLOC_CONF=max_split_size_mb:256
# huggingface:
export HF_ENDPOINT=https://hf-mirror.com

# cann:
source /home/sss/Ascend/ascend-toolkit/set_env.sh
source /home/sss/Ascend/nnal/atb/set_env.sh
```

`vim ~/.gitconfig`:

```bash
[user]
email = 467638484@qq.com
name = shen-shanshan
[alias]
pr = "!f() { git fetch -fu ${2:-$(git remote |grep ^upstream || echo origin)} refs/pull/$1/head:pr/$1 && git checkout pr/$1; }; f"
# 检查配置：git config --list
```

`vim ~/.pip/pip.conf`:

```bash
[global]
index-url = https://pypi.tuna.tsinghua.edu.cn/simple
[install]
trusted-host = https://pypi.tuna.tsinghua.edu.cn
```

配置 github ssh 密钥：

```bash
# 若未安装 ssh-keygen：
sudo apt update
sudo apt install openssh-client

ssh-keygen -t ed25519 -C "467638484@qq.com"
eval "$(ssh-agent -s)"
ssh-add ~/.ssh/id_ed25519
cat ~/.ssh/id_ed25519.pub  # Add it to your github
```

参考资料：

- https://docs.github.com/en/authentication/connecting-to-github-with-ssh/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent
- https://docs.github.com/en/authentication/connecting-to-github-with-ssh/adding-a-new-ssh-key-to-your-github-account

## 安装 miniconda

```bash
# 查看操作系统：
# uname -i 或者 -r 或者 -a

wget https://repo.anaconda.com/miniconda/Miniconda3-latest-Linux-aarch64.sh
bash Miniconda3-latest-Linux-aarch64.sh
```

## 安装 CANN

```bash
# Install required python packages.
pip3 install -i https://pypi.tuna.tsinghua.edu.cn/simple attrs 'numpy<2.0.0' decorator sympy cffi pyyaml pathlib2 psutil protobuf scipy requests absl-py wheel typing_extensions

# Download and install the CANN package.
wget --header="Referer: https://www.hiascend.com/" https://ascend-repo.obs.cn-east-2.myhuaweicloud.com/CANN/CANN%208.1.RC1/Ascend-cann-toolkit_8.1.RC1_linux-"$(uname -i)".run
chmod +x ./Ascend-cann-toolkit_8.1.RC1_linux-"$(uname -i)".run
./Ascend-cann-toolkit_8.1.RC1_linux-"$(uname -i)".run --full

source /usr/local/Ascend/ascend-toolkit/set_env.sh

wget --header="Referer: https://www.hiascend.com/" https://ascend-repo.obs.cn-east-2.myhuaweicloud.com/CANN/CANN%208.1.RC1/Ascend-cann-kernels-910b_8.1.RC1_linux-"$(uname -i)".run
chmod +x ./Ascend-cann-kernels-910b_8.1.RC1_linux-"$(uname -i)".run
./Ascend-cann-kernels-910b_8.1.RC1_linux-"$(uname -i)".run --install

wget --header="Referer: https://www.hiascend.com/" https://ascend-repo.obs.cn-east-2.myhuaweicloud.com/CANN/CANN%208.1.RC1/Ascend-cann-nnal_8.1.RC1_linux-"$(uname -i)".run
chmod +x ./Ascend-cann-nnal_8.1.RC1_linux-"$(uname -i)".run
./Ascend-cann-nnal_8.1.RC1_linux-"$(uname -i)".run --install

source /usr/local/Ascend/nnal/atb/set_env.sh

# 查看 cann 环境：
cat /home/sss/Ascend/ascend-toolkit/latest/aarch64-linux/ascend_toolkit_install.info
# package_name=Ascend-cann-toolkit
# version=8.1.RC1
# innerversion=V100R001C21SPC001B238
# compatible_version=[V100R001C15],[V100R001C18],[V100R001C19],[V100R001C20],[V100R001C21]
# arch=aarch64
# os=linux
# path=/home/sss/Ascend/ascend-toolkit/8.1.RC1/aarch64-linux
```

## 安装 vllm & vllm-ascend

```bash
# Install vLLM
git clone git@github.com:shen-shanshan/vllm.git
cd vllm
VLLM_TARGET_DEVICE=empty pip install -v -e .
cd ..

# Install vLLM Ascend
git clone git@github.com:shen-shanshan/vllm-ascend.git
cd vllm-ascend
export PIP_EXTRA_INDEX_URL=https://mirrors.huaweicloud.com/ascend/repos/pypi
# disable build custom ops
# export COMPILE_CUSTOM_KERNELS=0
pip install -v -e .
cd ..
```

#!/usr/bin/env bash

# do this in host machine:
# sudo chown -R 1000:1000 /home/sss


# install packages
apt-get update
apt-get install sudo
sudo apt install curl -y
sudo apt install openssh-client -y
sudo apt install openssh-server -y
sudo apt-get install python3-dev python3-venv python3-pip -y

# config git
echo "[user]" >> ~/.gitconfig
echo "        email = 467638484@qq.com" >> ~/.gitconfig
echo "        name = shen-shanshan" >> ~/.gitconfig
echo "[alias]" >> ~/.gitconfig
echo '        pr = "!f() { git fetch -fu ${2:-$(git remote |grep ^upstream || echo origin)} refs/pull/$1/head:pr/$1 && git checkout pr/$1; }; f"' >> ~/.gitconfig
echo '        sync = "!f() { git fetch upstream && git rebase upstream/main; }; f"' >> ~/.gitconfig
echo '        nb = "!f() { git fetch upstream && git checkout -b $1 upstream/main && git branch; }; f"' >> ~/.gitconfig
echo '        db = "!f() { git branch -D $1 && git branch; }; f"' >> ~/.gitconfig
echo '        cb = "!f() { git checkout $1 ; }; f"' >> ~/.gitconfig


# config pip
pip config set global.index-url https://pypi.tuna.tsinghua.edu.cn/simple


# config vim
echo "set number" >> ~/.vimrc


# config github ssh
ssh-keygen -t ed25519 -C "467638484@qq.com"
eval "$(ssh-agent -s)"
ssh-add ~/.ssh/id_ed25519
echo "-------------------------------------------------------------------------"
cat ~/.ssh/id_ed25519.pub  # Add it to your github
echo "-------------------------------------------------------------------------"
## config ssh server
sudo echo "Port 22" >> /etc/ssh/sshd_config
sudo echo "PermitRootLogin yes" >> /etc/ssh/sshd_config
sudo /etc/init.d/ssh restart
echo "-------------------------------------------------------------------------"
ps -ef | grep sshd
echo "-------------------------------------------------------------------------"
# ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIND8yv+WNiwbHYxZnexetSUo067m6hfRyB9jBrfEQndq 467638484@qq.com

# install nvcc
cd /shared/sss/
mkdir cuda
cd cuda
sudo wget https://developer.download.nvidia.com/compute/cuda/12.4.0/local_installers/cuda_12.4.0_550.54.14_linux.run
sudo sh cuda_12.4.0_550.54.14_linux.run
# 摁一下空格取消 Driver 安装，然后选择 Install
# 默认安装到了 /usr/local/cuda-12.4/，这里我移动到了自己的家目录下
cd /shared/sss/
mkdir cuda-12.4
sudo mv /usr/local/cuda-12.4/* /shared/sss/cuda-12.4/
# 配置环境变量
sudo vim ~/.bashrc
export CUDA_HOME=/shared/sss/cuda-12.4
export PATH=$PATH:$CUDA_HOME/bin
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$CUDA_HOME/lib64
alias gpus='nvidia-smi'
alias watch-gpus='watch -n 1 nvidia-smi'
source ~/.bashrc
# 验证安装是否成功
echo "-------------------------------------------------------------------------"
nvcc --version
echo "-------------------------------------------------------------------------"


# 安装 uv
sudo curl -LsSf https://astral.sh/uv/install.sh | sh
source $HOME/.local/bin/env
# check installation
echo "-------------------------------------------------------------------------"
uv
echo "-------------------------------------------------------------------------"
export UV_CACHE_DIR=/shared/uv


# install vllm
cd /shared/sss/github/vllm
export UV_INDEX_URL="https://pypi.tuna.tsinghua.edu.cn/simple"
export UV_HTTP_TIMEOUT=1000000000
export UV_CACHE_DIR=/shared/uv
# export UV_LINK_MODE=copy
uv venv --python 3.12 --seed -v
source .venv/bin/activate

uv pip install pre-commit -i https://mirrors.tuna.tsinghua.edu.cn/pypi/web/simple
pre-commit install

UV_HTTP_TIMEOUT=300 UV_MAX_CONCURRENCY=32 \
uv pip install torch==2.10.0 torchaudio==2.10.0 torchvision==0.25.0 \
-i https://mirrors.tuna.tsinghua.edu.cn/pypi/web/simple \
-f https://mirrors.aliyun.com/pytorch-wheels/cu124

uv pip install "cmake>=3.26.1" "ninja" "packaging>=24.2" "setuptools>=77.0.3, <81.0.0" "setuptools-scm>=8.0" "wheel" "jinja2" \
-i https://mirrors.tuna.tsinghua.edu.cn/pypi/web/simple \
-f https://mirrors.aliyun.com/pytorch-wheels/cu124

# Install requirements
uv pip install -r requirements/common.txt \
--index-url https://pypi.tuna.tsinghua.edu.cn/simple \
--extra-index-url https://download.pytorch.org/whl/cu124

uv pip install -r requirements/cuda.txt \
--index-url https://pypi.tuna.tsinghua.edu.cn/simple \
--extra-index-url https://download.pytorch.org/whl/cu124

uv pip install -r benchmarks/multi_turn/requirements.txt \
--index-url https://pypi.tuna.tsinghua.edu.cn/simple \
--extra-index-url https://download.pytorch.org/whl/cu124

VLLM_USE_PRECOMPILED=1 uv pip install -v --editable . \
--index-url https://download.pytorch.org/whl/cu124 \
--index-strategy unsafe-best-match \
--prerelease=allow \
--no-build-isolation


# clear memory
df -h
du -h . | sort -hr | head -20
# sudo mv /home/sss/host_data/* /shared/sss/
# sudo rsync -a --whole-file --info=progress2 /home/sss/host_data/github/* /shared/sss/github/
# sudo rsync -av --progress /shared/sss/cuda-12.4/* /home/sss/host_data/cuda-12.4/
# rsync -a --delete
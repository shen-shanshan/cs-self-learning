#!/usr/bin/env bash


# install packages
apt-get update
apt-get install sudo
sudo apt install curl
sudo apt install openssh-client -y
sudo apt install openssh-server -y


# for pre-commit install
sudo apt install golang-go -y
go version
go env -w GOPROXY=https://goproxy.cn,direct


# config env var
echo "export VLLM_USE_V1=1" >> ~/.bashrc
echo "export VLLM_WORKER_MULTIPROC_METHOD=spawn" >> ~/.bashrc
echo "export VLLM_USE_MODELSCOPE=True" >> ~/.bashrc
echo "export PYTORCH_NPU_ALLOC_CONF=max_split_size_mb:256" >> ~/.bashrc
echo "export PIP_EXTRA_INDEX_URL=https://mirrors.huaweicloud.com/ascend/repos/pypi" >> ~/.bashrc
echo "export LD_LIBRARY_PATH=/usr/local/Ascend/ascend-toolkit/latest/aarch64-linux/devlib:$LD_LIBRARY_PATH" >> ~/.bashrc
echo "export LD_LIBRARY_PATH=/usr/local/Ascend/driver/lib64/driver:$LD_LIBRARY_PATH" >> ~/.bashrc


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
# mkdir ~/.pip
# echo "[global]" >> ~/.pip/pip.conf
# echo "        index-url = https://pypi.tuna.tsinghua.edu.cn/simple" >> ~/.pip/pip.conf
# echo "[install]" >> ~/.pip/pip.conf
# echo "        trusted-host = https://pypi.tuna.tsinghua.edu.cn" >> ~/.pip/pip.conf
# pip config set global.index-url https://mirrors.tuna.tsinghua.edu.cn/pypi/web/simple
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
echo "Port 22" >> /etc/ssh/sshd_config
echo "PermitRootLogin yes" >> /etc/ssh/sshd_config
sudo /etc/init.d/ssh restart
echo "-------------------------------------------------------------------------"
ps -ef | grep sshd
echo "-------------------------------------------------------------------------"


# install miniconda
cd /home/sss/software/miniconda/
bash Miniconda3-latest-Linux-aarch64.sh
source ~/.bashrc
conda create -n vllm python=3.10 -y
conda activate vllm

# install vllm
cd /vllm-workspace/vllm/
git remote remove origin
git remote add origin git@github.com:shen-shanshan/vllm.git
git remote add upstream git@github.com:vllm-project/vllm.git
git sync
VLLM_TARGET_DEVICE=empty pip install -v -e .


# install vllm-ascend
cd /vllm-workspace/vllm-ascend/
git remote remove origin
git remote add origin git@github.com:shen-shanshan/vllm-ascend.git
git remote add upstream git@github.com:vllm-project/vllm-ascend.git
git sync
git fetch origin
source /usr/local/Ascend/ascend-toolkit/set_env.sh
source /usr/local/Ascend/nnal/atb/set_env.sh
pip install -v -e .
pip install -r requirements-dev.txt
pip install modelscope>=1.18.1
pre-commit install


# others
passwd  # 333

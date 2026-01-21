#!/usr/bin/env bash


# install packages
apt-get update
apt-get install sudo
sudo apt install curl -y
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
echo "export PIP_EXTRA_INDEX_URL=https://mirrors.huaweicloud.com/ascend/repos/pypi" >> ~/.bashrc
echo "export LD_LIBRARY_PATH=/usr/local/Ascend/ascend-toolkit/latest/aarch64-linux/devlib:$LD_LIBRARY_PATH" >> ~/.bashrc
echo "export LD_LIBRARY_PATH=/usr/local/Ascend/driver/lib64/driver:$LD_LIBRARY_PATH" >> ~/.bashrc
echo "export COMPILE_CUSTOM_KERNELS=1" >> ~/.bashrc
# echo "export PYTORCH_NPU_ALLOC_CONF=max_split_size_mb:256" >> ~/.bashrc
# source ~/.bashrc
# export LD_LIBRARY_PATH=/usr/local/Ascend/ascend-toolkit/latest/aarch64-linux/devlib:$LD_LIBRARY_PATH
# export LD_LIBRARY_PATH=/usr/local/Ascend/driver/lib64/driver:$LD_LIBRARY_PATH


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
# eval "$(/root/miniconda3/bin/conda shell.bash hook)"
conda create -n vllm python=3.11 -y
conda activate vllm

# install vllm
cd /vllm-workspace/vllm/
git remote remove origin
git remote add origin git@github.com:shen-shanshan/vllm.git
git remote add upstream git@github.com:vllm-project/vllm.git
git nb main
VLLM_TARGET_DEVICE=empty pip install -v -e . -i https://pypi.tuna.tsinghua.edu.cn/simple
pre-commit install


# install vllm-ascend
cd /vllm-workspace/vllm-ascend/
git remote remove origin
git remote add origin git@github.com:shen-shanshan/vllm-ascend.git
git remote add upstream git@github.com:vllm-project/vllm-ascend.git
git sync
source /usr/local/Ascend/ascend-toolkit/set_env.sh
source /usr/local/Ascend/nnal/atb/set_env.sh
# export COMPILE_CUSTOM_KERNELS=1
# export PIP_EXTRA_INDEX_URL=https://mirrors.huaweicloud.com/ascend/repos/pypi
pip install -v -e . -i https://pypi.tuna.tsinghua.edu.cn/simple
pip install -r requirements-dev.txt
pip install modelscope>=1.18.1 -i https://pypi.tuna.tsinghua.edu.cn/simple
pre-commit install


# others
passwd  # 333

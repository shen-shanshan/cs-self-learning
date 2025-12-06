#!/usr/bin/env bash


# install packages
sudo apt-get update
sudo apt install openssh-client -y


# for pre-commit
sudo apt install golang-go -y
go env -w GOPROXY=https://goproxy.cn,direct


# config env var
echo " " >> ~/.bashrc
echo "# --------------------- custom env vars ---------------------" >> ~/.bashrc
echo "export VLLM_USE_V1=1" >> ~/.bashrc
echo "export VLLM_WORKER_MULTIPROC_METHOD=spawn" >> ~/.bashrc
echo "export VLLM_USE_MODELSCOPE=True" >> ~/.bashrc


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
# ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIPrrydSf7q6RsLpFJ1APYK89iJBRkEp2M6KeBxNBWDnp 467638484@qq.com
echo "-------------------------------------------------------------------------"


# install miniconda
cd ~
mkdir miniconda
cd miniconda
# wget https://repo.anaconda.com/miniconda/Miniconda3-latest-Linux-aarch64.sh
bash Miniconda3-latest-Linux-aarch64.sh
source ~/.bashrcl
# eval "$(/root/miniconda3/bin/conda shell.bash hook)"
conda create -n vllm python=3.11 -y
conda activate vllm


# for cuda
# sudo apt remove gcc -y
# sudo apt-get install gcc-11 g++-11 -y
# sudo ln -s /usr/bin/gcc-11 /usr/bin/gcc
# sudo ln -s /usr/bin/g++-11 /usr/bin/g++
# sudo ln -s /usr/bin/gcc-11 /usr/bin/cc
# sudo ln -s /usr/bin/g++-11 /usr/bin/c++
# gcc --version

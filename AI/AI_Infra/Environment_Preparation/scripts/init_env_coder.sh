#!/usr/bin/env bash

# install packages
sudo apt-get update
sudo apt install openssh-client -y

# for pre-commit
sudo apt install golang-go -y
go env -w GOPROXY=https://goproxy.cn,direct

# to solve "Python.h: No such file or directory" for vllm
sudo apt-get install python3-dev python3-venv python3-pip -y

# for claude code
curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
sudo apt-get install -y nodejs

# for cuda 12.2
sudo apt remove gcc -y
sudo apt-get install gcc-11 g++-11 -y
sudo ln -s /usr/bin/gcc-11 /usr/bin/gcc
sudo ln -s /usr/bin/g++-11 /usr/bin/g++
sudo ln -s /usr/bin/gcc-11 /usr/bin/cc
sudo ln -s /usr/bin/g++-11 /usr/bin/c++
gcc --version

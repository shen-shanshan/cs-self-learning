#!/usr/bin/env bash

sudo apt update

# for git ssh
sudo apt install openssh-client -y

# for cuda
sudo apt remove gcc -y
sudo apt-get install gcc-11 g++-11 -y
sudo ln -s /usr/bin/gcc-11 /usr/bin/gcc
sudo ln -s /usr/bin/g++-11 /usr/bin/g++
sudo ln -s /usr/bin/gcc-11 /usr/bin/cc
sudo ln -s /usr/bin/g++-11 /usr/bin/c++
gcc --version

# for pre-commit
sudo apt install golang-go -y
go env -w GOPROXY=https://goproxy.cn,direct

#!/usr/bin/env bash

sudo apt update
sudo apt install openssh-client -y

sudo apt remove gcc -y
sudo apt-get install gcc-11 g++-11 -y
sudo ln -s /usr/bin/gcc-11 /usr/bin/gcc
sudo ln -s /usr/bin/g++-11 /usr/bin/g++
sudo ln -s /usr/bin/gcc-11 /usr/bin/cc
sudo ln -s /usr/bin/g++-11 /usr/bin/c++
gcc --version

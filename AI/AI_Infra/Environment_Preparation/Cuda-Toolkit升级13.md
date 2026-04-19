# CUDA Toolkit 升级

```bash
which nvcc
# /shared/sss/cuda-12.4/bin/nvcc

# 卸载旧 CUDA Toolkit（12.4）
cd /shared/sss/cuda-12.4/bin/
sudo ./cuda-uninstaller

# https://developer.nvidia.com/cuda-13-0-0-download-archive
wget https://developer.download.nvidia.com/compute/cuda/13.0.0/local_installers/cuda_13.0.0_580.65.06_linux.run
sudo sh cuda_13.0.0_580.65.06_linux.run
# Toolkit:  Installed in /usr/local/cuda-13.0/
# Please make sure that
#  -   PATH includes /usr/local/cuda-13.0/bin
#  -   LD_LIBRARY_PATH includes /usr/local/cuda-13.0/lib64, or, add /usr/local/cuda-13.0/lib64 to /etc/ld.so.conf and run ldconfig as root

# Move
mkdir cuda-13.0
sudo mv /usr/local/cuda-13.0/* /shared/sss/cuda-13.0/

# 配置环境变量
sudo vim ~/.bashrc
export CUDA_HOME=/shared/sss/cuda-13.0
export PATH=$PATH:$CUDA_HOME/bin
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$CUDA_HOME/lib64
source ~/.bashrc

# 验证安装是否成功
nvcc --version
```

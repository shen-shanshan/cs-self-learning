# Docker

## 构建自己的镜像并推送到 docker hub

### 1. 注册 docker hub 账号

https://www.docker.com/products/docker-hub/

我的账号：467638484
13309089096a

### 2. 构建镜像并推送到 docker hub

```bash
# 设置代理（防止 Ubuntu 镜像拉不下来）
export HTTP_PROXY=http://127.0.0.1:7890
export HTTPS_PROXY=http://127.0.0.1:7890
# 设置环境变量后再推送
export DOCKER_CLIENT_TIMEOUT=30000
export COMPOSE_HTTP_TIMEOUT=30000

docker build -t your-dockerhub-username/image-name:tag .
docker build -t your-image-name:tag .
# example: docker build -t johndoe/myapp:v1.0 .
# mine: docker build -t 467638484/sss-ubuntu-base:v1.0 .
docker build -t sss-ubuntu-base:v1.0 .

docker login
docker push your-dockerhub-username/image-name:tag
# example: docker push johndoe/myapp:v1.0
# mine: docker push 467638484/sss-ubuntu-base:v1.0
```

### 3. 环境配置

拉取镜像：

```bash
docker pull 467638484/sss-ubuntu-base:v1.0
# docker pull swr.cn-southwest-2.myhuaweicloud.com/modelfoundry/dev_images/s00845128/sss_ubuntu_base:a6ab75a
```

启动容器：

```bash
# docker run \
# --privileged=true \
# --name sss-vllm-gpu \
# --net=host \
# -e CUDA_VISIBLE_DEVICES=4,5,6,7 \
# --device /dev/nvidia4 \
# --device /dev/nvidia5 \
# --device /dev/nvidia6 \
# --device /dev/nvidia7 \
# --device /dev/davinci_manager \
# --device /dev/devmm_svm \
# --device /dev/hisi_hdc \
# -v /usr/local/dcmi:/usr/local/dcmi \
# -v /usr/local/Ascend/driver/tools/hccn_tool:/usr/local/Ascend/driver/tools/hccn_tool \
# -v /usr/local/bin/npu-smi:/usr/local/bin/npu-smi \
# -v /usr/local/Ascend/driver/lib64/:/usr/local/Ascend/driver/lib64/ \
# -v /usr/local/Ascend/driver/version.info:/usr/local/Ascend/driver/version.info \
# -v /etc/ascend_install.info:/etc/ascend_install.info \
# -v /home/sss:/home/sss \
# -v /mnt/sfs_turbo/ascend-ci-share-nv-action-vllm-benchmarks:/root/.cache \
# -p 8002:8002 \
# -e VLLM_USE_MODELSCOPE=True \
# -e PYTORCH_NPU_ALLOC_CONF=max_split_size_mb:256 \
# -it $IMAGE /bin/bash

docker run \
--privileged=true \
--name sss-vllm-gpu \
--gpus all \
-v /home/sss:/home/sss/host_data \
-v /mnt/sfs_turbo:/shared \
-p 8000:8000 \
-p 8333:22 \
--ipc=host \
-it sss-ubuntu-base:v1.0 /bin/bash
# --runtime nvidia \
# --net=host \

docker stop sss-vllm-gpu
docker rm sss-vllm-gpu
```

为什么没有 bashrc 等文件，为什么终端颜色异常？

```bash
cp /etc/skel/.bash_logout /etc/skel/.bashrc /etc/skel/.profile ~/
```

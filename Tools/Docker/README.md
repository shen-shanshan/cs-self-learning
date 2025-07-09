# Docker

## 构建自己的镜像并推送到 docker hub

### 1. 注册 docker hub 账号

https://www.docker.com/products/docker-hub/

我的账号：467638484

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

为什么没有 bashrc 等文件，为什么终端颜色异常？

```bash
cp /etc/skel/.bash_logout /etc/skel/.bashrc /etc/skel/.profile ~/
```

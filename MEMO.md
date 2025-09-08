# MEMO

## Linux

```bash
# 安装 sudo
apt-get update
apt-get install sudo

# 安装 curl
sudo apt update
sudo apt upgrade
sudo apt install curl

# 将自己的 SSH 公钥配置到远程服务器上
ssh user@IP -p port
# 将 id_rsa.pub 公钥放在服务器上的 authorized_keys 文件中
vim /root/.ssh/authorized_keys

# 安装 SSH 服务
sudo apt update
sudo apt install openssh-client

# 将 SSH 公钥配置到 GitHub 上
ssh-keygen -t ed25519 -C "467638484@qq.com"
eval "$(ssh-agent -s)"
ssh-add ~/.ssh/id_ed25519
cat ~/.ssh/id_ed25519.pub  # Add it to your github

tmux ls
tmux new -s download

export CPLUS_INCLUDE_PATH=$CPLUS_INCLUDE_PATH:/usr/include/c++/13:/usr/include/c++/13/x86_64-openEuler-linux

export https_proxy=http://127.0.0.1:7890 http_proxy=http://127.0.0.1:7890 all_proxy=socks5://127.0.0.1:7890
```

## Docker

```bash
# 启动容器（Ascend 01 and 02）
cd /data/disk3/sss/docker
docker-compose -p sss up -d

# 常用命令
docker exec -it <容器名或ID> /bin/bash
docker stop <容器名或ID>
docker restart <容器名或ID>
docker commit <容器名或ID> <镜像名>
docker rm <容器名或ID>
```

## Git

```bash
# 基本配置
git config --global user.email "467638484@qq.com"
git config --global user.name "shen-shanshan"

# 设置命令别名
vim ~/.gitconfig
# 编辑 [alias] section
#【格式】：你的别名 = '!f() { 命令1; 命令2; 命令3; }; f'

# 一键下载 PR（参考链接：https://github.com/Yikun/yikun.github.com/issues/89）
# 快速将 Pull Request ID 为 736 的代码下载到本地：git pr 736
pr = "!f() { git fetch -fu ${2:-$(git remote |grep ^upstream || echo origin)} refs/pull/$1/head:pr/$1 && git checkout pr/$1; }; f"
# 同步上游最新代码
sync = "!f() { git fetch upstream && git rebase upstream/main; }; f"
# nb (new branch) 同步上游并创建新分支
nb = "!f() { git fetch upstream && git checkout -b $1 upstream/main && git branch; }; f"
db = "!f() { git branch -D $1 && git branch; }; f"

# 常用命令
git checkout <commit>
git checkout -b <commit>
git stash
git stash pop
git stash drop [stash_id]
git stash clear
git clone -b 分支名 仓库地址
git cherry-pick <commitHash>
git checkout -b <branch_name> <tag_name>

# 提交代码时，添加共同作者
$ git commit -m "Refactor usability tests. \
>
> Co-authored-by: NAME <NAME@EXAMPLE.COM>
> Co-authored-by: linfeng-yuan <1102311262@qq.com>"

# 安装 git-lfs（官网：https://git-lfs.com/）
curl -s https://packagecloud.io/install/repositories/github/git-lfs/script.deb.sh | sudo bash
sudo apt-get install git-lfs
git lfs install

# 安装 pre-commit 报错 --> 需要安装 go 并设置国内代理
sudo apt update
# apt search golang-go
sudo apt install golang-go -y
go version
go env -w GOPROXY=https://goproxy.cn,direct
```

## Pip

```bash
pip config set global.index-url https://mirrors.tuna.tsinghua.edu.cn/pypi/web/simple
pip install --upgrade transformers
```

## CANN

```bash
# set env
source /home/sss/Ascend/ascend-toolkit/set_env.sh
source /home/sss/Ascend/nnal/atb/set_env.sh

source /usr/local/Ascend/ascend-toolkit/set_env.sh
source /usr/local/Ascend/nnal/atb/set_env.sh

# show env
cat /home/sss/Ascend/ascend-toolkit/latest/aarch64-linux/ascend_toolkit_install.info
cat /usr/local/Ascend/ascend-toolkit/latest/aarch64-linux/ascend_toolkit_install.info
```

## Model

```python
# 防止终端断开连接，导致下载中断：
# tmux new -s download

import os
from modelscope import snapshot_download

os.environ["MODELSCOPE_CACHE"] = "/shared/cache/modelscope/hub"
os.environ["MODELSCOPE_CACHE"] = "/root/.cache/modelscope/hub"  # A3
model_dir = snapshot_download('ZhipuAI/glm-4-9b')
```

```bash
# Ascend 01 & 02
/home/sss/.cache/modelscope/hub/Qwen/Qwen2.5-0.5B-Instruct
/home/sss/cache/modelscope/models/Qwen/Qwen2.5-7B-Instruct
/home/sss/cache/modelscope/models/deepseek-ai/DeepSeek-V2-Lite-Chat
/home/sss/.cache/modelscope/hub/models/LLM-Research/Meta-Llama-3.1-8B-Instruct
/root/.cache/modelscope/hub/models/Qwen/Qwen2.5-0.5B-Instruct

# Coder
/home/sss/.cache/modelscope/hub/models/Qwen/Qwen2.5-1.5B-Instruct
/shared/cache/modelscope/hub/models/Qwen/Qwen2.5-7B-Instruct
/shared/cache/modelscope/hub/models/Qwen/Qwen2.5-VL-7B-Instruct
/shared/cache/modelscope/hub/models/Qwen/Qwen2-Audio-7B-Instruct
/shared/cache/modelscope/hub/models/Qwen/Qwen3-30B-A3B  # tensor_parallel_size=4
/shared/cache/modelscope/hub/models/ZhipuAI/glm-4-9b
# Spec Decode
/shared/cache/modelscope/hub/models/LLM-Research/Meta-Llama-3.1-8B-Instruct
/home/sss/models/models/models/vllm-ascend/EAGLE3-LLaMA3.1-Instruct-8B
/home/sss/models/models/models/vllm-ascend/DeepSeek-R1-W8A8

# A3
/root/.cache/modelscope/hub/models/Qwen/Qwen2.5-7B-Instruct
/root/.cache/modelscope/hub/models/ZhipuAI/glm-4-9b
/root/.cache/modelscope/hub/models/ZhipuAI/GLM-4___5
```

## Open Source

```bash
# 开源社区常用话术
The CI is finally passed and this PR can be merged.
I have rebased on the latest main and nothing changed.

# 常用符号
🎯

> [!NOTE]
>
```

## VSCode

```bash
# 常用快捷键
折叠所有：Ctrl/Cmd + K + 0
展开所有：Ctrl/Cmd + K + J
```

## A3 集群

基本信息：

```bash
# 密码：%cQlTuPZOdE+/T4TnIPGUNw+

# 挂载卷：
mkdir -p /mnt/sfs_turbo
mount -t nfs -o vers=3,nolock,proto=tcp,noresvport 23021270-7ebf-43b2-925c-b1686da4868a.sfsturbo.internal:/ /mnt/sfs_turbo

1.95.9.213 (172.22.0.218)
172.22.0.155
172.22.0.188
172.22.0.212

ssh root@172.22.0.218
ssh root@172.22.0.155
ssh root@172.22.0.188
ssh root@172.22.0.212

exit

npu-list
npu-smi info
```

启动容器：

```bash
# -itd
export IMAGE=quay.io/ascend/vllm-ascend:main-a3
export IMAGE=quay.io/ascend/vllm-ascend:v0.10.1rc1-a3
export IMAGE=quay.io/ascend/vllm-ascend:v0.10.0rc1-a3

export IMAGE=quay.io/ascend/vllm-ascend:main-a3
docker run \
--privileged=true \
--name sss \
--net=host \
-e ASCEND_VISIBLE_DEVICES=0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15 \
--device /dev/davinci0 \
--device /dev/davinci1 \
--device /dev/davinci2 \
--device /dev/davinci3 \
--device /dev/davinci4 \
--device /dev/davinci5 \
--device /dev/davinci6 \
--device /dev/davinci7 \
--device /dev/davinci8 \
--device /dev/davinci9 \
--device /dev/davinci10 \
--device /dev/davinci11 \
--device /dev/davinci12 \
--device /dev/davinci13 \
--device /dev/davinci14 \
--device /dev/davinci15 \
--device /dev/davinci_manager \
--device /dev/devmm_svm \
--device /dev/hisi_hdc \
-v /home/sss:/home/sss \
-v /usr/local/dcmi:/usr/local/dcmi \
-v /usr/local/Ascend/driver/tools/hccn_tool:/usr/local/Ascend/driver/tools/hccn_tool \
-v /usr/local/bin/npu-smi:/usr/local/bin/npu-smi \
-v /usr/local/Ascend/driver/lib64/:/usr/local/Ascend/driver/lib64/ \
-v /usr/local/Ascend/driver/version.info:/usr/local/Ascend/driver/version.info \
-v /etc/ascend_install.info:/etc/ascend_install.info \
-v /mnt/sfs_turbo/ascend-ci-share-nv-action-vllm-benchmarks:/root/.cache \
-p 8002:8002 \
-e VLLM_USE_MODELSCOPE=True \
-e PYTORCH_NPU_ALLOC_CONF=max_split_size_mb:256 \
-it $IMAGE /bin/bash

docker exec -it sss /bin/bash
docker start sss
docker stop sss
docker rm sss
exit

git@github.com:vllm-project/vllm-ascend.git
```

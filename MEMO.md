# MEMO

## 环境搭建

- [Preparation](./AI/AI_Infra/Environment_Preparation/README.md)
- [Dockerfile](./Tools/Docker/Dockerfiles/README.md)
- [Init env script](./AI/AI_Infra/Environment_Preparation/scripts/init_env.sh)

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
sudo apt install openssh-client -y

# 将 SSH 公钥配置到 GitHub 上
ssh-keygen -t ed25519 -C "467638484@qq.com"
eval "$(ssh-agent -s)"
ssh-add ~/.ssh/id_ed25519
cat ~/.ssh/id_ed25519.pub  # Add to https://github.com/settings/keys

# 配置容器内 SSH 服务
sudo apt-get update
sudo apt-get install openssh-server -y
# 启动 SSH 服务
sudo service sshd status
sudo service sshd start
# 查看 SSH 是否启动（打印 sshd 则说明已成功启动）
ps -e | grep sshd
sudo vim /etc/ssh/sshd_config
# ------------------------------------------------------------------------
Port 22
PermitRootLogin yes
# ------------------------------------------------------------------------
# systemctl restart sshd.service
# systemctl enable sshd.service
# sudo systemctl enable sshd
sudo /etc/init.d/ssh restart

# sshd: unrecognized service
cat /etc/hostname  # liteserver-for-vllm-ascend-00001 f26631d36eaa
sudo vim /etc/hosts
```

Vim 配置：d

```bash
vim ~/.vimrc

set number
```

其它：

```bash
# 查看文件大小（human readable）
ls -lhS

# sudo apt install tmux -y
tmux ls
tmux new -s download
# 基本操作
tmux new-session -s session_name    # 创建新会话
tmux attach -t session_name         # 连接到会话
tmux list-sessions                  # 列出所有会话
tmux kill-session -t session_name   # 结束会话
# 会话内操作
# Ctrl+b d      # 分离当前会话
# Ctrl+b "      # 水平分割窗格
# Ctrl+b %      # 垂直分割窗格
# Ctrl+b ←→↑↓   # 在窗格间切换

# 从本地复制文件到远程主机
scp example.txt user@remote_host:/home/user/
scp 1.png root@139.9.155.20:/home/sss/images/
# 从远程主机复制文件到本地
scp user@remote_host:/home/user/example.txt .

export CPLUS_INCLUDE_PATH=$CPLUS_INCLUDE_PATH:/usr/include/c++/13:/usr/include/c++/13/x86_64-openEuler-linux
export https_proxy=http://127.0.0.1:7890 http_proxy=http://127.0.0.1:7890 all_proxy=socks5://127.0.0.1:7890

echo "要追加的文本" >> 文件名

# 测试网络连接
telnet [ip] [port]
nc -zv 1.95.9.213 2222

# 缺少驱动 xxx.so 库
sudo find / -name "libdrvdsmi_host.so"
export LD_LIBRARY_PATH=/usr/local/Ascend/driver/lib64/driver:$LD_LIBRARY_PATH
```

## Docker

```bash
# 启动容器（Ascend 01 and 02）
# cd /data/disk3/sss/docker
cd /home/sss/docker/
docker-compose -p sss up -d
docker exec -it sss /bin/bash

# 常用命令
docker exec -it <容器名或ID> /bin/bash
docker stop <容器名或ID>
docker restart <容器名或ID>
docker commit <容器名或ID> <镜像名>
docker rm <容器名或ID>
docker rename <old name> <new name>
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
# 解决：error: The following untracked working tree files would be overwritten by checkout
git stash push -u
git stash pop
git stash drop [stash_id]
git stash clear
git clone -b 分支名 仓库地址
git cherry-pick <commitHash>
git checkout -b <branch_name> <tag_name>

git fetch --tags
# 查看所有 tag
git tag -l
# 切换到 v2.0.1 标签并创建分支
git checkout -b release-2.0.1 v2.0.1
# 或者直接查看 tag 内容（这会进入"分离头指针"状态，在此状态下的修改需要创建新分支来保存）
git checkout v2.0.1

# 回退到某个commit（包含该commit之后的所有更改）
git revert <commit-hash>..HEAD --no-edit
# 回退单个commit
git revert <commit-hash>
# 回退多个连续的commit（从commitA到commitB，不包括commitA）
git revert <commitA>..<commitB>

# 提交代码时，添加共同作者
$ git commit -m "Refactor usability tests. \
>
> Co-authored-by: NAME <NAME@EXAMPLE.COM>
> Co-authored-by: linfeng-yuan <1102311262@qq.com>"

# 安装 git-lfs（官网：https://git-lfs.com/）
curl -s https://packagecloud.io/install/repositories/github/git-lfs/script.deb.sh | sudo bash
sudo apt-get install git-lfs
git lfs install
```

pre-commit：

```bash
pip install pre-commit
pre-commit install

# 安装 pre-commit 报错 --> 需要安装 go 并设置国内代理
sudo apt update
# apt search golang-go
sudo apt install golang-go -y
go version
go env -w GOPROXY=https://goproxy.cn,direct

# 禁用 pre-commit 检查
--no-verify

# pre-commit 初始化时一直卡住
git config --global http.version HTTP/1.1
pre-commit clean
rm -rf ~/.cache/pre-commit
pre-commit install
pre-commit run --all-files
```

## Pip

```bash
pip config set global.index-url https://mirrors.tuna.tsinghua.edu.cn/pypi/web/simple
pip install --upgrade transformers
```

Conda:

```bash
# 升级 python
conda install python=3.11
```

## CANN

```bash
# set env
source /usr/local/Ascend/ascend-toolkit/set_env.sh
source /usr/local/Ascend/nnal/atb/set_env.sh
source /home/sss/Ascend/ascend-toolkit/set_env.sh
source /home/sss/Ascend/nnal/atb/set_env.sh

# show env
cat /usr/local/Ascend/ascend-toolkit/latest/aarch64-linux/ascend_toolkit_install.info
cat /home/sss/Ascend/ascend-toolkit/latest/aarch64-linux/ascend_toolkit_install.info

# ImportError: libascend_hal.so: cannot open shared object file: No such file or directory
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/local/Ascend/ascend-toolkit/latest/`uname -i`-linux/devlib
find . -name libascend_hal.so
env | grep LD_LIBRARY_PATH
export LD_LIBRARY_PATH=/usr/local/Ascend/driver/lib64/driver:$LD_LIBRARY_PATH
export LD_LIBRARY_PATH=/home/sss/Ascend/ascend-toolkit/latest/aarch64-linux/devlib:$LD_LIBRARY_PATH

# ImportError: /usr/local/Ascend/ascend-toolkit/latest/lib64/libruntime_common.so: undefined symbol: _ZN12ErrorManager19ATCReportErrMessageESsRKSt6vectorISsSaISsEES4_
source /usr/local/Ascend/ascend-toolkit/set_env.sh
source /usr/local/Ascend/nnal/atb/set_env.sh
```

## HCCL

```bash
export HCCL_BUFFSIZE=2048

# ERR02200 DIST call hccl api failed.
# Failed to bind the IP port. Reason: The IP address and port have been bound already.
export HCCL_IF_BASE_PORT=50000
sysctl -w net.ipv4.ip_local_reserved_ports=50000-50015
sysctl -w net.ipv4.ip_local_reserved_ports=60000-60015
```

## Model

```bash
# sudo apt install tmux -y
tmux ls
tmux new -s download
# 基本操作
tmux new-session -s session_name    # 创建新会话
tmux attach -t session_name         # 连接到会话
# tmux attach -t download
tmux list-sessions                  # 列出所有会话
tmux kill-session -t session_name   # 结束会话
# 会话内操作
# Ctrl+b d      # 分离当前会话
# Ctrl+b "      # 水平分割窗格
# Ctrl+b %      # 垂直分割窗格
# Ctrl+b ←→↑↓   # 在窗格间切换
```

```python
# 防止终端断开连接，导致下载中断：
# tmux new -s download
import os
from modelscope import snapshot_download

os.environ["MODELSCOPE_CACHE"] = "/home/sss/.cache/modelscope/hub"  # Coder
os.environ["MODELSCOPE_CACHE"] = "/root/.cache/modelscope/hub"  # A2 (01)
os.environ["MODELSCOPE_CACHE"] = "/root/.cache/modelscope/hub"  # A3

model_dir = snapshot_download("Qwen/Qwen3-Omni-30B-A3B-Thinking")


# huggingface 下载
# uv pip install huggingface_hub -i https://mirrors.tuna.tsinghua.edu.cn/pypi/web/simple
# export HF_ENDPOINT=https://hf-mirror.com
from huggingface_hub import snapshot_download

MODEL = "rednote-hilab/dots.ocr"

model_dir = snapshot_download(
    repo_id=MODEL,
    revision="main",
    local_dir="/home/sss/.cache/huggingface/hub/models/model_name",
    local_dir_use_symlinks=False,  # 把文件真实拷贝下来（推荐）
)
print(f"Download {MODEL} to {model_dir} finished.")
```

```bash
# Ascend 01
/home/sss/.cache/modelscope/hub/Qwen/Qwen2.5-0.5B-Instruct
/home/sss/cache/modelscope/models/Qwen/Qwen2.5-7B-Instruct
/home/sss/cache/modelscope/models/deepseek-ai/DeepSeek-V2-Lite-Chat
/home/sss/.cache/modelscope/hub/models/LLM-Research/Meta-Llama-3.1-8B-Instruct
# Ascend 01 docker
/root/.cache/modelscope/hub/models/Qwen/Qwen2.5-0.5B-Instruct
/root/.cache/modelscope/hub/models/Qwen/Qwen3-8B
/root/.cache/modelscope/hub/models/deepseek-ai/DeepSeek-V2-Lite

# Coder
/home/sss/.cache/modelscope/hub/models/Qwen/Qwen2.5-1.5B-Instruct
/shared/cache/modelscope/hub/models/Qwen/Qwen2.5-7B-Instruct
/shared/cache/modelscope/hub/models/Qwen/Qwen2.5-VL-7B-Instruct
/shared/cache/modelscope/hub/models/Qwen/Qwen2-Audio-7B-Instruct
/shared/cache/modelscope/hub/models/Qwen/Qwen3-30B-A3B  # tensor_parallel_size=4
/shared/cache/modelscope/hub/models/ZhipuAI/glm-4-9b
# A100
/home/sss/.cache/modelscope/hub/models/Qwen/Qwen2.5-VL-7B-Instruct
# Spec Decode
/shared/cache/modelscope/hub/models/LLM-Research/Meta-Llama-3.1-8B-Instruct
/home/sss/models/models/models/vllm-ascend/EAGLE3-LLaMA3.1-Instruct-8B
/home/sss/models/models/models/vllm-ascend/DeepSeek-R1-W8A8

# A3
/root/.cache/modelscope/hub/models/Qwen/Qwen2___5-0___5B-Instruct
/root/.cache/modelscope/hub/models/Qwen/Qwen2.5-7B-Instruct
/root/.cache/modelscope/hub/models/Qwen/Qwen3-30B-A3B
/root/.cache/modelscope/hub/models/ZhipuAI/glm-4-9b
/root/.cache/modelscope/hub/models/ZhipuAI/GLM-4___5
```

## Ascend 01

```bash
docker pull quay.nju.edu.cn/ascend/vllm-ascend:main

export IMAGE=quay.io/ascend/vllm-ascend:main
export IMAGE=quay.nju.edu.cn/ascend/vllm-ascend:main

docker run \
--name sss \
-e ASCEND_VISIBLE_DEVICES=0,1 \
--device /dev/davinci0 \
--device /dev/davinci1 \
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
-v /data/disk2/cache:/root/.cache \
-p 8002:8002 \
-p 8333:22 \
-it $IMAGE /bin/bash

cd /home/sss/
docker-compose -p sss up -d
docker-compose -p sss-cann-test up -d
```

`docker-compose.yaml`：

```yaml
services:
  sss:
    image: quay.nju.edu.cn/ascend/vllm-ascend:main
    container_name: sss
    volumes:
      - /usr/local/dcmi:/usr/local/dcmi
      - /usr/local/bin/npu-smi:/usr/local/bin/npu-smi
      - /usr/local/Ascend/driver/lib64:/usr/local/Ascend/driver/lib64
      - /usr/local/Ascend/driver/version.info:/usr/local/Ascend/driver/version.info
      - /etc/ascend_install.info:/etc/ascend_install.info
      - /home/sss:/home/sss
      - /data/disk2/cache:/root/.cache
    ports:
      - 8009:8009
    restart: unless-stopped
    hostname: ascend-01
    tty: true
    devices:
      - /dev/davinci4
      - /dev/davinci5
      - /dev/davinci_manager
      - /dev/devmm_svm
      - /dev/hisi_hdc
    cap_add:
      - SYS_PTRACE
    shm_size: 20gb
```

## A3 集群

基本信息：

```bash
# 密码：
%cQlTuPZOdE+/T4TnIPGUNw+

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
export IMAGE=quay.io/ascend/vllm-ascend:main
export IMAGE=quay.io/ascend/vllm-ascend:main-a3
export IMAGE=quay.nju.edu.cn/ascend/vllm-ascend:main-a3

export IMAGE=quay.io/ascend/vllm-ascend:main-a3
docker run \
--privileged=true \
--name sss \
--net=host \
-e ASCEND_RT_VISIBLE_DEVICES=0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15 \
--device /dev/davinci0 \
--device /dev/davinci1 \
--device /dev/davinci2 \
--device /dev/davinci3 \
--device /dev/davinci_manager \
--device /dev/devmm_svm \
--device /dev/hisi_hdc \
-v /usr/local/dcmi:/usr/local/dcmi \
-v /usr/local/Ascend/driver/tools/hccn_tool:/usr/local/Ascend/driver/tools/hccn_tool \
-v /usr/local/bin/npu-smi:/usr/local/bin/npu-smi \
-v /usr/local/Ascend/driver/lib64/:/usr/local/Ascend/driver/lib64/ \
-v /usr/local/Ascend/driver/version.info:/usr/local/Ascend/driver/version.info \
-v /etc/ascend_install.info:/etc/ascend_install.info \
-v /home/sss:/home/sss \
-v /mnt/sfs_turbo/ascend-ci-share-nv-action-vllm-benchmarks:/root/.cache \
-p 8002:8002 \
-e VLLM_USE_MODELSCOPE=True \
-e PYTORCH_NPU_ALLOC_CONF=max_split_size_mb:256 \
-it $IMAGE /bin/bash

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

# --net=host
# host 模式直接使用宿主机的网络空间，该模式下无法使用 -p 命令选项映射端口，容器和宿主机直接共享端口

docker exec -it sss /bin/bash
docker start sss
docker stop sss
docker rm sss

exit
```

## VSCode

```bash
# 常用快捷键
折叠所有：Ctrl/Cmd + K + 0
展开所有：Ctrl/Cmd + K + J

# vllm 代码阅读（排除文件）
*.md,*.yaml,*.h,*.hpp,*.cu,*.cuh,test*.py,*.cmake,examples/*,tests/*,*.sh,*.env,*.yml,.gitignore
*.md,*.yaml,*.h,*.hpp,*.cu,*.cuh,*.cmake,examples/*,*.sh,*.env,*.yml,.gitignore
```

## Debug

```bash
# logger = init_logger(__name__)
# logger.info_once("...")

# https://zhuanlan.zhihu.com/p/1948019110530232787
# 只允许 rank0 打印日志
if torch.distributed.get_rank() == 0:
    print("xxx")

# 将日志按照 rank 写入不同的文件下
with open(f"./debug_{torch.distributed.get_rank()}.log","w") as f:
    print(f"scheduler_output: {scheduler_output}",file=f, flush=True)

# 立即把缓冲区里的内容写到输出（stdout）中，而不是等到缓冲区填满或程序结束才输出
print(..., flush=True)
```

VSCode 调试配置：

```json
{
    // 使用 IntelliSense 了解相关属性。 
    // 悬停以查看现有属性的描述。
    // 欲了解更多信息，请访问: https://go.microsoft.com/fwlink/?linkid=830387
    "version": "0.2.0",
    "configurations": [
        {
            "name": "Python 调试程序: 当前文件",
            "type": "debugpy",
            "request": "launch",
            "program": "${file}",
            "console": "integratedTerminal",
            "python": "/root/miniconda3/envs/vllm/bin/python",  // 指定虚拟环境
            "subProcess": true,  // 开启多进程调试
            "justMyCode": false,  // 允许进入并调试第三方库源码、Python 标准库代码
        }
    ]
}
```

## Open Source

```bash
# 开源社区常用话术
The CI is finally passed and this PR can be merged.
I have rebased on the latest main and nothing changed.

Hello Russell, I have worked on some structured output PR before and really thanks for your review and suggestion. I apologize for the intrusion, but I was hoping you could take a look at https://github.com/vllm-project/vllm/pull/21999 and https://github.com/vllm-project/vllm/pull/22481, or if there is a sig about this feature and contributors can share their PRs needed review? Looking forward to your reply~

Hello, I'm really thanks for your review and suggestion about https://github.com/vllm-project/vllm/pull/26487. I have updated the code and related doc recently. Could you please help take a look at this PR? Thanks~

# 常用符号
🎯

> [!NOTE]
>

<details>
<summary>logs</summary>
</details>
```

PR 模板：

```markdown
**Background:**

...

**Proposed Change:**

...
```

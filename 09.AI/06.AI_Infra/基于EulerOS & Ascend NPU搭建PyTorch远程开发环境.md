# 基于 EulerOS & Ascend NPU 搭建 PyTorch 远程开发环境

[toc]

## 一、概述

本文记录了自己在基于 EulerOS & Ascend NPU 的华为云远程服务器上，使用 docker 容器搭建 PyTorch 开发环境的主要过程以及遇到的问题。

硬件规格如下：

```
Kunpeng + Ascend: 192 CPU，1.5T MEM，21T DISK，8*Ascend XXX 型号
EulerOS V2 R10
```

## 二、创建 docker 镜像并运行容器

### 2.1 编写 Dockerfile

创建 `Dockerfile` 如下：

```dockerfile
FROM ubuntu:20.04

# define your var
ARG YOUR_USER_NAME="sss"
ARG YOUR_GROUP_ID="9005"
ARG YOUR_USER_ID="9005"

ARG DEBIAN_FRONTEND=noninteractive
ENV TZ="Asia/shanghai"

RUN sed -i 's/ports.ubuntu.com/mirrors.aliyun.com/g' /etc/apt/sources.list && \
    apt-get update && \
    yes | unminimize && \
    apt-get install -y adduser sudo vim gcc g++ cmake make gdb git tmux openssh-server \
                   net-tools iputils-ping python3-distutils python3-setuptools \
                   python3-wheel python3-yaml python3-dbg python3-pip libmpich-dev

# Config user. User must join group HwHiAiUser(1000) to use npu.
# Identify user id and group id to match user out of docker. (optional)
RUN groupadd -g $YOUR_GROUP_ID $YOUR_USER_NAME && \
    useradd -u $YOUR_USER_ID -g $YOUR_USER_NAME -ms /bin/bash $YOUR_USER_NAME && \
    sed -i "/root\tALL=(ALL:ALL) ALL/a"${YOUR_USER_NAME}"\tALL=(ALL:ALL) ALL" /etc/sudoers && \
    echo "source /home/${YOUR_USER_NAME}/Ascend/ascend-toolkit/set_env.sh" >> /home/"$YOUR_USER_NAME"/.bashrc && \
    echo "export LD_LIBRARY_PATH=/usr/local/Ascend/driver/lib64/common/:/usr/local/Ascend/driver/lib64/driver/:${LD_LIBRARY_PATH}" >> /home/"$YOUR_USER_NAME"/.bashrc && \
    ssh-keygen -A

CMD ["/bin/bash", "/home/sss/bin/entrypoint.sh"]
```

> 注意：
>
> - Dockerfile 中的 `YOUR_USER_NAME`、`YOUR_GROUP_ID` 以及 `YOUR_USER_ID` 为将要在容器中创建的用户和用户组，请自行进行设置；
> - `CMD` 中的 `entrypoint.sh` 脚本文件为容器每次启动时都会去执行的命令集合，这里文件前面的路径需要替换为自己容器中的路径（Dockerfile 里面写的路径都是指容器里面的）；
> - 我们需要将自己宿主机中存放个人数据的目录（我是 `/data/disk/sss`）挂载到容器中自己的用户目录（我是 `/home/sss`）下，并将编写好的 `entrypoint.sh` 脚本存放到 `/data/disk/sss/bin` 目录下，这样我们进入容器后，就能在容器的 `/home/sss/bin` 目录下找到 `entrypoint.sh` 脚本并执行它（如果目录映射不对，在容器中找不到该脚本文件，那么容器启动时就会报错）。

### 2.2 构建 base 镜像

在 `Dockerfile` 所在目录执行下面的命令：

```bash
# docker build -t <镜像名称>:<镜像tag> <Dockerfile所在目录>
docker build -t sss_base_image:1.0 .
```

其它镜像常用命令：

```bash
# 查看镜像
docker images

# 删除镜像
docker rmi image:tag

# 创建新的标签
docker tag <old_image_name>:<old_tag> <new_image_name>:<new_tag>
```

### 2.3 编写容器启动脚本

创建 `entrypoint.sh` 脚本文件如下：

```bash
# /bin/bash

# define your var
your_user_name="sss"
your_password="xxx"
# Create passwd
echo "${your_user_name}:${your_password}" | chpasswd

# Add to group 1000(HwHiAiUser) to use npu
cat /etc/passwd | awk -F ":" '{print $4}' | grep 1000
if [ $? -ne 0 ]
then
    groupadd -g 1000 HwHiAiUser
    useradd -u 1000 -g HwHiAiUser -ms /bin/bash HwHiAiUser
fi

usermod -a -G 1000 ${your_user_name}

# For jumper
if [ $(grep -c "HostkeyAlgorithms +ssh-rsa" /etc/ssh/sshd_config) -eq 0 ]
then
    echo "HostkeyAlgorithms +ssh-rsa" >> /etc/ssh/sshd_config
fi

if [ ! -d /run/sshd ]
then
    mkdir /run/sshd
fi

/usr/sbin/sshd -D

chown -R sss:sss /home/sss
```

> 注意：脚本中的 `your_user_name` 和 `your_password` 为容器中的用户，请自行进行设置（该用户会被加入到 `HwHiAiUser` 用户组中，这样该用户才能使用 NPU 进行计算）。

### 2.4 编写容器配置文件

创建 `docker-compose.yaml` 配置文件如下：

```yaml
services:
  chattts:
    image: sss_base_image:1.0
    container_name: sss
    volumes:
      # 保证 ~/bin/entrypoint.sh 文件的映射路径正确
      - /data/disk3/sss:/home/sss
      # ----- 此处保持不变 ----- #
      - /usr/local/dcmi:/usr/local/dcmi
      - /usr/local/bin/npu-smi:/usr/local/bin/npu-smi
      - /usr/local/Ascend/driver/lib64:/usr/local/Ascend/driver/lib64
      - /usr/local/Ascend/driver/version.info:/usr/local/Ascend/driver/version.info
      - /etc/ascend_install.info:/etc/ascend_install.info
      # ---------------------- #
    ports:
      # 映射22端口，方便 ssh 远程连接容器
      - 8333:22
      # 可添加更多端口映射
      - 8008:8008
    restart: unless-stopped
    hostname: ascend910b-02
    tty: true
    devices:
      # 此处更改为可用的 NPU 卡号，可通过 npu-list 查询卡的占用状态
      - /dev/davinci3
      - /dev/davinci_manager
      - /dev/devmm_svm
      - /dev/hisi_hdc
    cap_add:
      - SYS_PTRACE
    shm_size: 20gb
    # command: /bin/bash -c "chown -R sss:sss /home/sss && /bin/bash"
```

将配置文件中的以下变量替换为自己的：

- `chattts`：服务名称；
- `image: sss_base_image:1.0`：镜像名称:tag（注意：如果你的镜像 tag 不是 `latest` 的话，不能省略版本信息）；
- `container_name: sss`：容器名称；
- `- /data/disk3/sss:/home/sss`：将自己的用户目录（`/data/disk3/sss`）挂载到容器中的用户目录（`/data/disk3/sss`）下；
- `- 8333:22`：将宿主机端口（自己设置，这里我随便设置的 8333）映射到容器中的端口（22，这是 ssh 服务的默认端口，方便后续使用 ssh 直接连接到自己的容器中）；
- `hostname: ascend910b-02`：宿主机名称。

> 注意：这里的 `docker-compose.yaml` 中不能加 `command` 选项，因为该选项中的命令会覆盖 `Dockerfile` 中的 `CMD` 选项，导致 `entrypoint.sh` 脚本不会被执行（后果很严重！）。这里如果还想加一些在容器启动时需要执行的命令，可以直接加到 `entrypoint.sh` 脚本中，这样每次容器启动时都会执行这些命令。

### 2.5 启动并进入容器

启动容器：

```bash
# 临时启动（运行一次）：docker-compose -p <project-name> up
# 后台启动（一直运行）：docker-compose -p <project-name> up -d
docker-compose -p chattts up -d
```

进入容器：

```bash
# docker exec -it <容器名或ID> /bin/bash
docker exec -it sss /bin/bash
# 退出容器：exit
```

其它容器常用命令：

```bash
# 停止容器
docker stop <容器名或ID>

# 重启停止的容器
docker restart <容器名或ID>

# 保存容器为新的镜像
docker commit <容器名或ID> <镜像名>

# 删除容器
docker rm <容器名或ID>
```

> 参考资料：[<u>使用 docker-compose 搭建 npu 环境的容器</u>](https://github.com/cosdt/cosdt.github.io/issues/28)。

## 三、安装 CANN 软件

### 3.1 确认环境

进入容器，检查当前环境是否满足以下要求：

| 软件 | 要求版本 |
| :- | :- |
| 操作系统 | openEuler20.03/22.03, Ubuntu 20.04/22.04 |
| python | 3.8, 3.9, 3.10 |

确认昇腾 AI 处理器已安装：

```bash
# 安装 lspci
sudo apt-get install pciutils
# 查看是否有 Processing accelerators
lspci | grep 'Processing accelerators'
```

确认操作系统架构及版本：

```bash
uname -m && cat /etc/*release
```

确认 Python 版本：

```bash
python --version
```

### 3.2 安装 miniconda

```bash
# 安装 miniconda
wget https://repo.anaconda.com/miniconda/Miniconda3-latest-Linux-aarch64.sh
bash Miniconda3-latest-Linux-aarch64.sh

# 启用 conda 环境（这里替换为自己的安装路径）
eval "$(/home/sss/bin/miniconda/miniconda3/bin/conda shell.bash hook)"

# 创建 conda 虚拟环境并激活
conda create -n cann python=3.10
conda env list
conda activate cann

# 查看 python 版本和已安装的 python 包
python --version
conda list
```

> 参考资料：
>
> - [<u>安装 miniconda aarch64 版本</u>](https://blog.csdn.net/Damien_J_Scott/article/details/136563747)；
> - [<u>conda 环境启用 & 基本使用</u>](https://www.cnblogs.com/milton/p/18023969)。

设置 miniconda 的 channel：

```bash
# 设置为清华镜像源
conda config --add channels http://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud/conda-forge/
conda config --add channels http://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/free/
conda config --add channels http://mirrors.tuna.tsinghua.edu.cn/anaconda/cloud/bioconda/
```

> 参考资料：[<u>miniconda 设置 channel</u>](https://blog.csdn.net/weixin_43949246/article/details/109637468)。

安装 python 依赖：

```bash
conda install -i https://pypi.tuna.tsinghua.edu.cn/simple attrs numpy decorator sympy cffi pyyaml pathlib2 psutil protobuf scipy requests absl-py wheel typing_extensions
```

### 3.3 安装 CANN-toolkit

```bash
wget "https://ascend-repo.obs.cn-east-2.myhuaweicloud.com/Milan-ASL/Milan-ASL V100R001C19SPC802/Ascend-cann-toolkit_8.0.RC3.alpha001_linux-aarch64.run"
sh Ascend-cann-toolkit_8.0.RC3.alpha001_linux-aarch64.run --install
# 使用 sh 安装可能会报错，换 bash 试试
# bash Ascend-cann-toolkit_8.0.RC3.alpha001_linux-aarch64.run --install
```

> 注意：在容器中安装 CANN 软件时，为保证安装路径的正确，需要切换到自己的用户进行安装（该软件会安装到 `~/Ascend` 目录下）。

```bash
# 切换用户：su <用户名>
su sss
# 退出当前用户：exit
```

### 3.4 安装算子包

```bash
wget "https://ascend-repo.obs.cn-east-2.myhuaweicloud.com/Milan-ASL/Milan-ASL V100R001C19SPC802/Ascend-cann-kernels-910b_8.0.RC3.alpha001_linux.run"
sh Ascend-cann-kernels-910b_8.0.RC3.alpha001_linux.run --install
# 使用 sh 安装可能会报错，换 bash 试试
# bash Ascend-cann-kernels-910b_8.0.RC3.alpha001_linux.run --install
```

> 注意：这里同样也需要切换到自己的用户进行安装。

### 3.5 设置环境变量

```bash
echo "source ~/Ascend/ascend-toolkit/set_env.sh" >> ~/.bashrc
source ~/.bashrc
```

> 参考资料：[<u>快速安装昇腾环境</u>](https://ascend.github.io/docs/sources/ascend/quick_install.html)。

### 3.6 其它问题

**个人用户缺少权限**：

问题现象：在容器中，从 `root` 用户切换为个人用户后，发现访问不了某些目录，显示 `permission denied`。

执行（该命令已添加到容器启动脚本 `entrypoint.sh` 中）：

```bash
chown -R sss:sss /home/sss
```

**个人用户缺少命令**：

问题现象：在容器中，从 `root` 用户切换为个人用户后，执行 `ll`，显示用户没有该命令。

检查个人用户目录下是否缺少 `.bash_lougout`、`.bashrc`、`.profile` 文件，若没有，则将容器中 `/etc/skel` 目录下的这三个文件拷贝一份到自己的用户目录下并修改其权限。

```bash
cp /etc/skel/.bashrc /home/sss/
cp /etc/skel/.bash_logout /home/sss/
cp /etc/skel/.profile /home/sss/

# 644 means writable, readable and executable for the user and readable for groups and others.
chmod 644 .bashrc
chmod 644 .bash_logout
chmod 644 .profile

chown sss:sss .bashrc
chown sss:sss .bash_logout
chown sss:sss .profile
```

> 参考资料：[<u>Bash on Ubuntu on Windows gives error "-bash: /home/user/.bashrc: Permission denied" on startup</u>](https://superuser.com/questions/1318942/bash-on-ubuntu-on-windows-gives-error-bash-home-user-bashrc-permission-den)。

**安装 CANN 软件报错**：

问题现象：安装 CANN 软件报错，显示当前用户没有被添加到 HwHiAiUser 用户组中。

```
User is not belong to the dirver or firmware's installed usergroup! Please add the user (sss) to the group (HwHiAiUser).
```

检查 `entrypoint.sh` 脚本是否有被成功执行，然后检查用户组 `HwHiAiUser` 有没有成功被创建。

我自己在安装时报了这个错是因为 `Dockerfile` 中的 `CMD` 被后来在 `docker-compose.yaml` 中添加的 `command` 选项覆盖了，导致 `entrypoint.sh` 脚本未成功执行，用户 `sss` 未被添加到用户组 `HwHiAiUser` 中，因此无法使用 NPU。

## 四、安装 PyTorch

### 4.1 安装 torch

```bash
pip install torch==2.1.0 -i https://pypi.tuna.tsinghua.edu.cn/simple
pip install pyyaml -i https://pypi.tuna.tsinghua.edu.cn/simple
pip install setuptools -i https://pypi.tuna.tsinghua.edu.cn/simple
```

### 4.2 安装 torch-npu

关于 torch-npu：

> This repository develops the Ascend Extension for PyTorch named torch_npu to adapt Ascend NPU to PyTorch so that developers who use the PyTorch can obtain powerful compute capabilities of Ascend AI Processors.
> Ascend is a full-stack AI computing infrastructure for industry applications and services based on Huawei Ascend processors and software. For more information about Ascend, see Ascend Community.

GitHub 仓库地址：[<u>Ascend Extension for PyTorch</u>](https://github.com/Ascend/pytorch)。

安装 torch-npu：

```bash
pip install torch-npu==2.1.0.post6 -i https://pypi.tuna.tsinghua.edu.cn/simple
```

> 注意：torch-npu 的大版本（如：`2.1.0`）需要和 torch 匹配，具体的版本匹配信息请参考 [<u>Ascend Extension for PyTorch</u>](https://github.com/Ascend/pytorch) 中的 `Ascend Auxiliary Software` 部分。

### 4.3 验证安装是否成功

创建 `test.py` 程序如下：

```python
import torch
import torch_npu

x = torch.randn(2, 2).npu()
y = torch.randn(2, 2).npu()
z = x.mm(y)

print(z)
```

运行程序 `python test.py`，若出现以下信息则说明安装成功：

```
tensor([...], device='npu:0')
```

> 参考资料：[<u>Ascend Extension for PyTorch 配置与安装</u>](https://www.hiascend.com/document/detail/zh/Pytorch/60RC2/configandinstg/instg/insg_0001.html)。

## 五、开启容器 SSH 服务

### 5.1 安装并配置 openssh

```bash
# 安装 openssh
sudo apt-get update
sudo apt-get install openssh-server

# 查看 SSH 是否启动（打印 sshd 则说明已成功启动）
ps -e | grep ssh
```

修改 ssh 配置：

```bash
PubkeyAuthentication yes #启用公钥私钥配对认证方式 
AuthorizedKeysFile .ssh/authorized_keys #公钥文件路径（和上面生成的文件同） 
PermitRootLogin yes #root能使用ssh登录
ClientAliveInterval 60  #参数数值是秒 , 是指超时时间
ClientAliveCountMax 3 #设置允许超时的次数
UsePAM yes # 更改为 UsePAM no
Port 80 #指定好端口号，默认是22 后面这个数字要在你run容器的时候用到
```

然后重启 SSH 服务：

```bash
systemctl restart sshd.service
# 或：
sudo /etc/init.d/ssh restart
```

> 参考资料：
>
> - [<u>Ubuntu 安装 SSH SERVER</u>](https://blog.csdn.net/qq_39698985/article/details/136193187)；
> - [<u>使用 Docker 容器配置 ssh 服务，远程直接进入容器</u>](https://blog.csdn.net/qq_33259057/article/details/124737659)。

### 5.2 配置 VSCode 客户端

在 VSCode 的远程资源管理器中点击设置，找到 `xxx/.ssh/config` 文件，添加以下配置：

```bash
# 远程服务器名称，这里可以随意设置
Host sss-docker
    # 替换为自己的宿主机 IP
    HostName xxx.xxx.xxx.xxx
    # 替换为自己容器的 22 端口映射的宿主机端口，在 docker-compose.yaml 中设置的，我设置的是 8333:22
    Port 8333
    # 容器中的个人用户，使用个人用户密码登录容器，在 entrypoint.sh 脚本中设置的
    User sss
    ForwardAgent yes
    # 每300秒向服务端主动发个包，防止一会儿不操作就和服务器断开连接
    ServerAliveInterval 300
    # 3次发包均无响应会断开连接
    ServerAliveCountMax 3
```

## 六、配置 Git 并拉取代码

### 6.1 在容器中配置 Git

```bash
# 配置用户名和邮箱
git config --global user.name "shanshan shen"
git config --global user.email xxx@gmail.com

# 查看配置
git config --list
```

### 6.2 拉取代码

```bash
git clone xxx.git
```

## 七、总结

到此为止，我们就可以在基于 EulerOS & Ascend NPU 的华为云远程服务器上，在自己搭建的 docker 容器中使用 PyTorch 框架并进行 AI 模型的训练与推理。

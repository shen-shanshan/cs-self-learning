# vLLM

## 常用链接

- [<u>GitHub</u>](https://github.com/vllm-project/vllm)
- [<u>Documentation</u>](https://docs.vllm.ai/en/stable/index.html)
- [<u>Installation</u>](https://docs.vllm.ai/en/stable/getting_started/installation.html)
- [<u>Contributing to vLLM</u>](https://docs.vllm.ai/en/stable/contributing/overview.html)
- [<u>Architecture Overview</u>](https://docs.vllm.ai/en/stable/design/arch_overview.html)

**NPU 支持：**

- [<u>PR: Add Ascend NPU backend</u>](https://github.com/vllm-project/vllm/pull/8054)
- [<u>模型支持验证清单</u>](https://github.com/cosdt/vllm/tree/main/model_support)

## 环境搭建

### 创建用户

```bash
useradd -m sss
passwd sss # 369833

# 切换用户
su - sss
# 切回 root 用户
exit
```

### 安装 miniconda

```bash
# 查看操作系统架构
arch # x86_64

wget https://repo.anaconda.com/miniconda/Miniconda3-latest-Linux-x86_64.sh
bash Miniconda3-latest-Linux-x86_64.sh

# conda config --set auto_activate_base false
```

### 配置 git

```bash
git config --global user.email "467638484@qq.com"
git config --global user.name "Shanshan Shen"

# fatal: unable to access 'https://github.com/shen-shanshan/vllm.git/': Error in the HTTP2 framing layer
git config --global --unset http.proxy
git config --global --unset https.proxy
```

配置 SSH：

```bash
ssh-keygen -t ed25519 -C "467638484@qq.com" # no password
eval "$(ssh-agent -s)"
ssh-add ~/.ssh/id_ed25519

cat ~/.ssh/id_ed25519.pub
```

配置远程仓库：

```bash
git remote add upstream git@github.com:vllm-project/vllm.git
git remote add origin git@github.com:shen-shanshan/vllm.git
```

设置 pip：

```bash
cd ~/.config/pip
vim pip.conf

[global]
index-url=https://mirrors.tuna.tsinghua.edu.cn/pypi/web/simple

index-url=http://mirrors.aliyun.com/pypi/simple/
trusted-host=mirrors.aliyun.com
```

### 安装 vllm

**拉取代码：**

```bash
git clone https://github.com/shen-shanshan/vllm.git
```

**安装依赖：**

```bash
# (Recommended) Create a new conda environment.
conda create -n vllm python=3.12 -y
# conda create -n vllm python=3.10 -y
conda activate vllm
pip install -r requirements-dev.txt -i https://pypi.tuna.tsinghua.edu.cn/simple
pip install -r requirements-lint.txt -i https://pypi.tuna.tsinghua.edu.cn/simple
```

Build from source：Python-only build (without compilation)

```bash
git clone https://github.com/vllm-project/vllm.git
cd vllm
VLLM_USE_PRECOMPILED=1 pip install --editable . # GPU
VLLM_TARGET_DEVICE=npu pip install -e . # NPU
```

## TODO

### RoPE 算子适配

实现 rope 算子：调 torch_npu 接口，重写 `forward_npu()` 方法，可以参考华为云的实现。

torch_npu 接口：`npu_apply_rotary_pos_emb()`。

代码位置：`vllm/model_executor/layers/rotary_embedding.py`。

### 模型验证（在 NPU 上）

**环境配置：**

```bash
# log
python model_verify.py
Downloading Model to directory: /home/sss/.cache/modelscope/hub/ZhipuAI/chatglm2-6b

# 设置 cann 环境变量
echo "source ~/Ascend/ascend-toolkit/set_env.sh" >> ~/.bashrc
source ~/.bashrc

# 设置使用 modelscope
echo "export VLLM_USE_MODELSCOPE=True" >> ~/.bashrc
source ~/.bashrc

pip install git+https://github.com/huggingface/transformers.git
```

**模型下载：**

```bash
modelscope download --model 'ZhipuAI/chatglm2-6b' --local_dir '/home/sss/models/chatglm2-6b'
modelscope download --model 'ZhipuAI/glm-4-9b-chat-hf' --local_dir '/home/sss/models/glm-4-9b-chat-hf'
```

- [<u>模型支持验证清单</u>](https://github.com/cosdt/vllm/tree/main/model_support)
- [<u>模型验证脚本</u>](https://docs.vllm.ai/en/stable/models/supported_models.html#modelscope)

### 其它

vllm 这边除了 vllm-npu 的开发，就剩下公共特性了，一方面可以在每天刷新的 issue、pr 里面去找可以修的 bug 或者可以重构的代码，另一方面就是源神这个 [<u>RFC</u>](https://github.com/vllm-project/vllm/issues/11162)。

还有就是对关键的一些 pr 可以积极参与 review 啥的。

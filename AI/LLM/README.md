# LLM

## 模型下载

### HuggingFace

```bash
# 1.安装依赖
pip install -U huggingface_hub

# 2.设置环境变量（可以写入 ~/.bashrc）
# Linux：
export HF_ENDPOINT=https://hf-mirror.com
# Windows：
$env:HF_ENDPOINT = "https://hf-mirror.com"

# 3.下载模型
huggingface-cli download --resume-download gpt2 --local-dir gpt2

# 4.下载数据集
huggingface-cli download --repo-type dataset --resume-download wikitext --local-dir wikitext
```

示例：

```bash
huggingface-cli download --resume-download google/gemma-1.1-2b-it --local-dir /home/sss/models-hf/google/gemma-1.1-2b-it
```

### ModelScope

模型默认下载地址：`~/.cache/modelscope/hub`，可通过环境变量 `MODELSCOPE_CACHE` 进行设置。

**使用命令行下载：**

```bash
# 环境安装
pip install modelscope

# 下载整个模型 repo 到指定目录
modelscope download --model 'Qwen/Qwen2-7b' --local_dir 'path/to/dir'
# 指定下载单个文件（以 tokenizer.json 文件为例）
modelscope download --model 'Qwen/Qwen2-7b' tokenizer.json
# 指定下载多个个文件
modelscope download --model 'Qwen/Qwen2-7b' tokenizer.json config.json
# 指定下载某些文件
modelscope download --model 'Qwen/Qwen2-7b' --include '*.safetensors'
# 过滤指定文件
modelscope download --model 'Qwen/Qwen2-7b' --exclude '*.safetensors'
```

**使用 Python SDK 下载：**

```python
from modelscope import snapshot_download

model_dir = snapshot_download('baichuan-inc/baichuan-7B', cache_dir='/home/sss/model')
```

- [<u>ModelScope 首页</u>](https://www.modelscope.cn/home)
- [<u>ModelScope 模型下载</u>](https://www.modelscope.cn/docs/models/download)

## 学习资料

- [<u>猛猿的知乎 ✨</u>](https://zhuanlan.zhihu.com/p/654910335)
- [<u>Jay Alammar's blog</u>](https://jalammar.github.io/)

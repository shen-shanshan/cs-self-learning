# LLM

## 学习路线

## 模型下载

- HF 镜像：`https://hf-mirror.com/`；
- 魔乐社区：`https://modelers.cn/`。

环境配置：

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

> 注意：可以添加 `--local-dir-use-symlinks false` 参数禁用文件软链接，这样下载路径下所见即所得。

## 学习资料

[<u>猛猿的知乎 ✨</u>](https://zhuanlan.zhihu.com/p/654910335)；

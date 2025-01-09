# TODO

## Quick Start

开发新特性，新建分支：

```bash
git fetch upstream
git checkout -b <branch_name> upstream/main
```

## 模型验证（NPU）

### 环境配置

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

**测试 NPU 环境：**

```python
import torch, torch_npu
torch.npu.devcice_count()
```

```bash
# 显示对应 npu 设备，表示环境可用
npu-smi info
```

### 模型下载

**命令行方式：**

```bash
pip install modelscope

modelscope download --model 'ZhipuAI/chatglm2-6b' --local_dir '/home/sss/models/ZhipuAI/chatglm2-6b' # 已验证
modelscope download --model 'ZhipuAI/glm-4-9b-chat-hf' --local_dir '/home/sss/models/ZhipuAI/glm-4-9b-chat-hf'
```

**SDK 方式：**

```python
from modelscope import snapshot_download

# decoder-only
model_dir = snapshot_download('AI-ModelScope/AquilaChat-7B', cache_dir='/home/sss/models')
model_dir = snapshot_download('baichuan-inc/baichuan-7B', cache_dir='/home/sss/models') # pip install "transformers<4.34" -U
model_dir = snapshot_download('AI-ModelScope/bloom-3b', cache_dir='/home/sss/models')
# 未找到 DeciLM-7B
model_dir = snapshot_download('AI-ModelScope/EXAONE-3.0-7.8B-Instruct', cache_dir='/home/sss/models')
model_dir = snapshot_download('AI-ModelScope/falcon-7b', cache_dir='/home/sss/models')
model_dir = snapshot_download('AI-ModelScope/gemma-2b', cache_dir='/home/sss/models')
model_dir = snapshot_download('LLM-Research/gemma-2-9b', cache_dir='/home/sss/models')
model_dir = snapshot_download('ZhipuAI/glm-4-9b-chat-hf', cache_dir='/home/sss/models')
model_dir = snapshot_download('AI-ModelScope/gpt2', cache_dir='/home/sss/models')
model_dir = snapshot_download('TabbyML/StarCoder-1B', cache_dir='/home/sss/models')
model_dir = snapshot_download('AI-ModelScope/gpt-j-6b', cache_dir='/home/sss/models')

# encoder-decoder
model_dir = snapshot_download('owl123/bart-base', cache_dir='/home/sss/models')

# multi-modality
model_dir = snapshot_download('Qwen/Qwen-VL-Chat', cache_dir='/home/sss/models')
model_dir = snapshot_download('fixie-ai/ultravox-v0_3', cache_dir='/home/sss/models')
```

### 验证脚本

```python
from vllm import LLM

llm = LLM(model="/home/sss/models/baichuan-inc/baichuan-7B", task="generate", trust_remote_code=True)

# For generative models (task=generate) only
output = llm.generate("Hello, my name is")
# print(output)
for output in outputs:
    prompt = output.prompt
    generated_text = output.outputs[0].text
    print(f"Prompt: {prompt!r}, Generated text: {generated_text!r}")
```

### 验证情况

**ZhipuAI/chatglm2-6b**: ✅

**AI-ModelScope/AquilaChat-7B**: ✅

**baichuan-inc/baichuan-7B**: ❌

模型官方文档要求 `transformers<4.34`，与 NPU 环境要求冲突导致报错。

```bash
vllm 0.1.dev3820+g27244b2.npu requires tokenizers>=0.19.1, but you have tokenizers 0.13.3 which is incompatible.
vllm 0.1.dev3820+g27244b2.npu requires transformers>=4.45.2, but you have transformers 4.33.3 which is incompatible.

ModuleNotFoundError: No module named 'transformers.models.mllama'
[ERROR] 2025-01-06-06:39:29 (PID:151904, Device:-1, RankID:-1) ERR99999 UNKNOWN applicaiton exception
```

而若按 NPU 环境要求，使用 `transformers-4.47.1`，则报错：

```bash
AttributeError: 'LLM' object has no attribute 'llm_engine'
```

**AI-ModelScope/bloom-3b**: ✅

**AI-ModelScope/EXAONE-3.0-7.8B-Instruct**: ✅

**AI-ModelScope/falcon-7b**: ❌

```bash
[rank0]: RuntimeError: call aclnnPromptFlashAttentionV3 failed, detail:EZ1001: [PID: 179252] 2025-01-08-02:11:31.602.007 PromptFlashAttention LaunchAicore failed.
[rank0]:         TraceBack (most recent call last):
[rank0]:         numHeads / numKeyValueHeads = 71, cannot be larger than 64[FUNC:SetTilingHeadNumRatio][FILE:prompt_flash_attention_tiling.cpp][LINE:1445]
[rank0]:         Tiling failed
[rank0]:         Tiling Failed.
[rank0]:         Kernel GetWorkspace failed. opType: 27
[rank0]:         PromptFlashAttention LaunchAicore failed.

numHeads(71) in query shape must be equal to numHeads(7) in attr.
```

**AI-ModelScope/gemma-2b**: ✅

**LLM-Research/gemma-2-9b**: ✅

**ZhipuAI/glm-4-9b-chat-hf**: ❌

```bash
[rank0]: File "/home/sss/github/vllm/vllm/model_executor/layers/rotary_embedding.py", line 66, in _apply_rotary_emb
[rank0]:     o1 = x1 * cos - x2 * sin
[rank0]: RuntimeError: The size of tensor a (0) must match the size of tensor b (64) at non-singleton dimension 2
```

**AI-ModelScope/gpt2**: ✅

**TabbyML/StarCoder-1B**: ✅

llm = LLM(model="/home/sss/models/AI-ModelScope/gpt2")

### 参考资料

- [<u>模型支持验证清单</u>](https://github.com/cosdt/vllm/tree/main/model_support)
- [<u>模型验证脚本</u>](https://docs.vllm.ai/en/stable/models/supported_models.html#modelscope)

## RoPE 算子适配

实现 rope 算子：调 torch_npu 接口，重写 `forward_npu()` 方法，可以参考华为云的实现。

torch_npu 接口：`npu_apply_rotary_pos_emb()`。

代码位置：`vllm/model_executor/layers/rotary_embedding.py`。

## 其它

vllm 这边除了 vllm-npu 的开发，就剩下公共特性了，一方面可以在每天刷新的 issue、pr 里面去找可以修的 bug 或者可以重构的代码，另一方面就是源神这个 [<u>RFC</u>](https://github.com/vllm-project/vllm/issues/11162)。

还有就是对关键的一些 pr 可以积极参与 review 啥的。

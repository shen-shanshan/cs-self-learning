# vllm-ascend UT 验证

## 环境准备

```python
# 下载模型
model_dir = snapshot_download('rubraAI/Gemma-1.1-2b-Instruct', cache_dir='/home/sss/models')

# 替换模型
MODEL = "/home/sss/models/rubraAI/Gemma-1.1-2b-Instruct"
MODEL = "/home/sss/models/rubraAI/Gemma-1___1-2b-Instruct"
```

执行 UT 测试：

```bash
pytest xxx.py
pytest xxx.py -sv  # 打印更多信息
```

某些模型需要登陆 hf：

```bash
huggingface-cli login
```

## 测试结果（第二批：2025/03/30）

……

## 测试结果（第一批：2025/02/20）

### test_load ✅

使用 `rubraAI/Gemma-1.1-2b-Instruct` 模型进行测试，原代码中的设置为 `google/gemma-1.1-2b-it`。

### test_abort ✗

`EXPECTED_TOKENS` 最大不能超过 52，原代码中的设置为 250。

查看模型输出：

```python
tasks.append(asyncio.create_task(generate(client, request_id, EXPECTED_TOKENS, True)))

for task in tasks:
    output = await task
    print(output.outputs[0].text)
```

### test_error_handling ✗

```bash
FAILED test_error_handling.py::test_mp_crash_detection - requests.exceptions.HTTPError: The request model: facebook/opt-125m does not exist!
FAILED test_error_handling.py::test_mp_cuda_init - AssertionError: Torch not compiled with CUDA enabled
```

执行指定测试用例（函数）：

```bash
pytest test_error_handling.py::test_mp_cuda_init
pytest test_error_handling.py::test_mp_crash_detection
```

涉及 cuda 硬编码，修改如下：

```python
@pytest.mark.asyncio
async def test_mp_cuda_init():
    # ...
    from vllm.platforms import current_platform
    if current_platform.device_name == "cuda":
        torch.cuda.init()
    # ...
```

### test_tokenizer ✅

### test_tokenizer_group ✅

需要 `pip install ray`。

### test_get_eos ✅

需要登陆 hf，获取模型访问权限。

### test_cached_tokenizer ✅

### test_detokenize ✅

需要登陆 hf，获取模型访问权限。

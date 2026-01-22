# mrope 性能优化

## 图像测试

```bash
# export VLLM_USE_MODELSCOPE=True
vllm serve /root/.cache/modelscope/hub/models/Qwen/Qwen3-Omni-30B-A3B-Thinking \
--dtype bfloat16 \
--limit-mm-per-prompt '{"image": 1}' \
--max-model-len 16384 \
--max-num-batched-tokens 16384 \
--tensor-parallel-size 2
```

```bash
export VLLM_USE_MODELSCOPE=False
export HF_ENDPOINT="https://hf-mirror.com"
vllm bench serve \
--model /root/.cache/modelscope/hub/models/Qwen/Qwen3-Omni-30B-A3B-Thinking \
--backend openai-chat \
--endpoint /v1/chat/completions \
--dataset-name hf \
--hf-split train \
--dataset-path lmarena-ai/vision-arena-bench-v0.1 \
--num-prompts 100 \
--no-stream
```

torch_npu.npu_mrope():

```
============ Serving Benchmark Result ============
Successful requests:                     100       
Failed requests:                         0         
Benchmark duration (s):                  36.17     
Total input tokens:                      16589     
Total generated tokens:                  12737     
Request throughput (req/s):              2.76      
Output token throughput (tok/s):         352.14    
Peak output token throughput (tok/s):    700.00    
Peak concurrent requests:                100.00    
Total token throughput (tok/s):          810.77    
---------------Time to First Token----------------
Mean TTFT (ms):                          12532.30  
Median TTFT (ms):                        12119.49  
P99 TTFT (ms):                           18094.13  
-----Time per Output Token (excl. 1st token)------
Mean TPOT (ms):                          179.07    
Median TPOT (ms):                        182.39    
P99 TPOT (ms):                           213.26    
---------------Inter-token Latency----------------
Mean ITL (ms):                           177.20    
Median ITL (ms):                         142.90    
P99 ITL (ms):                            997.51    
==================================================
```

forward_native():

```
============ Serving Benchmark Result ============
Successful requests:                     100       
Failed requests:                         0         
Benchmark duration (s):                  35.78     
Total input tokens:                      16589     
Total generated tokens:                  12729     
Request throughput (req/s):              2.79      
Output token throughput (tok/s):         355.74    
Peak output token throughput (tok/s):    800.00    
Peak concurrent requests:                100.00    
Total token throughput (tok/s):          819.35    
---------------Time to First Token----------------
Mean TTFT (ms):                          12446.45  
Median TTFT (ms):                        12405.39  
P99 TTFT (ms):                           18111.44  
-----Time per Output Token (excl. 1st token)------
Mean TPOT (ms):                          176.69    
Median TPOT (ms):                        177.27    
P99 TPOT (ms):                           211.81    
---------------Inter-token Latency----------------
Mean ITL (ms):                           174.65    
Median ITL (ms):                         140.33    
P99 ITL (ms):                            1131.78   
==================================================
```

## 语音测试

下载数据集：

```python
# ds = load_dataset(
#     path="zxl/audiobench_datasets",
#     name="alpaca_audio_test",  # subset
#     split="train",
# )

# ddwang2000/MMSU

from datasets import load_dataset

def download():
    ds = load_dataset(
        path="openslr/librispeech_asr",
        name="clean",  # subset
        split="test",
    )
    print(ds)
    print("Downloaded samples:", len(ds))

if __name__ == "__main__":
    download()
```

```bash
# export VLLM_USE_MODELSCOPE=True
vllm serve /root/.cache/modelscope/hub/models/Qwen/Qwen3-Omni-30B-A3B-Thinking \
--max-model-len 16384 \
--max-num-batched-tokens 16384 \
--tensor-parallel-size 2 \
--limit-mm-per-prompt '{"audio": 1}'

# --allowed-local-media-path /path/to/sharegpt4v/images
```

```bash
export VLLM_USE_MODELSCOPE=False
export HF_ENDPOINT="https://hf-mirror.com"

vllm bench serve \
--model /root/.cache/modelscope/hub/models/Qwen/Qwen3-Omni-30B-A3B-Thinking \
--backend openai-chat \
--endpoint /v1/chat/completions \
--dataset-name hf \
--dataset-path openslr/librispeech_asr \
--hf-subset clean \
--hf-split test \
--num-prompts 100 \
--no-stream
```

需要修改 vllm 代码：

```python
dataset_class = ASRDataset
# args.hf_split = "train"
```

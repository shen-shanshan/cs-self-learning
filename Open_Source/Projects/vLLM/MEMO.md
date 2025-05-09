```bash
------------------------------------------------------------------------------
# vllm test
VLLM_USE_V1=0 pytest tests
VLLM_USE_V1=0 pytest tests -sv

# Model path
/home/sss/.cache/modelscope/hub/Qwen/Qwen2.5-0.5B-Instruct
/home/sss/cache/modelscope/models/Qwen/Qwen2.5-7B-Instruct
/home/sss/cache/modelscope/models/deepseek-ai/DeepSeek-V2-Lite-Chat

# Online test
vllm serve Qwen/Qwen2.5-0.5B-Instruct --max_model_len 26240
vllm serve /home/sss/cache/modelscope/models/Qwen/Qwen2.5-7B-Instruct --max_model_len 26240

curl http://localhost:8000/v1/completions \
    -H "Content-Type: application/json" \
    -d '{
        "model": "/home/sss/cache/modelscope/models/Qwen/Qwen2.5-7B-Instruct",
        "prompt": "Hello, my name is",
        "max_tokens": 7,
        "temperature": 0
    }'

# Clear process
ps -ef | grep vllm | cut -c 9-16 | xargs kill -9
ps -ef | grep python | cut -c 9-16 | xargs kill -9

------------------------------------------------------------------------------
# Git 常用操作
git chekcout <commit>
git stash
git stash pop
git stash drop [stash_id]
git stash clear
git clone -b 分支名 仓库地址
git cherry-pick <commitHash>

------------------------------------------------------------------------------
# Docker 常用操作
docker-compose -p sss up -d
docker exec -it <容器名或ID> /bin/bash
docker stop <容器名或ID>
docker restart <容器名或ID>
docker commit <容器名或ID> <镜像名>
docker rm <容器名或ID>

------------------------------------------------------------------------------
# Co-author
Co-authored-by: didongli182 <didongli@huawei.com>

------------------------------------------------------------------------------
# Structured output
pytest tests/v1/entrypoints/llm/test_struct_output_generate.py -sv

/root/.cache/modelscope/hub/models/Qwen/Qwen2.5-0.5B-Instruct

vllm serve /root/.cache/modelscope/hub/models/Qwen/Qwen2.5-0.5B-Instruct --max_model_len 26240 --pipeline-parallel-size 2

curl http://localhost:8000/v1/completions \
    -H "Content-Type: application/json" \
    -d '{
        "model": "/root/.cache/modelscope/hub/models/Qwen/Qwen2.5-0.5B-Instruct",
        "prompt": "Hello, my name is",
        "max_tokens": 7,
        "temperature": 0
    }'

------------------------------------------------------------------------------
```

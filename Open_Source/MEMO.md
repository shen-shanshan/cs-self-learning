```bash
--------------------------------------------------------------------------------
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

# vllm-ascend format
yapf -i <file>
isort <file>  # to solve: Imports are incorrectly sorted and/or formatted.

vllm_ascend/models/qwen3_moe.py
vllm_ascend/ops/fused_moe.py

--------------------------------------------------------------------------------
# Git 常用操作
git chekcout <commit>
git stash
git stash pop
git stash drop [stash_id]
git stash clear
git clone -b 分支名 仓库地址
git cherry-pick <commitHash>

vim ~/.gitconfig  # 修改 [alias] section.
# 快速下载 PR
pr = "!f() { git fetch -fu ${2:-$(git remote |grep ^upstream || echo origin)} refs/pull/$1/head:pr/$1 && git checkout pr/$1; }; f"
# 快速将 Pull Request ID 为 736 的代码下载到本地
git pr 736
# 参考链接：https://github.com/Yikun/yikun.github.com/issues/89

git config --global user.email "467638484@qq.com"
git config --global user.name "shen-shanshan"

--------------------------------------------------------------------------------
# Docker 常用操作
# cd /data/disk3/sss/docker
docker-compose -p sss up -d
docker exec -it <容器名或ID> /bin/bash
docker stop <容器名或ID>
docker restart <容器名或ID>
docker commit <容器名或ID> <镜像名>
docker rm <容器名或ID>

docker run --rm \
--name vllm-ascend-sss \
--device /dev/davinci6 \
--device /dev/davinci_manager \
--device /dev/devmm_svm \
--device /dev/hisi_hdc \
-v /usr/local/dcmi:/usr/local/dcmi \
-v /usr/local/bin/npu-smi:/usr/local/bin/npu-smi \
-v /usr/local/Ascend/driver/lib64/:/usr/local/Ascend/driver/lib64/ \
-v /usr/local/Ascend/driver/version.info:/usr/local/Ascend/driver/version.info \
-v /etc/ascend_install.info:/etc/ascend_install.info \
-v /root/.cache:/root/.cache \
-it m.daocloud.io/quay.io/ascend/vllm-ascend:v0.7.3 bash

docker exec -it vllm-ascend-sss /bin/bash

--------------------------------------------------------------------------------
# V1
VLLM_USE_V1=

if __name__ == "__main__":

Co-authored-by: didongli182 <didongli@huawei.com>
Co-authored-by: zouyida2002 <didongli@huawei.com>

--------------------------------------------------------------------------------
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

--------------------------------------------------------------------------------
# 系统配置
apt-get update
apt-get install sudo
# install curl
sudo apt update
sudo apt upgrade
sudo apt install curl

source /home/sss/Ascend/ascend-toolkit/set_env.sh
source /home/sss/Ascend/nnal/atb/set_env.sh

source /usr/local/Ascend/ascend-toolkit/set_env.sh
source /usr/local/Ascend/nnal/atb/set_env.sh

--------------------------------------------------------------------------------
export USE_OPTIMIZED_MODEL=0

vllm serve /home/sss/.cache/modelscope/hub/models/Qwen/Qwen2___5-VL-7B-Instruct \
--dtype bfloat16 \
--max_model_len 32768 \
--max-num-batched-tokens 32768

curl http://localhost:8000/v1/chat/completions \
    -H "Content-Type: application/json" \
    -d '{
    "model": "/home/sss/.cache/modelscope/hub/models/Qwen/Qwen2___5-VL-7B-Instruct",
    "messages": [
    {"role": "system", "content": "You are a helpful assistant."},
    {"role": "user", "content": [
        {"type": "image_url", "image_url": {"url": "https://modelscope.oss-cn-beijing.aliyuncs.com/resource/qwen.png"}},
        {"type": "text", "text": "What is the text in the illustrate?"}
    ]}
    ]
    }'

--------------------------------------------------------------------------------
# vllm 环境变量
VLLM_USE_V1
VLLM_USE_MODELSCOPE

--------------------------------------------------------------------------------
I have rebased on the latest main and nothing changed.

--------------------------------------------------------------------------------
# SSH
ssh user@IP -p port

# 将 id_rsa.pub 公钥放在服务器上的 authorized_keys 文件中
vim /root/.ssh/authorized_keys

--------------------------------------------------------------------------------
# VSCode 快捷键
折叠所有：Ctrl/Cmd + K + 0
展开所有：Ctrl/Cmd + K + J

--------------------------------------------------------------------------------
```

```bash
Traceback (most recent call last):
  File "/home/sss/software/miniconda3/envs/vllm-v1/bin/vllm", line 8, in <module>
    sys.exit(main())
  File "/home/sss/github/vllm-project/vllm/vllm/entrypoints/cli/main.py", line 50, in main
    cmd.subparser_init(subparsers).set_defaults(
  File "/home/sss/github/vllm-project/vllm/vllm/entrypoints/cli/serve.py", line 101, in subparser_init
    serve_parser = make_arg_parser(serve_parser)
  File "/home/sss/github/vllm-project/vllm/vllm/entrypoints/openai/cli_args.py", line 254, in make_arg_parser
    parser = AsyncEngineArgs.add_cli_args(parser)
  File "/home/sss/github/vllm-project/vllm/vllm/engine/arg_utils.py", line 1582, in add_cli_args
    current_platform.pre_register_and_update(parser)
  File "/home/sss/github/vllm-project/vllm-ascend/vllm_ascend/platform.py", line 80, in pre_register_and_update
    if ASCEND_QUATIZATION_METHOD not in quant_action.choices:
TypeError: argument of type 'NoneType' is not iterable
[ERROR] 2025-06-03-02:53:42 (PID:6005, Device:-1, RankID:-1) ERR99999 UNKNOWN applicaiton exception

_StoreAction(option_strings=['--quantization', '-q'], dest='quantization', nargs=None, const=None, default=None, type=<class 'str'>, choices=None, required=False, help='Method used to quantize the weights. If `None`, we first check the\n`quantization_config` attribute in the model config file. If that is\n`None`, we assume the model weights are not quantized and use `dtype` to\ndetermine the data type of the weights.', metavar=None)
```

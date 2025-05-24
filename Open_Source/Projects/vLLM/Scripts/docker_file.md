```dockerfile
FROM quay.io/ascend/cann:8.1.rc1-910b-openeuler24.03-py3.10

ARG PIP_INDEX_URL="https://mirrors.tuna.tsinghua.edu.cn/pypi/web/simple"
ARG COMPILE_CUSTOM_KERNELS=1

ENV COMPILE_CUSTOM_KERNELS=${COMPILE_CUSTOM_KERNELS}

RUN yum update -y && \
    yum install -y python3-pip git vim wget net-tools gcc gcc-c++ make cmake numactl-devel && \
    rm -rf /var/cache/yum

RUN pip config set global.index-url ${PIP_INDEX_URL}

WORKDIR /workspace

COPY . /vllm-workspace/vllm-ascend/

# Install vLLM
ARG VLLM_REPO=https://github.com/vllm-project/vllm.git
ARG VLLM_TAG=v0.8.5.post1

RUN git clone --depth 1 $VLLM_REPO --branch $VLLM_TAG /vllm-workspace/vllm
# In x86, triton will be installed by vllm. But in Ascend, triton doesn't work correctly. we need to uninstall it.
RUN VLLM_TARGET_DEVICE="empty" python3 -m pip install -e /vllm-workspace/vllm/ --extra-index https://download.pytorch.org/whl/cpu/ && \
    python3 -m pip uninstall -y triton && \
    python3 -m pip cache purge

# Install vllm-ascend
RUN source /usr/local/Ascend/ascend-toolkit/set_env.sh && \
    source /usr/local/Ascend/nnal/atb/set_env.sh && \
    export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/local/Ascend/ascend-toolkit/latest/`uname -i`-linux/devlib && \
    python3 -m pip install -v -e /vllm-workspace/vllm-ascend/ --extra-index https://download.pytorch.org/whl/cpu/ && \
    python3 -m pip cache purge

# Install modelscope (for fast download) and ray (for multinode)
RUN python3 -m pip install modelscope ray && \
    python3 -m pip cache purge

CMD ["/bin/bash"]
```

steps:

```bash
# docker build -t <镜像名称>:<镜像tag> <Dockerfile所在目录>
docker build -t sss_vllm_ascend_euler24:1.0 .
# 查看镜像
docker images

# 启动容器
export IMAGE=quay.io/ascend/vllm-ascend:v0.8.5rc1
docker run --rm \
--name vllm-ascend \
--device /dev/davinci0 \
--device /dev/davinci_manager \
--device /dev/devmm_svm \
--device /dev/hisi_hdc \
-v /usr/local/dcmi:/usr/local/dcmi \
-v /usr/local/bin/npu-smi:/usr/local/bin/npu-smi \
-v /usr/local/Ascend/driver/lib64/:/usr/local/Ascend/driver/lib64/ \
-v /usr/local/Ascend/driver/version.info:/usr/local/Ascend/driver/version.info \
-v /etc/ascend_install.info:/etc/ascend_install.info \
-v /root/.cache:/root/.cache \
-p 8000:8000 \
-it $IMAGE bash

export VLLM_USE_MODELSCOPE=True
```

offline inference:

```python
from vllm import LLM, SamplingParams

prompts = [
    "Hello, my name is",
    "The future of AI is",
]
sampling_params = SamplingParams(temperature=0.8, top_p=0.95)
llm = LLM(model="Qwen/Qwen3-8B", max_model_len=26240)

outputs = llm.generate(prompts, sampling_params)
for output in outputs:
    prompt = output.prompt
    generated_text = output.outputs[0].text
    print(f"Prompt: {prompt!r}, Generated text: {generated_text!r}")
```

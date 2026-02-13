#!/usr/bin/env bash

echo "--------------------------------------------------------------------------"
cd /home/sss/software/miniconda3/envs/vllm-v1/lib/python3.10/site-packages
echo "Find packages of vllm and vllm-ascend:"
ls -alF | grep vllm
echo "Clear ..."
find . -name "*vllm*" | xargs rm -rf
echo "After clear:"
ls -alF | grep vllm

echo "--------------------------------------------------------------------------"
echo "Install vllm ..."
cd /home/sss/github/vllm-project/vllm
VLLM_TARGET_DEVICE=empty pip install . --extra-index https://download.pytorch.org/whl/cpu/

echo "--------------------------------------------------------------------------"
echo "Install vllm-ascend ..."
cd /home/sss/github/vllm-project/vllm-ascend
pip install -e . --extra-index https://download.pytorch.org/whl/cpu/

#!/usr/bin/env bash

docker run \
--privileged=true \
--name sss-vllm-gpu \
--gpus all \
-v /mnt/sfs_turbo:/shared \
-p 8333:22 \
--ipc=host \
--shm-size=8g \
-it sss-vllm-gpu:v1.0 /bin/bash

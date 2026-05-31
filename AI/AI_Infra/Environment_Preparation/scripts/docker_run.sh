#!/usr/bin/env bash

docker run \
--privileged=true \
--name sss-vllm-gpu \
--gpus all \
-v /mnt/sfs_turbo:/shared \
-v /opt/docker_cache/sss:/home/sss-host \
-p 8333:22 \
--ipc=host \
--shm-size=8g \
-it sss-gpu-dev:base /bin/bash

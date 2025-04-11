# FAQ v0.7.3

## General FAQs

### 2. How to get our docker containers?

You can get our containers at `Quay.io`, e.g., [<u>vllm-ascend</u>](https://quay.io/repository/ascend/vllm-ascend?tab=tags) and [<u>cann</u>](https://quay.io/repository/ascend/cann?tab=tags).

Plus, if you are in China, you can config your docker proxy to accelerate downloading:

```bash
vim /etc/docker/daemon.json
# Add `https://quay.io` to `registry-mirrors` and `insecure-mirrors`

vim /etc/systemd/system/docker.service.d/https-proxy.conf
# Config proxy
[Service]
Environment="HTTP_PROXY=xxx"

sudo systemctl daemon-reload
sudo systemctl restart docker
```

### 3. What models does vllm-ascend supports?

Currently, we have already supported `Qwen` / `Deepseek` (V0 only) / `Llama` models, other models we have tested are shown [<u>here</u>](https://vllm-ascend.readthedocs.io/en/latest/user_guide/supported_models.html). Plus, accoding to users' feedback, `gemma3` and `glm4` are not supported yet. Besides, more models need test.

### 4. How to get in touch with our community?

There are many channels that you can communicate with our community developers / users:

- Submit a GitHub [<u>issue</u>](https://github.com/vllm-project/vllm-ascend/issues?page=1)
- Join our [<u>weekly meeting</u>](https://docs.google.com/document/d/1hCSzRTMZhIB8vRq1_qOOjx4c9uYUxvdQvDsMV2JcSrw/edit?tab=t.0#heading=h.911qu8j8h35z) and share your ideas
- Join our [<u>WeChat</u>](https://github.com/vllm-project/vllm-ascend/issues/227) group and ask your quenstions
- Join [<u>vLLM forums</u>](https://discuss.vllm.ai/top?period=monthly) and publish your topics

### 5. What features does vllm-ascend V1 supports?

Find more details [<u>here</u>](https://github.com/vllm-project/vllm-ascend/issues/414).

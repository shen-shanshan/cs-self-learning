```bash
# local
scp -P 8333 images.zip root@139.9.155.20:/media/

# remote
sudo apt update
sudo apt install -y unzip
cd /media
unzip images.zip
```

准备图片：

```bash
#!/usr/bin/env bash

image_base64=$(base64 -w 0 /media/b0.jpg)
cat > /media/image_request.json <<EOF

{
    "model": "/root/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct",
    "messages": [
        {"role": "system", "content": "You are a helpful assistant."},
        {"role": "user", "content": [
            {"type": "image_url", "image_url": {"url": "data:image/jpeg;base64,$image_base64"}},
            {"type": "text", "text": "What is the text in the illustrate?"}
        ]}
    ]
}
EOF
```

```bash
export PYTORCH_NPU_ALLOC_CONF='expandable_segments:True'
export NPU_MEMORY_FRACTION=0.96

vllm serve /root/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct \
--max_model_len 32768 \
--enforce-eager \
--gpu-memory-utilization 0.75


curl -X POST http://localhost:8000/v1/chat/completions \
-H "Content-Type: application/json" \
-d @/media/image_request.json

curl http://xxxx:50000/v1/chat/completions  -H "Content-Type: application/json" -d @/tmp/request.json
```

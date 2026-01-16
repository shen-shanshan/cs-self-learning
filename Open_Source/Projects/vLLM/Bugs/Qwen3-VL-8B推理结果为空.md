```bash
python -m vllm.entrypoints.openai.api_server \
--model /root/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct \
--dtype bfloat16 \
--gpu-memory-utilization 0.9 \
--max-model-len 8192  \
--max_num_batched_tokens 8192

--host 0.0.0.0 \
--port 8000 \
--compilation-config '{"cudagraph_capture_sizes": [1, 2, 4, 8, 16, 32, 64, 128, 256, 512]}'


curl -X POST http://localhost:8000/v1/chat/completions \
    -H "Content-Type: application/json" \
    -d '{
        "model": "/root/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct",
        "messages": [
            {"role": "system", "content": "You are a helpful assistant."},
            {"role": "user", "content": [
                {"type": "image_url", "image_url": {"url": "https://modelscope.oss-cn-beijing.aliyuncs.com/resource/qwen.png"}},
                {"type": "text", "text": "What is the text in the illustrate? How does it look?"}
            ]}
        ],
        "max_tokens": 4096
    }'

max_completion_tokens
```

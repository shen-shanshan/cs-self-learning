```bash
export VLLM_TORCH_PROFILER_DIR="./vllm_profile"

vllm serve /root/.cache/modelscope/hub/models/Qwen/Qwen3-Omni-30B-A3B-Thinking \
--tensor-parallel-size 2 \
--enable-expert-parallel \
--max_model_len 32768 \
--profiler-config '{"profiler": "torch", "torch_profiler_dir": "./vllm_profile", "max_iterations": 2}'

curl -X POST http://localhost:8000/start_profile

curl http://localhost:8000/v1/chat/completions \
-X POST \
-H "Content-Type: application/json" \
-d '{
    "model": "/root/.cache/modelscope/hub/models/Qwen/Qwen3-Omni-30B-A3B-Thinking",
    "messages": [
        {
            "role": "user",
            "content": [
                {
                    "type": "image_url",
                    "image_url": {
                        "url": "https://qianwen-res.oss-cn-beijing.aliyuncs.com/Qwen3-Omni/demo/cars.jpg"
                    }
                },
                {
                    "type": "audio_url",
                    "audio_url": {
                        "url": "https://qianwen-res.oss-cn-beijing.aliyuncs.com/Qwen3-Omni/demo/cough.wav"
                    }
                },
                {
                    "type": "video_url",
                    "video_url": {
                        "url": "https://qianwen-res.oss-cn-beijing.aliyuncs.com/Qwen3-Omni/demo/draw.mp4"
                    }

                },
                {
                    "type": "text",
                    "text":  "Analyze this audio, image, and video together."
                }
            ]
        }
    ],
    "max_tokens": 100
}'

curl -X POST http://localhost:8000/stop_profile

scp -P 8333 root@139.9.155.20:/workspace/vllm_profile/ascend-01_328066_20260122115949617_ascend_pt/ASCEND_PROFILER_OUTPUT/trace_view.json .
```

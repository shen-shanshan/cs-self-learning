from transformers import AutoProcessor
from vllm import LLM, SamplingParams
from qwen_vl_utils import process_vision_info
import gc
import torch
import os
from vllm import LLM, SamplingParams
from vllm.distributed.parallel_state import (
    destroy_distributed_environment,
    destroy_model_parallel
)
from modelscope import Qwen3OmniMoeProcessor
from qwen_omni_utils import process_mm_info
from torch_npu.profiler.profiler import analyse


os.environ["HCCL_BUFFSIZE"] = "1024"

MODEL_PATH = "/root/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct"


def clean_up():
    """Clean up distributed resources and NPU memory"""
    destroy_model_parallel()
    destroy_distributed_environment()
    gc.collect()  # Garbage collection to free up memory
    torch.npu.empty_cache()


def main():
    llm = LLM(
        model=MODEL_PATH,
        max_model_len=16384,
        limit_mm_per_prompt={"image": 2},
        tensor_parallel_size=1,
    )

    sampling_params = SamplingParams(
        max_tokens=100
    )

    image_urls = [
        "https://modelscope.oss-cn-beijing.aliyuncs.com/resource/qwen.png",
        "https://qianwen-res.oss-cn-beijing.aliyuncs.com/Qwen-VL/assets/demo.jpeg",
        "https://qianwen-res.oss-accelerate.aliyuncs.com/Qwen3-VL/qwen3vllogo.png",
        "https://qianwen-res.oss-accelerate.aliyuncs.com/Qwen3-VL/qwen3vl_arc.jpg",
        "https://qianwen-res.oss-cn-beijing.aliyuncs.com/Qwen3-VL/table_thinking_vl_.jpg",
        "https://qianwen-res.oss-accelerate.aliyuncs.com/Qwen3-VL/table_nothinking_vl.jpg",
        "https://qianwen-res.oss-accelerate.aliyuncs.com/Qwen3-VL/qwen3vl_2b_32b_vl_instruct.jpg",
        "https://qianwen-res.oss-accelerate.aliyuncs.com/Qwen3-VL/qwen3vl_2b_32b_vl_thinking.jpg",
        "https://qianwen-res.oss-accelerate.aliyuncs.com/Qwen3-VL/table_nothinking_text.jpg",
        "https://qianwen-res.oss-accelerate.aliyuncs.com/Qwen3-VL/qwen3vl_4b_8b_text_instruct.jpg",
    ]

    processor = AutoProcessor.from_pretrained(MODEL_PATH)

    llm_inputs = []
    for url in image_urls:
        messages = [
            {"role": "system", "content": "You are a helpful assistant."},
            {
                "role": "user",
                "content": [
                    {
                        "type": "image",
                        "image": url,
                        "min_pixels": 224 * 224,
                        "max_pixels": 1280 * 28 * 28,
                    },
                    {"type": "text", "text": "Please provide a detailed description of this image"},
                ],
            },
        ]

        prompt = processor.apply_chat_template(
            messages,
            tokenize=False,
            add_generation_prompt=True,
        )

        image_inputs, _, _ = process_vision_info(messages, return_video_kwargs=True)

        mm_data = {}
        if image_inputs is not None:
            mm_data["image"] = image_inputs

        llm_inputs.append({
            "prompt": prompt,
            "multi_modal_data": mm_data,
        })

    outputs = llm.generate(llm_inputs, sampling_params=sampling_params)

    for i, output in enumerate(outputs):
        generated_text = output.outputs[0].text
        print(f"=== Image {i + 1} ===")
        print(generated_text)
        print()

    del llm
    clean_up()


if __name__ == "__main__":
    main()

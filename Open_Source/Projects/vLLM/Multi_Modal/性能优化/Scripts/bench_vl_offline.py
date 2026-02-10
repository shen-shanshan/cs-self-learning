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
# os.environ["VLLM_TORCH_PROFILER_DIR"] = "./vllm_profile"


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
        limit_mm_per_prompt={"image": 1},
        tensor_parallel_size=2,
        mm_encoder_tp_mode="data",
        profiler_config={
            "profiler": "torch",
            "torch_profiler_dir": "./vllm_profile",
            "max_iterations": 2,
        },
    )

    sampling_params = SamplingParams(
        max_tokens=2
    )

    image_messages = [
        {"role": "system", "content": "You are a helpful assistant."},
        {
            "role": "user",
            "content": [
                {
                    "type": "image",
                    "image": "https://modelscope.oss-cn-beijing.aliyuncs.com/resource/qwen.png",
                    "min_pixels": 224 * 224,
                    "max_pixels": 1280 * 28 * 28,
                },
                {"type": "text", "text": "Please provide a detailed description of this image"},
            ],
        },
    ]

    messages = image_messages

    processor = AutoProcessor.from_pretrained(MODEL_PATH)
    prompt = processor.apply_chat_template(
        messages,
        tokenize=False,
        add_generation_prompt=True,
    )

    image_inputs, _, _ = process_vision_info(messages, return_video_kwargs=True)

    mm_data = {}
    if image_inputs is not None:
        mm_data["image"] = image_inputs

    llm_inputs = {
        "prompt": prompt,
        "multi_modal_data": mm_data,
    }

    llm.start_profile()

    outputs = llm.generate([llm_inputs], sampling_params=sampling_params)

    llm.stop_profile()
    analyse(profiler_path="./vllm_profile")

    generated_text = outputs[0].outputs[0].text

    print(generated_text)

    del llm
    clean_up()


if __name__ == "__main__":
    main()

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

def clean_up():
    """Clean up distributed resources and NPU memory"""
    destroy_model_parallel()
    destroy_distributed_environment()
    gc.collect()  # Garbage collection to free up memory
    torch.npu.empty_cache()


def main():
    os.environ["VLLM_TORCH_PROFILER_DIR"] = "./vllm_profile"

    MODEL_PATH = "/root/.cache/modelscope/hub/models/Qwen/Qwen3-Omni-30B-A3B-Thinking"
    llm = LLM(
        model=MODEL_PATH,
        tensor_parallel_size=2,
        enable_expert_parallel=True,
        distributed_executor_backend="mp",
        limit_mm_per_prompt={'image': 5, 'video': 2, 'audio': 3},
        max_model_len=32768,
    )

    sampling_params = SamplingParams(
        temperature=0.6,
        top_p=0.95,
        top_k=20,
        max_tokens=16384,
    )

    processor = Qwen3OmniMoeProcessor.from_pretrained(MODEL_PATH)
    messages = [
        {
            "role": "user",
            "content": [
                {"type": "video", "video": "https://qianwen-res.oss-cn-beijing.aliyuncs.com/Qwen3-Omni/demo/draw.mp4"},
                {"type": "text", "text": "What can you see and hear? Answer in one sentence."}
            ]
        }
    ]

    text = processor.apply_chat_template(
        messages,
        tokenize=False,
        add_generation_prompt=True
    )
    # 'use_audio_in_video = True' requires equal number of audio and video items, including audio from the video. 
    audios, images, videos = process_mm_info(messages, use_audio_in_video=True)

    inputs = {
        "prompt": text,
        "multi_modal_data": {},
        "mm_processor_kwargs": {"use_audio_in_video": True}
    }
    if images is not None:
        inputs['multi_modal_data']['image'] = images
    if videos is not None:
        inputs['multi_modal_data']['video'] = videos
    if audios is not None:
        inputs['multi_modal_data']['audio'] = audios

    llm.start_profile()

    outputs = llm.generate([inputs], sampling_params=sampling_params)

    llm.stop_profile()
    analyse(profiler_path="./vllm_profile")

    for output in outputs:
        prompt = output.prompt
        generated_text = output.outputs[0].text
        print(f"Prompt: {prompt!r}, Generated text: {generated_text!r}")

    del llm
    clean_up()


if __name__ == "__main__":
    main()

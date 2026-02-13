# pip install qwen_vl_utils
# pip install pillow
# export OOM_SNAPSHOT_ENABLE=1
# export OOM_SNAPSHOT_PATH=""

import base64
import os
import gc
import json
import requests
from pathlib import Path
import mimetypes
from transformers import AutoProcessor
import datetime
from PIL import Image

import torch
import torch_npu
# from torch_npu.contrib import transfer_to_npu
from vllm import LLM, SamplingParams
from vllm.utils.mem_constants import GiB_bytes
from vllm.distributed.parallel_state import (destroy_distributed_environment,
                                             destroy_model_parallel)


MODEL_PATH = "/root/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct"
IMAGE_PATH_1 = "/home/sss/images"
IMAGE_PATH_2 = "/home/sss/images-2"
IMAGE_PATH_3 = "/home/sss/large_images"


def process_folder_images(folder_path):
    folder = Path(folder_path)

    image_extensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"}
    image_files = []
    for ext in image_extensions:
        image_files.extend(folder.glob(f"*{ext}"))
        image_files.extend(folder.glob(f"*{ext.upper()}"))
    
    if not image_files:
        print(f"No image files found in {folder_path}.")
        return []
    print(f"Found {len(image_files)} image files.")

    return image_files


def encode_image(image_path):
    try:
        with open(image_path, "rb") as image_file:
            encoded_string = base64.b64encode(image_file.read()).decode('utf-8')

            mime_type, _ = mimetypes.guess_type(image_path)
            if mime_type is None:
                mime_type = "image/jpeg"
            
            data_url = f"data:{mime_type};base64,{encoded_string}"
            return data_url
    except Exception as e:
        print(f"Error encoding image {image_path}: {e}")
        return None


def resize_and_pad_4096(
    img: Image.Image,
    target_size: int = 4096,
    fill_color=(0, 0, 0),
) -> Image.Image:
    """
    等比 resize + pad 到 4096x4096
    - 保持宽高比
    - 最终 shape 恒定
    """
    if img.mode != "RGB":
        img = img.convert("RGB")

    w, h = img.size

    # 等比缩放比例
    scale = min(target_size / w, target_size / h)
    new_w = int(round(w * scale))
    new_h = int(round(h * scale))

    # resize
    img_resized = img.resize((new_w, new_h), Image.BICUBIC)

    # pad 到固定尺寸
    canvas = Image.new("RGB", (target_size, target_size), fill_color)
    left = (target_size - new_w) // 2
    top = (target_size - new_h) // 2
    canvas.paste(img_resized, (left, top))

    return canvas


def send_request(llm, sampling_params, image_path):
    image = Image.open(image_path).convert("RGB")
    image = resize_and_pad_4096(image)
    
    questions = [
        "What is the content of this image?",
        # "Describe the content of this image in detail.",
        # "What's in the image?",
        # "Where is this image taken?",
    ]
    placeholder = "<|image_pad|>"
    prompts = [
        (
            "<|im_start|>system\nYou are a helpful assistant.<|im_end|>\n"
            f"<|im_start|>user\n<|vision_start|>{placeholder}<|vision_end|>"
            f"{question}<|im_end|>\n"
            "<|im_start|>assistant\n"
        )
        for question in questions
    ]

    # Single inference
    # uuid = "uuid_0"
    inputs = {
        "prompt": prompts[0],
        "multi_modal_data": {"image": image},
        # "multi_modal_uuids": {modality: uuid},
    }

    outputs = llm.generate(inputs, sampling_params=sampling_params)
    # outputs = llm.generate([llm_inputs], sampling_params=sampling_params)
    for o in outputs:
        generated_text = o.outputs[0].text
        print(generated_text)


def bench_vl():
    # Pre-process image input
    image_files = process_folder_images(IMAGE_PATH_1)

    # torch_npu.npu.memory._record_memory_history(max_entries=100000)

    # NOTE: The default `max_num_seqs` and `max_model_len` may result in OOM on lower-end GPUs.
    llm = LLM(
        model=MODEL_PATH,
        max_model_len=65536,
        max_num_seqs=8,
        max_num_batched_tokens=32768,
        gpu_memory_utilization=0.95,
        tensor_parallel_size=2,
        # enforce_eager=True,
        # limit_mm_per_prompt={"image": 10},
    )
    sampling_params = SamplingParams(max_tokens=1024)

    total = len(image_files)
    cnt = 1
    for image_path in image_files:
        print(f"{'-' * 100}")
        print(f"{cnt}/{total}:")
        print(f"Processing: {image_path.name}")
        send_request(llm, sampling_params, image_path)
        cnt += 1
        # torch_npu.npu.memory._dump_snapshot("./mem_snapshot/bench_vl.pickle")
        # torch_npu.npu.memory._record_memory_history(enabled=None)
    
    del llm

def clean_up():
    destroy_model_parallel()
    destroy_distributed_environment()
    gc.collect()
    torch.npu.empty_cache()


if __name__ == "__main__":
    # torch.npu.set_device(0)
    bench_vl()
    clean_up()

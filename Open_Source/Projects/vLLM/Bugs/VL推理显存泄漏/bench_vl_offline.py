# pip install qwen_vl_utils
# pip install pillow
# export OOM_SNAPSHOT_ENABLE=1
# export OOM_SNAPSHOT_PATH=""

import base64
import os
import json
import requests
from pathlib import Path
import mimetypes
from transformers import AutoProcessor
import datetime
from PIL import Image

import torch_npu
from torch_npu.contrib import transfer_to_npu
from vllm import LLM, SamplingParams
from qwen_vl_utils import process_vision_info


MODEL_PATH = "/root/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct"
IMAGE_PATH_1 = "/home/sss/images"
IMAGE_PATH_2 = "/home/sss/large_images"


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


def send_request(llm, sampling_params, image_path):
    image_data = self.encode_image(image_path)
    if image_data is None:
        return None

    image_messages = [
        {"role": "system", "content": "You are a helpful assistant."},
        {"role": "user", "content": [
            {"type": "image_url", "image_url": {"url": image_data}},
            {"type": "text", "text": "What is the text in the illustrate?"},
        ]},
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
    outputs = llm.generate([llm_inputs], sampling_params=sampling_params)
    generated_text = outputs[0].outputs[0].text
    print(generated_text)


def bench_vl():
    # Pre-process image input
    image_files = process_folder_images(IMAGE_PATH_1)

    # NOTE: The default `max_num_seqs` and `max_model_len` may result in OOM on lower-end GPUs.
    llm = LLM(
        model=MODEL_PATH,
        max_model_len=65536,
        max_num_seqs=8,
        max_num_batched_tokens=32768,
        gpu_memory_utilization=0.95,
        # limit_mm_per_prompt={"image": 10},
    )

    total = len(image_files)
    cnt = 1
    for image_path in image_files:
        print(f"{'-' * 100}")
        print(f"{cnt}/{total}:")
        print(f"Processing: {image_path.name}")
        send_request(llm, sampling_params, image_path)
        cnt += 1


if __name__ == "__main__":
    bench_vl()

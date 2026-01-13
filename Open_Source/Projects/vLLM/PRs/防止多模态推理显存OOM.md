# Fix multi-modal inference OOM issues by setting `expandable_segments:True`

As mentioned in https://github.com/vllm-project/vllm-ascend/issues/5339, multi-modal inference on vllm-ascend may lead to OOM issues in some scenarios.

After our analysis, this is due to the memory fragmentation caused by frequent dynamic memory size adjustments during runtime. During the inference, the figure for non-torch memory see a gradual increase from around 1G to over 4G until the OOM issue occurs.

We find that this problem can be solved by just directly setting `PYTORCH_NPU_ALLOC_CONF=expandable_segments:True`. Find more details at https://docs.vllm.ai/projects/ascend/en/latest/faqs.html#how-to-handle-the-out-of-memory-issue. Thus, we decide to set this value by default, except RL (sleep mode) scenarios.

It's also worthy to note that this environment variable may have more than one key-value pairs. We should append `",expandable_segments:True"` to the current configs.

For example:

```python
PYTORCH_NPU_ALLOC_CONF = "page_size:1g" + ",expandable_segments:True".
```

> [!NOTE]
> `max_split_size_mb` or `garbage_collection_threshold` cannot be enabled together with `expandable_segments=True`.

---

I have build a dataset consisting of my own photographs, which can stably reproduce this OOM issue on Qwen3-VL serie models.

After apply this PR, this problem has been resolved and the amount of non-torch memory will keep stable at around 1G throughout the whole inference.

<details>
<summary>scripts</summary>

```python
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
from vllm import LLM, SamplingParams
from vllm.utils.mem_constants import GiB_bytes
from vllm.distributed.parallel_state import (destroy_distributed_environment,
                                             destroy_model_parallel)


MODEL_PATH = "/root/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct"
IMAGE_PATH = "/home/sss/images"


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


def send_request(llm, sampling_params, image_path, cnt):
    image = Image.open(image_path).convert("RGB")
    
    questions = [
        "What is the content of this image?",
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
    inputs = {
        "prompt": prompts[0],
        "multi_modal_data": {"image": image},
    }

    outputs = llm.generate(inputs, sampling_params=sampling_params)
    for o in outputs:
        generated_text = o.outputs[0].text
        print(generated_text)


def bench_vl():
    # Pre-process image input
    image_files = process_folder_images(IMAGE_PATH)

    # torch_npu.npu.memory._record_memory_history(max_entries=100000)

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
        send_request(llm, sampling_params, image_path, cnt)
        cnt += 1
    
    del llm

def clean_up():
    destroy_model_parallel()
    destroy_distributed_environment()
    gc.collect()
    torch.npu.empty_cache()


if __name__ == "__main__":
    bench_vl()
    clean_up()
```

</details>

---

```
# Set this environment variable to optimize NPU memory management.
# Find more details at https://docs.vllm.ai/projects/ascend/en/latest/faqs.html#how-to-handle-the-out-of-memory-issue
"PYTORCH_NPU_ALLOC_CONF":
lambda: os.getenv("PYTORCH_NPU_ALLOC_CONF", "expandable_segments:True"),

if model_config and not model_config.enable_sleep_mode:
    os.environ["PYTORCH_NPU_ALLOC_CONF"] = "expandable_segments:True"

PYTORCH_NPU_ALLOC_CONF=max_split_size_mb:256

`max_split_size_mb` or `garbage_collection_threshold`, cannot be enabled with `expandable_segments`, please set `expandable_segments` to `False`.
```

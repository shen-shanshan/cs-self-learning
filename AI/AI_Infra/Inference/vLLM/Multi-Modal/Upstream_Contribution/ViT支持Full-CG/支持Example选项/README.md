Add `--enable-vit-cuda-graph` option:

```bash
export VLLM_USE_MODELSCOPE=False
export HF_ENDPOINT="https://hf-mirror.com"
export CUDA_VISIBLE_DEVICES=7

python examples/offline_inference/vision_language.py -m qwen3_vl --modality "image" --enable-vit-cuda-graph
python examples/offline_inference/vision_language.py -m qwen3_vl --modality "video" --enable-vit-cuda-graph
python examples/offline_inference/vision_language.py -m qwen3_vl --modality "image+video" --enable-vit-cuda-graph
```

```
# --modality "image":
--------------------------------------------------
This image captures a beautiful and iconic scene from Tokyo, Japan, featuring the **Tokyo Skytree** framed by blooming **cherry blossoms** (sakura).

Here's a breakdown of the content:

*   **Foreground:** The image is dominated by the vibrant pink and white blossoms of cherry trees
--------------------------------------------------
This image captures a beautiful and iconic scene, likely from Japan, featuring:

- **Cherry Blossoms (Sakura)**: The foreground is filled with delicate pink cherry blossoms in full bloom, framing the view. The branches create a natural, organic frame, with some flowers in sharp focus and others softly blurred
--------------------------------------------------
This image captures a beautiful and iconic scene: the **Tokyo Skytree** viewed through the blossoming branches of **cherry blossom trees** (sakura).

Here's a breakdown of the content:

*   **Foreground:** The frame is filled with the delicate, pink petals of cherry blossoms. The branches
--------------------------------------------------
This image captures a beautiful and iconic scene: the **Tokyo Skytree**, Japan’s tallest tower, viewed through a canopy of blooming **cherry blossoms** (sakura).

Here’s a breakdown of the content:

*   **Foreground:** The image is framed by the dark, silhouetted
--------------------------------------------------

# --modality "video":
--------------------------------------------------
The video is funny because it shows a baby wearing glasses and pretending to read a book, which is an adorable and unexpected sight. The baby's serious demeanor while holding the book and trying to "read" it adds to the humor, as it's clear the baby doesn't actually understand the text. The contrast between the
--------------------------------------------------
The video is funny because it shows a baby wearing glasses and pretending to read a book. The baby's serious and focused expression, combined with the absurdity of a baby wearing glasses and trying to read, creates a humorous contrast. The baby's actions, such as turning the pages and looking at the book with concentration,
--------------------------------------------------
The video is funny because it shows a baby wearing glasses and pretending to read a book. The baby's serious and focused expression, combined with the absurdity of a baby wearing glasses and trying to read, creates a humorous contrast. The baby's actions, such as flipping through the pages and occasionally looking up as if trying
--------------------------------------------------
The video is funny because it shows a baby wearing glasses and pretending to read a book, which is an adorable and unexpected sight. The baby's serious demeanor while reading adds to the humor, as it contrasts with the typical image of a baby engaging in such an activity. The baby's actions, such as flipping through the
--------------------------------------------------

# --modality "image+video":
--------------------------------------------------
The image shows a baby sitting on a bed, wearing glasses, and reading a book. The baby is focused on the book, turning the pages with curiosity and interest. The baby's glasses add a touch of charm and humor to the scene, as it appears to be taking the act of reading very seriously.

In the video, the baby continues to read the book, turning the pages and occasionally looking up, as if pondering the content. The baby's concentration and engagement with the book are evident
--------------------------------------------------
The image shows a baby wearing glasses, sitting on a bed and reading a book. The baby is dressed in a light blue shirt and pink pants. The baby is holding the book with both hands and appears to be focused on reading. The background shows a bedroom with a crib and some clothes on the bed.

In the video, the baby continues to read the book, turning the pages and occasionally looking up. The baby seems to be enjoying the activity and is fully engaged in reading. The video captures
--------------------------------------------------
The image shows a baby girl sitting on a bed, wearing glasses and reading a book. She is surrounded by a cozy bedroom setting with a crib and clothes in the background.

In the video, the baby girl continues to read the book, turning the pages and occasionally looking up. She seems to be enjoying her reading time, fully immersed in the story. The video captures the innocence and curiosity of a young child engaging in a quiet, intellectual activity.
--------------------------------------------------
The image shows a baby girl sitting on a bed, wearing glasses and reading a book. She is focused on the book, turning the pages and occasionally looking up. The background includes a crib and some clothes on the bed.

In the video, the baby girl continues to read the book, turning the pages and looking at the text. She occasionally looks up and smiles, showing her enjoyment of the activity. The video captures the baby's concentration and curiosity as she engages with the book.
--------------------------------------------------
```

---
In examples/offline_inference/vision_language.py, maybe_add_vit_cuda_graph_compilation_config hardcodes encoder_cudagraph_token_budgets: [512, 1024, 1536, 2048, 2560, 3072, 3584, 4096]. This list has a fixed max of 4096 tokens regardless of which model is used. Different models (and their scheduler configs) can have very different max_num_batched_tokens values, so a single hardcoded list is both arbitrary and potentially wrong for non-default configurations.

The engine already has an auto-inference path in EncoderCudaGraphManager.__init__: when encoder_cudagraph_token_budgets is left empty (the default), it calls model.get_encoder_cudagraph_budget_range(vllm_config) to get (min_budget, max_budget) and then runs _generate_budgets(min_budget, max_budget) to produce a power-of-2 sequence capped at the scheduler's max_num_batched_tokens. We just need to stop providing the hardcoded list so the auto path activates.

Budgets are computed as a power-of-2 sequence from min_budget up to scheduler_config.max_num_batched_tokens, which adapts correctly across models.

```bash
[
    512,
    1024,
    1536,
    2048,
    2560,
    3072,
    3584,
    4096,
],
Using max model len 4096
Chunked prefill is enabled with max_num_batched_tokens=8192.

# image:
EncoderCudaGraphManager initialized with budgets=[64, 128, 256, 512, 1024, 2048, 4096, 8192]
# video:

```

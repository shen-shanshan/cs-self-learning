```bash
cd ~/github/vllm/
source .venv/bin/activate
# export VLLM_USE_MODELSCOPE=True
export VLLM_USE_MODELSCOPE=False
export HF_ENDPOINT="https://hf-mirror.com"

# def run_qwen3_vl(
python examples/offline_inference/vision_language.py \
-m qwen3_vl \
--modality "image+video" \
--time-generate

--modality "video" \
--num-prompts 1 \
```

```python
def run_qwen3_vl(questions: list[str], modality: str) -> ModelRequestData:
    model_name = "/shared/models/modelscope/models/Qwen/Qwen3-VL-8B-Instruct"
    mm_limit = {"image": 1, "video": 1} if modality == "image+video" else {modality: 1}
    engine_args = EngineArgs(
        model=model_name,
        max_model_len=4096,
        max_num_seqs=5,
        mm_processor_kwargs={
            "min_pixels": 28 * 28,
            "max_pixels": 1280 * 28 * 28,
            "fps": 1,
        },
        limit_mm_per_prompt=mm_limit,
        compilation_config={
            "cudagraph_mm_encoder": True,
            "encoder_cudagraph_token_budgets": [512, 1024, 1536, 2048, 2560, 3072, 3584, 4096, 4864],
            "encoder_cudagraph_max_vision_items_per_batch": 8,
            "encoder_cudagraph_max_frames_per_batch": 64,
        }
    )
```

```bash
python examples/offline_inference/vision_language.py -m qwen3_vl --modality "image+video"

# outputs:
--------------------------------------------------
The image shows a baby sitting on a bed, wearing glasses, and reading a book. The baby is focused on the book, turning the pages with curiosity and interest. The baby's glasses add a charming and adorable touch to the scene, making it seem like the baby is taking the reading activity seriously. The baby's movements are gentle and deliberate as they turn the pages, indicating a sense of engagement and enjoyment in the activity. The overall atmosphere of the image is cozy and heartwarming, capturing a sweet moment of a baby exploring the world of books.

In the video, the baby continues to read the book, occasionally looking up and smiling, showing their enjoyment and fascination with the story. The baby's glasses remain on, adding to the charm of the scene. The baby's movements are gentle and deliberate as they turn the pages, indicating a sense of engagement and enjoyment in the activity. The overall atmosphere of the video is cozy and heartwarming, capturing a sweet moment of a baby exploring
--------------------------------------------------
The image shows a baby wearing glasses, sitting on a bed and reading a book. The baby is dressed in a light blue shirt and pink pants. The baby is holding the book with both hands and appears to be focused on reading. The background shows a bedroom with a crib and some clothes on the bed.

In the video, the baby continues to read the book, turning the pages and occasionally looking up. The baby seems to be enjoying the activity and is fully engaged in reading. The video captures the baby's concentration and curiosity as they explore the book.
--------------------------------------------------
The image shows a baby girl sitting on a bed, wearing glasses and reading a book. She is surrounded by a cozy bedroom setting with a crib and clothes in the background.

In the video, the baby girl continues to read the book, turning the pages and occasionally looking up. She seems to be enjoying her reading time, fully immersed in the story. The video captures a sweet and adorable moment of a young child engaging in a quiet, intellectual activity.
--------------------------------------------------
The image shows a baby girl sitting on a bed, wearing glasses and reading a book. She is focused on the book, turning the pages and occasionally looking up. The background includes a crib and some clothes on the bed.

In the video, the baby girl continues to read the book, turning the pages and looking at the text. She occasionally looks up and smiles, showing her enjoyment of the activity. The video captures the baby's concentration and curiosity as she engages with the book.
--------------------------------------------------
```

ViT CG:

```bash
python examples/offline_inference/vision_language.py -m qwen3_vl --modality "image+video" --time-generate

--------------------------------------------------
-- generate time = 6.013127326965332
--------------------------------------------------
--------------------------------------------------
The image shows a baby girl sitting on a bed, wearing glasses and reading a book. She is focused on the book, turning the pages with her small hands. The room appears to be a bedroom, with a crib and some clothes visible in the background.

In the video, the baby girl continues to read the book, occasionally looking up and smiling. She seems to be enjoying her reading time, and her glasses add a touch of charm to her appearance. The video captures a sweet and innocent moment of a child engaging in a quiet activity.
--------------------------------------------------
The image shows a baby girl sitting on a bed, wearing glasses, and reading a book.

The video shows the same baby girl continuing to read the book, turning the pages, and occasionally looking up. She appears to be very focused on the book and is enjoying her reading time.
--------------------------------------------------
The image shows a baby girl sitting on a bed, wearing glasses, and reading a book. She is focused on the book, turning the pages with her small hands. The background includes a crib and some clothes, suggesting a cozy and comfortable setting.

In the video, the baby girl continues to read the book, occasionally looking up and smiling. She seems to be enjoying the activity and is fully engaged in the story. The video captures the innocence and curiosity of a young child exploring the world of books.
--------------------------------------------------
The image shows a baby sitting on a bed, wearing glasses, and reading a book. The baby is dressed in a light blue shirt and pink pants. The background includes a crib and some clothes on the bed.

In the video, the baby continues to read the book, turning the pages and occasionally looking up. The baby seems to be enjoying the activity and is focused on the book. The video captures the baby's concentration and curiosity as they explore the book.
--------------------------------------------------
```

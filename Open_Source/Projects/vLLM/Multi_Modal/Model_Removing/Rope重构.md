apply_rotary_pos_emb_vision
dispatch_rotary_emb_function

```python
from vllm.model_executor.layers.rotary_embedding.common import (
    ApplyRotaryEmb,
)

# Qwen2-VL
def apply_rotary_pos_emb_vision(
    t: torch.Tensor, cos: torch.Tensor, sin: torch.Tensor
) -> torch.Tensor:
    rotary_emb_function = dispatch_rotary_emb_function(
        default=partial(apply_rotary_emb_torch, is_neox_style=True)
    )
    output = rotary_emb_function(t, cos, sin).type_as(t)
    return output
```

apply_rotary_emb_dispatch

```python
self.apply_rotary_emb = ApplyRotaryEmb(
    is_neox_style=self.is_neox_style,
    is_unsqueeze=True,
)

        from vllm_ascend.vllm_debug import vllm_debug
        info = {
            "x.shape": x.shape,
            "q.shape": q.shape,  # [1, 176, 8, 80]
            "cos.shape": cos.shape,  # [1, 176, 1, 80]
            "sin.shape": sin.shape,
            # "out.shape": output.shape,
        }
        vllm_debug(info, print_once=True)
```

apply_rotary_emb_torch

```bash
(Worker_TP0 pid=365847) q.shape: torch.Size([1, 176, 8, 80])
(Worker_TP0 pid=365847) cos.shape: torch.Size([1, 176, 1, 80])
(Worker_TP0 pid=365847) sin.shape: torch.Size([1, 176, 1, 80])



# 2.5vl
{"id":"chatcmpl-9ab4de23690c85aa","object":"chat.completion","created":1764748509,"model":"/root/.cache/modelscope/hub/models/Qwen/Qwen2.5-VL-7B-Instruct","choices":[{"index":0,"message":{"role":"assistant","content":"The text in the image reads \"TONGYI Qwen.\" The word \"TONGYI\" is written in blue, and \"Qwen\" is written in gray. The font appears to be modern and clean, with \"TONGYI\" being slightly larger than \"Qwen.\" The design includes a geometric, abstract shape on the left side of the logo, which complements the text.","refusal":null,"annotations":null,"audio":null,"function_call":null,"tool_calls":[],"reasoning":null,"reasoning_content":null},"logprobs":null,"finish_reason":"stop","stop_reason":null,"token_ids":null}],"service_tier":null,"system_fingerprint":null,"usage":{"prompt_tokens":78,"total_tokens":162,"completion_tokens":84,"prompt_tokens_details":null},"prompt_logprobs":null,"prompt_token_ids":null,"kv_transfer_params":null}

# 3vl
{"id":"chatcmpl-ac8e9b693173368d","object":"chat.completion","created":1764753905,"model":"/root/.cache/modelscope/hub/models/Qwen/Qwen3-VL-8B-Instruct","choices":[{"index":0,"message":{"role":"assistant","content":"The text in the illustration is **“TONGYI Qwen”**.\n\n### How it looks:\n- **“TONGYI”** is written in **uppercase letters** in a **bold, modern sans-serif font**, colored **blue**.\n- **“Qwen”** is written in **lowercase letters** in a **slightly thinner, elegant sans-serif font**, colored **dark gray**.\n- The two lines of text are stacked vertically, with “TONG","refusal":null,"annotations":null,"audio":null,"function_call":null,"tool_calls":[],"reasoning":null,"reasoning_content":null},"logprobs":null,"finish_reason":"length","stop_reason":null,"token_ids":null}],"service_tier":null,"system_fingerprint":null,"usage":{"prompt_tokens":112,"total_tokens":212,"completion_tokens":100,"prompt_tokens_details":null},"prompt_logprobs":null,"prompt_token_ids":null,"kv_transfer_params":null}
```

```python
"""
Args:
    x: [seq_len, num_heads, head_size]
    cos: [seq_len, head_size // 2]
    sin: [seq_len, head_size // 2]
    is_neox_style: Whether to use the Neox-style or GPT-J-style.
"""
def forward_static(): pass
```

https://github.com/vllm-project/flash-attention/blob/main/vllm_flash_attn/layers/rotary.py
https://github.com/vllm-project/flash-attention/blob/main/vllm_flash_attn/ops/triton/rotary.py
https://github.com/Dao-AILab/flash-attention/blob/main/flash_attn/ops/triton/rotary.py

---

模型测试（GPU）：

rednote-hilab/dots.ocr:

```bash
# You need to register model to vllm at first
python3 tools/download_model.py

export hf_model_path=./weights/DotsOCR  # Path to your downloaded model weights, Please use a directory name without periods (e.g., `DotsOCR` instead of `dots.ocr`) for the model save path. This is a temporary workaround pending our integration with Transformers.
export PYTHONPATH=$(dirname "$hf_model_path"):$PYTHONPATH

sed -i '/^from vllm\.entrypoints\.cli\.main import main$/a\
from DotsOCR import modeling_dots_ocr_vllm' `which vllm`  # If you downloaded model weights by yourself, please replace `DotsOCR` by your model saved directory name, and remember to use a directory name without periods (e.g., `DotsOCR` instead of `dots.ocr`) 

# launch vllm server
CUDA_VISIBLE_DEVICES=0 vllm serve /home/sss/.cache/huggingface/hub/models/dots_ocr \
--gpu-memory-utilization 0.95 \
--chat-template-content-format string \
--served-model-name model \
--trust-remote-code


```

Signed-off-by: shen-shanshan <467638484@qq.com>
Co-authored-by: gcanlin <canlinguosdu@gmail.com>

AscendSiluAndMul

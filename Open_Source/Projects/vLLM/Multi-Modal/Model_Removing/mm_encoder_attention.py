from collections.abc import Callable

import torch
import torch.nn.functional as F

import vllm.envs as envs
from vllm.attention.backends.registry import AttentionBackendEnum
from vllm.config import MultiModalConfig
from vllm.logger import init_logger
from vllm.model_executor.custom_op import CustomOp
from vllm.model_executor.models.vision import get_vit_attn_backend
from vllm.platforms import current_platform
from functools import lru_cache, partial

import einops
import torch
import torch.nn as nn
import torch.nn.functional as F
import torch_npu
from transformers.models.qwen2_5_vl.configuration_qwen2_5_vl import \
    Qwen2_5_VLVisionConfig
from transformers.models.qwen2_vl.configuration_qwen2_vl import \
    Qwen2VLVisionConfig
from vllm.attention.backends.registry import AttentionBackendEnum
from vllm.attention.layer import (check_upstream_fa_availability,
                                  maybe_get_vit_flash_attn_backend)
from vllm.model_executor.layers.activation import get_act_and_mul_fn
from vllm.model_executor.layers.layernorm import RMSNorm
from vllm.model_executor.layers.quantization import QuantizationConfig
from vllm.model_executor.layers.rotary_embedding import get_rope
from vllm.model_executor.layers.rotary_embedding.common import (
    apply_rotary_emb_torch, dispatch_rotary_emb_function)
from vllm.model_executor.models.qwen2_5_vl import (
    Qwen2_5_VisionAttention, Qwen2_5_VisionBlock, Qwen2_5_VisionPatchEmbed,
    Qwen2_5_VisionPatchMerger, Qwen2_5_VisionTransformer,
    Qwen2_5_VLForConditionalGeneration, Qwen2_5_VLImageInputs,
    Qwen2_5_VLVideoInputs)
from vllm.model_executor.models.qwen2_vl import (Qwen2VisionAttention,
                                                 Qwen2VisionBlock,
                                                 Qwen2VisionPatchEmbed,
                                                 Qwen2VisionPatchMerger,
                                                 Qwen2VisionTransformer)
from vllm.model_executor.models.utils import cast_overflow_tensors
from vllm.model_executor.models.vision import (
    get_vit_attn_backend, run_dp_sharded_mrope_vision_model)
from vllm.attention.layers.mm_encoder_attention import MMEncoderAttention

import vllm_ascend.envs as envs_ascend
from vllm_ascend.ascend_forward_context import set_ascend_forward_context

MIN_PAD_SIZE = 64  # min_size to pad weight
MAX_PAD_SIZE = 128  # max_size to pad weight


class AscendMMEncoderAttention(MMEncoderAttention):

    def __init__(
        self,
        num_heads: int,
        num_heads_per_partition: int,
        head_size: int,
        scale: float,
        num_kv_heads: int | None = None,
        # This has no effect, it is only here to make it easier to swap
        # between Attention and MultiHeadAttention
        prefix: str = "",
        multimodal_config: MultiModalConfig | None = None,
    ) -> None:
        super().__init__(
            num_heads=num_heads,
            num_heads_per_partition=num_heads_per_partition,
            head_size=head_size,
            scale=scale,
            num_kv_heads=num_kv_heads,
            prefix=prefix,
            multimodal_config=multimodal_config,
        )
    
    def forward_oot(
        self,
        query: torch.Tensor,
        key: torch.Tensor,
        value: torch.Tensor,
        **kwargs,
    ) -> torch.Tensor:
        enable_pad = (envs_ascend.USE_OPTIMIZED_MODEL
                      and self.head_size > MIN_PAD_SIZE
                      and self.head_size < MAX_PAD_SIZE)
        origin_shape = query.shape[-1]

        if enable_pad:
            pad_len = MAX_PAD_SIZE - origin_shape
            # q/k/v: [b * s, head, head_dim] -> [b * s, head, MAX_PAD_SIZE]
            query = F.pad(query, (0, pad_len), mode="constant", value=0)
            key = F.pad(key, (0, pad_len), mode="constant", value=0)
            value = F.pad(value, (0, pad_len), mode="constant", value=0)

        context_layer = torch.empty_like(query)

        # operator requires pta version >= 2.5.1
        torch_npu._npu_flash_attention_unpad(
            query=query,
            key=key,
            value=value,
            seq_len=kwargs["cu_seqlens"],
            scale_value=self.head_size**-0.5,
            num_heads=self.num_heads_per_partition,
            num_kv_heads=self.num_heads_per_partition,
            out=context_layer,
        )

        if enable_pad:
            context_layer = context_layer[..., :origin_shape]

        context_layer = einops.rearrange(context_layer,
                                         "(b s) h d -> s b (h d)",
                                         b=batch_size).contiguous()
        return context_layer

class AscendMMEncoderAttention(MMEncoderAttention):
    def __init__(
        self,
        num_heads: int,
        head_size: int,
        scale: float | None = None,
        num_kv_heads: int | None = None,
        prefix: str = "",
    ) -> None:
        """
        Args:
            num_heads: number of attention heads per partition.
            head_size: hidden_size per attention head.
            scale: scale factor.
            num_kv_heads: number of kv heads.
            prefix: This has no effect, it is only here to make it easier to
                    swap between Attention and MMEncoderAttention.
            multimodal_config: configs for multi-modal.
        """
        super().__init__(
            num_heads=num_heads,
            head_size=head_size,
            scale=scale,
            num_kv_heads=num_kv_heads,
            prefix=prefix,
        )

    def reshape_qkv_to_3d(
        self,
        query: torch.Tensor,
        key: torch.Tensor,
        value: torch.Tensor,
        bsz: int,
        q_len: int,
        kv_len: int,
    ) -> tuple[torch.Tensor, torch.Tensor, torch.Tensor]:
        """
        Reshape query, key, value to 3D tensors:
        (batch_size * seq_len, num_heads, head_size)
        """
        query = query.view(bsz * q_len, self.num_heads, self.head_size)
        key = key.view(bsz * kv_len, self.num_kv_heads, self.head_size)
        value = value.view(bsz * kv_len, self.num_kv_heads, self.head_size)
        self.num_queries_per_kv = self.num_heads // self.num_kv_heads
        if (num_repeat := self.num_queries_per_kv) > 1:
            # Handle MQA and GQA
            key = torch.repeat_interleave(key, num_repeat, dim=1)
            value = torch.repeat_interleave(value, num_repeat, dim=1)

        return query, key, value

    def forward_oot(
        self,
        query: torch.Tensor,
        key: torch.Tensor,
        value: torch.Tensor,
        cu_seqlens: torch.Tensor | None = None,
        max_seqlen: torch.Tensor | None = None,  # No use in ascend npu.
    ):
        bsz, q_len = query.size()[:2]
        kv_len = key.size(1)
        is_reshaped = query.dim() == 4

        # q, k, v: [b, s, head, head_dim] -> [b * s, head, head_dim]
        q, k, v = self.reshape_qkv_to_3d(query, key, value, bsz, q_len, kv_len)

        enable_pad = self.head_size > MIN_PAD_SIZE and self.head_size < MAX_PAD_SIZE

        if enable_pad:
            origin_shape = q.shape[-1]
            pad_len = MAX_PAD_SIZE - origin_shape
            # Merge qkv to reduce the overhead of launching npu pad operation.
            # [3, b*s, head, head_dim]
            qkv = torch.stack([q, k, v], dim=0)
            # qkv: [3, b * s, head, head_dim] -> [3, b * s, head, MAX_PAD_SIZE]
            qkv = F.pad(qkv, (0, pad_len), mode="constant", value=0)
            q, k, v = qkv.unbind(dim=0)

        context_layer = torch.empty_like(q)

        if cu_seqlens is None:
            cu_seqlens = torch.arange(0, (bsz + 1) * q_len, step=q_len, dtype=torch.int32, device=query.device)

        cu_seqlens = torch.diff(cu_seqlens).to("cpu")

        # operator requires pta version >= 2.5.1
        torch_npu._npu_flash_attention_unpad(
            query=q,
            key=k,
            value=v,
            seq_len=cu_seqlens,
            scale_value=self.head_size**-0.5,
            num_heads=self.num_heads,
            num_kv_heads=self.num_kv_heads,
            out=context_layer,
        )

        if enable_pad:
            context_layer = context_layer[..., :origin_shape]

        if is_reshaped:
            context_layer = einops.rearrange(context_layer, "(b s) h d -> b s h d", b=bsz).contiguous()
        else:
            context_layer = einops.rearrange(context_layer, "(b s) h d -> b s (h d)", b=bsz).contiguous()
        return context_layer

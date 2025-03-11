if hasattr(layer, 'quant_method'):
            # TODO: Add attr (num_prefills, prefill_metadata, decode_metadata) to AscendMetadata

            isPrefill = True if attn_metadata.num_prefills > 0 else False
            if isPrefill:
                assert attn_metadata.prefill_metadata is not None
                self.seq_lens_tensor_cpu = torch.from_numpy(
                    np.array(attn_metadata.prefill_metadata.seq_lens).astype(
                        np.int32))
            else:
                assert attn_metadata.decode_metadata is not None
                self.seq_lens_tensor_cpu = torch.from_numpy(
                    np.array(attn_metadata.decode_metadata.seq_lens).astype(
                        np.int32))
            block_tables = attn_metadata.decode_metadata.block_tables if attn_metadata.decode_metadata else None
            # Details of kv_cache arrangement in attention quantization
            # are implemented by quant_method.
            layer.quant_method.apply(layer, query, key, value, kv_cache,
                                     self.scale, self.seq_lens_tensor_cpu,
                                     block_tables, isPrefill, attn_metadata,
                                     output)

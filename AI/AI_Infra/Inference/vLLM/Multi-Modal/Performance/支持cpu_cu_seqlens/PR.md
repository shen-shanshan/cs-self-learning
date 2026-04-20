# Support CPU `cu_seqlens` for MMEncoderAttention in Qwen3-VL

Support CPU `cu_seqlens` for OOT MMEncoderAttention kernels.

To make sure OOT classmethod can be called, we need to get `layer_cls_to_instantiate` from `op_registry_oot` from CustomOp first, then call the classmethod with this OOT registered class.

Find more details at .

---

Ascend PR:

...

support cpu seq_lens for OOT MMEncoderAttention kernels

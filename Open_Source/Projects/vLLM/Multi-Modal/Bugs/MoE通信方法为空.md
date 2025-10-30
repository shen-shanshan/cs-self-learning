```python
is_moe_model()
_select_moe_comm_method()
set_ascend_forward_context(..., moe_comm_type, ...)
moe_comm_type = None
forward_context = get_forward_context()
forward_context.moe_comm_method.prepare()


_MULTI_MODAL_MODEL_ARCH_KEY_WORDS = []

def is_moe_model(vllm_config: VllmConfig):
    global _IS_MOE_MODEL
    if _IS_MOE_MODEL is None:
        model_configs = vllm_config.model_config.hf_config.to_dict()

        if ("VL" in model_configs["architectures"][0]
            and "text_config" in model_configs):
            # Check VL multi-modal models
            _IS_MOE_MODEL = any("experts" in key.lower()
                                for key in model_configs["text_config"])
        elif ("Omni" in model_configs["architectures"][0]
              and "talker_config" in model_configs
              and "text_config" in model_configs["talker_config"]):
             # Check Omni multi-modal models
            _IS_MOE_MODEL = any("experts" in key.lower()
                                for key in model_configs["talker_config"]["text_config"])
        else:
            # Check text-only models
            _IS_MOE_MODEL = any("experts" in key.lower()
                                for key in model_configs)
    return _IS_MOE_MODEL
```

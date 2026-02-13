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


def is_moe_model_by_layer(model: torch.nn.Module) -> bool:
    """Checks if the model is a MoE model by layer"""
    from vllm.model_executor.layers.fused_moe import FusedMoE

    global _IS_MOE_MODEL
    if _IS_MOE_MODEL is None:
        _IS_MOE_MODEL =  bool(any(isinstance(module, FusedMoE) for module in model.modules()))
    return _IS_MOE_MODEL
```

is_moe_model
update_aclgraph_sizes

```python
def is_moe_model(vllm_config: VllmConfig):
    """Checks if the model is a MoE model by config"""
    global _IS_MOE_MODEL
    if _IS_MOE_MODEL is None:
        model_configs = vllm_config.model_config.hf_config.to_dict()
        _IS_MOE_MODEL = _is_contain_expert(model_configs)
    return _IS_MOE_MODEL


def _is_contain_expert(config: dict | str):
    if isinstance(config, dict):
        for k, v in config.items():
            if "expert" in str(k):
                return True
            if _is_contain_expert(v):
                return True
    return False
```

# weight loader 报错修复

```bash
ERROR 03-04 07:43:12 [multiproc_executor.py:783] WorkerProc failed to start.
ERROR 03-04 07:43:12 [multiproc_executor.py:783] Traceback (most recent call last):
ERROR 03-04 07:43:12 [multiproc_executor.py:783]   File "/vllm-workspace/vllm/vllm/v1/executor/multiproc_executor.py", line 754, in worker_main
ERROR 03-04 07:43:12 [multiproc_executor.py:783]     worker = WorkerProc(*args, **kwargs)
ERROR 03-04 07:43:12 [multiproc_executor.py:783]              ^^^^^^^^^^^^^^^^^^^^^^^^^^^
ERROR 03-04 07:43:12 [multiproc_executor.py:783]   File "/vllm-workspace/vllm/vllm/v1/executor/multiproc_executor.py", line 580, in __init__
ERROR 03-04 07:43:12 [multiproc_executor.py:783]     self.worker.load_model()
ERROR 03-04 07:43:12 [multiproc_executor.py:783]   File "/vllm-workspace/vllm-ascend/vllm_ascend/worker/worker.py", line 429, in load_model
ERROR 03-04 07:43:12 [multiproc_executor.py:783]     self.model_runner.load_model()
ERROR 03-04 07:43:12 [multiproc_executor.py:783]   File "/vllm-workspace/vllm-ascend/vllm_ascend/worker/model_runner_v1.py", line 2417, in load_model
ERROR 03-04 07:43:12 [multiproc_executor.py:783]     self.model = get_model(vllm_config=self.vllm_config)
ERROR 03-04 07:43:12 [multiproc_executor.py:783]                  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
ERROR 03-04 07:43:12 [multiproc_executor.py:783]   File "/vllm-workspace/vllm/vllm/model_executor/model_loader/__init__.py", line 136, in get_model
ERROR 03-04 07:43:12 [multiproc_executor.py:783]     return loader.load_model(
ERROR 03-04 07:43:12 [multiproc_executor.py:783]            ^^^^^^^^^^^^^^^^^^
ERROR 03-04 07:43:12 [multiproc_executor.py:783]   File "/vllm-workspace/vllm/vllm/model_executor/model_loader/base_loader.py", line 62, in load_model
ERROR 03-04 07:43:12 [multiproc_executor.py:783]     self.load_weights(model, model_config)
ERROR 03-04 07:43:12 [multiproc_executor.py:783]   File "/vllm-workspace/vllm/vllm/model_executor/model_loader/default_loader.py", line 290, in load_weights
ERROR 03-04 07:43:12 [multiproc_executor.py:783]     loaded_weights = model.load_weights(self.get_all_weights(model_config, model))
ERROR 03-04 07:43:12 [multiproc_executor.py:783]                      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
ERROR 03-04 07:43:12 [multiproc_executor.py:783]   File "/vllm-workspace/vllm/vllm/model_executor/models/qwen3_5.py", line 747, in load_weights
ERROR 03-04 07:43:12 [multiproc_executor.py:783]     return loader.load_weights(weights, mapper=self.hf_to_vllm_mapper)
ERROR 03-04 07:43:12 [multiproc_executor.py:783]            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
ERROR 03-04 07:43:12 [multiproc_executor.py:783]   File "/vllm-workspace/vllm/vllm/model_executor/model_loader/reload/torchao_decorator.py", line 50, in patched_model_load_weights
ERROR 03-04 07:43:12 [multiproc_executor.py:783]     return original_load_weights(self, weights, *args, **kwargs)
ERROR 03-04 07:43:12 [multiproc_executor.py:783]            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
ERROR 03-04 07:43:12 [multiproc_executor.py:783]   File "/vllm-workspace/vllm/vllm/model_executor/models/utils.py", line 344, in load_weights
ERROR 03-04 07:43:12 [multiproc_executor.py:783]     autoloaded_weights = set(self._load_module("", self.module, weights))
ERROR 03-04 07:43:12 [multiproc_executor.py:783]                          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
ERROR 03-04 07:43:12 [multiproc_executor.py:783]   File "/vllm-workspace/vllm/vllm/model_executor/models/utils.py", line 292, in _load_module
ERROR 03-04 07:43:12 [multiproc_executor.py:783]     yield from self._load_module(
ERROR 03-04 07:43:12 [multiproc_executor.py:783]   File "/vllm-workspace/vllm/vllm/model_executor/models/utils.py", line 265, in _load_module
ERROR 03-04 07:43:12 [multiproc_executor.py:783]     loaded_params = module_load_weights(weights)
ERROR 03-04 07:43:12 [multiproc_executor.py:783]                     ^^^^^^^^^^^^^^^^^^^^^^^^^^^^
ERROR 03-04 07:43:12 [multiproc_executor.py:783]   File "/vllm-workspace/vllm/vllm/model_executor/models/qwen3_5.py", line 604, in load_weights
ERROR 03-04 07:43:12 [multiproc_executor.py:783]     return loader.load_weights(weights)
ERROR 03-04 07:43:12 [multiproc_executor.py:783]            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^
ERROR 03-04 07:43:12 [multiproc_executor.py:783]   File "/vllm-workspace/vllm/vllm/model_executor/model_loader/reload/torchao_decorator.py", line 50, in patched_model_load_weights
ERROR 03-04 07:43:12 [multiproc_executor.py:783]     return original_load_weights(self, weights, *args, **kwargs)
ERROR 03-04 07:43:12 [multiproc_executor.py:783]            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
ERROR 03-04 07:43:12 [multiproc_executor.py:783]   File "/vllm-workspace/vllm/vllm/model_executor/models/utils.py", line 344, in load_weights
ERROR 03-04 07:43:12 [multiproc_executor.py:783]     autoloaded_weights = set(self._load_module("", self.module, weights))
ERROR 03-04 07:43:12 [multiproc_executor.py:783]                          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
ERROR 03-04 07:43:12 [multiproc_executor.py:783]   File "/vllm-workspace/vllm/vllm/model_executor/models/utils.py", line 292, in _load_module
ERROR 03-04 07:43:12 [multiproc_executor.py:783]     yield from self._load_module(
ERROR 03-04 07:43:12 [multiproc_executor.py:783]   File "/vllm-workspace/vllm/vllm/model_executor/models/utils.py", line 265, in _load_module
ERROR 03-04 07:43:12 [multiproc_executor.py:783]     loaded_params = module_load_weights(weights)
ERROR 03-04 07:43:12 [multiproc_executor.py:783]                     ^^^^^^^^^^^^^^^^^^^^^^^^^^^^
ERROR 03-04 07:43:12 [multiproc_executor.py:783]   File "/vllm-workspace/vllm/vllm/model_executor/models/qwen3_5.py", line 430, in load_weights
ERROR 03-04 07:43:12 [multiproc_executor.py:783]     weight_loader(param, loaded_weight, shard_id)
ERROR 03-04 07:43:12 [multiproc_executor.py:783]   File "/vllm-workspace/vllm/vllm/model_executor/layers/linear.py", line 743, in weight_loader
ERROR 03-04 07:43:12 [multiproc_executor.py:783]     raise NotImplementedError(
ERROR 03-04 07:43:12 [multiproc_executor.py:783] NotImplementedError: Shard id with multiple indices is not supported in weight_loader, please use weight_loader_v2 instead.
```

```python
from vllm.model_executor.layers.linear import register_weight_loader_v2_supported_method

@register_weight_loader_v2_supported_method
class AscendUnquantizedLinearMethod(UnquantizedLinearMethod):
```

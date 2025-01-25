# vLLM 源码学习

## 总览

- **LLM**（LLMEngine）：offline batched inference
- **AsyncLLMEngine**（LLMEngine）：online serving

LLMEngine -> Executor(1) -> Worker(N) -> ModelRunner(loading and running the model) -> Model(`torch.nn.Module`)

VllmConfig：在全局传递的配置类。

## 对象动态导入

**指定“类的绝对路径”：**

```python
@classmethod
def check_and_update_config(cls, vllm_config: VllmConfig) -> None:
    parallel_config = vllm_config.parallel_config
    scheduler_config = vllm_config.scheduler_config

    if parallel_config.worker_cls == "auto":
        if scheduler_config.is_multi_step:
            if envs.VLLM_USE_V1:
                raise NotImplementedError
            else:
                parallel_config.worker_cls = "vllm.worker.multi_step_worker.MultiStepWorker"
        elif vllm_config.speculative_config:
            if envs.VLLM_USE_V1:
                raise NotImplementedError
            else:
                parallel_config.worker_cls = "vllm.spec_decode.spec_decode_worker.create_spec_worker"
                parallel_config.sd_worker_cls = "vllm.worker.worker.Worker"
        else:
            if envs.VLLM_USE_V1:
                parallel_config.worker_cls = "vllm.v1.worker.gpu_worker.Worker"
            else:
                parallel_config.worker_cls = "vllm.worker.worker.Worker"
```

**根据“类的绝对路径”初始化对象：**

```python
from vllm.utils import (resolve_obj_by_qualname, ...)

def init_worker(self, *args, **kwargs):
    """
    Here we inject some common logic before initializing the worker.
    Arguments are passed to the worker class constructor.
    """
    enable_trace_function_call_for_thread(self.vllm_config)

    # see https://github.com/NVIDIA/nccl/issues/1234
    os.environ['NCCL_CUMEM_ENABLE'] = '0'

    from vllm.plugins import load_general_plugins
    load_general_plugins()

    worker_class = resolve_obj_by_qualname(self.vllm_config.parallel_config.worker_cls)
    self.worker = worker_class(*args, **kwargs)
    assert self.worker is not None
```

`resolve_obj_by_qualname` 原理：

```python
def resolve_obj_by_qualname(qualname: str) -> Any:
    """
    Resolve an object by its fully qualified name.
    """
    module_name, obj_name = qualname.rsplit(".", 1)
    module = importlib.import_module(module_name)
    return getattr(module, obj_name)
```

**参考资料：**

- [<u>python importlib 用法小结</u>](https://zhuanlan.zhihu.com/p/521128790)

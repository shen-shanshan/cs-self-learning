```bash
The model: Qwen/Qwen2.5-VL-7B-Instruct has no revision: main !

modelscope 1.21.1
modelscope<1.23.0
容器：
modelscope 1.22.3

def _snapshot_download(revision: Optional[str] = DEFAULT_MODEL_REVISION)
DEFAULT_MODEL_REVISION = None

revision: Optional[str] = 'main'

/home/sss/.cache/modelscope/hub/models/Qwen/Qwen2.5-VL-7B-Instruct

docker run --rm \
--name vllm-ascend-sss \
--device /dev/davinci6 \
--device /dev/davinci_manager \
--device /dev/devmm_svm \
--device /dev/hisi_hdc \
-v /usr/local/dcmi:/usr/local/dcmi \
-v /usr/local/bin/npu-smi:/usr/local/bin/npu-smi \
-v /usr/local/Ascend/driver/lib64/:/usr/local/Ascend/driver/lib64/ \
-v /usr/local/Ascend/driver/version.info:/usr/local/Ascend/driver/version.info \
-v /etc/ascend_install.info:/etc/ascend_install.info \
-v /root/.cache:/root/.cache \
-it m.daocloud.io/quay.io/ascend/vllm-ascend:v0.7.3 bash

docker exec -it vllm-ascend-sss /bin/bash

vllm serve Qwen/Qwen2.5-VL-7B-Instruct \
--dtype bfloat16 --max_model_len 16384 --max-num-batched-tokens 16384 \
--revision master

curl http://localhost:8000/v1/chat/completions \
    -H "Content-Type: application/json" \
    -d '{
    "model": "Qwen/Qwen2.5-VL-7B-Instruct",
    "messages": [
    {"role": "system", "content": "You are a helpful assistant."},
    {"role": "user", "content": [
        {"type": "image_url", "image_url": {"url": "https://modelscope.oss-cn-beijing.aliyuncs.com/resource/qwen.png"}},
        {"type": "text", "text": "What is the text in the illustrate?"}
    ]}
    ]
    }'

/vllm-workspace/vllm/vllm/transformers_utils

revision = kwargs.get('revision', "None")
print("transformers/processing_utils | revision: " + str(revision))
```

报错信息：

```bash
ERROR 05-13 11:59:07 engine.py:400] The model: Qwen/Qwen2.5-VL-7B-Instruct has no revision: main !
ERROR 05-13 11:59:07 engine.py:400] Traceback (most recent call last):
ERROR 05-13 11:59:07 engine.py:400]   File "/vllm-workspace/vllm/vllm/engine/multiprocessing/engine.py", line 391, in run_mp_engine
ERROR 05-13 11:59:07 engine.py:400]     engine = MQLLMEngine.from_engine_args(engine_args=engine_args,
ERROR 05-13 11:59:07 engine.py:400]   File "/vllm-workspace/vllm/vllm/engine/multiprocessing/engine.py", line 124, in from_engine_args
ERROR 05-13 11:59:07 engine.py:400]     return cls(ipc_path=ipc_path,
ERROR 05-13 11:59:07 engine.py:400]   File "/vllm-workspace/vllm/vllm/engine/multiprocessing/engine.py", line 76, in __init__
ERROR 05-13 11:59:07 engine.py:400]     self.engine = LLMEngine(*args, **kwargs)
ERROR 05-13 11:59:07 engine.py:400]   File "/vllm-workspace/vllm/vllm/engine/llm_engine.py", line 276, in __init__
ERROR 05-13 11:59:07 engine.py:400]     self._initialize_kv_caches()
ERROR 05-13 11:59:07 engine.py:400]   File "/vllm-workspace/vllm/vllm/engine/llm_engine.py", line 421, in _initialize_kv_caches
ERROR 05-13 11:59:07 engine.py:400]     self.model_executor.determine_num_available_blocks())
ERROR 05-13 11:59:07 engine.py:400]   File "/vllm-workspace/vllm/vllm/executor/executor_base.py", line 102, in determine_num_available_blocks
ERROR 05-13 11:59:07 engine.py:400]     results = self.collective_rpc("determine_num_available_blocks")
ERROR 05-13 11:59:07 engine.py:400]   File "/vllm-workspace/vllm/vllm/executor/uniproc_executor.py", line 56, in collective_rpc
ERROR 05-13 11:59:07 engine.py:400]     answer = run_method(self.driver_worker, method, args, kwargs)
ERROR 05-13 11:59:07 engine.py:400]   File "/vllm-workspace/vllm/vllm/utils.py", line 2196, in run_method
ERROR 05-13 11:59:07 engine.py:400]     return func(*args, **kwargs)
ERROR 05-13 11:59:07 engine.py:400]   File "/usr/local/python3.10.17/lib/python3.10/site-packages/torch/utils/_contextlib.py", line 116, in decorate_context
ERROR 05-13 11:59:07 engine.py:400]     return func(*args, **kwargs)
ERROR 05-13 11:59:07 engine.py:400]   File "/vllm-workspace/vllm-ascend/vllm_ascend/worker/worker.py", line 255, in determine_num_available_blocks
ERROR 05-13 11:59:07 engine.py:400]     self.model_runner.profile_run()
ERROR 05-13 11:59:07 engine.py:400]   File "/usr/local/python3.10.17/lib/python3.10/site-packages/torch/utils/_contextlib.py", line 116, in decorate_context
ERROR 05-13 11:59:07 engine.py:400]     return func(*args, **kwargs)
ERROR 05-13 11:59:07 engine.py:400]   File "/vllm-workspace/vllm-ascend/vllm_ascend/worker/model_runner.py", line 1452, in profile_run
ERROR 05-13 11:59:07 engine.py:400]     .dummy_data_for_profiling(self.model_config,
ERROR 05-13 11:59:07 engine.py:400]   File "/vllm-workspace/vllm/vllm/inputs/registry.py", line 336, in dummy_data_for_profiling
ERROR 05-13 11:59:07 engine.py:400]     dummy_data = profiler.get_dummy_data(
ERROR 05-13 11:59:07 engine.py:400]   File "/vllm-workspace/vllm/vllm/multimodal/profiling.py", line 168, in get_dummy_data
ERROR 05-13 11:59:07 engine.py:400]     mm_inputs = self._get_dummy_mm_inputs(seq_len, mm_counts)
ERROR 05-13 11:59:07 engine.py:400]   File "/vllm-workspace/vllm/vllm/multimodal/profiling.py", line 138, in _get_dummy_mm_inputs
ERROR 05-13 11:59:07 engine.py:400]     processor_inputs = factory.get_dummy_processor_inputs(
ERROR 05-13 11:59:07 engine.py:400]   File "/vllm-workspace/vllm/vllm/model_executor/models/qwen2_vl.py", line 944, in get_dummy_processor_inputs
ERROR 05-13 11:59:07 engine.py:400]     hf_processor = self.info.get_hf_processor()
ERROR 05-13 11:59:07 engine.py:400]   File "/vllm-workspace/vllm/vllm/model_executor/models/qwen2_5_vl.py", line 698, in get_hf_processor
ERROR 05-13 11:59:07 engine.py:400]     return self.ctx.get_hf_processor(
ERROR 05-13 11:59:07 engine.py:400]   File "/vllm-workspace/vllm/vllm/inputs/registry.py", line 136, in get_hf_processor
ERROR 05-13 11:59:07 engine.py:400]     return super().get_hf_processor(
ERROR 05-13 11:59:07 engine.py:400]   File "/vllm-workspace/vllm/vllm/inputs/registry.py", line 100, in get_hf_processor
ERROR 05-13 11:59:07 engine.py:400]     return cached_processor_from_config(
ERROR 05-13 11:59:07 engine.py:400]   File "/vllm-workspace/vllm/vllm/transformers_utils/processor.py", line 106, in cached_processor_from_config
ERROR 05-13 11:59:07 engine.py:400]     return cached_get_processor(
ERROR 05-13 11:59:07 engine.py:400]   File "/vllm-workspace/vllm/vllm/transformers_utils/processor.py", line 69, in get_processor
ERROR 05-13 11:59:07 engine.py:400]     processor = processor_factory.from_pretrained(

ERROR 05-13 11:59:07 engine.py:400]   File "/usr/local/python3.10.17/lib/python3.10/site-packages/transformers/processing_utils.py", line 1079, in from_pretrained
ERROR 05-13 11:59:07 engine.py:400]     args = cls._get_arguments_from_pretrained(pretrained_model_name_or_path, **kwargs)
ERROR 05-13 11:59:07 engine.py:400]   File "/usr/local/python3.10.17/lib/python3.10/site-packages/transformers/processing_utils.py", line 1143, in _get_arguments_from_pretrained
ERROR 05-13 11:59:07 engine.py:400]     args.append(attribute_class.from_pretrained(pretrained_model_name_or_path, **kwargs))

ERROR 05-13 11:59:07 engine.py:400]   File "/usr/local/python3.10.17/lib/python3.10/site-packages/modelscope/utils/hf_util.py", line 247, in from_pretrained
ERROR 05-13 11:59:07 engine.py:400]     model_dir = get_model_dir(pretrained_model_name_or_path, None,
ERROR 05-13 11:59:07 engine.py:400]   File "/usr/local/python3.10.17/lib/python3.10/site-packages/modelscope/utils/hf_util.py", line 172, in get_model_dir
ERROR 05-13 11:59:07 engine.py:400]     model_dir = snapshot_download(
ERROR 05-13 11:59:07 engine.py:400]   File "/usr/local/python3.10.17/lib/python3.10/site-packages/modelscope/hub/snapshot_download.py", line 109, in snapshot_download
ERROR 05-13 11:59:07 engine.py:400]     return _snapshot_download(
ERROR 05-13 11:59:07 engine.py:400]   File "/usr/local/python3.10.17/lib/python3.10/site-packages/modelscope/hub/snapshot_download.py", line 257, in _snapshot_download
ERROR 05-13 11:59:07 engine.py:400]     revision_detail = _api.get_valid_revision_detail(
ERROR 05-13 11:59:07 engine.py:400]   File "/usr/local/python3.10.17/lib/python3.10/site-packages/modelscope/hub/api.py", line 600, in get_valid_revision_detail
ERROR 05-13 11:59:07 engine.py:400]     raise NotExistError('The model: %s has no revision: %s !' % (model_id, revision))
ERROR 05-13 11:59:07 engine.py:400] modelscope.hub.errors.NotExistError: The model: Qwen/Qwen2.5-VL-7B-Instruct has no revision: main !

qwen2_5_vl | revision: None
transformers_utils/processor | revision: None
transformers/processing_utils | revision: main
get_model_dir | revision: main
snapshot_download | revision: main
get_valid_revision_detail | revision: main

vim +69 /vllm-workspace/vllm/vllm/transformers_utils/processor.py
```

```python
def get_processor(
    processor_name: str,
    *args: Any,
    revision: Optional[str] = None,
    trust_remote_code: bool = False,
    processor_cls: Union[type[_P], tuple[type[_P], ...]] = ProcessorMixin,
    **kwargs: Any,
) -> _P:
    """Load a processor for the given model name via HuggingFace."""
    # don't put this import at the top level
    # it will call torch.cuda.device_count()
    from transformers import AutoProcessor

    processor_factory = (AutoProcessor if processor_cls == ProcessorMixin or
                         isinstance(processor_cls, tuple) else processor_cls)

    try:
        processor = processor_factory.from_pretrained(
            processor_name,
            *args,
            revision=revision,
            trust_remote_code=trust_remote_code,
            **kwargs,
        )
    except ValueError as e:
        # If the error pertains to the processor class not existing or not
        # currently being imported, suggest using the --trust-remote-code flag.
        # Unlike AutoTokenizer, AutoProcessor does not separate such errors
        if not trust_remote_code:
            err_msg = (
                "Failed to load the processor. If the processor is "
                "a custom processor not yet available in the HuggingFace "
                "transformers library, consider setting "
                "`trust_remote_code=True` in LLM or using the "
                "`--trust-remote-code` flag in the CLI.")
            raise RuntimeError(err_msg) from e
        else:
            raise e

    if not isinstance(processor, processor_cls):
        raise TypeError("Invalid type of HuggingFace processor. "
                        f"Expected type: {processor_cls}, but "
                        f"found type: {type(processor)}")

    return processor


cached_get_processor = lru_cache(get_processor)


def cached_processor_from_config(
    model_config: "ModelConfig",
    processor_cls: Union[type[_P], tuple[type[_P], ...]] = ProcessorMixin,
    **kwargs: Any,
) -> _P:
    return cached_get_processor(
        model_config.model,
        revision=model_config.revision,
        trust_remote_code=model_config.trust_remote_code,
        processor_cls=processor_cls,  # type: ignore[arg-type]
        **_merge_mm_kwargs(model_config, **kwargs),
    )
```

patch:

```python
# __init__.py
import vllm_ascend.patch.patch_transformers_utils  # noqa
```

```python
# patch_transformers_utils.py
from typing import TYPE_CHECKING, Any, Optional, Union

from transformers.processing_utils import ProcessorMixin
from typing_extensions import TypeVar
from vllm.transformers_utils.processor import (get_processor, cached_get_processor, _merge_mm_kwargs)

if TYPE_CHECKING:
    from vllm.config import ModelConfig

_P = TypeVar("_P", bound=ProcessorMixin, default=ProcessorMixin)


def get_processor_with_revision(
    processor_name: str,
    *args: Any,
    revision: Optional[str] = None,
    trust_remote_code: bool = False,
    processor_cls: Union[type[_P], tuple[type[_P], ...]] = ProcessorMixin,
    **kwargs: Any,
) -> _P:
    """Load a processor for the given model name via HuggingFace."""
    # don't put this import at the top level
    # it will call torch.cuda.device_count()
    from transformers import AutoProcessor

    processor_factory = (AutoProcessor if processor_cls == ProcessorMixin or
                         isinstance(processor_cls, tuple) else processor_cls)

    try:
        processor = processor_factory.from_pretrained(
            processor_name,
            *args,
            revision=revision,
            trust_remote_code=trust_remote_code,
            **kwargs,
        )
    except ValueError as e:
        # If the error pertains to the processor class not existing or not
        # currently being imported, suggest using the --trust-remote-code flag.
        # Unlike AutoTokenizer, AutoProcessor does not separate such errors
        if not trust_remote_code:
            err_msg = (
                "Failed to load the processor. If the processor is "
                "a custom processor not yet available in the HuggingFace "
                "transformers library, consider setting "
                "`trust_remote_code=True` in LLM or using the "
                "`--trust-remote-code` flag in the CLI.")
            raise RuntimeError(err_msg) from e
        else:
            raise e

    if not isinstance(processor, processor_cls):
        raise TypeError("Invalid type of HuggingFace processor. "
                        f"Expected type: {processor_cls}, but "
                        f"found type: {type(processor)}")

    return processor


def cached_processor_from_config_with_revision(
    model_config: "ModelConfig",
    processor_cls: Union[type[_P], tuple[type[_P], ...]] = ProcessorMixin,
    **kwargs: Any,
) -> _P:
    return cached_get_processor(
        model_config.model,
        revision=model_config.revision,
        trust_remote_code=model_config.trust_remote_code,
        processor_cls=processor_cls,  # type: ignore[arg-type]
        **_merge_mm_kwargs(model_config, **kwargs),
    )

get_processor = get_processor_with_revision
cached_processor_from_config = cached_processor_from_config_with_revision
```

# Qwen2-VL 精度问题

```bash
------------------------------ Captured log call -------------------------------
WARNING  transformers.models.auto.image_processing_auto:logging.py:328 The image processor of type `Qwen2VLImageProcessor` is now loaded as a fast processor by default, even if the model checkpoint was saved with a slow processor. This is a breaking change and may produce slightly different outputs. To continue using the slow processor, instantiate this class with `use_fast=False`. Note that this behavior will be extended to all models in a future release.

=========================== short test summary info ============================
FAILED tests/e2e/singlecard/multi_modal/test_vl_dense_model.py::test_models_with_aclgraph[32-Qwen/Qwen2-VL-2B-Instruct] - AssertionError: Test0:
vllm_eager_outputs:	'The image features a logo with two main components: the company name and a stylized graphic element. The company name is "TONGYI Qwen,"'
vllm_aclgraph_outputs:	'The image features a logo with two main components: the word "TONGYI" and the word "Qwen." The word "TONGYI'
============= 1 failed, 2 passed, 8 warnings in 629.00s (0:10:29) ==============
```

v0.11.0

```bash
=========================== short test summary info ============================
FAILED tests/e2e/singlecard/test_vlm.py::test_multimodal_vl[qwen2.5vl] - requests.exceptions.HTTPError: 400 Client Error: Bad Request, Request id: 950239c536184ec7a4edaa1b050a23ad for url: https://www.modelscope.cn/api/v1/models/Qwen/Qwen2.5-VL-3B-Instruct/repo/files?Revision=master&Recursive=True
======== 1 failed, 1 passed, 1 skipped, 7 warnings in 239.58s (0:03:59) ========
```

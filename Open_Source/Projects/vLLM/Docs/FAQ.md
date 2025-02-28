# FAQ

**3. How to do multi node deployment?**

You can launch multi-node service with Ray, find more details at our tutorials: [<u>Online Serving on Multi Machine</u>](https://vllm-ascend.readthedocs.io/en/latest/tutorials.html#online-serving-on-multi-machine).

**4. "RuntimeError: Failed to infer device type" or "ImportError: `libatb.so`: cannot open shared object file: No such file or directory".**

This is usually because of the wrong `torch_npu` version or lack of Ascend CANN NNAL Package.

Make sure you install the correct version of `torch_npu`.

Install with the specific CANN and NNAL.

The details of `torch_npu` and CANN NNAL could be found at our [<u>docs</u>](https://vllm-ascend.readthedocs.io/en/latest/installation.html#setup-vllm-and-vllm-ascend).

**5. Is Atlas 300 currently supported?**

Not supported yet, currently only Atlas A2 series devices supported as shown [<u>here</u>](https://github.com/vllm-project/vllm-ascend?tab=readme-ov-file#prerequisites).

From a technical view, vllm-ascend support would be possible if the torch-npu is supported. Otherwise, we have to implement it by using custom ops. We are also welcome to join us to improve together.

**6. Are Quantization algorithms currently supported?**

Not support now, but we will support **W8A8** and **FA3** quantization algorithms in the future.

**7. Inference speed is slow.**

Currently, the performance of vLLM on Ascend still need to be improved. We are also working together with the Ascend team to improve it. The first release will be `v0.7.3` in 2025 Q1. Therefore, welcome everyone join us to improve it.

**8. DeepSeek V3 / R1 related errors.**

Known issue will be fixed in vllm-ascend `v0.7.3rc1` (March. 2025) with CANN `8.1.RC1.alpha001` (Feb. 2025):

- `AssertionError: Torch not compiled with CUDA enabled.`
- `RuntimeError: GroupTopkOperation CreateOperation failed.`
- `ValueError: Unknown quantization method: ascend.`
- ...

Find more details in #72, which tracks initial support for the Deepseek V3 model with vllm-ascend.

**9. Qwen2-VL / Qwen2.5-VL related errors.**

**Q1:** `Qwen2-VL-72B-Instruct` inference failure: `RuntimeError: call aclnnFlashAttentionScore failed`. (#115)

This is caused by the inner error of CANN ops, which will be fixed in the next CANN version.

BTW, qwen2 in vllm only works with torch SDPA on non-GPU platform. We'll improve it in vLLM to make it support more backend in the next release. Find more details [<u>here</u>](https://github.com/vllm-project/vllm/blob/main/vllm/attention/layer.py#L254-L257).

**Q2:** `Qwen2.5-VL-7B` always answer `!`, whereas that of `Qwen2.5-VL-3B` is correct. (#131)

_TODO_

**7. Accuracy related errors.**

**Q1:** Inference result on Atlas 800I A2 occured repeat. (#31)

_TODO_

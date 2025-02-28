# Add support for Guided Decoding

## Motivation

In our roadmap, we plan to support **guided decoding** in 2025 Q1 as shown here (#71).

**Currently:**

I have tested `vllm/examples/offline_inference/structured_outputs.py` directly on NPU device and the experiment results showed that guided decoding is natively supported on NPU with `outlines` backend.

The test logs are shown below:

```bash
Processed prompts: 100%|█████| 1/1 [00:00<00:00,  6.62it/s, est. speed input: 73.09 toks/s, output: 13.29 toks/s]
Positive
Processed prompts: 100%|█████| 1/1 [00:04<00:00,  4.24s/it, est. speed input: 7.54 toks/s, output: 1.89 toks/s]
alan_turing@enigma.com
Processed prompts: 100%|█████| 1/1 [00:13<00:00, 13.08s/it, est. speed input: 1.68 toks/s, output: 1.22 toks/s]
{"brand": "Ford", "model": "Mustang", "car_type
Processed prompts: 100%|█████| 1/1 [08:01<00:00, 481.33s/it, est. speed input: 0.04 toks/s, output: 0.03 toks/s]
SELECT username,email FROM usersonentokddt839npt0
```

Plus, I have analysed the code in vLLM and have found that the tensors related to guide logits computation are all on `npu` device, which have also  demonstrated that guided decoding is natively supported on NPU.

## Optimization

However, there are still some problems need to be fixed, such as [<u>incomplete json output</u>](https://github.com/vllm-project/vllm/issues/13683) and [<u>inference speed is too slow</u>](https://github.com/vllm-project/vllm/issues/13821).

Feel free to feedback your issues when using guided decoding with vllm-ascend, and we will try to fix them if we can.

## CC List

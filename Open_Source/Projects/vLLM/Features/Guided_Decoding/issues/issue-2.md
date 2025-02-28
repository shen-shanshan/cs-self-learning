# Add Jump-Forward support for Guided Decoding

## Motivation

Currently, vLLM use FSM to implement guided decoding with backends such as `outlines`. However, the FSM is constructed at the token level, it can transition the state by only one token at each step. Consequently, it can decode only one token at a time, which results in slow decoding.

In my experiment, the inference speed is too slow (`0.89 toks/s`) with `Qwen2.5-7B-Instruct` on my npu device.

Would you mind community contributors to introduce **Jump-Forward Decoding** proposed by SGLang?

Find more details [<u>here</u>](https://lmsys.org/blog/2024-02-05-compressed-fsm/).

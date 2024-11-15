# TODO List

## vLLM

实现 rope 算子：调 torch_npu 接口，重写 forward_npu() 方法，可以参考华为云的实现。
torch_npu 接口：npu_apply_rotary_pos_emb
代码位置：vllm/model_executor/layers/rotary_embedding.py

## llama.cpp
新建 PR，合入 cann 的 patch（git apply cann.path）。
代码：[cann.patch](https://github.com/gpustack/llama-box/blob/main/llama-box/patches/llama.cpp/cann.patch)（看懂）
发 issue 和邮件，联系 Frank Mai（抄送：huafengchun@gmail.com），添加 coauthor（示例：Co-authored-by: wangshuai09 <391746016@qq.com>）

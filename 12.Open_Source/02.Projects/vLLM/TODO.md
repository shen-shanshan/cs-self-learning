# TODO

## 总览

vllm 这边除了 vllm-npu 的开发，就剩下公共特性了，一方面可以在每天刷新的 issue、pr 里面去找可以修的 bug 或者可以重构的代码，另一方面就是源神这个 rfc：https://github.com/vllm-project/vllm/issues/11162。

另一方面就是源神这个 rfc 提到的待办项，都可以搞：https://github.com/vllm-project/vllm/issues/11162。

还有就是对关键的一些 pr 可以积极参与 review 啥的。

## RoPE 算子适配

实现 rope 算子：调 torch_npu 接口，重写 `forward_npu()` 方法，可以参考华为云的实现。

torch_npu 接口：`npu_apply_rotary_pos_emb()`。

代码位置：`vllm/model_executor/layers/rotary_embedding.py`。

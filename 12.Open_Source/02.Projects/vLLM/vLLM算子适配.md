# vLLM 算子适配

## TODO

实现 rope 算子：调 `torch_npu` 接口，重写 `forward_npu()` 方法，可以参考华为云的实现。
torch_npu 接口：`npu_apply_rotary_pos_emb`。
代码位置：`vllm/model_executor/layers/rotary_embedding.py`。
可以参考华为云内源 vLLM RoPE 算子的实现。

## 源码阅读

`vllm/model_executor/custom_op.py`:

**CustomOp**:

Base class for custom ops. Dispatches the forward method to the appropriate backend.

`def dispatch_forward(self)`：根据 `Platform` 选择对应后端的 `forward()` 方法。

`def enabled(cls) -> bool`：判断自定义算子类是否已注册。

`op_registry: Dict[str, Type['CustomOp']] = {}`：Dictionary of all custom ops (classes, indexed by registered name). To check if an op with a name is enabled, call `.enabled()` on the class.

`def register(cls, name: str)`：用于自定义算子类实现注册的装饰器。

注册方式：

```python
@CustomOp.register("custom_op_name")
class CustomOpName(CustomOp):
    # ...
```

通过在自定义算子类中重写 `xxx_forward(self, *args, **kwargs)` 方法，可以调用指定后端提供的算子接口（对于 NPU，是调用 `torch_npu` 的接口）。

## 基础知识

### Python 基本语法

- `@classmethod` 和 `@staticmethod`
- 自定义装饰器（`@`）
- `*args` 和 `**kwargs`
- `typing` 模块：提供强类型校验

### Rotary Positional Embeddings

RoPE（Rotary Positional Embeddings，旋转位置编码）：

……

**参考资料：**

- [<u>十分钟读懂旋转编码（RoPE）</u>](https://zhuanlan.zhihu.com/p/647109286)
- [<u>一文看懂 LLaMA 中的旋转式位置编码（Rotary Position Embedding）</u>](https://zhuanlan.zhihu.com/p/642884818)

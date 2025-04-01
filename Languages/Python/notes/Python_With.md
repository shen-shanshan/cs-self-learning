# Python With 原理与用法

## 基本原理

`with` 语句实现原理建立在上下文管理器之上。上下文管理器是一个实现 `__enter__` 和 `__exit__` 方法的类，使用 `with` 语句确保在嵌套块的末尾调用 `__exit__` 方法。

通过 `contextlib` 模块下提供的 `@contextmanager` 装饰器，我们能够更方便的将一个普通类变成上下文管理器。`@contextmanager` 通过将一个函数变成生成器的方式来为普通类添加进入和退出时的处理代码，从而实现了将普通类变成一个上下文管理器。

## 代码示例

```python
from contextlib import contextmanager


class File():
    def query(self):
        print('查询文件')


@contextmanager
def open():
    print('打开文件')
    yield File()
    print('关闭文件')


with open() as f:
    f.query()
```

**执行流程：**

1. `with` 语句调用 `open()` 函数；
2. 执行 `open()` 中 `yield` 之前的代码（打开文件）；
3. 执行 `yield` 语句（`yield File()` -> `as f`）
4. 执行 `with` 语句中的代码（`f.query()`，查询文件）；
5. 执行 `yiled` 语句后的代码（关闭文件）。

> 注意：`yield` 前的代码就相当于 `__enter__`；`yield` 后的代码就相当于 `__exit__`；`yield` 前、中、后的代码都可以省略不写。

## 代码示例（vllm）

```python
from contextlib import contextmanager


@contextmanager
def set_forward_context(attn_metadata: Any,
                        vllm_config: VllmConfig,
                        virtual_engine: int = 0,
                        num_tokens: int = 0):
    """A context manager that stores the current forward context,
    can be attention metadata, etc.
    Here we can inject common logic for every model forward pass.
    """
    # 执行 __enter__ 逻辑：
    # ...
    global _forward_context
    prev_context = _forward_context  # 记录当前上下文
    _forward_context = ForwardContext(
        no_compile_layers=vllm_config.compilation_config.
        static_forward_context,
        virtual_engine=virtual_engine,
        attn_metadata=attn_metadata,
        dp_metadata=dp_metadata)
    try:
        yield  # 执行被 with 包裹的代码
    finally:
        # 执行 __exit__ 逻辑：
        # ...
        _forward_context = prev_context  # 恢复之前的上下文？
```

```python
# Run forward pass
with set_forward_context(attn_metadata, self.vllm_config):
    assert self.model is not None
    hidden_states = self.model(
        input_ids=input_ids,
        positions=positions,
        intermediate_tensors=intermediate_tensors,
        inputs_embeds=None,
    )
```

## 参考资料

- [Python with 关键字](https://www.runoob.com/python3/python-with.html)
- [Python 中 @contextmanager 的用法](https://www.cnblogs.com/yeer-xuan/p/13493902.html)

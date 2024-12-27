# Python Entry Points

## 基本原理

`setup.py`：

entry_points={key: value}

- key -> **Plugin group**
- value -> ["**Plugin name** = **Plugin value**", ...]

Plugin value -> `python_module:function_name`

## 示例（vLLM）

```python
# inside `setup.py` file
from setuptools import setup

setup(name='vllm_add_dummy_model',
      version='0.1',
      packages=['vllm_add_dummy_model'],
      entry_points={
          'vllm.general_plugins':
          ["register_dummy_model = vllm_add_dummy_model:register"]
      })

# inside `vllm_add_dummy_model.py` file
def register():
    from vllm import ModelRegistry

    if "MyLlava" not in ModelRegistry.get_supported_archs():
        ModelRegistry.register_model("MyLlava",
                                        "vllm_add_dummy_model.my_llava:MyLlava")
```

## 参考资料

- [<u>Entry Points Docs</u>](https://setuptools.pypa.io/en/stable/userguide/entry_point.html)
- [<u>Entry points specification</u>](https://packaging.python.org/en/latest/specifications/entry-points/)
- [<u>vLLM's Plugin System</u>](https://docs.vllm.ai/en/stable/design/plugin_system.html#how-vllm-discovers-plugins)

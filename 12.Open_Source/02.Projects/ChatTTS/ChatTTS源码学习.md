# ChatTTS 源码学习

## Python 基础

### Python 关键字

- `async`：定义异步函数；
- `global`：定义全局变量。

### `__init__.py`

- 将目录标记为 Python 包；
- 包级别的初始化代码（包被导入时执行）；
- 定制对外的接口，间接导入函数、类或其它模块。

> 参考资料：[<u>python init 文件原理解析</u>](https://blog.csdn.net/Bill_seven/article/details/104391208)。

### 字符操作

- `str.maketrans()`：构建翻译表的映射关系；
- `str.translate()`：执行翻译，替换为映射到的词；
- `ord()`：Python 的内置函数，返回对应的 ASCII 数值，或者 Unicode 数值。

### 正则表达式

> 参考资料：[<u>正则表达式-菜鸟教程</u>](https://www.runoob.com/python/python-reg-expressions.html)。

### dataclass

`dataclass`：用于配置参数（超参数？）。

```python
from dataclasses import dataclass

@dataclass(repr=False, eq=False)
class Config:
    # ...
```

## web 框架

### FastAPI

常用依赖：

- `from fastapi import FastAPI`：提供服务端接口；
- `from pydantic import BaseModel`：用于服务端接口的参数校验；
- `import requests`：用于客户端向服务端发送请求。

常用注解：

- `on_event(“startup”)`：当后端服务启动时执行该函数，用于服务端的初始化；
- `post(“/…”)`：收到对应 post 请求时执行该函数，相当 Java 中的 `@Controller`。

### gradio

gradio 用于为 python 应用提供 web UI。

> 官方网站：[<u>gradio</u>](https://www.gradio.app/)。

## 模型导出、部署

### onnx

onnx：一种表示模型的格式，便于在不同框架、平台上迁移模型。

## 性能优化

### numba

`numba`：python 的 JIT（即时编译）库。

> 官方说明：Numba generates optimized machine code from pure Python code using the LLVM compiler infrastructure. With a few simple annotations, array-oriented and math-heavy Python code can be just-in-time optimized to performance similar as C, C++ and Fortran, without having to switch languages or Python interpreters.

JIT（即时编译）：当某段代码即将第一次被执行时进行编译。动态地将高级语言编写的代码转换为机器码，可以直接由计算机的处理器执行，这是在运行时完成的，也就是代码执行之前，因此称为“即时”。

两种模式：

- `nopython`：The behaviour of the nopython compilation mode is to essentially compile the decorated function so that it will run entirely without the involvement of the Python interpreter. This is the recommended and best-practice way to use the Numba jit decorator as it leads to the best performance；
- `object`：This achieved through using the `forceobj=True` key word argument to the `@jit` decorator. In this mode Numba will compile the function with the assumption that everything is a Python object and essentially run the code in the interpreter. Specifying `looplift=True` might gain some performance over pure object mode as Numba will try and compile loops into functions that run in machine code, and it will run the rest of the code in the interpreter. For best performance avoid using object mode mode in general.

示例：

```python
from numba import jit

@jit
def function():
    # ...
```

numba 在首次调用函数的时候进行了编译，但当编译发生后，numba 会将该函数的机器码进行缓存，如果再次调用该函数，它会直接从缓存中加载，而不需要再次编译。

> 官方说明：once the compilation has taken place Numba caches the machine code version of your function for the particular types of arguments presented. If it is called again with the same types, it can reuse the cached version instead of having to compile again.

Numba for CUDA GPUs：

Numba 能够支持 CUDA GPU 编程，能够自动地在 host 与 device 之间传递 Numpy 数组。

> 参考资料：
>
> - [<u>Numba GitHub 地址</u>](https://github.com/numba/numba)；
> - [<u>Numba documentation</u>](https://numba.readthedocs.io/en/stable/index.html)；
> - [<u>python 运行加速神器——numba 原理解析</u>](https://blog.csdn.net/weixin_45977690/article/details/133886829)；
> - [<u>JIT 即时编译技术</u>](https://zhuanlan.zhihu.com/p/193035135)。

## TTS 原理

1. 文本预处理；
2. 频谱生成；
3. 时域波形生成。

> 参考资料：[<u>Text-to-Speech with Tacotron2</u>](https://pytorch.org/audio/stable/tutorials/tacotron2_pipeline_tutorial.html)。

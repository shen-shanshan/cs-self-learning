# ChatTTS 源码学习

## 环境搭建

依赖列表：

```
numpy<2.0.0
numba
torch==2.1.0
torchaudio
tqdm
vector_quantize_pytorch
transformers>=4.41.1
vocos
IPython
gradio
pybase16384
pynini==2.1.5; sys_platform == 'linux'
WeTextProcessing==1.0.2; sys_platform == 'linux'
nemo_text_processing==1.0.2; sys_platform == 'linux'
av
pydub
pyyaml
setuptools>=65.5.1
torch-npu==2.1.0.post6
```

安装依赖：

```bash
pip install -r requirements.txt -i https://pypi.tuna.tsinghua.edu.cn/simple
```

### 安装 pynini 时找不到 openfst

安装 openfst：

```bash
cd /usr/local
mkdir openfst
cd openfst
wget http://www.openfst.org/twiki/pub/FST/FstDownload/openfst-1.8.3.tar.gz
tar -zxvf openfst-1.8.3.tar.gz
cd openfst-1.8.3
./configure --prefix=/usr/local/openfst/openfst-1.6.7 <--enable-python> --enable-grm
make -j4
make install
ln -s /usr/local/openfst/openfst-1.6.7 /usr/local/openfst/openfst

export PATH=/home/sss/.local/fst/openfst-1.7.2/src/bin:$PATH
export PATH=/~/bin/miniconda/miniconda3/envs/chattts_2/lib:$PATH
echo $PATH
```

环境配置写入 `.bashrc`：

```bash
# >>> conda initialize >>>
# !! Contents within this block are managed by 'conda init' !!
__conda_setup="$('/home/sss/bin/miniconda/miniconda3/bin/conda' 'shell.bash' 'hook' 2> /dev/null)"
if [ $? -eq 0 ]; then
    eval "$__conda_setup"
else
    if [ -f "/home/sss/bin/miniconda/miniconda3/etc/profile.d/conda.sh" ]; then
        . "/home/sss/bin/miniconda/miniconda3/etc/profile.d/conda.sh"
    else
        export PATH="/home/sss/bin/miniconda/miniconda3/bin:$PATH"
    fi
fi
unset __conda_setup
# <<< conda initialize <<<

export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:~/bin/miniconda/miniconda3/envs/chattts_2/lib/
#export LD_PRELOAD=$LD_PRELOAD:~/bin/miniconda/miniconda3/envs/chattts_2/lib/

export PATH=$PATH:/home/sss/.local/fst/openfst-1.7.2/src/bin:/~/bin/miniconda/miniconda3/envs/chattts_2/lib
```

手动安装 pynini，指定头文件路径：

```bash
cd /home/sss/bin/miniconda/miniconda3/envs/cann/lib/python3.10/site-packages/pynini

git clone https://github.com/kylebgorman/pynini.git
cd pynini
python setup.py build_ext --include-dirs=/home/sss/.local/fst/openfst-1.6.7

python setup.py build_ext --include-dirs="/home/sss/.local/include" --library-dirs="/home/sss/.local/lib" install

python setup.py build_ext --include-dirs=~/bin/miniconda/miniconda3/envs/chattts_2/lib/
```

> 参考资料：
>
> - [<u>openfst 以及其 python 扩展安装</u>](https://blog.csdn.net/qq_33424313/article/details/122293358)；
> - [<u>安装 openfst 依赖</u>](https://blog.csdn.net/weixin_53694631/article/details/128552804)；
> - [<u>openfst Installation issues</u>](https://github.com/kylebgorman/pynini/issues/16)。

## Python 基础

### Python 关键字

`async`：

定义异步函数。

> 参考资料：[python 异步 async/await（进阶详解）_python async-CSDN博客](https://blog.csdn.net/qq_43380180/article/details/111573642)。

`global`：

定义全局变量。

`yield`：

带 `yield` 的函数是一个生成器，而不是一个函数。

生成器有一个是 `next()` 函数，`next()` 就相当于“下一步”生成哪个数，这一次的 `next()` 开始的地方是接着上一次的 `next()` 停止的地方执行的，遇到 `yield` 后，return 出要生成的数，此步就结束。

> 参考资料：[<u>python 中 yield 的用法详解</u>](https://blog.csdn.net/mieleizhi0522/article/details/82142856/)。

`assert`：

用于断言测试。

示例：

```python
assert expression, "错误信息"
```

- `expression` 为 `true`：什么也不发生；
- `expression` 为 `false`：程序报错。

### Python 注解

`@dataclass`：

在定义数据类时，我们通常需要编写一些重复性的代码，如构造函数、属性访问器和字符串表示等。使用 `@dataclass` 可以自动生成这些通用方法，从而简化数据类的定义过程。

具体地，`@dataclass` 可以省略 `__init__()` 和 `__str__()` 方法，适合于属性特别多、专门用于存储数据的类（类似于后端开发中的“实体类”）。

```python
from dataclasses import dataclass, field
from typing import List


@dataclass
class Person1:
    name: str
    age: int
    salary: float
    gender: bool
    hobbies: List[str]


def gen_list():
    return ['guitar', 'badminton', 'programme']


@dataclass(frozen=True)  # 使用 frozen=True 可以将该类的属性设置为只读，外界无法修改
class Person2:
    name: str = 'sss_2'  # 属性可以设置默认值
    age: int = 26
    salary: float = field(default=20000.000, repr=False)  # 使用 repr 参数可以设置隐藏字段（不会被 print() 方法打印）
    gender: bool = True
    # hobbies: List[str] = ['guitar', 'badminton']
    """
    ValueError: mutable default <class 'list'> for field hobbies is not allowed: use default_factory
    注意：对于可变类型（如 list）的属性，需要用工厂方法来产生默认值
    """
    hobbies: List[str] = field(default_factory=gen_list)


if __name__ == '__main__':
    p1 = Person1('sss', 18, 15000.000, True, ['guitar', 'badminton'])
    print(p1)  # Person1(name='sss', age=18, salary=15000.0, gender=True, hobbies=['guitar', 'badminton'])

    p2 = Person2()
    print(p2)  # Person2(name='sss_2', age=26, gender=True, hobbies=['guitar', 'badminton', 'programme'])

    p1.name = 'ma yun'
    print(p1.name)  # ma yun
    
    # p2.name = 'ma hua teng'
    """
    dataclasses.FrozenInstanceError: cannot assign to field 'name'
    Person2 的属性已被冻结，无法修改
    """
```

> 参考资料：[掌握python的dataclass，让你的代码更简洁优雅 - wang_yb - 博客园 (cnblogs.com)](https://www.cnblogs.com/wang_yb/p/18077397)。

`@metaclass`：

> 参考资料：
>
> - [Python进阶——详解元类，metaclass的原理和用法 - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/149126959)；
> - [使用元类 - Python教程 - 廖雪峰的官方网站 (liaoxuefeng.com)](https://liaoxuefeng.com/books/python/oop-adv/meta-class/index.html)。

### Python 方法

`eval()`：

```python
class A(torch.nn.Module):
    # ...

# 初始化模型
a = A().eval()
```

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

## PyTorch 框架

### 常用注解

`@torch.no_grad()`：

推理时，不存储计算图中每个节点的梯度。

`@torch.inference_mode()`：

是一个类似于 `no_grad` 的新上下文管理器，该模式禁用了视图跟踪和版本计数器，所以在此模式下运行代码能够获得更好的性能，速度也会更快。

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

## 模型推理

### 推流流程

1. 加载模型：选择 `device`、初始化模型权重；
2. text -> normalizer（正则化：替换同义词、……）；
3. refine text：自动加入语气词？（可选）；
4. tokenizer -> encode -> embed -> gpt.generate；
5. decode to wavs。

### 多卡推理

> NPU 适配，搜索关键词：`torch.cuda`、`gpu`。

`configs.py`：

**ParallelConfig**:
Configuration for the distributed execution.
    `max_parallel_loading_workers`：max concurrent workers.

`llm_engine.py`：

**LLMEngine**：
This is the main class for the vLLM engine.
    The `LLM` class wraps this class for offline batched inference.
    The `AsyncLLMEngine` class wraps this class for online serving.
`__init__()`：
    Create the parallel GPU workers.
    `_init_workers_ray(placement_group)`：？
        `_run_workers("init_model")`：？
        `_run_workers("load_model", max_concurrent_workers)`：？
    `_init_workers()`：？
        `_run_workers("init_model")`：？
        `_run_workers("load_model")`：？
    `_run_workers(..., method, ...)`：Runs the given method on all workers.

分布式计算框架 Ray：
    driver worker：？
    ray worker：？

`worker.py`：

🌟 需要适配的 API：
    `set_device()` ✅
    `empty_cache()` ✅
    `synchronize()`
    `mem_get_info()` ✅
    `get_device_capability()`

`model_runner.py`：

`load_model()`：调用 `get_model()` 获取 `model_config`。

🌟 需要适配的 API：
    `synchronize()`
    `CUDAGraph()`

`model_loader.py`：

`get_model()`：涉及 `torch.cuda.get_device_capability()`。

🌟 需要适配的 API：
    `get_device_capability()`

[torch_npu API 查询](https://www.hiascend.com/document/detail/zh/Pytorch/60RC2/apiref/apilist/ptaoplist_000655.html)。

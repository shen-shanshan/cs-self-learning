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

`@dataclass`：？。

```python
from dataclasses import dataclass

@dataclass(repr=False, eq=False)
class Config:
    # ...
```

`@metaclass`：？

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

- `@torch.no_grad()`：？
- `@torch.inference_mode()`：？

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

## 模型导出

### onnx

onnx：一种表示模型的格式，便于在不同框架、平台上迁移模型。

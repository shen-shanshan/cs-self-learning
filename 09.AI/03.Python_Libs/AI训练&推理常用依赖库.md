# AI 训练 & 推理常用依赖库

## PyTorch

### torch

……

### torchaudio

`torchaudio` 是 PyTorch 官方用于处理音频数据和进行音频相关深度学习任务的工具包，提供了音频数据的加载和保存、频谱分析、预训练的音频模型（支持音频分类、语音识别等任务）、与 PyTorch 的数据集和数据加载器集成等功能。

`torchaudio.load()` 是 `torchaudio` 库中的一个函数，用于加载音频文件并返回音频数据及其采样率。它可以方便地将音频文件加载到 PyTorch 的张量中，以便进行后续的音频处理和深度学习任务。

具体功能包括：

- 读取音频文件：支持多种音频格式（如 WAV、MP3 等）；
- 返回数据和采样率：返回两个值：音频信号的张量表示（通常是浮点数）和音频的采样率（Hz），方便后续处理和分析。

`torchaudio.functional.resample(y, orig_freq=xxx, new_freq=xxx)` 是用于对音频信号进行重采样的函数。该函数适用于需要调整音频采样率的场景，如匹配不同音频源的采样频率或准备音频数据以供模型训练。

具体功能包括：

- 重采样音频信号：将输入的音频张量 `y` 从原始采样频率 `orig_freq` 转换到新的采样频率 `new_freq`；
- 保持音质：通过适当的算法，尽量减少重采样过程中的失真，确保音质尽可能保持。

> 参考资料：
>
> - [<u>TorchAudio Documentation</u>](https://pytorch.org/audio/stable/index.html)；
> - [<u>TorchAudio 简介</u>](https://datawhalechina.github.io/thorough-pytorch/%E7%AC%AC%E5%85%AB%E7%AB%A0/8.5%20%E9%9F%B3%E9%A2%91%20-%20torchaudio.html#)。

## NLP

### transformers

……

> 参考资料：
>
> - [<u>Transformers GitHub 地址</u>](https://github.com/huggingface/transformers)；
> - [<u>🤗 Transformers</u>](https://huggingface.co/docs/transformers/en/index)。

## Audio

### vocos

`vocos` 主要用于音频处理和合成任务，可以支持声码器（vocoder）模型的训练和推理。

- 声码器支持：实现了多种声码器模型，如 WaveNet 和 WaveRNN，能够将梅尔谱（Mel-spectrogram）转换为音频波形；
- 音频处理：提供了一些音频预处理和后处理的工具，以便于输入和输出音频；
- 模型训练：支持声学模型的训练和评估。

`vocos` 基于 GAN 进行训练，针对频谱系数进行建模，能够根据频域的梅尔频谱，通过傅立叶逆变换快速重建时域音频信号。

> 参考资料：
>
> - [<u>Vocos GitHub 地址</u>](https://github.com/gemelo-ai/vocos)。

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
> - [<u>Numba Documentation</u>](https://numba.readthedocs.io/en/stable/index.html)；
> - [<u>python 运行加速神器——numba 原理解析</u>](https://blog.csdn.net/weixin_45977690/article/details/133886829)；
> - [<u>JIT 即时编译技术</u>](https://zhuanlan.zhihu.com/p/193035135)。

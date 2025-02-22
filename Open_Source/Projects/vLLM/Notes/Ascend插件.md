# Ascend 插件

## 整体方案

- [<u>[RFC]: Hardware pluggable</u>](https://github.com/vllm-project/vllm/issues/11162)
- [<u>[Platform] Add platform pluggable framework</u>](https://github.com/vllm-project/vllm/pull/11222)
- [<u>[platforms] enable platform plugins</u>](https://github.com/vllm-project/vllm/pull/11602)

## Python 插件原理

`setup.py`：

`entry_points={key: value}`

- key -> **Plugin group**；
- value -> ["**Plugin name** = **Plugin value**", ...]；
- Plugin value -> `python_module:function_name`。

## Ascend 插件设置

代码地址：[<u>vllm-ascend-plugin</u>](https://github.com/cosdt/vllm-ascend-plugin)。

```python
# setup.py
from setuptools import setup

setup(name='vllm_ascend_plugin',
      version='0.1',
      packages=['vllm_ascend_plugin'],
      entry_points={
          'vllm.platform_plugins':
          ["ascend_plugin = vllm_ascend_plugin:register"]
      })
```

## Ascend 插件调用

安装插件：

```bash
git clone git@github.com:cosdt/vllm-ascend-plugin.git
cd vllm-ascend-plugin
pip install -e .
```

安装 vLLM CPU 版本：

```bash
git clone git@github.com:cosdt/vllm.git -b apply_plugin
cd vllm

sudo apt-get update -y
sudo apt-get install -y gcc-12 g++-12 libnuma-dev
# sudo apt-get install -y gcc-12
# sudo apt-get install -y g++-12
# sudo apt-get install -y libnuma-dev

sudo update-alternatives --install /usr/bin/gcc gcc /usr/bin/gcc-12 10 --slave /usr/bin/g++ g++ /usr/bin/g++-12
pip install cmake>=3.26 wheel packaging ninja "setuptools-scm>=8" numpy
pip install -r requirements-cpu.txt

VLLM_TARGET_DEVICE=cpu python setup.py install
```

参考资料：[<u>Installation with CPU - Build from source</u>](https://docs.vllm.ai/en/stable/getting_started/cpu-installation.html#build-from-source)

# 深入解析 Python 包调用原理与最佳实践

[toc]

## 一、引言

写下这篇文章的起因，是最近我在参与 vLLM 项目的开发过程中，发现其中使用了一种动态加载对象的方式值得学习，并由此想对 Python 语言加载依赖的方式做一个研究和总结。本文将通过实验的方式，对 Python import 的原理以及不同 import 方式的区别进行介绍，并针对具体开发过程中可能会遇到的一些问题，分享一些最佳实践的解决方案。

## 二、什么是 Python 的包？

**Python 中的模块、包以及库有什么区别？**

一种简单且直观的理解：

- **模块（module）**：任何 `.py` 文件都可以作为一个“模块”（除了 `.py` 文件之外，模块还可以有其它形式）；
- **包（package）**：任何包含了一个 `__init__.py` 文件的文件夹都是一个“包”，一个包里可以包含其它的包和模块；
- **库（library）**：“库”更多地是一种编程上的概念，表示可重复利用的代码。

> 关于这个问题，更深入的分析和讲解可以参考知乎上“[<u>风影忍者</u>][1]”和“[<u>看图学</u>][2]”的这两个回答，这里不再深入进行介绍。

## 三、深入解析 Python import 原理

下面我将通过一个个具体的实验，来对 Python import 的原理进行深入的研究和探索。

### 3.1 实验一

首先，我们设置代码的目录结构如下：

```bash
.
|-- main.py
`-- package_a
    |-- __init__.py
    |-- package_b
    |   `-- test_b.py
    `-- package_c
        `-- test_c.py
```

`main.py`：

```python
import package_a


def print_dir(dirs, name):
    '''
    dir() 返回一个字典，包含传入对象的所有属性和方法
    '''
    print("-----------------------------")
    print("dir(" + name + "):")
    for dir in dirs:
        print(dir)


print_dir(dir(), "main")
```

`package_a/__init__.py`：

```python
print("package_a has been imported.")
```

运行 `main.py`，输出结果如下：

```bash
package_a has been imported.
-----------------------------
dir(main):
__annotations__
__builtins__
__cached__
__doc__
__file__
__loader__
__name__
__package__
__spec__
package_a
print_dir
```

**实验结论：**

- 当我们 `import package_a` 时，`package_a/__init__.py` 文件中的内容会被执行；
- import 后的内容（`package_a`）会被添加到当前文件的属性中，我们可以在 `main.py` 中直接调用 `package_a`；
- 只有 `package_a` 被 import 了，但其中的 `package_b` 和 `package_c` 没有被 import 到。

**补充说明：**

`__init__.py` 文件存在于一个需要作为 Python 包被调用的文件夹下，该文件夹可以被 import 到任何 Python 工程中。当我们执行 `import package` 时，Python 程序将运行 `__init__.py` 中所有的命令，并将所有与 package 模块相关的对象记录。如果 `__init__.py` 为空，则生成一个空的 package 对象，它是无法自动处理文件夹下的其他文件的。

**那么我们要怎么才能将 `package_b` 和 `package_c` 也 import 到 `main.py` 中呢？**

### 3.2 实验二

我们修改 `main.py` 文件如下：

```python
import sys

import package_a.package_b
import package_a.package_c


def print_dir(dirs, name):
    print("-----------------------------")
    print("dir(" + name + "):")
    for dir in dirs:
        print(dir)


print_dir(dir(), "main")

'''
sys.modules 是一个全局字典，该字典从 python 启动后就加载在内存中。
每当我们导入一个新的模块，sys.modules 就会记录这些模块。
当某个模块第一次被导入时，sys.modules 将自动记录该模块；当第二次再导入该模块时，python 会直接到字典中查找，从而加快程序运行的速度。
'''
print("-----------------------------")
print("sys.modules:")
for k, v in sys.modules.items():
    print(k, ":", v)

'''
globals() 会以字典类型返回当前位置的全部全局变量
'''
print("-----------------------------")
print("globals():")
for k, v in globals().items():
    print(k, ":", v)

'''
locals() 会以字典类型返回当前位置的全部局部变量
'''
print("-----------------------------")
print("locals():")
for k, v in locals().items():
    print(k, ":", v)
```

增加 `package_b/__init__.py` 文件：

```python
print("package_b has been imported.")
```

增加 `package_c/__init__.py` 文件：

```python
print("package_c has been imported.")
```

运行 `main.py`，输出结果如下：

```bash
package_a has been imported.
package_b has been imported.
package_c has been imported.
-----------------------------
dir(main):
...
package_a
-----------------------------
sys.modules:
...
package_a : <module 'package_a' from '/.../package_a/__init__.py'>
package_a.package_b : <module 'package_a.package_b' from '/.../package_a/package_b/__init__.py'>
package_a.package_c : <module 'package_a.package_c' from '/.../package_a/package_c/__init__.py'>
-----------------------------
globals():
__name__ : __main__
__package__ : None
...
package_a : <module 'package_a' from '/.../package_a/__init__.py'>
-----------------------------
locals():
__name__ : __main__
__package__ : None
...
package_a : <module 'package_a' from '/.../package_a/__init__.py'>
```

可以看到，`package_b` 和 `package_c` 被成功 import 了，但没有被添加到 `main.py` 的属性中，不能直接被访问。

我们再修改 `main.py` 文件如下：

```python
import sys

from package_a import package_b
from package_a import package_c

# 其余内容保持不变……
```

运行 `main.py`，输出结果如下：

```bash
package_a has been imported.
package_b has been imported.
package_c has been imported.
-----------------------------
dir(main):
...
package_b
package_c
-----------------------------
sys.modules:
...
package_a : <module 'package_a' from '/.../package_a/__init__.py'>
package_a.package_b : <module 'package_a.package_b' from '/.../package_a/package_b/__init__.py'>
package_a.package_c : <module 'package_a.package_c' from '/.../package_a/package_c/__init__.py'>
-----------------------------
globals():
...
package_b : <module 'package_a.package_b' from '/.../package_a/package_b/__init__.py'>
package_c : <module 'package_a.package_c' from '/.../package_a/package_c/__init__.py'>
-----------------------------
locals():
...
package_b : <module 'package_a.package_b' from '/.../package_a/package_b/__init__.py'>
package_c : <module 'package_a.package_c' from '/.../package_a/package_c/__init__.py'>
```

可以看到，`package_b` 和 `package_c` 同样被成功 import 了，并且还被添加到了 `main.py` 的属性中，但 `package_a` 属性没了。

**实验结论：**

使用 `import package_a.package_b` 或 `from package_a import package_b` 都可以将 `package_b` 给 import 进来，但两种方式的区别如下：

- 第一种方式是将 `package_a` 给添加到当前文件的属性中，我们可以直接调用的是 `package_a`。若想进一步访问其中的内容，我们可以使用 `package_a.xxx` 的方式；
- 第二种方式是将 import 后的内容（`package_b` 和 `package_c`）给添加到当前文件的属性中，而不添加 from 后的内容（`package_a`），我们可以直接访问的是 `package_b`。若想进一步访问 `package_b` 中的内容，我们可以使用 `package_b.xxx` 的方式，而不需要使用 `package_a.package_b.xxx`（因为此时我们访问不到 `package_a` 变量）。

**补充说明：**

`import ...` vs `from ... import ...`：

- `import ...` 是间接调用，当我们要使用 package 内的方法或对象（`X`）时，需要使用 `package.X` 的方式来访问；
- `from ... import X` 是直接调用，我们可以直接使用 `X` 来调用 `X` 方法或对象。

这里再补充一段 stack overflow 上的解释：

> `import X`: Imports the module X, and creates a reference to that module in the current namespace. Then you need to define completed module path to access a particular attribute or method from inside the module (e.g.: X.name or X.attribute).
>
> `from X import *`: Imports the module X, and creates references to all public objects defined by that module in the current namespace (that is, everything that doesn’t have a name starting with _) or whatever name you mentioned. In other words, after you've run this statement, you can simply use a plain (unqualified) name to refer to things defined in module X. But X itself is not defined, so X.name doesn't work. And if name was already defined, it is replaced by the new version. And if name in X is changed to point to some other object, your module won’t notice.
>
> `from X import a as b`: You can directly call `b()` rather than `a()`.

**补充实验：**

我们修改 `main.py` 文件如下：

```python
import sys

import package_a
from package_a.package_b.test_b import test


def test():
    print("call test() in main.")


package_a.package_b.test_b.test()
test()
```

`package_b/test_b.py`：

```python
def test():
    print("call test() in package_b.")
```

运行 `main.py`，输出结果如下：

```bash
call test() in package_b.
call test() in main.
```

可以看到，当我们使用 `from ... import ...` 的方式将 `test` import 到 `main.py` 文件中时，该函数会被我们在当前文件中定义的同名函数给覆盖（就近原则）。

### 3.3 实验三

我们修改 `main.py` 文件如下：

```python
import sys

import package_a
from package_a import *


def print_dir(dirs, name):
    print("-----------------------------")
    print("dir(" + name + "):")
    for dir in dirs:
        print(dir)


print_dir(dir(), "main")
print_dir(dir(package_a), "package_a")

print("-----------------------------")
print("sys.modules:")
for k, v in sys.modules.items():
    print(k, ":", v)

print("-----------------------------")
print("globals():")
for k, v in globals().items():
    print(k, ":", v)

print("-----------------------------")
print("locals():")
for k, v in locals().items():
    print(k, ":", v)
```

修改 `package_a/__init__.py` 文件如下：

```python
print("package_a has been imported.")

__all__ = ['package_b']
```

运行 `main.py`，输出结果如下：

```bash
package_a has been imported.
package_b has been imported.
-----------------------------
dir(main.py):
...
package_a
package_b
-----------------------------
dir(package_a):
...
package_b
-----------------------------
sys.modules:
...
package_a : <module 'package_a' from '/.../package_a/__init__.py'>
package_a.package_b : <module 'package_a.package_b' from '/.../package_a/package_b/__init__.py'>
-----------------------------
globals():
...
package_a : <module 'package_a' from '/.../package_a/__init__.py'>
package_b : <module 'package_a.package_b' from '/.../package_a/package_b/__init__.py'>
-----------------------------
locals():
...
package_a : <module 'package_a' from '/.../package_a/__init__.py'>
package_b : <module 'package_a.package_b' from '/.../package_a/package_b/__init__.py'>
```

**实验结论：**

- `from package_a import *` 语句会将 `package_a/__init__.py` 中 `__all__` 变量里的包（`package_b`）给 import 到当前文件中，而 `package_c` 由于没有被添加到 `__all__` 里面，因此不会被 import；
- `package_b` 同时还会被添加到 `package_a` 的属性中。

**补充说明：**

`__init__.py` 文件中的 `__all__` 变量关联了一个模块列表，当执行 `from ... import *` 时，就会导入该列表中的所有模块，并且该操作还会继续查找 `package_b` 中的 `__init__.py` 并执行。

### 3.4 实验四

修改 `package_a/__init__.py` 文件如下：

```python
from package_b import test_b


print("package_a has been imported.")

__all__ = ['package_b']
```

运行 `main.py`，输出结果如下：

```bash
package_a has been imported.
...
ModuleNotFoundError: No module named 'package_b'
```

可以看到，此时 Python 程序显示找不打 `package_b`，import 失败，为什么呢？我们继续实验看看结果。

修改 `package_a/__init__.py` 文件如下：

```python
from package_a.package_b import test_b


print("package_a has been imported.")

__all__ = ['package_b']
```

运行 `main.py`，输出结果如下：

```bash
package_a has been imported.
package_b has been imported.
-----------------------------
dir(main.py):
...
package_a
package_b
-----------------------------
dir(package_a):
...
package_b
test_b
-----------------------------
sys.modules:
...
package_a.package_b : <module 'package_a.package_b' from '/.../package_a/package_b/__init__.py'>
package_a.package_b.test_b : <module 'package_a.package_b.test_b' from '/.../package_a/package_b/test_b.py'>
package_a : <module 'package_a' from '/.../package_a/__init__.py'>
-----------------------------
globals():
...
package_b : <module 'package_a.package_b' from '/.../package_a/package_b/__init__.py'>
-----------------------------
locals():
...
package_b : <module 'package_a.package_b' from '/.../package_a/package_b/__init__.py'>
```

可以看到，此时 `test_b` 终于被成功 import 到了 `package_a` 中。

**实验结论：**

当我们在 `main.py` 中执行 import 时，当前目录是不会变的，所以当我们需要在 `package_a` 中 import `package_b` 时，必须使用完整的包名（`package_a.package_b`）。

## 四、循环依赖问题

### 4.1 实验五

首先，我们设置代码的目录结构如下：

```bash
.
|-- main.py
|-- package_a
|   |-- __init__.py
|   `-- func_a.py
`-- package_b
    |-- __init__.py
    `-- func_b.py
```

`main.py`：

```python
import package_a
```

`package_a/__init__.py`：

```python
from package_a import func_a
```

`package_a/func_a.py`：

```python
from package_b.func_b import function_b


def function_a():
    print("call function_a().")
    function_b()
```

`package_b/__init__.py`：

```python
from package_b import func_b
```

`package_b/func_b.py`：

```python
from package_a.func_a import function_a


def function_b():
    function_a()
    print("call function_b().")
```

运行 `main.py`，输出结果如下：

```bash
...
ImportError: cannot import name 'function_a' from partially initialized module 'package_a.func_a' (most likely due to a circular import)
```

可以看到，此时 Python 程序 import 报错，这是因为我们在 `package_a/func_a.py` 中引入了 `package_b` 的同时，又在 `package_b/func_b.py` 中引入了 `package_a`，从而导致了循环依赖问题。

**实验结论：**

当我们在两个不同的 package 中互相 import 对方时，就会导致循环依赖问题。

### 4.2 实验六

我们修改 `main.py` 文件如下：

```python
import package_a
import package_b


package_a.func_a.get_class()
package_b.func_b.get_class()
```

修改 `package_a/func_a.py` 文件如下：

```python
from package_b.func_b import B


class A:
    def __init__(self):
        pass


def get_class() -> B:
    print("get class B from package_b.")
```

修改 `package_b/func_b.py` 文件如下：

```python
from package_a.func_a import A


class B:
    def __init__(self):
        pass


def get_class() -> A:
    print("get class A from package_a.")
```

运行 `main.py`，输出结果如下：

```bash
...
ImportError: cannot import name 'function_a' from partially initialized module 'package_a.func_a' (most likely due to a circular import)
```

可以看到，这里同样是一个循环依赖的问题，那么我们应该如何解决这类问题呢？

这里我们可以使用 `typing` 库中的 `TYPE_CHECKING` 来解决循环依赖的问题。

修改 `package_a/func_a.py` 文件如下：

```python
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from package_b.func_b import B


class A:
    def __init__(self):
        pass


def get_class() -> B:
    print("get class B from package_b.")
```

修改 `package_b/func_b.py` 为：

```python
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from package_a.func_a import A


class B:
    def __init__(self):
        pass


def get_class() -> A:
    print("get class A from package_a.")
```

运行 `main.py`，输出结果如下：

```bash
...
def get_class() -> B:
NameError: name 'B' is not defined
```

这里报错的原因是 `get_class() ->` 后的 `A` 和 `B` 没有使用引号包裹。

修改 `package_a/func_a.py` 文件如下：

```python
# 其余内容保持不变……

def get_class() -> "B":
    print("get class B from package_b.")
```

修改 `package_b/func_b.py` 文件如下：

```python
# 其余内容保持不变……

def get_class() -> "A":
    print("get class A from package_a.")
```

运行 `main.py`，输出结果如下：

```bash
get class B from package_b.
get class A from package_a.
```

可以看到，`get_class()` 函数调用成功，不存在循环依赖问题。

**实验结论：**

当两个 Python 文件互相导入引用时，如果不使用 `if typing.TYPE_CHECKING:` 包裹就直接导入，就会因为循环导入而产生错误。

使用 `TYPE_CHECKING` 导入的任何对象，只能作为注解使用，不可以真的去使用这些对象，因为这些对象只有在编辑器检查的阶段才会被导入，并且在使用这些类型作为解注时，必须使用引号包裹。否则在真正的代码业务执行时，就会抛出 `NameError: xxx is not defined` 错误。

**补充说明：**

`TYPE_CHECKING` 是一个会被第三方静态类型检查器假定为 `True` 的特殊常量，而在运行时则会被假定为 `False`，也就是它下面的 `import` 是不执行的，但它可以为第三方静态类型检查器提供所需要检查的类型。关于 `TYPE_CHECKING` 的更多细节可以自行查阅了解。

## 五、动态加载对象

最后，介绍一下我在 vLLM 项目中看到的一种动态加载对象的方式。

`vllm/utils.py/resolve_obj_by_qualname()` 函数定义如下：

```python
def resolve_obj_by_qualname(qualname: str) -> Any:
    """
    Resolve an object by its fully qualified name.
    """
    module_name, obj_name = qualname.rsplit(".", 1)
    module = importlib.import_module(module_name)
    return getattr(module, obj_name)
```

该函数会接收一个类的全路径名（比如：`vllm.worker.multi_step_worker.MultiStepWorker`），然后将该字符串拆分为两部分，分别表示该类所在的 package（`vllm.worker.multi_step_worker`）以及该类（`MultiStepWorker`）。最后，该函数会加载对应的 package 并返回对应的类。

根据情况返回不同的 `Worker`：

```python
@classmethod
def check_and_update_config(cls, vllm_config: VllmConfig) -> None:
    parallel_config = vllm_config.parallel_config
    scheduler_config = vllm_config.scheduler_config

    if parallel_config.worker_cls == "auto":
        if scheduler_config.is_multi_step:
            if envs.VLLM_USE_V1:
                raise NotImplementedError
            else:
                parallel_config.worker_cls = "vllm.worker.multi_step_worker.MultiStepWorker"
        elif vllm_config.speculative_config:
            if envs.VLLM_USE_V1:
                raise NotImplementedError
            else:
                parallel_config.worker_cls = "vllm.spec_decode.spec_decode_worker.create_spec_worker"
                parallel_config.sd_worker_cls = "vllm.worker.worker.Worker"
        else:
            if envs.VLLM_USE_V1:
                parallel_config.worker_cls = "vllm.v1.worker.gpu_worker.Worker"
            else:
                parallel_config.worker_cls = "vllm.worker.worker.Worker"
```

根据情况动态加载不同的 `Worker` 对象：

```python
from vllm.utils import (resolve_obj_by_qualname, ...)

def init_worker(self, *args, **kwargs):
    # ...
    worker_class = resolve_obj_by_qualname(self.vllm_config.parallel_config.worker_cls)
    self.worker = worker_class(*args, **kwargs)
    assert self.worker is not None
```

使用这种方式，我们不需要在 `check_and_update_config()`  函数所在的文件中将所有的 `Worker` 类都 import 进来，而只需要以纯字符串的形式返回对应的类，避免了可能存在的循环依赖问题，从而极大地提升了依赖管理的灵活性。

## 六、总结

- 将 `__init__.py` 文件放到一个文件夹中，使其可以作为一个 python package 被 import。当该 package 被 import 时，`__init__.py` 文件中的内容将会被执行；
- `import X` 与 `from X import xxx` 的区别：第一种对应的调用方式为 `X.xxx()`；第二种对应的调用方式为 `xxx()`；
- `__init__.py` 文件中的 `__all__` 变量关联了一个模块列表，当执行 `from ... import *` 时，就会导入该列表中的所有模块；
- 当我们执行 import 时，当前目录是不会变的，因此需要指定完整的包名；
- 当我们在两个不同的 package 中互相 import 对方时，就会导致循环依赖问题；
- 使用 `TYPE_CHECKING` 导入的任何对象，只能作为注解使用，不可以真的去使用这些对象，因为这些对象只有在编辑器检查的阶段才会被导入，并且在使用这些类型作为解注时，必须使用引号包裹；
- 可以使用 `importlib.import_module(module_name)` 的方式动态加载我们所需的 package 以及其中的类。

## 七、参考资料

- [<u>python 中的模块、库、包有什么区别？- 风影忍着的回答 - 知乎</u>][1]
- [<u>python 中的模块、库、包有什么区别？- 看图学的回答 - 知乎</u>][2]
- [<u>[Python 库调用和管理] `__init__.py` 的基本使用和运作机制</u>](https://blog.csdn.net/Bill_seven/article/details/104391208?spm=1001.2014.3001.5502)
- [<u>Python Tips: `__init__.py` 的作用</u>](https://www.cnblogs.com/tp1226/p/8453854.html)
- [<u>`from ... import` vs `import .`</u>](https://stackoverflow.com/questions/9439480/from-import-vs-import)
- [<u>python importlib 用法小结</u>](https://zhuanlan.zhihu.com/p/521128790)

[1]: https://www.zhihu.com/question/30082392/answer/2030353759
[2]: https://www.zhihu.com/question/30082392/answer/2315826832

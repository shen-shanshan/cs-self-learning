# 深入解析 Python 库调用原理与最佳实践

## 基本原理

### `__init__.py` 是什么（what）

**Python 模块中的 `__init__.py` 是做什么的？**

一般来说，`__init__.py` 文件存在于一个需要作为 Python 库被调用的文件夹下；换言之，如果一个文件夹下含有 `__init__.py` 文件，则它可以被作为 Python 库被 import 进任何 Python 工程。

```bash
.
└── package
    ├── __init__.py
    ├── subpackage1
    │   ├── __init__.py
    │   └── subpackage1.py
    └── subpackage2
        ├── __init__.py
        └── subpackage2.py
```

当 Python 程序的工作目录位于当前目录 . 时，则可以运行 import package 调用 package 库。此时 Python 程序将运行 `__init__.py` 中所有的命令，并将所有与 package 模块相关的对象记录。

如果 `__init__.py` 为空，则生成一个空的 package 对象，它是无法自动处理文件夹下的其他文件的。

**总结：`__init__.py` 文件用于标志文件夹是否可以作为 Python 库被调用。它在 import 操作时被执行。**

### `__init__.py` 怎么用（how）

**声明所有对外的对象列表：**

```python
__all__ = ['subpackage1', 'subpackage2']
```

当我们执行 `from package import *` 时，会导入 `subpackage1` 和 `subpackage2` 两个库对象；此时是否能够调用到 `subpackage1` 和 `subpackage2` 的子对象，则取决于这两个库目录下的 `__init__.py` 文件。

`from package import *` 相当于执行了 `import package` 但不在主程区域内生成 `package` 的直接对象，而是生成了一个库对象，可以通过 `sys.modules['package']` 来查询；执行完 `import package` 所需要执行的 `__init__.py` 之后，继续对 `package.__all__` 中的所有对象执行 `import` 操作，并在主程直接生成 `package.__all__` 中的所有对象，此时这些对象不再是 `package` 的元素对象；换言之，相对引用变成了绝对引用，这点可以通过 `dir()` 命令进行查询。**(???)**

**直接引入所有可用的子库对象：**

```python
from .subpackage1 import *
from .subpackage2 import *
```

当我们执行 `from package import *` 时，会默认拉取所有由运行 `__init__.py` 文件生成的 `package` 库的子对象作为对象记录在主程区域，包括 `subpackage1` 和 `subpackage2` 本身以及他们的子对象（如果存在）。

注意重名的情况会导致函数、对象被重新定义因而对程序的运行逻辑产生影响。

### import 原理与用法

**从形式上说：**

- `import ...` 是间接调用，我们要使用 package 库内的方法、对象 `X` 时，需要采用 `package.X` 的方式来访问；
- `from ... import ...` 是直接调用，我们可以直接使用 `X` 来调用 `X` 方法或对象。

**从库加载的本质上说：**

`from ... import ...` 包含了 `import ...` 的执行步骤。

`import ...` 除了加载库，也就是执行 `package.py` 或 `package/__init__.py` 之外，在主程中记录了 package 对象，加载过程中它所涉及的子对象全部被记录在主程中的 package 对象中。

`from ... import ...` 命令在 `import` 阶段的操作除了记录之外，库加载的过程与 `import ...` 是完全一样的。此处要注意， `from A import B` 在加载完声明的库 `A` 后，会多出一步加载 `A.B` 的操作；之后不会直接记录 `A` 对象在主程，而是直接记录 `B` 对象在主程中。如果是 `from A import *`，在 `A.__all__` 没有声明的情况下，默认载入所有 `A` 下的对象。

`from ... import ...` 可以使用相对关系，比如 `from .subpackage1 import *` 或 `from ..package import *`。其加载生成的模块关系是与 `import ...` 完全相同的，只是借助了库的相对关系而已；但是，其所记录的对象是相对于当前库的。换句话说，模块关系是模块关系，可由 `sys.modules[]` 命令进行查询，其遵循文件结构，是文件结构的同构体；对象关系是对象关系，由调用关系和位置决定相对关系，由调用方式决定记录与否，它与文件结构未必同构。 理解以上这句话是非常重要的。**(???)**

补充说明：`from A import B` 显式生成的对象是 `B`，因此 `A` 使用相对关系也可，因为不产生相对母库的 `A` 对象；`import A` 显式生成的对象是 `A`，会生成相对母库的 `A` 对象，因此需要明确的绝对名称和关系。**(???)**

**什么叫主程？母库？使用相对关系？**

**补充：**

使用 `from A import *` 时，若没有定义 `A.__all__` 则默认返回所有 `A` 下对象；若定义了 `A.__all__` 则返回定义列表中的对象。这里要注意，所有 `A` 下对象指的是模块加载成功的对象，并非文件结构中存在的对象。若 `A.__all__` 中定义的对象不存在或者没有加载成功，则可能报错。**(???)**

使用 `import A` 时，会记录所有加载成功的 `A` 以及其下辖的对象，无论显式地记录与否，包括 `from` 操作中的相对继承关系。每一个 `__init__.py` 文件在运行时候，所记录的对象都是在当前对象之下的：若在 `A` 下 `import A.B` 则除了隐式产生 `A.B` 对象之外还产生了 `A.A` 的嵌套对象，因为 `import` 命令显式记录其本身；而在 `A` 下 `from .B import *` 则不会有这个问题因为不记录 `A` 本身，显式记录了 `A.*` 而隐式记录也只记录了 `A.B`。**(???)**

**什么叫 import 显示记录其本身？**

**总而言之，对象记录的过程是对象加载（隐式）和记录（显式）的共同过程。**

**模块关系结构 vs. 对象关系结构：**

如前所说，模块关系结构是与文件结构同构的。其构建随模块加载的过程，每遇到一个新的模块，比如 `A.B` 自动查找母模块 `A` 所在模块位置下的 `B.py` 或 `A/B/__init__.py`，这样生成的模块关系结构自然是与文件结构完全相同的。

对象关系结构，由于 `import ...` 和 `from ... import ...` 命令的存在，相对来说是更加自由的，是由模块关系结构的 blocks 作为积木搭建起来的关系结构。搭建的自由度和文件结构、加载顺序都相关。

特别补充：

无论是 `from A import B` 还是 `import A`，这里的 `A` 一定指的是模块关系（可以是绝对或者相对关系），且无法通过对象关系导入；前者生成的对象是 `B`，对于后者则特别地生成一个对象 `A`。这里如果对象是一个模块，则是对模块本身的一个指针（即把对象作为一个子节点）；如果对象是一个方法或一个函数，则应当是对方法或函数的一段拷贝。

## import ... vs from ... import ...

`import X`:

Imports the module X, and creates a reference to that module in the current namespace. Then you need to define completed module path to access a particular attribute or method from inside the module (e.g.: X.name or X.attribute).

`from X import *`:

Imports the module X, and creates references to all public objects defined by that module in the current namespace (that is, everything that doesn’t have a name starting with _) or whatever name you mentioned.

Or, in other words, after you've run this statement, you can simply use a plain (unqualified) name to refer to things defined in module X. But X itself is not defined, so X.name doesn't work. And if name was already defined, it is replaced by the new version. And if name in X is changed to point to some other object, your module won’t notice.

`from xxx import ... as xxx`: ...

## 循环依赖问题

`TYPE_CHECKING` 的用法（简单介绍 `typing` 库）。

## 动态加载对象

vLLM 对象动态加载代码分析。

## 总结

……

## 参考资料

- [[ Python 库调用和管理 ] `__init__.py` 的基本使用和运作机制](https://blog.csdn.net/Bill_seven/article/details/104391208?spm=1001.2014.3001.5502)
- [`from ... import` vs `import .` [duplicate]](https://stackoverflow.com/questions/9439480/from-import-vs-import)
- [Python Tips: `__init__.py` 的作用](https://www.cnblogs.com/tp1226/p/8453854.html)

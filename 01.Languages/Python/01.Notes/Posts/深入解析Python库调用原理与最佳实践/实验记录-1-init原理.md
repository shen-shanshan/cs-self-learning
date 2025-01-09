# 实验记录

增加 `print(__package__)` 实验记录。
不在 `dir()` 中的不是没有被 import，而是不在命名空间中，不能直接被调用。

```python
import sys

dir() ?
sys.modules ?
globals() ?
locals() ?
```

## 实验 01

目录结构如下：

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
    print("-----------------------------------------")
    print("[" + name + "] Imported dirs are showed below:")
    for dir in dirs:
        print(dir)

print_dir(dir(), "main.py")
```

`package_a/__init__.py`:

```python
print("package_a has been imported.")
```

此时运行 `main.py`，输出如下：

```bash
package_a has been imported.
-----------------------------------------
[main.py] Imported dirs are showed below:
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

> TODO: 查一下这些包都是干啥的？ dir() 是干啥的？

可以看到：

- `package_a/__init__.py` 中的内容被执行了；
- `package_a` 被 import 到了 `main.py` 中。

## 实验 02

当我们希望将 `package_b` 和 `package_c` import 到 `main.py` 中：

`main.py`：

```python
import package_a.package_b
import package_a.package_c


def print_dir(dirs, name):
    print("-----------------------------------------")
    print("[" + name + "] Imported dirs are showed below:")
    for dir in dirs:
        print(dir)

print_dir(dir(), "main.py")
```

输出：

```bash
package_a has been imported.
-----------------------------------------
[main.py] Imported dirs are showed below:
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

发现没有 import 进来。

将 `main.py` 修改为：

```python
from package_a import package_b
from package_a import package_c


def print_dir(dirs, name):
    print("-----------------------------------------")
    print("[" + name + "] Imported dirs are showed below:")
    for dir in dirs:
        print(dir)

print_dir(dir(), "main.py")
```

输出：

```bash
package_a has been imported.
-----------------------------------------
[main.py] Imported dirs are showed below:
__annotations__
__builtins__
__cached__
__doc__
__file__
__loader__
__name__
__package__
__spec__
package_b
package_c
print_dir
```

可以看到：

- b/c import 进来了；
- a 没 import 进来，说明使用 `from A import B` 时，只会引入 B，不会引入 A。

## 实验 03

将 `package_a/__init__.py` 修改为：

```python
print("package_a has been imported.")

__all__ = ['package_b']
```

增加 `package_b/__init__.py`：

```python
print("package_b has been imported.")
```

将 `main.py` 修改为：

```python
import package_a
from package_a import *


def print_dir(dirs, name):
    print("-----------------------------------------")
    print("[" + name + "] Imported dirs are showed below:")
    for dir in dirs:
        print(dir)

print_dir(dir(), "main.py")
print_dir(dir(package_a), "package_a")
```

输出：

```bash
package_a has been imported.
package_b has been imported.
-----------------------------------------
[main.py] Imported dirs are showed below:
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
package_b
print_dir
-----------------------------------------
[package_a] Imported dirs are showed below:
__all__
__builtins__
__cached__
__doc__
__file__
__loader__
__name__
__package__
__path__
__spec__
package_b
```

可以看到：

- `from package_a import *` 语句导入了 `package_b/__init__.py` 中 `__all__` 的包（package_b），而 package_c 没有被导入；
- `package_b/__init__.py` 中的内容被执行了；
- `package_b` 同时被 import 到了 `main.py` 和 `package_a` 中。

`__all__` 关联了一个模块列表，当执行 `from xx import *` 时，就会导入下面列表中的模块。
导入操作会继续查找 package_b 中的 `__init__.py` 并执行（但此时不会执行 `import *`）。

疑问：

为什么 `package_b/__init__.py` 中没有 import `test_b`，却仍然 import 到了 `package_b` 中？？？

## 实验 04

增加 `package_b/test_b.py`：

```python
def print_test_b():
    print("call test_b().")
```

将 `package_a/__init__.py` 修改为：

```python
from package_b import test_b

print("package_b has been imported.")

__all__ = ['package_b']
```

报错：

```bash
ModuleNotFoundError: No module named 'package_b'
```

在我们执行 import 时，当前目录 (main.py) 是不会变的（就算是执行子目录的文件），还是需要完整的包名。

将 `package_a/__init__.py` 修改为：

```python
from package_a.package_b import test_b

print("package_b has been imported.")

__all__ = ['package_b']
```

输出：

```bash
package_a has been imported.
package_b has been imported.
-----------------------------------------
[main.py] Imported dirs are showed below:
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
package_b
print_dir
-----------------------------------------
[package_a] Imported dirs are showed below:
__all__
__builtins__
__cached__
__doc__
__file__
__loader__
__name__
__package__
__path__
__spec__
package_b
test_b
```

发现：`test_b.py` 成功导入 package_a 中。

将 `main.py` 修改为：

```python
import package_a
from package_a import *


def print_dir(dirs, name):
    print("-----------------------------------------")
    print("[" + name + "] Imported dirs are showed below:")
    for dir in dirs:
        print(dir)

print_dir(dir(), "main.py")
print_dir(dir(package_a), "package_a")
print_dir(dir(package_b), "package_b")

package_a.test_b.print_test_b()
package_b.test_b.print_test_b()
```

输出：

```bash
package_a has been imported.
package_b has been imported.
-----------------------------------------
[main.py] Imported dirs are showed below:
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
package_b
print_dir
-----------------------------------------
[package_a] Imported dirs are showed below:
__all__
__builtins__
__cached__
__doc__
__file__
__loader__
__name__
__package__
__path__
__spec__
package_b
test_b
-----------------------------------------
[package_b] Imported dirs are showed below:
__builtins__
__cached__
__doc__
__file__
__loader__
__name__
__package__
__path__
__spec__
test_b
call test_b().
call test_b().
```

说明：`test_b` 同时被 import 到了 `package_a` 和 `package_b` 中，所以上面两种方式都能调用成功。

## 实验 05

将 `package_a/__init__.py` 修改为：

```python
# from package_a.package_b import test_b

print("package_b has been imported.")

__all__ = ['package_b']
```

将 `package_b/__init__.py` 修改为：

```python
from package_a.package_b import test_b

print("package_b has been imported.")
```

输出：

```bash
package_a has been imported.
package_b has been imported.
-----------------------------------------
[main.py] Imported dirs are showed below:
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
package_b
print_dir
-----------------------------------------
[package_a] Imported dirs are showed below:
__all__
__builtins__
__cached__
__doc__
__file__
__loader__
__name__
__package__
__path__
__spec__
package_b
-----------------------------------------
[package_b] Imported dirs are showed below:
__builtins__
__cached__
__doc__
__file__
__loader__
__name__
__package__
__path__
__spec__
test_b
Traceback (most recent call last):
  File "/Users/shanshan-shen/Documents/GitHub/cs-self-learning/01.Languages/Python/01.Notes/Posts/深入解析Python库调用原理与最佳实践/codes/main.py", line 19, in <module>
    package_a.test_b.print_test_b() # ok
AttributeError: module 'package_a' has no attribute 'test_b'
```

说明：`test_b` 只被 import 到了 package_b 中，无法直接通过 package_a 调用。

总结：`__init__.py` 只会将模块加载到当前 package 中。

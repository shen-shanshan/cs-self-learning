# 实验记录 2 - 不同 import 方式的区别

## 实验 01

`main.py`：

```python
import package_a.package_b.test_b


print_test_b()
```

输出：

```bash
NameError: name 'print_test_b' is not defined
```

`main.py`：

```python
import package_a.package_b.test_b


package_a.package_b.test_b.print_test_b()
```

输出：

```bash
package_a has been imported.
package_b has been imported.
call test_b().
```

`main.py`：

```python
from package_a.package_b.test_b import print_test_b


package_a.package_b.test_b.print_test_b()
```

输出：

```bash
NameError: name 'package_a' is not defined
```

根据之前的实验，此时 package_a 是没有被 `main.py` import 的，所以无法调用。

`main.py`：

```python
from package_a.package_b.test_b import print_test_b


print_test_b()
```

调用成功。

## 实验 02

`main.py`：

```python
import package_a.package_b.test_b

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
print_dir
```

说明：

- package_a 和 package_b 的 `__init__.py` 都被调用了；
- `package_a` 被加载到 `main.py` 中了；
- `print_test_b` 没有被加载到 `main.py` 中，所以不能直接调用，需要通过 `package_a` 调用。

`main.py`：

```python
from package_a.package_b import test_b

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
print_dir
print_test_b
```

说明：

- package_a 和 package_b 的 `__init__.py` 都被调用了；
- `print_test_b` 直接被加载到了 `main.py` 中，但 package_a 和 package_b 没被加载。

## 实验 03

`main.py`:

```python
from package_a.package_b.test_b import print_test_b


def print_test_b():
    print("call test_b(). in main")


print_test_b()
```

输出：

```bash
package_a has been imported.
package_b has been imported.
call test_b(). in main
```

`print_test_b()` 被 `main.py` 中的定义覆盖了。

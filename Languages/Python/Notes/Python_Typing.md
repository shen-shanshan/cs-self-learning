# Python typing

## 基本用法

导入类型：

```python
from typing import ...
```

**List**（列表）：

```python
var: List[int or float] = [2, 3.5]
var: List[List[int]] = [[1, 2], [2, 3]]
```

**Tuple**（元组）：

```python
person: Tuple[str, int, float] = ('Mike', 22, 1.75)
```

**Dict**（字典）：

```python
def size(rect: Mapping[str, int]) -> Dict[str, int]:
    return {'width': rect['width'] + 100, 'height': rect['width'] + 100}
```

**Set**（集合）：

```python
def describe(s: AbstractSet[int]) -> Set[int]:
    return set(s)
```

**Any**（所有类型）：

```python
def add(a: Any) -> Any:
    return a + 1

# 等价于：
def add(a):
    return a + 1
```

**Callable**（可调用类型，通常用来注解一个方法）：

```python
def date(year: int, month: int, day: int) -> str:
    return f'{year}-{month}-{day}'

# 返回了 date() 方法本身（为 Callable 类型），中括号内分别标记了返回的方法的参数类型和返回值类型
def get_date_fn() -> Callable[[int, int, int], str]:
    return date
```

**Union**（联合类型）：

```python
Union[int, str]  # 要么是 int 类型，要么是 str 类型
Union[Union[int, str], float] == Union[int, str, float]  # 联合类型的联合类型等价于展平后的类型
Union[int] == int  # 仅有一个参数的联合类型会坍缩成参数自身
Union[int, str, int] == Union[int, str]  # 多余的参数会被跳过
Union[int, str] == Union[str, int]  # 参数顺序会被忽略
```

**Optional**（参数为空或是已经声明的类型）：

```python
Optional[str] == Union[str, None]  # 不代表这个参数可以不传递了，而是说这个参数可以传为 None

# 当一个方法执行结束，如果执行完毕就不返回错误信息，如果发生问题就返回错误信息
def judge(result: bool) -> Optional[str]:
    if result: return 'Error Occurred'
```

## TYPE_CHECKING

`TYPE_CHECKING` 是一个会被第三方静态类型检查器假定为 `True` 的特殊常量，而在运行时将假定为 `False`。

`TYPE_CHECKING` 作为一个静态检查的标志，在真正运行代码的时候它是 `False`，也就是它下面的 `import` 是不执行的，但它可以为第三方静态类型检查器提供所需要检查的类型。

**使用场景：**

解决循环导入问题：两个文件相互导入引用，如果不使用 `if typing.TYPE_CHECKING:` 就直接导入，那么此时就会因为循环导入产生错误。

注意：使用 `TYPE_CHECKING` 导入的任何对象，只能作为注解使用，不可以真的去使用这些对象，因为这些对象只有在编辑器检查的阶段才会被导入，并且在使用这些类型作为解注时，必须使用引号包裹。否则在真正的代码业务执行时，就会抛出 `NameError: xxx is not defined` 错误。

## 参考资料

- [<u>Python 中 typing 模块和类型注解的使用</u>](https://cuiqingcai.com/7071.html)
- [<u>TYPE_CHECKING 的含义和用法</u>](https://blog.csdn.net/Edward__J/article/details/129398949)
- [<u>typing 类型解注全网最强攻略，妈妈再也不用担心我拼错单词辣！</u>](https://juejin.cn/post/7266099935179046948)

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

## 参考资料

- [<u>Python 中 typing 模块和类型注解的使用</u>](https://cuiqingcai.com/7071.html)

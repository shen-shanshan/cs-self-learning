# Python Decorator

## 基本原理

……

## 内置装饰器

### @property

`@property`：把一个方法变成属性调用。

**示例一：**

把一个 getter 方法变成属性，只需要加上 `@property` 就可以了，此时，`@property` 本身又创建了另一个装饰器 `@score.setter`，负责把一个 setter 方法变成属性赋值。

```python
class Student(object):
    @property
    def score(self):
        return self._score

    @score.setter
    def score(self, value):
        if not isinstance(value, int):
            raise ValueError('score must be an integer!')
        if value < 0 or value > 100:
            raise ValueError('score must between 0 ~ 100!')
        self._score = value
```

**示例二：**

`@property` 的作用：

- 重命名类的变量；
- 控制哪些变量是只读的（只有 `@property`），哪些是可写的（还有 `@local_path.setter`）。

```python
class LoRARequest(
        msgspec.Struct,
        omit_defaults=True,
        array_like=True):

    __metaclass__ = AdapterRequest

    lora_name: str
    lora_int_id: int
    lora_path: str = ""
    lora_local_path: Optional[str] = msgspec.field(default=None)
    long_lora_max_len: Optional[int] = None
    base_model_name: Optional[str] = msgspec.field(default=None)

    @property
    def adapter_id(self):
        return self.lora_int_id

    @property
    def name(self):
        return self.lora_name

    @property
    def path(self):
        return self.lora_path

    @property
    def local_path(self):
        warnings.warn(
            "The 'local_path' attribute is deprecated "
            "and will be removed in a future version. "
            "Please use 'path' instead.",
            DeprecationWarning,
            stacklevel=2)
        return self.lora_path

    @local_path.setter
    def local_path(self, value):
        warnings.warn(
            "The 'local_path' attribute is deprecated "
            "and will be removed in a future version. "
            "Please use 'path' instead.",
            DeprecationWarning,
            stacklevel=2)
        self.lora_path = value
```

**参考资料：**

- [<u>使用 @property - 廖雪峰</u>](https://liaoxuefeng.com/books/python/oop-adv/property/index.html)

### @abstractmethod

`@abstractmethod` 由标准模块 `abc` 提供。

使用 `@abstractmethod` 抽象方法：

- 所在的 class 继承 `abc.ABC`；
- 给需要抽象的实例方法添加装饰器 `@abstractmethod`。

完成这两步后，这个 class 就变成了抽象类，不能被直接实例化，要想使用抽象类，必须继承该类并实现该类的所有抽象方法。

**示例一：**

```python
from abc import ABC, abstractmethod

class Animal(ABC):
    @abstractmethod
    def info(self):
        print("Animal")

class Bird(Animal):
    # 实现抽象方法
    def info(self):
        # 调用基类方法（即便是抽象方法）
        super().info()
        print("Bird")
```

除了继承 `abc.ABC`，也可以使用 metaclass 达到相同的效果。

> 注意：如果抽象方法不仅是实例方法, 而且还是类方法或静态方法, 则应分别使用 `@abstractclassmethod` 和 `@abstractstaticmethod`。

**示例二：**

```python
from abc import abstractmethod, ABCMeta

class Animal(metaclass=ABCMeta):
    @abstractmethod
    def info(self):
        print("Animal")
```

**示例三：**

```python
from abc import ABC, abstractmethod

class AdapterRequest(ABC):

    @property
    @abstractmethod
    def adapter_id(self) -> int:
        raise NotImplementedError
# -----------------------------------
class LoRARequest(msgspec.Struct, omit_defaults=True, array_like=True):

    __metaclass__ = AdapterRequest

    @property
    def adapter_id(self):
        return self.lora_int_id
```

## 参考资料

- [<u>python3-cookbook</u>](https://python3-cookbook.readthedocs.io/zh-cn/latest/c09/p01_put_wrapper_around_function.html#)

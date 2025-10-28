# Python 中 copy.deepcopy 的原理和用法

## 1. 基本概念

`copy.deepcopy()` 是 Python 标准库 `copy` 模块中的一个函数，用于创建对象的**深度拷贝**（深拷贝）。

```python
import copy

# 浅拷贝 vs 深拷贝
original_list = [1, 2, [3, 4]]
shallow_copy = copy.copy(original_list)    # 浅拷贝
deep_copy = copy.deepcopy(original_list)   # 深拷贝
```

## 2. 深拷贝的原理

### 工作原理

- **递归复制**：深度拷贝会递归地遍历对象的所有层级，为每一层创建新的对象
- **完全独立**：新对象与原始对象完全独立，修改一个不会影响另一个
- **处理循环引用**：能够正确处理对象间的循环引用关系

### 实现机制

```python
# 简化的深拷贝原理示意
def deep_copy(obj, memo=None):
    if memo is None:
        memo = {}
    
    # 检查是否已经拷贝过（处理循环引用）
    if id(obj) in memo:
        return memo[id(obj)]
    
    # 创建新对象
    if isinstance(obj, list):
        new_obj = []
        memo[id(obj)] = new_obj
        for item in obj:
            new_obj.append(deep_copy(item, memo))
        return new_obj
    elif isinstance(obj, dict):
        new_obj = {}
        memo[id(obj)] = new_obj
        for key, value in obj.items():
            new_obj[deep_copy(key, memo)] = deep_copy(value, memo)
        return new_obj
    # ... 处理其他类型
    else:
        # 不可变对象直接返回
        return obj
```

## 3. 使用示例

### 基本用法

```python
import copy

# 列表的深拷贝
original = [1, 2, [3, 4]]
deep_copied = copy.deepcopy(original)

# 修改深拷贝后的对象
deep_copied[2].append(5)

print("Original:", original)      # [1, 2, [3, 4]]
print("Deep copy:", deep_copied)  # [1, 2, [3, 4, 5]]
```

### 嵌套对象的深拷贝

```python
import copy

# 嵌套字典
data = {
    'name': 'Alice',
    'scores': [85, 92, 78],
    'info': {
        'age': 25,
        'hobbies': ['reading', 'swimming']
    }
}

deep_copy = copy.deepcopy(data)

# 修改深拷贝
deep_copy['scores'].append(95)
deep_copy['info']['hobbies'].append('coding')

print("Original:", data)
# {'name': 'Alice', 'scores': [85, 92, 78], 'info': {'age': 25, 'hobbies': ['reading', 'swimming']}}

print("Deep copy:", deep_copy)
# {'name': 'Alice', 'scores': [85, 92, 78, 95], 'info': {'age': 25, 'hobbies': ['reading', 'swimming', 'coding']}}
```

### 自定义类的深拷贝

```python
import copy

class Person:
    def __init__(self, name, friends=None):
        self.name = name
        self.friends = friends if friends is not None else []
    
    def __repr__(self):
        return f"Person({self.name}, friends: {[f.name for f in self.friends]})"

# 创建对象
alice = Person("Alice")
bob = Person("Bob")
charlie = Person("Charlie")

alice.friends = [bob, charlie]
bob.friends = [alice]

# 深拷贝
alice_copy = copy.deepcopy(alice)

# 修改拷贝
alice_copy.name = "Alice Copy"
alice_copy.friends[0].name = "Bob Copy"

print("Original Alice:", alice)        # Person(Alice, friends: ['Bob', 'Charlie'])
print("Deep copy Alice:", alice_copy)  # Person(Alice Copy, friends: ['Bob Copy', 'Charlie'])
```

## 4. 与浅拷贝的对比

```python
import copy

original = [1, 2, [3, 4]]

# 浅拷贝
shallow = copy.copy(original)
# 深拷贝
deep = copy.deepcopy(original)

# 修改第一层
shallow[0] = 100
deep[0] = 100

print("After modifying first level:")
print("Original:", original)  # [1, 2, [3, 4]]
print("Shallow:", shallow)    # [100, 2, [3, 4]]
print("Deep:", deep)          # [100, 2, [3, 4]]

# 修改嵌套层
shallow[2].append(5)
deep[2].append(5)

print("\nAfter modifying nested level:")
print("Original:", original)  # [1, 2, [3, 4, 5]] - 被浅拷贝影响!
print("Shallow:", shallow)    # [100, 2, [3, 4, 5]]
print("Deep:", deep)          # [100, 2, [3, 4, 5]]
```

## 5. 处理循环引用

```python
import copy

# 创建循环引用
list_a = [1, 2]
list_b = [3, 4]
list_a.append(list_b)
list_b.append(list_a)

print("Original structure:")
print(f"list_a: {list_a}")  # [1, 2, [3, 4, [...]]]
print(f"list_b: {list_b}")  # [3, 4, [1, 2, [...]]]

# 深拷贝可以正确处理循环引用
list_a_copy = copy.deepcopy(list_a)
print("\nAfter deep copy:")
print(f"list_a_copy: {list_a_copy}")  # [1, 2, [3, 4, [...]]]
```

## 6. 性能考虑

```python
import copy
import time

# 深拷贝比浅拷贝更耗时
large_list = [list(range(1000)) for _ in range(1000)]

# 测试浅拷贝时间
start = time.time()
shallow_copy = copy.copy(large_list)
shallow_time = time.time() - start

# 测试深拷贝时间
start = time.time()
deep_copy = copy.deepcopy(large_list)
deep_time = time.time() - start

print(f"Shallow copy time: {shallow_time:.4f} seconds")
print(f"Deep copy time: {deep_time:.4f} seconds")
```

## 7. 自定义深拷贝行为

你可以为自定义类定义 `__deepcopy__` 方法来控制深拷贝行为：

```python
import copy

class CustomClass:
    def __init__(self, data, special_attr=None):
        self.data = data
        self.special_attr = special_attr
        self.timestamp = time.time()  # 不希望被拷贝的属性
    
    def __deepcopy__(self, memo):
        # 创建新实例，但不拷贝 timestamp
        new_instance = self.__class__(
            copy.deepcopy(self.data, memo),
            copy.deepcopy(self.special_attr, memo)
        )
        # timestamp 使用当前时间
        new_instance.timestamp = time.time()
        return new_instance
```

## 总结

- **深拷贝**创建完全独立的对象副本，包括所有嵌套对象
- **适用场景**：需要完全独立的对象副本，避免意外的副作用
- **性能代价**：比浅拷贝更耗时，特别是对于大型嵌套结构
- **智能处理**：能够正确处理循环引用和复杂对象关系
- **自定义控制**：可以通过 `__deepcopy__` 方法自定义拷贝行为

在需要确保对象完全独立，不受原始对象修改影响时，应该使用 `copy.deepcopy()`。

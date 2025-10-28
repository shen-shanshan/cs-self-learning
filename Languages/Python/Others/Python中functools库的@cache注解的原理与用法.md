# Python functools 库中的 @cache 装饰器

`@cache` 是 Python 3.9+ 中引入的一个装饰器，用于自动缓存函数的结果，基于函数的参数。它是 `@lru_cache(maxsize=None)` 的简化版本。

## 原理

`@cache` 的核心原理是 **记忆化（Memoization）**：

1. **参数作为键**：将函数的参数转换为哈希键
2. **结果缓存**：将函数返回值存储在与参数键对应的缓存中
3. **命中返回**：当相同参数再次出现时，直接返回缓存结果，避免重复计算

## 基本用法

```python
from functools import cache

@cache
def fibonacci(n):
    if n < 2:
        return n
    return fibonacci(n-1) + fibonacci(n-2)

# 首次计算会执行实际计算
print(fibonacci(10))  # 55

# 相同参数再次调用，直接返回缓存结果
print(fibonacci(10))  # 55 (从缓存返回)
```

## 与 @lru_cache 的比较

```python
from functools import cache, lru_cache

# 以下两种方式是等价的
@cache
def func1(x):
    return x * x

@lru_cache(maxsize=None)
def func2(x):
    return x * x
```

## 实际应用示例

### 1. 计算密集型函数

```python
@cache
def expensive_calculation(x, y):
    print(f"Computing {x} + {y}...")  # 只有首次计算会打印
    return x ** 2 + y ** 2

print(expensive_calculation(3, 4))  # 输出: Computing 3 + 4... \n 25
print(expensive_calculation(3, 4))  # 直接输出: 25 (无打印信息)
```

### 2. API 调用缓存

```python
import requests
from functools import cache

@cache
def get_user_data(user_id):
    # 模拟 API 调用
    print(f"Fetching data for user {user_id}")
    return {"user_id": user_id, "name": f"User {user_id}"}

# 第一次调用会执行实际请求
user1 = get_user_data(1)  # 输出: Fetching data for user 1

# 相同参数再次调用，直接返回缓存
user1_cached = get_user_data(1)  # 无输出，使用缓存
```

### 3. 配置读取

```python
@cache
def load_config(config_path):
    print(f"Loading config from {config_path}")
    # 模拟读取配置文件
    return {"path": config_path, "settings": "some_settings"}

config1 = load_config("/etc/app/config.json")  # 输出: Loading config...
config2 = load_config("/etc/app/config.json")  # 无输出，使用缓存
```

## 注意事项

### 1. 参数必须可哈希

```python
@cache
def process_data(data_list):  # 错误！列表不可哈希
    return sum(data_list)

# 正确做法：使用元组
@cache
def process_data(*args):
    return sum(args)

print(process_data(1, 2, 3))  # 正常工作
```

### 2. 副作用函数不适合缓存

```python
# 不适合缓存的例子 - 有副作用
counter = 0

@cache
def increment_counter():
    global counter
    counter += 1
    return counter

print(increment_counter())  # 1
print(increment_counter())  # 1 (缓存了结果，副作用只发生一次)
```

### 3. 需要清除缓存的情况

```python
@cache
def cached_function(x):
    return x * 2

# 清除特定参数的缓存
cached_function.cache_clear()

# 查看缓存信息
print(cached_function.cache_info())  # 输出缓存命中情况
```

## 性能对比

```python
from functools import cache
import time

def fibonacci_naive(n):
    if n < 2:
        return n
    return fibonacci_naive(n-1) + fibonacci_naive(n-2)

@cache
def fibonacci_cached(n):
    if n < 2:
        return n
    return fibonacci_cached(n-1) + fibonacci_cached(n-2)

# 性能测试
start = time.time()
result1 = fibonacci_naive(30)
time1 = time.time() - start

start = time.time()
result2 = fibonacci_cached(30)
time2 = time.time() - start

print(f"无缓存: {time1:.4f}秒")
print(f"有缓存: {time2:.4f}秒")
print(f"加速比: {time1/time2:.1f}倍")
```

## 总结

`@cache` 装饰器适用于：

- **计算密集型函数**
- **重复调用相同参数的函数**
- **IO 操作（如 API 调用、文件读取）**
- **配置加载等初始化操作**

不适用于：

- **有副作用的函数**
- **参数不可哈希的情况**
- **需要频繁更新结果的场景**

使用 `@cache` 可以显著提升程序性能，但需要注意内存使用和缓存一致性。

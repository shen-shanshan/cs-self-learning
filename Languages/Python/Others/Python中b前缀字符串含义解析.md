在Python中，字符串前面加`b`表示创建一个**字节串(bytes)**对象，而不是普通的字符串(str)对象。

## 基本概念

```python
# 普通字符串 (str)
text_str = "hello"
print(type(text_str))  # <class 'str'>

# 字节串 (bytes)
text_bytes = b"hello"  
print(type(text_bytes))  # <class 'bytes'>
```

## 主要区别

| 特性 | 字符串(str) | 字节串(bytes) |
|------|-------------|---------------|
| 编码 | Unicode字符 | 原始字节序列 |
| 使用场景 | 文本处理 | 二进制数据、网络传输、文件I/O |
| 可读性 | 人类可读 | 机器可读 |
| 前缀 | 无或`u` | `b` |

## 实际应用示例

```python
# 1. 网络编程
import socket
data = b"GET / HTTP/1.1\r\nHost: example.com\r\n\r\n"

# 2. 文件操作 - 二进制读写
with open("image.jpg", "rb") as f:  # 读取二进制文件
    image_data = f.read()  # 返回bytes对象

# 3. 编码转换
text = "你好"
utf8_bytes = text.encode('utf-8')  # b'\xe4\xbd\xa0\xe5\xa5\xbd'
print(utf8_bytes == b"\xe4\xbd\xa0\xe5\xa5\xbd")  # True

# 4. 只能包含ASCII字符
# b"中文"  # 这会报错，因为包含非ASCII字符

# 5. 字节串操作
data = b"ready"
print(data[0])     # 114 (ASCII码值)
print(data[1:4])   # b'ead'
```

## 编码转换

```python
# 字符串转字节串
text = "ready"
bytes_data = text.encode('utf-8')  # 等同于 b"ready"

# 字节串转字符串
bytes_data = b"ready"
text = bytes_data.decode('utf-8')  # "ready"
```

## 总结

`b"ready"`创建的是一个字节序列，主要用于处理二进制数据。在需要与底层系统、网络协议或二进制文件交互时使用字节串，而在处理文本内容时使用普通字符串。

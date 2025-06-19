# C++ 常用三方库

## gtest

**参考资料：**

- [GoogleTest + VScode 编译和编写简单测试用例](https://blog.csdn.net/Msjiangmei/article/details/130151637)
- [[C++] gtest 入门教程](https://blog.csdn.net/jkddf9h8xd9j646x798t/article/details/105964161)

## glog

头文件：`#include <glog/logging.h>`。

### 日志级别

- `INFO`
- `WARNING`
- `ERROR`
- `FATAL`

打印 `FATAL` 消息会在打印完成后终止程序。

### CHECK 宏

`glog` 提供了 `CHECK()` 宏帮助我们检查程序的错误，当 `CHECK()` 的条件不满足时，它会记录 `FATAL` 日志并终止程序。

```cpp
#define CHECK_EQ(x,y) CHECK_OP(x,y,EQ,==)
#define CHECK_NE(x,y) CHECK_OP(x,y,NE,!=)
#define CHECK_LE(x,y) CHECK_OP(x,y,LE,<=)
#define CHECK_LT(x,y) CHECK_OP(x,y,LT,<)
#define CHECK_GE(x,y) CHECK_OP(x,y,GE,>=)
#define CHECK_GT(x,y) CHECK_OP(x,y,GT,>)
```

- `CHECK(Condition expression)`：判断条件表达式是否成立；
- `CHECK_NE(a, b)`：判断两个值是否不相等；
- `CHECK_EQ(a, b)`：判断两个值是否相等；
- `CHECK_NOTNULL`：判断是否为空指针。

> 注意：与 `assert()` 不同的是，无论程序是否开启 `NODEBUG`，`CHECK()` 都会执行。

**参考资料：**

- [glog 使用记录](https://blog.csdn.net/fb_941219/article/details/122467769)

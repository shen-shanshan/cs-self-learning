# C++ 基础知识

## 常用关键字

### auto

`auto` 允许编译器根据初始化表达式自动推导变量的类型，从而减少了手动指定类型的繁琐工作。这不仅提高了代码的可维护性，还减少了类型错误的可能性。

**注意事项：**

- `auto` 关键字不能用于函数参数声明，这是因为函数参数的类型必须在函数声明时明确指定，而 `auto` 关键字只能用于变量的类型推导；
- `auto` 关键字不能用于直接声明数组类型，这是因为数组的大小必须在编译时确定，而 `auto` 关键字用于类型推导时，无法推导出数组的大小。

**参考资料：**

- [【C++】auto 关键字（C++ 11，超详细解析，小白必看系列）](https://blog.csdn.net/m0_73494049/article/details/141115004)

### const

**`const` 修饰普通类型的变量：**

即常量（无法修改其值）。

**`const` 修饰指针变量：**

- 若 `const` 修饰指针指向的内容，则内容为不可变量；
- 若 `const` 修饰指针，则指针为不可变量；
- 若 `const` 修饰指针和指针指向的内容，则指针和指针指向的内容都为不可变量。

```c++
// 常量指针？不能修改指针指向的值
const int *p = 8;

// 指针常量？不能修改指针指向的地址
int a = 8;
int* const p = &a;
*p = 9; // 正确
int b = 7;
p = &b; // 错误

// 结合上面两种
int a = 8;
const int * const p = &a;
```

> 总结：“左定值，右定向”。

**`const` 修饰函数参数：**

```c++
void test1(const int a)
{
    cout<<a;
    // ++a; 是错误的，a 不能被改变
}

// 当 const 参数为指针时，可以防止指针被意外篡改
void test2(int *const a) // a 的指向不能修改，但是其指向的值可以变化（左定值，右定向）
{
    cout<<*a<<" ";
    *a = 9; // a 变为 9
}
```

**`const` 修饰函数的返回值：**

……

**`const` 修饰类成员函数：**

- 被 `const` 修饰的成员函数无法修改被调用对象的值；
- 被 `mutable` 关键字修饰的成员可以被 `const` 成员函数修改。

```cpp
#include <iostream>

using namespace std;

class Test
{
  public:
    Test(int _m, int _t) : _cm(_m), _ct(_t){}
    void Kf() const
    {
        ++_cm; // 错误
        ++_ct; // 正确
    }
  private:
    int _cm;
    mutable int _ct;
};
 
int main(void)
{
    Test t(8, 7);
    return 0;
}
```

> 注意：`const` 关键字不能与 `static` 关键字同时使用，因为 `static` 关键字修饰静态成员函数，静态成员函数不含有 `this` 指针，即不能实例化，而 `const` 成员函数必须具体到某一实例。

**参考资料：**

- [C++ const 关键字小结](https://www.runoob.com/w3cnote/cpp-const-keyword.html)

### virtual

**“虚函数”：**

```c++
virtual void print()
{
    cout<<"...";
}
```

只有在通过基类指针或引用间接指向派生类子类型时多态性才会起作用：

- 子类指针的所有函数调用都只是调用自己的函数，和多态性无关；
- 基类指针的函数调用如果有 `virtual` 则根据多态性调用派生类的，如果没有 `virtual` 则是正常的静态函数调用，还是调用基类的。

**“纯虚函数”：**

“纯虚函数”提供了一个可被子类型改写的接口，但它本身并不能通过虚拟机制被调用。

```c++
// 函数声明后面紧跟赋值 0
virtual void print() = 0;
```

包含一个或多个“纯虚函数”的类被编译器识别为抽象基类。抽象基类不能被实例化，一般用于继承。

> 总结：“虚函数”在基类和子类中都有对应的实现；“纯虚函数”在基类中没有实现，只能在子类中实现。

**“虚继承”：**

“虚继承”主要用于解决“菱形继承”问题：

- **“虚继承”**：在继承定义中包含了 `virtual` 关键字的继承关系；
- **“虚基类”**：在虚继承体系中，通过 `virtual` 继承而来的基类（`A`）。

```c++
class A {/*...*/};

class B : public virtual A {/*...*/};
```

**参考资料：**

- [C++ 中 virtual 关键字的用法](https://zhuanlan.zhihu.com/p/147601339)

### override

如果派生类在虚函数声明时使用了 `override` 描述符，那么该函数必须重载其基类中的同名函数，否则代码将无法通过编译。

### explicit

`explicit` 用来修饰只有一个参数的类构造函数，以表明该构造函数是显式的，而非隐式的。它将禁止类对象之间的隐式转换，以及禁止隐式调用拷贝构造函数。

> **“隐式类型转换”**：先构造一个临时对象，然后再通过这个临时对象进行“拷贝构造”给目标对象。

**为什么需要 `explicit` 关键字？**

对于可读性不是很好的代码，可以使用 `explicit` 修饰构造函数，将会禁止构造函数的隐式转换，以确保类型转换的清晰和明确。

**参考资料：**

- [【C++】explicit 关键字详解](https://blog.csdn.net/weixin_45031801/article/details/137796214)

## 常用函数

C++ 引入了四种功能不同的强制类型转换运算符以进行强制类型转换：`static_cast`、`dynamic_cast`、`const_cast` 和 `reinterpret_cast`。

### static_cast

`static_cast` 能够完成指向相关类的指针上的转换，`upcast` 和 `downcast` 都能够支持，但并不会有运行时的检查来确保转换到目标类型上的指针所指向的对象**有效且完整**（？），常用于进行比较自然和低风险的转换，如整型和浮点型、字符型之间的互相转换。

`static_cast` 能够完成所有隐式类型转换以及它们的相反转换：

- `void*` 与任意其它类型指针的相互转换；
- 整型、浮点型以及枚举类型之间的相互转换。

### dynamic_cast

`dynamic_cast` 只能够用在指向类的指针或者引用上（或 `void*`），这种转换的目的是确保目标指针类型所指向的是一个**有效且完整**（？）的对象。

- 当被转换指针指向的对象不完整时，会返回空指针来表示转换失败；
- 如果是无法完成的引用转换，则会抛出 `bad_cast` 异常。

此外 `dynamic_cast` 也能够完成空指针在任意指针类型上的转换，以及任意指针类型向 `void*` 的转换。

### const_cast

`const_cast` 可以用来设置或者移除指针所指向对象的 `const`。

示例：

```cpp
void print (char * str)
{
  cout << str << '\n';
}

int main () {
  const char * c = "sample text";
  print(const_cast<char*>(c));  // 把一个 const 指针传入一个接受非 const 指针的函数里
  return 0;
}
```

### reinterpret_cast

`reinterpret_cast` 能够完成任意指针类型向任意指针类型的转换，即使它们毫无关联。该转换的操作结果是出现一份完全相同的二进制复制品，既不会有指向内容的检查，也不会有指针本身类型的检查。

基本上 `reinterpret_cast` 能做但 `static_cast` 不能做的转换大多都是一些基于重新解释二进制的底层操作，因此会导致代码限定于特定的平台进而导致差移植性。

示例：

```cpp
class A { /* ... */ };
class B { /* ... */ };

A * a = new A;
B * b = reinterpret_cast<B*>(a);  // 将 A 指向的二进制块当做 B 来重新解释，虽然可以编译，但没什么用处
```

## 面向对象

### 继承

派生类可以访问基类中所有的非私有成员。因此基类成员如果不想被派生类的成员函数访问，则应在基类中声明为 `private`。

不同访问修饰符类型的区别如下：

![access-specifier](./images/Snipaste_2025-01-27_14-32-56.png)

**继承类型：**

- **公有继承（public）**：当一个类派生自公有基类时，基类的公有成员也是派生类的公有成员，基类的保护成员也是派生类的保护成员，基类的私有成员不能直接被派生类访问，但是可以通过调用基类的公有和保护成员来访问；
- **保护继承（protected）**：当一个类派生自保护基类时，基类的公有和保护成员将成为派生类的保护成员；
- **私有继承（private）**：当一个类派生自私有基类时，基类的公有和保护成员将成为派生类的私有成员。

> 一般来说，几乎不使用 `protected` 或 `private` 继承，而是使用 `public` 继承居多。

## 函数指针

声明方式：

```cpp
data_types (*func_pointer)(data_types arg1, data_types arg2, ...);

// 这里定义了一个指向一个函数（入参和返回值都为 int 类型）的指针：fp
int (*fp)(int a);
```

示例：

```cpp
void add_kernel_cpu(const tensor::Tensor& input1, const tensor::Tensor& input2,
                    const tensor::Tensor& output, void* stream) {/*...*/}
void add_kernel_cu(const tensor::Tensor& input1, const tensor::Tensor& input2,
                   const tensor::Tensor& output, void* stream) {/*...*/}

typedef void (*AddKernel)(const tensor::Tensor& input1, const tensor::Tensor& input2,
                          const tensor::Tensor& output, void* stream);

AddKernel get_add_kernel(base::DeviceType device_type) {
  if (device_type == base::DeviceType::kDeviceCPU) {
    return add_kernel_cpu; // 这里返回的是一个函数指针（函数的名字就是一个指向函数地址的指针）
  } else if (device_type == base::DeviceType::kDeviceCUDA) {
    return add_kernel_cu; // 这里返回的是一个函数指针（函数的名字就是一个指向函数地址的指针）
  } else {
    LOG(FATAL) << "Unknown device type for get a add kernel.";
    return nullptr;
  }
}
```

> 函数存放在内存的代码区域内，它们同样有地址，它的地址就是函数的名字；同数组一样，数组的名字就是数组的起始地址。

**参考资料：**

- [C++ 函数指针 & 类成员函数指针](https://www.runoob.com/w3cnote/cpp-func-pointer.html)

## 智能指针

### shared_ptr

`shared_ptr` 采用引用计数，每一个 `shared_ptr` 的拷贝都指向相同的内容，当最后一个 `shared_ptr` 析构的时候，内存就会被释放。

**声明方式：**

```cpp
#include <memory>

class A {/*...*/}

A a;
auto sp1 = std::make_shared<A>(a); // 被引用则会增加计数

std::shared_ptr<A> sp2(new A);

std::shared_ptr<A> sp3(sp1); // 再次被引用则计数 +1
```

> 注意：一般来说 `std::make_shared` 是最推荐的一种写法。——为什么？

在函数内改变计数（声明新的 `shared_ptr`），超过生命周期（函数退出）后计数会恢复（该 `shared_ptr` 被析构）。

获取原指针：`sp.get()`。

当使用 `shared_ptr` 删除数组时，需要指定删除器。

**底层原理：**

智能指针是**模板类**而不是指针，创建一个智能指针时，必须指针可以指向的类型（`shared_ptr<xxx>`）。

智能指针本质上就是一个重载了 `->` 和 `*` 操作符的类，由类来实现对内存的管理，确保即使有异常产生，也可以通过智能指针类的析构函数完成内存的释放。

智能指针利用了引用计数技术和 C++ 的 RAII（资源获取就是初始化）特性：

- **引用计数**：可以跟踪对象所有权，并能够自动销毁对象，可以说引用计数就是一个简单的垃圾回收系统；
- **RAII**：可以保证在任何情况下，使用对象时先构造对象，最后析构对象。

**参考资料：**

- [【智能指针】shared_ptr基本用法和原理（共享指针）](https://blog.csdn.net/bandaoyu/article/details/107133606)

### shared_from_this & enable_shared_from_this

**应用场景：**

在对象的成员函数中获取指向自身的智能指针，以确保对象在某些操作（特别是异步操作）期间的生命周期。在涉及异步操作（如异步 I/O、定时器、线程等）时，需要确保对象在异步操作完成之前不会被销毁。

当对象的生命周期可能在外部被管理，但在内部需要延长自身的生命周期，比如：资源管理类——其对象管理着一些资源，需要在异步清理或回收资源时防止自身被销毁。

```cpp
class Buffer : std::enable_shared_from_this<Buffer> {
  private:
    // ...
    std::shared_ptr<DeviceAllocator> allocator_;
    // ...
};
```

**优点：**

- **确保对象生命周期的安全**：使用 `shared_from_this` 可以获取到自身的 `std::shared_ptr`，从而增加对象的引用计数，确保对象在异步操作或回调过程中不会被销毁，避免出现悬空指针和未定义行为；
- **简化内存管理**：通过智能指针自动管理对象的生命周期，减少了手动管理内存的复杂性，降低了内存泄漏和双重释放的风险；
- **防止多重所有权问题**：直接在类内部使用 `std::shared_ptr(this)` 可能导致多个独立的 `shared_ptr` 管理同一对象，造成引用计数不一致。`std::enable_shared_from_this` 确保了所有的 `shared_ptr` 共享同一个引用计数。

**示例：**

```cpp
#include <iostream>
#include <memory>

class AsyncWorker : public std::enable_shared_from_this<AsyncWorker> {
public:
    void startAsyncOperation() {
        // 假设这是一个异步操作的模拟
        auto self = shared_from_this(); // 获取自身的 shared_ptr
        std::thread([self]() {
            // 在异步线程中使用 self，确保对象不被销毁
            self->doWork();
        }).detach();
    }

    void doWork() {
        // 执行实际工作
        std::cout << "Async work is done." << std::endl;
    }
};

int main() {
    auto worker = std::make_shared<AsyncWorker>();
    worker->startAsyncOperation();
    // 主线程可能在此退出，但 AsyncWorker 对象仍然存活，直到异步操作完成
    std::this_thread::sleep_for(std::chrono::seconds(1));
    return 0;
}
```

如果不使用 `shared_from_this`，在 `startAsyncOperation` 中捕获 `this`，一旦 `worker` 在主线程中超出作用域被销毁，异步线程中的 `this` 就变成了悬空指针。解决方案：通过 `shared_from_this` 获取 `shared_ptr`，增加引用计数，确保对象在异步操作期间存活。

**注意事项：**

- **对象必须由 `std::shared_ptr` 管理**：使用 `shared_from_this` 的前提是对象已被 `std::shared_ptr` 管理，否则会导致未定义行为；
- **避免在构造函数或析构函数中使用 `shared_from_this`**：此时对象可能尚未完全构造或已经开始析构，使用 `shared_from_this` 不安全；
- **不要手动创建新的 `shared_ptr`**：避免使用 `std::shared_ptr(this)`，这会绕过已有的引用计数管理，导致错误。

**参考资料：**

- [【编程技巧】深入理解 std::enable_shared_from_this 与 shared_from_this](https://blog.csdn.net/qq_21438461/article/details/142532830)

## 编译构建

### g++

g++ 是 GNU 编译器集合（GCC）中的 C++ 编译器，提供了丰富的功能和选项，帮助开发者编译、链接和优化 C++ 代码。

基本语法：

```bash
g++ [options] source_files -o output_file
```

- `options`：编译器的选项参数，用于指定编译器的行为和配置；
- `source_files`：要编译的源代码文件，可以是单个文件或多个文件；
- `output_file`：生成的可执行文件的名称。

### cmake

`CMAKE_BUILD_TYPE` 是 cmake 中用于指定构建类型的内置变量。它的默认值是空字符串，这意味着如果不显式设置该变量，cmake 将不会应用任何特定的构建类型。

- `Release`：启用各种优化选项，以提高代码的性能和运行速度；
- ……

**参考资料：**

- [CMake 教程 - 菜鸟教程](https://www.runoob.com/cmake/cmake-tutorial.html)

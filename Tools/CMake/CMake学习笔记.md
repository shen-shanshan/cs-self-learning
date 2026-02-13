# CMake 学习笔记

## 概述

CMake 是一个开源的跨平台自动化建构系统，用来管理软件建置的程序，并不依赖于某特定编译器，并可支持多层目录、多个应用程序与多个函数库。

CMake 通过使用简单的配置文件 `CMakeLists.txt`，自动生成不同平台的构建文件（如 Makefile、Ninja 构建文件、Visual Studio 工程文件等），简化了项目的编译和构建过程。

CMake 本身不是构建工具，而是生成构建系统的工具，它生成的构建系统可以使用不同的编译器和工具链。

**工作流程：**

1. 编写 `CMakeLists.txt` 文件；
2. 生成构建文件：`cmake ...`；
3. 执行构建：`make ...`。

## CMakeLists.txt

`CMakeLists.txt` 是 CMake 的配置文件，用于定义项目的构建规则、依赖关系以及编译选项等。

### 常用指令

定义项目的名称和使用的编程语言：

```cmake
project(<project_name> [<language>...])
project(MyProject CXX)
```

设置变量的值：

```cmake
set(<variable> <value>...)
set(CMAKE_CXX_STANDARD 11)
```

查找系统安装的库或第三方库：

```cmake
find_package(Boost REQUIRED)
find_package(Boost 1.70 REQUIRED) # 指定版本
find_package(OpenCV REQUIRED PATHS /path/to/opencv) # 指定路径
```

添加头文件搜索路径：

```cmake
include_directories(<dirs>...)
include_directories(${PROJECT_SOURCE_DIR}/include)
```

添加链接文件搜索路径：

```cmake
link_directories(${Boost_LIBRARY_DIRS})
```

创建一个库（静态库或动态库）及其源文件：

```cmake
add_library(<target> <source_files>...)
add_library(MyLibrary STATIC library.cpp)
```

指定要生成的可执行文件和其源文件：

```cmake
add_executable(<target> <source_files>...)
add_executable(MyExecutable main.cpp other_file.cpp)
```

链接目标文件与其他库：

```cmake
target_link_libraries(<target> <libraries>...)
target_link_libraries(MyExecutable Boost::Boost)
```

## 构建流程

为了保持源代码的整洁，CMake 鼓励使用独立的构建目录（`build`）存放构建生成的文件，与源代码分开存放。

构建流程：

```bash
mkdir build
cd build
cmake ..
# .. 为 CMakeLists.txt 所在的位置
# 也可以使用 -DCMAKE_BUILD_TYPE 指定构建类型为 Debug 或 Release：
# cmake -DCMAKE_BUILD_TYPE=Release ..
make
# 构建特定的目标：
# make MyExecutable

# 清理构建文件
rm -rf build/*
```

## 实践案例

项目结构：

```bash
MyProject/
├── CMakeLists.txt
├── src/
│   ├── main.cpp
│   └── mylib.cpp
└── include/
    └── mylib.h
```

`CMakeLists.txt`：

```cmake
cmake_minimum_required(VERSION 3.10)   # 指定最低 CMake 版本

project(MyProject VERSION 1.0)          # 定义项目名称和版本

# 设置 C++ 标准为 C++11
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# 添加头文件搜索路径
include_directories(${PROJECT_SOURCE_DIR}/include)

# 添加源文件
add_library(MyLib src/mylib.cpp)        # 创建一个库目标 MyLib
add_executable(MyExecutable src/main.cpp)  # 创建一个可执行文件目标 MyExecutable

# 链接库到可执行文件
target_link_libraries(MyExecutable MyLib)
```

## 参考资料

- [<u>CMake 教程 - 菜鸟教程</u>](https://www.runoob.com/cmake/cmake-tutorial.html)

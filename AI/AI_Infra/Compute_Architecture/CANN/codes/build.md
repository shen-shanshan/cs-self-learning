# 构建命令

```bash
mkdir build
cd build
cmake .. -DCMAKE_CXX_COMPILER=g++ -DCMAKE_SKIP_RPATH=TRUE
make
./opapi_test
```

build 后目录结构（以 add 为例）：

```bash
(base) sss@ascend910b-02:~/src/compute_son/add$ tree
.
|-- CMakeLists.txt
|-- build
|   |-- CMakeCache.txt
|   |-- CMakeFiles
|   |   |-- 3.16.3
|   |   |   |-- CMakeCCompiler.cmake
|   |   |   |-- CMakeCXXCompiler.cmake
|   |   |   |-- CMakeDetermineCompilerABI_C.bin
|   |   |   |-- CMakeDetermineCompilerABI_CXX.bin
|   |   |   |-- CMakeSystem.cmake
|   |   |   |-- CompilerIdC
|   |   |   |   |-- CMakeCCompilerId.c
|   |   |   |   |-- a.out
|   |   |   |   `-- tmp
|   |   |   `-- CompilerIdCXX
|   |   |       |-- CMakeCXXCompilerId.cpp
|   |   |       |-- a.out
|   |   |       `-- tmp
|   |   |-- CMakeDirectoryInformation.cmake
|   |   |-- CMakeOutput.log
|   |   |-- CMakeTmp
|   |   |-- Makefile.cmake
|   |   |-- Makefile2
|   |   |-- TargetDirectories.txt
|   |   |-- cmake.check_cache
|   |   |-- opapi_test.dir
|   |   |   |-- CXX.includecache
|   |   |   |-- DependInfo.cmake
|   |   |   |-- build.make
|   |   |   |-- cmake_clean.cmake
|   |   |   |-- depend.internal
|   |   |   |-- depend.make
|   |   |   |-- flags.make
|   |   |   |-- link.txt
|   |   |   |-- progress.make
|   |   |   `-- test_add.cpp.o
|   |   `-- progress.marks
|   |-- Makefile
|   |-- bin
|   |   `-- opapi_test
|   `-- cmake_install.cmake
`-- test_add.cpp
```

命令说明：

```bash
cmake ..  # 表示在上层文件夹下寻找 CMakeLists 并生成 Makefile 以及相关文件
make  # 执行 Makefile
make install  # 安装（可选）
make clean  # 清理工程
```

> [!NOTE]
>
> 修改代码后，需要删除 build 文件夹中的所有内容，并在其中重新执行 cmake 和 make 后代码更新才会生效。

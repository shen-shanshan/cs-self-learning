# README

## 环境配置

配置国内镜像：

```powershell
# 添加镜像源
conda config --add channels https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/main
conda config --add channels https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/free
conda config --add channels https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/r
conda config --add channels https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/pro
conda config --add channels https://mirrors.tuna.tsinghua.edu.cn/anaconda/pkgs/msys2

# 启动设置好的国内镜像源
conda config --set show_channel_urls yes

# 查看是否添加上了源
conda config --show
```

创建虚拟环境：

```powershell
conda create -n [虚拟环境的名称] python=3.9
```

更新 conda 版本：

```powershell
conda update -n base -c defaults conda

# 把conda更新到最新版
conda update conda
# 把anaconda更新到最新版
conda update anaconda

# 一键更新
conda update --all
```

> 参考资料：[Anaconda如何升级到新版？ - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/121601968)

查看当前所有的虚拟环境：

```powershell
conda info --envs
```

激活虚拟环境：

```powershell
conda activate [环境名]
conda activate deepshare
```

> 默认的虚拟环境：base。

## 启动 Jupyter

切换到指定目录：

```powershell
# 在Windows系统下，切换目录需要先进入指定的盘，使用“盘符:”，盘符不区分大小写，其下的目录都需要区分
e:
cd E:\编程\AI\深度之眼\人工智能0基础训练营\课件\5.numpy
# 查看当前目录下的所有文件
dir /b
```

启动 Jupyter，两种方式都可以：

```powershell
jupyter notebook
jupyter lab
```

## 注意事项

- [python文件头部声明# coding=utf-8_python coding=utf-8-CSDN博客](https://blog.csdn.net/daningliu/article/details/121617391)

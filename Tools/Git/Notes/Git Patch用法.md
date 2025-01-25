# Git Patch 用法

## 概述

patch 就是打补丁，通过 git 工具把代码的差分，生成 patch 文件，然后通过 git 工具可以直接把 patch 文件的内容，merge 到代码里面。

## 常用命令

创建 patch：

```bash
# 本地变更 git diff 的内容，生成 patch 文件
git diff > xxx.patch
git diff <文件名> > xxx.patch
```

应用 patch：

```bash
# 测试是否可以将 patch 完美打入本地代码中
git apply --check xxx.patch
# 将 patch 文件内容 merge 到本地代码中
git apply xxx.patch
# 强制应用 patch
git apply --reject xxx.patch
```

## 参考资料

- [git 生成 patch 和打 patch](https://blog.csdn.net/u013318019/article/details/114860407)
- [How to create and apply a patch with Git Diff and Git Apply commands](https://www.specbee.com/blogs/how-create-and-apply-patch-git-diff-and-git-apply-commands-your-drupal-website)

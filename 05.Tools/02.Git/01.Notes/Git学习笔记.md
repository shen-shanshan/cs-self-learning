# Git 学习笔记

## Git 安装与配置

### Git 安装

[<u>Git 下载地址</u>](https://git-scm.com/download)。
去掉勾选 git-lfs（有问题）。
是否修改环境变量：选 `use git bash only`。
查看 git 版本信息：右键点击 `git bash here`，执行 `git version`。

### Git 基本配置

Git 的配置可以分为：

- 系统配置（system）：对所有用户都适用；
- 用户配置（global）：只适用于该用户；
- 仓库配置（local）：只对当前仓库有效。

查看配置：

```bash
git config --system/global/local [配置名]
```

配置个人身份：

```bash
git config --global user.name "shanshan shen"
git config --global user.email shanshanshen333@gmail.com
```

> 注意：上面的配置会在 git 的提交信息中体现，但与 GitHub 的账号密码无关。

配置文本换行符：

```bash
git config --global core.autocrlf true(windows)/input(mac or linux)
```

配置服务器认证：

https：……

SSH：

1. 生成公钥：`ssh-keygen -t rsa -C [邮箱]`；
2. 添加公钥到 GitHub：`profile settings` -> `SSH Keys` -> `add ssh key` -> `public key`。

## Git 基本命令

### Git 基本概念

工程区域：

- 版本库（repository）：`./git`，存放该工程的所有版本数据；
- 工作区（work directory）：代码文件；
- 暂存区（stage）：`./git/index`。

文件状态：

- 已提交；
- 已修改（未提交）；
- 已暂存（把已修改的文件暂存起来，留待之后进行提交）。

### Git 常用命令

新建项目仓库：

```bash
git init [仓库名称]
```

下载项目：

```bash
git clone [url]

# 完整下载仓库中的二进制文件，使用 lfs 插件
git lfs clone [url]
```

讲文件添加到暂存区（未提交）：

```bash
git add [文件名]

# 将所有新增文件添加到暂存区
git add .
```

> 注意：在较新的 git 版本中，不再需要 add，可以直接提交文件。

将指定文件彻底从当前分支的缓存区删除，之后不再受 git 工程的管理：

```bash
git rm [文件名]
```

> 注意：也可以直接从硬盘上删除该文件，然后执行 git commit。

查看工作目录和暂存区的状态：

```bash
git status
```

将暂存区的文件提交到本地的版本库：

```bash
git commit [文件名] -m "提交信息"

# 一次性提交暂存区中的所有文件
git commit -am "提交信息"
```

查看日志：

```bash
git log
```

推送到远程仓库：

```bash
git push origin [分支名称]
```

查看分支：

```bash
# 查看本地仓库的所有分支
git branch

# 查看远程仓库的所有分支，分支名带 origin 前缀
git branch -r

# 查看本地和远程仓库的所有分支
git branch -a
```

新建分支：

```bash
# 不会切换到新分支
git branch [分支名称]

# 会自动切换到新分支上（签出）
git checkout -b [分支名称]
```

删除分支：

```bash
# 删除本地分支（-D：强制删除）
git branch -d/-D [分支名称]

# 删除远程分支（需要推送远端）
git branch -d -r [分支名称]
git push origin : [分支名称]
# 或：
git push origin --delete [分支名称]
```

切换分支：

```bash
# -f：强制签出
git checkout [-f] [分支名称]
```

拉取远程分支最新内容，并合并到本地分支：

```bash
git pull origin [远程分支名称]:[本地分支名称]

# 若远程分支名称与本地的相同
git pull origin [远程分支名称]
```

拉取远程分支最新内容，但不合并到本地分支：

```bash
git fetch origin [远程分支名称]:[本地分支名称]

# 若远程分支名称与本地的相同
git fetch origin [远程分支名称]
```

合并分支：

```bash
# 将指定分支与当前分支进行合并，从二者最近的一个共同节点 base 开始进行比较
git merge [分支名称]

git rebase [分支名称]
```

回退提交：

```bash
# 回退到某个节点
git reset --mixed/hard/soft commit_id

# 回退本地所有修改但未提交的内容（直接用暂存区的文件覆盖本地文件），谨慎使用
git checkout .
# 只回退某个文件
git checkout -filename
```

## Git 底层原理

### Git 对象

Git 文件版本管理依赖于核心四对象及其相互之间的指向关系：标签（tag）-> 提交（commit）-> 目录树（tree）-> 块（block）。

Git 对象间的关联查找是通过键值对层层实现的。
Git 对象的键是依据其对应工作区的文件内容等信息，通过 SHA-1 等散列函数计算得到的。

Git 为了降低对象文件的存储、传输成本，提供了 GC 机制，将松散对象等文件收纳到包文件中。

### Git 引用

Git 引用（ref）存放的是一个 SHA-1 散列值，指向 Git 对象库中的对象。

在本地仓库的 `./git/refs/` 目录中，有三种不同的命名空间，代表不同的引用：

- `refs/heads/ref`：本地分支；
- `refs/remotes/ref`：代表远程跟踪分支；
- `refs/tags/ref`：标签。

> 打标签：git tag -a tag_name -m msg

## Git 代码平台

### GitHub

- [<u>GitHub 入门文档</u>](https://docs.github.com/zh/get-started)；
- [<u>GitHub Skills</u>](https://skills.github.com/)；
- [<u>GitHub Issues 快速入门</u>](https://docs.github.com/zh/issues/tracking-your-work-with-issues/quickstart)；
- [<u>GitHub Flow</u>](https://docs.github.com/zh/get-started/using-github/github-flow)；
- [<u>GitHub 贡献开源 Git 操作流程</u>](https://github.com/yikun/yikun.github.com/issues/89)。

### License

An open source license protects contributors and users. Open source licenses grant permission for anybody to use, modify, and share licensed software for any purpose.

- [<u>Choose an open source license</u>](https://choosealicense.com/)；
- [<u>License 的种类及其限制</u>](https://choosealicense.com/licenses/)。

### CLA

CLA（Contributor License Agreement）：项目接收贡献者提交的 Pull Request 之前，需要贡献者签署的一份协议，协议只需签署一次，对该贡献者的所有提交都生效。

CLA 可以看做是对开源软件本身采用的开源协议的补充。一般分为公司级和个人级别的 CLA，所谓公司级即某公司代表签署 CLA 后即可代表该公司所有员工都签署了该 CLA，而个人级别 CLA 只代表个人认可该 CLA。

- [<u>开源社区贡献者协议 CLA 介绍</u>](https://jimmysong.io/blog/open-source-cla/)。

## Git 实践经验

### 什么是 upstream？

新增 fork 仓库的上游远程分支：

```bash
git remote add upstream https://github.com/apache/spark.git
```

说明：

The wiki is talking from a forked repo point of view. You have access to pull and push from origin, which will be your fork of the main diaspora repo. To pull in changes from this main repo, you add a remote, "upstream" in your local repo, pointing to this original and pull from it.

So "origin" is a clone of your fork repo, from which you push and pull. "Upstream" is a name for the main repo, from where you pull and keep a clone of your fork updated, but you don't have push access to it.

查看远程仓库原地址：

```bash
git remote -v
```

修改远程仓库原地址：

```bash
git remote set-url origin xxx.git

# 或：
git remote rm origin
git remote add origin xxx.git
```

查看本地分支与远程分支的对应情况：

```bash
git branch -vva
```

After added upstream, we should type `git pull upstream` before create a new branch.

> 需要指定要拉取的分支，否则会出现：
> You asked to pull from the remote 'upstream', but did not specify
a branch. Because this is not the default configured remote
for your current branch, you must specify a branch on the command line.

将 upstream 分支的最新内容拉取到本地分支，正确的操作如下：

```bash
git pull upstream [upstream_branch_name]
```

使用 git alias 快速同步上游代码：

```bash
sync-upstream = !"git fetch upstream;git checkout master;git merge upstream/master;git push origin master"

git sync-upstream
```

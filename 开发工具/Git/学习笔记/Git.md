# 1  概述

## 1.1  大纲

- 常用功能：仓库管理、文件管理、分支管理、标签管理、远程操作；
- 进阶功能：IDE 集成 Git、访问 GitHub、Gitee 等代码托管平台；
- 功能指令：add、commit、log、branch、tag、remote、status。

## 1.2  安装

git 官网：[Git (git-scm.com)](https://git-scm.com/)

# 2  客户端

## 2.1  安装

客户端软件：GUI Clients - GitHub Desktop

## 2.2  基本使用

设置个人信息：

![image-20230414110044783](C:/Users/SSS/AppData/Roaming/Typora/typora-user-images/image-20230414110044783.png)

> 注意：这里的 name 就相当于用户名（账号），email 相当于密码。

创建本地仓库：

![image-20230414110242215](C:/Users/SSS/AppData/Roaming/Typora/typora-user-images/image-20230414110242215.png)

## 2.3  文件操作

创建文件后，git 客户端会识别出文件的变化，提交（commit）后与仓库达成一致。

![image-20230414112236420](Git.assets/image-20230414112236420.png)

修改文件后，git 客户端会识别出文件的变化，提交（commit）后与仓库达成一致。

![image-20230414111617624](Git.assets/image-20230414111617624.png)

版本号：40 个 16 进制的数字组成的版本号（提交码）。

![image-20230414111709387](Git.assets/image-20230414111709387.png)

删除文件后，git 客户端会识别出文件的变化，提交（commit）后与仓库达成一致。

![image-20230414111920645](Git.assets/image-20230414111920645.png)

> git 客户端的比对功能仅能识别文本文件，不能识别如 word 等文件发生的变化。

## 2.4  分支操作

为什么需要分支？便于模块化开发。

![image-20230414113936884](Git.assets/image-20230414113936884.png)

创建分支：

![image-20230414113401778](Git.assets/image-20230414113401778.png)

提交到当前分支：

![image-20230414113757233](Git.assets/image-20230414113757233.png)

合并到主分支：

![image-20230414114057293](Git.assets/image-20230414114057293.png)

![image-20230414114201532](Git.assets/image-20230414114201532.png)

合并分支时文件出现冲突：

![image-20230414114449647](Git.assets/image-20230414114449647.png)

![image-20230414114551711](Git.assets/image-20230414114551711.png)

查看发生冲突的文件：

![image-20230414114705245](Git.assets/image-20230414114705245.png)

> 说明：`HEAD` 为冲突头，即开始发生冲突的地方；`>>> order` 代表下面的修改来自 `order` 分支。

手动解决冲突：

![image-20230414114804650](Git.assets/image-20230414114804650.png)

## 2.5  标签操作

创建标签：

![image-20230414114927371](Git.assets/image-20230414114927371.png)

## 2.6  远程仓库

创建仓库：

![image-20230414115229782](Git.assets/image-20230414115229782.png)

或者：

![image-20230414152841304](Git.assets/image-20230414152841304.png)

![image-20230414152929760](Git.assets/image-20230414152929760.png)

创建文件并提交：

![image-20230414115440229](Git.assets/image-20230414115440229.png)

![image-20230414152309423](Git.assets/image-20230414152309423-16814569903441.png)

创建新的分支：

![image-20230414152455127](Git.assets/image-20230414152455127.png)

![image-20230414152513808](Git.assets/image-20230414152513808.png)

![image-20230414152529634](Git.assets/image-20230414152529634.png)

删除仓库：

![image-20230414152627322](Git.assets/image-20230414152627322.png)

![image-20230414152721149](Git.assets/image-20230414152721149.png)

下载（克隆）仓库到本地（需要先在客户端登录自己的账号）：

![image-20230414153116007](Git.assets/image-20230414153116007.png)

![image-20230414153314284](Git.assets/image-20230414153314284.png)

![image-20230414153328696](Git.assets/image-20230414153328696.png)

提交代码时，客户端设置的 email 应与 GitHub 的账号保持一致。

![image-20230414153746601](Git.assets/image-20230414153746601.png)

这里的提交只是将文件提交到了本地，并未同步到远程仓库。

![image-20230414153910824](Git.assets/image-20230414153910824.png)

将本地文件推送到远程仓库：

![image-20230414154021746](Git.assets/image-20230414154021746.png)

> 国内的远程仓库：https://gitee.com/

## 2.7  Git ignore

忽略某一个文件：

![image-20230414155135672](Git.assets/image-20230414155135672.png)

忽略某一类文件（如下图中的 `.bak` 文件）：

![image-20230414155328279](Git.assets/image-20230414155328279.png)

## 2.8 比对功能

- 删除文件：红；
- 修改文件：黄；
- 新增文件：绿；

![image-20230414155744708](Git.assets/image-20230414155744708.png)

- -1：第一行删除了内容；
- +1, 2：第一行和第二行增加了内容。

![image-20230414155953713](Git.assets/image-20230414155953713.png)

# 3  IDEA 集成 Git

## 3.1  发布项目

创建仓库：

![image-20230414160432326](Git.assets/image-20230414160432326.png)

登录 GitHub 账号（授权）：

![image-20230414160601082](Git.assets/image-20230414160601082.png)

提交文件到本地仓库（Commit）：

![image-20230414161030404](Git.assets/image-20230414161030404.png)

提交文件到本地仓库并上传到远程仓库（Commit and Push）：

![image-20230414161304115](Git.assets/image-20230414161304115.png)

拉取远程仓库的文件到本地（Pull）：

![image-20230414161455507](Git.assets/image-20230414161455507.png)

![image-20230414161511308](Git.assets/image-20230414161511308.png)

## 3.2  下载项目

将远程仓库的项目克隆到本地：

![image-20230414162025862](Git.assets/image-20230414162025862.png)

> 克隆之后会直接创建一个新的项目。

## 3.3  集成 Gitee

需要先安装对应的客户端插件：

![image-20230414162725224](Git.assets/image-20230414162725224.png)

![image-20230414162813376](Git.assets/image-20230414162813376.png)

登录账号：

![image-20230414162834686](Git.assets/image-20230414162834686.png)

下载项目：

![image-20230414163111596](Git.assets/image-20230414163111596.png)

![image-20230414163147656](Git.assets/image-20230414163147656.png)

# 4  版本号

## 4.1  文件操作

一个版本号对应一次提交（commit），使用 SHA-1 进行加密（40 位），可以用于定位仓库中的文件：2（`.git` 文件夹的 `Object` 中的文件夹名）+ 38（文件名）。

根据版本号查看文件：

- 打开 git bash 窗口；
- 查看文件：`git cat-file -p [版本号]`。

## 4.2 分支操作

`.git/refs/heads` 文件夹中保存的是当前仓库的分支信息。每一个分支为一个文件，文件内容为最后一次提交的版本号。

> 注意：不同分支可以指向不同的版本号。

`.git/HEAD` 文件中保存的是当前版本所引用的分支，如下所示。

```
ref: refs/heads/master
```

![image-20230414170006952](Git.assets/image-20230414170006952.png)

# 5  常用命令

## 5.1  概述

![image-20230415094049364](Git.assets/image-20230415094049364.png)

## 5.2  仓库操作

创建仓库：

- 新建文件夹；
- 打开终端窗口：`git bash here`；
- 初始化仓库：`git init`（此时还没有提交任何内容，没有分支信息）。

下载远程仓库到本地：

- 克隆：`git clone [远程仓库URL]`；
- 或者：`git clone [远程仓库URL] [自定义的本地仓库名称]`。

![image-20230415095949275](Git.assets/image-20230415095949275.png)

配置仓库：

- `git config user.name [用户名]`；
- `git config user.email [用户邮箱]`。

或者：

- `git config --global user.name [用户名]`；
- `git config --global user.email [用户邮箱]`。

> `--global`：表示对整个 git 软件中所有的仓库应用该配置。

查看配置（系统文件）：`C/用户/xxx/.gitconfig`。

查看配置（客户端工具）：

![image-20230415101304635](Git.assets/image-20230415101304635.png)

![image-20230415101405618](Git.assets/image-20230415101405618.png)

另一个地方：

![image-20230415101431106](Git.assets/image-20230415101431106.png)

![image-20230415101444119](Git.assets/image-20230415101444119.png)

查看日志：`git log (--oneline)`。

## 5.3  文件操作

![image-20230414170411384](Git.assets/image-20230414170411384.png)

- 查看暂存区的状态：`git status`；

- 添加所有文件（工作区 -> 暂存区）：`git add *`；

- 提交所有文件（暂存区 -> 工作区）：`git commit -m [提交信息]`；

- 恢复文件（误删除已经提交的文件）：`git restore [文件名]`。


> 注意：删除文件是在工作区进行的，由于文件已经提交，存储区里保存有被删除的文件，因此可以直接恢复。若操作顺序为：删除文件 -> add -> commit，则此时文件不能被恢复（该文件在仓库中的最新版本已经被删除了）。
>

重置仓库版本：

```bash
$ git log --oneline
4db7600 (HEAD -> master) ddddd
078f1a7 aaa

$ git reset --hard 078f1a7

$ git log --oneline
4db7600 (HEAD -> master) aaa
```

> 注意：这里的 `git log` 只能查看当前指针所在版本及之前的版本，所以删除的版本是不显示的。使用 `git reflog -v` 命令可以查看到所有的版本信息，包括刚才删除的版本也会显示出来。
>

还原（恢复到某一次提交之前的版本）：

```bash
$ git log --oneline
20eb1ef (HEAD -> master) ddddd
078f1a7 aaa

$ git revert 20eb1ef

$ git log --oneline
d7583ad (HEAD -> master) revrert "ddddd" 
20eb1ef ddddd
078f1a7 aaa
```

> 注意：`git revert` 就相当于将版本（078f1a7）又重新提交了一次，并不会删除之前已经提交过的记录。

## 5.4  分支操作

- 创建分支：`git branch [分支名称]`；

- 查看分支：`git branch -v`；

- 切换分支（签出）：`git checkout [分支名称]`；

- 创建并切换分支：`git checkout -b [分支名称]`；

- 删除分支：`git branch -d [分支名称]`；

- 合并分支：`git merge [被合并的分支名称]`。


> 注意：
>
> - git 中的分支是基于提交的，不能直接创建分支（至少需要有一个分支后才能创建其它的分支）；
> - `*`：表示当前分支；
> - 在合并分支时，若文件之间（同名文件）发生冲突，则需要人工判断进行取舍（内容），并将修改后的（冲突）文件 add 并 commit，此时合并就完成了。

## 5.5  标签操作

- 查看所有标签：`git tag`；

- 创建标签：`git tag [标签名称] [版本号]`；

- 查看某一次提交之前的日志：`git log [标签名称] `。


使用客户端创建标签：

![image-20230415112543946](Git.assets/image-20230415112543946-16815291444361.png)

## 5.6  远程仓库

- 手动配置远程仓库：`.git/config/remote`；
- 创建远程仓库：`git remote [远程仓库名称] [远程仓库URL]`；

- 修改远程仓库名称：`git remote rename [远程仓库名称]`；

- 推送到远程仓库：`git push [远程仓库名称]`。

下图表示可以使用名称 `origin` 来代替其中的 URL。

![image-20230415113048898](Git.assets/image-20230415113048898.png)

> 注意：当 URL 为 SSH 形式时，不能直接向远程仓库进行推送，还需要进行安全认证。
>
> 生成安全证书：`ssh-keygen -t rsa -C [SSH形式的URL]`。
>
> 查看安全认证的公钥（`id_rsa.pub`）：
>
> ![image-20230415113903232](Git.assets/image-20230415113903232.png)
>
> 拷贝到如下位置：
>
> ![image-20230415114146380](Git.assets/image-20230415114146380.png)

# 6  进阶操作

## 6.1  暂存

当您想记录工作目录和索引的当前状态，但又想返回一个干净的工作目录时，请使用 `git stash`。该命令将保存本地修改，并恢复工作目录以匹配头部提交。stash 命令能够将还未 commit 的代码存起来，让你的工作目录变得干净。

> 应用场景：某一天你正在 feature 分支开发新需求，突然产品经理跑过来说线上有 bug，必须马上修复。而此时你的功能开发到一半，于是你急忙想切到 master 分支，然后你就会看到报错。因为当前有文件更改了，需要提交 commit 保持工作区干净才能切分支。由于情况紧急，你只有急忙 commit 上去，commit 信息也随便写了个“暂存代码”，于是该分支提交记录就留了一条黑历史。

使用 stash 暂存当前工作区中还未提交的代码，并在将来进行恢复。

```shell
# 保存当前未commit的代码
git stash

# 保存当前未commit的代码并添加备注
git stash save "备注的内容"

# 列出stash的所有记录
git stash list

# 删除stash的所有记录
git stash clear

# 应用最近一次的stash
git stash apply

# 应用最近一次的stash，随后删除该记录
git stash pop

# 删除最近的一次stash
git stash drop
```

当有多条 stash，可以指定某个 stash 进行恢复。

```shell
# 首先使用stash list列出所有记录
$ git stash list
stash@{0}: WIP on ...
stash@{1}: WIP on ...
stash@{2}: On ...

# 应用第二条记录
$ git stash apply stash@{1}
```

## 6.2  软回溯

软回溯（reset --soft）可以回退你已提交的 commit，并将 commit 的修改内容放回到暂存区，这会使所有已更改的文件变为「要提交的更改」。

一般我们在使用 reset 命令时，`git reset --hard` 会被提及的比较多，它能让 commit 记录强制回溯到某一个节点。而 `git reset --soft` 的作用正如其名，`--soft`（柔软的）除了回溯节点外，还会保留当前的修改内容。

> 应用场景：有时候手滑不小心把不该提交的内容 commit 了，这时想改回来，只能再 commit 一次，又多一条“黑历史”。规范些的团队，一般对于 commit 的内容要求职责明确，颗粒度要细，便于后续出现问题排查。本来属于两块不同功能的修改，一起 commit 上去，这种就属于不规范。这次恰好又手滑了，一次性 commit 上去。软回溯相当于后悔药，给你重新改过的机会。对于上面的场景，就可以再次修改重新提交，保持干净的 commit 记录。

示例：

```shell
# 恢复最近一次commit
git reset --soft HEAD^
```

> 注意：以上说的是还未 push 的 commit。对于已经 push 的 commit，也可以使用该命令，不过再次 push 时，由于远程分支和本地分支有差异，需要使用强制推送 `git push -f` 来覆盖被 reset 的 commit。

在 `reset --soft` 指定 commit 号时，会将该 commit 到最近一次 commit 的所有修改内容全部恢复，而不是只针对该 commit。

> 示例：
>
> 1. commit 记录有 c（距当前时间最近）、b、a（距当前时间最远）；
> 2. reset 到 a；
> 3. 此时的 HEAD 到了 a，并且 b、c 的修改内容也都回到了暂存区。

## 6.3  优选

优选（cherry-pick）可以将已经提交的 commit 复制出新的 commit 并应用到当前的分支中。

> 应用场景：有时候版本的一些优化需求开发到一半，可能其中某一个开发完的需求要临时上，或者某些原因导致待开发的需求卡住了已开发完成的需求上线。这时候就需要把 commit 抽出来，单独处理。或者有时候当前开发分支中的代码记录被污染了，导致当前开发分支合到线上分支有问题，这时就需要拉一条干净的开发分支，再从旧的开发分支中，把 commit 复制到新分支。

示例：

```shell
# cherry-pick单个commit
git cherry-pick [commitHash]

# cherry-pick多个commit
git cherry-pick commit1 commit2

# 多个连续的commit，也可区间复制
git cherry-pick commit1^..commit2
```

在 cherry-pick 多个 commit 时，可能会遇到代码冲突，这时 cherry-pick 会停下来，让用户决定如何继续操作。

- 解决代码冲突，重新提交到暂存区，然后使用 `cherry-pick --continue` 让 cherry-pick 继续进行下去；
- 有时候也可能需要在代码冲突后，放弃或退出流程，使用 `git cherry-pick --abort` 回到操作前的样子；
- 使用 `git cherry-pick --quit` 退出 cherry-pick，不回到操作前的样子，即保留已经成功 cherry-pick 的 commit，并退出 cherry-pick 流程。

## 6.4  撤销

撤销（revert）可以将现有的提交还原，恢复提交的内容，并生成一条还原记录。

> 应用场景：有一天测试突然跟你说，你开发上线的功能有问题，需要马上撤回，否则会影响到系统使用。这时可能会想到用 reset 回退，可是你看了看分支上最新的提交还有其他同事的代码，用 reset 会把这部分代码也撤回了。由于情况紧急，又想不到好方法，还是任性的使用 reset，然后再让同事把他的代码合一遍（同事听到想打人），于是你的技术形象在同事眼里一落千丈。学会 revert 之后，立马就可以拯救这种尴尬的情况。

示例：

```shell
# 假设当前master的提交记录如下：
# c：同事的提交（最近的提交）
# b：自己的提交
# a：……

# revert普通提交
# 撤回自己在b中的修改，同时保留c中的内容
git revert 21dcd937fe555f58841b17466a99118deb489212 # commitHash

# revert合并提交
# -m后面要跟一个parent number标识出"主线"，一般使用1保留主分支代码
git revert -m 1 <commitHash>
```

> 注意：revert 合并提交后，再次合并分支会失效。在 master 分支 revert 合并提交后，然后切到 feature 分支修复好 bug，再合并到 master 分支时，会发现之前被 revert 的修改内容没有重新合并进来。因为使用 revert 后， feature 分支的 commit 还是会保留在 master 分支的记录中，当你再次合并进去时，git 判断有相同的 commitHash，就忽略了相关 commit 修改的内容。这时就需要 revert 掉之前 revert 的合并提交。再次使用 revert，之前被 revert 的修改内容就又回来了。

## 6.5  操作日志

操作日志（reflog）记录了所有的 commit 操作记录，便于错误操作后找回记录。

> 应用场景：某天你眼花，发现自己在其他人分支提交了代码还推到远程分支，这时因为分支只有你的最新提交，就想着使用 `reset --hard`，结果紧张不小心记错了 commitHash，reset 过头，把同事的 commit 搞没了。没办法，`reset --hard` 是强制回退的，找不到 commitHash 了，只能让同事从本地分支再推一次。

示例：

```shell
# 假设当前master的提交记录如下：
# c：自己的错误提交（最近的提交）
# b：同事的提交
# a：……

# 想要reset到b，误操作reset过头，b没了，最新的只剩下a
git reset --hard [a的commitHash]

# 这时用git reflog查看历史记录，把错误提交（c）的那次commitHash记下
git reflog

# 再次reset回去，就会发现b回来了
git reset --hard [c的commitHash]
```

# 7  短命令

## 7.1  方式一

```shell
# 设置短命令
git config --global alias.ps push
```

## 7.2  方式二

```shell
# 打开全局配置文件
vim ~/.gitconfig

# 写入内容
[alias] 
        co = checkout
        ps = push
        pl = pull
        mer = merge --no-ff
        cp = cherry-pick
        
# 使用短命令
git cp <commitHash> # 等同于git cherry-pick <commitHash>
```

# 8  参考资料

- [Git 不要只会 pull 和 push，试试这 5 条提高效率的命令！ (qq.com)](https://mp.weixin.qq.com/s/EUkOnmTMxEqPQA1Uxbcq6w)；
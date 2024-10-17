# Git 常用命令组合

## 前置准备

```bash
git remote add upstream <xxx.git>  # 新增 fork 仓库的上游远程分支
git remote -v  # 查看 remote 和 upstream 的 URL
git branch -vva  # 查看本地和远程的分支情况
```

## 同步上游分支

```bash
git fetch upstream
git checkout <branch>
git merge upstream/<branch>
git push origin <branch>
```

**特殊情况：本地已有多次 commit，且中间穿插着其他人的 commit。**

本地（pull 最新上游分支后）的提交顺序为：

1. **A：最近一次我的提交**；
2. B：其它人的提交；
3. **C：我的提交**；
4. D：其它人的提交；
5. ...

远程仓库的提交顺序与本地相同（本地已经 push origin 过一次）。

上游的提交顺序为：

1. B：其它人的提交；
2. D：其它人的提交；
3. ...

**当前诉求：将我的两次 commit 合并为一次提交，并放到 B 节点之后。**

1. **E：A + C**；
2. B：其它人的提交；
3. D：其它人的提交；
4. ...

可以按如下命令进行操作：

```bash
git reset --soft <commit_id>  # 当前所有修改内容前最近的一个提交节点（D）
git stash  # 暂存 A 和 C 修改的内容
git pull upstream <upstream_branch>:<your_branch>  # 重新同步上游分支
git stash pop  # 恢复 A 和 C 修改的内容
git add .
git commit -am "A + C 的提交信息"
git push origin <branch> --force  # 注意：这里必须加上 --force，否则本地的提交顺序和 origin 的冲突，会要求必须先合并 origin，这样 commit 的顺序就又乱了
```

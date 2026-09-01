> Q：对于 github 上的项目，怎么将一个未 merge 的 PR，apply 到本地的代码中（没有 conflict）？对应的 git 命令是什么？

---

如果 GitHub 上有一个**还没 merge 的 PR**，你想把它应用到本地代码，而且**没有 conflict**，最常用的方法是直接 `git fetch` PR 对应的 ref，然后 `cherry-pick` 或 `merge`。

### 方法一：直接 cherry-pick PR

假设 PR 是：

```text
https://github.com/vllm-project/vllm/pull/48687
```

PR 编号是 `48687`，可以：

```bash
git fetch origin pull/48687/head:pr-48687
git cherry-pick pr-48687
```

这里：

```bash
git fetch origin pull/48687/head:pr-48687
```

会把 GitHub 上 PR 的代码拉到本地一个叫 `pr-48687` 的 branch/ref。

然后：

```bash
git cherry-pick pr-48687
```

把 PR 中的 commit 应用到你当前所在的 branch。

如果 PR 有多个 commit，`cherry-pick` 会把这些 commit 都应用过来。

---

### 方法二：直接把 PR merge 到当前分支

如果你希望保留 PR 的完整 commit history，并且不介意产生一个 merge commit：

```bash
git fetch origin pull/48687/head
git merge FETCH_HEAD
```

也可以一条命令：

```bash
git pull origin pull/48687/head
```

不过我更推荐：

```bash
git fetch origin pull/48687/head
git merge FETCH_HEAD
```

因为每一步发生了什么更清楚。

---

### 方法三：只想测试 PR，不想真正 merge

如果你的目的只是：

> “我想把这个 PR 的代码放到本地看看/跑 benchmark/profile，但不想修改我的当前 branch history。”

可以：

```bash
git fetch origin pull/48687/head:pr-48687
git switch pr-48687
```

这样你就直接进入 PR 的代码了。

测试完之后：

```bash
git switch your-branch
```

即可。

---

### 如果你的目的就是“把 PR 应用到当前本地代码”

我最推荐这个：

```bash
git fetch origin pull/48687/head:pr-48687
git cherry-pick pr-48687
```

例如你现在在：

```text
main
```

然后：

```bash
git fetch origin pull/48687/head:pr-48687
git cherry-pick pr-48687
```

最终：

```text
main
  │
  ├── A
  ├── B
  ├── C
  │
  ├── PR commit 1
  ├── PR commit 2
  └── PR commit 3
```

如果 PR 本身是基于 `main` 的，而且你本地 `main` 和 PR base 没有冲突，那么通常会非常顺利。

**另外一个很实用的 GitHub 命令是：**

```bash
gh pr checkout 48687
```

如果你安装了 GitHub CLI，它可以直接把 PR checkout 到本地。但这个操作更偏向于“切换到 PR 分支”，而不是“把 PR 应用到我当前 branch”。

所以如果你现在正在做 **vLLM PR 的性能测试/代码验证**，通常我会建议使用：

```bash
git fetch origin pull/<PR号>/head:pr-<PR号>
git cherry-pick pr-<PR号>
```

这样最可控。

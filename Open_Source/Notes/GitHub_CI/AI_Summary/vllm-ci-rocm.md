**我的 leader 叫我维护 vLLM 中 AMD ROCm 相关的 CI（https://buildkite.com/vllm/ci/builds?branch=https%3A%2F%2Fgithub.com%2FROCm%2Fvllm），以确保它们的稳定运行，如果有相关的 CI 出问题了，直接下架即可。我应该怎么阅读并分析这个 ROCm 的 buildkite 界面？如果某个 CI 出问题了，我应该怎么下架它，要去哪个仓库修改哪一个文件的代码？**

---

结合你的岗位职责来看，你现在的角色已经不是普通的 vLLM Contributor，而更像是 **ROCm CI Maintainer**。你的工作目标其实可以概括成一句话：

> **保证 AMD ROCm 分支的 Buildkite CI 始终是“绿色且可信”的。如果某个测试长期不稳定（flaky）或已经失去价值，就及时把它从 CI 中移除（下架），避免影响开发效率。**

下面我按实际工作流程介绍。

---

# 一、首先理解你看到的 Buildkite 页面

你给的链接实际上不是 vLLM 的 `main` CI，而是：

```
branch=https://github.com/ROCm/vllm
```

说明这里只展示 **ROCm fork** 的 CI。

整个页面每一行其实都是：

```
ROCm/vllm

↓

一次 commit

↓

一次 Build
```

例如

```
Build #53281

branch:
https://github.com/ROCm/vllm

commit:
abcdef1

status:
Passed
```

意味着：

```
ROCm/vllm 某个 commit

↓

触发 Buildkite

↓

执行 AMD GPU CI
```

因此你的主要关注对象不是 NVIDIA 的 CUDA 测试，而是：

* ROCm Queue
* MI300
* MI250
* gfx90a
* gfx942
* ROCm Docker
* ROCm pytest

---

# 二、每天应该怎么看这个页面？

我建议形成一个固定流程。

## Step 1：看有没有红色 Build

第一页先不要点日志。

先回答两个问题：

```
最近一天

有没有 Failed？

有没有 Cancelled？
```

如果都是绿色：

说明今天没什么事。

---

## Step 2：点进去看失败的是哪个 Step

例如：

```
✓ Lint

✓ Unit

✗ ROCm Models

✓ CPU
```

你真正关心的是：

```
ROCm Models
```

而不是：

```
Lint
```

因为你的职责就是 ROCm CI。

---

## Step 3：判断失败类型

一般分四类。

### 第一类：代码 Bug

例如

```
AssertionError

RuntimeError

HIP kernel error
```

这是：

```
代码问题
```

应该找对应开发者。

---

### 第二类：Infrastructure

例如：

```
Agent Lost

Docker pull timeout

No GPU

Queue timeout
```

这是：

```
CI Infrastructure
```

一般不用下架。

---

### 第三类：Flaky Test

例如：

```
昨天 PASS

今天 FAIL

明天 PASS
```

这种：

```
偶现失败
```

就是 CI Maintainer 最头疼的。

---

### 第四类：长期失败

例如：

最近：

```
Build 53101 FAIL

53125 FAIL

53150 FAIL

53183 FAIL
```

连续一个星期都失败。

这种基本可以认为：

```
CI 已经坏了
```

如果没人修：

> 可以直接 Disable。

vLLM 官方也建议在排查 CI 时，先确认失败是否已经在 `main` 或最近的 Build 中持续存在，再决定是修复还是暂时屏蔽。([vllm.website.cncfstack.com][1])

---

# 三、怎样判断应该"下架"？

一般满足下面三个条件。

## 条件一

失败率非常高。

例如：

```
80%
```

---

## 条件二

不是 Infrastructure。

例如：

```
GPU 不够

×

Docker Registry 挂了

×

Agent Lost
```

这些不能下架。

---

## 条件三

近期没人修。

GitHub Issue：

```
Open

两个月没人管
```

Leader 一般会说：

```
Disable
```

---

# 四、所谓"下架"到底是什么意思？

很多新人会误以为：

```
删掉 pytest
```

实际上不是。

真正做的是：

> **不要把这个 Job 上传到 Buildkite。**

例如：

原来：

```yaml
steps:

- label: ROCm Llama

- label: ROCm Gemma

- label: ROCm Mistral
```

现在：

```yaml
steps:

- label: ROCm Gemma

- label: ROCm Mistral
```

Llama Job 消失。

Buildkite 根本不会运行它。

---

# 五、应该改哪个仓库？

**几乎一定先改 `ci-infra`。**

因为：

```
Pipeline

↓

ci-infra 生成
```

不是：

```
vLLM
```

生成。

README 也明确说明，Buildkite 的 bootstrap 和 Pipeline 模板都位于 `ci-infra/buildkite/`，负责动态生成并上传整个 CI Pipeline。([Gitea: Git with a cup of tea][2])

---

# 六、通常改哪些文件？

虽然不同分支可能略有不同，但 ROCm CI 基本都会在：

```
ci-infra/

buildkite/
```

里面。

重点看：

```
buildkite/

    bootstrap.sh

    test-template-ci.j2

    pipeline_generator/

    tests/
```

其中：

## bootstrap.sh

负责：

```
哪些测试要上传
```

---

## test-template-ci.j2

负责：

```
生成每一个 Step
```

例如：

```
ROCm

↓

queue=gfx942
```

都是这里定义。

---

## pipeline generator

新的版本越来越多逻辑放这里。

例如：

```
Python

↓

生成 YAML

↓

upload
```

---

# 七、如果我要 Disable 一个 ROCm Test，通常怎么做？

一般有三种方式。

## 方法一：删掉 Step（最常见）

例如：

```yaml
- label: ROCm FP8 Test
```

直接删。

以后：

```
Buildkite

↓

没有这个 Job
```

---

## 方法二：加条件

例如：

```yaml
if: false
```

或者：

```
enabled=False
```

具体取决于 Pipeline Generator 的实现。

---

## 方法三：不要生成它

例如：

Generator：

```python
if test.disabled:
    continue
```

以后：

```
YAML

↓

没有这一项
```

这是大型项目最常见的方法。

---

# 八、建议你建立一套 ROCm CI 排查 Checklist

以后每天值班时，基本按下面流程即可：

| 步骤 | 看什么                  | 如果异常怎么办                             |
| -- | -------------------- | ----------------------------------- |
| ①  | 最近 24 小时 Build 是否有红色 | 打开失败 Build                          |
| ②  | 是哪个 Step 失败          | 判断是否属于 ROCm Queue                   |
| ③  | 查看 Console Log       | 区分代码问题、基础设施问题还是 Flaky               |
| ④  | 查看历史                 | 最近 5–10 次是否一直失败                     |
| ⑤  | 查看 GitHub Issue      | 是否已有人跟进                             |
| ⑥  | 与 Leader 确认          | 若长期失败且无人维护，则在 `ci-infra` 中禁用对应 Step |

---

## 我建议你重点阅读的代码（按优先级）

如果你的职责是维护 **AMD ROCm Buildkite CI**，我建议阅读顺序如下：

1. **`ci-infra/buildkite/bootstrap.sh`**（最高优先级）

   * CI 如何接收 GitHub Webhook。
   * 如何识别当前是 `ROCm/vllm` 分支还是 `vllm-project/vllm`。
   * 如何决定上传哪些 Pipeline。

2. **Pipeline Generator（或 `test-template-ci.j2`）**

   * 每一个 ROCm Job 是如何生成的。
   * 每个 Job 对应哪个 Queue（例如 MI250、MI300）。
   * 修改这里通常就是“下架”某个 CI 的入口。([Gitea: Git with a cup of tea][2])

3. **ROCm Queue 和测试映射**

   * 搞清楚每个 Buildkite Step 实际运行的是哪个 `pytest`、使用哪个 Docker 镜像、在哪类 AMD GPU 上执行。
   * 最好整理成一张映射表，例如：

| Buildkite Step   | Queue    | GPU    | 执行命令                           |
| ---------------- | -------- | ------ | ------------------------------ |
| ROCm Unit        | `mi300`  | MI300X | `pytest tests/...`             |
| ROCm Models      | `gfx942` | MI300X | `pytest tests/models/...`      |
| ROCm Distributed | `mi250`  | MI250X | `pytest tests/distributed/...` |

有了这张表，以后某个 ROCm CI 红了，你基本可以在几分钟内判断：**是代码问题、机器问题，还是应该直接从 `ci-infra` 中暂时下架对应的 Pipeline Step。**

[1]: https://vllm.website.cncfstack.com/contributing/ci/failures.html?utm_source=chatgpt.com "CI Failures - vLLM"
[2]: https://gitea.cncfstack.com/vLLM/ci-infra/src/branch/amd_blocking_test_collection?utm_source=chatgpt.com "vLLM/ci-infra: This repo hosts code for vLLM CI & Performance Benchmark infrastructure. - ci-infra - Gitea: Git with a cup of tea"

---

**那我每天应该监控的面板是 https://buildkite.com/vllm/ci/builds?branch=https%3A%2F%2Fgithub.com%2FROCm%2Fvllm 还是 https://buildkite.com/vllm/ci/builds?branch=main ？这两个分支有什么关联和区别？**

---

如果你的 Leader 说的是：

> **维护 vLLM 中 AMD ROCm 相关的 CI，确保稳定运行；如果某个 ROCm CI 坏了，就直接下架。**

那么**你每天主要应该监控的是 ROCm fork 的 Build，而不是 `main`。**

也就是说：

* **日常值班面板：**`branch=https://github.com/ROCm/vllm` ✅
* **辅助对照面板：**`branch=main` ✅（用于判断问题是否是 ROCm 独有）

这两个页面不是二选一，而是**主从关系**。

---

# 先理解为什么会有两个 branch

实际上这里对应的是两条不同的开发线。

```text
                 vllm-project/vllm
                       │
                upstream main
                       │
        ───────────────┼──────────────
                       │
                定期同步(cherry-pick / merge)
                       │
                       ▼
                 ROCm/vllm
              (AMD fork / downstream)
```

也就是说：

**main**

表示：

```text
官方 vLLM 主仓
```

而：

**ROCm/vllm**

表示：

```text
AMD 自己维护的 fork
```

ROCm fork 通常会包含一些尚未进入 upstream 的内容，例如：

* 新 ROCm Kernel
* HIP Backend
* AITER
* 新 GPU（MI300、MI350 等）
* AMD 专有优化

因此它不一定和 upstream `main` 完全一致，AMD 会定期同步 upstream 的代码，再叠加自己的修改。([Factory][1])

---

# 两个 Buildkite 页面分别代表什么？

## ① main

```text
branch = main
```

表示：

```text
vllm-project/vllm

↓

官方 CI
```

主要回答：

> **官方代码有没有坏？**

例如：

```
PR merge

↓

main Build

↓

所有平台
```

包括：

* CUDA
* CPU
* ROCm（如果配置了）
* Docs
* Release

---

## ② ROCm/vllm

你给的链接：

```
branch=https://github.com/ROCm/vllm
```

实际上表示：

```text
AMD fork

↓

自己的 Build

↓

ROCm CI
```

回答的是：

> **AMD 自己维护的 fork 是否稳定？**

这是你 Leader 更关心的。

---

# 为什么 AMD 要单独跑一套？

因为：

AMD fork 往往：

```
比 upstream 多：

HIP patch

Kernel patch

AITER

ROCm Feature
```

这些：

**main 根本没有。**

例如：

```
main

↓

PASS

↓

ROCm fork

↓

FAIL
```

完全可能。

原因：

AMD 新加了一段：

```
HIP Graph

↓

Bug
```

只有 ROCm fork 有。

所以：

main 永远不会失败。

---

反过来也可能：

```
main

↓

FAIL

↓

ROCm fork

↓

PASS
```

例如：

官方刚 merge 一个：

```
CUDA 改动
```

AMD fork 还没同步。

因此：

ROCm fork 没受影响。

---

# 那每天到底看哪个？

我建议按下面这个优先级。

## 第一优先级（你的工作）

每天：

```
ROCm/vllm Build

★★★★★
```

这里应该重点关注：

* 有没有新的 Failed Build
* 哪个 ROCm Step 红了
* 是不是连续失败
* 是不是 flaky

这是你的值班面板。

---

## 第二优先级

如果：

```
ROCm Build

FAIL
```

下一步：

去看：

```
main
```

目的只有一个：

> **判断是不是 upstream 本来就坏了。**

这是 CI Maintainer 最常见的分析方式。

---

举个例子。

今天：

```
ROCm Build

FAIL

↓

test_llama.py
```

你马上：

打开：

```
main
```

发现：

```
同一个 test

FAIL
```

说明：

不是 AMD。

而是：

```
upstream 已经坏了
```

这种：

一般：

**不用下架 ROCm CI。**

应该：

等 upstream 修。

---

另一种情况：

```
ROCm

FAIL

↓

main

PASS
```

说明：

问题：

```
AMD 独有
```

例如：

```
HIP

AITER

ROCm kernel
```

这就是你应该处理的。

---

# 什么时候需要下架？

你的 Leader 说：

> 如果有相关 CI 出问题了，直接下架即可。

这里通常隐含的是：

> **ROCm 特有 CI。**

例如：

```
ROCm FlashAttention

FAIL

main 没有

↓

Disable
```

或者：

```
MI300 FP8

FAIL

连续两周

↓

Disable
```

但是：

如果：

```
Python Unit Test

FAIL

main 也 FAIL
```

那不是：

ROCm CI 的问题。

不应该你去 Disable。

---

# 我建议你每天采用的工作流

这是我觉得最适合 ROCm CI Maintainer 的流程：

```text
每天早上

↓

① 打开 ROCm/vllm Build 页面

↓

有没有 Failed？

↓

没有
↓

结束

↓

有

↓

② 点进去

↓

哪个 Step 红？

↓

③ 看日志

↓

Infrastructure？

↓

通知 CI Owner

↓

代码问题？

↓

通知开发者

↓

ROCm 特有？

↓

④ 打开 main

↓

main 是否也 FAIL？

↓

YES
↓

upstream 问题

↓

等待修复

↓

NO

↓

ROCm 独有

↓

⑤ 查看最近 10 次

↓

一直 FAIL？

↓

YES

↓

Disable Pipeline
```

---

# 从维护者的角度，我建议把两个页面分工明确

| 页面                                        | 建议查看频率       | 用途                                              |
| ----------------------------------------- | ------------ | ----------------------------------------------- |
| **`branch=https://github.com/ROCm/vllm`** | ⭐⭐⭐⭐⭐ 每天持续关注 | **你的主监控面板**，负责发现 ROCm CI 是否稳定、是否需要下架某个 ROCm 测试。 |
| **`branch=main`**                         | ⭐⭐⭐ 按需查看     | **对照组**，用于判断失败是 upstream 共性问题还是 AMD 独有问题。       |

所以，如果我是 AMD ROCm CI Maintainer，我浏览器里会长期固定打开两个标签页：

* **标签页 1：ROCm/vllm** —— 日常监控、处理告警、决定是否禁用某个 ROCm Step。
* **标签页 2：main** —— 出现失败时快速对照，判断责任归属，避免把 upstream 已知问题误判成 ROCm 回归。

这种工作方式既能聚焦你的职责范围，又能快速判断问题来源，是维护下游 fork CI 最常见的分析思路。

[1]: https://factory.ai/open-source-wikis/vllm?page=how-to-contribute%2Ftooling.md&utm_source=chatgpt.com "Tooling – vLLM wiki | Factory"

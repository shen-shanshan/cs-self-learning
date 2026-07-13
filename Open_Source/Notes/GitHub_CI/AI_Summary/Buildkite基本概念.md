**请介绍下 github 上 buildkite 工具的原理和用法，以及 https://buildkite.com/vllm/ci/builds 这个界面是怎么看的？**

---

如果你经常给 **vLLM** 提 PR，那么 **Buildkite** 基本就是你每天都会打交道的 CI 平台。虽然它和 GitHub Actions 都属于 CI/CD 工具，但它们的设计理念其实很不一样。

下面我结合 **vLLM 的 Buildkite** 来介绍它的原理和你平时看到的页面。

---

# 一、Buildkite 是什么？

一句话来说：

> **Buildkite 负责"调度（Orchestrator）"，真正跑 CI 的机器由项目自己提供。**

它不像 GitHub Actions 那样，GitHub 给你提供 Runner。

Buildkite 分成两部分：

```
GitHub
   │
   │ webhook
   ▼
Buildkite Server (SaaS)
   │
   │ 分发 Job
   ▼
Buildkite Agent
   │
   ▼
你的机器 / AWS / GCP / Kubernetes
```

因此：

* GitHub 负责代码
* Buildkite 负责调度
* Agent 负责真正执行命令

官方将一个 CI 工作流定义为 **Pipeline**，当 Pipeline 被触发时会创建一个 **Build**，Build 中包含多个 **Job（Step）**，这些 Job 会被分发给可用的 Agent 执行。([Buildkite][1])

---

# 二、为什么很多大项目喜欢 Buildkite？

例如：

* vLLM
* TensorFlow
* Reddit
* Shopify
* Canva

原因主要有：

## 1. 可以自己控制机器

GitHub Actions：

```
GitHub Runner
```

Buildkite：

```
自己的 GPU 集群
```

对于 vLLM 来说，需要：

* H100
* A100
* MI300
* ROCm
* CUDA12
* 多节点

GitHub Actions 根本没有这些机器。

所以：

```
Buildkite
        ↓
GPU Queue
        ↓
H100机器
```

非常适合 AI 项目。

---

## 2. Agent 可以无限扩容

例如：

```
100 个 PR

↓

Buildkite

↓

100 台机器
```

所有 Job 可以同时跑。

---

## 3. Pipeline 非常灵活

可以动态生成。

例如：

```
Python3.10

↓

上传新的 Pipeline

↓

再生成

GPU Tests

CPU Tests

ROCm Tests
```

这种 Dynamic Pipeline 是 Buildkite 的特色之一。([Buildkite][2])

---

# 三、Buildkite 的几个核心概念

这是最重要的。

```
Pipeline
    │
    ▼
Build
    │
    ▼
Step
    │
    ▼
Job
    │
    ▼
Agent
```

例如：

```
Pipeline

CI

↓

Build #12563

↓

Lint

↓

Unit Test

↓

GPU Test

↓

Deploy
```

其中：

Pipeline：

```
整个 CI 模板
```

Build：

```
一次 CI 执行
```

例如：

```
#15231
```

Step：

```
一个阶段
```

例如：

```
Lint

Formatting

GPU Tests

CPU Tests
```

Job：

真正运行的任务。

Agent：

真正跑命令的机器。

---

# 四、Buildkite Pipeline 怎么写？

一般仓库会有：

```
.buildkite/

pipeline.yml
```

例如：

```yaml
steps:
  - label: Lint
    command:
      - ruff check .

  - label: Tests
    command:
      - pytest
```

Buildkite Agent 会执行：

```
ruff check .

pytest
```

Step 之间还可以配置依赖关系、并行度、条件执行等。([Buildkite][1])

---

# 五、vLLM 的 Buildkite 是怎么工作的？

以你提交 PR 为例。

```
GitHub PR

↓

Webhook

↓

Buildkite

↓

创建 Build

↓

生成很多 Step

↓

分发 Agent

↓

回写 GitHub Status
```

所以 GitHub 上你会看到：

```
buildkite/ci/pr

✓

或者

×

```

实际上就是 Buildkite 回写的状态。

---

# 六、你给的页面怎么看？

你的链接：

> [https://buildkite.com/vllm/ci/builds](https://buildkite.com/vllm/ci/builds)

这是：

> **vLLM organization**
>
> **Pipeline = ci**
>
> **Build 列表**

里面每一行：

```
Build #53210
```

其实对应：

```
一次 PR CI
```

或者：

```
一次 main 分支 CI
```

---

一般每一列是什么意思？

虽然 Buildkite UI 会不断迭代，但核心信息基本一致。

## （1）Build Number

例如：

```
#53218
```

表示：

```
第53218次 Build
```

不是 GitHub PR 编号。

---

## （2）Commit

例如：

```
8af5d32
```

对应：

```
Git commit SHA
```

---

## （3）Branch

例如：

```
main

feature/xxx
```

如果是 PR：

```
refs/pull/43586/head
```

或者显示：

```
PR #43586
```

---

## （4）Author

谁提交的。

例如：

```
you
```

或者：

```
dependabot
```

---

## （5）Message

Git Commit Message。

例如：

```
Support CUDA Graph
```

---

## （6）State

最重要。

一般有：

```
Running
```

正在跑。

```
Passed
```

全部成功。

```
Failed
```

失败。

```
Canceled
```

取消。

```
Blocked
```

等待人工。

```
Waiting
```

等待 Agent。

---

## （7）Duration

例如：

```
25m
```

CI 花费时间。

---

# 七、点进一个 Build 会看到什么？

例如：

```
Build #53218
```

进去以后。

你会看到很多方块：

```
Lint

Formatting

CPU

CUDA

ROCm

Docs

Integration
```

其实每一个都是：

```
Step
```

绿色：

```
✓
```

红色：

```
×
```

黄色：

```
Running
```

灰色：

```
Waiting
```

---

例如：

```
Lint

↓

Passed
```

点进去：

```
Console Log
```

里面就是：

```
ruff check

....

```

---

GPU Test：

```
pytest

...

PASSED
```

都在 Console Log。

---

# 八、为什么有时候 CI 一直 Waiting？

你之前就问过：

> buildkite/ci/pr
>
> Expected — Waiting for status to be reported

这通常意味着 **GitHub 已经知道应该有一个 Buildkite 状态，但 Buildkite 还没有真正开始执行或还没有回写状态**。常见原因包括：

```
GitHub

↓

Webhook

↓

Buildkite

↓

等待 GPU Queue

↓

Agent 空闲

↓

开始执行
```

如果：

GPU Queue 满了：

```
Waiting
```

如果：

Agent 坏了：

```
Waiting
```

如果：

Pipeline 配置异常：

```
Waiting
```

也可能是 Buildkite 与 GitHub 的状态同步尚未完成。Buildkite 将 Build 分发给 Agent 后，才会逐步更新 GitHub 的检查状态。([Buildkite][3])

---

# 九、怎么看 CI 卡在哪？

进入 Build。

看：

```
Lint        ✓

CPU         ✓

CUDA        Running

ROCm        Waiting

Docs        Waiting
```

说明：

```
CUDA还没结束
```

或者：

```
ROCm没有机器
```

如果：

```
全部 Waiting
```

通常说明：

```
Agent 不够
```

如果：

```
一个 Step 红了

×

```

点进去：

```
Console
```

查看日志即可。

---

# 十、结合 vLLM，你最常见的流程

你平时开发 vLLM 时，整个流程基本就是：

```text
git push

        │
        ▼
GitHub PR

        │
        ▼
Webhook

        │
        ▼
Buildkite

        │
        ▼
Create Build (#53xxx)

        │
        ▼
Upload Pipeline

        │
        ▼
生成几十个 Steps

        │
        ▼
不同 Agent
(H100 / A100 / MI300 / CPU)

        │
        ▼
pytest
ruff
mypy
integration
...

        │
        ▼
所有 Step 结束

        │
        ▼
Build Success

        │
        ▼
GitHub 显示
✓ buildkite/ci/pr
```

---

## 对 vLLM 开发者特别有用的几个技巧

如果你经常给 vLLM 提 PR，建议重点关注以下几点：

* **先看 Pipeline Overview**：一眼就能判断是哪个 Step 失败，而不是直接翻几万行日志。
* **善用搜索日志**：进入失败的 Step 后，用浏览器搜索 `ERROR`、`FAILED`、`Traceback`、`AssertionError`，通常能快速定位问题。
* **区分代码问题和基础设施问题**：如果日志里出现 GPU 不可用、Agent 超时、网络拉取镜像失败等，往往是 CI 环境问题，而不是你的代码。
* **留意 Queue 状态**：对于 GPU 项目，`Waiting` 很多时候只是队列中没有空闲 GPU，并不意味着 CI 已经失败。

结合你之前提到的 `buildkite/ci/pr` 长时间等待、CUDA Graph 测试以及 vLLM 的开发经历，熟悉 Buildkite 的 Pipeline、Build、Step、Job 和 Agent 这几个概念后，阅读 CI 页面和定位失败原因会容易得多。

[1]: https://buildkite.com/docs/pipelines/getting-started?utm_source=chatgpt.com "Getting started with Pipelines | Buildkite Documentation"
[2]: https://buildkite.com/docs/pipelines/configure?utm_source=chatgpt.com "Pipeline configuration overview | Buildkite Documentation"
[3]: https://buildkite.com/docs/apis/rest-api/builds?utm_source=chatgpt.com "Builds API | Buildkite Documentation"

**https://github.com/vllm-project/ci-infra 这个项目是做什么的？它和 vLLM 主仓的 CI 代码有什么区别和关联？**

---

这是一个很多 vLLM 新贡献者都会疑惑的问题。

**一句话总结：**

> **vLLM 主仓负责"被测试的代码（What to test）"，而 `ci-infra` 仓库负责"CI 基础设施（How to test & Where to test）"。** ([Gitea: Git with a cup of tea][1])

可以把它们理解成下面这种关系：

```text
                    GitHub PR
                        │
                        ▼
              Buildkite Webhook
                        │
                        ▼
      ┌─────────────────────────────────┐
      │        ci-infra 仓库            │
      │                                 │
      │ bootstrap.sh                    │
      │ pipeline generator              │
      │ Buildkite YAML                  │
      │ Terraform                       │
      │ Docker                          │
      └─────────────────────────────────┘
                        │
                生成 Build Pipeline
                        │
                        ▼
      ┌─────────────────────────────────┐
      │          vLLM 主仓              │
      │                                 │
      │ Python/CUDA/Rust                │
      │ pytest                          │
      │ unit tests                      │
      │ integration tests               │
      └─────────────────────────────────┘
```

---

# 一、vLLM 主仓负责什么？

你平时开发的就是这个仓库。

里面包括：

* Python 源码
* CUDA Kernel
* ROCm Kernel
* Rust
* tests/
* docs/

例如：

```text
vllm/
tests/
examples/
benchmarks/
```

你的 PR：

> [https://github.com/vllm-project/vllm/pull/43586](https://github.com/vllm-project/vllm/pull/43586)

修改的就是这里。

CI 真正执行的大部分命令，例如：

```bash
pytest tests/...

ruff check

mypy
```

都是针对这个仓库运行的。([GitHub][2])

---

# 二、ci-infra 仓库负责什么？

它不是 vLLM 本身。

而是：

> **整个 Buildkite CI 平台的配置仓库。**

README 第一行就说明了：

> This repository contains the infrastructure and bootstrap code for the vLLM continuous integration pipeline using Buildkite. ([Gitea: Git with a cup of tea][1])

它里面主要有几类内容。

---

## 第一类：Buildkite Pipeline Generator（最重要）

例如：

```text
buildkite/
```

里面包括：

```text
bootstrap.sh

test-template-ci.j2

pipeline generator
```

它们负责：

**把一次 GitHub PR 转换成真正的 Buildkite Pipeline。**

流程如下：

```text
GitHub PR

↓

bootstrap.sh

↓

读取 test list

↓

生成 pipeline.yml

↓

buildkite-agent pipeline upload
```

注意：

**最终 Buildkite 运行的 YAML 并不是手写好的，而是动态生成的。** ([Gitea: Git with a cup of tea][1])

---

## 第二类：Buildkite Bootstrap

这是 CI 最先执行的脚本。

例如：

```bash
bootstrap.sh
```

作用包括：

* 下载 vLLM 代码
* 判断是不是 PR
* 判断是不是 nightly
* 判断 branch
* 找出需要跑哪些测试
* 生成 Pipeline
* 上传给 Buildkite

因此：

```text
bootstrap.sh

↓

不是测试

↓

而是在"生成测试"
```

---

## 第三类：Terraform

例如：

```text
terraform/

    aws/

    gcp/
```

这部分很多开发者平时不会接触。

它负责：

创建整个 CI 集群。

例如：

AWS：

```text
EC2

↓

安装 Buildkite Agent

↓

加入 Queue
```

或者：

```text
Auto Scaling Group
```

再例如：

GCP：

```text
TPU

↓

Buildkite Agent
```

README 中明确提到，该仓库维护 AWS Buildkite Elastic CI Stack、GCP TPU 节点以及相关基础设施代码。([Gitea: Git with a cup of tea][1])

---

## 第四类：Docker

CI 用的镜像。

例如：

```text
cuda image

rocm image

test image
```

这些镜像会被：

```text
Buildkite Agent

↓

docker run

↓

pytest
```

使用。

---

# 三、为什么要单独拆一个仓库？

这是很多大型项目都会采用的做法。

如果所有 CI 配置都放进 vLLM：

```text
vllm/

    code

    terraform

    buildkite

    aws

    docker

    scripts
```

会非常混乱。

于是拆出来：

```text
vllm
```

负责：

```text
模型代码
```

而：

```text
ci-infra
```

负责：

```text
CI 运维
```

这样：

CI 团队可以独立修改：

* Agent
* Queue
* AWS
* Pipeline

而不用改 vLLM。

---

# 四、CI 运行时两个仓库如何配合？

这是最关键的一点。

假设你提交：

```text
PR #43586
```

整个过程实际上是：

```text
GitHub

↓

Webhook

↓

Buildkite

↓

ci-infra/bootstrap.sh

↓

clone vLLM

↓

checkout PR

↓

读取 tests

↓

生成 pipeline

↓

Upload Pipeline

↓

Buildkite Agent

↓

pytest
```

注意：

**ci-infra 并不会测试自己。**

它只是：

> **负责 orchestrate（编排）整个测试流程。**

真正测试的是：

```text
git clone vllm
```

得到的代码。

---

# 五、为什么修改 CI 时，有时需要改两个仓库？

举个你比较容易遇到的例子。

假设你新增：

```text
tests/models/test_new_feature.py
```

如果：

只是新增普通 pytest：

可能：

**只改 vLLM 就够了。**

---

但是如果：

需要：

* 新 GPU Queue
* 新 ROCm Queue
* Nightly Only
* Blackwell Queue
* TPU Queue

那么：

vLLM：

```text
新增测试
```

同时：

ci-infra：

```text
修改 bootstrap

修改 pipeline

修改 queue
```

否则：

Buildkite 根本不知道：

> 这个测试应该去哪台机器跑。

---

# 六、你之前遇到的 "Waiting for status" 和 ci-infra 有关系吗？

有，而且关系很大。

例如：

```text
Expected

Waiting for status to be reported
```

很多情况下并不是：

```text
pytest
```

的问题。

而是：

```text
GitHub

↓

Webhook

↓

Buildkite

↓

bootstrap.sh

↓

Pipeline Upload

↓

Agent Queue
```

中某一步没有完成。

例如：

* webhook 没收到
* bootstrap 崩了
* pipeline upload 失败
* queue 没有 Agent
* Terraform 创建的机器都下线了

这些都属于 **ci-infra** 管理的范畴，而不是 vLLM 代码本身。

---

# 七、作为 vLLM 开发者，什么时候需要关注 ci-infra？

对于像你这样经常给 vLLM 提 PR 的开发者，可以把关注点分成两层：

| 场景                                  | 看 vLLM 主仓 | 看 ci-infra                                   |
| ----------------------------------- | --------- | -------------------------------------------- |
| 修改模型实现（如 CUDA Graph、Vision Encoder） | ✅         | ❌                                            |
| 新增 pytest                           | ✅         | 一般不需要                                        |
| 修改测试矩阵                              | ❌         | ✅                                            |
| 新增 GPU 队列（如 MI300、H100）             | ❌         | ✅                                            |
| CI 一直 `Waiting`                     | 有时        | **优先排查**                                     |
| Buildkite Pipeline 为什么生成这些 Step     | ❌         | **需要查看 `bootstrap.sh` 和 Pipeline Generator** |
| Agent、AWS、Terraform、Buildkite 配置    | ❌         | ✅                                            |

---

**结合你最近在做的 CUDA Graph PR（#43586）来看，我建议你下一步重点阅读 `ci-infra` 中的两个文件：**

1. **`buildkite/bootstrap.sh`**：这是整个 CI 的入口，负责解析 PR 信息、决定测试范围，并生成动态 Pipeline，是理解 Buildkite 工作流程的最佳切入点。([Gitea: Git with a cup of tea][1])
2. **`buildkite/test-template-ci.j2`（以及新的 Pipeline Generator）**：这里定义了各种 Step（如 CPU、CUDA、ROCm、Nightly 等）是如何生成的，以及为什么同一个 PR 会拆分成几十个 Buildkite Job。README 也提到，vLLM 正在逐步从 Jinja2 模板迁移到自定义 Pipeline Generator。([Gitea: Git with a cup of tea][1])

如果把这两部分读懂，你基本就能理解 **Buildkite 页面上的每一个 Step 是从哪里来的、为什么会进入不同的 Queue，以及为什么有时会出现长时间 Waiting**。

[1]: https://gitea.cncfstack.com/vLLM/ci-infra/src/commit/97842c48e5a7698ca37998731636dbfe2c3fd643?utm_source=chatgpt.com "vLLM/ci-infra: This repo hosts code for vLLM CI & Performance Benchmark infrastructure. - ci-infra - Gitea: Git with a cup of tea"
[2]: https://github.com/vllm-project/vLLM?utm_source=chatgpt.com "GitHub - vllm-project/vllm: A high-throughput and memory-efficient inference and serving engine for LLMs · GitHub"

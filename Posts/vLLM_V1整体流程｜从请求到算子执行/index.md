# vLLM V1 整体流程｜从请求到算子执行

## 一、引言

**vLLM V1** 是 vLLM 团队基于 V0 的实践经验并参考工业界其它相关工作提出的最新架构，从 vLLM **0.8.x** 版本开始，**V1 Engine** 将作为 vLLM 启动时的默认选项。

相比于 V0，vLLM V1 具有以下优势：

- **可读性**：代码更加简洁易懂、更加模块化；
- **高性能**：提供更好的推理性能，使用双进程异步处理不同的 CPU 操作，极大地降低了推理的时延和开销；
- **易扩展**：可以轻松集成多样化的特性；
- **易用性**：简化了配置，会默认开启一些特性，以提供更好的性能和体验。

下面，本文将揭秘 vLLM V1 从接收请求到算子执行的推理全流程（附超长流程图，画图不易，欢迎点赞 & 收藏~）。

## 二、整体概览

在深入具体细节之前，让我们先从整体上认识下 V1 Engine 的推理流程。

下面是 vLLM 官方博客中提供的 V1 Engine 在线推理架构图。在 V1 中，vLLM 将不同类型的 CPU 密集型操作拆分到了两个相互独立的进程中，以便能够异步执行不同的 CPU 操作，减少了不同步骤之间相互等待的时间，因此能够更好地压榨硬件的计算性能。

![](./images/1_v1_arch.png)

- `Process 0` 主要负责请求的预处理（如：参数校验）、Tokenization 以及 Detokenization 等操作；
- `Process 1` 主要负责请求的调度和模型推理等操作。

在优化前（V0），`Process 0` 和 `Process 1` 中的操作顺序执行，因此存在许多 CPU 空闲等待的时间，而在 V1 中则是并行执行上面两个进程中的操作，因此极大地提升了整体的推理效率。

下面，本文将基于 vLLM **v0.8.5**，并以 Qwen 模型的离线推理为例（在线推理类似，本文不再详细展开，请自行阅读源码了解），深入剖析 vLLM V1 自顶向下的推理全流程。

**先上图，一切尽在图中~**

![](./images/2_v1_pipeline.svg)

> 高清图片链接：[<u>link</u>](https://github.com/shen-shanshan/cs-self-learning/tree/master/Open_Source/Projects/vLLM/Notes/%E6%95%B4%E4%BD%93%E6%B5%81%E7%A8%8B/images)，画图不易，走过路过欢迎点一个 Star！

## 三、具体流程

### 3.1 LLMEngine 执行流程

`LLMEngine` 是 vLLM 的离线推理引擎 `LLM` 和在线推理引擎 `AsyncLLM` 的基座，主要用于与推理引擎外部进行交互（如：接收并处理用户请求、持续获取并输出推理结果等），属于 `Process 0`。

![](./images/3_LLMEngine.svg)

**下行流程（红色）：**

1. 将 `Prompt` 列表以及采样参数等内容传入 `LLM` 的 `generate()` 方法；
2. `LLMEngine` 调用 `add_request()` 方法，并将新请求分别加入到 `Processor`、`EngineCoreClient` 以及 `OutputProcessor` 中；
3. `Processor` 调用 `process_inputs()` 方法，对请求进行预处理，包括：参数校验、语法校验（如果开启 Structured Output 的话）以及 Tokenization 等操作；
4. `SyncMPClient` 调用 `add_request()` 方法，将经过预处理的请求放到 `input_socket` 中，并通过 ZMQ Socket 发送给 `EngineCore`。`SyncMPClient` 是 `EngineCoreClient` 的一种，是 `LLMEngine` 中专门用于与 `EngineCore` 进行交互的模块。

**上行流程（蓝色）：**

1. `SyncMPClient` 在初始化时，会创建一个线程专门用于从 `output_socket` 中持续获取推理结果并放到 `outputs_queue` 中；
2. `LLMEngine` 循环调用 `step()` 方法，并通过 `get_output()` 方法从 `outputs_queue` 中获取推理结果；
3. `OutputProcessor` 对推理结果进行后处理，包括：Detokenization、准备 `RequestOutput` 对象以及终止已经生成结束符的请求等操作；
4. `LLM` 通过 `generate()` 方法返回 `RequestOutput` 给用户。

### 3.2 EngineCore 执行流程

`EngineCore` 是 vLLM 推理引擎的核心，主要负责**请求调度**和**推理执行**。

当 `LLMEngine` 初始化时，会调用 `make_client()` 方法创建一个 `EngineCoreClient` 对象，并可以根据配置创建不同的 `EngineCoreClient`，用户可以选择继续使用 V0-style 的 Engine，也可以选择 V1-style（本文仅讨论 V1）。当使用 V1 Engine 时，vLLM 会创建一个新的进程 `Process 1`，并在该进程中执行 `EngineCoreProc` 中的 `run_engine_core()` 方法，同时维持一个 `run_busy_loop()`。

![](./images/4_EngineCore.svg)

**下行流程（红色）：**

1. `EngineCoreProc` 在初始化时，会创建两个线程，分别负责将 `input_socket` 中新到来的请求放到 `input_queue` 中，以及将 `output_queue` 中的推理结果通过 `output_socket` 发送给 `EngineCoreClient`；
2. 在 busy loop 中，首先执行 `_process_input_queue()` 方法，从 `input_queue` 中读取请求并转换为 `Request` 对象，放到 `Scheduler` 的 `waiting` 队列中；然后，执行 `_process_engine_step()` 方法，让 `Scheduler` 进行一次调度，并生成调度结果 `SchedulerOutput`（每个请求本次调度需要推理几个 Token）。更细节的调度逻辑这里不再展开，感兴趣的读者可以自行阅读源码了解；
3. 将 `SchedulerOutput` 传递给 `MultiprocExecutor`，并调用 `collective_rpc("execute_model")` 方法。这里，`collective_rpc()` 方法会将需要调用的 `Worker` 方法名以及对应的参数打包并放到 `rpc_broadcast_mq` 中，而 `Worker` 则可以从该消息队列中获取需要执行的命令。

**上行流程（蓝色）：**

1. `MultiprocExecutor` 从 `worker_response_mq` 中读取各个 `Worker` 返回的推理结果；
2. `Scheduler` 根据当前调度的 `SchedulerOutput` 和 `Worker` 返回的 `ModelRunnerOutput` 更新状态，生成 `EngineCoreOutputs` 对象并放到 `output_queue` 中；
3. `EngineCoreProc` 中的 `process_output_socket()` 线程通过 ZMQ Socket 将 `output_queue` 中的推理结果返回给 `LLMEngine`。

### 3.3 Worker 执行流程

`MultiprocExecutor` 初始化时，会创建对应的 **1~N** 个 `Worker`，每个 `Worker` 分别属于一个独立的进程（每个 `Worker` 对应一张卡）。因此，如果有 **N** 个 `Worker`，则整个 vLLM 应用将包含 **N + 2** 个进程。

每个 `Worker` 被创建时，都可以拿到 `rpc_broadcast_mq` 的 Handler（全局只有 1 个），从而可以接收到 `MultiprocExecutor` 发来的消息；同样地，每个 `Worker` 也会将自己的 `worker_response_mq` 的 Handler（全局共有 N 个）交给 `MultiprocExecutor`，用于返回 `Worker` 推理的结果。

![](./images/5_Worker.svg)

**下行流程（红色）：**

1. `Worker` 从 `rpc_broadcast_mq` 获取数据并执行 `execute_model()` 方法；
2. `ModelRunner` 执行 `execute_model()` 方法，为模型准备输入（如：将数据从 CPU 上搬运到 GPU 上）；
3. `Model` 执行前向推理并计算 Logits；
4. `Sampler` 根据 Logits 进行采样（包括 Greedy、Top p 以及 Top k 等方式），得到最终的输出 Token 并生成 `ModelRunnerOutput` 对象。

**上行流程（蓝色）：**

1. `Worker` 将模型的推理结果 `ModelRunnerOutput` 通过 `worker_response_mq` 返回给 `EngineCore`。

### 3.4 Model forward 与算子调用

下面以 Qwen 模型为例，展示了各个 Layer 的计算流程，这里不再详细介绍，一切尽在图中~

值得注意的是，对于 `Attention` Layer，vLLM 中提供了多种后端，如 Flash Attention、Triton 等，每种后端的实现都放在了对应的 `AttentionBackend` 类中。另外，vLLM 中的大部分算子都放在了 `csrc` 目录下，在实际调用时，PyTorch 的 Dispatch 机制会根据不同的 Device Key 去调用不同设备的算子。

![](./images/6_Model.svg)

## 四、总结

到此为止，vLLM V1 自顶向下的推理全流程就梳理完了。这里声明一下，本文的主要目的是理清并展示整个推理引擎的 pipeline，从而可以让刚接触 vLLM 或大模型推理领域的读者对整个推理流程的全貌有一个直观的印象，而对于其中的一些细节（如：调度逻辑、KV Cache 处理以及 Socket 通信的实现细节等）选择了略过，感兴趣的读者可以自行阅读源码进行了解，后面我也会考虑再单独写文章对其中的一些模块进行介绍。

另外，目前我的工作就是全职参与 vLLM 社区的开发与维护，后续我还会持续分享更多关于 vLLM 的最新知识，欢迎大家持续关注～

## 五、参考资料

- [<u>vLLM GitHub</u>](https://github.com/vllm-project/vllm)
- [<u>vLLM V1: A Major Upgrade to vLLM’s Core Architecture</u>](https://blog.vllm.ai/2025/01/27/v1-alpha-release.html)
- [<u>图解 Vllm V1 系列 1：整体流程</u>](https://zhuanlan.zhihu.com/p/1900126076279160869?share_code=18FtZ4wqQM3hR&utm_psn=1900940137866716878)
- [<u>图解 Vllm V1 系列 2：Executor-Workers 架构</u>](https://zhuanlan.zhihu.com/p/1900613601577899465)

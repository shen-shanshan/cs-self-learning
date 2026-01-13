## 一、引言

随着 vLLM 逐渐成为生产级场景下大模型推理的通用解决方案之一，期望 vLLM 支持各式各样算力底座的需求日趋强烈。目前，在 vLLM 的官方仓库中维护着 NVIDIA GPU、AMD GPU 以及 Google TPU 等多家芯片厂商的代码，但除此之外，还有更多的 AI 芯片是通过“硬件插件化机制”来支持自家算力的。

所有不在 vLLM 官方仓库中支持的硬件，都被统称为 **OOT（Out Of Tree）Device**，包括：

- **官方插件**（指存在于 vllm-project 官方项目下的插件）：vllm-ascend（华为昇腾 NPU）、vllm-spyre、vllm-gaudi（Intel Gaudi）、vllm-neuron（AWS Neuron）、vllm-metal（Apple Silicon）等；
- **非官方插件**：vLLM-metax（沐曦 GPU）、vLLM-Kunlun（百度昆仑芯 XPU）等。

本文将深入介绍 vLLM 硬件插件化系统的原理，以及如何通过 CustomOp 完成自定义算子的注册与替换，从而使 vLLM 能够灵活地、高效地支持多样性算力。

## 二、硬件插件化

### 2.1 vLLM 的插件系统

随着 vLLM 支持的功能和特性越来越多、越来越复杂，其代码也变得越来越臃肿。为了满足更多用户的“魔改”需求，vLLM 提出了插件系统，使用户能够在不直接修改 vLLM 仓库代码的前提下，通过在插件项目中实现自定义的功能并将插件注册到 vLLM 中的方式，完成对应组件的动态替换。

该系统依赖于 Python 的 `entry_points` 机制，通过在插件项目的 `setup.py` 中指定 `entry_points` 对应的 `register()` 方法，vLLM 便能够在启动时自动检测并加载对应的插件（调用插件中定义的 `register()` 方法）。关于该机制的更多细节，可以自行阅读《Setup Tools Docs - Entry Points》进行了解，下面将直接以 vllm-ascend 为例进行说明。

> - Setup Tools Docs - Entry Points: https://setuptools.pypa.io/en/latest/userguide/entry_point.html
> - vllm-ascend: https://github.com/vllm-project/vllm-ascend

...

> 高清图片链接：https://github.com/shen-shanshan/cs-self-learning/tree/master/Posts/vLLM%E7%AE%97%E5%8A%9B%E5%A4%9A%E6%A0%B7%E6%80%A7%EF%BD%9CPlatform%E6%8F%92%E4%BB%B6%E4%B8%8ECustomOp/images，画图不易，走过路过欢迎点一个 Star！

```python
# vllm-ascend/setup.py
setup(
    name="vllm_ascend",
    ...
    entry_points={
        "vllm.platform_plugins": ["ascend = vllm_ascend:register"],
        "vllm.general_plugins": [
            "ascend_kv_connector = vllm_ascend:register_connector",
            "ascend_model_loader = vllm_ascend:register_model_loader",
            "ascend_service_profiling = vllm_ascend:register_service_profiling"
        ],
    },
)
```

字段说明:

- **Plugin Group**：`entry_points` 的 Key，代表插件组的名称（如：`vllm.general_plugins`）。在 vLLM 中有多个插件组，包括：General Plugins（如：自定义模型注册）、Platform Plugins（硬件插件，本文讲解的重点）、IO Processor Plugins 以及 Stat Logger Plugins；
- **Plugin Name**：`entry_points` 的 Value，代表插件的名称（如：`ascend_kv_connector`），一个插件组中包含多个插件；
- **Plugin Value**：注册方法的全路径名称（`=` 后面的内容，如：`vllm_ascend:register_model_loader`），`:` 后的内容是 vLLM 在发现该插件时会调用的方法名。

这些插件对应的注册方法的实现如下（返回自定义组件或调用自定义方法）：

```python
# vllm-ascend/vllm_ascend/__init__.py
def register():
    return "vllm_ascend.platform.NPUPlatform"

def register_connector():
    from vllm_ascend.distributed import register_connector
    register_connector()

def register_model_loader():
    from .model_loader.netloader import register_netloader
    register_netloader()

def register_service_profiling():
    from .profiling_config import generate_service_profiling_config
    generate_service_profiling_config()
```

完成插件的基本开发和配置后，我们就可以通过在同一个虚拟环境中同时安装 vLLM 及其插件的方式来启用该插件。

```bash
pip install vllm vllm-ascend
```

当插件启用后，vLLM 就可以通过 `load_plugins_by_group()` 来加载对应的插件组。

```python
# vllm/plugins/__init__.py
def load_plugins_by_group(group: str):
    from importlib.metadata import entry_points
    # group：插件组名称，如："vllm.platform_plugins"
    discovered_plugins = entry_points(group=group)
    ...
    plugins = dict[str, Callable[[], Any]]()
    for plugin in discovered_plugins:
        ...
        # 这里的 load() 为 entry_points 提供的接口，会返回当前插件注册的方法
        # 如：vllm_ascend:register
        func = plugin.load()
        # plugin.name：插件名称，如：ascend
        plugins[plugin.name] = func
    return plugins
```

以 Platform Plugins 为例，当该插件组加载完毕后，vLLM 就会调用对应插件的 `func()`（即 `vllm_ascend:register`）来获取我们自定义的组件，如：`NPUPlatform`。

```python
# vllm/platforms/__init__.py
_current_platform = None

def __getattr__(name: str):
    if name == "current_platform":
        global _current_platform
        if _current_platform is None:
            platform_cls_qualname = resolve_current_platform_cls_qualname()
            # 根据全路径名称创建 NPUPlatform 对象（单例）
            _current_platform = resolve_obj_by_qualname(platform_cls_qualname)()
            ...
        return _current_platform

def resolve_current_platform_cls_qualname():
    # 加载 Platform Plugins
    platform_plugins = load_plugins_by_group(PLATFORM_PLUGINS_GROUP)
    for name, func in chain(builtin_platform_plugins.items(), platform_plugins.items()):
        # 调用 vllm_ascend __init__.py 中的 register() 方法
        # 返回 "vllm_ascend.platform.NPUPlatform"
        platform_cls_qualname = func()
    ...
    return platform_cls_qualname
```

在此之后，vLLM 代码中所有通过 `current_platform` 变量调用的方法都会被动态替换为 NPU 插件自定义的实现。

```python
from vllm.platforms import current_platform

# 调用的是 NPUPlatform 中实现的 get_device_name() 和 get_device_capability()
device_name = current_platform.get_device_name()
device_capability = current_platform.get_device_capability()
```

通过上述方式，我们便可以在运行时将 vLLM 中与硬件相关的接口动态地替换为我们自定义的方法实现。

### 2.2 Platform 插件

在了解了 vLLM 插件系统的基本原理之后，让我们再来看下 vLLM 中代表不同硬件后端的 Platform 模块提供了怎样的功能，以及硬件插件到底替换了些什么。

在 vLLM 中，每一个硬件后端都有一个属于自己的 Platform 类，如：`CpuPlatform`、`CudaPlatform` 以及 `RocmPlatform` 等。其中定义了许多与硬件相关的接口，如：`check_and_update_config`（做一些适用于当前硬件的全局配置、指定要使用的 Worker，如：`GPUWorker`）、`get_attn_backend_cls`（获取当前硬件支持的 Attention 计算后端）以及一些用于获取当前硬件信息的方法。

除了 Platform 之外，不同硬件也有属于自己的 Worker 和 ModelRunner（ModelRunner 在 Worker 中指定并创建），给不同硬件的定制化留下了充足的空间（不过目前看来这并不一定是好事，因为越灵活就意味着维护成本越高，往往一个新特性出来，可能需要在所有硬件后端上都适配一遍，很多公共的东西并没有很好地复用起来）。

...

> 高清图片链接：https://github.com/shen-shanshan/cs-self-learning/tree/master/Posts/vLLM%E7%AE%97%E5%8A%9B%E5%A4%9A%E6%A0%B7%E6%80%A7%EF%BD%9CPlatform%E6%8F%92%E4%BB%B6%E4%B8%8ECustomOp/images，画图不易，走过路过欢迎点一个 Star！

对于 vLLM 的硬件插件而言，它们需要替换的核心组件就是上面我们谈到的 Platform、Worker、ModelRunner 以及各种算子。

以 vllm-ascend 为例，当我们通过插件机制将 `NPUPlatform` 注册到 `current_platform` 上后，就可以通过 `NPUPlatform` 获取到 NPU 的相关组件，如：`NPUWorker`、`NPUModelRunner`、`NPUCommunicator` 以及 `AscendAttentionBackend` 等。也就是说，当 vLLM 通过 `current_platform` 调用相关接口获取这些类并实例化时，创建的就是 NPU 组件的对象。

通过这样一套机制，便可以让 vLLM 无缝支持任意的硬件后端。芯片厂商无需直接贡献代码到 vLLM 中，就可以享有自定义全局配置、计算流程、通信以及算子的能力，为 vLLM 灵活地支持多样性算力提供了支持。

关于如何实现一个 vLLM 硬件插件的更多细节，可以参考官方文档《Guidelines for Writing Plugins》，本文不再赘述。

> Guidelines for Writing Plugins: https://docs.vllm.ai/en/latest/design/plugin_system/#guidelines-for-writing-plugins

## 三、自定义算子

### 3.1 CustomOp 的基本原理

CustomOp 是 vLLM 在框架（Python）侧定义的算子基类，可以根据当前的 `current_platform` 将该算子的 `forward()` 分发到对应的硬件后端，并调用对应硬件的算子。简而言之，CustomOp 为 vLLM 中各种各样的算子定义了一套统一的抽象接口，并负责管理算子的注册与分发。

**CustomOp 是如何进行算子分发的？**

当我们调用一个 CustomOp（调用其 `forward()` 方法）时，如果该算子是 enable 的，那么 CustomOp 就会根据当前的 `current_platform` 调用对应的 `forward_xxx()` 方法；反之（该算子是 disable 的），CustomOp 则不会进行分发，而是会直接调用 `forward_native()` 方法（PyTorch-native 的算子实现，一般用于在 `torch.compile` 模式下去编译和生成对应的 Triton 算子）。

具体的分发逻辑如下：

- **CPU platform**：分发给 `forward_cpu()`；
- **CUDA platform**：分发给 `forward_cuda()`；
- **ROCm platform**：分发给 `forward_hip()`，如果某个 CustomOp 没有实现 `forward_hip()` 方法，则会分发给 `forward_cuda()` 作为 fallback；
- **XPU platform**：分发给 `forward_xpu()`.
- **TPU platform**：分发给 `forward_tpu()`.
- **OOT platform**：分发给 `forward_oot()`，即调用 OOT 硬件插件的算子；
- **Default**：分发给 `forward_native()` 作为所有硬件后端的 final fallback。

> 注意：算子之间的继承关系可能导致 CustomOp 基类的 dispatch 逻辑被子类覆盖，因此实际的分发逻辑应以具体情况为准。

**如何 enable/disable 一个 CustomOp？**

在 vLLM 中，是否启用一个 CustomOp 是通过 `compilation_config.custom_ops` 来进行控制的。

在默认配置下，当满足 `compilation_config.backend == "inductor"` 且 `compilation_config.mode != CompilationMode.NONE` 时，`compilation_config.custom_ops` 的值为 `none`（即 disable 所有 CustomOp）；反之，则为 `all`（即 enable 所有 CustomOp）。

也就是说，对于使用 Inductor 作为 `torch.compile` 后端的硬件（如：NVIDIA GPU），当开启 `torch.compile` 时，所有 CustomOp 的分发都会被禁用，直接全部走 `forward_native()` 分支去编译并生成 Triton 算子。只有在 `enforce_eager=True`（即单算子模式）下，vLLM 才会启用 CustomOp 的分发，才会去调用不同硬件后端自己优化过的算子。

> 注意：对于多模态模型，vLLM 强制开启了 ViT 部分的 CustomOp 以获得更好的性能，比如：`MMEncoderAttention` 和 `ApplyRotaryEmb`。我们可以通过在创建 CustomOp 对象时传入一个 `enforce_enable=True` 参数来强制开启该 CustomOp 对象。

另外，vLLM 还为 CustomOp 的 enable/disable 提供了细粒度控制的能力，用户可以根据自己的需要手动开启或关闭一个 CustomOp。

具体地，有如下几种配置方式：

- `--compilation_config.custom_ops '["all"]'`：enable 所有 CustomOp；
- `--compilation_config.custom_ops '["none"]'`：disable 所有 CustomOp；
- `--compilation_config.custom_ops '["all,-op1"]'`：enable 除了 `op1` 之外的其它所有 CustomOp（在名称前一个 `-` 代表 disable 该算子）；
- `--compilation_config.custom_ops '["none,+op1,+op2"]'`：只 enable `op1` 和 `op2`（在名称前一个 `+` 代表 enabl 该算子）。

> 注意：`all` 和 `none` 不能同时出现在 `compilation_config.custom_ops` 中。

### 3.2 CustomOp 的实现与注册

**如何实现并注册一个新的 CustomOp？**

下面，我们将介绍如何在 vLLM 中实现并注册一个新的 CustomOp。

...

> 高清图片链接：https://github.com/shen-shanshan/cs-self-learning/tree/master/Posts/vLLM%E7%AE%97%E5%8A%9B%E5%A4%9A%E6%A0%B7%E6%80%A7%EF%BD%9CPlatform%E6%8F%92%E4%BB%B6%E4%B8%8ECustomOp/images，画图不易，走过路过欢迎点一个 Star！

具体的实现步骤如下：

1. 创建一个继承自 CustomOp 的算子类；
2. 根据需要实现不同的 `forward_xxx()` 方法；
3. 在类上添加 `@CustomOp.register("op_name")` 完成算子的注册。

```python
@CustomOp.register("op_name")
class XxxOp(CustomOp):

    def __init__(...):
        ...
    
    def forward_native(...):
        ...
    
    def forward_cpu(...):
        ...
    
    def forward_cuda(...):
        ...
```

在 CustomOp 中，有两个字典（Key：算子名称，Value：算子类）分别负责管理 vLLM 中原生的 CustomOp 以及硬件插件（OOT）的 CustomOp，其中记录了所有已注册的算子。

```python
# vllm/model_executor/custom_op.py
class CustomOp(nn.Module):

    op_registry: dict[str, type["CustomOp"]] = {}
    op_registry_oot: dict[str, type["CustomOp"]] = {}
```

在 vLLM 中，可以通过 `@CustomOp.register("op_name")` 实现算子的注册。当我们给一个 CustomOp 的子类（假设叫 `XxxCustomOp`）加上该装饰器后，`op_name` 和 `XxxCustomOp` 就会被添加到 `op_registry` 中，这样就算完成该算子的注册了。

**如何实现并注册一个 OOT CustomOp？**

目前，除了 vLLM 官方维护的硬件后端之外，越来越多的硬件厂商都纷纷创建了自己的 vLLM 硬件插件项目（形如：vllm-xxx）。对于这些项目而言，OOT CustomOp 让他们能够在不直接修改原生 CustomOp forward 分发逻辑的前提下，将调用的算子无缝替换为自己的高性能 kernel，极大地提升了 vLLM 算子调用的可扩展性。

具体的实现步骤如下（假设我们需要替换 `AaaCustomOp` 算子）：

1. 创建一个继承自 `AaaCustomOp` 的算子类（`BbbCustomOp`）；
2. 实现 `forward_oot()` 方法；
3. 在类上添加 `@CustomOp.register_oot("Aaa")` 完成算子的注册。

```python
# 在 vllm 中：
from vllm.model_executor.custom_op import CustomOp

@CustomOp.register("Aaa")
class AaaCustomOp(CustomOp):
    ...


# 在 vllm-xxx 插件中：
from vllm.xxx import AaaCustomOp

@CustomOp.register_oot("Aaa")
class BbbCustomOp(AaaCustomOp):

    def __init__(...):
        super().__init__(...)

    def forward_oot(...):
        # Call optimized device-specific kernels.
        ...
```

通过 `@CustomOp.register_oot("Aaa")`，`BbbCustomOp` 将会被注册到 CustomOp 基类的 `op_registry_oot` 中（增加一个新的键值对：`{"Aaa": BbbCustomOp}`）。

完成注册后，当 vLLM 实际去创建 `AaaCustomOp` 的实例对象时，创建的就会是 `BbbCustomOp` 对象，并且在做 forward 的 dispatch 时，由于检测到当前的 `current_platform` 属于 OOT device，最终就会调用我们自己实现的 `forward_oot()` 方法，里面则会调用硬件插件深度优化过的 kernel。

相关代码如下：

```python
# vllm/model_executor/custom_op.py
class CustomOp(nn.Module):

    def __new__(cls, *args, **kwargs):
        op_name = cls.__name__
        ...
        if op_name not in cls.op_registry_oot:
            op_cls_to_instantiate = cls
        else:
            # 如果不属于 vLLM 原生注册的 CustomOp（即是 OOT CustomOp），
            # 就会根据 op_name 去找已注册的 OOT 算子类作为要实例化的类
            op_cls_to_instantiate = cls.op_registry_oot[op_name]
        return super().__new__(op_cls_to_instantiate)
    
    @classmethod
    def register(cls, name: str):
        def decorator(op_cls):
            ...
            op_cls.name = name
            cls.op_registry[name] = op_cls
            return op_cls
        return decorator
    
    @classmethod
    def register_oot(cls, _decorated_op_cls=None, name: str | None = None):
        def decorator(op_cls):
            ...
            op_cls.name = name
            cls.op_registry_oot[name] = op_cls
            return op_cls
```

## 四、总结

最后总结一下，vLLM 通过 Platform 插件以及 CustomOp 等机制，增强了硬件模块的可扩展性与灵活性，为多样性算力的支持提供了助力。

## 五、参考资料

- vLLM GitHub: https://github.com/vllm-project/vllm
- vLLM-Ascend GitHub: https://github.com/vllm-project/vllm-ascend
- Introducing vLLM Hardware Plugin, Best Practice from Ascend NPU: https://blog.vllm.ai/2025/05/12/hardware-plugin.html
- Setup Tools Docs: Entry Points: https://setuptools.pypa.io/en/latest/userguide/entry_point.html
- vLLM Docs: Plugin System: https://docs.vllm.ai/en/latest/design/plugin_system/
- vLLM Docs: CustomOp: https://docs.vllm.ai/en/latest/design/custom_op/

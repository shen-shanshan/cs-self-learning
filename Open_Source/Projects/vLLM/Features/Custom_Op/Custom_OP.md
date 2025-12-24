# vLLM Custom Op

## vLLM 代码

`vllm.utils.direct_register_custom_op`:

```python
from torch.library import Library


# create a library to hold the custom op
vllm_lib = Library("vllm", "FRAGMENT")


def direct_register_custom_op(
        op_name: str,
        op_func: Callable,
        mutates_args: list[str],
        fake_impl: Optional[Callable] = None,
        target_lib: Optional[Library] = None,
        dispatch_key: str = "CUDA",
        tags: tuple[torch.Tag, ...] = (),
):
    """
    `torch.library.custom_op` can have significant overhead because it
    needs to consider complicated dispatching logic. This function
    directly registers a custom op and dispatches it to the CUDA backend.
    See https://gist.github.com/youkaichao/ecbea9ec9fc79a45d2adce1784d7a9a5
    for more details.

    By default, the custom op is registered to the vLLM library. If you
    want to register it to a different library, you can pass the library
    object to the `target_lib` argument.

    IMPORTANT: the lifetime of the operator is tied to the lifetime of the
    library object. If you want to bind the operator to a different library,
    make sure the library object is alive when the operator is used.
    """
    # ...

    import torch.library
    if hasattr(torch.library, "infer_schema"):
        schema_str = torch.library.infer_schema(op_func,
                                                mutates_args=mutates_args)
    # ...

    my_lib = target_lib or vllm_lib
    my_lib.define(op_name + schema_str, tags=tags)
    my_lib.impl(op_name, op_func, dispatch_key=dispatch_key)

    if fake_impl is not None:
        my_lib._register_fake(op_name, fake_impl)
```

FusedMoE:

```python
@CustomOp.register("fused_moe")
class FusedMoE(CustomOp):

    def forward_native(...):
        # self.shared_experts is not None
        shared_output, fused_output = torch.ops.vllm.moe_forward_shared(...)

    def forward_impl(...):
        # ...


def moe_forward_shared(
    hidden_states: torch.Tensor,
    router_logits: torch.Tensor,
    layer_name: str,
) -> tuple[torch.Tensor, torch.Tensor]:
    forward_context: ForwardContext = get_forward_context()
    self = forward_context.no_compile_layers[layer_name]
    assert self.shared_experts is not None
    return self.forward_impl(hidden_states, router_logits)


direct_register_custom_op(
    op_name="moe_forward_shared",
    op_func=moe_forward_shared,
    mutates_args=["hidden_states"],
    fake_impl=moe_forward_shared_fake,
    dispatch_key=current_platform.dispatch_key,
    tags=(torch.Tag.needs_fixed_stride_order, ),
)
```

CompilationConfig:

```python
@config
@dataclass
class CompilationConfig:
    custom_ops: list[str] = field(default_factory=list)
    """Fine-grained control over which custom ops to enable/disable. Use 'all'
    to enable all, 'none' to disable all. Also specify a list of custom op
    names to enable (prefixed with a '+'), or disable (prefixed with a '-').
    Examples:

    - 'all,-op1' to enable all except op1
    - 'none,+op1,+op2' to enable only op1 and op2

    By default, all custom ops are enabled when running without Inductor and
    disabled when running with Inductor: level>=PIECEWISE and use_inductor=True.
    Inductor generates (fused) Triton kernels for disabled custom ops."""
    
    enabled_custom_ops: Counter[str] = field(default_factory=Counter, init=False)
    """custom ops that are enabled"""
    
    disabled_custom_ops: Counter[str] = field(default_factory=Counter, init=False)
    """custom ops that are disabled"""
```

## 参考资料

```python
import torch
import torch.library as lib

# 1. 定义算子的前向计算（CPU版本）
def sigmoid_cpu(x):
    return 1 / (1 + torch.exp(-x))

# 2. 注册算子
# 定义schema（算子签名）
lib.define("myns::sigmoid(Tensor x) -> Tensor")

# 为CPU实现注册
lib.impl("myns::sigmoid", sigmoid_cpu, "CPU")

# 3. 定义反向计算（用于自动求导）
def sigmoid_backward(grad_output, output):
    return grad_output * output * (1 - output)

# 注册反向计算
lib.define("myns::sigmoid_backward(Tensor grad_output, Tensor output) -> Tensor")
lib.impl("myns::sigmoid_backward", sigmoid_backward, "CPU")

# 4. 包装成函数（用于自动求导）
class Sigmoid(torch.autograd.Function):
    @staticmethod
    def forward(ctx, x):
        output = torch.ops.myns.sigmoid(x)
        ctx.save_for_backward(output)
        return output

    @staticmethod
    def backward(ctx, grad_output):
        output, = ctx.saved_tensors
        return torch.ops.myns.sigmoid_backward(grad_output, output)

# 使用自定义算子
x = torch.tensor([1.0, 2.0, 3.0], requires_grad=True)
y = Sigmoid.apply(x)
loss = y.sum()
loss.backward()
print(x.grad)  # 输出梯度
```

```python
class MySigmoidModule(torch.nn.Module):
    def forward(self, x):
        return MySigmoid.apply(x)

model = MySigmoidModule()
x = torch.tensor([1.0, 2.0, 3.0], requires_grad=True)
y = model(x)
```

---

| Category | OP Name | OP Class |
|----------|---------|----------|
| Attention | `mm_encoder_attn` | `MMEncoderAttention` |
| Attention | `multi_head_latent_attention` | `MultiHeadLatentAttentionWrapper` |
| Activation | `fatrelu_and_mul` | `FatreluAndMul` |
| Activation | `silu_and_mul` | `SiluAndMul` |
| Activation | `mul_and_silu` | `MulAndSilu` |
| Activation | `gelu_and_mul_sparse` | `GeluAndMulSparse` |
| Activation | `gelu_and_mul` | `GeluAndMul` |
| Activation | `swigluoai_and_mul` | `SwigluOAIAndMul` |
| Activation | `gelu_new` | `NewGELU` |
| Activation | `gelu_fast` | `FastGELU` |
| Activation | `quick_gelu` | `QuickGELU` |
| Activation | `relu2` | `ReLUSquaredActivation` |
| Activation | `xielu` | `XIELU` |
| Conv | `conv2d` | `Conv2dLayer` |
| Conv | `conv3d` | `Conv3dLayer` |
| Conv | `short_conv` | `ShortConv` |
| Embedding | `vocab_parallel_embedding` | `VocabParallelEmbedding` |
| Embedding | `parallel_lm_head` | `ParallelLMHead` |
| Linear | `row_parallel_linear` | `RowParallelLinear` |
| Linear | `column_parallel_linear` | `ColumnParallelLinear` |
| Linear | `replicated_linear` | `ReplicatedLinear` |
| Logits Processor | `logits_processor` | `LogitsProcessor` |
| Mamba | `mamba_mixer` | `MambaMixer` |
| Mamba | `mamba_mixer2` | `MambaMixer2` |
| Mamba | `plamo2_mamba_mixer` | `Plamo2MambaMixer` |
| Mamba | `mixer2_gated_rms_norm` | `Mixer2RMSNormGated` |
| MoE | `fused_moe` | `FusedMoE` |
| MoE | `modular_fused_moe` | `FusedMoEModularMethod` |
| MoE | `unquantized_fused_moe` | `UnquantizedFusedMoEMethod` |
| MoE | `transformers_fused_moe` | `TransformersFusedMoE` |
| MoE | `grouped_topk` | `GroupedTopk` |
| Norm | `rms_norm` | `RMSNorm` |
| Norm | `gemma_rms_norm` | `GemmaRMSNorm` |
| Norm | `rms_norm_gated` | `RMSNormGated` |
| Quantization | `quant_fp8` | `QuantFP8` |
| Rope | `rotary_embedding` | `RotaryEmbeddingBase` |
| Rope | `dual_chunk_rotary_embedding` | `DualChunkRotaryEmbedding` |
| Rope | `apply_rotary_emb` | `ApplyRotaryEmb` |

---

```
Attention:
mm_encoder_attn
multi_head_latent_attention

Activation:
silu_and_mul
mul_and_silu
gelu_new
gelu_fast
quick_gelu
gelu_and_mul
gelu_and_mul_sparse
relu2
xielu
swigluoai_and_mul
fatrelu_and_mul

MM-Conv:
conv2d
conv3d

Embedding:
vocab_parallel_embedding
parallel_lm_head

Linear:
row_parallel_linear
column_parallel_linear
replicated_linear

Logits Processor:
logits_processor

Mamba:
mamba_mixer
mamba_mixer2
mixer2_gated_rms_norm
plamo2_mamba_mixer
short_conv

MoE:
fused_moe
modular_fused_moe
unquantized_fused_moe
transformers_fused_moe
grouped_topk

Norm:
rms_norm
rms_norm_gated
gemma_rms_norm

Quantization:
quant_fp8

Rope:
rotary_embedding
dual_chunk_rotary_embedding
apply_rotary_emb

register("short_conv")
# --8<-- [start:mm_encoder_attn]
# --8<-- [end:mm_encoder_attn]
--8<-- "example.py:mm_encoder_attn"
```

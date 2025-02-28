# Add collect_env scripts from vLLM

Add `collect_env.py` scripts from vLLM and remove `nvidia`, `gpu`, `cuda` related codes, thus users of vllm-ascend can collect their env info when reporting bugs.

A example of results are shown below:

```bash
PyTorch version: 2.5.1
Is debug build: False
ROCM used to build PyTorch: N/A

OS: Ubuntu 22.04.5 LTS (aarch64)
GCC version: (Ubuntu 11.4.0-1ubuntu1~22.04) 11.4.0
Clang version: Could not collect
CMake version: version 3.31.4
Libc version: glibc-2.35

Python version: 3.10.16 (main, Dec 11 2024, 16:18:56) [GCC 11.2.0] (64-bit runtime)
Python platform: Linux-4.19.90-vhulk2211.3.0.h1804.eulerosv2r10.aarch64-aarch64-with-glibc2.35
HIP runtime version: N/A
MIOpen runtime version: N/A
Is XNNPACK available: True

CPU:
Architecture:                       aarch64
CPU op-mode(s):                     64-bit
Byte Order:                         Little Endian
CPU(s):                             192
On-line CPU(s) list:                0-191
Vendor ID:                          HiSilicon
Model name:                         Kunpeng-920
Model:                              0
Thread(s) per core:                 1
Core(s) per cluster:                48
Socket(s):                          -
Cluster(s):                         4
Stepping:                           0x1
BogoMIPS:                           200.00
Flags:                              fp asimd evtstrm aes pmull sha1 sha2 crc32 atomics fphp asimdhp cpuid asimdrdm jscvt fcma dcpop asimddp asimdfhm ssbs
L1d cache:                          12 MiB (192 instances)
L1i cache:                          12 MiB (192 instances)
L2 cache:                           96 MiB (192 instances)
L3 cache:                           192 MiB (8 instances)
NUMA node(s):                       8
NUMA node0 CPU(s):                  0-23
NUMA node1 CPU(s):                  24-47
NUMA node2 CPU(s):                  48-71
NUMA node3 CPU(s):                  72-95
NUMA node4 CPU(s):                  96-119
NUMA node5 CPU(s):                  120-143
NUMA node6 CPU(s):                  144-167
NUMA node7 CPU(s):                  168-191
Vulnerability Gather data sampling: Not affected
Vulnerability Itlb multihit:        Not affected
Vulnerability L1tf:                 Not affected
Vulnerability Mds:                  Not affected
Vulnerability Meltdown:             Not affected
Vulnerability Mmio stale data:      Not affected
Vulnerability Retbleed:             Not affected
Vulnerability Spec store bypass:    Mitigation; Speculative Store Bypass disabled via prctl
Vulnerability Spectre v1:           Mitigation; __user pointer sanitization
Vulnerability Spectre v2:           Not affected
Vulnerability Srbds:                Not affected
Vulnerability Tsx async abort:      Not affected

Versions of relevant libraries:
[pip3] mypy==1.11.1
[pip3] mypy-extensions==1.0.0
[pip3] numpy==1.26.4
[pip3] pyzmq==26.2.1
[pip3] torch==2.5.1
[pip3] torch-npu==2.5.1.dev20250218
[pip3] torchaudio==2.5.1
[pip3] torchvision==0.20.1
[pip3] transformers==4.49.0
[conda] numpy                     1.26.4                   pypi_0    pypi
[conda] pyzmq                     26.2.1                   pypi_0    pypi
[conda] torch                     2.5.1                    pypi_0    pypi
[conda] torch-npu                 2.5.1.dev20250218          pypi_0    pypi
[conda] torchaudio                2.5.1                    pypi_0    pypi
[conda] torchvision               0.20.1                   pypi_0    pypi
[conda] transformers              4.49.0                   pypi_0    pypi
ROCM Version: Could not collect
Neuron SDK Version: N/A
vLLM Version: 0.1.dev4809+g145944c (git sha: 145944c
vLLM Build Flags:
ROCm: Disabled; Neuron: Disabled

VLLM_USE_MODELSCOPE=True
LD_LIBRARY_PATH=/home/sss/software/miniconda3/envs/vllm/lib/python3.10/site-packages/cv2/../../lib64:/home/sss/Ascend/nnal/atb/latest/atb/cxx_abi_1/lib:/home/sss/Ascend/nnal/atb/latest/atb/cxx_abi_1/examples:/home/sss/Ascend/nnal/atb/latest/atb/cxx_abi_1/tests/atbopstest:/home/sss/Ascend/ascend-toolkit/latest/tools/aml/lib64:/home/sss/Ascend/ascend-toolkit/latest/tools/aml/lib64/plugin:/home/sss/Ascend/ascend-toolkit/latest/lib64:/home/sss/Ascend/ascend-toolkit/latest/lib64/plugin/opskernel:/home/sss/Ascend/ascend-toolkit/latest/lib64/plugin/nnengine:/home/sss/Ascend/ascend-toolkit/latest/opp/built-in/op_impl/ai_core/tbe/op_tiling/lib/linux/aarch64:/usr/local/Ascend/driver/lib64:/usr/local/Ascend/driver/lib64/common:/usr/local/Ascend/driver/lib64/driver:/home/sss/Ascend/nnal/atb/latest/atb/cxx_abi_1/lib:/home/sss/Ascend/nnal/atb/latest/atb/cxx_abi_1/examples:/home/sss/Ascend/nnal/atb/latest/atb/cxx_abi_1/tests/atbopstest:/home/sss/Ascend/ascend-toolkit/latest/tools/aml/lib64:/home/sss/Ascend/ascend-toolkit/latest/tools/aml/lib64/plugin:/home/sss/Ascend/ascend-toolkit/latest/lib64:/home/sss/Ascend/ascend-toolkit/latest/lib64/plugin/opskernel:/home/sss/Ascend/ascend-toolkit/latest/lib64/plugin/nnengine:/home/sss/Ascend/ascend-toolkit/latest/opp/built-in/op_impl/ai_core/tbe/op_tiling/lib/linux/aarch64:/usr/local/Ascend/driver/lib64:/usr/local/Ascend/driver/lib64/common:/usr/local/Ascend/driver/lib64/driver:
NCCL_CUMEM_ENABLE=0
TORCHINDUCTOR_COMPILE_THREADS=1
```

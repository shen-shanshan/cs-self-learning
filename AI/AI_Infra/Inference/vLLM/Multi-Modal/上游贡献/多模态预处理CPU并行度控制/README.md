# 多模态预处理 CPU 并行度控制

## Background

- 多实例导致的cpu占用过高 [#29078](https://github.com/vllm-project/vllm/issues/29078)
- vLLM 3x slower than SGLang serving Qwen3-VL [#29869](https://github.com/vllm-project/vllm/issues/29869)

- [How to set OMP_NUM_THREADS for distruted training?](https://discuss.pytorch.org/t/how-to-set-omp-num-threads-for-distruted-training/143234)

PR:

- Set default torch num threads for input processing [#31879](https://github.com/vllm-project/vllm/pull/31879)
- CFS-aware torch thread count in set_default_torch_num_threads [#34462](https://github.com/vllm-project/vllm/pull/34462)

---

```bash
lscpu
# Architecture:            aarch64
#   CPU op-mode(s):        64-bit
#   Byte Order:            Little Endian
# CPU(s):                  192
#   On-line CPU(s) list:   0-191
# Vendor ID:               HiSilicon
#   Model name:            Kunpeng-920
#     Model:               0
#     Thread(s) per core:  1
#     Core(s) per cluster: 48
#     Socket(s):           -
#     Cluster(s):          4
#     Stepping:            0x1
#     BogoMIPS:            200.00
#     Flags:               fp asimd evtstrm aes pmull sha1 sha2 crc32 atomics fphp asimdhp cpuid asimdrdm jscvt fcma dcpop asimddp asimdfhm ssbs
# Caches (sum of all):     
#   L1d:                   12 MiB (192 instances)
#   L1i:                   12 MiB (192 instances)
#   L2:                    96 MiB (192 instances)
#   L3:                    192 MiB (8 instances)
# NUMA:                    
#   NUMA node(s):          8
#   NUMA node0 CPU(s):     0-23
#   NUMA node1 CPU(s):     24-47
#   NUMA node2 CPU(s):     48-71
#   NUMA node3 CPU(s):     72-95
#   NUMA node4 CPU(s):     96-119
#   NUMA node5 CPU(s):     120-143
#   NUMA node6 CPU(s):     144-167
#   NUMA node7 CPU(s):     168-191
# Vulnerabilities:         
#   Gather data sampling:  Not affected
#   Itlb multihit:         Not affected
#   L1tf:                  Not affected
#   Mds:                   Not affected
#   Meltdown:              Not affected
#   Mmio stale data:       Not affected
#   Retbleed:              Not affected
#   Spec store bypass:     Mitigation; Speculative Store Bypass disabled via prctl
#   Spectre v1:            Mitigation; __user pointer sanitization
#   Spectre v2:            Not affected
#   Srbds:                 Not affected
#   Tsx async abort:       Not affected

nproc  
# 192
```

最佳实践：
OMP_NUM_THREADS = physical_cores / processes (nproc_per_node)

OMP_NUM_THREADS should be set to #PhysicalCores / #Processes.

- os.process_cpu_count, as @handaru mentioned and was added in Python 3.13, as well as nproc and htop, gives the number of logical CPUs. Please use psutil.cpu_count(logical=False) to get the number of physical CPUs.
- Don’t forget to divide it by nproc_per_node as @Kenzo mentioned, or you’ll have many processes competing for each core.

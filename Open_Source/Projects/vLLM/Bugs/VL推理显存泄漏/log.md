```bash
[rank0]:[E107 02:38:42.269390205 compiler_depend.ts:444] NPU out of memory. NPUWorkspaceAllocator tried to allocate 180.00 MiB(NPU 0; 60.96 GiB total capacity; 89.51 MiB free). If you want to reduce memory usage, take a try to set the environment variable TASK_QUEUE_ENABLE=1.

[ERROR] 2026-01-07-02:38:42 (PID:1692557, Device:0, RankID:-1) ERR00006 PTA memory error
Exception raised from malloc at build/CMakeFiles/torch_npu.dir/compiler_depend.ts:447 (most recent call first):
frame #0: c10::Error::Error(c10::SourceLocation, std::__cxx11::basic_string<char, std::char_traits<char>, std::allocator<char> >) + 0xb0 (0xffff925a48c0 in /root/miniconda3/envs/vllm/lib/python3.11/site-packages/torch/lib/libc10.so)
frame #1: c10::detail::torchCheckFail(char const*, char const*, unsigned int, std::__cxx11::basic_string<char, std::char_traits<char>, std::allocator<char> > const&) + 0x68 (0xffff9254c140 in /root/miniconda3/envs/vllm/lib/python3.11/site-packages/torch/lib/libc10.so)
frame #2: <unknown function> + 0x9ef468 (0xffff801ef468 in /root/miniconda3/envs/vllm/lib/python3.11/site-packages/torch_npu/lib/libtorch_npu.so)
frame #3: <unknown function> + 0x9efdcc (0xffff801efdcc in /root/miniconda3/envs/vllm/lib/python3.11/site-packages/torch_npu/lib/libtorch_npu.so)
frame #4: <unknown function> + 0x9e946c (0xffff801e946c in /root/miniconda3/envs/vllm/lib/python3.11/site-packages/torch_npu/lib/libtorch_npu.so)
frame #5: <unknown function> + 0x2a43e64 (0xffff82243e64 in /root/miniconda3/envs/vllm/lib/python3.11/site-packages/torch_npu/lib/libtorch_npu.so)
frame #6: at_npu::native::allocate_workspace(unsigned long, void*) + 0x2c (0xffff801e5d6c in /root/miniconda3/envs/vllm/lib/python3.11/site-packages/torch_npu/lib/libtorch_npu.so)
frame #7: <unknown function> + 0xade1c (0xffff6c25de1c in /root/miniconda3/envs/vllm/lib/python3.11/site-packages/torch_npu/lib/libop_plugin_atb.so)
frame #8: <unknown function> + 0x29f0894 (0xffff821f0894 in /root/miniconda3/envs/vllm/lib/python3.11/site-packages/torch_npu/lib/libtorch_npu.so)
frame #9: <unknown function> + 0x9cc700 (0xffff801cc700 in /root/miniconda3/envs/vllm/lib/python3.11/site-packages/torch_npu/lib/libtorch_npu.so)
frame #10: <unknown function> + 0x9cd2dc (0xffff801cd2dc in /root/miniconda3/envs/vllm/lib/python3.11/site-packages/torch_npu/lib/libtorch_npu.so)
frame #11: <unknown function> + 0x9cb1f8 (0xffff801cb1f8 in /root/miniconda3/envs/vllm/lib/python3.11/site-packages/torch_npu/lib/libtorch_npu.so)
frame #12: <unknown function> + 0xda294 (0xffffa441a294 in /root/miniconda3/envs/vllm/bin/../lib/libstdc++.so.6)
frame #13: <unknown function> + 0x80398 (0xffffa45c0398 in /lib/aarch64-linux-gnu/libc.so.6)
frame #14: <unknown function> + 0xe9e9c (0xffffa4629e9c in /lib/aarch64-linux-gnu/libc.so.6)
```

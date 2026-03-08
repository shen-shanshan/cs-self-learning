# TODO

## Tasks

- [vLLM & vLLM-Ascend Tasks](./Open_Source/Projects/vLLM/TODO.md)

**TODO（P0）：**

- [ ] 学习 Triton 算子开发
  - https://github.com/gpu-mode/lectures/tree/main/lecture_014
  - https://gitcode.com/Ascend/triton-ascend
- [ ] 学习 Diffusion 模型原理与推理技术
  - https://www.zhihu.com/question/1919814183635093018
- [ ] 学习 CUDA Graph 原理
  - https://github.com/CalvinXKY/InfraTech/blob/main/llm_infer/cuda_graph.ipynb
- [ ] 梳理多模态弹性推理技术综述
  - https://arxiv.org/abs/2401.13601
- [ ] 学习 Qwen3.5 模型结构

**疑惑：**

- [ ] CUDA Graph：
  - 哪些操作能和不能被捕获入图？
  - 需要占用一定的显存资源，vLLM 是如何管理的？
  - 什么是动态参数？
  - prefill 与 decode 通信同步等待，这里 event 是 cpu 操作，cuda graph 抓不到也不好做，除非又搞子图（？）

## LLM 基础

基础知识：

- [ ] [大模型推理必学：专业术语与概念引导（上）](https://mp.weixin.qq.com/s/sfRFrvT3s1PGHjjiT0pi5A)
- [ ] [大模型推理必学：专业术语与概念引导（下）](https://mp.weixin.qq.com/s/FGQzmFlZ6MTkVizeGSJoXQ)
- [ ] [2025 年最全面的 LLM 架构技术解析](https://mp.weixin.qq.com/s/7qVwBhE5JcJFnyOKpYNCeA)
- [ ] [开源大模型推理引擎现状及常见推理优化方法](https://zhuanlan.zhihu.com/p/755874470?share_code=13fdP1R6gsoY8&utm_psn=1995146863595254830)
- [ ] [LLM推理优化的上半场：单实例推理优化接近尾声](https://zhuanlan.zhihu.com/p/1912580139667154389)

NLP：

- [ ] [The Unreasonable Effectiveness of Recurrent Neural Networks](https://karpathy.github.io/2015/05/21/rnn-effectiveness/)
- [ ] [The Illustrated Word2vec - Jay Alammar](https://jalammar.github.io/illustrated-word2vec/)

Transformer：

- [ ] [缓存与效果的极限拉扯：从 MHA、MQA、GQA 到 MLA - 苏剑林](https://spaces.ac.cn/archives/10091)
- [ ] MQA/GQA
- [ ] [避开复数推导，我们还可以怎么理解 RoPE？](https://zhuanlan.zhihu.com/p/863378538?share_code=kfC2mCzqjeww&utm_psn=1925889251461031860)
- [ ] [为什么当前主流的大模型都使用 RMS-Norm？](https://zhuanlan.zhihu.com/p/12392406696)
- [ ] [Transformer Pre-Norm 和 Post-Norm 如何选择？](https://zhuanlan.zhihu.com/p/12228475399)
- [ ] [Transformer 似懂非懂的 Norm 方法](https://zhuanlan.zhihu.com/p/12113221623)
- [ ] [LLM预训练MFU计算公式&计算工具构建思路分享（上）](https://mp.weixin.qq.com/s/Og_iggeQBmicg2OWQo2spw)
- [ ] [The Big LLM Architecture Comparison 🌟](https://sebastianraschka.com/blog/2025/the-big-llm-architecture-comparison.html)

MoE：

- [ ] [MoE 中的路由机制 🌟](https://apxml.com/zh/courses/how-to-build-a-large-language-model/chapter-14-advanced-architectural-modifications/routing-mechanisms-moe)
- [ ] [全面解析MoE专家并行EP all-to-all Pytorch算子 🌟](https://zhuanlan.zhihu.com/p/1967192540953425479)

多模态：

- [ ] [Encoder Disaggregation for Scalable Multimodal Model Serving 🌟](https://blog.vllm.ai/2025/12/15/vllm-epd.html)
- [ ] [统一多模态理解与生成综述：83页长文梳理进展和挑战](https://mp.weixin.qq.com/s/MjAzSf-EFG6TlZk_zOQy9Q)

Linear Attention：

- [ ] [Qwen/Kimi带火的LinearAttention：原理与细节解析（上）](https://mp.weixin.qq.com/s/GSplGYVQYU42M-zrazJp8g)
- [ ] [Qwen/Kimi带火的LinearAttention：原理与细节解析（下）](https://mp.weixin.qq.com/s/Z6a1Fq_bnnZSSaVs-rn8vg)
- [ ] [LinearAttention在KV cache的存储上有多大优势？用计算来推演](https://mp.weixin.qq.com/s/LPKzHcHLx2pMMcr2foMsUQ)

DeepSeek：

- [ ] [再读 MLA，还有多少细节是你不知道的 - 猛猿](https://zhuanlan.zhihu.com/p/19585986234?share_code=YaRCFnX257AQ&utm_psn=1931045105759913204)
- [ ] [MLA 结构代码实现及优化](https://www.armcvai.cn/2025-02-10/mla-code.html)
- [ ] [DeepSeek MLA 矩阵吸收浅谈](https://zhuanlan.zhihu.com/p/1888290264377976190)
- [ ] [带你从头发明 MLA](https://zhuanlan.zhihu.com/p/1911795330434986569)
- [ ] [Implementation of Multi-Head Latent Attention (MLA) mechanism](https://github.com/Sailkiki/MLA-DeepSeekV3)
- [ ] [超细图解 MLA 计算流 & 吸收矩阵对比分析](https://zhuanlan.zhihu.com/p/1948769945132470860)
- [ ] [DeepSeek-V3.2-Exp 版本更新，有哪些信息值得关注？](https://www.zhihu.com/question/1956013610666005512/answer/1956104405892969405?share_code=18u0uUzQDSDeP&utm_psn=1956143330766324043)
- [ ] [学习一下 DeepSeek-V3.2](https://mp.weixin.qq.com/s/LYhfpduM72hEJJGe2GFDXw)

Others：

- [ ] [聊聊 Reasoning Model 的精巧实现（ReFT, Kimi K1.5, DeepSeek R1）](https://zhuanlan.zhihu.com/p/20356958978)
- [ ] [从系统 1 到系统 2 推理范式，300+ 文献总结 o1/R1 类推理大模型的技术路线](https://zhuanlan.zhihu.com/p/27230460558)
- [ ] [超全解析！大模型面试宝典 60 题 🌟](https://mp.weixin.qq.com/s/Q6FPYenia1aHl-8e8Nhx4g)

## LLM 推理加速

综述：

- [ ] [3 万字详细解析清华大学最新综述工作：大模型高效推理综述](https://mp.weixin.qq.com/s/U9ESiWehnoKc9SnDz7DVKg)
- [ ] [25 种 LLM 部署框架你知道多少？](https://zhuanlan.zhihu.com/p/1933217002698306629)
- [ ] [大模型推理各方向新发展整理](https://zhuanlan.zhihu.com/p/693680304)
- [ ] [LLM (18)：LLM 的推理优化技术纵览](https://zhuanlan.zhihu.com/p/642412124?utm_psn=1897433318875693188)
- [ ] [3 万字长文！通俗解析大语言模型 LLM 原理](https://mp.weixin.qq.com/s/EX8uQUfZvFbYlrbpiGhd9Q)

基础知识：

- [ ] [LLM 推理基础：采样(Sampling)常见知识概览](https://mp.weixin.qq.com/s/ohUN2SzTfYMCCcH_OgyRKw)
- [ ] [LLM 确定性推理](https://mp.weixin.qq.com/s/5XJNolYVhYTCvI5TjNQrkA)
- [ ] [大模型显存计算公式与优化 🌟](https://mp.weixin.qq.com/s/DLOJwXJfr5cdswRJDMlzhA)
- [ ] [How continuous batching enables 23x throughput in LLM inference while reducing p50 latency](https://www.anyscale.com/blog/continuous-batching-llm-inference)
- [x] [图文详解 Continuous Batch：不写 CUDA Kernel 也能成倍优化推理效率](https://zhuanlan.zhihu.com/p/876908831)
- [x] [AI Infra 之模型显存管理分析](https://mp.weixin.qq.com/s/lNcszOFnGVktBRAAsHDVIA)
- [x] [为什么现在大模型在推理阶段都是左 padding？](https://mp.weixin.qq.com/s/qNSHpmUZrHQsTeXLwSZVCA)

vLLM：

- [ ] [vLLM PIECEWISE CUDA Graph 技术学习笔记](https://zhuanlan.zhihu.com/p/1955402895890560120)
- [ ] [Shared Memory IPC Caching: Accelerating Data Transfer in LLM Inference Systems](https://blog.vllm.ai/2025/11/13/shm-ipc-cache.html)
- [ ] [大模型推理框架，SGLang和vLLM有哪些区别？](https://www.zhihu.com/question/666943660/answer/1914348903668651349?share_code=X0cS0WZalNIH&utm_psn=1980344296080164538)
- [ ] [Ray symmetric-run：让 vLLM 多节点部署更轻盈 🌟](https://mp.weixin.qq.com/s/1cZtyTDr97qLZ40VfOQ5xw)
- [ ] [混合注意力的KV cache该如何设计？框架已给出答案](https://mp.weixin.qq.com/s/Bl3AHM9K9b0ovj8DUe1OqA)
- [ ] [Docker Model Runner 携手 vLLM：让高吞吐推理如江入海](https://mp.weixin.qq.com/s/wGBiGjCuLnJHf3Z7hr25yQ)
- [ ] [vLLM插件系统深度解析：手术刀式代码修改的艺术](https://mp.weixin.qq.com/s/F3V71TfhZqYRnAaayC-E1g)
- [ ] [LMCache + vLLM 实战指南，让大模型的推理速度显著提升！🌟](https://mp.weixin.qq.com/s/J2iyrSjJ7JYjoHoIMAQP4w)
- [ ] [Distributed Inference with vLLM](https://blog.vllm.ai/2025/02/17/distributed-inference.html)
- [ ] [vLLM: Easy, Fast, and Cheap LLM Serving with PagedAttention](https://blog.vllm.ai/2023/06/20/vllm.html)
- [ ] [图解大模型计算加速系列之：vLLM 核心技术 PagedAttention 原理](https://zhuanlan.zhihu.com/p/691038809)
- [ ] [图解 vLLM V1 系列 7：使用 AsyncLLM 做异步推理](https://zhuanlan.zhihu.com/p/1916187125931554299)
- [ ] [图解 vLLM V1 系列 6：KVCacheManager 与 PrefixCaching](https://zhuanlan.zhihu.com/p/1916181593229334390)
- [ ] [PagedAttention 论文新鲜出炉](https://zhuanlan.zhihu.com/p/617015570)
- [ ] [图解 vLLM Prefix Prefill Triton Kernel](https://zhuanlan.zhihu.com/p/695799736?share_code=Hz1PZDdfXLy7&utm_psn=1900943218725598209)
- [ ] [vLLM 的 prefix cache 为何零开销](https://zhuanlan.zhihu.com/p/1896927732027335111)
- [ ] [原理 & 图解 vLLM Automatic Prefix Cache (RadixAttention): 首 Token 时延优化](https://zhuanlan.zhihu.com/p/693556044)
- [ ] [vLLM + DeepSeek-R1 671B 多机部署及修 Bug 笔记](https://zhuanlan.zhihu.com/p/29950052712)
- [ ] [vLLM 源码之 PageAttention](https://zhuanlan.zhihu.com/p/711304830)
- [ ] [被问懵了！腾讯面试官让我手写 PagedAttention](https://zhuanlan.zhihu.com/p/1911455737118457997?share_code=9fRcELOowc4U&utm_psn=1912436101039226918)
- [ ] [三行代码提升一倍 vllm 性能](https://zhuanlan.zhihu.com/p/1946143263867241420)
- [x] [vLLM调度器解密（上）：Continuous Batch 是如何工作的？](https://zhuanlan.zhihu.com/p/1117099341?share_code=3OZ9bBQsRAHV&utm_psn=1909578321869637005)
- [x] [vLLM调度器解密（下）：chunked prefill是如何进一步优化的？](https://zhuanlan.zhihu.com/p/6144374775?share_code=w9CKto9eLSq2&utm_psn=1909578492246466702)

并行策略：

- [ ] [全面解析 MoE 专家并行 EP all-to-all 算子 🌟](https://mp.weixin.qq.com/s/yFy9pKTcMfB4ciLF4uEs1g)
- [ ] [大模型数据并行 - DP、DDP 和 FSDP](https://zhuanlan.zhihu.com/p/28960311154)
- [ ] [梳理 SGLang 中 DP Attention 及其 Padding 问题](https://mp.weixin.qq.com/s/W0e6W4-v8PmzP10qXY71rQ)
- [ ] [大规模 EP 优化](https://zhuanlan.zhihu.com/p/1944715179439924643?share_code=oSqHiWjct1QC&utm_psn=1946182101884965795)
- [ ] [PP, TP, DP, ZeRO 一点理解](https://zhuanlan.zhihu.com/p/19480848641?share_code=1mdHLZXyhVgPd&utm_psn=1946517314418746403)
- [ ] [The Ultra-Scale Playbook: Training LLMs on GPU Clusters](https://huggingface.co/spaces/nanotron/ultrascale-playbook?section=high-level_overview)
- [ ] [LLM 推理并行优化的必备知识 🌟](https://zhuanlan.zhihu.com/p/1937449564509545940)
- [ ] [分布式推理优化思路 🌟](https://zhuanlan.zhihu.com/p/1937556222371946860)

Speculative Decoding：

- [ ] [搞懂投机推理难？这篇总结+框架实践帮你快速上手](https://mp.weixin.qq.com/s/bnRFKs6EBfBojhgG-wSczQ)
- [ ] [Speculative Decoding: 总结、分析、展望](https://zhuanlan.zhihu.com/p/1904881828906668879?share_code=hDIX8nBBfJOQ&utm_psn=1918275277408142518)
- [ ] [Speculators：生产级训练投机解码的标准化方案](https://mp.weixin.qq.com/s/ndmaREajMMWu79dyZeJj1w)

PD/AF 分离：

- [ ] [Step3 大模型 AE 分离的量化解析，兼谈 DeepSeek 为何不分离](https://mp.weixin.qq.com/s/8zGNWlEyZISgJXm9ygg_1w)
- [ ] [0.5x 提升：PD 分离 KV cache 传输的实践经验](https://zhuanlan.zhihu.com/p/1946608360259577576)
- [ ] [vLLM 的 PD 分离：作用、使用和实现](https://zhuanlan.zhihu.com/p/1929881199985263900?share_code=19q5ciyJnpgSQ&utm_psn=1930968089199088481)
- [ ] [通过 PD 分离实现 LLM 服务中的最大有效吞吐量](https://mp.weixin.qq.com/s/Zprd13tHXfUVRs21eKtMWQ)
- [ ] [LLM 推理优化：MLA 算力均衡实践（提升 0.3x）](https://mp.weixin.qq.com/s/Hl0D3OJ1nk-4ANgUAoZPNw)

## PyTorch

- [ ] [torch.compile 🌟](./Open_Source/Projects/vLLM/Features/Graph_Mode/README.md)
- [ ] [PyTorch internals 🌟](http://blog.ezyang.com/2019/05/pytorch-internals/)
- [ ] [PyTorch dispatcher 🌟](http://blog.ezyang.com/2020/09/lets-talk-about-the-pytorch-dispatcher/)
- [ ] [PyTorch 显存管理介绍与源码解析（二）](https://zhuanlan.zhihu.com/p/681651660)
- [ ] [PyTorch 显存管理介绍与源码解析（三）](https://zhuanlan.zhihu.com/p/692614846)

## CUDA

- [ ] [从GPU卡死到精准锁定出错代码：vLLM CUDA 调试实战技巧](https://mp.weixin.qq.com/s/VHFnA9nkasOJ-svIFp7IXQ)
- [ ] [大模型计算/通信overlapped kernel(一)--动机和概念](https://zhuanlan.zhihu.com/p/1930681680127047148)
- [ ] [大模型计算/通信overlapped kernel(二)--AllGather+Gemm/Gemm+ReduceScatter](https://zhuanlan.zhihu.com/p/1948749443802313796)
- [ ] [原理 & 图解 FlashDecoding/FlashDecoding++](https://zhuanlan.zhihu.com/p/696075602)
- [ ] [原理篇: 从 Online-Softmax 到 FlashAttention V1/V2/V3](https://zhuanlan.zhihu.com/p/668888063)
- [ ] [图解大模型计算加速系列：FlashAttention V1，从硬件到计算逻辑](https://zhuanlan.zhihu.com/p/669926191)
- [ ] [图解大模型计算加速系列：FlashAttention V2，从原理到并行计算](https://zhuanlan.zhihu.com/p/691067658)
- [ ] [Flash Attention 学习过程详解](https://www.bilibili.com/video/BV1FM9XYoEQ5/?buvid=XX09F7F01632BC5A89A856F4746F7484F93FD&from_spmid=main.space-contribution.0.0&is_story_h5=false&mid=7x3gpfL5LaqpE%2FSuWLsT4A%3D%3D&p=7&plat_id=116&share_from=ugc&share_medium=android&share_plat=android&share_session_id=164cfebd-b6f9-4d1a-9140-6cea76c26bb1&share_source=WEIXIN&share_tag=s_i&spmid=united.player-video-detail.0.0&timestamp=1758016444&unique_k=Sv4E1Ev&up_id=218427631&vd_source=2754a9b73cb316d2cad8eb1195f5aa23)
- [ ] [20行代码入门PyTorch自定义CUDA/C++](https://mp.weixin.qq.com/s/yJIjuT2Hsjg4dY10fqjUWQ)
- [ ] [20 行代码入门 PyTorch 自定义 CUDA/C++](https://zhuanlan.zhihu.com/p/579395211)
- [ ] [CUDA 编程：常用技巧/方法](https://zhuanlan.zhihu.com/p/584501634)
- [ ] [大语言模型所有算子逻辑](https://zhuanlan.zhihu.com/p/1909996866432668841?share_code=u4D2wlKwNjAp&utm_psn=1911112698021807580)
- [ ] [CUDA Core Dump: An Effective Tool to Debug Memory Access Issues and Beyond](https://blog.vllm.ai/2025/08/11/cuda-debugging.html)
- [ ] [高频面试题汇总-大模型手撕 CUDA](https://zhuanlan.zhihu.com/p/678903537)
- [ ] [LeetCUDA: v3.0 大升级-面试刷题不迷路](https://zhuanlan.zhihu.com/p/19862356369)
- [ ] [[MLSys 入门向读书笔记] CUDA by Example: An Introduction to General-Purpose GPU Programming（下）🌟](https://zhuanlan.zhihu.com/p/718988880)
- [ ] [[MLSys 入门向读书笔记] CUDA by Example: An Introduction to General-Purpose GPU Programming（上）🌟](https://zhuanlan.zhihu.com/p/709427098)
- [ ] [高性能计算方向面试问题总结](https://zhuanlan.zhihu.com/p/721562983)
- [ ] [pybind 11 doc](https://pybind11.readthedocs.io/en/stable/index.html)
- [ ] [CUDA 练手小项目 — Parallel Prefix Sum (Scan)](https://zhuanlan.zhihu.com/p/661460705?share_code=pseQOXxySVcl&utm_psn=1902627229709624968)
- [ ] [手撕 CUDA 算子：高频面试题汇总~](https://mp.weixin.qq.com/s/kSiQZGTumV1QkUhjQKB6Qg)
- [ ] [CUDA 算子手撕与面试](https://zhuanlan.zhihu.com/p/12661298743?share_code=19eWXGr1v72R0&utm_psn=1920624157227450744)
- [ ] [CUDA 性能分析工具](https://zhuanlan.zhihu.com/p/1911179137357419017)
- [ ] [CUDA 新手性能分析参考](https://zhuanlan.zhihu.com/p/1911511031525646518)
- [ ] [Cmake for CUDA](https://cliutils.gitlab.io/modern-cmake/chapters/packages/CUDA.html)
- [ ] [有没有一本讲解 gpu 和 CUDA 编程的经典入门书籍？ - JerryYin777 的回答 - 知乎](https://www.zhihu.com/question/26570985/answer/3465784970)
- [ ] [CUDA 学习资料及路线图](https://zhuanlan.zhihu.com/p/273607744)
- [ ] [推荐几个不错的 CUDA 入门教程](https://zhuanlan.zhihu.com/p/346910129?utm_psn=1891290780615820759)
- [ ] [熬了几个通宵，我写了份 CUDA 新手入门代码（pytorch 自定义算子）](https://zhuanlan.zhihu.com/p/360441891?utm_psn=1891290523299472507)
- [x] [CUDA 全局内存高效访问——对齐访问和合并内存访问](https://zhuanlan.zhihu.com/p/1921229353515189126?share_code=1eLd5u0xlqUqc&utm_psn=1921481712455624310)
- [x] [CUDA 编程之 Memory Coalescing](https://zhuanlan.zhihu.com/p/300785893)

## Research

- [ ] [当 CEO 重读 PhD：论智慧与勇气 🌟](https://zhuanlan.zhihu.com/p/1893638374646079902)
- [ ] [有哪些读博读废了的案例？🌟](https://www.zhihu.com/question/281702832/answer/1898063916208230560)
- [ ] [最全 AI Infra主题的 顶会award论文解读（MLSys、EuroSys HPCA、ASPLOS、SC、 ICLR、ICML、MICRO、 ISCA、OSDI、SOSP）🌟](https://zhuanlan.zhihu.com/p/1976227735400638099?share_code=YFzaBqPJ6BET&utm_psn=1977349340990309748)
- [ ] [Writing AI Conference Papers: A Handbook for Beginners（GitHub）](https://github.com/hzwer/WritingAIPaper)
- [ ] [读博那些事儿](https://zhuanlan.zhihu.com/p/82579410?share_code=1mKWQMYAGFAZ6&utm_psn=1922088666123198736)
- [ ] [CS 读博总结和建议文章（合集）🌟](https://zhuanlan.zhihu.com/p/347223193)
- [ ] [Awesome Reading Material for phd](https://galeselee.gitbook.io/awesome-papers/awesome_reading_material)
- [ ] [Doing The PhD](https://github.com/shengyp/doing_the_PhD)
- [ ] [博士申请套磁信的撰写模板](https://mp.weixin.qq.com/s/u1YtAqHKmFrfpG1KI_lBSg)
- [ ] [如何高效阅读学术论文，快速抓住核心信息？](https://www.zhihu.com/question/1984218203400979561/answer/1985100908015678961)

## Others

- [ ] [TileLang+TVM-FFI: 极致压榨CPU性能](https://zhuanlan.zhihu.com/p/1982449607435903684?share_code=1namqWxZu7LC4&utm_psn=1982944873624663147)
- [ ] [面向 ML 玩家的 Docker 零帧起手 🌟](https://zhuanlan.zhihu.com/p/1916764175230801287?share_code=FFpFk5rroxTE&utm_psn=1918221276146800528)
- [ ] [Nsight Systems 工具原理与 GPU 性能优化实战详解](https://zhuanlan.zhihu.com/p/1966508767869932083?share_code=UqBOEuwTgHss&utm_psn=1967250648773928246)
- [ ] [Visual Studio Code 配置 C/C++ 开发环境的最佳实践(VSCode + Clangd + XMake)](https://zhuanlan.zhihu.com/p/398790625)
- [ ] 云服务器租赁平台：优云智算
- [ ] [python实现异步的底层原理是什么？🌟](https://www.zhihu.com/question/432814091/answer/1969082606936110671)

---

## 学习资料

**博客专栏：**

- [vLLM Blog](https://blog.vllm.ai/)
- [Jay Alammar's blog](https://jalammar.github.io/)
- [猛猿的知乎](https://zhuanlan.zhihu.com/p/654910335)
- [转行大模型工程师 - 黄哲威](https://zhuanlan.zhihu.com/p/1916911329987503232?share_code=vlp9og7xKQt&utm_psn=1922571024798552459)
- [100+ 高性能计算与分布式技术博客 - DefTruth](https://github.com/xlite-dev/LeetCUDA?tab=readme-ov-file#-100-%E9%AB%98%E6%80%A7%E8%83%BD%E8%AE%A1%E7%AE%97%E4%B8%8E%E5%88%86%E5%B8%83%E5%BC%8F-%E6%8A%80%E6%9C%AF%E5%8D%9A%E5%AE%A2)
- [zhang's Blog](https://www.armcvai.cn/categories.html)
- [ezyang's Blog](http://blog.ezyang.com/archives/)
- [Research Journey](https://xtra-computing.github.io/raintreebook/)
- [How to Scale Your Model](https://jax-ml.github.io/scaling-book/)
- [Insu Jang's Blog](https://insujang.github.io/posts/)

**论文合集：**

- [LLM 推理论文合集](https://zhuanlan.zhihu.com/p/669777159)
- [460 篇多模态大语言模型论文合集](https://rcncqctdyl3f.feishu.cn/docx/QyFZd3ig3oiCPgxAvXhc1xPYnch)
- [大模型推理必看！2025最值得读的14篇论文和2篇博客 🌟](https://mp.weixin.qq.com/s/dg8FGNZ0mZOxF4b5fwUxkw)

**自学课程：**

- [傅傅猪 vllm 课程](https://space.bilibili.com/1822828582)
- Stanford cs336
  - 视频：b 站
  - 课件：[link](https://github.com/stanford-cs336/spring2025-lectures)
- GPU-mode（GitHub）
- [面向 AI-Infra 的 Cuda 零基础入门](https://tvle9mq8jh.feishu.cn/docx/BnqMdyaJ9oyXb1xwktgc7esMn4c)
- [LeetCode 刷题路线图](https://labuladong.online/algo/intro/quick-learning-plan/)

**访谈：**

- [OpenAI 核心贡献者翁家翌访谈：想法很廉价，模型的成败全在 Infra](https://mp.weixin.qq.com/s/iTx0DnFfjikgRfzcxNRcqw)

---
**[NVIDIA Developer](https://developer.nvidia.com/)**

**Docs:**

- [ ] [An Even Easier Introduction to CUDA (Updated)](https://developer.nvidia.com/blog/even-easier-introduction-cuda/)
- [x] [NVIDIA CUDA-X Libraries](https://developer.nvidia.com/gpu-accelerated-libraries)
  - [ ] [CUTLASS](https://docs.nvidia.com/cutlass/index.html)
  - [ ] [TensorRT](https://developer.nvidia.com/tensorrt)
  - [ ] [cuDNN](https://developer.nvidia.com/cudnn)
- [x] [NVIDIA Magnum IO](https://www.nvidia.com/en-us/data-center/magnum-io/)
  - [x] [GPUDirect](https://developer.nvidia.com/gpudirect)
    - [x] [GPUDirect Storage](https://developer.nvidia.com/gpudirect-storage)
    - [ ] [GPUDirect RDMA](https://docs.nvidia.com/cuda/gpudirect-rdma/?ncid=no-ncid)
    - [ ] GPUDirect P2P
    - [ ] GPUDirect Video
  - [x] [NVSHMEM](https://developer.nvidia.com/nvshmem)
    - [ ] [NVSHMEM Introduction](https://docs.nvidia.com/nvshmem/api/introduction.html)
    - [ ] IBGDA (InfiniBand GPUDirect Async)
  - [ ] NCCL

**Blogs:**

- [ ] [Getting Started with CUDA Graphs](https://developer.nvidia.com/blog/cuda-graphs/)
- [ ] [Accelerating IO in the Modern Data Center: Magnum IO Architecture](https://developer.nvidia.com/blog/accelerating-io-in-the-modern-data-center-magnum-io-architecture?ncid=no-ncid)
- [ ] [GPUDirect Storage: A Direct Path Between Storage and GPU Memory](https://developer.nvidia.com/blog/gpudirect-storage/?ncid=no-ncid)
- [x] [Scaling Scientific Computing with NVSHMEM](https://developer.nvidia.com/blog/scaling-scientific-computing-with-nvshmem/)
- [x] [IBGDA: Improving Network Performance of HPC Systems Using NVIDIA Magnum IO NVSHMEM and GPUDirect Async](https://developer.nvidia.com/blog/improving-network-performance-of-hpc-systems-using-nvidia-magnum-io-nvshmem-and-gpudirect-async/)

> Find More: [NV Blogs: Inference Performance](https://developer.nvidia.com/blog/tag/inference-performance/)

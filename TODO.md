# TODO

---

## LLM 基础

- [ ] [The Unreasonable Effectiveness of Recurrent Neural Networks](https://karpathy.github.io/2015/05/21/rnn-effectiveness/)
- [ ] [The Illustrated Word2vec - Jay Alammar](https://jalammar.github.io/illustrated-word2vec/)
- [ ] [再读 MLA，还有多少细节是你不知道的](https://zhuanlan.zhihu.com/p/19585986234?share_code=YaRCFnX257AQ&utm_psn=1931045105759913204)
- [ ] [深入理解 AWQ 量化技术](https://zhuanlan.zhihu.com/p/697761176)
- [ ] [避开复数推导，我们还可以怎么理解 RoPE？](https://zhuanlan.zhihu.com/p/863378538?share_code=kfC2mCzqjeww&utm_psn=1925889251461031860)
- [ ] [为什么当前主流的大模型都使用 RMS-Norm？](https://zhuanlan.zhihu.com/p/12392406696)
- [ ] [Transformer Pre-Norm 和 Post-Norm 如何选择？](https://zhuanlan.zhihu.com/p/12228475399)
- [ ] [Transformer 似懂非懂的 Norm 方法](https://zhuanlan.zhihu.com/p/12113221623)
- [ ] [MLA 结构代码实现及优化](https://www.armcvai.cn/2025-02-10/mla-code.html)
- [ ] [阿里通义千问 Qwen3 系列模型正式发布，该模型有哪些技术亮点？](https://www.zhihu.com/question/1900300358229652607/answer/1900452232018767979)
- [ ] [deepseek 技术解读(1) - 彻底理解 MLA（Multi-Head Latent Attention）](https://zhuanlan.zhihu.com/p/16730036197)
- [ ] [deepseek 技术解读(2) - MTP（Multi-Token Prediction）的前世今生](https://zhuanlan.zhihu.com/p/18056041194)
- [ ] [deepseek 技术解读(3) - MoE的演进之路](https://zhuanlan.zhihu.com/p/18565423596)
- [ ] [聊聊 Reasoning Model 的精巧实现（ReFT, Kimi K1.5, DeepSeek R1）](https://zhuanlan.zhihu.com/p/20356958978)
- [ ] [从系统 1 到系统 2 推理范式，300+ 文献总结 o1/R1 类推理大模型的技术路线](https://zhuanlan.zhihu.com/p/27230460558)
- [ ] [超全解析！大模型面试宝典 60 题](https://mp.weixin.qq.com/s/Q6FPYenia1aHl-8e8Nhx4g)

**已阅读：**

- [x] [The Illustrated Transformer - Jay Alammar ⭐](https://jalammar.github.io/illustrated-transformer/)
- [x] [The Illustrated GPT-2 (Visualizing Transformer Language Models) - Jay Alammar ⭐](https://jalammar.github.io/illustrated-gpt2/)

## 多模态

- [ ] [多模态技术梳理：ViT 系列](https://zhuanlan.zhihu.com/p/26719287825)
- [ ] [Qwen2-VL 源码解读：从准备一条样本到模型生成全流程图解](https://zhuanlan.zhihu.com/p/28205969434)
- [ ] [多模态技术梳理：Qwen-VL 系列](https://zhuanlan.zhihu.com/p/25267823390)
- [ ] [Qwen2.5-VL 论文](https://arxiv.org/abs/2502.13923)
- [ ] [Qwen2.5-VL 代码](https://github.com/QwenLM/Qwen2.5-VL)
- [ ] [Qwen2.5-VL transformer modeling](https://github.com/huggingface/transformers/blob/41925e42135257361b7f02aa20e3bbdab3f7b923/src/transformers/models/qwen2_5_vl/modeling_qwen2_5_vl.py)
- [ ] [torchcodec](https://github.com/pytorch/torchcodec)

**已阅读：**

- [x] [ViT 论文速读 ⭐](https://www.armcvai.cn/2024-09-08/vit-paper.html)
- [x] [ViT 解读](https://datawhalechina.github.io/thorough-pytorch/%E7%AC%AC%E5%8D%81%E7%AB%A0/ViT%E8%A7%A3%E8%AF%BB.html)
- [x] [ViT pytorch 实现](https://github.com/lucidrains/vit-pytorch/blob/main/vit_pytorch/vit.py)
- [x] [LLaVA 系列模型结构详解 ⭐](https://www.armcvai.cn/2024-11-28/llava-structure.html)
- [x] [万字长文图解 Qwen2.5-VL 实现细节 ⭐](https://zhuanlan.zhihu.com/p/1921289925552210138?share_code=oQnxmXt37SUD&utm_psn=1921301797538076351)

## LLM 推理加速

- [ ] [PagedAttention 论文新鲜出炉](https://zhuanlan.zhihu.com/p/617015570)
- [ ] [大模型推理各方向新发展整理](https://zhuanlan.zhihu.com/p/693680304)
- [ ] [原理 & 图解 FlashDecoding/FlashDecoding++](https://zhuanlan.zhihu.com/p/696075602)
- [ ] [原理篇: 从 Online-Softmax 到 FlashAttention V1/V2/V3](https://zhuanlan.zhihu.com/p/668888063)
- [ ] [原理 & 图解 vLLM Automatic Prefix Cache (RadixAttention): 首 Token 时延优化](https://zhuanlan.zhihu.com/p/693556044)
- [ ] [vLLM + DeepSeek-R1 671B 多机部署及修 Bug 笔记](https://zhuanlan.zhihu.com/p/29950052712)
- [ ] [How continuous batching enables 23x throughput in LLM inference while reducing p50 latency](https://www.anyscale.com/blog/continuous-batching-llm-inference)
- [ ] [25 种 LLM 部署框架你知道多少？](https://zhuanlan.zhihu.com/p/1933217002698306629)
- [ ] [DeepSeek V3/R1 推理效率分析: 满血版逆向工程分解](https://mp.weixin.qq.com/s/oa7ZW7qNW-B05K_iF5YNYA)（PD 分离解析）
- [ ] [通过 PD 分离实现 LLM 服务中的最大有效吞吐量](https://mp.weixin.qq.com/s/Zprd13tHXfUVRs21eKtMWQ)
- [ ] [vLLM 的 PD 分离：作用、使用和实现](https://zhuanlan.zhihu.com/p/1929881199985263900?share_code=19q5ciyJnpgSQ&utm_psn=1930968089199088481)
- [ ] [vLLM 源码之 PageAttention](https://zhuanlan.zhihu.com/p/711304830)
- [ ] [面向 ML 玩家的 Docker 零帧起手](https://zhuanlan.zhihu.com/p/1916764175230801287?share_code=FFpFk5rroxTE&utm_psn=1918221276146800528)
- [ ] [Speculative Decoding: 总结、分析、展望](https://zhuanlan.zhihu.com/p/1904881828906668879?share_code=hDIX8nBBfJOQ&utm_psn=1918275277408142518)
- [ ] [vLLM 推理引擎深入浅出 - 知乎专栏](https://www.zhihu.com/column/c_1397348083538153472)
- [ ] [图解 vLLM V1 系列 7：使用 AsyncLLM 做异步推理](https://zhuanlan.zhihu.com/p/1916187125931554299)
- [ ] [图解 vLLM V1 系列 6：KVCacheManager 与 PrefixCaching](https://zhuanlan.zhihu.com/p/1916181593229334390)
- [ ] [大语言模型所有算子逻辑](https://zhuanlan.zhihu.com/p/1909996866432668841?share_code=u4D2wlKwNjAp&utm_psn=1911112698021807580)
- [ ] [被问懵了！腾讯面试官让我手写 PagedAttention](https://zhuanlan.zhihu.com/p/1911455737118457997?share_code=9fRcELOowc4U&utm_psn=1912436101039226918)
- [ ] [vLLM调度器解密（上）：Continuous Batch 是如何工作的？](https://zhuanlan.zhihu.com/p/1117099341?share_code=3OZ9bBQsRAHV&utm_psn=1909578321869637005)
- [ ] [vLLM调度器解密（下）：chunked prefill是如何进一步优化的？](https://zhuanlan.zhihu.com/p/6144374775?share_code=w9CKto9eLSq2&utm_psn=1909578492246466702)
- [ ] [3 万字详细解析清华大学最新综述工作：大模型高效推理综述](https://mp.weixin.qq.com/s/U9ESiWehnoKc9SnDz7DVKg)
- [ ] [vLLM 框架 V1 演进分析](https://zhuanlan.zhihu.com/p/1894423873145004335)
- [ ] [LLM (18)：LLM 的推理优化技术纵览](https://zhuanlan.zhihu.com/p/642412124?utm_psn=1897433318875693188)
- [ ] [图文详解 LLM inference：KV Cache](https://zhuanlan.zhihu.com/p/1893220743053030641?utm_psn=1897576305303721590)
- [ ] [大模型推理 - 为什么要 PD 分离？](https://zhuanlan.zhihu.com/p/1897270081664300462?utm_psn=1897629970966217092)
- [ ] [图解 vLLM Prefix Prefill Triton Kernel](https://zhuanlan.zhihu.com/p/695799736?share_code=Hz1PZDdfXLy7&utm_psn=1900943218725598209)
- [ ] [vLLM 的 prefix cache 为何零开销](https://zhuanlan.zhihu.com/p/1896927732027335111)
- [ ] [prefill 和 decode 该分离到不同的卡上么？](https://zhuanlan.zhihu.com/p/1280567902?share_code=z1ij3mzQpXAE&utm_psn=1902828634068226129)
- [ ] [vLLM PD 分离 KV cache 传递机制详解与演进分析](https://zhuanlan.zhihu.com/p/1906741007606878764?share_code=1m2xkCswqTA9N&utm_psn=1907185030842782292)
- [ ] [vLLM PD 分离方案浅析](https://zhuanlan.zhihu.com/p/1889243870430201414?utm_psn=1889596220076426760)
- [ ] [<u>清华大模型推理综述</u>](https://mp.weixin.qq.com/s/U9ESiWehnoKc9SnDz7DVKg)

**已阅读：**

基础知识：

vLLM 源码解析：

- [x] [图解 vLLM V1 系列 1：整体流程 ⭐](https://zhuanlan.zhihu.com/p/1900126076279160869?share_code=18FtZ4wqQM3hR&utm_psn=1900940137866716878)
- [x] [图解 vLLM V1 系列 2：Executor-Workers 架构 ⭐](https://zhuanlan.zhihu.com/p/1900613601577899465)
- [x] [图解 vLLM V1 系列 3：KV Cache 初始化](https://zhuanlan.zhihu.com/p/1900932850829730567)
- [x] [图解 vLLM V1 系列 4：加载模型权重（load_model）](https://zhuanlan.zhihu.com/p/1908151478557839879?share_code=RlCt8lDNStds&utm_psn=1912310198112059517)
- [x] [图解 vLLM V1 系列 5：调度器策略（Scheduler）⭐](https://zhuanlan.zhihu.com/p/1908153627639551302?share_code=02jBOS1PfJxF&utm_psn=1912310252315079385)
- [x] [vLLM V1 源码阅读](https://zhuanlan.zhihu.com/p/32045324831)（V1 全流程讲解，很细节）
- [x] [vLLM V1 Scheduler 的调度逻辑 & 优先级分析](https://zhuanlan.zhihu.com/p/1900957007575511876?share_code=o9ZDfDnEpemP&utm_psn=1901069245619635086)
- [x] [vLLM 模型权重加载：使用 setattr](https://zhuanlan.zhihu.com/p/714531623?utm_psn=1916989579635975888)
- [x] [vLLM 算子开发流程：“保姆级”详细记录 ⭐](https://zhuanlan.zhihu.com/p/1892966682634473987?share_code=1lbfAKTh5A2Vr&utm_psn=1913354916832997933)（如何向 vLLM 提交一个优化 kernel 的 PR）
- [x] [vLLM 显存管理详解](https://zhuanlan.zhihu.com/p/1916529253169734444?share_code=aePDPg2VonBo&utm_psn=1917144770171606655)

其它：

- [x] [AI Infra 之模型显存管理分析](https://mp.weixin.qq.com/s/lNcszOFnGVktBRAAsHDVIA)（计算显存、推理时延评估方法）

---

## CUDA

- [ ] [高频面试题汇总-大模型手撕 CUDA](https://zhuanlan.zhihu.com/p/678903537)
- [ ] [LeetCUDA: v3.0 大升级-面试刷题不迷路](https://zhuanlan.zhihu.com/p/19862356369)
- [ ] [[MLSys 入门向读书笔记] CUDA by Example: An Introduction to General-Purpose GPU Programming（下）](https://zhuanlan.zhihu.com/p/718988880)
- [ ] [[MLSys 入门向读书笔记] CUDA by Example: An Introduction to General-Purpose GPU Programming（上）](https://zhuanlan.zhihu.com/p/709427098)
- [ ] [高性能计算方向面试问题总结](https://zhuanlan.zhihu.com/p/721562983)
- [ ] [pybind 11 doc](https://pybind11.readthedocs.io/en/stable/index.html)
- [ ] [DiT推理加速综述: Caching](https://zhuanlan.zhihu.com/p/711223667?share_code=14mIAOoTFRYPB&utm_psn=1923895335429832738)
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

**已阅读：**

- [x] [从啥也不会到 CUDA GEMM 优化 ⭐](https://zhuanlan.zhihu.com/p/703256080)（深入分析了什么是 bank conflict）
- [x] [CUDA 内核优化策略 ⭐](https://www.armcvai.cn/2024-08-25/cuda-kernel.html)（全面介绍了各类优化手段，**实际案例：TODO**）
- [x] [CUDA 全局内存高效访问——对齐访问和合并内存访问](https://zhuanlan.zhihu.com/p/1921229353515189126?share_code=1eLd5u0xlqUqc&utm_psn=1921481712455624310)
- [x] [CUDA 编程之 Memory Coalescing](https://zhuanlan.zhihu.com/p/300785893)

## PyTorch

- [ ] [PyTorch internals](http://blog.ezyang.com/2019/05/pytorch-internals/)
- [ ] [PyTorch dispatcher](http://blog.ezyang.com/2020/09/lets-talk-about-the-pytorch-dispatcher/)
- [ ] [PyTorch 显存管理介绍与源码解析（一）](https://zhuanlan.zhihu.com/p/680769942)
- [ ] [PyTorch 显存管理介绍与源码解析（二）](https://zhuanlan.zhihu.com/p/681651660)
- [ ] [PyTorch 显存管理介绍与源码解析（三）](https://zhuanlan.zhihu.com/p/692614846)
- [ ] [PyTorch 显存可视化与 Snapshot 数据分析](https://zhuanlan.zhihu.com/p/677203832)

**已阅读：**

- [x] [Accelerating PyTorch with CUDA Graphs](https://pytorch.org/blog/accelerating-pytorch-with-cuda-graphs/)
- [x] [PyTorch 图模式技术概览](https://zhuanlan.zhihu.com/p/1921889729026172253)

---

## 科研

- [ ] [Research Taste Exercises](https://colah.github.io/notes/taste/)
- [ ] [锻炼研究品味的种种方法](https://zhuanlan.zhihu.com/p/1904764443784647788)
- [ ] [论文阅读模板](https://wentao.site/flash_attention_v3_summary/)
- [ ] [科研能力是指什么能力？](https://www.zhihu.com/question/60042037/answer/3601970421)
- [ ] [Writing AI Conference Papers: A Handbook for Beginners「黄哲威」](https://github.com/hzwer/WritingAIPaper)
- [ ] [读博那些事儿](https://zhuanlan.zhihu.com/p/82579410?share_code=1mKWQMYAGFAZ6&utm_psn=1922088666123198736)
- [ ] [《Instructions for PhD Students》：Dimitris 给 PhD 学生的忠告](https://zhuanlan.zhihu.com/p/400248999?share_code=1a4eZuv2CLy0K&utm_psn=1920625675238372098)
- [ ] [怎么知道自己适不适合读博？](https://www.zhihu.com/question/13724964306?share_code=1ocLZTWwEgGkz&utm_psn=1920484065313821698)
- [ ] [写在 Ph.D 第 0 年：AI/CV 科研菜鸟的持续进阶之路](https://zhuanlan.zhihu.com/p/960781637?share_code=13GKbPaHvl60E&utm_psn=1904443459802206715)
- [ ] [CS 读博总结和建议文章](https://zhuanlan.zhihu.com/p/347223193)
- [ ] [科研大牛们怎么读文献？](https://www.zhihu.com/question/21278186/answer/1269255636)
- [ ] [Awesome Reading Material for phd](https://galeselee.gitbook.io/awesome-papers/awesome_reading_material)
- [ ] [Doing The PhD](https://github.com/shengyp/doing_the_PhD)

---

## 其它

- [ ] [Visual Studio Code 配置 C/C++ 开发环境的最佳实践(VSCode + Clangd + XMake)](https://zhuanlan.zhihu.com/p/398790625)

---

**博客专栏：**

- [Jay Alammar's blog](https://jalammar.github.io/)
- [猛猿的知乎](https://zhuanlan.zhihu.com/p/654910335)
- [转行大模型工程师 - 黄哲威](https://zhuanlan.zhihu.com/p/1916911329987503232?share_code=vlp9og7xKQt&utm_psn=1922571024798552459)
- [100+ 高性能计算与分布式技术博客 - DefTruth](https://github.com/xlite-dev/LeetCUDA?tab=readme-ov-file#-100-%E9%AB%98%E6%80%A7%E8%83%BD%E8%AE%A1%E7%AE%97%E4%B8%8E%E5%88%86%E5%B8%83%E5%BC%8F-%E6%8A%80%E6%9C%AF%E5%8D%9A%E5%AE%A2)
- [insujang.github.io](https://insujang.github.io/posts/)（LLM 推理技术）
- [zhang's Blog](https://www.armcvai.cn/categories.html)
- [ezyang's Blog](http://blog.ezyang.com/archives/)
- 月球大叔（b站/小红书）
- GPU-mode（GitHub）
- [LLM 推理论文合集](https://zhuanlan.zhihu.com/p/669777159)
- [460 篇多模态大语言模型论文合集](https://rcncqctdyl3f.feishu.cn/docx/QyFZd3ig3oiCPgxAvXhc1xPYnch)

---

**学习课程：**

- Stanford cs336
- [面向 AI-Infra 的 Cuda 零基础入门](https://tvle9mq8jh.feishu.cn/docx/BnqMdyaJ9oyXb1xwktgc7esMn4c)

---

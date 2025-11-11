# vllm-ascend issue 处理指南

# 1 问题定位
## 1.1 环境信息检查
- vllm-ascend发布的官方镜像
- vllm-ascend和vllm 版本的配套是否正确
- Ascend的配套是否正确
  -  CANN版本｜Torch NPU和Pytorch版本｜NPU型号｜Driver版本

## 1.2 错误信息查看
- 错误堆栈中初始报错点信息（一般是根，其他报错点只是出错后的连锁反应）
- 错误信息缺失/不详细 =》@作者补充信息（越早越好，时间就作者可能转投其他事项/环境没了）

## 1.3 根因定位
根因定位过程
- 已存在问题、历史已处理问题确认
  - vllm-ascend和vllm社区搜索问题关键字
  - 组内求助是否有人遇到过
- main分支尝试复现
  - 无法复现
    - 使用不正确？ =》 $$\color{red}{文档可以改进？}$$
    - 不能复现 =》给出正确的版本、执行过程/命令、评论到Issue让提出者进行尝试
  - 可以复现
    - 有PR在处理？=》 $$\color{red}{改进}$$
    - 无PR在处理，需要解决 =》 $$\color{red}{改进}$$
    - 根因定位技巧：
      - 报错的相关的代码最近是否有改动，是否是改动导致的？
      - 日志定位
        * 添加怀疑点日志打印
        * 调整vllm的志级别
          ```
          export VLLM_LOGGING_LEVEL=DEBUG
          export VLLM_DEBUG_INIT=1
          export VLLM_TRACE_FUNCTION=1
          ```
        * NPU host端日志
          ```
          需要查看每次运行时进程号对应的日志（一次运行存在多个子进程）,目录如下：
          错误日志：/root/ascend/log/debug/plog/ 
          运行日志：/root/ascend/log/run/plog/
          ```
        * 调整Ascend日志级别
          ```
          export ASCEND_GLOBAL_LOG_LEVEL=0
          ```
        * 导出NPU device端日志
          ```
          Driver安装目录/driver/tools/msnpureport report
          ```
      - debug
        * 卡住时定位卡的点
          ```
          py-spy --dump -p #pid
          ```
        * pdb debug
          ```
          python -m pdb your_script.py
          ```
        * vllm profiling data collect
          ```
          python -m pdb your_script.py
          ```

# 2 问题处理
**问题解决**
  - 正式解决
  ```
   issue中评论解决的PR、版本 => 关闭issue
  ```
  - 临时规避
  ```
   评论临时规避的方法/命令，以及长远的计划
  ```
**问题改进**
  - 代码问题
    * 已有测试用例守护 => 为什么没有守护住 =>  $$\color{red}{测试用例改进}$$
    * 没有测试用例守护 =>  $$\color{red}{添加测试用例}$$
  - 非代码问题
    * 指导文档缺失 =>  $$\color{red}{添加文档}$$
    * 指导文档不完善 =>  $$\color{red}{修复文档}$$
    * 客户未看文档 =>  Issue中回复文档链接 => **issue关闭**

## $$\color{red}{Notices}$$: 对于改进项，改进完成后进行**issue关闭**

# 3 历史问题处理经验
- 配套关系不正确导致的方法、属性找不到
```
   vllm-ascend和vllm的配套关系
   vllm-ascend依赖的组件的配套关系，如：Numpy, torch-npu等
```
- 算子报shap不匹配
```
   数据非正常被覆盖：计算路径上有路径错误、算子使用错误
   算子版本不匹配（CANN版本不匹配）
   随机数字：内存踩踏、内存未初始化
```
- 卡住问题
```
   卡间通讯、节点间通讯异常、多DP场景部分实例异常
   KVCache不足频繁换入换出
```

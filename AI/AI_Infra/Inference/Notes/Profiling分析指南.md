# Profiling 分析指南

## Ascend NPU + MindStudio Insight

**关键文件：**

trace_view.json
kernel_details.csv
op_stastic.csv

**kernel_details.csv 分析指南：**

冻结首行，start time 排序，只看一个 layer 的数据。
数量比较多的 stream id 就是“主流”，其它的是通信流、共享专家流（实现计算通信并行）。
静态/动态算子。
Duration（优化目标）：尽量让算子达到计算 bound，减少访存 bound -> 判断优化方向。
判断性能瓶颈：每个算子的时间除以整个 layer 的时间，再转换为百分比的形式 -> 判断优化重点。

**trace_view.json 分析指南：**

一般不用关注 CANN 这一层。
算子的实际执行时间主要看 Ascend Hardware 这一层。
AI Core Freq：当芯片温度比较高时，可能会出现降频，导致计算性能下降。

CV 并行：
cube 和 vector 是相互独立的计算单元，可以放到两条流上并行计算（但是会争访存带宽）。

bubble 分析：
观察设备 free 时间，算子执行之间有间隔，通过前后两个算子（连线）对应的 host 操作夹一下，找到 bubble 产生的位置（仅限不开图模式时）。

如何消除 bubble：
1.分析逻辑是否必要，消除、移到其它地方；
2.先下发高计算强度的算子，再下发低计算强度的算子（前提：前后之间没有依赖，可以调整顺序）；
3.邦核；
4.入图；

分析通信带宽是否合理：
通信算子的 count * 字节数 / latency = 理论通信带宽。

开启图模式之后的现象：
1.设备 free 总时长明显缩短；
2.设备主流上算子之间的间隔明显缩短，计算更密集。

怎么判断一个算子已经达到了计算 bound？
1.看 cube/vector/ai-core 利用率要达到 70-90%（prefill）；
2.找算子同学确认（计算公式很复杂）、利用分析工具。

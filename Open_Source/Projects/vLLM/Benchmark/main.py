# Preparation: pip install numpy pandas matplotlib
from draw import Draw


'''
A: vLLM Ascend v0.7.3
B: vLLM Ascend v0.7.3 + MindIE Turbo
C: vLLM Ascend v0.7.3 + Optimized Python / Torch / Torch NPU
D: vLLM Ascend v0.7.3 + MindIE Turbo + Optimized Python / Torch / Torch NPU
E: vLLM Ascend v0.7.3 + MindIE Turbo + Optimized Python / Torch / Torch NPU + TCMalloc
F: vLLM Ascend v0.7.3 + Optimized Python / Torch / Torch NPU + MindIE Turbo + TCMalloc + PYTORCH_NPU_ALLOC_CONF="max_split_size_mb:250"
G: vLLM Ascend v0.7.3 + Optimized Python / Torch / Torch NPU + MindIE Turbo + TCMalloc + PYTORCH_NPU_ALLOC_CONF="expandable_segments:True"
H: vLLM Ascend v0.7.3 + Optimized Python / Torch / Torch NPU + MindIE Turbo + TCMalloc + TASK_QUEUE_ENABLE=2
I: vLLM Ascend v0.7.3 + Optimized Python / Torch / Torch NPU + MindIE Turbo + TCMalloc + CPU_AFFINITY_CONF=1
'''
data_1 = {
    'Group': ['Group A', 'Group B', 'Group C', 'Group D', 'Group E', 'Group F', 'Group G', 'Group H', 'Group I'],
    'TTFT (Time to First Token)':   [86.58, 64.45, 69.44, 53.90, 52.72, 54.13, 53.67, 49.95, 50.09],
    'TPOT (Time per Output Token)': [32.57, 21.94, 22.60, 17.24, 17.16, 17.21, 17.17, 16.96, 17.06],
    'ITL (Inter-token Latency)':    [32.57, 21.94, 22.60, 17.24, 17.16, 17.21, 17.17, 16.96, 17.06]
}


'''
A: vllm-ascend + Qwen2
B: vllm-ascend + Qwen3
C: vllm-ascend + mindie-turbo + Qwen2
D: vllm-ascend + mindie-turbo + Qwen3
'''
data_2 = {
    'Group': ['Qwen3-8B with vllm-ascend', 'Qwen3-8B with vllm-ascend and mindie-turbo'],
    'TTFT (Time to First Token)':   [135.83, 99.78],
    'TPOT (Time per Output Token)': [55.45, 38.28],
    'ITL (Inter-token Latency)':    [55.45, 38.28],
}


if __name__ == "__main__":

    bar_width = 0.2  # 柱宽
    colors = ['#7BC8F6', '#A4D4AE', '#FFB347']  # 柱形颜色

    draw = Draw(bar_width=bar_width, colors=colors)
    draw.run(data=data_2, group='Group')

import numpy as np
import pandas as pd
import matplotlib.pyplot as plt


class Draw:

    def __init__(
        self,
        bar_width = 0.25,  # 柱宽
        colors = ['#7BC8F6', '#A4D4AE', '#FFB347'],  # 柱形颜色
    ):
        self.bar_width = bar_width
        self.colors = colors

    def run(
        self,
        data,
        group: str,
    ):
        bar_width = self.bar_width
        colors = self.colors

        df = pd.DataFrame(data)
        index = np.arange(len(df[group]))  # 分组位置

        # 绘制多系列柱形图
        fig, ax = plt.subplots(figsize=(10, 6))
        for i, col in enumerate(df.columns[1:]):  # 跳过 'Category' 列
            bars = plt.bar(
                index + i * bar_width,  # 计算每列位置
                df[col], 
                width=bar_width, 
                color=colors[i], 
                label=col
            )
            # if i == 1:
            #     continue
            for bar in bars:  # 在每个柱形上显示数值
                height = bar.get_height()
                plt.text(bar.get_x() + bar.get_width()/2, height, 
                        f'{height}', ha='center', va='bottom')
                # elif i == 2:
                #     plt.text(bar.get_x(), height, 
                #             f'{height}', ha='center', va='bottom')

        # 设置网格和背景颜色
        ax.grid(
            axis='both',         # 只显示横向网格线（'x'/'y'/'both'）
            linestyle='--',   # 虚线样式
            alpha=0.5,        # 透明度
            color='gray'      # 网格线颜色
        )
        ax.set_facecolor('#f0f0f0')  # 背景填充为浅灰色

        # 添加标签和标题
        # plt.xlabel('Combinations of Different Optimization Methods', fontsize=12)
        plt.ylabel('Mean Time (ms)', fontsize=12)
        plt.title('Benchmark Results', fontsize=16)
        plt.xticks(index + bar_width, df['Group'])  # X轴刻度
        plt.legend()  # 显示图例

        # 调整布局并显示
        plt.tight_layout()
        plt.show()

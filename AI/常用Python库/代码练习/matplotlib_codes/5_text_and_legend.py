# coding=utf-8

import numpy as np
import matplotlib.pyplot as plt


# matplotlib的文字与图例


def draw_text_1():
    fig = plt.figure()
    ax = fig.add_subplot()

    # 为 Figure 和 Axes 设置标题，注意两者的位置区别
    fig.suptitle('Bold Figure Suptitle', fontsize=14, fontweight='bold')
    ax.set_title('Axes Title')

    # 设置 X 和 Y 轴标签
    ax.set_xlabel('X Label')
    ax.set_ylabel('Y Label')

    # 设置 X 和 Y 轴显示范围
    ax.axis([0, 10, 0, 10])

    # 在 Axes 上添加文本
    ax.text(3, 8, 'Boxed Italics Text in Data Coords', style='italic',
            bbox={'facecolor': 'red', 'alpha': 0.5, 'pad': 10})

    # 在 Figure 上添加文本，通常更常见的是在 Axes 上添加文本
    fig.text(0.4, 0.8, 'Figure Text')

    ax.plot([2], [1], 'o')

    # 添加注解
    ax.annotate('Annotation', xy=(2, 1), xytext=(3, 4), arrowprops=dict(facecolor='black', shrink=0.05))


if __name__ == '__main__':
    # 设置 Matplotlib 参数以支持中文和负号显示
    plt.rcParams['font.sans-serif'] = ['SimHei']  # 设置字体以支持中文显示
    plt.rcParams['axes.unicode_minus'] = False  # 设置正常显示负号

    draw_text_1()
    plt.show()

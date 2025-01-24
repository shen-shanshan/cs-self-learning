# coding=utf-8

import numpy as np
import pandas as pd
import matplotlib.pyplot as plt


# matplotlib的布局策略


def draw_sub_figure_1():
    # 创建一个 2x5 的子图网格
    fig, axs = plt.subplots(2, 5, figsize=(10, 4), sharex=True, sharey=True)
    fig.suptitle('均匀子图布局', size=20)

    # 在每个子图上绘制散点图
    for i in range(2):
        for j in range(5):
            axs[i, j].scatter(np.random.randn(10), np.random.randn(10))
            axs[i, j].set_title(f'第{i + 1}行，第{j + 1}列')
            axs[i, j].set_xlim(-5, 5)
            axs[i, j].set_ylim(-5, 5)
            if i == 1:
                axs[i, j].set_xlabel('横坐标')
            if j == 0:
                axs[i, j].set_ylabel('纵坐标')

    # 调整布局以防止重叠
    fig.tight_layout(rect=[0, 0.03, 1, 0.95])


def draw_sub_figure_2():
    # 使用 GridSpec 创建非均匀子图
    fig = plt.figure(figsize=(10, 4))
    spec = fig.add_gridspec(nrows=2, ncols=5, width_ratios=[1, 2, 3, 4, 5], height_ratios=[1, 3])
    fig.suptitle('非均匀子图布局', size=20)

    # 创建并填充子图
    for i in range(2):
        for j in range(5):
            ax = fig.add_subplot(spec[i, j])
            ax.scatter(np.random.randn(10), np.random.randn(10))
            ax.set_title(f'第{i + 1}行，第{j + 1}列')
            if i == 1:
                ax.set_xlabel('横坐标')
            if j == 0:
                ax.set_ylabel('纵坐标')

    # 调整布局
    fig.tight_layout(rect=[0, 0.03, 1, 0.95])


def draw_sub_figure_3():
    # 创建一个新的 figure
    fig = plt.figure(figsize=(10, 4))
    spec = fig.add_gridspec(nrows=2, ncols=6, width_ratios=[2, 2.5, 3, 1, 1.5, 2], height_ratios=[1, 2])
    fig.suptitle('子图合并示例', size=20)

    # 创建合并后的子图
    ax1 = fig.add_subplot(spec[0, :3])
    ax1.scatter(np.random.randn(10), np.random.randn(10))
    ax1.set_title('合并前三列')

    ax2 = fig.add_subplot(spec[0, 3:5])
    ax2.scatter(np.random.randn(10), np.random.randn(10))
    ax2.set_title('合并第四和第五列')

    ax3 = fig.add_subplot(spec[:, 5])
    ax3.scatter(np.random.randn(10), np.random.randn(10))
    ax3.set_title('合并行')

    ax4 = fig.add_subplot(spec[1, 0])
    ax4.scatter(np.random.randn(10), np.random.randn(10))
    ax4.set_title('第二行第一列')

    ax5 = fig.add_subplot(spec[1, 1:5])
    ax5.scatter(np.random.randn(10), np.random.randn(10))
    ax5.set_title('合并第二行第二到第五列')

    # 调整布局
    fig.tight_layout(rect=[0, 0.03, 1, 0.95])


if __name__ == '__main__':
    # 设置 Matplotlib 参数以支持中文和负号显示
    plt.rcParams['font.sans-serif'] = ['SimHei']  # 设置字体以支持中文显示
    plt.rcParams['axes.unicode_minus'] = False  # 设置正常显示负号

    draw_sub_figure_3()
    plt.show()

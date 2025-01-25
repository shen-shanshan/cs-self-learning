# coding=utf-8

import numpy as np
import matplotlib
import matplotlib.pyplot as plt


# 03-matplotlib对象容器的使用


def draw_figure_1():
    # 创建一个 Figure 对象
    fig = plt.figure()

    # 添加 Axes 对象，占 Figure 的一定比例
    # [left, bottom, width, height] 以 Figure 的比例定义
    ax1 = fig.add_subplot(211)  # 作一幅2*1的图，选择第1个子图
    ax2 = fig.add_axes([0.1, 0.1, 0.7, 0.3])  # 位置参数，四个数分别代表了(left, bottom, width, height)

    # 显示 Axes 对象，确保它们被添加进 Figure
    print(fig.axes)


def draw_figure_2():
    fig = plt.figure()
    ax1 = fig.add_subplot(311)
    ax2 = fig.add_subplot(312)
    ax3 = fig.add_subplot(313)
    print(fig.axes)
    for i, ax in enumerate(fig.axes):
        ax.grid(True)
        if i == 1:
            ax.set_facecolor('lightgray')
        ax.plot(np.arange(101), np.random.randint(0, 10, 101))
        ax.set_ylim(0, 10)


def draw_axis_1():
    # 创建一个包含 Axes 的 Figure
    fig, ax = plt.subplots()

    # 获取 Axes 对象的 xaxis 属性
    xaxis = ax.xaxis

    # 设置刻度标签的格式
    xaxis.set_major_formatter(matplotlib.ticker.FormatStrFormatter('%0.1f'))

    # 开启网格线
    ax.grid(True)

    ax.plot([1, 2, 3, 4], [1, 4, 2, 3])

    axis = ax.xaxis  # axis为X轴对象
    print(axis.get_ticklocs())  # 获取刻度线位置
    print(axis.get_ticklabels())  # 获取刻度label列表(一个Text实例的列表）。 可以通过minor=True|False关键字参数控制输出minor还是major的tick label。
    print(axis.get_ticklines())  # 获取刻度线列表(一个Line2D实例的列表）。 可以通过minor=True|False关键字参数控制输出minor还是major的tick line。
    print(axis.get_data_interval())  # 获取轴刻度间隔


def draw_axis_2():
    fig = plt.figure()  # 创建一个新图表
    ax1 = fig.add_axes([0.1, 0.3, 0.4, 0.4])  # 创一个axes对象，从(0.1, 0.3)的位置开始，宽和高都为0.4
    # ax1 = fig.add_axes([0., 0., 0., 0.])

    for label in ax1.xaxis.get_ticklabels():
        # 调用x轴刻度标签实例，是一个text实例
        label.set_color('red')  # 颜色
        label.set_rotation(45)  # 旋转角度
        label.set_fontsize(16)  # 字体大小

    for line in ax1.yaxis.get_ticklines():
        # 调用y轴刻度线条实例, 是一个Line2D实例
        line.set_markeredgecolor('green')  # 颜色
        line.set_markersize(25)  # marker大小
        line.set_markeredgewidth(2)  # marker粗细


if __name__ == '__main__':
    draw_axis_2()
    plt.show()

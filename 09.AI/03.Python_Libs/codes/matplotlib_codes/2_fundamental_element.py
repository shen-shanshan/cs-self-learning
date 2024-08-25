# coding=utf-8

import numpy as np
import numpy.random
import matplotlib.pyplot as plt


# 02-matplotlib基本元素解析


def draw_line_2d_1():
    x = range(0, 5)
    y = [2, 5, 7, 8, 10]

    plt.plot(x, y, linewidth=4, linestyle='-', color='r', marker='*')


def draw_line_2d_2():
    x = range(0, 5)
    y1 = [2, 5, 7, 8, 10]
    y2 = [3, 6, 8, 9, 11]

    plt.plot(x, y1, label='Line 1')
    plt.plot(x, y2, label='Line 2', linestyle='--')
    plt.legend()


def draw_line_2d_3():
    x = np.arange(10)
    y = 2.5 * np.sin(x / 20 * np.pi)
    yerr = np.linspace(0.1, 0.2, 10)

    plt.errorbar(x, y, yerr=yerr, fmt='-o', ecolor='red', elinewidth=1)


def draw_hist():
    # 生成随机数据集
    x = np.random.randint(0, 200, 20)

    # 设置分布区间
    bins = np.arange(0, 201, 10)

    # 绘制直方图
    plt.hist(x, bins=bins, color='fuchsia')
    plt.xlabel('scores')
    plt.ylabel('count')
    plt.xlim(0, 200)


def draw_bar():
    x = np.arange(16)
    y = range(1, 17)
    z = numpy.random.randint(0, 101, 16)

    # 绘制柱状图
    plt.bar(x, z, alpha=0.2, width=0.8, color='blue', edgecolor='red', label='The First Bar')
    plt.legend()


def draw_pie():
    labels = ['Frogs', 'Hogs', 'Dogs', 'Logs']
    x = [15, 30, 45, 10]
    explode = (0, 0, 0, 0)

    # 绘制饼状图
    fig1, ax1 = plt.subplots()
    ax1.pie(x, explode=explode, labels=labels, autopct='%1.1f%%', shadow=True, startangle=180)
    ax1.axis('equal')


def draw_scatter():
    x = np.random.rand(50)
    y = np.random.rand(50)
    colors = np.random.rand(50)
    sizes = 1000 * np.random.rand(50)

    plt.scatter(x, y, c=colors, s=sizes, alpha=0.5, cmap='viridis')
    plt.colorbar()  # 显示颜色条


if __name__ == '__main__':
    draw_scatter()
    plt.show()

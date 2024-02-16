# coding=utf-8

import numpy as np
import matplotlib.pyplot as plt


# 01-matplotlib基本绘图元素


def draw_with_subplots():
    """
    使用面向对象的接口
    """

    x = np.linspace(0, 2, 100)

    # 创建图形和轴
    fig, ax = plt.subplots()

    # 在轴上绘制三个函数
    ax.plot(x, x, label='linear')
    ax.plot(x, x ** 2, label='quadratic')
    ax.plot(x, x ** 3, label='cubic')

    # 添加标签和标题
    ax.set_xlabel('x label')
    ax.set_ylabel('y label')
    ax.set_title("Simple Plot")

    # 显示图例
    ax.legend()

    # 显示图形
    plt.show()


def draw_with_plot():
    """
    使用 Pyplot 的简化接口
    """

    x = np.linspace(0, 2, 100)

    # 使用 pyplot 绘制三个函数
    plt.plot(x, x, label='linear')
    plt.plot(x, x ** 2, label='quadratic')
    plt.plot(x, x ** 3, label='cubic')

    # 添加标签和标题
    plt.xlabel('x label')
    plt.ylabel('y label')
    plt.title("Simple Plot")

    # 显示图例
    plt.legend()

    # 显示图形
    plt.show()


if __name__ == '__main__':
    draw_with_plot()

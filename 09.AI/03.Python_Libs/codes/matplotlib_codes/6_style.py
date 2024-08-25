# coding=utf-8

import numpy as np
import matplotlib.pyplot as plt


# matplotlib的缤纷样式


def draw_style_1():
    # 使用ggplot样式
    plt.style.use('ggplot')
    plt.plot([1, 2, 3, 4], [2, 3, 4, 5])


if __name__ == '__main__':
    # 设置 Matplotlib 参数以支持中文和负号显示
    plt.rcParams['font.sans-serif'] = ['SimHei']  # 设置字体以支持中文显示
    plt.rcParams['axes.unicode_minus'] = False  # 设置正常显示负号

    # print(plt.style.available)
    # ['Solarize_Light2', '_classic_test_patch', '_mpl-gallery', '_mpl-gallery-nogrid', 'bmh', 'classic', 'dark_background', 'fast', 'fivethirtyeight', 'ggplot', 'grayscale', 'seaborn-v0_8', 'seaborn-v0_8-bright', 'seaborn-v0_8-colorblind', 'seaborn-v0_8-dark', 'seaborn-v0_8-dark-palette', 'seaborn-v0_8-darkgrid', 'seaborn-v0_8-deep', 'seaborn-v0_8-muted', 'seaborn-v0_8-notebook', 'seaborn-v0_8-paper', 'seaborn-v0_8-pastel', 'seaborn-v0_8-poster', 'seaborn-v0_8-talk', 'seaborn-v0_8-ticks', 'seaborn-v0_8-white', 'seaborn-v0_8-whitegrid', 'tableau-colorblind10']

    draw_style_1()
    plt.show()

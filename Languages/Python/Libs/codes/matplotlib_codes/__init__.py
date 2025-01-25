# coding=utf-8

import numpy as np
import matplotlib.pyplot as plt

# matplotlib


if __name__ == '__main__':
    # 设置 Matplotlib 参数以支持中文和负号显示
    plt.rcParams['font.sans-serif'] = ['SimHei']  # 设置字体以支持中文显示
    plt.rcParams['axes.unicode_minus'] = False  # 设置正常显示负号

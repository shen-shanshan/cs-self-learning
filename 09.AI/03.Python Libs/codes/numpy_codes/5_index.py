# coding=utf-8

import numpy as np

# 05-numpy的索引


if __name__ == '__main__':
    # 创建一个随机数生成器
    rng = np.random.default_rng(seed=42)

    # 创建一个 5x4 的数组，元素是 0 到 20 的整数
    arr = rng.integers(0, 20, (5, 4))
    print("arr:\n", arr)

    # 获取第 0 行的所有元素
    print("arr[0]:\n", arr[0])

    # 获取第 0 行第 1 个元素
    print("arr[0, 1]:\n", arr[0, 1])

    # 获取第 1 到第 2 行的所有元素（不包括第 3 行）
    print("arr[1:3]:\n", arr[1:3])

    # 获取从开始到第 3 行的第 1 到第 2 列的元素
    print("arr[:3, 1:3]:\n", arr[:3, 1:3])

    # 创建两个形状相同的数组
    arr1 = rng.random((2, 3))
    arr2 = rng.random((2, 3))

    # 默认情况下，`np.concatenate` 沿着第一个轴（axis=0，即纵轴）进行连接
    concatenated_arr = np.concatenate((arr1, arr2))
    print("concatenated_arr:\n", concatenated_arr)

    # 可以指定 `axis` 参数来沿着不同的轴拼接数组，这里是沿着列（axis=1）
    concatenated_arr_axis = np.concatenate((arr1, arr2), axis=1)
    print("concatenated_arr_axis:\n", concatenated_arr_axis)

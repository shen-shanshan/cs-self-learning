# coding=utf-8

import numpy as np

# 06-numpy的数组形状的操作


if __name__ == '__main__':
    # 创建一个随机整数数组
    rng = np.random.default_rng(seed=42)  # 创建一个随机数生成器，种子为 42
    arr = rng.integers(1, 100, (3, 4))  # 生成一个 3x4 的整数数组，数值范围是 1 到 100
    print(arr)

    # 在第二个维度位置增加一个维度
    arr_expanded = np.expand_dims(arr, axis=1)
    print(arr_expanded.shape)
    print(arr_expanded)

    # 在多个位置增加维度
    expanded = np.expand_dims(arr, axis=(1, 3, 4))
    print(expanded.shape)

    # np.squeeze 函数用于移除数组形状中大小为 1 的维度
    arr_squeezed = np.squeeze(expanded)
    print(arr_squeezed.shape)

    # 将数组重塑为另一个形状
    arr_reshaped = arr.reshape(2, 2, 3)
    print(arr_reshaped)

    # 使用 np.resize 可以创建一个新数组，resize 还可以填充或截断原数组以匹配新的形状
    arr_resized = np.resize(arr, (5, 3))
    print(arr_resized)

    # 反序 numpy 数组
    # 默认情况下，使用切片 -1 可以反序数组的最外层维度
    arr_reversed = arr[::-1]
    print("arr:\n", arr)
    print("arr_reversed:\n", arr_reversed)

    # 可以在不同的维度上进行反序操作
    # 例如，反序所有维度
    arr_reversed_all_dims = arr[::-1, ::-1]
    print("arr_reversed_all_dims:\n", arr_reversed_all_dims)

    # 一维数组的转置
    arr_1d = np.array([1, 2])
    print(arr_1d.T.shape)
    print(arr_1d.T)  # 还是[1, 2]，因为一维数组的转置不会改变其形状

    # 多维数组的转置
    arr_transposed = arr.T
    print("arr:\n", arr)
    print("arr_transposed:\n", arr_transposed)

# coding=utf-8

import numpy as np

# 07-numpy的视图操作


if __name__ == '__main__':
    # 切片操作
    arr = np.array([1, 2, 3, 4, 5])
    view = arr[1:4]  # 创建 arr 的一个视图，包含索引 1 到 3 的元素
    print(view)

    view[0] = 99
    print(arr)

    # 展平操作
    arr = np.array([[1, 2, 3], [4, 5, 6]])
    view = arr.flatten()  # 创建 arr 的一维数组视图
    print(view)

    # 修改数组元素，看视图是否变化
    arr[0][0] = 100
    print(arr)
    print(view)  # 还是 [1 2 3 4 5 6]，没有马上变化？

    # 修改视图数据，看原始数据是否变化
    view[1] = 100
    print(view)
    print(arr)  # 没有变化？

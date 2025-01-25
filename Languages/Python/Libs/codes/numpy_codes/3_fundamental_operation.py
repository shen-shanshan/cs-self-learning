# coding=utf-8

import numpy as np

# 03-numpy的基本操作


if __name__ == '__main__':
    # 创建一个随机数生成器
    rng = np.random.default_rng(seed=42)

    # 生成一个均匀分布的数组
    arr = rng.uniform(0, 1, (3, 4))
    print("arr:\n", arr)

    # 维度
    print("arr ndim:\n", arr.ndim)
    # 形状
    print("arr shape:\n", arr.shape)
    # 数据量
    print("arr size:\n", arr.size)

    # axis=0 是纵轴
    print("arr.max(axis=0):\n", arr.max(axis=0))
    # axis=1 是横轴
    print("arr.max(axis=1):\n", arr.max(axis=1))




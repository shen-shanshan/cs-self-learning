# coding=utf-8

import numpy as np

# 04-numpy的广播机制


if __name__ == '__main__':
    # 创建一个随机数生成器
    rng = np.random.default_rng(seed=42)

    # 创建一个 3x4 的随机整数数组
    a = rng.integers(1, 100, (3, 4))
    print("a:\n", a)

    b = np.array([1, 2, 3, 4])
    print("b:\n", b)

    c = a + b
    print("c:\n", c)

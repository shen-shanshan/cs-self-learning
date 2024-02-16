# coding=utf-8

import numpy as np

# 08-numpy的线性代数操作


if __name__ == '__main__':
    rng = np.random.default_rng(42)
    arr = rng.integers(1, 20, (3, 4))
    print(arr)

    print(arr * 2)  # 全部元素都乘以 2

    # 超过5的都换成5（最小化到5）
    print(np.minimum(arr, 5))

    # 低于5的都换成5
    print(np.maximum(arr, 5))

    # 矩阵操作
    print("----------矩阵操作----------")
    rng = np.random.default_rng(42)
    a = rng.integers(1, 10, (3, 2))
    b = rng.integers(1, 10, (2, 4))
    c = rng.integers(1, 10, (3, 3))
    print(a, "\n", b, "\n", c)

    # 矩阵乘法（点乘）
    print("np.dot(a, b):\n", np.dot(a, b))
    print("a.dot(b):\n", a.dot(b))
    print("np.matmul(a, b):\n", np.matmul(a, b))
    print("a @ b:\n", a @ b)

    # 矩阵内积
    print("a:\n", a)
    print("np.inner(a, a):\n", np.inner(a, a))  # 即：a.dot(a.T)

# coding=utf-8

import numpy as np


# 02-numpy创建数组


def create_array_from_list_and_tuple():
    """
    从 Python 列表或元组创建

    列表：
    列表是写在方括号 [] 之间、用逗号分隔开的元素列表。
    列表中元素的类型可以不相同，它支持数字，字符串甚至可以包含列表。

    元组：
    元组的元素不能修改。
    元组写在小括号 () 里，元素之间用逗号隔开。
    元组中的元素类型也可以不相同。
    """

    # 从列表创建
    list_array = np.array([1, 2, 3])
    print("list_array:\n", list_array)
    print("list_array type:\n", type(list_array))

    list_array_2d = np.array([[1, 2., 3], [4, 5, 6]])
    print("list_array_2d:\n", list_array_2d)

    # 从元组创建
    tuple_array = np.array((1.1, 2.2))
    print("tuple_array:\n", tuple_array)


def create_array_by_arange():
    arange_array = np.arange(10)
    print("arange_array:\n", arange_array)  # [0 1 2 3 4 5 6 7 8 9]

    stepped_array = np.arange(100, 124, 2)  # [100 102 104 106 108 110 112 114 116 118 120 122]
    print("stepped_array:\n", stepped_array)


def create_array_by_linspace_and_logspace():
    # 使用 linspace 创建等间隔数值数组
    linear_space = np.linspace(0, 9, 10)
    print("linear_space:\n", linear_space)

    # 使用 logspace 创建对数间隔数值数组
    logarithmic_space = np.logspace(0, 9, 10, base=np.e)
    print("logarithmic_space:\n", logarithmic_space)
    print(8.10308393e+03)  # 等于 np.e ** 9


def create_array_by_ones_and_zeros():
    # 创建全 1 的数组
    ones_array = np.ones((2, 3))
    print("ones_array:\n", ones_array)

    # 创建全 0 的三维数组
    zeros_array = np.zeros((2, 3, 4))
    print("zeros_array:\n", zeros_array)


if __name__ == '__main__':
    create_array_from_list_and_tuple()
    create_array_by_arange()
    create_array_by_linspace_and_logspace()
    create_array_by_ones_and_zeros()

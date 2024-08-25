import functools


def my_cmp(o1, o2):
    if o1[1] != o2[1]:
        return o1[1] - o2[1]
    else:
        return o2[0] - o1[0]


if __name__ == '__main__':
    # 字典排序
    dict_1 = {1: 100, 2: 98, 3: 99}

    # 按键排序（默认升序）
    dict_2 = sorted(dict_1.items(), key=lambda x: x[0])
    print(dict_2)  # [(1, 100), (2, 98), (3, 99)]

    # 按值排序（降序）
    dict_3 = sorted(dict_1.items(), key=lambda x: x[1], reverse=True)
    print(dict_3)  # [(1, 100), (3, 99), (2, 98)]

    # 先按值升序，再按键降序
    # 可以先转换为列表再排序，方便后续按顺序取值
    dict_1[4] = 99
    dict_ls = list(dict_1.items())
    dict_4 = sorted(dict_ls, key=functools.cmp_to_key(my_cmp))
    print(dict_4)  # [(2, 98), (4, 99), (3, 99), (1, 100)]
    print(dict_4[0][0])  # 2

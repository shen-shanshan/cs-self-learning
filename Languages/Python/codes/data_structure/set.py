if __name__ == '__main__':
    # 创建集合
    # 方式一
    set_1 = {1, 2, 3}
    # 方式二
    # 注意：创建一个空集合必须用set()，因为{}创建的是一个空字典。
    set_2 = set()

    # 判断元素是否在集合中
    if 4 in set_1:
        print("4 is in set_1")

    # 可以直接传入一个字符串，并对其中的字符去重
    set_3 = set('abracadabra')
    print(set_3)  # {'c', 'a', 'd', 'b', 'r'}

    # 集合推导式
    set_4 = {x for x in 'abracadabra' if x not in 'abc'}
    print(set_4)  # {'r', 'd'}

    # 添加元素
    # 方式一：如果元素已存在，则不进行任何操作。
    set_1.add(4)
    # 方式二：参数可以是列表，元组，字典等
    set_1.add({5, 6})
    print(set_1)  # {1, 2, 3, 4, 5, 6}

    # 移除元素
    # 方式一：如果元素不存在，则会发生错误。
    set_1.remove(7)  # 报错
    # 方式二：如果元素不存在，不会发生错误。
    set_1.discard(7)
    # 方式三：随机删除集合中的一个元素。
    set_1.pop()

    # 清空集合
    set_1.clear()

if __name__ == '__main__':
    # 创建空字典
    dict_1 = {}
    dict_2 = dict()
    """
    注意：
    1.字典值可以是任何的python对象，既可以是标准的对象，也可以是用户自定义的，但键不行。
    2.键必须是唯一的，但值则不必。
    3.键必须不可变，所以可以用数字、字符串或元组充当，而用列表就不行。
    """
    dict_3 = {'Name': 'Runoob', 'Age': 7, 'Class': 'First'}

    # 访问元素
    print(dict_3['Name'])  # Runoob

    # 修改元素
    dict_3['Name'] = 'W3'
    print(dict_3['Name'])  # W3

    # 删除元素（键）
    del dict_3['Name']
    print(dict_3)  # {'Age': 7, 'Class': 'First'}

    # 清空字典
    dict_3.clear()
    # 或：del dict_3
    print(dict_3)  # {}

    # 遍历字典
    dict_4 = {'Name': 'Runoob', 'Age': "7", 'Class': 'First'}
    # 方式一：默认遍历的是key
    for k in dict_4:
        print(k)
    # 方式二：等价于方式一
    for k in dict_4.keys():
        print(k)
    # 方式三：遍历value
    for v in dict_4.values():
        print(v)
    # 方式四：遍历entry
    print(dict_4.items())  # [('Name', 'Runoob'), ('Age', "7"), ('Class', 'First')]
    for k, v in dict_4.items():
        print(k + ": " + v)
    # 方式五：
    for k, v in zip(dict_4.keys(), dict_4.values()):
        print(k + ": " + v)

    # 为字典中的键赋值时，一定要先判断这个键是否存在，否则会出现错误（KeyError:1）
    if 'Score' not in dict_4:
        dict_4['Score'] = 10
    else:
        dict_4['Score'] += 10
    print(dict_4)  # {'Name': 'Runoob', 'Age': '7', 'Class': 'First', 'Score': 10}

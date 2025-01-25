def func1(a, b):
    print('a: ', a)
    print('b: ', b)


def func2(*args):
    print(type(args))


def func3(**kwargs):
    print(type(kwargs))


def func4(arg, *args, **kwargs):
    print('arg:', arg)
    print('args:', args)
    print('kwargs:', kwargs)


if __name__ == '__main__':
    func1(1, b=1)
    # func(a=1, 1)  # SyntaxError: positional argument follows keyword argument

    func2(1, 2)  # <class 'tuple'>，*args 以元组的形式进行存储
    my_tuple = (1, 2)
    func2(*my_tuple)
    func2(my_tuple)  # 这样也可以

    func3(a=1, b=2)  # <class 'dict'>
    my_dict = {'a': 1, 'b': 2}
    # func3(my_dict)  # TypeError: func3() takes 0 positional arguments but 1 was given
    # func3(*my_dict)  # TypeError: func3() takes 0 positional arguments but 2 were given
    func3(**my_dict)  # <class 'dict'>

    '''
    注意：
    在调用 func2(my_tuple) 时，my_tuple 前可以不加 *，是因为此时 my_tuple 整体被当做了一个元素，传入了可变参数的元组中。
    
    而调用 func3(**my_dict) 时，my_dict 前必须加 **，是因为如果不加 **，此时的 my_dict 整体将被视为一个普通参数。
    而 **kwargs 只能接收键值对参数，因此无法直接接收一个字典元素，会报错。
    '''

    func4(1, 2, a=3)
    # arg: 1
    # args: (2,)
    # kwarg: {'a': 3}

    my_tuple2 = (1, 2)
    my_dict2 = {'a': 1, 'b': 2}
    func4(my_tuple2, my_dict2)
    # arg: (1, 2)
    # args: ({'a': 1, 'b': 2},)
    # kwargs: {}
    '''
    注意：
    这里 my_tuple2 整体被当做了 arg，而 my_dict2 整体被当做了 *args 中的一个可变参数。
    '''

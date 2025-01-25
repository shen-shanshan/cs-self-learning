import time


class A:
    def __new__(cls, *args, **kwargs):
        print("__new__")
        return super().__new__(cls, *args, **kwargs)

    def __init__(self):
        print("__init__")

    def __del__(self):
        print("__del__")


def main1():
    a = A()  # 分别调用：__new__() 和 __init__() 方法

    b = a
    c = a  # 此时指向 A() 这块内存的引用数为 3

    del a  # 用于删除变量或对象的引用，每次将对象的引用计数减 1
    del b  # 此时指向 A() 这块内存的引用数为 1

    time.sleep(5)
    # main1() 方法执行结束，此时指向 A() 这块内存的引用数为 0，该内存可以被清理，系统自动调用 __del__() 方法


class Person:
    def __init__(self, name, age):
        self.name = name
        self.age = age

    def __repr__(self):
        return f"Person(name={self.name}, age={self.age})"

    def __str__(self):
        return f"{self.name} is {self.age} years old"

    def __call__(self):
        print("__call__")

    def __len__(self):
        print("__len__")
        return 1


def main2():
    person = Person("Alice", 25)

    print(person)  # 调用 __str__() 方法（Alice is 25 years old）

    person()  # 调用 __call__() 方法（__call__）
    '''
    在 Python 中，使用 __call__() 方法可以实现将类的实例对象作为函数调用的效果，类似于调用一个函数。
    当我们调用一个类的实例对象时，Python 会自动调用这个实例对象的 __call__() 方法。
    '''

    len(person)  # 调用 __len__() 方法（__len__）


if __name__ == '__main__':
    main1()
    print("*" * 20)
    main2()

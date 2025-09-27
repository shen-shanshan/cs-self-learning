class Test:
    
    def __new__(cls, name):
        print(f"{name}: call Test __new__() method.")
        return super().__new__(SubTest)

    def __init__(self, name):
        print(f"{name}: call Test __init__() method.")
        self._name = name

    @property
    def name(self):
        return self._name

    def print(self):
        print(self)  # <__main__.Test object at 0x109bb8760>
        print(self.__class__)  # <class '__main__.Test'>
        print(self.__class__.__name__)  # Test


class SubTest(Test):

    def __new__(cls, name):
        print(f"{name}: call SubTest __new__() method.")
        return super().__new__(cls)

    def __init__(self, name):
        print(f"{name}: call SubTest __init__() method.")
        self._name = "sub " + name

    def print(self):
        print("call SubTest print")


a = Test('A')
# print(a.name)
# a.print()
# b = Test('B')

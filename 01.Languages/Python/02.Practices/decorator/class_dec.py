from typing import List


class Base:

    def __init__(self):
        print("init...")
    
    class_v: List[str] = []

    @classmethod
    def register(cls, name: str):

        def dec(sub_cls):
            cls.class_v.append(name)

        return dec


@Base.register("test")
class Test:

    def __init__(self):
        pass


if __name__ == '__main__':
    l = Base.class_v
    for i in range(len(l)):
        print(l[i])

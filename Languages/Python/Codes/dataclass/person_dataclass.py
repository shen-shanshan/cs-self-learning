from dataclasses import dataclass, field
from typing import List


@dataclass
class Person1:
    name: str
    age: int
    salary: float
    gender: bool
    hobbies: List[str]


def gen_list():
    return ['guitar', 'badminton', 'programme']


@dataclass(frozen=True)  # 使用 frozen=True 可以将该类的属性设置为只读，外界无法修改
class Person2:
    name: str = 'sss_2'  # 属性可以设置默认值
    age: int = 26
    salary: float = field(default=20000.000, repr=False)  # 使用 repr 参数可以设置隐藏字段（不会被 print() 方法打印）
    gender: bool = True
    # hobbies: List[str] = ['guitar', 'badminton']
    """
    ValueError: mutable default <class 'list'> for field hobbies is not allowed: use default_factory
    注意：对于可变类型（如 list）的属性，需要用工厂方法来产生默认值
    """
    hobbies: List[str] = field(default_factory=gen_list)


if __name__ == '__main__':
    p1 = Person1('sss', 18, 15000.000, True, ['guitar', 'badminton'])
    print(p1)  # Person1(name='sss', age=18, salary=15000.0, gender=True, hobbies=['guitar', 'badminton'])

    p2 = Person2()
    print(p2)  # Person2(name='sss_2', age=26, gender=True, hobbies=['guitar', 'badminton', 'programme'])

    p1.name = 'ma yun'
    print(p1.name)  # ma yun

    # p2.name = 'ma hua teng'
    """
    dataclasses.FrozenInstanceError: cannot assign to field 'name'
    Person2 的属性已被冻结，无法修改
    """

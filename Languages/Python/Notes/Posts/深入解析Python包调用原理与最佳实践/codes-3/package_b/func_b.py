# from package_a.func_a import function_a


# def function_b():
#     function_a()
#     print("call function_b().")


# from package_a.func_a import A
from typing import TYPE_CHECKING
if TYPE_CHECKING:
    from package_a.func_a import A


class B:
    def __init__(self):
        pass


def get_class() -> "A":
    print("get class A from package_a.")

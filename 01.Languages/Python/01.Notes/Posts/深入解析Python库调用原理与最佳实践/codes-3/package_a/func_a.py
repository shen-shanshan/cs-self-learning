# from package_b.func_b import function_b


# def function_a():
#     print("call function_a().")
#     function_b()


# from package_b.func_b import B
from typing import TYPE_CHECKING
if TYPE_CHECKING:
    from package_b.func_b import B


class A:
    def __init__(self):
        pass


def get_class() -> "B":
    print("get class B from package_b.")

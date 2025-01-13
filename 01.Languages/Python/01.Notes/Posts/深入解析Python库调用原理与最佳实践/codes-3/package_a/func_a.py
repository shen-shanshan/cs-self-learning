from package_b.func_b import function_b


def function_a():
    print("function of a execute.")
    function_b()


class A:
    def __init__(self):
        pass


from typing import TYPE_CHECKING
if TYPE_CHECKING:
    from package_b.func_b import B


def get_class() -> "B":
    print("get class B from package_b.")

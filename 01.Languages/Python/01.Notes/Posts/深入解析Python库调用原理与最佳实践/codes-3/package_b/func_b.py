# from package_a.func_a import function_a


def function_b():
    # function_a()
    print("function of b execute.")


class B:
    def __init__(self):
        pass


from typing import TYPE_CHECKING
if TYPE_CHECKING:
    from package_a.func_a import A


def get_class() -> "A":
    print("get class A from package_a.")

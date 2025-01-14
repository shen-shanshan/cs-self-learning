import sys

import package_a
# from package_a import *

# import package_a.package_b
# import package_a.package_c

# from package_a import package_b
# from package_a import package_c

from package_a.package_b.test_b import test


def print_dir(dirs, name):
    print("-----------------------------")
    print("dir(" + name + "):")
    for dir in dirs:
        print(dir)


print_dir(dir(), "main.py")
print_dir(dir(package_a), "package_a")
# print_dir(dir(package_b), "package_b")

print("-----------------------------")
print("sys.modules:")
for k, v in sys.modules.items():
    print(k, ":", v)

print("-----------------------------")
print("globals():")
for k, v in globals().items():
    print(k, ":", v)

print("-----------------------------")
print("locals():")
for k, v in locals().items():
    print(k, ":", v)


def test():
    print("call test() in main.")


print("-----------------------------")
package_a.package_b.test_b.test()
test()

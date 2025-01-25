'''
import ... vs from ... import ...
'''

import sys

# import package_a.package_b.test_b
from package_a.package_b.test_b import print_test_b


# def print_test_b():
#     print("call test_b(). in main")


# package_a.package_b.test_b.print_test_b()
# print_test_b()

# test_b.print_test_b()
# package_a.package_b.test_b.print_test_b()

# def print_dir(dirs, name):
#     print("-----------------------------------------")
#     print("[" + name + "] Imported dirs are showed below:")
#     for dir in dirs:
#         print(dir)

# print_dir(dir(), "main.py")

for k, v in sys.modules.items():
    print(k, ":", v)
'''
import package_a.package_b.test_b:

package_a : <module 'package_a' from '/.../package_a/__init__.py'>
package_a.package_b : <module 'package_a.package_b' from '/.../package_a/package_b/__init__.py'>
package_a.package_b.test_b : <module 'package_a.package_b.test_b' from '/.../package_a/package_b/test_b.py'>

from package_a.package_b.test_b import print_test_b:

同上
'''

print("----------------------------------------------------------------------------------")
for k, v in globals().items():
    print(k, ":", v)
'''
import package_a.package_b.test_b:

package_a : <module 'package_a' from '/.../package_a/__init__.py'>

from package_a.package_b.test_b import print_test_b:

print_test_b : <function print_test_b at 0x10b59e5e0>
'''

print("----------------------------------------------------------------------------------")
for k, v in locals().items():
    print(k, ":", v)
'''
import package_a.package_b.test_b:

package_a : <module 'package_a' from '/.../package_a/__init__.py'>

from package_a.package_b.test_b import print_test_b:

print_test_b : <function print_test_b at 0x10b59e5e0>
'''

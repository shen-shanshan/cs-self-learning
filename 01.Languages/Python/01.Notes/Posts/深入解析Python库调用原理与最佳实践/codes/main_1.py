'''
关于 __init__.py 的原理与应用
'''

import sys

import package_a
from package_a import *
# import package_a.package_b
# from package_a import package_b
# import package_a.package_c
# from package_a import package_c


# def print_dir(dirs, name):
#     print("-----------------------------------------")
#     print("[" + name + "] Imported dirs are showed below:")
#     for dir in dirs:
#         print(dir)

# print_dir(dir(), "main.py")
# print_dir(dir(package_a), "package_a")
# print_dir(dir(package_b), "package_b")

# package_a.test_b.print_test_b() # 需要在 package_a 的 __init__.py 中 from package_a.package_b import test_b 才 ok
# package_b.test_b.print_test_b() # ok


for k, v in sys.modules.items():
    print(k, ":", v)
'''
package_a : <module 'package_a' from '/.../package_a/__init__.py'>
package_a.package_b : <module 'package_a.package_b' from '/.../package_a/package_b/__init__.py'>
'''
print("----------------------------------------------------------------------------------")
for k, v in globals().items():
    print(k, ":", v)
'''
__name__ : __main__
__doc__ : 
关于 __init__.py 的原理与应用

__package__ : None
__loader__ : <_frozen_importlib_external.SourceFileLoader object at 0x10d768ca0>
__spec__ : None
__annotations__ : {}
__builtins__ : <module 'builtins' (built-in)>
__file__ : /Users/shanshan-shen/Documents/GitHub/cs-self-learning/01.Languages/Python/01.Notes/Posts/深入解析Python库调用原理与最佳实践/codes/main.py
__cached__ : None
sys : <module 'sys' (built-in)>
package_a : <module 'package_a' from '/Users/shanshan-shen/Documents/GitHub/cs-self-learning/01.Languages/Python/01.Notes/Posts/深入解析Python库调用原理与最佳实践/codes/package_a/__init__.py'>
package_b : <module 'package_a.package_b' from '/Users/shanshan-shen/Documents/GitHub/cs-self-learning/01.Languages/Python/01.Notes/Posts/深入解析Python库调用原理与最佳实践/codes/package_a/package_b/__init__.py'>
k : package_b
v : package_b
'''
print("----------------------------------------------------------------------------------")
for k, v in locals().items():
    print(k, ":", v)
'''
结果同上
'''

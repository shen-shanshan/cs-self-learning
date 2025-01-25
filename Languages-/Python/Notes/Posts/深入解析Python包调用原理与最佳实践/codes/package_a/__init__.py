'''
如果目录中包含了 __init__.py 文件，那么当用 import 导入该目录时，就会执行 __init__.py 里面的代码。
'''
print("package_a has been imported.")
# print(__package__) # package_a

'''
我们可以在 __init__.py 中指定默认需要导入的模块（使用从项目根目录（入口程序）开始的路径）。

from package_b import test_b

ModuleNotFoundError: No module named 'package_b'

在我们执行 import 时，当前目录 (main.py) 是不会变的（就算是执行子目录的文件），还是需要完整的包名。

注意：下面的 test_b 同时被 import 到了 package_a 和 package_b 中！但不在 main.py 中。
'''
# from package_b import test_b
# from package_a.package_b import test_b

'''
__all__ 关联了一个模块列表，当执行 from xx import * 时，就会导入下面列表中的模块。
导入操作会继续查找 package_b 中的 __init__.py 并执行（但此时不会执行 import *）。

我们在 package_b 下添加 __init__.py 文件:
'''
__all__ = ['package_b']

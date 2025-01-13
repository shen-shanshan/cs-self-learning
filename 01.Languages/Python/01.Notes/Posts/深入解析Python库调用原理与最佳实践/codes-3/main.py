import package_a
# from package_b import func_b
from package_b.func_b import function_b


'''
实验一：
ImportError: cannot import name 'function_a' from partially initialized module 'package_a.func_a' (most likely due to a circular import)
'''

package_a.func_a.get_class()

'''
实验二：
ImportError: cannot import name 'A' from partially initialized module 'package_a.func_a' (most likely due to a circular import)

使用 TYPE_CHECKING 但未加引号：
NameError: name 'A' is not defined

加引号：
get class B from package_b.
'''

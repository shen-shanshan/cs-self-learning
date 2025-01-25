print("package_b has been imported.")
# print(__package__) # package_a.package_b

'''
下面的 test_b 只被导入到了 package_b 中，而没有 import 到 package_a 中。
'''
# from package_a.package_b import test_b

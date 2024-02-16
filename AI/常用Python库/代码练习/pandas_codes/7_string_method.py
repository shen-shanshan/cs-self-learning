# coding=utf-8

import numpy as np
import pandas as pd

# pandas文本数据处理


if __name__ == '__main__':
    s = pd.Series(['abcd', 'efg', 'hi'])
    print(s.str[1])  # 获取每个字符串的第二个字符

    """
    Pandas 1.0.0 版本引入了 string 类型，这是一种专门用于存储字符串的数据类型。
    它的引入是为了区分字符串和混合类型（例如同时存储浮点、字符串、字典等）的 object 类型。
    string 类型支持缺失值表示。
    """
    s = pd.Series(['a', np.nan, 'c'])
    print(s.str.len())  # 使用 object 类型
    print(s.astype('string').str.len())  # 使用 string 类型

    # 需要注意的是，对于全数字的序列，即使其类型为 object，也不能直接使用 str 属性。但可以用 astype 转换为 string 类型。
    s = pd.Series([123, 233, 311])
    print(s.astype('string').str[0])
    # print(s.str.len())  # Can only use .str accessor with string values!
    print(s.astype('string').str.len())

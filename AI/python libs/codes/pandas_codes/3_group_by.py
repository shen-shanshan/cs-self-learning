# coding=utf-8

import numpy as np
import pandas as pd


# pandas的分组操作


# 自定义聚合函数
def range_func(series):
    return series.max() - series.min()


if __name__ == '__main__':
    df = pd.read_csv('E:\编程\AI\深度之眼\人工智能0基础训练营\课件/7.pandas\data/pandas_starter.csv')

    # print(df.groupby('customer_id')['price'].sum())
    # print(df.groupby(['customer_id', 'sales_channel_id'])['price'].sum())

    condition = df['age'] > 30
    # print(df.groupby(condition)['price'].count())  # 最后产生的结果就是按照条件列表中元素的值（此处是True和False）来分组

    item = np.random.choice(list('abc'), df.shape[0])
    # print(df.groupby(item)['price'].count())

    # Groupby对象
    gb = df.groupby(['customer_id', 'sales_channel_id'])
    # print("分组个数:", gb.ngroups)
    # print(gb.groups.keys())

    # 聚合函数
    gb = df.groupby('age')['price']
    # print(gb.max())

    # 同时使用多个函数
    gb = df.groupby('article_id')[['price', 'age']]
    print(gb.agg(['min', 'max', 'mean']))

    # 对特定的列使用特定的聚合函数
    print(gb.agg({'price': ['min', 'max'], 'age': 'median'}))

    # 应用自定义聚合函数
    print(gb.agg({'price': range_func, 'age': 'mean'}))

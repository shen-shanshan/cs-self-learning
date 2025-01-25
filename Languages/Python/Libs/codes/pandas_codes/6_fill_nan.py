# coding=utf-8

import numpy as np
import pandas as pd


# pandas缺失数据处理


def find_nan_value():
    df = pd.read_csv('E:\编程\AI\深度之眼\人工智能0基础训练营\课件/7.pandas\data/pandas_starter.csv')

    print(df.isna().head())

    # 计算每列缺失值的比例
    print(df.isna().mean())

    print(df['Active'].isna())

    # 筛选缺失值
    print(df.loc[df['Active'].isna(), ['Active']].head())

    # 检索在多个列中全部缺失、至少有一个缺失或没有缺失的行
    sub_set = df[['Active', 'detail_desc', 'FN']]
    # 全部缺失
    print(df.loc[sub_set.isna().all(axis=1), ['Active', 'detail_desc', 'FN']])
    # 至少有一个缺失
    print(df.loc[sub_set.isna().any(axis=1), ['Active', 'detail_desc', 'FN']].head())
    # 没有缺失
    print(df[sub_set.notna().all(axis=1)].head())


def fill_nan_value():
    s = pd.Series([np.nan, 1, np.nan, np.nan, 2, np.nan], list('aaabcd'), name="nan_data")
    # print(s)

    # 用前面的值向后填充
    s1 = s.fillna(method='ffill')  # Series.fillna with 'method' is deprecated and will raise in a future version.
    s1 = s.ffill()
    print(s1)

    # 连续出现的缺失，最多填充一次
    s2 = s.ffill(limit=1)
    print(s2)

    # 用0填充
    s3 = s.fillna(0)
    print(s3)


def fill_nan_value_with_insert():
    s = pd.Series([1, np.nan, np.nan, 10])

    # 线性插值
    s1 = s.interpolate()
    print(s1)

    # 最近邻插值
    s2 = s.interpolate(method='nearest')
    print(s2)

    # 索引插值
    s = pd.Series([0, np.nan, 100], index=[0, 3, 10])
    s3 = s.interpolate(method='index')
    print(s3)


if __name__ == '__main__':
    # find_nan_value()
    # fill_nan_value()
    fill_nan_value_with_insert()

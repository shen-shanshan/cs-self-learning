# coding=utf-8

import pandas as pd


# pandas的索引


def test_column_index():
    df = pd.read_csv('E:\编程\AI\深度之眼\人工智能0基础训练营\课件/7.pandas\data/pandas_starter.csv')

    print(df['customer_id'].head())
    print(df[['customer_id', 'article_id']].head())


def test_row_index():
    s = pd.Series([1, 2, 3, 4, 5, 6], index=['a', 'b', 'a', 'a', 'a', 'c'])

    print(s['a'])  # 返回所有'a'索引对应的值
    print(s[['c', 'b']])  # 返回索引'c'和'b'对应的值


def test_loc_index():
    df = pd.read_csv('E:\编程\AI\深度之眼\人工智能0基础训练营\课件/7.pandas\data/pandas_starter.csv')
    df_demo = df.set_index('t_dat')

    # 基于单个标签选取
    print(df_demo.loc['2019-05-24'].head())

    # 基于标签列表选取
    print(df_demo.loc[['2019-05-24', '2019-05-25'], ['customer_id', 'article_id']].head())

    # 基于布尔数组选取
    print(df_demo.loc[df_demo.price > 0.2, ['price']].head())  # 选取price超过0.2的行


def test_iloc_index():
    df = pd.read_csv('E:\编程\AI\深度之眼\人工智能0基础训练营\课件/7.pandas\data/pandas_starter.csv')
    df_demo = df.set_index('t_dat')

    print(df_demo.iloc[1, 1])  # 访问第2行第2列的元素
    print(df_demo.iloc[[0, 1], [0, 1]])  # 访问前两行前两列的元素
    print(df_demo.iloc[1:4, 2:4])  # 访问第2行到第4行，第3列到第4列的元素


def test_query():
    # 示例数据
    data = {
        'product': ['apple', 'banana', 'cherry', 'date'],
        'price': [0.25, 0.35, 0.22, 0.29],
        'quantity': [5, 7, 8, 6]
    }
    query_df = pd.DataFrame(data)

    # 使用query方法查询
    min_price = 0.2
    min_quantity = 6
    result = query_df.query('price > @min_price and quantity >= @min_quantity')
    print(result)


def test_random_sample():
    df = pd.read_csv('E:\编程\AI\深度之眼\人工智能0基础训练营\课件/7.pandas\data/pandas_starter.csv')
    print(df.sample(n=10))  # 随机抽取10行


if __name__ == '__main__':
    test_column_index()
    test_row_index()
    test_loc_index()
    test_iloc_index()
    test_query()
    test_random_sample()

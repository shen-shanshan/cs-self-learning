# coding=utf-8

import pandas as pd


# pandas的连接操作


def test_value_join():
    # 值连接（需要指定根据哪一列来进行连接）
    df1 = pd.DataFrame({'Name': ['San Zhang', 'Si Li'], 'Age': [20, 30]})
    df2 = pd.DataFrame({'Name': ['Si Li', 'Wu Wang'], 'Gender': ['F', 'M']})
    df3 = df1.merge(df2, on='Name', how='left')
    # print(df3)

    # 如果连接的列名不同，可以使用 left_on 和 right_on 指定
    df1 = pd.DataFrame({'df1_name': ['San Zhang', 'Si Li'], 'Age': [20, 30]})
    df2 = pd.DataFrame({'df2_name': ['Si Li', 'Wu Wang'], 'Gender': ['F', 'M']})
    df4 = df1.merge(df2, left_on='df1_name', right_on='df2_name', how='left')
    # print(df4)

    # 当两个表中有重复的列名时，可以通过 suffixes 参数来区分
    df1 = pd.DataFrame({'Name': ['San Zhang'], 'Grade': [70]})
    df2 = pd.DataFrame({'Name': ['San Zhang'], 'Grade': [80]})
    df5 = df1.merge(df2, on='Name', how='left', suffixes=['_Chinese', '_Math'])
    # print(df5)

    # 根据多个列的组合来执行连接
    df1 = pd.DataFrame({'Name': ['San Zhang', 'San Zhang'],
                        'Age': [20, 21],
                        'Class': ['one', 'two']})
    df2 = pd.DataFrame({'Name': ['San Zhang', 'San Zhang'],
                        'Gender': ['F', 'M'],
                        'Class': ['two', 'one']})
    df6 = df1.merge(df2, on=['Name', 'Class'], how='left')
    print(df6)


def test_index_join():
    # 索引连接（可以直接进行连接，连接的键即是索引）
    df1 = pd.DataFrame({'Age': [20, 30]}, index=pd.Series(['San Zhang', 'Si Li'], name='Name'))
    df2 = pd.DataFrame({'Gender': ['F', 'M']}, index=pd.Series(['Si Li', 'Wu Wang'], name='Name'))

    df3 = df1.join(df2, how='left')
    # print(df3)

    # 使用多级索引连接
    df1 = pd.DataFrame({'Age': [20, 21]}, index=pd.MultiIndex.from_arrays([['San Zhang', 'San Zhang'], ['one', 'two']],
                                                                          names=('Name', 'Class')))
    df2 = pd.DataFrame({'Gender': ['F', 'M']},
                       index=pd.MultiIndex.from_arrays([['San Zhang', 'San Zhang'], ['two', 'one']],
                                                       names=('Name', 'Class')))
    df4 = df1.join(df2)
    # print(df4)


def test_concat():
    df1 = pd.DataFrame({'Name': ['San Zhang', 'Si Li'], 'Age': [20, 30]})
    df2 = pd.DataFrame({'Name': ['Wu Wang'], 'Age': [40]})

    # 默认在纵轴上进行拼接（axis=0）
    print(pd.concat([df1, df2]))
    # 在横轴上进行拼接
    print(pd.concat([df1, df2], axis=1))


if __name__ == '__main__':
    test_value_join()
    test_index_join()
    test_concat()

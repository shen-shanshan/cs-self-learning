# coding=utf-8

import pandas as pd


# pandas基础


def read_file():
    df_csv = pd.read_csv('E:\编程\AI\深度之眼\人工智能0基础训练营\课件/7.pandas\data/01_sample.csv')  # 读取 CSV 文件
    print(df_csv.head())  # 显示前五行数据

    df_txt = pd.read_table('E:\编程\AI\深度之眼\人工智能0基础训练营\课件/7.pandas\data/01_sample.txt')  # 读取 TXT 文件
    print(df_txt.head())  # 显示前五行数据

    df_excel = pd.read_excel('E:\编程\AI\深度之眼\人工智能0基础训练营\课件/7.pandas\data/01_sample.xlsx')  # 读取 Excel 文件
    print(df_excel.head())  # 显示前五行数据


def write_file():
    df_csv = pd.read_csv('E:\编程\AI\深度之眼\人工智能0基础训练营\课件/7.pandas\data/01_sample.csv')  # 读取 CSV 文件

    # 写入文件
    df_csv.to_csv('./data/01_sample_saved.csv', index=False)
    df_csv.to_csv('./data/01_sample_saved_with_index.csv')


def fundamental_ops():
    # 常用基本函数

    df = pd.read_csv('E:\编程\AI\深度之眼\人工智能0基础训练营\课件/7.pandas\data/pandas_starter.csv')

    # 汇总函数
    print("head:\n", df.head(3), "\n")
    print("tail:\n", df.tail(3), "\n")
    print("info:\n", df.info(), "\n")
    print("describe:\n", df.describe(), "\n")

    # 数据统计函数、唯一值函数、排序函数


if __name__ == '__main__':
    # read_file()
    # write_file()
    fundamental_ops()

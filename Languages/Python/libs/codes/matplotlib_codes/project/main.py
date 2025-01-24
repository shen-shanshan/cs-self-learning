# coding=utf-8

import numpy as np
import pandas as pd
from matplotlib import pyplot as plt
import matplotlib.image as mpimg
from pandas import DataFrame
from wordcloud import WordCloud, STOPWORDS


def init():
    # 设置要显示的行数和列数
    pd.set_option('display.max_rows', 100)
    pd.set_option('display.max_columns', 50)

    # 用来正常显示中文标签
    plt.rcParams['font.sans-serif'] = ['SimHei']
    # 用来正常显示负号
    plt.rcParams['axes.unicode_minus'] = False

    articles = pd.read_csv("E:\编程\AI\深度之眼\人工智能0基础训练营\课件/6.matplotlib\data/articles.csv")
    customers = pd.read_csv("E:\编程\AI\深度之眼\人工智能0基础训练营\课件/6.matplotlib\data/customers.csv")
    transactions = pd.read_csv("E:\编程\AI\深度之眼\人工智能0基础训练营\课件/6.matplotlib\data/transactions_train.csv")

    return articles, customers, transactions


def draw_articles(articles: DataFrame):
    cols = ['index_name', 'index_group_name']
    fig, axs = plt.subplots(1, len(cols), figsize=(10, 4), sharex=True, sharey=True)
    fig.suptitle('Articles 部分字段频次统计', size=20)

    for idx, col in enumerate(cols):
        axs[idx].hist(articles[col], orientation="horizontal", color='orange')
        axs[idx].set_xlabel(f'Count by {col}')
        axs[idx].set_ylabel(col)

    # 调整布局以防止重叠
    fig.tight_layout(rect=[0, 0.03, 1, 0.95])


def draw_customers(customers: DataFrame):
    fig, ax = plt.subplots(figsize=(10, 5))
    ax.hist(customers['age'], color='orange', bins=70)
    ax.set_xlabel('Distribution of the customers age')


def draw_transactions(transactions: DataFrame):
    # 数据准备
    data1 = np.log(transactions.loc[transactions["sales_channel_id"] == 1].price.value_counts())
    data2 = np.log(transactions.loc[transactions["sales_channel_id"] == 2].price.value_counts())
    data3 = np.log(transactions.price.value_counts())

    # 创建分面图
    fig, axs = plt.subplots(3, 1, figsize=(14, 14))  # 3个子图

    # 子图1：销售渠道1
    axs[0].hist(data1, bins=30, alpha=0.5, color='blue')
    axs[0].set_title('Sales channel 1')

    # 子图2：销售渠道2
    axs[1].hist(data2, bins=30, alpha=0.5, color='green')
    axs[1].set_title('Sales channel 2')

    # 子图3：所有销售渠道
    axs[2].hist(data3, bins=30, alpha=0.5, color='red')
    axs[2].set_title('All Sales channels')

    # 调整子图布局
    plt.tight_layout()


if __name__ == '__main__':
    # H&M数据可视化实战
    articles, customers, transactions = init()

    # 预览数据
    # print(articles.head())

    draw_articles(articles)
    draw_customers(customers)
    draw_transactions(transactions)
    plt.show()

"""
73. 矩阵置零
"""
from typing import List


class Solution:
    def setZeroes(self, matrix: List[List[int]]) -> None:
        m = len(matrix)
        n = len(matrix[0])

        # 统计所有0的位置
        rows = set()
        columns = set()
        for i in range(m):
            for j in range(n):
                if matrix[i][j] == 0:
                    rows.add(i)
                    columns.add(j)

        list_rows = list(rows)
        list_columns = list(columns)

        for i in range(m):
            for j in range(n):
                if i in list_rows or j in list_columns:
                    matrix[i][j] = 0

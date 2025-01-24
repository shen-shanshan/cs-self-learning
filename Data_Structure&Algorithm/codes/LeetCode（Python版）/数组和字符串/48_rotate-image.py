"""
48. 旋转图像
"""
from typing import List


class Solution:
    def rotate(self, matrix: List[List[int]]) -> None:
        m = len(matrix)
        n = len(matrix[0])

        # 上下翻转
        for i in range(m // 2):
            for j in range(n):
                tmp = matrix[i][j]
                matrix[i][j] = matrix[m - 1 - i][j]
                matrix[m - 1 - i][j] = tmp

        # 按对角线翻转
        for i in range(m):
            for j in range(i):
                tmp = matrix[i][j]
                matrix[i][j] = matrix[j][i]
                matrix[j][i] = tmp

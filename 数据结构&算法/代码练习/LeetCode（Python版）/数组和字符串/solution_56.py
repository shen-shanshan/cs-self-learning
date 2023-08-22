from typing import List

"""
56. 合并区间
"""


class Solution:
    def merge(self, intervals: List[List[int]]) -> List[List[int]]:
        # 按起点排序
        intervals.sort(key=lambda x: x[0])

        ans = []
        left = intervals[0][0]
        right = intervals[0][1]

        for i in range(1, len(intervals)):
            if intervals[i][0] <= right:
                right = max(intervals[i][1], right)
            else:
                ans.append([left, right])
                left = intervals[i][0]
                right = intervals[i][1]

        ans.append([left, right])

        return ans

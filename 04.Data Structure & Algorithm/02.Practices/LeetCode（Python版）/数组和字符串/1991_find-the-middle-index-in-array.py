"""
1991. 找到数组的中间位置
"""
from typing import List


class Solution:
    def findMiddleIndex(self, nums: List[int]) -> int:
        if len(nums) == 1:
            return 0

        sums = sum(nums)
        left = 0

        for i in range(len(nums)):
            if left * 2 + nums[i] == sums:
                return i
            left += nums[i]

        return -1

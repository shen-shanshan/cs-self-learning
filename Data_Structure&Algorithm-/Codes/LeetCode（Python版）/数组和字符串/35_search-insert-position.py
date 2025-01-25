"""
35. 搜索插入位置
"""
from typing import List


class Solution:
    def searchInsert(self, nums: List[int], target: int) -> int:
        if min(nums) >= target:
            return 0
        if max(nums) < target:
            return len(nums)

        left = 0
        right = len(nums) - 1

        while left <= right:
            mid = left + (right - left) // 2
            if nums[mid] < target:
                left = mid + 1
            elif nums[mid] > target:
                right = mid - 1
            else:
                return mid

        return left

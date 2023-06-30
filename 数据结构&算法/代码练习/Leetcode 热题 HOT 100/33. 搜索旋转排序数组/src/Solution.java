class Solution {
    public int search(int[] nums, int target) {
        if (nums.length == 0) return -1;
        if (nums.length == 1) return nums[0] == target ? 0 : -1;
        int len = nums.length;
        int midValue = nums[0];
        // 二分找到分界点
        int left = 0;
        int right = len - 1;
        int mid = 0;
        while (left < right) {
            mid = left + ((right - left) >> 1);
            if (nums[mid] < midValue) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        // 分界点下标
        int index = 0;
        if (left == len - 1) {
            left = 0;
            right = len - 1;
        } else {
            index = left - 1;
            left = target >= midValue ? 0 : index + 1;
            right = target >= midValue ? index : len - 1;
        }
        // 二分查找
        while (left < right) {
            mid = left + ((right - left) >> 1);
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return nums[left] == target ? left : -1;
    }
}
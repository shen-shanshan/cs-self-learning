/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 34.在排序数组中查找元素的第一个和最后一个位置
 */
public class Solution34 {
    public int[] searchRange(int[] nums, int target) {
        // 找左边界
        int left = mySearch(nums, target, true);
        if (left == -1) {
            return new int[]{-1, -1};
        }

        // 找右边界
        int right = mySearch(nums, target, false);
        return new int[]{left, right};
    }

    public int mySearch(int[] nums, int target, boolean flag) {
        int left = 0;
        int right = nums.length - 1;

        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] < target) {
                left = mid + 1;
            } else if (nums[mid] > target) {
                right = mid - 1;
            } else {
                // nums[mid] == target
                if (flag) {
                    // 找左边界
                    if (mid == 0 || nums[mid - 1] < nums[mid]) {
                        return mid;
                    } else {
                        right = mid - 1;
                    }
                } else {
                    // 找右边界
                    if (mid == nums.length - 1 || nums[mid] < nums[mid + 1]) {
                        return mid;
                    } else {
                        left = mid + 1;
                    }
                }
            }
        }

        return -1;
    }
}

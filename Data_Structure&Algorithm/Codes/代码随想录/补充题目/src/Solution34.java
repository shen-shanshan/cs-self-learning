/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 34.在排序数组中查找元素的第一个和最后一个位置
 */
public class Solution34 {
    public int[] searchRange(int[] nums, int target) {
        if (nums.length == 0) {
            return new int[]{-1, -1};
        }

        // 找左边界
        int left = myBinarySearch(nums, target, 0);
        if (left == -1) {
            return new int[]{-1, -1};
        }

        // 找右边界
        int right = myBinarySearch(nums, target, 1);
        return new int[]{left, right};
    }

    public int myBinarySearch(int[] nums, int target, int flag) {
        int left = 0;
        int right = nums.length - 1;

        while (left <= right) {
            int mid = left + ((right - left) >> 1);

            // 找左边界
            if (flag == 0) {
                if (nums[mid] == target && (mid == 0 || nums[mid - 1] < target)) {
                    return mid;
                } else if (nums[mid] < target) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            // 找右边界
            if (flag == 1) {
                if (nums[mid] == target && (mid == nums.length - 1 || nums[mid + 1] > target)) {
                    return mid;
                }
                if (nums[mid] <= target) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }

        // 没找到
        return -1;
    }
}

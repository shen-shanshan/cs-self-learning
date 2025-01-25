/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {

    public int search(int[] nums, int target) {
        int len = nums.length;
        if (len == 0) {
            return 0;
        }
        int l = 0;
        int r = len - 1;

        int left = 0;
        int right = len - 1;
        // 找左边界
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] == target) {
                if (mid == 0 || nums[mid - 1] < target) {
                    l = mid;
                    break;
                }
                right = mid - 1;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        // 若不存在 target
        if (right < left) {
            return 0;
        }
        // 找右边界
        left = 0;
        right = len - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] == target) {
                if (mid == len - 1 || nums[mid + 1] > target) {
                    r = mid;
                    break;
                }
                left = mid + 1;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return r - l + 1;
    }

}

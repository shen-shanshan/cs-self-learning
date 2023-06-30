public class Solution {
    public int[] searchRange(int[] nums, int target) {
        int len = nums.length;
        if (len == 0) {
            return new int[]{-1, -1};
        }
        if (len == 1) {
            if (nums[0] == target) {
                return new int[]{0, 0};
            }
            return new int[]{-1, -1};
        }
        int l = -1;
        int r = -1;
        // 找左边界
        int left = 0;
        int right = len - 1;
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] < target) {
                left = mid + 1;
            } else if (nums[mid] > target) {
                right = mid;
            } else {
                // nums[mid] = target
                if ((mid > 0 && nums[mid - 1] < target) || mid == 0) {
                    l = mid;
                    break;
                }
                // nums[mid-1] = target
                right = mid;
            }
        }
        if (nums[left] == target && ((left > 0 && nums[left - 1] < target) || left == 0)) {
            l = left;
        }
        // 找右边界
        left = 0;
        right = len - 1;
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] < target) {
                left = mid + 1;
            } else if (nums[mid] > target) {
                right = mid;
            } else {
                // nums[mid] = target
                if ((mid + 1 < len && nums[mid + 1] > target) || mid == len - 1) {
                    r = mid;
                    break;
                }
                // nums[mid-1] = target
                left = mid + 1;
            }
        }
        if (nums[left] == target && ((left + 1 < len && nums[left + 1] > target) || left == len - 1)) {
            r = left;
        }
        // 返回结果
        return new int[]{l, r};
    }
}

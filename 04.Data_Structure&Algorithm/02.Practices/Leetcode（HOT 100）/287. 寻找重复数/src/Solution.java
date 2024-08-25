import java.util.Arrays;

public class Solution {
    public int findDuplicate(int[] nums) {
        // 排序
        Arrays.sort(nums);
        // 二分查找
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            if ((mid - 1 >= 0 && nums[mid] == nums[mid - 1]) || (mid + 1 < nums.length && nums[mid] == nums[mid + 1])) {
                // 找到了重复的数
                return nums[mid];
            }
            if (mid < nums[mid]) {
                // 重复的数在 mid 的右边
                left = mid;
            } else {
                // 重复的数是 mid 或在 mid 左边
                right = mid;
            }
        }
        return 0;
    }
}
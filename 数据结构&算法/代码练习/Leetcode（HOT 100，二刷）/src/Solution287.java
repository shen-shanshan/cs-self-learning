import java.util.Arrays;

/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 287.寻找重复数
 */
public class Solution287 {
    public int findDuplicate(int[] nums) {
        Arrays.sort(nums);

        int left = 0;
        int right = nums.length - 1;

        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] > mid) {
                // 重复的数在 mid 右边
                left = mid + 1;
            } else {
                // 重复的数是 mid 或在 mid 左边
                right = mid - 1;
            }
        }

        return nums[left];
    }
}

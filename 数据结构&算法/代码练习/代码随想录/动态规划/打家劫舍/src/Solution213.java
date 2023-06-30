/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 213.打家劫舍 II
 */
public class Solution213 {
    public int rob(int[] nums) {
        if (nums.length == 1) {
            return nums[0];
        } else if (nums.length == 2) {
            return Math.max(nums[0], nums[1]);
        }

        // 考虑第一个
        int ans1 = dp(nums, 0, nums.length - 2);
        // 不考虑第一个
        int ans2 = dp(nums, 1, nums.length - 1);

        return Math.max(ans1, ans2);
    }

    public int dp(int[] arr, int start, int end) {
        int a = arr[start];
        int b = Math.max(arr[start], arr[start + 1]);

        if (end - start == 1) {
            return b;
        }

        int c = 0;
        for (int i = start + 2; i <= end; i++) {
            c = Math.max(a + arr[i], b);
            a = b;
            b = c;
        }

        return c;
    }
}

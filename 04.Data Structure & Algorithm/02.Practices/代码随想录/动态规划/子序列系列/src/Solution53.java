/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 53.最大子数组和
 */
public class Solution53 {
    public int maxSubArray(int[] nums) {
        int len = nums.length;

        int[] dp = new int[len];
        dp[0] = nums[0];

        int max = nums[0];

        for (int i = 1; i < len; i++) {
            dp[i] = Math.max(dp[i - 1], 0) + nums[i];
            max = Math.max(max, dp[i]);
        }

        return max;
    }
}

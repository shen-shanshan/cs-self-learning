/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 494.目标和
 */
public class Solution494 {
    public int findTargetSumWays(int[] nums, int target) {
        if (nums.length == 0) {
            return 0;
        }

        int sum = 0;
        for (int x : nums) {
            sum += x;
        }
        if ((sum + target) % 2 == 1) {
            return 0;
        }
        int n = Math.abs((sum + target) / 2);

        int[] dp = new int[n + 1];
        dp[0] = 1;

        for (int i = 0; i < nums.length; i++) {
            for (int j = n; j >= nums[i]; j--) {
                dp[j] += dp[j - nums[i]];
            }
        }

        return dp[n];
    }
}

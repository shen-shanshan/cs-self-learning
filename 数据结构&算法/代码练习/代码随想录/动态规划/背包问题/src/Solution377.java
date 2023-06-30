/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 377.组合总和 Ⅳ
 */
public class Solution377 {
    public int combinationSum4(int[] nums, int target) {
        if (nums.length == 0) {
            return 0;
        }

        int[] dp = new int[target + 1];
        dp[0] = 1;

        for (int i = 1; i <= target; i++) {
            for (int j = 0; j < nums.length; j++) {
                if (i - nums[j] >= 0) {
                    dp[i] += dp[i - nums[j]];
                }
            }
        }

        return dp[target];
    }
}

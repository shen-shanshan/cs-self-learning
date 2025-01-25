// 动态规划
public class Solution2 {
    public int findTargetSumWays(int[] nums, int target) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        int diff = sum - target;
        // sum - target 必须是非负偶数
        if (diff < 0 || diff % 2 != 0) {
            return 0;
        }
        int neg = diff / 2;

        /*int n = nums.length, neg = diff / 2;
        int[][] dp = new int[n + 1][neg + 1];
        dp[0][0] = 1;
        for (int i = 1; i <= n; i++) {
            int num = nums[i - 1];
            for (int j = 0; j <= neg; j++) {
                // 不选取 num
                dp[i][j] = dp[i - 1][j];
                if (j >= num) {
                    // 加上选取 num 的情况数
                    dp[i][j] += dp[i - 1][j - num];
                }
            }
        }
        return dp[n][neg];*/

        int[] dp = new int[neg + 1];
        dp[0] = 1;
        for (int num : nums) {
            // 当 j < num 时，不需要更新 dp[j]，dp[j] 和上一层相同
            for (int j = neg; j >= num; j--) {
                dp[j] += dp[j - num];
            }
        }
        return dp[neg];
    }
}
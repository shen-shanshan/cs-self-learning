/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 322.零钱兑换
 */
public class Solution322 {
    public int coinChange(int[] coins, int amount) {
        int len = coins.length;

        int[] dp = new int[amount + 1];

        for (int i = 1; i <= amount; i++) {
            dp[i] = Integer.MAX_VALUE;
        }

        for (int i = 0; i < len; i++) {
            for (int j = coins[i]; j <= amount; j++) {
                if (dp[j - coins[i]] != Integer.MAX_VALUE) {
                    dp[j] = Math.min(dp[j], dp[j - coins[i]] + 1);
                }
            }
        }

        return dp[amount] == Integer.MAX_VALUE ? -1 : dp[amount];
    }
}

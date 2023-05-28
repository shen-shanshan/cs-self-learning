/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 309.最佳买卖股票时机含冷冻期
 */
public class Solution309 {
    public int maxProfit(int[] prices) {
        int len = prices.length;

        // dp[i][0]: 买入
        // dp[i][1]: 卖出，即冷冻期
        // dp[i][2]: 不持有股票
        int[][] dp = new int[len][3];

        dp[0][0] = -prices[0];

        for (int i = 1; i < len; i++) {
            dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][2] - prices[i]);
            dp[i][1] = dp[i - 1][0] + prices[i];
            dp[i][2] = Math.max(dp[i - 1][1], dp[i - 1][2]);
        }

        return Math.max(dp[len - 1][1], dp[len - 1][2]);
    }
}

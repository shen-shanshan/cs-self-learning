/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 714.买卖股票的最佳时机含手续费
 */
public class Solution714 {
    public int maxProfit(int[] prices, int fee) {
        int len = prices.length;

        int[][] dp = new int[len][2];

        dp[0][0] = -prices[0];

        for (int i = 1; i < len; i++) {
            dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1] - prices[i]);
            dp[i][1] = Math.max(dp[i - 1][0] + prices[i] - fee, dp[i - 1][1]);
        }

        return dp[len - 1][1];
    }
}

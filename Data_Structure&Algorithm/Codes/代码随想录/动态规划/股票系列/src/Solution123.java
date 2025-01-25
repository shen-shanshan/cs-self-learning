/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 123.买卖股票的最佳时机 III
 */
public class Solution123 {
    public int maxProfit(int[] prices) {
        int len = prices.length;

        // dp[i][0]: 第一次买入
        // dp[i][1]: 第一次卖出
        // dp[i][2]: 第二次买入
        // dp[i][3]: 第二次卖出
        int[][] dp = new int[len][4];

        dp[0][0] = -prices[0];
        dp[0][2] = -prices[0];

        for (int i = 1; i < len; i++) {
            dp[i][0] = Math.max(dp[i - 1][0], -prices[i]);
            dp[i][1] = Math.max(dp[i - 1][0] + prices[i], dp[i - 1][1]);
            dp[i][2] = Math.max(dp[i - 1][1] - prices[i], dp[i - 1][2]);
            dp[i][3] = Math.max(dp[i - 1][2] + prices[i], dp[i - 1][3]);
        }

        return Math.max(dp[len - 1][1], dp[len - 1][3]);
    }
}

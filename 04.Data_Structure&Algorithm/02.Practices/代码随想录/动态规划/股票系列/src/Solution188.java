/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 188.买卖股票的最佳时机 IV
 */
public class Solution188 {
    public int maxProfit(int k, int[] prices) {
        int len = prices.length;

        int[][] dp = new int[len][k * 2];

        for (int i = 0; i < k * 2; i += 2) {
            dp[0][i] = -prices[0];
        }

        for (int i = 1; i < len; i++) {
            dp[i][0] = Math.max(dp[i - 1][0], -prices[i]);
            dp[i][1] = Math.max(dp[i - 1][0] + prices[i], dp[i - 1][1]);
            for (int j = 2; j < k * 2; j += 2) {
                dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - 1] - prices[i]);
                dp[i][j + 1] = Math.max(dp[i - 1][j] + prices[i], dp[i - 1][j + 1]);
            }
        }

        int max = 0;
        for (int i = 0; i < k * 2; i += 2) {
            max = Math.max(max, dp[len - 1][i + 1]);
        }

        return max;
    }
}

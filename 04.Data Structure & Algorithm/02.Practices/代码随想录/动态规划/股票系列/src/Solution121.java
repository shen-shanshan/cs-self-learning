/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 121.买卖股票的最佳时机
 */
public class Solution121 {
    public int maxProfit(int[] prices) {
        int min = prices[0];

        int[] dp = new int[prices.length];
        dp[0] = 0;

        for (int i = 1; i < prices.length; i++) {
            dp[i] = Math.max(dp[i - 1], prices[i] - min);
            min = Math.min(min, prices[i]);
        }

        return dp[prices.length - 1];
    }
}

package editor.cn;

// 188.买卖股票的最佳时机 IV
class Solution188 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int maxProfit(int k, int[] prices) {
            if (prices.length == 0) {
                return 0;
            }

            int[][] dp = new int[prices.length][k * 2 + 1];
            for (int i = 1; i < k * 2; i += 2) {
                dp[0][i] = -prices[0];
            }

            for (int i = 1; i < prices.length; i++) {
                for (int j = 1; j < k * 2; j += 2) {
                    dp[i][j] = Math.max(dp[i - 1][j - 1] - prices[i], dp[i - 1][j]);
                    dp[i][j + 1] = Math.max(dp[i - 1][j] + prices[i], dp[i - 1][j + 1]);
                }
            }

            return dp[prices.length - 1][k * 2];
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
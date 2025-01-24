package editor.cn;

// 123.买卖股票的最佳时机 III
class Solution123 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int maxProfit(int[] prices) {
            int len = prices.length;

            int[][] dp = new int[len][5];
            dp[0][1] = -prices[0];
            dp[0][3] = -prices[0];

            for (int i = 1; i < len; i++) {
                // 第一次持有股票
                dp[i][1] = Math.max(dp[i - 1][0] - prices[i], dp[i - 1][1]);
                // 第一次卖出股票
                dp[i][2] = Math.max(dp[i - 1][1] + prices[i], dp[i - 1][2]);
                // 第二次持有股票
                dp[i][3] = Math.max(dp[i - 1][2] - prices[i], dp[i - 1][3]);
                // 第二次卖出
                dp[i][4] = Math.max(dp[i - 1][3] + prices[i], dp[i - 1][4]);
            }

            return dp[len - 1][4];
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
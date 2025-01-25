package editor.cn;

// 122.买卖股票的最佳时机 II
class Solution122 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int maxProfit(int[] prices) {
            int[][] dp = new int[2][2];
            dp[0][0] = -prices[0];
            dp[0][1] = 0;

            for (int i = 1; i < prices.length; i++) {
                // 目前持有股票
                dp[i % 2][0] = Math.max(dp[(i - 1) % 2][0], dp[(i - 1) % 2][1] - prices[i]);
                // 目前不持有股票
                dp[i % 2][1] = Math.max(dp[(i - 1) % 2][1], dp[(i - 1) % 2][0] + prices[i]);
            }

            return dp[(prices.length - 1) % 2][1];
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
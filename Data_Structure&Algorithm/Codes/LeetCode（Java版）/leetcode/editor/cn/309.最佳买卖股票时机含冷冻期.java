package editor.cn;

// 309.最佳买卖股票时机含冷冻期
class Solution309 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int maxProfit(int[] prices) {
            int len = prices.length;

            int[][] dp = new int[len][3];
            dp[0][0] = -prices[0];

            for (int i = 1; i < len; i++) {
                // 当前持有股票
                dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1] - prices[i]);
                // 当前不持有股票，并且不处于冷冻期
                dp[i][1] = Math.max(dp[i - 1][1], dp[i - 1][2]);
                // 当前处于冷冻期
                dp[i][2] = dp[i - 1][0] + prices[i];
            }

            return Math.max(dp[len - 1][1], dp[len - 1][2]);
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}
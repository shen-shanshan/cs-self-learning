package editor.cn;

// 714.买卖股票的最佳时机含手续费
class Solution714 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int maxProfit(int[] prices, int fee) {
            // 动态规划
            int len = prices.length;

            int[][] dp = new int[len][2];
            dp[0][0] = -prices[0];

            for (int i = 1; i < len; i++) {
                // 当前持有股票
                dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1] - prices[i]);
                // 当前不持有股票
                dp[i][1] = Math.max(dp[i - 1][0] + prices[i] - fee, dp[i - 1][1]);
            }

            return dp[len - 1][1];


            // 贪心算法
            /*int profit = 0;

            // 当前的买入价格，需要加上手续费
            int buy = prices[0] + fee;

            for (int i = 0; i < prices.length; i++) {
                if (prices[i] + fee < buy) {
                    // 找到最小的买入价格
                    buy = prices[i] + fee;
                } else if (prices[i] > buy) {
                    // 买入股票，在价格上升的过程中就不断计算利润
                    profit += prices[i] - buy;
                    // 在买入的时候就已经计算了 fee，在卖出之前后续更新的 buy 不用再加上 fee
                    buy = prices[i];
                }
            }

            return profit;*/
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
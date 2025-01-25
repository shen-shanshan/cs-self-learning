package editor.cn;

// 121.买卖股票的最佳时机
class Solution121 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int maxProfit(int[] prices) {
            int profit = 0;
            int buy = Integer.MAX_VALUE;

            for (int i = 0; i < prices.length; i++) {
                if (prices[i] > buy) {
                    profit = Math.max(profit, prices[i] - buy);
                }
                buy = Math.min(buy, prices[i]);
            }

            return profit;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
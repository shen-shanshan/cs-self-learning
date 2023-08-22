public class Solution {
    public int maxProfit(int[] prices) {
        // dp 数组表示前 i 天中所能获得的最大利润
        int dp = 0;
        // 记录前 i 天中出现的最低价
        int min = prices[0];
        // 遍历数组
        for (int i = 1; i < prices.length; i++) {
            dp = Math.max(dp, prices[i] - min);
            min = Math.min(min, prices[i]);
        }
        return dp;
    }
}

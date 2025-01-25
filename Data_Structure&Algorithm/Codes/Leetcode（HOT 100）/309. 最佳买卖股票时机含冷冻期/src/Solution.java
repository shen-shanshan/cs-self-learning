/*
* 动态规划：
* 每一天可以分为三种不同的状态：
* （1）目前持有一支股票，对应的「累计最大收益」记为 f[i][0]；
* （2）目前不持有任何股票，并且处于冷冻期中，对应的「累计最大收益」记为 f[i][1]；
* （3）目前不持有任何股票，并且不处于冷冻期中，对应的「累计最大收益」记为 f[i][2]。
* 这里的「处于冷冻期」指的是在第 i 天结束之后的状态。
* 也就是说：如果第 i 天结束之后处于冷冻期，那么第 i+1 天无法买入股票。
* 每一天的状态只与前一天的 3 种状态相关。
*/
public class Solution {
    public int maxProfit(int[] prices) {
        if (prices.length == 0) {
            return 0;
        }

        int n = prices.length;
        int f0 = -prices[0];
        int f1 = 0;
        int f2 = 0;
        for (int i = 1; i < n; ++i) {
            int newf0 = Math.max(f0, f2 - prices[i]);
            int newf1 = f0 + prices[i];
            int newf2 = Math.max(f1, f2);
            f0 = newf0;
            f1 = newf1;
            f2 = newf2;
        }

        return Math.max(f1, f2);
    }
}
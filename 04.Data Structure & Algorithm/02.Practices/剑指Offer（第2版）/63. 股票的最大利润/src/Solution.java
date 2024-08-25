/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {
    public int maxProfit(int[] prices) {
        // 记录前 i 日的最低价，在遍历数组时动态更新。
        int cost = Integer.MAX_VALUE;
        // 记录前 i 日的最大利润。
        int profit = 0;
        for (int price : prices) {
            cost = Math.min(cost, price);
            profit = Math.max(profit, price - cost);
        }
        return profit;
    }
}

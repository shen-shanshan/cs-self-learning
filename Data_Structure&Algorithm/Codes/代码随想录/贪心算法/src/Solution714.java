/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 714.买卖股票的最佳时机含手续费
 */
public class Solution714 {
    public int maxProfit(int[] prices, int fee) {
        // 在买入股票时，就将手续费也算入成本
        int buy = prices[0] + fee;

        int sum = 0;

        for (int p : prices) {
            if (p + fee < buy) {
                // 买入新股票
                buy = p + fee;
            } else if (p > buy) {
                // 计算利润
                sum += p - buy;
                // 下次计算利润时不用再考虑手续费
                buy = p;
            }
        }

        return sum;
    }
}

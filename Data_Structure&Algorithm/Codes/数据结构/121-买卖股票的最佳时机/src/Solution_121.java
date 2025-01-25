public class Solution_121 {

    public int maxProfit(int[] prices) {
        // dp数组表示前 i天中所能获得的最大利润
        int dp = 0;
        // 记录前 i天中出现的最低价
        int min = prices[0];
        // 遍历数组
        for (int i = 1; i < prices.length; i++) {
            dp = Math.max(dp, prices[i] - min);
            min = Math.min(min, prices[i]);
        }
        return dp;
    }

    public static void main(String[] args) {
        int[] prices1 = {7, 1, 5, 3, 6, 4}; // 5
        int[] prices2 = {7, 6, 4, 3, 1}; // 0

        Solution_121 s = new Solution_121();
        int max = s.maxProfit(prices2);
        System.out.println(max);
    }
}

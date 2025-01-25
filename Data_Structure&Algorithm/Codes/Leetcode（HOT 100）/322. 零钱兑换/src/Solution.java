// 递归：超时了！！！
public class Solution {
    public int coinChange(int[] coins, int amount) {
        if (amount == 0) {
            return 0;
        }
        int len = coins.length;
        int count = Integer.MAX_VALUE;
        for (int i = 0; i < len; i++) {
            if (coins[i] <= amount) {
                int tmp = coinChange(coins, amount - coins[i]);
                if (tmp != -1) {
                    count = Math.min(count, tmp) + 1;
                }
            }
        }
        if (count == Integer.MAX_VALUE) {
            return -1;
        }
        return count;
    }
}
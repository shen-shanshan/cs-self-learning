import java.util.Arrays;

// 动态规划
public class Solution2 {
    public int coinChange(int[] coins, int amount) {
        int len = coins.length;
        Arrays.sort(coins);
        int[] dp = new int[amount + 1];
        dp[0] = 0;
        for (int i = 1; i <= amount; i++) {
            int count = Integer.MAX_VALUE;
            for (int j = 0; j < len && coins[j] <= i; j++) {
                int tmp = dp[i - coins[j]];
                if (tmp != -1) {
                    count = Math.min(count, tmp+1);
                }
            }
            if (count == Integer.MAX_VALUE) {
                dp[i] = -1;
            } else {
                dp[i] = count;
            }
        }
        return dp[amount];
    }
}
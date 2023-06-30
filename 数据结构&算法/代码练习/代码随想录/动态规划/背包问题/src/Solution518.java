/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 518.零钱兑换 II
 */
public class Solution518 {
    public int change(int amount, int[] coins) {
        if (coins.length == 0) {
            return 0;
        }

        int[] dp = new int[amount + 1];
        dp[0] = 1;

        for (int i = 0; i < coins.length; i++) {
            for (int j = coins[i]; j <= amount; j++) {
                dp[j] += dp[j - coins[i]];
            }
        }

        return dp[amount];
    }
}

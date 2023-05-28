package editor.cn;

import java.lang.annotation.Target;

// 322.零钱兑换
class Solution322 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int coinChange(int[] coins, int amount) {
            if (amount == 0) {
                return 0;
            }

            int[] dp = new int[amount + 1];
            dp[0] = 0;
            for (int i = 1; i <= amount; i++) {
                dp[i] = Integer.MAX_VALUE;
            }

            for (int i = 0; i < coins.length; i++) {
                for (int j = coins[i]; j <= amount; j++) {
                    if (dp[j - coins[i]] != Integer.MAX_VALUE) {
                        dp[j] = Math.min(dp[j], dp[j - coins[i]] + 1);
                    }
                }
            }

            return dp[amount] == Integer.MAX_VALUE ? -1 : dp[amount];
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}
package editor.cn;

// 518.零钱兑换 II
class Solution518 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int change(int amount, int[] coins) {
            if (amount == 0) {
                return 1;
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
    //leetcode submit region end(Prohibit modification and deletion)

}
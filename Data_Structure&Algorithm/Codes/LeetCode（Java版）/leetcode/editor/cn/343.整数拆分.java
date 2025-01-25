package editor.cn;

// 343.整数拆分
class Solution343 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int integerBreak(int n) {
            int[] dp = new int[n + 1];
            dp[1] = 0;
            dp[2] = 1;

            for (int i = 3; i <= n; i++) {
                int max = 0;
                for (int j = 1; j <= i / 2; j++) {
                    // max = Math.max(dp[j] * dp[i - j], max);
                    max = Math.max(j * dp[i - j], max);
                    // max = Math.max(dp[j] * (i - j), max);
                    max = Math.max(j * (i - j), max);
                }
                dp[i] = max;
            }

            return dp[n];
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
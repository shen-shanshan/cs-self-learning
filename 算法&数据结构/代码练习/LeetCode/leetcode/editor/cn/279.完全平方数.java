package editor.cn;

// 279.完全平方数
class Solution279 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int numSquares(int n) {
            if (n <= 0) {
                return 0;
            }

            // 物品大小
            int num = 1;
            while (num * num <= n) {
                num++;
            }
            num--;

            int[] dp = new int[n + 1];
            dp[0] = 0;
            for (int i = 1; i <= n; i++) {
                dp[i] = Integer.MAX_VALUE;
            }

            for (int i = 1; i <= num; i++) {
                for (int j = i * i; j <= n; j++) {
                    dp[j] = Math.min(dp[j], dp[j - i * i] + 1);
                }
            }

            return dp[n];
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
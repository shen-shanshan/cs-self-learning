package editor.cn;

// 96.不同的二叉搜索树
class Solution96 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int numTrees(int n) {
            if (n == 1) {
                return 1;
            }
            int[] dp = new int[n + 1];
            dp[0] = 1;
            dp[1] = 1;
            dp[2] = 2;
            for (int i = 3; i <= n; i++) {
                int count = 0;
                for (int j = 1; j <= i; j++) {
                    count += dp[j - 1] * dp[i - j];
                }
                dp[i] = count;
            }
            return dp[n];
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
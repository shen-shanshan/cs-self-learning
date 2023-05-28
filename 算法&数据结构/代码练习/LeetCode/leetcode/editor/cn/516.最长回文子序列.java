package editor.cn;

// 516.最长回文子序列
class Solution516 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int longestPalindromeSubseq(String s) {
            int ans = 0;

            int len = s.length();

            int[][] dp = new int[len][len];

            for (int i = len - 1; i >= 0; i--) {
                for (int j = i; j < len; j++) {
                    int cur = 0;
                    if (i == j) {
                        cur = 1;
                    } else if (s.charAt(i) == s.charAt(j)) {
                        cur = dp[i + 1][j - 1] + 2;
                    } else {
                        cur = Math.max(dp[i + 1][j], dp[i][j - 1]);
                    }
                    dp[i][j] = cur;
                    ans = Math.max(ans, cur);

                }
            }

            return ans;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
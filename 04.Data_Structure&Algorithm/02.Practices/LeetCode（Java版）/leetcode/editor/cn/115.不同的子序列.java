package editor.cn;

// 115.不同的子序列
class Solution115 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int numDistinct(String s, String t) {
            if (s.length() < t.length()) {
                return 0;
            }

            int[][] dp = new int[s.length() + 1][t.length() + 1];

            for (int i = 0; i < s.length() + 1; i++) {
                dp[i][0] = 1;
            }

            for (int i = 0; i < s.length(); i++) {
                for (int j = 0; j < t.length(); j++) {
                    if (s.charAt(i) == t.charAt(j)) {
                        dp[i + 1][j + 1] = dp[i][j] + dp[i][j + 1];
                    } else {
                        dp[i + 1][j + 1] = dp[i][j + 1];
                    }
                }
            }

            return dp[s.length()][t.length()];
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
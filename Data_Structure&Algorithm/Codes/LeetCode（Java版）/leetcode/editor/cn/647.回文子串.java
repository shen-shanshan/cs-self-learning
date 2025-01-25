package editor.cn;

// 647.回文子串
class Solution647 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int countSubstrings(String s) {
            int ans = 0;

            int len = s.length();

            boolean[][] dp = new boolean[len][len];

            for (int i = len - 1; i >= 0; i--) {
                for (int j = i; j < len; j++) {
                    if (s.charAt(i) == s.charAt(j) && (j - i <= 1 || dp[i + 1][j - 1])) {
                        ans++;
                        dp[i][j] = true;
                    }
                }
            }

            return ans;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
package editor.cn;

// 474.一和零
class Solution474 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int findMaxForm(String[] strs, int m, int n) {
            int[][] dp = new int[m + 1][n + 1];

            for (int i = 0; i < strs.length; i++) {
                String cur = strs[i];

                // 统计每个字符串中 0、1 的数量
                int zero = 0;
                int one = 0;
                for (int j = 0; j < cur.length(); j++) {
                    if (cur.charAt(j) == '0') {
                        zero++;
                    } else {
                        one++;
                    }
                }

                // 二维背包
                for (int j = m; j >= zero; j--) {
                    for (int k = n; k >= one; k--) {
                        dp[j][k] = Math.max(dp[j][k], dp[j - zero][k - one] + 1);
                    }
                }
            }

            return dp[m][n];
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
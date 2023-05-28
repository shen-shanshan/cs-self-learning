/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 5.最长回文子串
 */
public class Solution5 {
    public String longestPalindrome(String s) {
        int len = s.length();

        boolean[][] dp = new boolean[len + 1][len + 1];

        int max = 0;

        String ans = "";

        for (int i = len; i > 0; i--) {
            for (int j = i; j <= len; j++) {
                if (s.charAt(i - 1) == s.charAt(j - 1)) {
                    if (j - i <= 1) {
                        dp[i][j] = true;
                    } else {
                        dp[i][j] = dp[i + 1][j - 1];
                    }
                }
                if (dp[i][j]) {
                    max = Math.max(max, j - i + 1);
                    if (max == j - i + 1) {
                        ans = s.substring(i - 1, j);
                    }
                }
            }
        }

        return ans;
    }
}

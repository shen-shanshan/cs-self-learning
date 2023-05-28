public class Solution {
    public int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];
        dp[0][0] = 1;
        // 初始化第一行
        for (int i = 1; i < n; i++) {
            dp[0][i] = 1;
        }
        // 初始化第一列
        for (int i = 1; i < m; i++) {
            dp[i][0] = 1;
        }
        // 遍历
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        }
        return dp[m - 1][n - 1];
    }
}
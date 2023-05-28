/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 221.最大正方形
 */
public class Solution221 {
    public int maximalSquare(char[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;

        // 以当前元素为正方形右下角时，所能组成的最大边长
        int[][] dp = new int[m][n];

        int maxEdge = 0;

        for (int i = 0; i < m; i++) {
            if (matrix[i][0] == '1') {
                dp[i][0] = 1;
                maxEdge = Math.max(maxEdge, dp[i][0]);
            }
        }
        for (int i = 0; i < n; i++) {
            if (matrix[0][i] == '1') {
                dp[0][i] = 1;
                maxEdge = Math.max(maxEdge, dp[0][i]);
            }
        }

        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (matrix[i][j] == '0') {
                    dp[i][j] = 0;
                } else {
                    int tmp = Math.min(dp[i - 1][j], dp[i][j - 1]);
                    tmp = Math.min(tmp, dp[i - 1][j - 1]);
                    dp[i][j] = tmp + 1;
                }
                maxEdge = Math.max(maxEdge, dp[i][j]);
            }
        }

        return maxEdge * maxEdge;
    }
}

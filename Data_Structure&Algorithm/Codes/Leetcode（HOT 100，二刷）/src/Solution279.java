/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 279.完全平方数
 */
public class Solution279 {
    public int numSquares(int n) {
        int[] dp = new int[n + 1];

        dp[0] = 1;
        dp[1] = 1;

        for (int i = 2; i <= n; i++) {
            int min = Integer.MAX_VALUE;
            for (int j = 1; j * j < i; j++) {
                min = Math.min(min, dp[j * j] + dp[i - j * j]);
            }
            dp[i] = min;
            // System.out.println("i = " + i + ", dp[i] = " + min);
        }

        return dp[n];
    }
}

/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 343.整数拆分
 */
public class Solution343 {
    public int integerBreak(int n) {
        int[] dp = new int[n + 1];

        dp[1] = 1;
        dp[2] = 1;

        for (int i = 3; i <= n; i++) {
            for (int j = 1; j < i; j++) {
                dp[i] = Math.max(dp[i], j * (Math.max(dp[i - j], i - j)));
            }
        }

        return dp[n];
    }
}

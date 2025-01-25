public class Solution {
    // n >= 2，至少分成两段
    public int cuttingRope(int n) {
        int[] dp = new int[n + 1];
        dp[1] = 1;
        dp[2] = 1;
        for (int i = 3; i <= n; i++) {
            int max = 0;
            // 将绳子剪为两段，第一段只用考虑到 <= i/2 的时候
            for (int j = 1; j <= i / 2; j++) {
                // 1.第二段继续剪成 2 段以上
                max = Math.max(max, j * dp[i - j]);
                // 2.第二段不剪了
                // 比如说对于 3，计算它自己的最大乘积时，必须拆成两个数以上，即 1 和 2，最大乘积为 2
                // 但是在比 3 大的数再次求 3 的最大乘积时，此时 3 可以不拆，直接就是 3
                max = Math.max(max, j * (i - j));
            }
            dp[i] = max;
        }
        return dp[n];
    }
}
/*
j<=i/2，并不是因为前后重复了，而是因为前面一定大于后面。
例如，当i=10，j=4时，dp[i]=max(4*6, 4*dp[6])；
当i=10，j=6时，dp[i]=max(6*4, 6*dp[4])，
但是4*dp[6]一定大于等于6*dp[4]，
因为dp[6]=max(2*4, 2*dp[4])>=2*dp[4]，所以4*dp[6]>=8*dp[4]。
*/
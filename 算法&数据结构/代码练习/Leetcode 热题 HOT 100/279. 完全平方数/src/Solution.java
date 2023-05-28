/*
* 动态规划
* f[i] 表示最少需要多少个数的平方来表示整数 i，这些数必然落在区间 [1,根号n]。
* 我们可以枚举这些数，假设当前枚举到 j，那么我们还需要取若干数的平方，构成 i-j^2。
* */
public class Solution {
    public int numSquares(int n) {
        int[] f = new int[n + 1];
        f[0] = 0;
        for (int i = 1; i <= n; i++) {
            int minn = Integer.MAX_VALUE;
            for (int j = 1; j * j <= i; j++) {
                minn = Math.min(minn, f[i - j * j]);
            }
            f[i] = minn + 1;
        }
        return f[n];
    }
}
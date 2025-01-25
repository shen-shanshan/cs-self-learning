/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution2 {
    // 快速幂算法
    public double myPow(double x, int n) {
        /**
         * Integer.MAXVALUE 为 2^31 - 1
         * 当 n 为负数最小值：-2^31 时，取反后会发生溢出
         * 因此需要用 long 类型来接收 n 的值
         */
        long N = (long) n;
        if (N < 0) {
            N = -N;
            x = x == 0.0 ? 0.0 : 1.0 / x;
        }
        // n >= 0
        return process(x, N);
    }

        public double process(double x, long N) {
        // baser case
        if (N == 0) {
            return 1.0;
        }
        long mid = N / 2;
        double extra = 1.0;
        // 若 N 为奇数，则需要多乘一个 x
        if (N % 2 == 1) {
            extra = x;
        }
        double cur = process(x, mid);
        return cur * cur * extra;
    }
}

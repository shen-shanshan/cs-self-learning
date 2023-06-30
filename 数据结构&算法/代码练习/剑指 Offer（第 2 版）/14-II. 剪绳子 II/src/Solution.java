// 贪心算法
public class Solution {
    public int cuttingRope(int n) {
        if (n <= 3) {
            return n - 1;
        }
        // 3 为最优的子段长度，应尽量以 3 为长度进行拆分
        // n = 3 * a + b
        long res = 1;
        while (n > 4) {
            res = res * 3 % 1000000007;
            n -= 3;
        }
        // 若最后 n = 4，应拆分为 2 * 2，即直接再乘 4
        // 若最后 n = 3，不再拆分
        // 若最后 n = 2，不再拆分
        // 若最后 n = 1，不再拆分
        return (int) (res * n % 1000000007);
    }
}

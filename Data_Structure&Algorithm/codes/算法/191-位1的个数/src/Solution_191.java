public class Solution_191 {
    // you need to treat n as an unsigned value
    public int hammingWeight(int n) {
        int ret = 0;
        // 方法一：循环检查二进制位
        // 我们可以直接循环检查给定整数 n 的二进制位的每一位是否为 1。
        // 具体代码中，当检查第 i 位时，我们可以让 n 与 2^i 进行与运算。
        // 当且仅当 n 的第 i 位为 1 时，运算结果不为 0。
        for (int i = 0; i < 32; i++) {
            if ((n & (1 << i)) != 0) {
                ret++;
            }
        }
        // 方法二：位运算优化
        // 运算 n & (n−1) 的结果恰为把 n 的二进制位中的最低位的 1 变为 0 之后的结果。
        // 在实际代码中，我们不断让当前的 n 与 n - 1 做与运算，直到 n 变为 0 即可。
        // 因为每次运算会使得 n 的最低位的 1 被翻转，因此运算次数就等于 n 的二进制位中 1 的个数。
        ret = 0;
        while (n != 0) {
            n &= n - 1;
            ret++;
        }
        return ret;
    }

    public static void main(String[] args) {
        int n1 = 0b00000000000000000000000000001011;
        int n2 = 0b11111111111111111111111111111101;

        Solution_191 s = new Solution_191();
        int res1 = s.hammingWeight(n1);
        int res2 = s.hammingWeight(n2);
        System.out.println(res1);
        System.out.println(res2);
    }
}

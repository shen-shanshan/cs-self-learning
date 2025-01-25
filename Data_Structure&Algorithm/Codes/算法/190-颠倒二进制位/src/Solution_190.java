public class Solution_190 {
    // you need treat n as an unsigned value
    // 在某些语言（如 Java）中，没有无符号整数类型，因此对 n 的右移操作应使用逻辑右移（>>>）。

    // 方法一：逐位颠倒
    // 将 n 视作一个长为 32 的二进制串，从低位往高位枚举 n 的每一位，将其倒序添加到翻转结果 rev 中。
    // 代码实现中，每枚举一位就将 n 右移一位，这样当前 n 的最低位就是我们要枚举的比特位。
    // 当 n 为 0 时即可结束循环。
    public int reverseBits1(int n) {
        int rev = 0;
        for (int i = 0; i < 32 && n != 0; ++i) {
            rev |= (n & 1) << (31 - i);
            n >>>= 1;
        }
        return rev;
    }

    // 方法二：位运算分治

    public static void main(String[] args) {
        int n1 = 0b00000010100101000001111010011100;
        int n2 = 0b11111111111111111111111111111101;

        Solution_190 s = new Solution_190();
        int res1 = s.reverseBits1(n1);
        int res2 = s.reverseBits1(n2);
        System.out.println(res1);
        System.out.println(res2);
    }
}

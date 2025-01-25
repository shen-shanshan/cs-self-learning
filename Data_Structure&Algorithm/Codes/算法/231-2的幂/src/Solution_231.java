public class Solution_231 {
    // 递归：
    /*public boolean isPowerOfTwo(int n) {
        if (n <= 0) return false;
        if(n == 1) return true;
        if(n%2 == 0) return isPowerOfTwo(n>>1);
        return false;
    }*/

    // 位运算：
    // 一个数 n 是 2 的幂：当且仅当 n 是正整数，并且 n 的二进制表示中仅包含 1 个 1。
    // 因此我们可以考虑使用位运算，将 n 的二进制表示中最低位的那个 1 提取出来，再判断剩余的数值是否为 0 即可。
    // 下面介绍两种常见的与「二进制表示中最低位」相关的位运算技巧。
    // 方法一：n & (n - 1)
    // 其中 & 表示按位与运算。该位运算技巧可以直接将 n 二进制表示的最低位 1 移除。
    // 因此，如果 n 是正整数并且 n & (n - 1) = 0，那么 n 就是 2 的幂。
    public boolean isPowerOfTwo1(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }
    // 方法二：n & (-n)
    // 该位运算技巧可以直接获取 n 二进制表示的最低位的 1。
    // 由于负数是按照补码规则在计算机中存储的，−n 的二进制表示为 n 的二进制表示的每一位取反再加上 1。
    // 因此，如果 n 是正整数并且 n & (-n) = n，那么 n 就是 2 的幂。
    public boolean isPowerOfTwo2(int n) {
        return n > 0 && (n & -n) == n;
    }

    public static void main(String[] args) {
        int n = 6;
        Solution_231 s = new Solution_231();
        boolean res = s.isPowerOfTwo1(n);
        System.out.println(res);
    }
}

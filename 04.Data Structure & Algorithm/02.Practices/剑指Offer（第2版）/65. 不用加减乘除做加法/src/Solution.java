/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {

    public int add(int a, int b) {
        // s = a + b
        // s = (a ^ b) + ((a & b) << 1) = 非进位和 + 进位
        // 当进位为 0 时跳出
        while(b != 0) {
            // c = 进位
            int c = (a & b) << 1;
            // a = 非进位和
            a ^= b;
            // b = 进位
            b = c;
        }
        return a;
    }

}

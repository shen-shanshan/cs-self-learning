/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {
    public int[] singleNumbers(int[] nums) {
        // 计算所有数的异或结果
        int ret = 0;
        for (int n : nums) {
            ret ^= n;
        }
        // 找到异或结果中不为 0 的最低位
        int div = 1;
        while ((div & ret) == 0) {
            div <<= 1;
        }
        // 分组异或
        int a = 0, b = 0;
        for (int n : nums) {
            if ((div & n) != 0) {
                a ^= n;
            } else {
                b ^= n;
            }
        }
        return new int[]{a, b};
    }
}

/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {

    public int[] constructArr(int[] a) {
        int len = a.length;
        if (len == 0) {
            return new int[]{};
        }
        int[] ans = new int[len];
        // 1.计算每一个位置左边所有数的乘积
        ans[0] = 1;
        for (int i = 1; i < len; i++) {
            ans[i] = ans[i - 1] * a[i - 1];
        }
        // 从右往左遍历，乘上每个位置右边所有数的乘积
        int right = 1;
        for (int i = len - 1; i >= 0; i--) {
            ans[i] *= right;
            right *= a[i];
        }
        return ans;
    }

}

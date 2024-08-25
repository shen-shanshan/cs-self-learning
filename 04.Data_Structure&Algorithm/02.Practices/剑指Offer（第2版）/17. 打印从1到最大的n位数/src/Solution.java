/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {
    public int[] printNumbers(int n) {
        int len = 1;
        while (n > 0) {
            len *= 10;
            n--;
        }
        int[] ans = new int[len - 1];
        for (int i = 0; i < len - 1; i++) {
            ans[i] = i + 1;
        }
        return ans;
    }
}

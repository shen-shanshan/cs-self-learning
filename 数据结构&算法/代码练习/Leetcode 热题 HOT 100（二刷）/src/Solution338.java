/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 338.比特位计数
 */
public class Solution338 {
    public int[] countBits(int n) {
        int[] ans = new int[n+1];

        int highBit = 0;

        for (int i = 1; i <= n; i++) {
            // i 是 2 的整数倍
            if ((i & (i - 1)) == 0) {
                highBit = i;
            }
            ans[i] = ans[i - highBit] + 1;
        }

        return ans;
    }
}

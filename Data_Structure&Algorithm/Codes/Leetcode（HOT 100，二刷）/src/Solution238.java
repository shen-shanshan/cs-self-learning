/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 238.除自身以外数组的乘积
 */
public class Solution238 {
    public int[] productExceptSelf(int[] nums) {
        int[] left = new int[nums.length];
        // 从左到右
        for (int i = 0; i < nums.length; i++) {
            if (i == 0) {
                left[i] = 1;
            } else {
                left[i] = left[i - 1] * nums[i - 1];
            }
        }

        int[] right = new int[nums.length];
        // 从右到左
        for (int i = nums.length - 1; i >= 0; i--) {
            if (i == nums.length - 1) {
                right[i] = 1;
            } else {
                right[i] = right[i + 1] * nums[i + 1];
            }
        }

        int[] ans = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            ans[i] = left[i] * right[i];
        }

        return ans;
    }
}

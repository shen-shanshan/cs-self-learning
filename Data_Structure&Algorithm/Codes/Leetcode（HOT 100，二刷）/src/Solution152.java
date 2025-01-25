/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 152.乘积最大子数组
 */
public class Solution152 {
    public int maxProduct(int[] nums) {
        int len = nums.length;

        int ans = nums[0];

        // 以当前位置结尾的子数组所能得到的最大乘积和最小乘积
        int curMax = nums[0];
        int curMin = nums[0];

        for (int i = 1; i < len; i++) {
            int lastMax = curMax;
            int lastMin = curMin;

            curMax = Math.max(lastMax * nums[i], lastMin * nums[i]);
            curMax = Math.max(curMax, nums[i]);

            curMin = Math.min(lastMax * nums[i], lastMin * nums[i]);
            curMin = Math.min(curMin, nums[i]);

            ans = Math.max(ans, curMax);
        }

        return ans;
    }
}

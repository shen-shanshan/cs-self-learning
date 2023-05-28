/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 55.跳跃游戏
 */
public class Solution55 {
    public boolean canJump(int[] nums) {
        int max = nums[0];

        for (int i = 1; i < nums.length; i++) {
            if (i <= max) {
                max = Math.max(max, i + nums[i]);
                if (max >= nums.length - 1) {
                    return true;
                }
            } else {
                return false;
            }
        }

        return true;
    }
}

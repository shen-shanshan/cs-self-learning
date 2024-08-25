/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 55.跳跃游戏
 */
public class Solution55 {
    public boolean canJump(int[] nums) {
        int len = nums.length;

        // 记录当前能到达的最大范围
        int maxIndex = 0;

        for (int i = 0; i < len; i++) {
            if (i <= maxIndex) {
                // 当前位置是可到达的
                maxIndex = Math.max(i + nums[i], maxIndex);
            } else {
                break;
            }
        }

        return maxIndex >= len - 1;
    }
}

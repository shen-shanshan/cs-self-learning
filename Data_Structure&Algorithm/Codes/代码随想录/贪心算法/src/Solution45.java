/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 45.跳跃游戏 II
 */
public class Solution45 {
    public int jump(int[] nums) {
        // 根本就不用跳
        if (nums.length == 1) {
            return 0;
        }

        int count = 1;

        int pre = 0;

        int maxIndex = nums[0];

        while (maxIndex < nums.length - 1) {
            int max = maxIndex;
            for (int j = pre + 1; j <= max; j++) {
                maxIndex = Math.max(maxIndex, j + nums[j]);
            }
            count++;
            pre = max;
        }

        return count;
    }
}

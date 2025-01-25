import java.util.Arrays;

/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {

    public boolean isStraight(int[] nums) {
        // len = 5
        // 0 <= num <= 13

        // 排序
        Arrays.sort(nums);

        // 关键：判断间隙是否能用 0 来填补
        int zero = 0;
        int gap = 0;
        for (int i = 0; i < 5; i++) {
            if (nums[i] == 0) {
                // 统计 0 的个数
                zero++;
            } else if (i != 0 && nums[i - 1] != 0) {
                // [0,0,2,2,5]
                // 连续的两个数字相等不算顺子，直接返回 false
                if (nums[i] == nums[i - 1]) {
                    return false;
                }
                // 统计间隙
                gap += nums[i] - nums[i - 1] - 1;
            }
        }
        return zero >= gap;
    }

}

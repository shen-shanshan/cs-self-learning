import java.util.Arrays;

/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 1005.K 次取反后最大化的数组和
 */
public class Solution1005 {
    public int largestSumAfterKNegations(int[] nums, int k) {
        int len = nums.length;

        Arrays.sort(nums);

        // 统计总和
        int sum = 0;

        // 绝对值最小的数
        int min = Integer.MAX_VALUE;

        // 优先反转绝对值大的负数
        for (int i = 0; i < len; i++) {
            if (k > 0 && nums[i] < 0) {
                nums[i] = -nums[i];
                k--;
            }
            min = Math.min(min, Math.abs(nums[i]));
            sum += nums[i];
        }

        // 所有数都已经 >= 0
        // 来回反转绝对值最小的数
        if (k > 0) {
            k = k % 2;
            sum -= k == 0 ? 0 : min * 2;
        }

        return sum;
    }
}

/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 376.摆动序列
 */
public class Solution376 {
    // 贪心算法
    public int wiggleMaxLength(int[] nums) {
        int len = nums.length;

        if (len == 0 || len == 1) {
            return len;
        }

        int ans = 1;

        // 前一对差值
        int pre = 0;

        for (int i = 1; i < len; i++) {
            int cur = nums[i] - nums[i - 1];
            if ((pre >= 0 && cur < 0) || (pre <= 0 && cur > 0)) {
                ans++;
                pre = cur;
            }
        }

        return ans;
    }

    // 动态规划
    public int wiggleMaxLength2(int[] nums) {
        int len = nums.length;

        // dp[i][0]: 上升（山峰）
        // dp[i][1]: 下降（山谷）
        int[][] dp = new int[len][2];

        dp[0][0] = 1;
        dp[0][1] = 1;

        for (int i = 1; i < len; i++) {
            dp[i][0] = dp[i][1] = 1;
            // 当前位置作山谷
            for (int j = 0; j < i; ++j) {
                if (nums[j] > nums[i]) {
                    dp[i][1] = Math.max(dp[i][1], dp[j][0] + 1);
                }
            }
            // 当前位置作山峰
            for (int j = 0; j < i; ++j) {
                if (nums[j] < nums[i]) {
                    dp[i][0] = Math.max(dp[i][0], dp[j][1] + 1);
                }
            }
        }

        return Math.max(dp[len - 1][0], dp[len - 1][1]);
    }
}

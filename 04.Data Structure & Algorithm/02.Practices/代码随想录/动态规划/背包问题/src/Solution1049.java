/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 1049.最后一块石头的重量 II
 */
public class Solution1049 {
    public int lastStoneWeightII(int[] stones) {
        if (stones.length == 1) {
            return stones[0];
        }

        int sum = 0;
        for (int x : stones) {
            sum += x;
        }
        int target = sum / 2;

        int[] dp = new int[target + 1];

        for (int i = 0; i < stones.length; i++) {
            for (int j = target; j >= stones[i]; j--) {
                dp[j] = Math.max(dp[j], dp[j - stones[i]] + stones[i]);
            }
        }

        int a = dp[target];
        int b = sum - a;

        return b - a;
    }
}

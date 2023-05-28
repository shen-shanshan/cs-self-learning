/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 746.使用最小花费爬楼梯
 */
public class Solution746 {
    public int minCostClimbingStairs(int[] cost) {
        int a = 0;
        int b = 0;
        int c = 0;

        for (int i = 2; i <= cost.length; i++) {
            c = Math.min(a+cost[i - 2], b+cost[i - 1]);
            a = b;
            b = c;
        }

        return c;
    }
}

/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 134.加油站
 * 总结：
 * 如果x到不了y+1（但能到y），那么从x到y的任一点出发都不可能到达y+1。
 * 因为从其中任一点出发的话，相当于从0开始加油，而如果从x出发到该点则不一定是从0开始加油，可能还有剩余的油。
 * 既然不从0开始都到不了y+1，那么从0开始就更不可能到达y+1了。
 */
public class Solution134 {
    public int canCompleteCircuit(int[] gas, int[] cost) {
        int len = gas.length;

        int gasSum = 0;
        int costSum = 0;

        for (int i = 0; i < len; i++) {
            gasSum += gas[i];
            costSum += cost[i];
        }

        // 肯定跑不完
        if (gasSum < costSum) {
            return -1;
        }

        // 起点
        int start = 0;
        int curSum = 0;

        for (int i = 0; i < len; i++) {
            curSum += gas[i] - cost[i];
            if (curSum < 0) {
                // 更新起点
                start = i + 1;
                curSum = 0;
            }
        }

        return start;
    }
}

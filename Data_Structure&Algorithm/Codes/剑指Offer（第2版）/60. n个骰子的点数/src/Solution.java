/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {

    double p = (double) 1 / 6;

    // 动态规划
    public double[] dicesProbability(int n) {
        // 初始状态：n = 1
        double[] last = new double[6];
        for (int i = 0; i < 6; i++) {
            last[i] = p;
        }
        int lastMin = 1;

        // 使用两个数组交替前进
        for (int i = 2; i <= n; i++) {
            // 当前可能出现的结果总数
            int len = i * 5 + 1;
            double[] cur = new double[len];
            int curMin = i;

            // 遍历上一个状态的数组，统计每一个元素对当前结果做出的贡献
            for (int j = 0; j < last.length; j++) {
                // 对上一个状态的每个元素分别计算 + 1~6 的情况
                for (int k = 1; k <= 6; k++) {
                    int newNum = lastMin + j + k;
                    int index = newNum - curMin;
                    cur[index] += last[j] * p;
                }
            }

            // 更新数组
            last = cur;
            lastMin = curMin;
        }
        return last;
    }

}

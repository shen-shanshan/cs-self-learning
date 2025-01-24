/*
 * 动态规划：
 * 创建二维数组 dp，包含 n 行 target+1 列。
 * 其中 dp[i][j] 表示从数组的 [0,i] 下标范围内选取若干个正整数（可以是 0 个），是否存在一种选取方案使得被选取
 * 的正整数的和等于 j。
 * 初始时，dp 中的全部元素都是 false。
 * */
public class Solution {
    public boolean canPartition(int[] nums) {
        int len = nums.length;
        // 只有一个数无法分割
        if (len < 2) {
            return false;
        }
        int sum = 0;
        int maxNum = 0;
        for (int i = 0; i < len; i++) {
            sum += nums[i];
            maxNum = Math.max(maxNum, nums[i]);
        }
        // 当 sum 为奇数时无法完成分割
        if (sum % 2 != 0) {
            return false;
        }
        int target = sum >> 1;
        // 当数组中的最大值大于总和的一半时无法完成分割
        if (maxNum > target) {
            return false;
        }

        // 动态规划
        boolean[] dp = new boolean[target + 1];
        // 初始化 dp
        dp[0] = true;
        dp[nums[0]] = true;
        // 从上到下，从右到左遍历
        for (int i = 1; i < len; i++) {
            for (int j = target; j >= 0; j--) {
                if (j >= nums[i]) {
                    dp[j] = dp[j - nums[i]] || dp[j];
                } else {
                    dp[j] = dp[j];
                }
            }
        }
        return dp[target];
    }
}

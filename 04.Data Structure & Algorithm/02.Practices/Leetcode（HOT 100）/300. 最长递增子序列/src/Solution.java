public class Solution {
    public int lengthOfLIS(int[] nums) {
        int len = nums.length;
        int[] dp = new int[len];
        int max = 1;
        // dp[i] 表示以 i 位置结尾的递增子序列的最大长度
        dp[0] = 1;
        for (int i = 1; i < len; i++) {
            int last = 0;
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    last = Math.max(last, dp[j]);
                }
            }
            dp[i] = last + 1;
            max = Math.max(max, dp[i]);
        }
        return max;
    }
}
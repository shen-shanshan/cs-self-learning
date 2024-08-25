package editor.cn;

// 416.分割等和子集
class Solution416 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public boolean canPartition(int[] nums) {
            // 确定背包大小
            int sum = 0;
            for (int i = 0; i < nums.length; i++) {
                sum += nums[i];
            }
            if (sum % 2 == 1) {
                return false;
            }
            int bagSize = sum / 2;

            int[] dp = new int[bagSize + 1];

            // 01 背包
            for (int i = 0; i < nums.length; i++) {
                for (int j = bagSize; j >= nums[i]; j--) {
                    dp[j] = Math.max(dp[j], dp[j - nums[i]] + nums[i]);
                }
            }

            return dp[bagSize] == bagSize;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
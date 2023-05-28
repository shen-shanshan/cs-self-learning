package editor.cn;

// 377.组合总和 Ⅳ
class Solution377 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int combinationSum4(int[] nums, int target) {
            int[] dp = new int[target + 1];
            dp[0] = 1;

            for (int i = 1; i <= target; i++) {
                for (int j = 0; j < nums.length; j++) {
                    if (i - nums[j] >= 0) {
                        dp[i] += dp[i - nums[j]];
                    }
                }
            }

            return dp[target];
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
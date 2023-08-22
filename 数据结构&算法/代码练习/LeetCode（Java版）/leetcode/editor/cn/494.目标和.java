package editor.cn;

import java.lang.annotation.Target;

// 494.目标和
class Solution494 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int findTargetSumWays(int[] nums, int target) {
            int sum = 0;
            for (int i = 0; i < nums.length; i++) {
                sum += nums[i];
            }

            if (sum < target || (sum + target) % 2 == 1) {
                return 0;
            }

            int bagSize = Math.abs((sum + target) / 2);

            int[] dp = new int[bagSize + 1];
            dp[0] = 1;
            for (int i = 0; i < nums.length; i++) {
                for (int j = bagSize; j >= nums[i]; j--) {
                    dp[j] += dp[j - nums[i]];
                }
            }

            return dp[bagSize];
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}
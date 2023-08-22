package editor.cn;

// 300.最长递增子序列
class Solution300 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int lengthOfLIS(int[] nums) {
            // 记录当前位置之前（包括当前位置）的最长递增子序列
            int[] dp = new int[nums.length];
            // 每个位置初始化为 1
            for (int i = 0; i < nums.length; i++) {
                dp[i] = 1;
            }

            int max = 1;

            for (int i = 1; i < nums.length; i++) {
                for (int j = 0; j < i; j++) {
                    if (nums[i] > nums[j]) {
                        dp[i] = Math.max(dp[i], dp[j] + 1);
                    }
                    max = Math.max(max, dp[i]);
                }
            }

            return max;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
package editor.cn;

// 1049.最后一块石头的重量 II
class Solution1049 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int lastStoneWeightII(int[] stones) {
            int len = stones.length;
            int sum = 0;

            for (int i = 0; i < len; i++) {
                sum += stones[i];
            }
            int target = sum / 2;

            // 背包
            int[] dp = new int[target + 1];
            for (int i = 0; i < len; i++) {
                for (int j = target; j >= 0; j--) {
                    if (j >= stones[i]) {
                        dp[j] = Math.max(dp[j], dp[j - stones[i]] + stones[i]);
                    }
                }
            }

            return (sum - dp[target]) - dp[target];
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
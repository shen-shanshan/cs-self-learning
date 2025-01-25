package editor.cn;

// 55.跳跃游戏
class Solution55 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public boolean canJump(int[] nums) {
            if (nums.length == 1) {
                return true;
            }

            // 当前最大能跳到的范围
            int max = 0;

            for (int i = 0; i < nums.length - 1; i++) {
                if (i <= max) {
                    max = Math.max(max, i + nums[i]);
                }
                if (max >= nums.length - 1) {
                    return true;
                }
            }

            return false;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
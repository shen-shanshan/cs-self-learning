package editor.cn;

// 198.打家劫舍
class Solution198 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int rob(int[] nums) {
            if (nums.length == 1) {
                return nums[0];
            } else if (nums.length == 2) {
                return Math.max(nums[0], nums[1]);
            }

            int a = nums[0];
            int b = Math.max(nums[0], nums[1]);
            int c = 0;

            for (int i = 2; i < nums.length; i++) {
                c = Math.max(a + nums[i], b);
                a = b;
                b = c;
            }

            return c;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
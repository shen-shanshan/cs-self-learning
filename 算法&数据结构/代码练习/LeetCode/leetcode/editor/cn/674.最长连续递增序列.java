package editor.cn;

// 674.最长连续递增序列
class Solution674 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int findLengthOfLCIS(int[] nums) {
            int max = 1;

            int len = 1;
            for (int i = 1; i < nums.length; i++) {
                if (nums[i] > nums[i - 1]) {
                    len++;
                } else {
                    max = Math.max(max, len);
                    len = 1;
                }
            }

            return Math.max(max, len);
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
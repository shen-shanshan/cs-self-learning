package editor.cn;

// 209.长度最小的子数组
class Solution209 {

    public static void main(String[] args) {
        Solution solution = new Solution209().new Solution();
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int minSubArrayLen(int target, int[] nums) {
            int i = 0;
            int j = 0;
            int len = Integer.MAX_VALUE;
            int cur = 0;
            while (j < nums.length) {
                cur += nums[j];
                while (cur >= target) {
                    len = Math.min(j - i + 1, len);
                    cur -= nums[i++];
                }
                j++;
            }
            return len == Integer.MAX_VALUE ? 0 : len;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
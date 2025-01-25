package editor.cn;

// 283.移动零
class Solution283 {

    public static void main(String[] args) {
        Solution solution = new Solution283().new Solution();
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public void moveZeroes(int[] nums) {
            int i = -1;
            int j = 0;
            while (j < nums.length) {
                while (nums[j] == 0) {
                    j++;
                    if (j == nums.length) {
                        return;
                    }
                }
                i++;
                int tmp = nums[i];
                nums[i] = nums[j];
                nums[j] = tmp;
                j++;
            }
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
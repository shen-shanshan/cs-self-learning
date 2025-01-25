package editor.cn;

// 27.移除元素
class Solution27 {

    public static void main(String[] args) {
        Solution solution = new Solution27().new Solution();
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int removeElement(int[] nums, int val) {
            if (nums.length == 0) {
                return 0;
            }
            int left = 0;
            int right = 0;
            while (right < nums.length) {
                if (nums[right] != val) {
                    int tmp = nums[left];
                    nums[left] = nums[right];
                    nums[right] = tmp;
                    left++;
                }
                right++;
            }
            return left;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
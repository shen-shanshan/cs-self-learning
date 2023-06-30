package editor.cn;

// 26.删除有序数组中的重复项
class Solution26 {

    public static void main(String[] args) {
        Solution solution = new Solution26().new Solution();
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int removeDuplicates(int[] nums) {
            int i = 0;
            int cur = nums[0];
            int j = 0;
            while (j < nums.length) {
                while (nums[j] == cur) {
                    j++;
                    if(j == nums.length){
                        return i + 1;
                    }
                }
                cur = nums[j];
                i++;
                int tmp = nums[i];
                nums[i] = nums[j];
                nums[j] = tmp;
                j++;
            }
            return i + 1;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
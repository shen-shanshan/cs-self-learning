package editor.cn;

// 213.打家劫舍 II
class Solution213 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int rob(int[] nums) {
            if(nums.length == 1){
                return nums[0];
            }else if(nums.length == 2){
                return Math.max(nums[0],nums[1]);
            }

            // 分别计算不包含首尾元素时进行 dp 的结果
            return Math.max(dp(nums, 0, nums.length - 2),
                            dp(nums, 1, nums.length - 1));
        }

        public int dp(int[] nums, int start, int end) {
            if (end - start == 0) {
                return nums[start];
            } else if (end - start == 1) {
                return Math.max(nums[start], nums[end]);
            }

            int a = nums[start];
            int b = Math.max(nums[start], nums[start + 1]);
            int c = 0;

            for (int i = start + 2; i <= end; i++) {
                c = Math.max(a + nums[i], b);
                a = b;
                b = c;
            }

            return c;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
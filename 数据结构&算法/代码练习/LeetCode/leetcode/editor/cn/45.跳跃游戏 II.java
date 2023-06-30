package editor.cn;

// 45.跳跃游戏 II
class Solution45 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int jump(int[] nums) {
            if (nums.length == 1) {
                return 0;
            }

            int maxIndex = 0;
            int curIndex = nums[0];

            int count = 0;

            int i = 0;
            while (i < nums.length) {
                if (curIndex < nums.length - 1) {
                    while (i <= curIndex) {
                        maxIndex = Math.max(maxIndex, i + nums[i]);
                        i++;
                    }
                    count++;
                    curIndex = maxIndex;
                } else {
                    count++;
                    break;
                }
            }

            return count;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
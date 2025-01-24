package editor.cn;

// 376.摆动序列
class Solution376 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int wiggleMaxLength(int[] nums) {
            if (nums.length == 1) {
                return 1;
            }

            int count = 1;

            int preDiff = 0;
            for (int i = 0; i < nums.length - 1; i++) {
                int curDiff = nums[i + 1] - nums[i];
                if ((curDiff > 0 && preDiff <= 0) || (curDiff < 0 && preDiff >= 0)) {
                    count++;
                    preDiff = curDiff;
                }
            }

            return count;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
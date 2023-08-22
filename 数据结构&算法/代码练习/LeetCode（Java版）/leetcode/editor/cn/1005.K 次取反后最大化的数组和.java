package editor.cn;

import java.util.Arrays;

// 1005.K 次取反后最大化的数组和
class Solution1005 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int largestSumAfterKNegations(int[] nums, int k) {
            Arrays.sort(nums);

            int sum = 0;

            int absMinNum = Integer.MAX_VALUE;

            for (int i = 0; i < nums.length; i++) {
                absMinNum = Math.min(absMinNum, Math.abs(nums[i]));
                if (k > 0 && nums[i] <= 0) {
                    nums[i] = -nums[i];
                    k--;
                }
                sum += nums[i];
            }

            if (k == 0) {
                return sum;
            }

            // 将所有负数变为正数后，若 k 仍大于 0，则将最小的正数来回翻转
            k = k % 2;
            while (k > 0) {
                absMinNum *= -1;
                k--;
                sum += absMinNum * 2;
            }

            return sum;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
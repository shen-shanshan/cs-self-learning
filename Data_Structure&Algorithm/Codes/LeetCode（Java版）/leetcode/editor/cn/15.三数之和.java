package editor.cn;

import java.util.*;

// 15.三数之和
class Solution15 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public List<List<Integer>> threeSum(int[] nums) {

            List<List<Integer>> ans = new LinkedList<>();

            Arrays.sort(nums);

            for (int i = 0; i < nums.length - 2; i++) {
                if (nums[i] > 0) {
                    return ans;
                }

                if (i > 0 && nums[i] == nums[i - 1]) {
                    continue;
                }

                int left = i + 1;
                int right = nums.length - 1;

                while (left < right) {
                    int cur = nums[i] + nums[left] + nums[right];
                    if (cur < 0) {
                        left++;
                    } else if (cur > 0) {
                        right--;
                    } else {
                        // cur == 0
                        ans.add(Arrays.asList(nums[i], nums[left], nums[right]));
                        // 保证不记录重复的情况
                        while (left < right && nums[right - 1] == nums[right]) {
                            right--;
                        }
                        while (left < right && nums[left + 1] == nums[left]) {
                            left++;
                        }
                        left++;
                        right--;
                    }
                }
            }

            return ans;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
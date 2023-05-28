package editor.cn;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

// 18.四数之和
class Solution18 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public List<List<Integer>> fourSum(int[] nums, int target) {

            List<List<Integer>> ans = new LinkedList<>();

            Arrays.sort(nums);

            for (int i = 0; i < nums.length - 3; i++) {

                if (i > 0 && nums[i] == nums[i - 1]) {
                    continue;
                }

                if ((long) nums[i] + nums[i + 1] + nums[i + 2] + nums[i + 3] > target) {
                    break;
                }

                if ((long) nums[i] + nums[nums.length - 3] + nums[nums.length - 2] + nums[nums.length - 1] < target) {
                    continue;
                }

                for (int j = i + 1; j < nums.length - 2; j++) {

                    if (j > i + 1 && nums[j] == nums[j - 1]) {
                        continue;
                    }

                    int left = j + 1;
                    int right = nums.length - 1;

                    while (left < right) {
                        long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];
                        if (sum < target) {
                            left++;
                        } else if (sum > target) {
                            right--;
                        } else {
                            ans.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));
                            while (left < right && nums[left + 1] == nums[left]) {
                                left++;
                            }
                            while (left < right && nums[right - 1] == nums[right]) {
                                right--;
                            }
                            left++;
                            right--;
                        }
                    }
                }
            }

            return ans;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
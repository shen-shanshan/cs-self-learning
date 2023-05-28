package editor.cn;

import java.util.Stack;

// 503.下一个更大元素 II
class Solution503 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int[] nextGreaterElements(int[] nums) {
            int len = nums.length;

            int[] ans = new int[len];
            for (int i = 0; i < len; i++) {
                ans[i] = -1;
            }

            // 存的是元素的下标
            Stack<Integer> stack = new Stack<>();

            for (int i = 0; i < len * 2; i++) {
                int index = i % len;
                while (!stack.isEmpty() && nums[index] > nums[stack.peek()]) {
                    int cur = stack.pop();
                    ans[cur] = nums[index];
                }
                stack.add(index);
            }

            return ans;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
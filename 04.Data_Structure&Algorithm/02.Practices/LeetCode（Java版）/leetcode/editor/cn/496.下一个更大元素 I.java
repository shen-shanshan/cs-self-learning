package editor.cn;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

// 496.下一个更大元素 I
class Solution496 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int[] nextGreaterElement(int[] nums1, int[] nums2) {
            Map<Integer, Integer> map = new HashMap<>();

            Stack<Integer> stack = new Stack<>();

            for (int i = 0; i < nums2.length; i++) {
                while (!stack.isEmpty() && nums2[i] > stack.peek()) {
                    int cur = stack.pop();
                    map.put(cur, nums2[i]);
                }
                stack.add(nums2[i]);
            }

            int[] ans = new int[nums1.length];

            for (int i = 0; i < nums1.length; i++) {
                ans[i] = map.getOrDefault(nums1[i], -1);
            }

            return ans;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
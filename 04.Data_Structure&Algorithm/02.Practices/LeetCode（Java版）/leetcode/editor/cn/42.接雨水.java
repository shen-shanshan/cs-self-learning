package editor.cn;

import java.util.Stack;

// 42.接雨水
class Solution42 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int trap(int[] height) {
            int ans = 0;

            // 存的是数组元素的下标
            Stack<Integer> stack = new Stack<>();

            for (int i = 0; i < height.length; i++) {
                if (stack.isEmpty() || height[i] <= height[stack.peek()]) {
                    stack.push(i);
                } else {
                    while (!stack.isEmpty() && height[i] > height[stack.peek()]) {
                        int mid = height[stack.pop()];
                        if (!stack.isEmpty()) {
                            int hig = Math.min(height[stack.peek()], height[i]) - mid;
                            int len = i - stack.peek() - 1;
                            ans += hig * len;
                        }
                    }
                    stack.push(i);
                }
            }

            return ans;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
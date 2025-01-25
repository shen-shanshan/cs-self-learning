package editor.cn;

import java.util.Stack;

// 84.柱状图中最大的矩形
class Solution84 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int largestRectangleArea(int[] heights) {
            int[] newHeights = new int[heights.length + 2];

            for (int i = 1; i < newHeights.length - 1; i++) {
                newHeights[i] = heights[i - 1];
            }

            int max = 0;

            // 存的是数组元素的下标
            Stack<Integer> stack = new Stack<>();

            stack.push(0);

            for (int i = 1; i < newHeights.length; i++) {
                while (newHeights[i] < newHeights[stack.peek()]) {
                    int cur = stack.pop();
                    int hig = newHeights[cur];
                    int len = i - stack.peek() - 1;
                    max = Math.max(max, hig * len);
                }
                stack.push(i);
            }

            return max;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
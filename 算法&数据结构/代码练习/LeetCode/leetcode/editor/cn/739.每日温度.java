package editor.cn;

import java.util.Stack;

// 739.每日温度
class Solution739 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int[] dailyTemperatures(int[] temperatures) {
            // 单调栈：栈底最大，栈顶最小（存放的是数组元素的下标）
            Stack<Integer> stack = new Stack<>();

            int len = temperatures.length;

            int[] ans = new int[len];

            for (int i = 0; i < len; i++) {
                // 弹栈，直到栈中元素对应的温度都大于等于当前温度
                while (!stack.isEmpty() && temperatures[stack.peek()] < temperatures[i]) {
                    int cur = stack.pop();
                    ans[cur] = i - cur;
                }
                // 将当前元素入栈
                stack.add(i);
            }

            return ans;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
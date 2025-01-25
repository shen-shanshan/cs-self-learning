package editor.cn;

import java.util.Stack;

// 20.有效的括号
class Solution20 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public boolean isValid(String s) {
            char[] chars = s.toCharArray();
            int len = chars.length;

            Stack<Character> stack = new Stack<>();

            for (int i = 0; i < len; i++) {
                char cur = chars[i];
                if (cur == '(' || cur == '[' || cur == '{') {
                    stack.push(cur);
                } else if (cur == ')' && !stack.isEmpty() && stack.peek() == '(') {
                    stack.pop();
                } else if (cur == ']' && !stack.isEmpty() && stack.peek() == '[') {
                    stack.pop();
                } else if (cur == '}' && !stack.isEmpty() && stack.peek() == '{') {
                    stack.pop();
                } else {
                    return false;
                }
            }

            return stack.isEmpty();
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
package editor.cn;

import java.util.Stack;

// 1047.删除字符串中的所有相邻重复项
class Solution1047 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public String removeDuplicates(String s) {
            Stack<Character> stack = new Stack<>();

            for (int i = 0; i < s.length(); i++) {
                char cur = s.charAt(i);
                if (stack.isEmpty()) {
                    stack.push(cur);
                } else if (stack.peek() == cur) {
                    stack.pop();
                } else {
                    stack.push(cur);
                }
            }

            StringBuilder sb = new StringBuilder();
            int size = stack.size();
            for (int i = 0; i < size; i++) {
                sb.append(stack.pop());
            }

            return sb.reverse().toString();
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}
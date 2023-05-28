import java.util.Stack;

public class Solution {
    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '{') {
                stack.push('}');
            } else if (s.charAt(i) == '[') {
                stack.push(']');
            } else if (s.charAt(i) == '(') {
                stack.push(')');
            } else if (s.charAt(i) == ')') {
                if (stack.isEmpty() || stack.pop() != ')') {
                    return false;
                }
            } else if (s.charAt(i) == ']') {
                if (stack.isEmpty() || stack.pop() != ']') {
                    return false;
                }
            } else {
                if (stack.isEmpty() || stack.pop() != '}') {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    public static void main(String[] args) {
        String s1 = "()[]{}";
        String s2 = "([)]";
        String s3 = "{[]}";

        Solution s = new Solution();
        boolean ans1 = s.isValid(s1);
        System.out.println(ans1);
        boolean ans2 = s.isValid(s2);
        System.out.println(ans2);
        boolean ans3 = s.isValid(s3);
        System.out.println(ans3);
    }
}

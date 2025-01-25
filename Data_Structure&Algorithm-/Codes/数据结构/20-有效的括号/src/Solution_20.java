import java.util.Stack;

public class Solution_20 {

    /*public boolean isValid(String s) {
        Stack<Character> st = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(' || s.charAt(i) == '[' || s.charAt(i) == '{') {
                st.push(s.charAt(i));
            }
            if (st.isEmpty()) {
                st.push(s.charAt(i));
                continue;
            }
            if (s.charAt(i) == ')') {
                if (st.peek() == '(') {
                    st.pop();
                } else {
                    st.push(s.charAt(i));
                }
            }
            if (s.charAt(i) == ']') {
                if (st.peek() == '[') {
                    st.pop();
                } else {
                    st.push(s.charAt(i));
                }
            }
            if (s.charAt(i) == '}') {
                if (st.peek() == '{') {
                    st.pop();
                } else {
                    st.push(s.charAt(i));
                }
            }
        }
        return st.isEmpty() ? true : false;
    }*/

    public boolean isValid(String s) {
        Stack<Character> st = new Stack<Character>(); // 创建一个元素为 char 类型的堆栈，先进后出，后进先出。
        for (char c : s.toCharArray()) {
            if (c == '(') {
                st.push(')');
            } else if (c == '[') {
                st.push(']');
            } else if (c == '{') {
                st.push('}');
            } else if (st.isEmpty() || c != st.pop()) { // 1.如果压入')'、']'、'}'时栈为空，即如果此时前面没有'('、'['、'{'，则 报错。
                return false;                           // 2.如果压入')'时，栈顶元素不为'('，则报错。（']'和'}'同理）
            }
        }
        return st.isEmpty();//isEmpty()为空返回 true，否则返回 false。
    }

    public static void main(String[] args) {
        String s1 = "()[]{}";
        String s2 = "([)]";
        String s3 = "{[]}";

        Solution_20 s = new Solution_20();
        boolean res = s.isValid(s3);
        System.out.println(res);
    }
}

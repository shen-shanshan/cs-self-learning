import java.util.Stack;

// 用栈解决
public class Solution2 {
    public String decodeString(String s) {
        Stack<Character> letter = new Stack<>();
        Stack<Integer> number = new Stack<>();

        char[] chars = s.toCharArray();
        int len = chars.length;

        int i = 0;
        while (i < len) {
            char cur = chars[i];
            if (cur - 'a' >= 0 && cur - 'a' < 26) {
                letter.push(cur);
                i++;
            } else if (cur - '0' >= 0 && cur - '0' <= 9) {
                int num = cur - '0';
                while (true) {
                    cur = chars[++i];
                    if (cur == '[') {
                        break;
                    } else {
                        num = num * 10 + (cur - '0');
                    }
                }
                letter.push('[');
                number.push(num);
                i++;
            } else if (cur == ']') {
                // 开始弹栈，读取当前括号内的字符串
                StringBuilder sb = new StringBuilder();
                while (true) {
                    char c = letter.pop();
                    if (c == '[') {
                        break;
                    } else {
                        sb.append(c);
                    }
                }
                // 将字符串逆序
                String str = sb.reverse().toString();
                // 生成解码后的字符串
                int num = number.pop();
                StringBuilder sb2 = new StringBuilder();
                for (int j = 0; j < num; j++) {
                    sb2.append(str);
                }
                // 将新字符串再压回栈
                String str2 = sb2.toString();
                int strLen = sb2.length();
                for (int j = 0; j < strLen; j++) {
                    letter.push(str2.charAt(j));
                }
                i++;
            }
        }
        StringBuilder sb = new StringBuilder();
        while (!letter.isEmpty()) {
            sb.append(letter.pop());
        }
        return sb.reverse().toString();
    }
}
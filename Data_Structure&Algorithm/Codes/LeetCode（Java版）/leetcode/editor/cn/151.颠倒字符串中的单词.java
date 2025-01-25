package editor.cn;

import java.util.LinkedList;
import java.util.List;

// 151.颠倒字符串中的单词
class Solution151 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        /**
         * 不使用Java内置方法实现
         * <p>
         * 1.去除首尾以及中间多余空格
         * 2.反转整个字符串
         * 3.反转各个单词
         */
        public String reverseWords(String s) {

            // 1.去除首尾以及中间多余空格
            StringBuilder sb = removeSpace(s);

            // 2.反转整个字符串
            reverseString(sb, 0, sb.length() - 1);

            // 3.反转各个单词
            reverseEachWord(sb);

            return sb.toString();
        }

        private StringBuilder removeSpace(String s) {
            int start = 0;
            int end = s.length() - 1;
            // 去除首尾的空格
            while (s.charAt(start) == ' ') start++;
            while (s.charAt(end) == ' ') end--;
            // 去除中间冗余的空格
            StringBuilder sb = new StringBuilder();
            while (start <= end) {
                char c = s.charAt(start);
                if (c != ' ' || sb.charAt(sb.length() - 1) != ' ') {
                    sb.append(c);
                }
                start++;
            }
            return sb;
        }

        /**
         * 反转字符串指定区间[start, end]的字符
         */
        public void reverseString(StringBuilder sb, int start, int end) {
            while (start < end) {
                char temp = sb.charAt(start);
                sb.setCharAt(start, sb.charAt(end));
                sb.setCharAt(end, temp);
                start++;
                end--;
            }
        }

        private void reverseEachWord(StringBuilder sb) {
            int start = 0;
            int end = 1;
            int n = sb.length();
            while (start < n) {
                while (end < n && sb.charAt(end) != ' ') {
                    end++;
                }
                reverseString(sb, start, end - 1);
                start = end + 1;
                end = start + 1;
            }
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
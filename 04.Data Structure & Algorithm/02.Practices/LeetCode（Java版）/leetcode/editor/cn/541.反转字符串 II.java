package editor.cn;

// 541.反转字符串 II
class Solution541 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public String reverseStr(String s, int k) {

            char[] chars = s.toCharArray();

            int i = 0;
            while (i + k * 2 - 1 < s.length()) {
                myReverse(chars, i, i + k - 1);
                i += k * 2;
            }

            if (i + k - 1 < s.length()) {
                // k <= 剩余字符数 < 2k
                myReverse(chars, i, i + k - 1);
            } else {
                // 剩余字符数 < k，反转剩余所有字符
                myReverse(chars, i, s.length() - 1);
            }

            return String.valueOf(chars);
        }

        public void myReverse(char[] c, int start, int end) {
            while (start < end) {
                char tmp = c[start];
                c[start] = c[end];
                c[end] = tmp;
                start++;
                end--;
            }
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
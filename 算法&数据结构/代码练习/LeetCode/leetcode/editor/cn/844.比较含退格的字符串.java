package editor.cn;

// 844.比较含退格的字符串
class Solution844 {

    public static void main(String[] args) {
        Solution solution = new Solution844().new Solution();
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public boolean backspaceCompare(String s, String t) {
            return parseString(s).equals(parseString(t));
        }

        public String parseString(String s) {
            char[] chars = s.toCharArray();
            int i = -1;
            int j = 0;
            // ab## c#d#
            while (j < chars.length) {
                if (chars[j] != '#') {
                    i++;
                    char tmp = chars[i];
                    chars[i] = chars[j];
                    chars[j] = tmp;
                } else {
                    i = Math.max(i - 1, -1);
                }
                j++;
            }
            if (i == -1) {
                return "";
            }
            return String.valueOf(chars).substring(0, i + 1);
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
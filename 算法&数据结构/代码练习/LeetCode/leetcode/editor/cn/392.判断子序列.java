package editor.cn;

// 392.判断子序列
class Solution392 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public boolean isSubsequence(String s, String t) {
            if (s.length() > t.length()) {
                return false;
            }

            int i = 0;
            int j = 0;

            while (i < s.length() && j < t.length()) {
                if (s.charAt(i) == t.charAt(j)) {
                    i++;
                }
                j++;
            }

            return i == s.length();
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
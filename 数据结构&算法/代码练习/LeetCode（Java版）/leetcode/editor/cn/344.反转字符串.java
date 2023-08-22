package editor.cn;

// 344.反转字符串
class Solution344 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public void reverseString(char[] s) {
            int i = 0;
            int j = s.length - 1;
            while (i < j) {
                char tmp = s[i];
                s[i] = s[j];
                s[j] = tmp;
                i++;
                j--;
            }
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
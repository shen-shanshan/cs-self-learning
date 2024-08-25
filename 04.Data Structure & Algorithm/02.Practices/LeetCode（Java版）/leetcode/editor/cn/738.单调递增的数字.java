package editor.cn;

// 738.单调递增的数字
class Solution738 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int monotoneIncreasingDigits(int n) {
            String s = String.valueOf(n);
            char[] chars = s.toCharArray();

            // 标记要赋值为 9 的范围的右边界
            // 注意：1000，若不用 last 进行标记，则结果为 900，而实际结果应为 999
            int last = chars.length - 1;
            // 从后向前遍历
            int i = chars.length - 1;
            while (i > 0) {
                if (chars[i - 1] > chars[i]) {
                    chars[i - 1]--;
                    for (int j = i; j <= last; j++) {
                        chars[j] = '9';
                    }
                    last = i - 1;
                }
                i--;
            }

            return Integer.parseInt(String.valueOf(chars));
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
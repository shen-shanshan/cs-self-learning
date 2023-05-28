package editor.cn;

import java.util.Arrays;

// 455.分发饼干
class Solution455 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int findContentChildren(int[] g, int[] s) {

            Arrays.sort(g);
            Arrays.sort(s);

            int count = 0;

            int i = g.length - 1;
            int j = s.length - 1;
            while (i >= 0 && j >= 0) {
                while (i >= 0 && g[i] > s[j]) {
                    i--;
                }
                if (i >= 0) {
                    count++;
                    i--;
                    j--;
                }
            }

            return count;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
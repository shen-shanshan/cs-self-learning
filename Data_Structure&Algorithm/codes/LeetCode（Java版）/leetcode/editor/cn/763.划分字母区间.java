package editor.cn;

import java.util.LinkedList;
import java.util.List;

// 763.划分字母区间
class Solution763 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public List<Integer> partitionLabels(String s) {
            List<Integer> ans = new LinkedList<>();

            // 统计每个字母最后一次出现的位置
            int[] right = new int[26];
            for (int i = 0; i < s.length(); i++) {
                right[s.charAt(i) - 'a'] = i;
            }

            int start = 0;
            int maxRight = 0;
            for (int i = 0; i < s.length(); i++) {
                maxRight = Math.max(maxRight, right[s.charAt(i) - 'a']);
                if (maxRight == i) {
                    ans.add(i - start + 1);
                    start = i + 1;
                }
            }

            return ans;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}
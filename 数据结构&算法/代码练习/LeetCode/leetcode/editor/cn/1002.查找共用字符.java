package editor.cn;

import java.util.LinkedList;
import java.util.List;

// 1002.查找共用字符
class Solution1002 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public List<String> commonChars(String[] words) {

            List<String> ans = new LinkedList<>();

            // 统计所有字符串里 26 个字符的出现频率，然后取每个字符频率最小值
            int[] count = new int[26];
            for (int i = 0; i < words[0].length(); i++) {
                count[words[0].charAt(i) - 'a']++;
            }

            if (words.length == 1) {
                for (int i = 0; i < 26; i++) {
                    while (count[i] > 0) {
                        ans.add(String.valueOf((char) ('a' + i)));
                        count[i]--;
                    }
                }
                return ans;
            }

            // 统计其它字符串中的字母出现频率
            for (int i = 1; i < words.length; i++) {
                char[] cur = words[i].toCharArray();
                int[] other = new int[26];
                for (int j = 0; j < cur.length; j++) {
                    other[cur[j] - 'a']++;
                }
                // 取最小值
                for (int j = 0; j < 26; j++) {
                    count[j] = Math.min(count[j], other[j]);
                }
            }

            // 记录答案
            for (int i = 0; i < 26; i++) {
                while (count[i] > 0) {
                    ans.add(String.valueOf((char) ('a' + i)));
                    count[i]--;
                }
            }

            return ans;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
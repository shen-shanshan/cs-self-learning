package editor.cn;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

// 438.找到字符串中所有字母异位词
class Solution438 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public List<Integer> findAnagrams(String s, String p) {

            List<Integer> ans = new LinkedList<>();

            if (s.length() < p.length()) {
                return ans;
            }

            // 记录不同的字母
            Map<Character, Integer> map = new HashMap<>();

            int len = p.length();
            for (int i = 0; i < len; i++) {
                char cur = p.charAt(i);
                map.put(cur, map.getOrDefault(cur, 0) + 1);
            }

            int left = 0;
            int right = len - 1;
            for (int i = 0; i < len; i++) {
                int count = map.getOrDefault(s.charAt(i), 0);
                if (count == 1) {
                    map.remove(s.charAt(i));
                } else {
                    map.put(s.charAt(i), count - 1);
                }
            }
            if (map.size() == 0) {
                ans.add(left);
            }

            while (right < s.length() - 1) {
                // 移除左边的元素
                int count = map.getOrDefault(s.charAt(left), 0);
                if (count == -1) {
                    map.remove(s.charAt(left));
                } else {
                    map.put(s.charAt(left), count + 1);
                }
                left++;
                // 加入右边的元素
                right++;
                count = map.getOrDefault(s.charAt(right), 0);
                if (count == 1) {
                    map.remove(s.charAt(right));
                } else {
                    map.put(s.charAt(right), count - 1);
                }
                if (map.size() == 0) {
                    ans.add(left);
                }
            }

            return ans;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
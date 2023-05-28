package editor.cn;

import java.util.HashMap;
import java.util.Map;

// 242.有效的字母异位词
class Solution242 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public boolean isAnagram(String s, String t) {

            // 记录不同字母出现的次数
            Map<Character, Integer> map = new HashMap<>();

            // 统计第一个字符串
            for (int i = 0; i < s.length(); i++) {
                map.put(s.charAt(i),
                        map.getOrDefault(s.charAt(i), 0) + 1);
            }

            // 统计第二个字符串
            for (int i = 0; i < t.length(); i++) {
                if (map.containsKey(t.charAt(i))) {
                    int count = map.get(t.charAt(i));
                    if (count == 1) {
                        map.remove(t.charAt(i));
                    } else {
                        map.put(t.charAt(i), count - 1);
                    }
                } else {
                    return false;
                }
            }

            return map.size() == 0;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
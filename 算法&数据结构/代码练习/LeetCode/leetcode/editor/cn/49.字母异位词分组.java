package editor.cn;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

// 49.字母异位词分组
class Solution49 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public List<List<String>> groupAnagrams(String[] strs) {

            List<List<String>> ans = new LinkedList<>();

            // 记录已经加入到结果中的字符串
            Map<Integer, Integer> map = new HashMap<>();

            for (int i = 0; i < strs.length; i++) {
                if (!map.containsKey(i)) {
                    List<String> cur = new LinkedList<>();
                    cur.add(strs[i]);
                    map.put(i, 1);
                    for (int j = 0; j < strs.length; j++) {
                        if (i != j && myEqual(strs[i], strs[j])) {
                            cur.add(strs[j]);
                            map.put(j, 1);
                        }
                    }
                    ans.add(cur);
                }
            }

            return ans;
        }

        public boolean myEqual(String s1, String s2) {

            if (s1.length() != s2.length()) {
                return false;
            }

            int[] count = new int[26];

            for (int i = 0; i < s1.length(); i++) {
                int index = s1.charAt(i) - 'a';
                count[index]++;
            }

            for (int i = 0; i < s2.length(); i++) {
                int index = s2.charAt(i) - 'a';
                count[index]--;
            }

            for (int i = 0; i < 26; i++) {
                if (count[i] != 0) {
                    return false;
                }
            }

            return true;
        }

    }
    //leetcode submit region end(Prohibit modification and deletion)

}
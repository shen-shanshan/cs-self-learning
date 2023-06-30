import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {

    public int lengthOfLongestSubstring(String s) {
        int len = s.length();
        if (len == 0 || len == 1) {
            return len;
        }
        // len > 1
        char[] chars = s.toCharArray();
        int start = 0;
        int end = 1;
        int max = 1;
        Map<Character, Integer> map = new HashMap<>();
        map.put(chars[0], 0);
        while (end < len) {
            if (!map.containsKey(chars[end])) {
                max = Math.max(max, end - start + 1);
            } else {
                int lastEnd = map.get(chars[end]);
                // 将 start 到 lastEnd 之间的元素从 map 中移除
                for (int i = start; i <= lastEnd; i++) {
                    map.remove(chars[i]);
                }
                start = lastEnd + 1;
            }
            map.put(chars[end], end);
            end++;
        }
        return max;
    }

}

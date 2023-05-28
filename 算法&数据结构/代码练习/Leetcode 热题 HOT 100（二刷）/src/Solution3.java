import java.util.HashMap;
import java.util.Map;

/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 3.无重复字符的最长子串
 */
public class Solution3 {
    public int lengthOfLongestSubstring(String s) {
        if (s.length() == 0 || s.length() == 1) {
            return s.length();
        }

        // 存每一个字符上一次出现的位置
        HashMap<Character, Integer> map = new HashMap<>();

        int start = 0;

        map.put(s.charAt(0), 0);

        int max = 1;

        for (int i = 1; i < s.length(); i++) {
            start = Math.max(start, map.getOrDefault(s.charAt(i), -1) + 1);
            max = Math.max(max, i - start + 1);
            map.put(s.charAt(i), i);
        }

        return max;
    }
}

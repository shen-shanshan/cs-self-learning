import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 超时
public class Solution {
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> ans = new ArrayList<>();
        int lenS = s.length();
        int lenP = p.length();
        if (lenS < lenP) {
            return ans;
        }
        // 统计字符串 p 的字母
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < lenP; i++) {
            map.put(p.charAt(i), map.getOrDefault(p.charAt(i), 0) + 1);
        }
        // 滑动窗口
        int p1 = 0;
        int p2 = lenP - 1;
        while (p2 < lenS) {
            if (isValid(s, p1, p2, lenP, map)) {
                ans.add(p1);
            }
            if (p2 - p1 + 1 < lenP) {
                p2++;
            } else {
                p1++;
            }
        }
        return ans;
    }

    public boolean isValid(String s, int start, int end, int lenP, Map<Character, Integer> map) {
        if (end - start + 1 < lenP) {
            return false;
        }
        // 统计当前子串的字母
        HashMap<Character, Integer> str = new HashMap<>();
        for (int i = start; i <= end; i++) {
            str.put(s.charAt(i), str.getOrDefault(s.charAt(i), 0) + 1);
        }
        for (Map.Entry<Character, Integer> entry : str.entrySet()) {
            char c = entry.getKey();
            int v = entry.getValue();
            if (map.getOrDefault(c, -1) != v) {
                return false;
            }
        }
        return true;
    }
}
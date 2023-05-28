import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Solution_567 {
    /*public boolean checkInclusion(String s1, String s2) {
        if (s1.length() > s2.length()) return false;
        // 记录 s1 中出现的字符及其出现次数
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < s1.length(); i++) {
            if (map.containsKey(s1.charAt(i))) {
                int count = map.get(s1.charAt(i));
                map.put(s1.charAt(i), count + 1);
            } else {
                map.put(s1.charAt(i), 1);
            }
        }
        // 遍历 s2
        char[] c = s2.toCharArray();
        int len = s2.length();
        int i = 0;
        int j = i + s1.length() - 1;

        *//*while (j < len) {
            int tmp = i;
            Map<Character, Integer> mapTmp = map;
            while (tmp <= j && mapTmp.containsKey(c[tmp]) && mapTmp.get(c[tmp]) > 0) {
                int count = mapTmp.get(c[tmp]);
                mapTmp.put(c[tmp], count - 1);
                tmp++;
            }
            if (tmp == j + 1) return true;
             i++;
            j = i + s1.length() - 1;
        }*//*

        Map<Character, Integer> map2 = new HashMap<>();
        for (int k = i; k < j; k++) {
            if (map2.containsKey(c[k])) {
                int count = map2.get(c[k]);
                map2.put(c[k], count + 1);
            } else {
                map2.put(c[k], 1);
            }
        }
        // 使用滑动窗口遍历
        while (j < len) {
            if (map2.containsKey(c[j])) {
                int count = map2.get(c[j]);
                map2.put(c[j], count + 1);
            } else {
                map2.put(c[j], 1);
            }
            if (mapEqual(map, map2)) return true;
            int count = map2.get(c[i]);
            map2.put(c[i], count - 1);
            i++;
            j++;
        }
        return false;
    }*/

    // 判断两个 map 包含的键值对是否相等
    // 泛型方法：public <泛型类型> 返回类型 方法名（泛型类型）。
    /*public static <K, V> boolean mapEqual(Map<K, V> m1, Map<K, V> m2) {
        // m1, m2 的 size 相同
        // 获取 m1 的所有键值对对象
        Set<Map.Entry<K, V>> set = m1.entrySet();
        for (Map.Entry<K, V> x : set) {
            K key = x.getKey();
            V value = x.getValue();
            if (!m2.containsKey(key)) {
                return false;
            } else if (m2.get(key) != value) {
                return false;
            }
        }
        return true;
    }*/

    public boolean checkInclusion(String s1, String s2) {
        int n = s1.length(), m = s2.length();
        if (n > m) {
            return false;
        }
        // 记录 s1 中各字符出现的次数，以负值记录
        int[] cnt = new int[26];
        for (int i = 0; i < n; ++i) {
            --cnt[s1.charAt(i) - 'a'];
        }
        int left = 0;
        for (int right = 0; right < m; ++right) {
            int x = s2.charAt(right) - 'a';
            ++cnt[x];
            // 若当前字符在当前框中出现的次数超出了在 s1 中出现的次数，或该字符在 s1 中不存在
            while (cnt[x] > 0) {
                --cnt[s2.charAt(left) - 'a'];
                // 框的左边界往右移一位
                ++left;
            }
            if (right - left + 1 == n) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        String s1 = "ab";
        String s2 = "eidbaooo";
        String s3 = "eidboaoo";

        String s4 = "adc";
        String s5 = "dcda";

        Solution_567 s = new Solution_567();
        boolean res1 = s.checkInclusion(s1, s2);
        boolean res2 = s.checkInclusion(s1, s3);
        boolean res3 = s.checkInclusion(s4, s5);

        System.out.println(res1);// true
        System.out.println(res2);// false
        System.out.println(res3);// true
    }
}

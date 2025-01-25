import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 205.同构字符串
 */
public class Solution205 {
    public boolean isIsomorphic(String s, String t) {
        // 记录映射关系
        Map<Character, Character> map1 = new HashMap<>();
        // 记录 t 中已被映射的字符
        Set<Character> set = new HashSet<>();

        for (int i = 0, j = 0; i < s.length(); i++, j++) {
            // map1 保存 s[i] 到 t[j] 的映射
            if (!map1.containsKey(s.charAt(i))) {
                if (!set.contains(t.charAt(j))) {
                    map1.put(s.charAt(i), t.charAt(j));
                    set.add(t.charAt(j));
                } else {
                    return false;
                }
            }
            // 无法映射，返回 false
            if (map1.get(s.charAt(i)) != t.charAt(j)) {
                return false;
            }
        }

        return true;
    }
}

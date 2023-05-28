import java.util.HashMap;
import java.util.Map;

/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {

    public char firstUniqChar(String s) {
        if (s.length() == 0) {
            return ' ';
        }
        char[] chars = s.toCharArray();
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            map.put(chars[i], map.getOrDefault(chars[i], 0) + 1);
        }
        for (int i = 0; i < s.length(); i++) {
            if (map.get(chars[i]) == 1) {
                return chars[i];
            }
        }
        return ' ';
    }

}

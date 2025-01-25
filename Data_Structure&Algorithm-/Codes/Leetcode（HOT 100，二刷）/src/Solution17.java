import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 17.电话号码的字母组合
 */
public class Solution17 {

    Map<Character, String> map;

    public List<String> letterCombinations(String digits) {
        if (digits.length() == 0) {
            return new ArrayList<>();
        }

        init();

        List<String> ans = new ArrayList<>();
        StringBuilder path = new StringBuilder();

        backtrack(digits, 0, ans, path);

        return ans;
    }

    public void init() {
        map = new HashMap<>();
        map.put('2', "abc");
        map.put('3', "def");
        map.put('4', "ghi");
        map.put('5', "jkl");
        map.put('6', "mno");
        map.put('7', "pqrs");
        map.put('8', "tuv");
        map.put('9', "wxyz");
    }

    public void backtrack(String str, int i, List<String> ans, StringBuilder path) {
        if (i == str.length()) {
            ans.add(path.toString());
            return;
        }

        char c = str.charAt(i);
        char[] list = map.get(c).toCharArray();

        for (int j = 0; j < list.length; j++) {
            path.append(list[j]);
            // 递归
            backtrack(str, i + 1, ans, path);
            // 回溯
            path.deleteCharAt(path.length() - 1);
        }
    }
}

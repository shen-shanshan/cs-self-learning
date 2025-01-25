import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {
    public List<String> letterCombinations(String digits) {
        List<String> ans = new ArrayList<>();
        if (digits.length() == 0) {
            return ans;
        }
        // 建立映射
        Map<Character, String> map = new HashMap<Character, String>() {{
            put('2', "abc");
            put('3', "def");
            put('4', "ghi");
            put('5', "jkl");
            put('6', "mno");
            put('7', "pqrs");
            put('8', "tuv");
            put('9', "wxyz");
        }};
        // 递归 + 回溯
        backtrack(ans, map, digits, 0, new StringBuffer());
        return ans;
    }

    public void backtrack(List<String> ans, Map<Character, String> map
            , String digits, int index, StringBuffer combination) {
        if (index == digits.length()) {
            // 遍历结束，保存结果
            ans.add(combination.toString());
        } else {
            // 取此时 index 对应的数字
            char digit = digits.charAt(index);
            String letters = map.get(digit);
            int lettersCount = letters.length();
            // 遍历该数字所有可能的取值
            for (int i = 0; i < lettersCount; i++) {
                combination.append(letters.charAt(i));
                // 递归
                backtrack(ans, map, digits, index + 1, combination);
                // 回溯
                combination.deleteCharAt(index);
            }
        }
    }

    public static void main(String[] args) {
        String s1 = "23";

        Solution s = new Solution();
        List<String> ans = s.letterCombinations(s1);
        for (String x : ans) {
            System.out.print(x + " ");
        }
    }
}

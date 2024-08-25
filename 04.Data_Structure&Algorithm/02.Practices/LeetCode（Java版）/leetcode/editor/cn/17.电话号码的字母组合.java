package editor.cn;

import java.util.*;

// 17.电话号码的字母组合
class Solution17 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        List<String> ans = new LinkedList<>();

        StringBuilder cur = new StringBuilder();

        Map<Character, String> map = new HashMap<>();

        public List<String> letterCombinations(String digits) {
            if(digits.length() == 0){
                return ans;
            }
            init();
            backtrack(digits, 0);
            return ans;
        }

        public void init() {
            map.put('2', "abc");
            map.put('3', "def");
            map.put('4', "ghi");
            map.put('5', "jkl");
            map.put('6', "mno");
            map.put('7', "pqrs");
            map.put('8', "tuv");
            map.put('9', "wxyz");
        }

        public void backtrack(String str, int index) {
            // 终止条件
            if (index == str.length()) {
                ans.add(cur.toString());
                return;
            }

            // 遍历
            String s = map.get(str.charAt(index));
            for (int i = 0; i < s.length(); i++) {
                cur.append(s.charAt(i));
                // 递归
                backtrack(str, index + 1);
                // 回溯
                cur.deleteCharAt(cur.length() - 1);
            }
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
package editor.cn;

import java.util.LinkedList;
import java.util.List;

// 131.分割回文串
class Solution131 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        List<List<String>> ans = new LinkedList<>();

        List<String> path = new LinkedList<>();

        public List<List<String>> partition(String s) {
            backtrack(s, 0);
            return ans;
        }

        public void backtrack(String s, int index) {
            // 终止条件
            if (index == s.length()) {
                ans.add(new LinkedList<>(path));
                return;
            }

            // 遍历
            for (int i = index; i < s.length(); i++) {
                // 当前截取的字符串为：[index, i]
                // 判断是否回文，若不是则剪枝
                if (!isPanlindrome(s, index, i)) {
                    continue;
                }
                path.add(s.substring(index, i + 1));
                // 递归
                backtrack(s, i + 1);
                // 回溯
                path.remove(path.size() - 1);
            }
        }

        public boolean isPanlindrome(String s, int start, int end) {
            while (start < end) {
                if (s.charAt(start) != s.charAt(end)) {
                    return false;
                }
                start++;
                end--;
            }
            return true;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
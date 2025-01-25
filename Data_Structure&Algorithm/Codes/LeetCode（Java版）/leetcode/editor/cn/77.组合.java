package editor.cn;

import java.util.LinkedList;
import java.util.List;

// 77.组合
class Solution77 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        List<List<Integer>> ans = new LinkedList<>();

        List<Integer> cur = new LinkedList<>();

        public List<List<Integer>> combine(int n, int k) {
            if (n < k) {
                return ans;
            }
            backtrack(n, k, 1);
            return ans;
        }

        public void backtrack(int n, int k, int index) {
            // 终止条件
            if (cur.size() == k) {
                ans.add(new LinkedList<>(cur));
                return;
            }

            // 遍历
            for (int i = index; i <= n - (k - cur.size()) + 1; i++) {
                cur.add(i);
                // 递归
                backtrack(n, k, i + 1);
                // 回溯
                cur.remove(cur.size() - 1);
            }
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
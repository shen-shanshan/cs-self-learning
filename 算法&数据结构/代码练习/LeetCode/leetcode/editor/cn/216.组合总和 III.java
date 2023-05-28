package editor.cn;

import java.util.LinkedList;
import java.util.List;

// 216.组合总和 III
class Solution216 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        List<List<Integer>> ans = new LinkedList<>();

        List<Integer> cur = new LinkedList<>();

        int sum = 0;

        public List<List<Integer>> combinationSum3(int k, int n) {
            if (n > 45) {
                return ans;
            }
            backtrack(k, n, 1);
            return ans;
        }

        public void backtrack(int k, int n, int index) {
            // 终止条件
            if (cur.size() == k && sum == n) {
                ans.add(new LinkedList<>(cur));
                return;
            }

            // 剪枝
            // 只有当 size < k 且 sum < n 使才继续递归
            if (cur.size() >= k || sum >= n) {
                return;
            }

            // 遍历
            for (int i = index; i <= 9 - (k - cur.size()) + 1; i++) {
                cur.add(i);
                sum += i;
                // 递归
                backtrack(k, n, i + 1);
                // 回溯
                cur.remove(cur.size() - 1);
                sum -= i;
            }
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
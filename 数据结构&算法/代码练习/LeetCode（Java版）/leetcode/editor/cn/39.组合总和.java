package editor.cn;

import java.util.LinkedList;
import java.util.List;

// 39.组合总和
class Solution39 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        List<List<Integer>> ans = new LinkedList<>();

        List<Integer> cur = new LinkedList<>();

        public List<List<Integer>> combinationSum(int[] candidates, int target) {
            backtrack(candidates, target, 0, 0);
            return ans;
        }

        public void backtrack(int[] arr, int target, int index, int sum) {
            // 终止条件
            if (sum == target) {
                ans.add(new LinkedList<>(cur));
                return;
            }

            // 剪枝
            if (sum > target) {
                return;
            }

            // 遍历
            for (int i = index; i < arr.length; i++) {
                cur.add(arr[i]);
                // 原地递归
                backtrack(arr, target, i, sum + arr[i]);
                // 回溯
                cur.remove(cur.size() - 1);
            }
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
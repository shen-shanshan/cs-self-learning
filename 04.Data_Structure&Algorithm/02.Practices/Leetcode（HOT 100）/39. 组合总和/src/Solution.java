import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Solution {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> ans = new LinkedList<>();
        if (candidates.length == 0) return ans;
        backtrack(candidates, target, 0, new LinkedList<Integer>(), ans);
        return ans;
    }

    public void backtrack(int[] arr, int target, int index, List<Integer> cur, List<List<Integer>> ans) {
        // 递归结束条件
        if (target == 0) {
            ans.add(new ArrayList<>(cur));
            return;
        }
        if (index == arr.length) {
            return;
        }
        // 1.继续加当前数
        if (target - arr[index] >= 0) {
            cur.add(arr[index]);
            backtrack(arr, target - arr[index], index, cur, ans);
            // 回溯
            cur.remove(cur.size() - 1);
        }
        // 2.跳过当前数
        backtrack(arr, target, index + 1, cur, ans);
    }
}
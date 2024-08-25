import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 39.数组总和
 */
public class Solution39 {

    List<List<Integer>> ans = new ArrayList<>();

    List<Integer> path = new ArrayList<>();

    int curSum = 0;

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        backtrack(candidates, target, 0);
        return ans;
    }

    public void backtrack(int[] arr, int target, int start) {
        if (curSum == target) {
            ans.add(new ArrayList<>(path));
            return;
        }

        // 剪枝
        if (curSum > target) {
            return;
        }

        for (int i = start; i < arr.length; i++) {
            path.add(arr[i]);
            curSum += arr[i];
            // 递归
            backtrack(arr, target, i);
            // 回溯
            path.remove(path.size() - 1);
            curSum -= arr[i];
        }
    }
}

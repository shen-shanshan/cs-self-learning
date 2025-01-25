import java.util.ArrayList;
import java.util.List;

public class Solution {
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        if (nums.length == 0) return ans;
        backtrack(nums, 0, new ArrayList<>(), ans);
        return ans;
    }

    public void backtrack(int[] nums, int index, List<Integer> cur, List<List<Integer>> ans) {
        // basecase
        if (index == nums.length) {
            ans.add(new ArrayList<>(cur));
            return;
        }

        for (int i = index; i < nums.length; i++) {
            cur.add(nums[i]);
            swap(nums, i, index);
            backtrack(nums, index + 1, cur, ans);
            cur.remove(cur.size() - 1);
            swap(nums, i, index);
        }
    }

    public void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}

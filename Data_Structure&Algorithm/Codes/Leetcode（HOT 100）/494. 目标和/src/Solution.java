import java.util.ArrayList;

// 递归回溯
public class Solution {
    int count = 0;

    public int findTargetSumWays(int[] nums, int target) {
        ArrayList<Integer> ans = new ArrayList<>();
        backtrack(nums, target, 0);
        return count;
    }

    public void backtrack(int[] nums, int target, int index) {
        // base case
        if (index == nums.length) {
            if (target == 0) {
                count++;
            }
            return;
        }
        // +nums[index]
        backtrack(nums, target - nums[index], index + 1);
        // -nums[index]
        backtrack(nums, target + nums[index], index + 1);
    }
}
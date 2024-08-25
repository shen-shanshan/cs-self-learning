import java.util.ArrayList;

public class Solution {
    public boolean canJump(int[] nums) {
        if (nums.length == 0) return false;
        ArrayList<Integer> a = new ArrayList<>();
        backtrack(nums, 0, a);
        boolean ans = false;
        if (a.size() != 0) {
            ans = true;
        }
        return ans;
    }

    public void backtrack(int[] nums, int index, ArrayList<Integer> a) {
        // basecase
        if (index >= nums.length - 1) {
            if (a.size() == 0) {
                a.add(1);
            }
            return;
        }
        if (nums[index] == 0) {
            return;
        }
        for (int i = 1; i <= nums[index]; i++) {
            backtrack(nums, index + i, a);
        }
    }
}
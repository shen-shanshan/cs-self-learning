public class Solution {
    public int rob(int[] nums) {
        if (nums.length == 0) {
            return nums[0];
        }
        int a = nums[0];
        int b = Math.max(nums[0], nums[1]);
        int cur = b;
        for (int i = 2; i < nums.length; i++) {
            cur = Math.max(a + nums[i], b);
            a = b;
            b = cur;
        }
        return cur;
    }
}

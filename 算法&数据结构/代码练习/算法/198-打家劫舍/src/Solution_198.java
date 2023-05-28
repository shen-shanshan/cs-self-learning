public class Solution_198 {
    public int rob(int[] nums) {
        int n = nums.length;
        if (n == 1) return nums[0];
        if (n == 2) return Math.max(nums[0], nums[1]);
        int[] dp = new int[n];
        // dp[0] = nums[0];
        // dp[1] = Math.max(nums[0], nums[1]);
        int a = nums[0];
        int b = Math.max(nums[0], nums[1]);
        int c = 0;
        for (int i = 2; i < n; i++) {
            // dp[i] = Math.max(dp[i - 1], nums[i] + dp[i - 2]);
            c = Math.max(b, nums[i] + a);
            a = b;
            b = c;
        }
        // return dp[n - 1];
        return c;
    }

    public static void main(String[] args) {
        int[] nums1 = {1, 2, 3, 1};
        int[] nums2 = {2, 7, 9, 3, 1};
        int[] nums3 = {2, 1};

        Solution_198 s = new Solution_198();
        int res1 = s.rob(nums1);
        int res2 = s.rob(nums2);
        int res3 = s.rob(nums3);
        System.out.println(res1);// 4
        System.out.println(res2);// 12
        System.out.println(res3);// 2
    }
}

public class Solution_53 {

    public int maxSubArray(int[] nums) {
        int sum = 0;
        int max = nums[0];
        for (int num : nums) {
            if (sum > 0) {
                sum += num;
            } else {
                sum = num;
            }
            max = Math.max(max, sum);
        }
        return max;
    }

    public static void main(String[] args) {
        int[] nums1 = {-2, 1, -3, 4, -1, 2, 1, -5, 4}; // 6
        int[] nums2 = {1}; // 1
        int[] nums3 = {-2, 1, 2, 4}; // 7
        int[] nums4 = {1, -2, 2, 4}; // 6
        int[] nums5 = {1, -3}; // 1

        Solution_53 s = new Solution_53();
        int max = s.maxSubArray(nums1);
        System.out.println(max);
    }
}

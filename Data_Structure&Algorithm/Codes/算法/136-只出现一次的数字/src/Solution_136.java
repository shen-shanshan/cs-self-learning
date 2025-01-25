public class Solution_136 {
    public int singleNumber(int[] nums) {
        int n = nums.length;
        int res = 0;
        for(int i = 0; i < n;i++){
            res ^= nums[i];
        }
        return res;
    }

    public static void main(String[] args) {
        int[] nums1 = {2, 2, 1};
        int[] nums2 = {4, 1, 2, 1, 2};

        Solution_136 s = new Solution_136();
        int res1 = s.singleNumber(nums1);
        int res2 = s.singleNumber(nums2);
        System.out.println(res1);// 1
        System.out.println(res2);// 4
    }
}

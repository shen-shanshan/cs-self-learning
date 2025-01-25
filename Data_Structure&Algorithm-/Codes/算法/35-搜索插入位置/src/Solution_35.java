public class Solution_35 {
    public int searchInsert(int[] nums, int target) {
        if (nums == null) return -1;
        int low = 0;
        int high = nums.length - 1;
        int mid;
        while (low <= high) {
            mid = low + (high - low) / 2;
            if (nums[mid] < target) {
                low = mid + 1;
            } else if (nums[mid] > target) {
                high = mid - 1;
            } else {
                // 找到了 target
                return mid;
            }
        }
        // low > high 时跳出循环，没找到目标值
        return high + 1;
    }

    public static void main(String[] args) {
        int[] nums1 = {1, 3, 5, 6};
        int[] nums2 = {1};
        int target1 = 5;
        int target2 = 2;
        int target3 = 7;
        int target4 = 0;

        Solution_35 s = new Solution_35();
        int res1 = s.searchInsert(nums1, target1);
        int res2 = s.searchInsert(nums1, target2);
        int res3 = s.searchInsert(nums1, target3);
        int res4 = s.searchInsert(nums1, target4);
        int res5 = s.searchInsert(nums2, target4);
        System.out.println(res1);// 2
        System.out.println(res2);// 1
        System.out.println(res3);// 4
        System.out.println(res4);// 0
        System.out.println(res5);// 0
    }
}

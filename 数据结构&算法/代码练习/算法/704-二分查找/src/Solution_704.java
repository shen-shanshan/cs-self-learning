public class Solution_704 {
    public int search(int[] nums, int target) {
        if (nums.length == 0) return -1;
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
                return mid;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] nums = {-1, 0, 3, 5, 9, 12};
        int target1 = 9;
        int target2 = 2;
        int[] nums2 = {5};
        int target3 = 5;

        Solution_704 s = new Solution_704();
        int res1 = s.search(nums, target1);
        int res2 = s.search(nums, target2);
        System.out.println(res1);// 4
        System.out.println(res2);// -1
        System.out.println(s.search(nums2, target3));// 0
    }
}

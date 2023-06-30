public class Solution {
    public int findMin(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[0] <= nums[mid]) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        if (left > nums.length - 1) {
            return nums[0];
        }
        return nums[left];
    }
}

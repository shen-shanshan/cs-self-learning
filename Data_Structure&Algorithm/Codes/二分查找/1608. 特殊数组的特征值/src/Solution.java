public class Solution {
    public int specialArray(int[] nums) {
        int len = nums.length;
        // x = 0
        if (len == 0) {
            return 0;
        }
        // 0 < x <= len
        int left = 1;
        int right = len;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (count(nums, mid) > mid) {
                left = mid + 1;
            } else if (count(nums, mid) < mid) {
                right = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    // 统计数组中 >= x 的元素个数
    public int count(int[] arr, int x) {
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] >= x) {
                count++;
            }
        }
        return count;
    }
}

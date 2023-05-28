public class Solution {
    public void sortColors(int[] nums) {
        int len = nums.length;
        int less = -1;
        int more = len;
        int cur = 0;
        while (cur < more) {
            if (nums[cur] < 1) {
                swap(nums, ++less, cur++);
            } else if (nums[cur] > 1) {
                swap(nums, cur, --more);
            } else {
                cur++;
            }
        }
    }

    public void swap(int[] arr, int a, int b) {
        int tmp = arr[a];
        arr[a] = arr[b];
        arr[b] = tmp;
    }
}
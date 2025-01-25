public class Solution {
    public int findUnsortedSubarray(int[] nums) {
        int n = nums.length;
        int maxn = Integer.MIN_VALUE, right = -1;
        int minn = Integer.MAX_VALUE, left = -1;
        for (int i = 0; i < n; i++) {
            // 从左往右，找到比左边最大值还小的最右下标
            if (maxn > nums[i]) {
                right = i;
            } else {
                maxn = nums[i];
            }
            // 从右往左，找到比右边最小值还大的最左下标
            if (minn < nums[n - i - 1]) {
                left = n - i - 1;
            } else {
                minn = nums[n - i - 1];
            }
        }
        // 若原数组本来就有序，则返回 0
        return right == -1 ? 0 : right - left + 1;
    }
}
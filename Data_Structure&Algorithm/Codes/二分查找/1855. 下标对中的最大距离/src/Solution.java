public class Solution {
    public int maxDistance(int[] nums1, int[] nums2) {
        int len1 = nums1.length;
        int len2 = nums2.length;
        int res = 0;
        // 对于 nums2 中的每一个元素，在 nums1 中找下标最小的且满足 nums1[i] <= nums2[j] 的元素
        for (int j = 0; j < len2; j++) {
            int i = search(nums1, nums2[j]);
            if (i >= 0 && i < len1) {
                res = Math.max(res, j - i);
            }
        }
        return res;
    }

    public int search(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (arr[mid] > target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }
}

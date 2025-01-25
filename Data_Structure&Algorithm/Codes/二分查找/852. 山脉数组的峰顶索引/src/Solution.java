public class Solution {
    public int peakIndexInMountainArray(int[] arr) {
        int left = 0;
        int right = arr.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (mid - 1 >= 0 && arr[mid - 1] > arr[mid]) {
                // mid 在右边的坡上
                right = mid - 1;
            } else if (mid + 1 < arr.length && arr[mid] < arr[mid + 1]) {
                // mid 在左边的坡上
                left = mid + 1;
            } else if (arr[mid - 1] < arr[mid] && arr[mid] > arr[mid + 1]) {
                return mid;
            }
        }
        return left;
    }
}

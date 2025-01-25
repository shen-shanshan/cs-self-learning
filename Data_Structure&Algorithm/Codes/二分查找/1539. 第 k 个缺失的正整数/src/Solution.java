public class Solution {
    public int findKthPositive(int[] arr, int k) {
        int len = arr.length;
        // 特殊情况 1
        if (arr[0] > k) {
            return k;
        }
        // 特殊情况 2
        int sumLose = arr[len - 1] - len;
        if (sumLose < k) {
            return arr[len - 1] + k - sumLose;
        }
        // 一般情况
        int left = 0;
        int right = len - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            int lose = arr[mid] - mid - 1;
            if (lose < k) {
                left = mid + 1;
            } else if (lose > k) {
                right = mid - 1;
            } else {
                return arr[mid] - 1;
            }
        }
        // left > right 且 lose >= k
        int lose = arr[left] - left - 1;
        return arr[left] - (lose - k + 1);
    }
}

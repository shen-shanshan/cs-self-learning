import java.util.Arrays;

public class Solution {
    public boolean checkIfExist(int[] arr) {
        Arrays.sort(arr);
        for (int i = 0; i < arr.length; i++) {
            boolean flag = search(arr, arr[i] * 2, 0, i - 1)
                    || search(arr, arr[i] * 2, i + 1, arr.length - 1);
            if (flag) {
                return true;
            }
        }
        return false;
    }

    public boolean search(int[] arr, int target, int start, int end) {
        if (end < 0 || start >= arr.length) {
            return false;
        }
        int left = start;
        int right = end;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (arr[mid] < target) {
                left = mid + 1;
            } else if (arr[mid] > target) {
                right = mid - 1;
            } else {
                return true;
            }
        }
        return false;
    }
}

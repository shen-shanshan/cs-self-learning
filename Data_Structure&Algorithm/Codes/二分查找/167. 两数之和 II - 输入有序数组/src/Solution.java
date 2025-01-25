public class Solution {
    public int[] twoSum(int[] numbers, int target) {
        int index1 = 0;
        int index2 = 0;
        while (index1 < numbers.length) {
            // 找 target - numbers[index1]
            int cur = search(numbers, target - numbers[index1], index1 + 1);
            if (cur != -1) {
                index2 = cur;
                break;
            }
            index1++;
        }
        return new int[]{index1 + 1, index2 + 1};
    }

    public int search(int[] arr, int target, int start) {
        int left = start;
        int right = arr.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (arr[mid] < target) {
                left = mid + 1;
            } else if (arr[mid] > target) {
                right = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
    }
}

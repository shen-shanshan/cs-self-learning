public class Solution {
    /*
    将数组一分为二，其中一定有一个是有序的，另一个可能是有序，也能是部分有序。
    此时有序部分用二分法查找。
    无序部分再一分为二，其中一个一定有序，另一个可能有序，可能无序。
    就这样循环。
    */
    public int search(int[] nums, int target) {
        return search(nums, 0, nums.length - 1, target);
    }

    public int search(int[] nums, int start, int end, int target) {
        if(start >= nums.length || end < 0){
            return -1;
        }
        int mid = start + ((end - start) >> 1);
        if (nums[start] < nums[mid]) {
            // 前半部分有序
            int ret = bSearch(nums, start, mid, target);
            if (ret == -1) {
                // 递归
                return search(nums, mid + 1, end, target);
            } else {
                return ret;
            }
        } else {
            // 后半部分有序
            int ret = bSearch(nums, mid, end, target);
            if (ret == -1) {
                // 递归
                return search(nums, start, mid - 1, target);
            } else {
                return ret;
            }
        }
    }

    public int bSearch(int[] arr, int start, int end, int target) {
        int left = start;
        int right = end;
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

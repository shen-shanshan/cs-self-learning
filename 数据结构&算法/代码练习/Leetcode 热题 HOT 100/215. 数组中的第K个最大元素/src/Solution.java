// 快速排序
public class Solution {
    public int findKthLargest(int[] nums, int k) {
        // 从小到大排序
        quickSort(nums, 0, nums.length - 1);
        // 返回倒数第 k 个数
        return nums[nums.length - k];
    }

    public void quickSort(int[] nums, int left, int right) {
        if (nums.length == 0 || nums.length == 1 || left >= right) {
            return;
        }
        // 引入随机化加速递归过程
        swap(nums, left + (int) (Math.random() * (right - left + 1)), right);
        int[] equal = partition(nums, left, right);
        // equal[0]：等于区的左边界
        // equal[1]：等于区的右边界
        quickSort(nums, left, equal[0] - 1);
        quickSort(nums, equal[1] + 1, right);
    }

    // 分层
    public int[] partition(int[] arr, int left, int right) {
        int less = left - 1;
        int more = right;
        int i = left;
        while (i < more) {
            if (arr[i] < arr[right]) {
                swap(arr, ++less, i++);
            } else if (arr[i] > arr[right]) {
                swap(arr, i, --more);
            } else {
                i++;
            }
        }
        swap(arr, i, right);
        return new int[]{less + 1, i};
    }

    public void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }
}
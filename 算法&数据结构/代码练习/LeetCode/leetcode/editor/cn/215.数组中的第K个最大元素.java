package editor.cn;

// 215.数组中的第K个最大元素
class Solution215 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int findKthLargest(int[] nums, int k) {
            return quickSort(nums, 0, nums.length - 1, nums.length - k);
        }

        public int quickSort(int[] arr, int start, int end, int target) {
            // 随机化
            swap(arr, start + (int) (Math.random() * (end - start + 1)), end);

            // 分层
            int pos = partition(arr, start, end);

            // 递归
            if (pos < target) {
                // 递归右子区间
                return quickSort(arr, pos + 1, end, target);
            } else if (pos > target) {
                // 递归左子区间
                return quickSort(arr, start, pos - 1, target);
            }

            // pos == target
            return arr[pos];
        }

        public int partition(int[] arr, int start, int end) {
            int ref = arr[end];

            int less = start - 1;
            int more = end;
            int i = start;

            while (i < more) {
                if (arr[i] <= ref) {
                    swap(arr, ++less, i++);
                } else {
                    swap(arr, --more, i);
                }
            }

            swap(arr, i, end);

            return i;
        }

        public void swap(int[] arr, int i, int j) {
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
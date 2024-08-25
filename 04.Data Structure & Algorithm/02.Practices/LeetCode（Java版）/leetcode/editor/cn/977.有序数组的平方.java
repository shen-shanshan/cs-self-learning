package editor.cn;

// 977.有序数组的平方
class Solution977 {

    public static void main(String[] args) {
        Solution solution = new Solution977().new Solution();
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int[] sortedSquares(int[] nums) {
            int[] ans = new int[nums.length];
            // 先找到绝对值最小的位置
            int mid = 0;
            while (mid + 1 < nums.length && Math.abs(nums[mid]) >= Math.abs(nums[mid + 1])) {
                mid++;
            }
            // 从中间向两边遍历
            int i = mid - 1;
            int j = mid + 1;
            int cur = 0;
            ans[cur++] = nums[mid] * nums[mid];
            while (i >= 0 && j < nums.length) {
                if (Math.abs(nums[i]) <= Math.abs(nums[j])) {
                    ans[cur++] = nums[i] * nums[i];
                    i--;
                } else {
                    ans[cur++] = nums[j] * nums[j];
                    j++;
                }
            }
            while (i >= 0) {
                ans[cur++] = nums[i] * nums[i];
                i--;
            }
            while (j < nums.length) {
                ans[cur++] = nums[j] * nums[j];
                j++;
            }
            return ans;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
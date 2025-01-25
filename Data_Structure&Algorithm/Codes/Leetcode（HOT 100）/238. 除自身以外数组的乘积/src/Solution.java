public class Solution {
    public int[] productExceptSelf(int[] nums) {
        int len = nums.length;
        // left[i] 代表的是 i 左侧所有数字的乘积
        int[] left = new int[len];
        // right[i] 代表的是 i 右侧所有数字的乘积
        int[] right = new int[len];

        // 初始化辅助数组
        left[0] = 1;
        for (int i = 1; i < len; i++) {
            left[i] = left[i - 1] * nums[i - 1];
        }
        right[len - 1] = 1;
        for (int i = len - 2; i >= 0; i--) {
            right[i] = right[i + 1] * nums[i + 1];
        }

        // 计算答案
        int[] ans = new int[len];
        for (int i = 0; i < len; i++) {
            if (i == 0) {
                ans[i] = right[i];
            } else if (i == len - 1) {
                ans[i] = left[i];
            } else {
                ans[i] = left[i] * right[i];
            }
        }
        return ans;
    }
}
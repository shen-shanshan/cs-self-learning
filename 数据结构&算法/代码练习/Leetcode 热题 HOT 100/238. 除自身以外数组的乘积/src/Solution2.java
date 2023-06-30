// 优化：不使用额外空间
public class Solution2 {
    public int[] productExceptSelf(int[] nums) {
        int len = nums.length;
        int[] ans = new int[len];
        // 先将 ans 当作 left 使用
        ans[0] = 1;
        for (int i = 1; i < len; i++) {
            ans[i] = ans[i - 1] * nums[i - 1];
        }
        // 遍历原数组，获得每一个位置的 right，并动态更新 right 和 ans
        int right = 1;
        for (int i = len - 1; i >= 0; i--) {
            ans[i] = right * ans[i];
            right = right * nums[i];
        }
        return ans;
    }
}
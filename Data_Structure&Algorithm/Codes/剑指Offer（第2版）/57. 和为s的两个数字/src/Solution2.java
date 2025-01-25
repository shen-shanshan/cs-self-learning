import java.util.HashMap;
import java.util.Map;

/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution2 {

    // 双指针
    public int[] twoSum(int[] nums, int target) {
        int len = nums.length;
        int[] ans = new int[]{};
        if (len < 2) {
            return ans;
        }

        int left = 0;
        int right = len - 1;
        while (left < right) {
            int sum = nums[left] + nums[right];
            if (sum > target) {
                right--;
            } else if (sum < target) {
                left++;
            } else {
                return new int[]{nums[left], nums[right]};
            }
        }

        return ans;
    }

}

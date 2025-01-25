/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 724.寻找数组的中心下标
 */
public class Solution724 {
    public int pivotIndex(int[] nums) {
        int len = nums.length;

        if (len == 1) {
            return 0;
        }

        int left = 0;
        int right = 0;

        for (int i = 1; i < len; i++) {
            right += nums[i];
        }

        // 枚举中心点
        for (int i = 0; i < len; i++) {
            if (left == right) {
                return i;
            } else if (i < len - 1) {
                left += nums[i];
                right -= nums[i + 1];
            }
        }

        return -1;
    }
}

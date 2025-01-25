/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 1365.有多少小于当前数字的数字
 */
public class Solution1365 {
    public int[] smallerNumbersThanCurrent(int[] nums) {
        int len = nums.length;

        // 统计每个数字出现的次数
        int[] count = new int[101];
        for (int i = 0; i < len; i++) {
            count[nums[i]]++;
        }

        int curSum = 0;
        for (int i = 0; i < 101; i++) {
            if (count[i] > 0) {
                int tmp = count[i];
                count[i] = curSum;
                curSum += tmp;
            }
        }

        int[] ans = new int[len];
        for (int i = 0; i < len; i++) {
            ans[i] = count[nums[i]];
        }

        return ans;
    }
}

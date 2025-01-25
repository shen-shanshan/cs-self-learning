/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {
    public int[] exchange(int[] nums) {
        int even = 0;
        int cur = 0;
        while (cur < nums.length) {
            if (nums[cur] % 2 == 1) {
                // 当前数是奇数
                swap(nums, cur, even);
                even++;
                cur++;
            } else {
                // 当前数是偶数
                cur++;
            }
        }
        return nums;
    }

    public void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}

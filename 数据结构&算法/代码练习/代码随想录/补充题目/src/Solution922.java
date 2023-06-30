/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 922.按奇偶排序数组 II
 */
public class Solution922 {
    public int[] sortArrayByParityII(int[] nums) {
        int len = nums.length;

        // 记录已经确定了的奇数位
        int odd = -1;

        // 只要偶数位全为偶数了，那么奇数位就全是奇数了，因此可以只考虑其中一种
        for (int i = 0; i < len; i += 2) {
            if (nums[i] % 2 == 1) {
                // 找奇数位的偶数替换
                odd += 2;
                while (nums[odd] % 2 == 1) {
                    odd += 2;
                }
                swap(nums, i, odd);
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

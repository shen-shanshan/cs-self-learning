/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 75.颜色分类
 */
public class Solution75 {
    public void sortColors(int[] nums) {
        int len = nums.length;

        if (len == 1) {
            return;
        }

        // 1.排红色
        int i = 0;
        int j = 0;
        while (j < len) {
            if (nums[j] == 0) {
                swap(nums, i, j);
                i++;
            }
            j++;
        }

        // 2.排白色
        j = i;
        while (j < len) {
            if (nums[j] == 1) {
                swap(nums, i, j);
                i++;
            }
            j++;
        }
    }

    public void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}

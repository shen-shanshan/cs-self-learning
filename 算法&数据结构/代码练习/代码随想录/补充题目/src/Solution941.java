/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 941.有效的山脉数组
 */
public class Solution941 {
    public boolean validMountainArray(int[] arr) {
        if (arr.length < 3) {
            return false;
        }

        // 双指针
        int left = 0;
        int right = arr.length - 1;

        // 注意防止指针越界
        while (left + 1 < arr.length && arr[left] < arr[left + 1]) {
            left++;
        }
        while (right > 0 && arr[right] < arr[right - 1]) {
            right--;
        }

        // 如果left或者right都在起始位置，说明不是山峰
        if (left == right && left != 0 && right != arr.length - 1) {
            return true;
        }

        return false;
    }
}

/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {
    public boolean verifyPostorder(int[] postorder) {
        if (postorder.length == 0) {
            return true;
        }
        return isValid(postorder, 0, postorder.length - 1);
    }

    public boolean isValid(int[] arr, int left, int right) {
        if (right - left == 0) {
            return true;
        }
        int root = arr[right];
        int leftStart = left;
        int leftEnd = left;
        int rightStart = right;
        int rightEnd = right;
        int index = left;
        // 判断是否能分为小于区和大于区
        while (index < right) {
            if (arr[index] < root) {
                index++;
            } else {
                break;
            }
        }
        // 若没有左子树
        if (index == left) {
            while (index < right) {
                if (arr[index] > root) {
                    index++;
                } else {
                    break;
                }
            }
            if (index < right) {
                return false;
            }
            rightStart = left;
            rightEnd = index - 1;
            return isValid(arr, rightStart, rightEnd);
        }
        // 若没有右子树
        leftEnd = index - 1;
        if (index == right) {
            return isValid(arr, leftStart, leftEnd);
        }
        // 既有左子树，也有右子树
        rightStart = index;
        while (index < right) {
            if (arr[index] > root) {
                index++;
            } else {
                break;
            }
        }
        rightEnd = index - 1;
        if (index < right) {
            return false;
        }
        return isValid(arr, leftStart, leftEnd) && isValid(arr, rightStart, rightEnd);
    }
}

/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 240.搜索二维矩阵 II
 */
public class Solution240 {
    public boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length;
        int n = matrix[0].length;

        // 从右上角开始遍历
        int i = 0;
        int j = n - 1;

        while (i < m && j >= 0) {
            if (matrix[i][j] == target) {
                return true;
            } else if (matrix[i][j] > target) {
                j--;
            } else {
                i++;
            }
        }

        return false;
    }
}

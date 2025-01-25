public class Solution {
    public boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length;
        int n = matrix[0].length;
        int i = 0;
        int j = n - 1;
        // 从右上角开始搜索
        while (i < m && j >= 0) {
            if (matrix[i][j] == target) {
                return true;
            } else if (matrix[i][j] > target) {
                // 此时应往左走
                j--;
            } else {
                // matrix[i][j] < target
                // 此时应往下走
                i++;
            }
        }
        return false;
    }
}
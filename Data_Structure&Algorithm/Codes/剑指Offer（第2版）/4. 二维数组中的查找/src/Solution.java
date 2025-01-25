public class Solution {
    public boolean findNumberIn2DArray(int[][] matrix, int target) {
        if(matrix.length == 0){
            return false;
        }
        int m = matrix.length;
        int n = matrix[0].length;
        int i = 0;
        int j = n - 1;
        while (i < m && j >= 0) {
            if (matrix[i][j] < target) {
                // 往下走
                i++;
            } else if (matrix[i][j] > target) {
                // 往左走
                j--;
            } else {
                return true;
            }
        }
        return false;
    }
}

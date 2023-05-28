public class Solution {
    public boolean searchMatrix(int[][] matrix, int target) {
        // 对每一行的第一个元素进行二分查找
        int row = rowSearch(matrix, target);
        if (row < 0) {
            return false;
        }
        // 对目标行进行二分查找
        return columnSearch(matrix, row, target);
    }

    public int rowSearch(int[][] matrix, int target) {
        int m = matrix.length;
        int n = matrix[0].length;
        // 二分查找
        int left = 0;
        int right = m - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (matrix[mid][0] <= target) {
                left = mid + 1;
            } else if (matrix[mid][0] > target) {
                right = mid - 1;
            }
        }
        return right;
    }

    public boolean columnSearch(int[][] matrix, int row, int target) {
        int m = matrix.length;
        int n = matrix[0].length;
        // 二分查找
        int left = 0;
        int right = n - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (matrix[row][mid] < target) {
                left = mid + 1;
            } else if (matrix[row][mid] > target) {
                right = mid - 1;
            } else {
                return true;
            }
        }
        return false;
    }
}

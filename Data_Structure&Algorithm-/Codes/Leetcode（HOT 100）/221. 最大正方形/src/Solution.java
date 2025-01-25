// 暴力解法
public class Solution {
    public int maximalSquare(char[][] matrix) {
        int max = 0;
        int m = matrix.length;
        int n = matrix[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == '1') {
                    int len = 1;
                    boolean ans = search(matrix, i, j, len);
                    while (ans) {
                        max = Math.max(max, len * len);
                        ans = search(matrix, i, j, ++len);
                    }
                }
            }
        }
        return max;
    }

    // 向下向右搜索正方形
    public boolean search(char[][] matrix, int i, int j, int len) {
        for (int k = i; k < i + len; k++) {
            if (k >= matrix.length) {
                return false;
            }
            for (int l = j; l < j + len; l++) {
                if (l >= matrix[0].length || matrix[k][l] == '0') {
                    return false;
                }
            }
        }
        return true;
    }
}
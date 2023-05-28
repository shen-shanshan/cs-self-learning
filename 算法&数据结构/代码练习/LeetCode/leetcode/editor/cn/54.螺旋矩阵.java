package editor.cn;

import java.util.LinkedList;
import java.util.List;

// 54.螺旋矩阵
class Solution54 {

    public static void main(String[] args) {
        Solution solution = new Solution54().new Solution();
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public List<Integer> spiralOrder(int[][] matrix) {
            List<Integer> ans = new LinkedList<>();
            int m = matrix.length;
            int n = matrix[0].length;
            int count = 0;
            int i = 0;
            int j = 0;
            while (count < m * n) {
                // 向右
                while (j < n && matrix[i][j] != 200) {
                    ans.add(matrix[i][j]);
                    matrix[i][j] = 200;
                    j++;
                    count++;
                }
                j--;
                i++;
                // 向下
                while (i < m && matrix[i][j] != 200) {
                    ans.add(matrix[i][j]);
                    matrix[i][j] = 200;
                    i++;
                    count++;
                }
                i--;
                j--;
                // 向左
                while (j >= 0 && matrix[i][j] != 200) {
                    ans.add(matrix[i][j]);
                    matrix[i][j] = 200;
                    j--;
                    count++;
                }
                j++;
                i--;
                // 向上
                while (i >= 0 && matrix[i][j] != 200) {
                    ans.add(matrix[i][j]);
                    matrix[i][j] = 200;
                    i--;
                    count++;
                }
                i++;
                j++;
            }
            return ans;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
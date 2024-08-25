/**
 * @BelongsProject: 剑指 Offer（第 2 版）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description:
 */
public class Solution {
    public int[] spiralOrder(int[][] matrix) {
        int m = matrix.length;
        if (m == 0) {
            return new int[]{};
        }
        int n = matrix[0].length;
        int[] ans = new int[m * n];
        // 记录还未被遍历到的区域的边界
        int up = -1;
        int down = m;
        int left = -1;
        int right = n;
        // 控制方向
        int dir = 0;
        // 当前坐标
        int i = 0;
        int j = -1;
        int cur = 0;
        // 开始遍历
        // j + 1 < right || i + 1 < down || j-1 > left || i-1 > up
        while (cur < m * n) {
            if (dir % 4 == 0) {
                // 向右走
                j++;
                while (j < right) {
                    ans[cur++] = matrix[i][j++];
                }
                up = i;
                j--;
            } else if (dir % 4 == 1) {
                // 向下走
                i++;
                while (i < down) {
                    ans[cur++] = matrix[i++][j];
                }
                right = j;
                i--;
            } else if (dir % 4 == 2) {
                // 向左走
                j--;
                while (j > left) {
                    ans[cur++] = matrix[i][j--];
                }
                down = i;
                j++;
            } else if (dir % 4 == 3) {
                // 向上走
                i--;
                while (i > up) {
                    ans[cur++] = matrix[i--][j];
                }
                left = j;
                i++;
            }
            dir++;
        }
        return ans;
    }
}

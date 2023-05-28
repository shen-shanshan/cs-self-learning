public class Solution {
    public int countNegatives(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        int cur = 0;
        int count = 0;
        while (cur < n && grid[0][cur] >= 0) {
            cur++;
        }
        count += (n - cur);
        for (int i = 1; i < m; i++) {
            // 每一层从 cur 开始倒序遍历
            while (cur - 1 >= 0 && grid[i][cur - 1] < 0) {
                cur--;
            }
            count += n - cur;
        }
        return count;
    }
}

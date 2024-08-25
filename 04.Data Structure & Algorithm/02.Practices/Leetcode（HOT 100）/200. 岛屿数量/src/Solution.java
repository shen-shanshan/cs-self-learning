// 深度优先搜索
public class Solution {
    public int numIslands(char[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int nums = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '1') {
                    infect(grid, i, j);
                    nums++;
                }
            }
        }
        return nums;
    }

    public void infect(char[][] grid, int m, int n) {
        // 上、下、左、右
        grid[m][n] = '2';
        int[][] move = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
        for (int[] mo : move) {
            int i = m + mo[0];
            int j = n + mo[1];
            if (i >= 0 && i < grid.length && j >= 0 && j < grid[0].length && grid[i][j] == '1') {
                // grid[i][j] = '2';
                infect(grid, i, j);
            }
        }
    }
}
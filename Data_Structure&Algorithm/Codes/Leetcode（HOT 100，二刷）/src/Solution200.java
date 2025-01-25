/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 200.岛屿数量
 */
public class Solution200 {

    // 上下左右
    int[] dx = {-1, 1, 0, 0};
    int[] dy = {0, 0, -1, 1};

    int ans = 0;

    public int numIslands(char[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '1') {
                    infect(grid, i, j);
                    ans++;
                }
            }
        }

        return ans;
    }

    public void infect(char[][] grid, int i, int j) {
        grid[i][j] = '0';

        // 上下左右进行搜索
        for (int k = 0; k < 4; k++) {
            int x = i + dx[k];
            int y = j + dy[k];
            if (x >= 0 && x < grid.length && y >= 0 && y < grid[0].length &&
                    grid[x][y] == '1') {
                infect(grid, x, y);
            }
        }
    }
}

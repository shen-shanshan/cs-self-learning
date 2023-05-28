import java.util.Deque;
import java.util.LinkedList;

public class Solution_695 {
    // 广度优先搜索
    public int maxAreaOfIsland(int[][] grid) {
        int max = 0;
        int m = grid.length;
        int n = grid[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] != 0) {
                    max = Math.max(max, searchIsland(grid, i, j));
                }
            }
        }
        return max;
    }

    int[] dx = {1, 0, 0, -1};
    int[] dy = {0, 1, -1, 0};

    public int searchIsland(int[][] grid, int x, int y) {
        Deque<int[]> q = new LinkedList<>();
        // 记录岛屿面积
        int area = 1;
        grid[x][y] = 0;
        q.push(new int[]{x, y});
        while (!q.isEmpty()) {
            int[] tmp = q.pop();
            int i = tmp[0];
            int j = tmp[1];
            for (int k = 0; k < 4; k++) {
                int ii = i + dx[k];
                int jj = j + dy[k];
                if (ii >= 0 && jj >= 0 && ii < grid.length && jj < grid[0].length && grid[ii][jj] != 0) {
                    q.push(new int[]{ii, jj});
                    area++;
                    // 把被遍历过的位置的值置为 0， 防止重复循环
                    grid[ii][jj] = 0;
                }
            }
        }
        return area;
    }

    public static void main(String[] args) {
        int[][] grid = {{0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0},
                {0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0},
                {0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0}};

        Solution_695 s = new Solution_695();
        int max = s.maxAreaOfIsland(grid);
        System.out.println(max);
    }
}

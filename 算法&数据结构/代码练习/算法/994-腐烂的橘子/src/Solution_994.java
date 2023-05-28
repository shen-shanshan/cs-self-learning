import java.util.LinkedList;
import java.util.Queue;

public class Solution_994 {
    static int[][] dxy = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    public int orangesRotting(int[][] grid) {
        int time = 0;
        int m = grid.length;
        int n = grid[0].length;
        Queue<int[]> q = new LinkedList<>();
        // 以腐烂的橘子作为源头，把所有腐烂的橘子入队
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 2) q.offer(new int[]{i, j});
            }
        }
        // 多源广度优先搜索
        while (!q.isEmpty()) {
            int size = q.size();
            for (int k = 0; k < size; k++) {
                int[] tmp = q.poll();
                int x = tmp[0];
                int y = tmp[1];
                for (int i = 0; i < 4; i++) {
                    int xx = x + dxy[i][0];
                    int yy = y + dxy[i][1];
                    if (xx >= 0 && xx < m && yy >= 0 && yy < n && grid[xx][yy] == 1) {
                        q.offer(new int[]{xx, yy});
                        grid[xx][yy] = 2;
                    }
                }
            }
            if (q.isEmpty()) break;
            time++;
        }
        // 判断是否还有没腐坏的橘子
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) return -1;
            }
        }
        return time;
    }

    public static void main(String[] args) {
        int[][] grid1 = {{2, 1, 1}, {1, 1, 0}, {0, 1, 1}};
        int[][] grid2 = {{2, 1, 1}, {0, 1, 1}, {1, 0, 1}};
        int[][] grid3 = {{0, 2}, {0, 0}};
        int[][] grid4 = {{0}};
        int[][] grid5 = {{0, 2, 2}};

        Solution_994 s = new Solution_994();
        int time1 = s.orangesRotting(grid1);
        int time2 = s.orangesRotting(grid2);
        int time3 = s.orangesRotting(grid3);
        int time4 = s.orangesRotting(grid4);
        int time5 = s.orangesRotting(grid5);
        System.out.println(time1);// 4
        System.out.println(time2);// -1
        System.out.println(time3);// 0
        System.out.println(time4);// 0
        System.out.println(time5);// 0
    }
}

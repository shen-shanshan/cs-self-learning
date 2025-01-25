import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 994.腐烂的橘子
 */
public class Solution994 {
    // 上下左右
    public int[] dx = {-1, 1, 0, 0};
    public int[] dy = {0, 0, -1, 1};

    public int orangesRotting(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        // 新鲜的橘子数量
        int good = 0;

        // 已经腐烂的橘子
        Queue<int[]> queue = new LinkedList<>();

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    good++;
                } else if (grid[i][j] == 2) {
                    queue.offer(new int[]{i, j});
                }
            }
        }

        // 因为 0 分钟时已经没有新鲜橘子了，所以答案就是 0
        if (good == 0) {
            return 0;
        }

        int time = 0;

        // 多源广度优先搜索
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                // 当前节点
                int[] cur = queue.poll();
                int x = cur[0];
                int y = cur[1];
                // 向四周扩散
                for (int j = 0; j < 4; j++) {
                    int newX = x + dx[j];
                    int newY = y + dy[j];
                    if (newX >= 0 && newX < m && newY >= 0 && newY < n && grid[newX][newY] == 1) {
                        good--;
                        grid[newX][newY] = 2;
                        queue.offer(new int[]{newX, newY});
                    }
                }
            }
            time++;
        }

        return good == 0 ? time - 1 : -1;
    }
}

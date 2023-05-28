import java.util.Deque;
import java.util.LinkedList;

public class Solution_733 {
    // 广度优先搜索
    /*public int[][] floodFill(int[][] image, int sr, int sc, int newColor) {
        int OldColor = image[sr][sc];
        if (OldColor == newColor) {
            return image;
        }
        int m = image.length;
        int n = image[0].length;
        // 记录坐标位置
        Deque<int[]> q = new LinkedList<>();
        int[] pos = new int[2];
        pos[0] = sr;
        pos[1] = sc;
        image[sr][sc] = newColor;

        q.push(pos);
        while (!q.isEmpty()) {
            int[] tmpos = q.pop();
            int x = tmpos[0];
            int y = tmpos[1];
            // 上
            if (x - 1 >= 0 && image[x - 1][y] == OldColor) {
                q.push(new int[]{x - 1, y, 1});
                image[x - 1][y] = newColor;
            }
            // 下
            if (x + 1 < m && image[x + 1][y] == OldColor) {
                q.push(new int[]{x + 1, y, 1});
                image[x + 1][y] = newColor;
            }
            // 左
            if (y - 1 >= 0 && image[x][y - 1] == OldColor) {
                q.push(new int[]{x, y - 1, 1});
                image[x][y - 1] = newColor;
            }
            // 右
            if (y + 1 < n && image[x][y + 1] == OldColor) {
                q.push(new int[]{x, y + 1, 1});
                image[x][y + 1] = newColor;
            }
        }
        return image;
    }*/

    // 深度优先搜索
    int[] dx = {1, 0, 0, -1};
    int[] dy = {0, 1, -1, 0};

    public int[][] floodFill(int[][] image, int sr, int sc, int newColor) {
        int currColor = image[sr][sc];
        if (currColor != newColor) {
            dfs(image, sr, sc, currColor, newColor);
        }
        return image;
    }

    public void dfs(int[][] image, int x, int y, int color, int newColor) {
        if (image[x][y] == color) {
            image[x][y] = newColor;
            for (int i = 0; i < 4; i++) {
                int mx = x + dx[i], my = y + dy[i];
                if (mx >= 0 && mx < image.length && my >= 0 && my < image[0].length) {
                    dfs(image, mx, my, color, newColor);
                }
            }
        }
    }

    public static void main(String[] args) {
        int[][] image = {{1, 1, 1}, {1, 1, 0}, {1, 0, 1}};
        int sr = 1;
        int sc = 1;
        int newColor = 2;

        /*int[][] image = {{0, 0, 0}, {0, 1, 1}};
        int sr = 1;
        int sc = 1;
        int newColor = 2;*/

        Solution_733 s = new Solution_733();
        int[][] res = s.floodFill(image, sr, sc, newColor);
        for (int[] x : res) {
            for (int y : x) {
                System.out.print(y + " ");
            }
            System.out.println();
        }
    }
}

/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 79.单词搜索
 */
public class Solution79 {
    public boolean exist(char[][] board, String word) {
        int m = board.length;
        int n = board[0].length;

        int[][] flag = new int[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                dfs(board, i, j, word, 0, flag);
            }
        }

        return ans;
    }

    boolean ans = false;

    // 上下左右
    int[] dx = {-1, 1, 0, 0};
    int[] dy = {0, 0, -1, 1};

    public void dfs(char[][] board, int i, int j,
                    String word, int index, int[][] flag) {
        if (board[i][j] != word.charAt(index)) {
            return;
        }

        // 结束条件
        if (index == word.length() - 1) {
            ans = true;
            return;
        }

        flag[i][j] = 1;

        // 上下左右
        for (int k = 0; k < 4; k++) {
            int x = i + dx[k];
            int y = j + dy[k];
            if (x >= 0 && x < board.length && y >= 0 && y < board[0].length &&
                    flag[x][y] == 0) {
                dfs(board, x, y, word, index + 1, flag);
            }
        }

        // 回溯
        flag[i][j] = 0;
    }
}

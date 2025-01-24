public class Solution {
    public boolean exist(char[][] board, String word) {
        int m = board.length;
        int n = board[0].length;
        boolean[][] flags = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (find(board, word, flags, i, j, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean find(char[][] board, String word, boolean[][] flags, int i, int j, int k) {
        char c = word.charAt(k);
        if (board[i][j] != c) {
            return false;
        } else if (k == word.length() - 1) {
            return true;
        }
        // 标记当前位置访问过了，避免重复访问
        flags[i][j] = true;
        boolean ret = false;
        // 像四周去匹配字符串剩下的部分
        int[][] dir = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int l = 0; l < 4; l++) {
            int x = dir[l][0];
            int y = dir[l][1];
            if (0 <= i + x && i + x < board.length && 0 <= j + y && j + y < board[0].length
                    && !flags[i + x][j + y]) {
                // 只要有一个为 true 就返回
                boolean ans = find(board, word, flags, i + x, j + y, k + 1);
                if (ans) {
                    ret = true;
                    break;
                }
            }
        }
        // 回溯
        flags[i][j] = false;
        return ret;
    }
}

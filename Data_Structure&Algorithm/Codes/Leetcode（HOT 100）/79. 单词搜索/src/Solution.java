import java.util.ArrayList;
import java.util.List;

public class Solution {
    public boolean exist(char[][] board, String word) {
        List<Integer> ans = new ArrayList<>();
        int m = board.length;
        int n = board[0].length;
        int[][] flags = new int[m][n];
        backtrack(board, 0, 0, word, new StringBuilder(), ans, flags);
        return !ans.isEmpty();
    }

    public void backtrack(char[][] board, int m, int n, String word, StringBuilder cur
            , List<Integer> ans, int[][] flags) {
        // 跳过当前节点
        flags[m][n] = 1;
        move(board, m, n, word, cur, ans, flags);
        // 获取当前节点
        cur.append(board[m][n]);
        move(board, m, n, word, cur, ans, flags);
        cur.deleteCharAt(cur.length() - 1);
        flags[m][n] = 0;
    }

    public void move(char[][] board, int m, int n, String word, StringBuilder cur
            , List<Integer> ans, int[][] flags) {
        // base case
        if (String.valueOf(cur).equals(word)) {
            ans.add(1);
            return;
        }
        // 向下走
        if (m < board.length - 1 && flags[m + 1][n] != 1) {
            backtrack(board, m + 1, n, word, cur, ans, flags);
        }
        // 向上走
        if (m > 0 && flags[m - 1][n] != 1) {
            backtrack(board, m - 1, n, word, cur, ans, flags);
        }
        // 向右走
        if (n < board[0].length - 1 && flags[m][n + 1] != 1) {
            backtrack(board, m, n + 1, word, cur, ans, flags);
        }
        // 向左走
        if (n > 0 && flags[m][n - 1] != 1) {
            backtrack(board, m, n - 1, word, cur, ans, flags);
        }
    }
}
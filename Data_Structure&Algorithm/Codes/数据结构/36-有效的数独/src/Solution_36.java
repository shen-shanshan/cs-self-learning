import java.util.HashMap;

public class Solution_36 {

    public boolean isValidSudoku(char[][] board) {
        // 记录每行中 1~9 的数字出现的次数
        int[][] rows = new int[9][9];
        // 记录每列中 1~9 的数字出现的次数
        int[][] columns = new int[9][9];
        // 记录每一个小九宫格中 1~9 的数字出现的次数
        int[][][] subboxes = new int[3][3][9];
        // 遍历一次整个矩阵
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                char c = board[i][j];
                if (c != '.') {
                    // 把 1~9 的字符转为 0~8 的数字
                    int index = c - '0' - 1;
                    rows[i][index]++;
                    columns[j][index]++;
                    subboxes[i / 3][j / 3][index]++;
                    // 若同一数字在每行、每列以及每个小九宫格内重复出现，则该数独无效
                    if (rows[i][index] > 1 || columns[j][index] > 1 || subboxes[i / 3][j / 3][index] > 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        char[][] board = {{'5', '3', '.', '.', '7', '.', '.', '.', '.'}
                , {'6', '.', '.', '1', '9', '5', '.', '.', '.'}
                , {'.', '9', '8', '.', '.', '.', '.', '6', '.'}
                , {'8', '.', '.', '.', '6', '.', '.', '.', '3'}
                , {'4', '.', '.', '8', '.', '3', '.', '.', '1'}
                , {'7', '.', '.', '.', '2', '.', '.', '.', '6'}
                , {'.', '6', '.', '.', '.', '.', '2', '8', '.'}
                , {'.', '.', '.', '4', '1', '9', '.', '.', '5'}
                , {'.', '.', '.', '.', '8', '.', '.', '7', '9'}}; // true
        Solution_36 s = new Solution_36();
        boolean res = s.isValidSudoku(board);
        System.out.println(res);
    }
}

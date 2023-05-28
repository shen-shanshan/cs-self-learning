import java.util.LinkedList;
import java.util.List;

public class Solution_120 {
    public int minimumTotal(List<List<Integer>> triangle) {
        int row = triangle.size();
        int column = triangle.get(row - 1).size();
        if (row == 1) return triangle.get(0).get(0);
        // 先将三角形转化为一个二维数组
        int[][] matrix = new int[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < triangle.get(i).size(); j++) {
                matrix[i][j] = triangle.get(i).get(j);
            }
        }
        int min = 10000;
        // 遍历矩阵，并动态修改矩阵的值
        for (int i = 1; i < row; i++) {
            int n = triangle.get(i).size();
            for (int j = 0; j < n; j++) {
                if (j == 0) {
                    matrix[i][j] += matrix[i - 1][j];
                } else if (j == n - 1) {
                    matrix[i][j] += matrix[i - 1][j - 1];
                } else {
                    matrix[i][j] += Math.min(matrix[i - 1][j - 1], matrix[i - 1][j]);
                }
                if (i == row - 1) {
                    min = Math.min(min, matrix[i][j]);
                }
            }
        }
        return min;
    }

    public static void main(String[] args) {
        List<List<Integer>> triangle = new LinkedList<>();
        List<Integer> row1 = new LinkedList<>();
        row1.add(2);
        List<Integer> row2 = new LinkedList<>();
        row2.add(3);
        row2.add(4);
        List<Integer> row3 = new LinkedList<>();
        row3.add(6);
        row3.add(5);
        row3.add(7);
        List<Integer> row4 = new LinkedList<>();
        row4.add(4);
        row4.add(1);
        row4.add(8);
        row4.add(3);
        triangle.add(row1);
        triangle.add(row2);
        triangle.add(row3);
        triangle.add(row4);

        Solution_120 s = new Solution_120();
        int res = s.minimumTotal(triangle);
        System.out.println(res);// 11
    }
}

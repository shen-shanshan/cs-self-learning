import java.util.HashSet;
import java.util.Set;

public class Solution_73 {

    // 暴力解法：（两次遍历）
    public void setZeroes(int[][] matrix) {
        Set<Integer> row = new HashSet<>();
        Set<Integer> column = new HashSet<>();
        // 第一次遍历，统计 0 出现的所有位置
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    row.add(i);
                    column.add(j);
                }
            }
        }
        // 第二次遍历，修改对应的元素值
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (row.contains(i) == true || column.contains(j) == true) {
                    matrix[i][j] = 0;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[][] matrix = {{0, 1, 2, 0}, {3, 4, 5, 2}, {1, 3, 1, 5}};
        Solution_73 s = new Solution_73();
        s.setZeroes(matrix);
        for (int[] x : matrix) {
            for (int y : x) {
                System.out.print(y + " ");
            }
            System.out.println();
        }
        // [[0,0,0,0],[0,4,5,0],[0,3,1,0]]
    }
}

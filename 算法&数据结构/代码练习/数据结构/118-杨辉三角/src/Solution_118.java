import java.util.ArrayList;
import java.util.List;

public class Solution_118 {

    /*public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> res = new ArrayList<>();
        int[][] dp = new int[numRows][numRows];
        // 初始化第一行
        dp[0][0] = 1;
        res.add(new ArrayList<Integer>() {{
            add(1);
        }});
        // 开始遍历
        int i = 1;
        while (i < numRows) {
            List<Integer> l = new ArrayList<>();
            for (int j = 0; j <= i; j++) {
                if (j == 0 || j == i) {
                    dp[i][j] = 1;
                    l.add(1);
                } else {
                    dp[i][j] = dp[i - 1][j - 1] + dp[i - 1][j];
                    l.add(dp[i][j]);
                }
            }
            res.add(l);
            i++;
        }
        return res;
    }*/

    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> triangle = new ArrayList<>();
        // 开始遍历
        for (int row = 0; row < numRows; row++) {
            List<Integer> numbers = new ArrayList<>();
            for (int column = 0; column <= row; column++) {
                if (column == 0 || column == row) {
                    numbers.add(1);
                } else {
                    numbers.add(triangle.get(row - 1).get(column - 1) + triangle.get(row - 1).get(column));
                }
            }

            triangle.add(numbers);
        }
        return triangle;
    }

    public static void main(String[] args) {
        Solution_118 s = new Solution_118();
        List<List<Integer>> res = s.generate(5);
        for (List<Integer> x : res) {
            for (Integer y : x) {
                System.out.print(y + " ");
            }
            System.out.println();
        }
    }
}

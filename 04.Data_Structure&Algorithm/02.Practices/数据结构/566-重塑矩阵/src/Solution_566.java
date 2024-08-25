public class Solution_566 {

    public int[][] matrixReshape(int[][] mat, int r, int c) {
        // 若输入参数不合理，则返回原矩阵
        int m = mat.length;
        int n = mat[0].length;
        if (m * n != r * c) {
            return mat;
        }

        int[][] newMat = new int[r][c];
        /*int i = 0;
        int j = 0;
        for (int[] x : mat) {
            for (int y : x) {
                if (j < c) {
                    newMat[i][j] = y;
                } else if (j == c) {
                    j = 0;
                    i++;
                    newMat[i][j] = y;
                }
                j++;
            }
        }*/

        /* 方法二：
        将二维数组 nums 映射成一个一维数组，将这个一维数组映射回 r 行 c 列的二维数组。
        */
        for (int x = 0; x < m * n; x++) {
            newMat[x / c][x % c] = mat[x / n][x % n];
        }
        return newMat;
    }

    public static void main(String[] args) {
        int[][] mat = {{1, 2}, {3, 4}};

        int r1 = 1;
        int c1 = 4;

        int r2 = 2;
        int c2 = 4;

        Solution_566 s = new Solution_566();
        int[][] res = s.matrixReshape(mat, r2, c2);
        for (int[] i : res) {
            for (int j : i) {
                System.out.print(j + " ");
            }
            System.out.println();
        }
    }
}

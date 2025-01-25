package editor.cn;

// 59.螺旋矩阵 II
class Solution59 {

    public static void main(String[] args) {
        Solution solution = new Solution59().new Solution();
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int[][] generateMatrix(int n) {
            // 数组元素默认值是 0
            int[][] ans = new int[n][n];
            // 当前坐标
            int i = 0;
            int j = 0;
            // 当前要填入的值
            int cur = 1;
            // 循环装填
            while (cur <= n * n) {
                // 向右
                while (j < n && ans[i][j] == 0) {
                    ans[i][j++] = cur++;
                }
                j--;
                i++;
                // 向下
                while (i < n && ans[i][j] == 0) {
                    ans[i++][j] = cur++;
                }
                i--;
                j--;
                // 向左
                while (j >= 0 && ans[i][j] == 0) {
                    ans[i][j--] = cur++;
                }
                j++;
                i--;
                // 向上
                while (i >= 0 && ans[i][j] == 0) {
                    ans[i--][j] = cur++;
                }
                i++;
                j++;
            }
            return ans;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
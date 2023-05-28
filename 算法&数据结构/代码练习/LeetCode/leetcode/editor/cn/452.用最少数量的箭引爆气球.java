package editor.cn;

import java.util.Arrays;
import java.util.Comparator;

// 452.用最少数量的箭引爆气球
class Solution452 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int findMinArrowShots(int[][] points) {
            Arrays.sort(points, new myComparator());

            int count = 1;
            // 每组重叠气球的最小右边界（射击点）
            int right = points[0][1];

            for (int i = 1; i < points.length; i++) {
                if (points[i][0] > right) {
                    // 下一组重叠的气球
                    count++;
                    right = points[i][1];
                } else {
                    // 更新当前重叠的气球组的最小右边界
                    right = Math.min(right, points[i][1]);
                }
            }

            return count;
        }
    }

    class myComparator implements Comparator<int[]> {
        @Override
        public int compare(int[] o1, int[] o2) {
            // 按起始位置排序
            // 最近新增了 Test Case:
            // [[-2147483646,-2147483645],[2147483646,2147483647]]
            // 因为差值过大会产生溢出
            // 不要用 a - b 来比较，要用 Integer.compare(a, b)！！!
            return Integer.compare(o1[0],o2[0]);
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
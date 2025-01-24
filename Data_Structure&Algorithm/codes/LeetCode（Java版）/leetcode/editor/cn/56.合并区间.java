package editor.cn;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

// 56.合并区间
class Solution56 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int[][] merge(int[][] intervals) {

            Arrays.sort(intervals, new myComparator());

            List<int[]> list = new LinkedList<>();

            int left = intervals[0][0];
            int right = intervals[0][1];

            for (int i = 1; i < intervals.length; i++) {
                if (intervals[i][0] <= right) {
                    // 发生重叠，更新最远右边界
                    right = Math.max(right, intervals[i][1]);
                } else {
                    // 没有重叠，记录上一段
                    list.add(new int[]{left, right});
                    // 开始新的一段
                    left = intervals[i][0];
                    right = intervals[i][1];
                }
            }
            // 记录最后一段
            list.add(new int[]{left, right});

            return list.toArray(new int[list.size()][]);
        }
    }

    class myComparator implements Comparator<int[]> {
        @Override
        public int compare(int[] o1, int[] o2) {
            // 优先按左边界从小到大排序
            if (o1[0] != o2[0]) {
                return Integer.compare(o1[0], o2[0]);
            }
            // 其次按右边界从小到大排序
            return Integer.compare(o1[1], o2[1]);
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
package editor.cn;

import java.util.Arrays;
import java.util.Comparator;

// 435.无重叠区间
class Solution435 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int eraseOverlapIntervals(int[][] intervals) {
            // 优先按右边界从小到大进行排序
            Arrays.sort(intervals, new Comparator<int[]>() {
                @Override
                public int compare(int[] o1, int[] o2) {
                    if (o1[1] != o2[1]) {
                        return Integer.compare(o1[1], o2[1]);
                    } else {
                        return Integer.compare(o1[0], o2[0]);
                    }
                }
            });

            int count = 0;

            int right = intervals[0][1];
            for (int i = 1; i < intervals.length; i++) {
                if (intervals[i][0] < right) {
                    // 发生了重叠，需要移除当前的线段
                    count++;
                } else {
                    // 开始记录下一段不重复的线段
                    right = intervals[i][1];
                }
            }

            return count;
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}
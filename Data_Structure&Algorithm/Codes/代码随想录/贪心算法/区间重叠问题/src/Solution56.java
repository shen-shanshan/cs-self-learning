import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 56.合并区间
 */
public class Solution56 {
    public int[][] merge(int[][] intervals) {
        int len = intervals.length;

        // 按起点排序
        Arrays.sort(intervals, (o1, o2) -> {
            if (o1[0] != o2[0]) {
                return Integer.compare(o1[0], o2[0]);
            } else {
                return Integer.compare(o1[1], o2[1]);
            }
        });

        List<int[]> list = new ArrayList<>();

        int left = intervals[0][0];
        int right = intervals[0][1];

        for (int i = 1; i < len; i++) {
            if (intervals[i][0] <= right) {
                left = Math.min(left, intervals[i][0]);
                right = Math.max(right, intervals[i][1]);
            } else {
                list.add(new int[]{left, right});
                left = intervals[i][0];
                right = intervals[i][1];
            }
        }
        // 记录最后一个区间组
        list.add(new int[]{left, right});

        int[][] ans = new int[list.size()][2];

        for (int i = 0; i < list.size(); i++) {
            int[] cur = list.get(i);
            ans[i][0] = cur[0];
            ans[i][1] = cur[1];
        }

        return ans;
    }
}

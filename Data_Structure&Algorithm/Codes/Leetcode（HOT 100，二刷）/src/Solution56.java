import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @BelongsProject: Leetcode 热题 HOT 100（二刷）
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 56.合并区间
 */
public class Solution56 {
    public int[][] merge(int[][] intervals) {
        List<int[]> ans = new ArrayList<>();

        // 按起点从小到大排序
        Arrays.sort(intervals, (o1, o2) -> {
            if (o1[0] != o2[0]) {
                return Integer.compare(o1[0], o2[0]);
            } else {
                return Integer.compare(o1[1], o2[1]);
            }
        });

        int left = intervals[0][0];
        int right = intervals[0][1];

        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] <= right) {
                right = Math.max(right, intervals[i][1]);
            } else {
                ans.add(new int[]{left, right});
                left = intervals[i][0];
                right = intervals[i][1];
            }
        }
        ans.add(new int[]{left, right});

        return ans.toArray(new int[0][]);
    }
}

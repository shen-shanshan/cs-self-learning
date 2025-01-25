import java.util.Arrays;

/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 435.无重叠区间
 * 总结：
 * 按右边界排序，动态更新最小右边界。
 * 按照右边界排序，就要从左向右遍历，因为右边界越小越好。
 * 只要右边界越小，留给下一个区间的空间就越大，所以从左向右遍历，优先选右边界小的。
 */
public class Solution435 {
    public int eraseOverlapIntervals(int[][] intervals) {
        int len = intervals.length;

        // 按右边界排序
        Arrays.sort(intervals, (o1, o2) -> {
            if (o1[1] != o2[1]) {
                return Integer.compare(o1[1], o2[1]);
            } else {
                return Integer.compare(o1[0], o2[0]);
            }
        });

        int minRigth = intervals[0][1];

        int count = 0;

        for (int i = 1; i < len; i++) {
            if (intervals[i][0] < minRigth) {
                // minRigth = Math.min(minRigth, intervals[i][1]);
                count++;
            } else {
                minRigth = intervals[i][1];
            }
        }

        return count;
    }
}

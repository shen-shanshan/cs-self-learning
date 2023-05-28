import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Solution {
    public int[][] merge(int[][] intervals) {
        // int[][] ans = new int[][]{};
        List<int[]> ans = new ArrayList<>();
        if (intervals.length == 0) {
            return ans.toArray(new int[0][]);
        }
        // 排序
        Arrays.sort(intervals, new MyComparator());

        // 记录当前遍历到的最远的结束位置
        int cur = -1;
        int j = 0;
        // 遍历
        for (int i = 0; i < intervals.length; i++) {
            if (intervals[i][0] > cur) {
                cur = intervals[i][1];
                ans.add(new int[]{intervals[i][0], cur});
                j++;
            } else {
                cur = Math.max(cur, intervals[i][1]);
                int[] last = ans.get(j - 1);
                int start = last[0];
                ans.remove(j - 1);
                ans.add(new int[]{start, cur});
            }
        }
        return ans.toArray(new int[0][]);
    }
}

class MyComparator implements Comparator<int[]> {

    @Override
    public int compare(int[] o1, int[] o2) {
        // 按起点排序
        return o1[0] - o2[0];
    }
}
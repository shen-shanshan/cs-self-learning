import java.util.Arrays;

/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 452.用最少数量的箭引爆气球
 * 总结：
 * 按照起始位置排序，从前向后遍历气球数组，靠左尽可能让气球重复。
 * 每次都取当前气球最右边界的坐标，可以有更大可能射爆下一个气球。
 */
public class Solution452 {
    public int findMinArrowShots(int[][] points) {
        int len = points.length;

        int count = 1;

        // 当前最小右边界
        int minRight = Integer.MAX_VALUE;

        // 按起始位置排序
        Arrays.sort(points, (o1, o2) -> {
            if (o1[0] != o2[0]) {
                return Integer.compare(o1[0], o2[0]);
            } else {
                return Integer.compare(o1[1], o2[1]);
            }
        });

        for (int i = 0; i < len; i++) {
            if (points[i][0] <= minRight) {
                minRight = Math.min(minRight, points[i][1]);
            } else {
                minRight = points[i][1];
                count++;
            }
        }

        return count;
    }
}

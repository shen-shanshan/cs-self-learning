import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @BelongsProject: 代码随想录
 * @BelongsPackage: PACKAGE_NAME
 * @Author: SSS
 * @Description: 406.根据身高重建队列
 */
public class Solution406 {
    public int[][] reconstructQueue(int[][] people) {
        int len = people.length;

        // 排序：
        // 1.hi高的在前面
        // 2.ki小的在前面
        Arrays.sort(people, (o1, o2) -> {
            if (o1[0] != o2[0]) {
                return Integer.compare(o2[0], o1[0]);
            } else {
                return Integer.compare(o1[1], o2[1]);
            }
        });

        List<int[]> list = new ArrayList<>();

        for (int i = 0; i < len; i++) {
            list.add(people[i][1], people[i]);
        }

        int[][] ans = new int[len][2];

        for (int i = 0; i < len; i++) {
            ans[i] = list.get(i);
        }

        return ans;
    }
}

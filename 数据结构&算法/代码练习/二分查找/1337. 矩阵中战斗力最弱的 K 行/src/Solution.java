import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Solution {
    public int[] kWeakestRows(int[][] mat, int k) {
        int m = mat.length;
        int n = mat[0].length;

        // 统计每一行军人的数量
        RowInfo[] rows = new RowInfo[m];
        for (int i = 0; i < m; i++) {
            rows[i] = new RowInfo(i, search(mat, i));
        }

        Arrays.sort(rows, new Comparator<RowInfo>() {
            @Override
            public int compare(RowInfo o1, RowInfo o2) {
                if (o1.soldiers != o2.soldiers) {
                    return o1.soldiers - o2.soldiers;
                }
                return o1.index - o2.index;
            }
        });

        int[] ret = new int[k];
        for (int i = 0; i < k; i++) {
            ret[i] = rows[i].index;
        }

        return ret;
    }

    public int search(int[][] mat, int m) {
        int left = 0;
        int right = mat[m].length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (mat[m][mid] == 1) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return right + 1;
    }
}

class RowInfo {
    int index;
    int soldiers;

    public RowInfo(int index, int soldiers) {
        this.index = index;
        this.soldiers = soldiers;
    }
}